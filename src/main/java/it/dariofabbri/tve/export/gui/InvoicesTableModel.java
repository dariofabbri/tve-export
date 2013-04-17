package it.dariofabbri.tve.export.gui;

import it.dariofabbri.tve.export.model.Documento;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class InvoicesTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private Object[][] data;
	private String[] columns = {"", "Numero fattura", "Data fattura", "Importo fattura", "Righe di dettaglio"};
	
	public InvoicesTableModel() {
		
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
		
		return columnIndex == 0;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return getValueAt(0, columnIndex).getClass();	
	}
	
	public void setData(List<Documento> documenti) {
	
		// Allocate space.
		//
		data = new Object[documenti.size()][columns.length];
		
		for(int i = 0; i < documenti.size(); ++i) {
			
			Documento documento = documenti.get(i);
			
			data[i][0] = new Boolean(true);
			data[i][1] = documento.getNumeroDocumento();
			data[i][2] = documento.getDataDocumento();
			data[i][3] = documento.getImporto().getImportoTotale();
			data[i][4] = documento.getProdotti().size();
		}
	}
}
