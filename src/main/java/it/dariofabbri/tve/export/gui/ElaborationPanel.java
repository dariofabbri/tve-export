package it.dariofabbri.tve.export.gui;

import it.dariofabbri.tve.export.model.Documento;
import it.dariofabbri.tve.export.service.analyzer.AnalysisResult;
import it.dariofabbri.tve.export.service.analyzer.Analyzer;
import it.dariofabbri.tve.export.service.marshaller.Marshaller;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;

public class ElaborationPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable table;
	
	public ElaborationPanel() {
		
		this.setLayout(new BorderLayout(10, 10));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JLabel labelHeader = ControlFactory.makeHeaderLabel("Elaborazione PDF");
		this.add(labelHeader, BorderLayout.NORTH);		
		
		table = new JTable(new InvoicesTableModel());
        table.setPreferredScrollableViewportSize(new Dimension(800, 300));
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(false);
		JScrollPane scrollPane = new JScrollPane(table);
		
		
        this.add(scrollPane, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.add(buttonPanel, BorderLayout.SOUTH);		
		
		JButton cancelButton = ControlFactory.makeFormButton("Annulla");
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TheApplication frame = (TheApplication)SwingUtilities.getWindowAncestor(ElaborationPanel.this);
				frame.setMainPanel(new HomePanel());				
			}
		});
		buttonPanel.add(cancelButton);
		
		JButton loadButton = ControlFactory.makeFormButton("Carica PDF");
		loadButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileChooser = new JFileChooser();
				if(fileChooser.showOpenDialog(ElaborationPanel.this) == JFileChooser.APPROVE_OPTION) {
					loadInvoices(fileChooser.getSelectedFile());
				}
			}
		});
		buttonPanel.add(loadButton);
		
		JButton exportButton = ControlFactory.makeFormButton("Esporta XML");
		exportButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileChooser = new JFileChooser();
				if(fileChooser.showOpenDialog(ElaborationPanel.this) == JFileChooser.APPROVE_OPTION) {
					loadInvoices(fileChooser.getSelectedFile());
				}
			}
		});
		buttonPanel.add(exportButton);
	}


	private void loadInvoices(File file) {
		
		// Set wait cursor.
		//
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		// Check if the selected file exists.
		//
		if(!file.exists()) {
			JOptionPane.showMessageDialog(
				null,
			    "Il file selezionato non esiste.",
			    "Errore",
			    JOptionPane.ERROR_MESSAGE);
			this.setCursor(Cursor.getDefaultCursor());
			return;
		}
		
		// Check if the selected file is really a file.
		//
		if(!file.isFile()) {
			JOptionPane.showMessageDialog(
					null,
				    "E'obbligatorio selezionare un file.",
				    "Errore",
				    JOptionPane.ERROR_MESSAGE);
			this.setCursor(Cursor.getDefaultCursor());
			return;
		}
		
		// Check if the selected file ends with PDF extension.
		//
		if(!file.getName().toLowerCase().endsWith(".pdf")) {
			JOptionPane.showMessageDialog(
					null,
				    "E' obbligatorio selezionare un file di tipo PDF.",
				    "Errore",
				    JOptionPane.ERROR_MESSAGE);			
			this.setCursor(Cursor.getDefaultCursor());
			return;
		}
		
		// Open the PDF document.
		//
		PDDocument document = null;
		try {
			document = PDDocument.load(file);
		} catch (IOException e) {
			e.printStackTrace();
			
			JOptionPane.showMessageDialog(
					null,
				    "Si è verificato un errore in fase di caricamento del PDF selezionato.",
				    "Errore",
				    JOptionPane.ERROR_MESSAGE);			
			this.setCursor(Cursor.getDefaultCursor());
			return;
		}

		// Analyze the PDF document.
		//
		Analyzer analyzer = new Analyzer();
		AnalysisResult result = analyzer.analyze(document);
		if(result == null) {
			JOptionPane.showMessageDialog(
					null,
				    "Si è verificato un errore in fase di estrazione delle fatture dal PDF selezionato.",
				    "Errore",
				    JOptionPane.ERROR_MESSAGE);						
			this.setCursor(Cursor.getDefaultCursor());
			return;
		}
		
		// If some non blocking errors have been detected in the PDF file,
		// show them.
		//
		List<Pair<Integer, String>> messaggi = result.getMessaggi();
		if(messaggi != null && messaggi.size() > 0) {
			
			String message = buildErrorMessage(messaggi);
			JOptionPane.showMessageDialog(
					null,
				    message,
				    "Attenzione",
				    JOptionPane.WARNING_MESSAGE);						
		}

		// Set read documents in the table data model.
		//
		InvoicesTableModel model = (InvoicesTableModel)table.getModel();
		model.setData(result.getDocumenti());
		this.setCursor(Cursor.getDefaultCursor());
	}


	private String buildErrorMessage(List<Pair<Integer, String>> messaggi) {
		
		StringBuilder sb = new StringBuilder();
		sb
			.append("Durante l'analisi alcune fatture non sono state estratte a causa dei seguenti errori:")
			.append(System.lineSeparator())
			.append(System.lineSeparator());
		
		for(Pair<Integer, String> pair : messaggi) {
			sb
				.append(String.format("Pagina %d: %s", pair.getLeft(), pair.getRight()))
				.append(System.lineSeparator());
		}
		
		return sb.toString();
	}
	
	
	private void exportSelectedInvoices(File file) {

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Get list of selected documents.
		//
		InvoicesTableModel model = (InvoicesTableModel)table.getModel();
		List<Documento> documenti = model.getSelectedDocuments();

		// At least on document must have been selected.
		//
		if(documenti == null || documenti.size() == 0) {
			JOptionPane.showMessageDialog(
					null,
				    "E' necessario selezionare almeno una fattura.",
				    "Errore",
				    JOptionPane.ERROR_MESSAGE);						
			this.setCursor(Cursor.getDefaultCursor());
			return;
		}
		
		// Get selected creation date.
		//
		
		try {
			// Open file output stream.
			//
			FileOutputStream fos = new FileOutputStream(file);
			
			Marshaller marshaller = new Marshaller();
			//marshaller.generateXml(documenti, creationDate, fos);
			
			// Close stream.
			//
			fos.flush();
			fos.close();
			
		} catch(Exception e) {
			
			e.printStackTrace();
			JOptionPane.showMessageDialog(
					null,
				    "Si è verificato un errore in fase di generazione del file XML.",
				    "Errore",
				    JOptionPane.ERROR_MESSAGE);
			
			this.setCursor(Cursor.getDefaultCursor());
			return;
		}

		this.setCursor(Cursor.getDefaultCursor());
	}
}
