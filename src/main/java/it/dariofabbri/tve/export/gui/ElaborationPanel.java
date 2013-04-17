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
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;

public class ElaborationPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable table;
	private JTextField creationDate;
	private JButton exportButton;

	
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
		
		JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel labelDate = ControlFactory.makeFormLabel("Data di esportazione");
		datePanel.add(labelDate);
		creationDate = ControlFactory.makeTextField();
		creationDate.setColumns(20);
		creationDate.setText(String.format("%1$td/%1$tm/%1$tY", new Date()));
		creationDate.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				checkAndEnable();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				checkAndEnable();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				checkAndEnable();
			}
			
			private void checkAndEnable() {
				exportButton.setEnabled(!StringUtils.isEmpty(creationDate.getText()));
			}
		});
		datePanel.add(creationDate);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.add(scrollPane);
		centerPanel.add(datePanel);
		
        this.add(centerPanel, BorderLayout.CENTER);
		
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
				loadInvoices();
			}
		});
		buttonPanel.add(loadButton);
		
		exportButton = ControlFactory.makeFormButton("Esporta XML");
		exportButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				exportSelectedInvoices();
			}
		});
		buttonPanel.add(exportButton);
	}


	private void loadInvoices() {

		// Open file chooser.
		//
		JFileChooser fileChooser = new JFileChooser();
		if(fileChooser.showOpenDialog(ElaborationPanel.this) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = fileChooser.getSelectedFile();
		
		// Set wait cursor.
		//
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		// Check if the selected file exists.
		//
		if(!file.exists()) {
			this.setCursor(Cursor.getDefaultCursor());
			JOptionPane.showMessageDialog(
				null,
			    "Il file selezionato non esiste.",
			    "Errore",
			    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// Check if the selected file is really a file.
		//
		if(!file.isFile()) {
			this.setCursor(Cursor.getDefaultCursor());
			JOptionPane.showMessageDialog(
					null,
				    "E'obbligatorio selezionare un file.",
				    "Errore",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// Check if the selected file ends with PDF extension.
		//
		if(!file.getName().toLowerCase().endsWith(".pdf")) {
			this.setCursor(Cursor.getDefaultCursor());
			JOptionPane.showMessageDialog(
					null,
				    "E' obbligatorio selezionare un file di tipo PDF.",
				    "Errore",
				    JOptionPane.ERROR_MESSAGE);			
			return;
		}
		
		// Open the PDF document.
		//
		PDDocument document = null;
		try {
			document = PDDocument.load(file);
		} catch (IOException e) {
			e.printStackTrace();
			this.setCursor(Cursor.getDefaultCursor());
			JOptionPane.showMessageDialog(
					null,
				    "Si è verificato un errore in fase di caricamento del PDF selezionato.",
				    "Errore",
				    JOptionPane.ERROR_MESSAGE);			
			return;
		}

		// Analyze the PDF document.
		//
		Analyzer analyzer = new Analyzer();
		AnalysisResult result = analyzer.analyze(document);
		if(result == null) {
			this.setCursor(Cursor.getDefaultCursor());
			JOptionPane.showMessageDialog(
					null,
				    "Si è verificato un errore in fase di estrazione delle fatture dal PDF selezionato.",
				    "Errore",
				    JOptionPane.ERROR_MESSAGE);						
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
	
	
	private void exportSelectedInvoices() {

		// Get creation date.
		//
		String s = creationDate.getText();
		if(StringUtils.isEmpty(s)) {
			JOptionPane.showMessageDialog(
					null,
				    "E' necessario specificare la data di esportazione.",
				    "Errore",
				    JOptionPane.ERROR_MESSAGE);						
			return;
		}
		Date date = null;
		try {
			date = DateUtils.parseDateStrictly(s, "dd/MM/yyyy", "dd/MM/yy");
		} catch(Exception e) {
			JOptionPane.showMessageDialog(
					null,
				    "La data specificata non è valida. Usare il formato dd/mm/aaaa",
				    "Errore",
				    JOptionPane.ERROR_MESSAGE);						
			return;
		}

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
			return;
		}

		// Open file chooser.
		//
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setDialogTitle("Selezione cartella di esportazione");
		if(fileChooser.showDialog(ElaborationPanel.this, "Seleziona cartella") != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = fileChooser.getSelectedFile();

		
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		
		try {
		
			// Marshal documents to XML.
			//
			Marshaller marshaller = new Marshaller();
			marshaller.generateXml(documenti, date, file);
			marshaller.generateZip(documenti, date, file);
			
		} catch(Exception e) {
			
			e.printStackTrace();
			this.setCursor(Cursor.getDefaultCursor());
			JOptionPane.showMessageDialog(
					null,
				    "Si è verificato un errore in fase di generazione del file XML.",
				    "Errore",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}

		this.setCursor(Cursor.getDefaultCursor());
		JOptionPane.showMessageDialog(
				null,
			    "L'esportazione si è conclusa con successo.",
			    "Esportazione",
			    JOptionPane.INFORMATION_MESSAGE);	
	}
}
