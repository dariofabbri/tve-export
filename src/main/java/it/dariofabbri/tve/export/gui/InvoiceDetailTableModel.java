package it.dariofabbri.tve.export.gui;

import it.dariofabbri.tve.export.model.Documento;
import it.dariofabbri.tve.export.model.Importo;
import it.dariofabbri.tve.export.model.Prodotto;
import it.dariofabbri.tve.export.util.DecimalUtils;
import it.dariofabbri.tve.export.util.ValidationUtils;

import java.math.BigDecimal;

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
	
	private String imponibileTotale;
	private String importoTotaleTassa;
	private String importoTotale;
	

	public String getImponibileTotale() {
		return imponibileTotale;
	}

	public void setImponibileTotale(String imponibileTotale) {
		this.imponibileTotale = imponibileTotale;
	}

	public String getImportoTotaleTassa() {
		return importoTotaleTassa;
	}

	public void setImportoTotaleTassa(String importoTotaleTassa) {
		this.importoTotaleTassa = importoTotaleTassa;
	}

	public String getImportoTotale() {
		return importoTotale;
	}

	public void setImportoTotale(String importoTotale) {
		this.importoTotale = importoTotale;
	}

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
		
		// Apply validation. We accept only valid floating point values.
		//
		if(columnIndex == 1) {
			if(!ValidationUtils.isValidQuantity((String)aValue)) {
				return;
			}
		}
		if(columnIndex == 3) {
			if(!ValidationUtils.isValidPrice((String)aValue)) {
				return;
			}
		}
		
		// Set modified value.
		//
		data[rowIndex][columnIndex] = aValue;
		
		// Recalculate total for selected row.
		//
		BigDecimal quantity = DecimalUtils.makeBigDecimalFromString((String)data[rowIndex][1]);
		BigDecimal price = DecimalUtils.makeBigDecimalFromString((String)data[rowIndex][3]);
		BigDecimal total = price.multiply(quantity);
		data[rowIndex][4] = DecimalUtils.makeString(total, 2);
		
		// Fire update event.
		//
        fireTableCellUpdated(rowIndex, columnIndex);
        fireTableCellUpdated(rowIndex, 4);
        
        // Update totals.
        //
        updateTotals();
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
	
	public void saveChanges() {
		
		int size = 0;
		if(documento.getProdotti() != null) {
			size = documento.getProdotti().size();
		}

		// Copy relevant fields in source document.
		//
		for(int i = 0; i < size; ++i) {
			
			Prodotto prodotto = documento.getProdotti().get(i);
			
			prodotto.setQuantita((String)data[i][1]);
			prodotto.getPrezzo().setPrezzoCessione((String)data[i][3]);
			prodotto.getImporto().setImponibile((String)data[i][4]);
		}
		
		
		// Set document totals.
		//
		Importo importo = documento.getImporto();
		importo.setImponibileTotale(imponibileTotale);
		importo.setImportoTotaleTassa(importoTotaleTassa);
		importo.setImportoTotale(importoTotale);
		importo.setImportoTotaleEuro(importoTotale);
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
		
		// Update totals.
		//
		updateTotals();
		
		// Fire refresh.
		//
		this.fireTableDataChanged();
	}

	private void updateTotals() {
		
		// Accumulate total.
		//
		BigDecimal total = new BigDecimal(0);
		for(int i = 0; i < data.length; ++i) {

			total = total.add(
					DecimalUtils.makeBigDecimalFromString((String)data[i][4]));
		}
		
		// Retrieve tax percentage.
		//
		BigDecimal percentage = DecimalUtils.makeBigDecimalFromString(documento.getTassa().getPercentualeIva());
		percentage = percentage.divide(new BigDecimal(100));
		
		// Calculate total tax.
		//
		BigDecimal taxTotal = total.multiply(percentage);
		
		// Calculate grand total.
		//
		BigDecimal grandTotal = total.add(taxTotal);
		
		// Format strings.
		//
		imponibileTotale = DecimalUtils.makeString(total, 2);
		importoTotaleTassa = DecimalUtils.makeString(taxTotal, 2);
		importoTotale = DecimalUtils.makeString(grandTotal, 2);
		
		fireTableDataChanged();
	}
}
