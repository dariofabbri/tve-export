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
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class InvoiceDetailPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable table;
	private JTextField total;
	private JTextField taxTotal;
	private JTextField grandTotal;
	
	private JButton removeButton;

	private InvoiceDetailTableModel model;
	
	
	public InvoiceDetailPanel(Documento documento) {
		
		this.setLayout(new BorderLayout(10, 10));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JLabel labelHeader = ControlFactory.makeHeaderLabel("Modifica fattura");
		this.add(labelHeader, BorderLayout.NORTH);		
		
		model = new InvoiceDetailTableModel();
		model.setDocumento(documento);
		model.addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				updateTotals();
			}
		});
		
		table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(800, 300));
        table.setFillsViewportHeight(true);
        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(5));
        table.setRowSelectionAllowed(true);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting()) {
					return;
				}
				removeButton.setEnabled(table.getSelectedRow() >= 0);
			}
		});
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
		
		JLabel labelGrandTotal = ControlFactory.makeFormLabel("Importo totale:");
		totalPanel.add(labelGrandTotal);
		grandTotal = ControlFactory.makeTextField();
		grandTotal.setColumns(10);
		grandTotal.setEditable(false);
		totalPanel.add(grandTotal);
		
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
		
		JButton addButton = ControlFactory.makeFormButton("Aggiungi riga");
		addButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				model.addEmptyRow();
			}
		});
		buttonPanel.add(addButton);
		
		removeButton = ControlFactory.makeFormButton("Rimuovi riga");
		removeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int rowIndex = table.getSelectedRow();
				if(rowIndex >= 0) {
					model.deleteRow(rowIndex);
				}
			}
		});
		removeButton.setEnabled(false);
		buttonPanel.add(removeButton);
		
		JButton applyButton = ControlFactory.makeFormButton("Salva modifiche");
		applyButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				model.saveChanges();
				
				TheApplication frame = (TheApplication)SwingUtilities.getWindowAncestor(InvoiceDetailPanel.this);
				frame.setMainPanel(new ElaborationPanel());				
			}
		});
		buttonPanel.add(applyButton);
	}


	private void updateTotals() {
		
		total.setText(model.getImponibileTotale());
		taxTotal.setText(model.getImportoTotaleTassa());
		grandTotal.setText(model.getImportoTotale());
	}
}
