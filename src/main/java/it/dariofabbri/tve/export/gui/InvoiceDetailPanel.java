package it.dariofabbri.tve.export.gui;

import it.dariofabbri.tve.export.model.Documento;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class InvoiceDetailPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable table;
	private JTextField total;
	private JTextField taxTotal;

	private InvoiceDetailTableModel model;
	
	
	public InvoiceDetailPanel(Documento documento) {
		
		this.setLayout(new BorderLayout(10, 10));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JLabel labelHeader = ControlFactory.makeHeaderLabel("Modifica fattura");
		this.add(labelHeader, BorderLayout.NORTH);		
		
		model = new InvoiceDetailTableModel();
		model.setDocumento(documento);
		
		table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(800, 300));
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(false);
		JScrollPane scrollPane = new JScrollPane(table);
		
		JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel labelTotal = ControlFactory.makeFormLabel("Imponibile totale:");
		totalPanel.add(labelTotal);
		total = ControlFactory.makeTextField();
		total.setColumns(10);
		total.setEditable(false);
		totalPanel.add(total);
		JLabel labelTaxTotal = ControlFactory.makeFormLabel("Totale tasse:");
		totalPanel.add(labelTaxTotal);
		taxTotal = ControlFactory.makeTextField();
		taxTotal.setColumns(10);
		taxTotal.setEditable(false);
		totalPanel.add(taxTotal);
		updateTotals();
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.add(scrollPane);
		centerPanel.add(totalPanel);
		
        this.add(centerPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.add(buttonPanel, BorderLayout.SOUTH);		
		
		JButton cancelButton = ControlFactory.makeFormButton("Annulla");
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TheApplication frame = (TheApplication)SwingUtilities.getWindowAncestor(InvoiceDetailPanel.this);
				frame.setMainPanel(new ElaborationPanel());				
			}
		});
		buttonPanel.add(cancelButton);
		
		JButton applyButton = ControlFactory.makeFormButton("Salva modifiche");
		applyButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TheApplication frame = (TheApplication)SwingUtilities.getWindowAncestor(InvoiceDetailPanel.this);
				frame.setMainPanel(new ElaborationPanel());				
			}
		});
		buttonPanel.add(applyButton);
	}


	private void updateTotals() {
		
	}
}
