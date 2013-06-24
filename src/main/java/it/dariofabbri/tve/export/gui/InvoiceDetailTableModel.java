package it.dariofabbri.tve.export.gui;

import it.dariofabbri.tve.export.model.Documento;
import it.dariofabbri.tve.export.model.Prodotto;

import javax.swing.table.AbstractTableModel;

public class InvoiceDetailTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private Documento documento;
	private Object[][] data;
	private String[] columns = {
				"Descrizione", 
				"Quantità", 
				"Unità", 
				"Prezzo unitario", 
				"Imponibile"
	};
	
	public InvoiceDetailTableModel() {
		
		data = new Object[0][columns.length];
	}
	
	@Override
	public int getRowCount() {
		
		return data.length;
	}

	@Override
	public int getColumnCount() {

		return columns.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		return data[rowIndex][columnIndex];	
	}
	
	@Override
	public String getColumnName(int column) {
		
		return columns[column];
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		
		return columnIndex == 1 || columnIndex == 3;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
		data[rowIndex][columnIndex] = aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return getValueAt(0, columnIndex).getClass();	
	}
	
	public void setDocumento(Documento documento) {
	
		// Save the original value for later use.
		//
		this.documento = documento;
		
		// Update data model.
		//
		updateModel();
	}
	public Documento getDocumento() {
		return documento;
	}

	
	public void updateModel() {
		
		int size = 0;
		if(documento.getProdotti() != null) {
			size = documento.getProdotti().size();
		}
		
		// Allocate space.
		//
		data = new Object[size][columns.length];
		
		// Copy relevant fields in local data model.
		//
		for(int i = 0; i < size; ++i) {
			
			Prodotto prodotto = documento.getProdotti().get(i);
			
			data[i][0] = prodotto.getDescrizione();
			data[i][1] = prodotto.getQuantita();
			data[i][2] = prodotto.getUnitaMisura();
			data[i][3] = prodotto.getPrezzo().getPrezzoCessione();
			data[i][4] = prodotto.getImporto().getImponibile();
		}
		
		// Fire refresh.
		//
		this.fireTableDataChanged();
	}
}
