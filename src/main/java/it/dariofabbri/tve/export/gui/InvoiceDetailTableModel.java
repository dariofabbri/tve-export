package it.dariofabbri.tve.export.gui;

import it.dariofabbri.tve.export.model.Documento;
import it.dariofabbri.tve.export.model.Importo;
import it.dariofabbri.tve.export.model.ImportoProdotto;
import it.dariofabbri.tve.export.model.Prezzo;
import it.dariofabbri.tve.export.model.Prodotto;
import it.dariofabbri.tve.export.model.Tassa;
import it.dariofabbri.tve.export.service.analyzer.Analyzer;
import it.dariofabbri.tve.export.util.DecimalUtils;
import it.dariofabbri.tve.export.util.ValidationUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.lang3.StringUtils;

public class InvoiceDetailTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private Documento documento;
	private Object[][] data;
	private String[] columns = {
				"Descrizione", 
				"Quantità", 
				"Unità", 
				"Prezzo unitario", 
				"Imponibile",
				"Id"
	};
	
	private String imponibileTotale;
	private String importoTotaleTassa;
	private String importoTotale;
	
	private Set<Integer> deleted = new HashSet<Integer>();
	

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
		
		return columnIndex == 0 || columnIndex == 1 || columnIndex == 3;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
		// Validate description field.
		//
		if(columnIndex == 0) {
			
			String s = "- " + (String)aValue;
			
			// Try to extract product code.
			//
			String codiceProdotto = Analyzer.extractCodiceProdotto((String)s);
			if(StringUtils.isEmpty(codiceProdotto)) {
				JOptionPane.showMessageDialog(
						null, 
						"Impossibile estrarre il codice prodotto dalla descrizione immessa.", 
						"Errore", 
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Try to extract unit of measurement.
			//
			String unitaMisura = Analyzer.extractUnitaMisuraProdotto(codiceProdotto);
			if(StringUtils.isEmpty(unitaMisura)) {
				JOptionPane.showMessageDialog(
						null, 
						"Impossibile estrarre l'unità di misura dalla descrizione immessa.", 
						"Errore", 
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			data[rowIndex][2] = unitaMisura;
		}
		
		// Validate quantity field.
		//
		if(columnIndex == 1) {
			if(!ValidationUtils.isValidQuantity((String)aValue)) {
				JOptionPane.showMessageDialog(
						null, 
						"Inserire una quantità valida.", 
						"Errore", 
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		// Validate price field.
		//
		if(columnIndex == 3) {
			if(!ValidationUtils.isValidPrice((String)aValue)) {
				JOptionPane.showMessageDialog(
						null, 
						"Inserire un prezzo valido.", 
						"Errore", 
						JOptionPane.ERROR_MESSAGE);
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
		data[rowIndex][4] = DecimalUtils.makeString(total);
		
		// Fire update event.
		//
        fireTableCellUpdated(rowIndex, columnIndex);
        fireTableCellUpdated(rowIndex, 2);
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

		// Copy relevant fields in source document.
		//
		for(int i = 0; i < data.length; ++i) {

			Integer prdIndex = (Integer)data[i][5];
			
			if(prdIndex == null) {

				// Prepare fields.
				//
				String descrizione = (String)data[i][0];
				String quantita = (String)data[i][1];
				String prezzoCessione = (String)data[i][3];
				String imponibile = (String)data[i][4];
				
				Prezzo prezzo = new Prezzo();
				prezzo.setPrezzoCessione(prezzoCessione);
				
				ImportoProdotto importo = new ImportoProdotto();
				importo.setImponibile(imponibile);
				
				String codiceProdotto = Analyzer.extractCodiceProdotto("- " + descrizione);
				String unitaMisura = Analyzer.extractUnitaMisuraProdotto(codiceProdotto);
				String codificaProdotto = "CODICE_FORNITORE";
				
				// Add new product.
				//
				Prodotto prodotto = new Prodotto();
				prodotto.setCodiceProdotto(codiceProdotto);
				prodotto.setCodificaProdotto(codificaProdotto);
				prodotto.setDescrizione(descrizione);
				prodotto.setQuantita(quantita);
				prodotto.setUnitaMisura(unitaMisura);
				prodotto.setPrezzo(prezzo);
				prodotto.setImporto(importo);
				
				documento.getProdotti().add(prodotto);
				prodotto.setProgressivo(documento.getProdotti().indexOf(prodotto));
			}
			else {
				
				// Update existing one.
				//
				Prodotto prodotto = documento.getProdotti().get(prdIndex);
				prodotto.setDescrizione((String)data[i][0]);
				prodotto.setQuantita((String)data[i][1]);
				prodotto.getPrezzo().setPrezzoCessione((String)data[i][3]);
				prodotto.getImporto().setImponibile((String)data[i][4]);
			}
		}
		
		// Remove deleted rows.
		//
		for(Integer idx : deleted) {
			
			if(idx == null) {
				continue;
			}
			
			documento.getProdotti().remove((int)idx);
		}

		
		// Set document totals.
		//
		Importo importo = documento.getImporto();
		importo.setImponibileTotale(imponibileTotale);
		importo.setImportoTotaleTassa(importoTotaleTassa);
		importo.setImportoTotale(importoTotale);
		importo.setImportoTotaleEuro(importoTotale);
		
		Tassa tassa = documento.getTassa();
		tassa.setImponibile(imponibileTotale);
		tassa.setImportoTassa(importoTotaleTassa);
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
			data[i][5] = i;
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
		BigDecimal percentage = DecimalUtils.makeBigDecimalFromString(
				documento.getTassa().getPercentualeIva());
		percentage = percentage.divide(new BigDecimal(100));
		
		// Calculate total tax.
		//
		BigDecimal taxTotal = total.multiply(percentage);
		
		// Calculate grand total.
		//
		BigDecimal grandTotal = total.add(taxTotal);
		
		// Format strings.
		//
		imponibileTotale = DecimalUtils.makeString(total);
		importoTotaleTassa = DecimalUtils.makeString(taxTotal);
		importoTotale = DecimalUtils.makeString(grandTotal);
		
		fireTableDataChanged();
	}
	
	
	public void addEmptyRow() {
		
		Object[][] newData = new Object[data.length + 1][columns.length];
		
		for(int i = 0; i < data.length; ++i) {
			for(int j = 0; j < columns.length; ++j) {
				newData[i][j] = data[i][j];
			}
		}
		
		newData[data.length][0] = "";
		newData[data.length][1] = "0";
		newData[data.length][2] = "";
		newData[data.length][3] = "0";
		newData[data.length][4] = "0";
		newData[data.length][5] = null;

		data = newData;
		
		updateTotals();
	}
	
	
	public void deleteRow(int rowIndex) {
		
		Object[][] newData = new Object[data.length - 1][columns.length];
		
		for(int i = 0; i < rowIndex; ++i) {
			for(int j = 0; j < columns.length; ++j) {
				newData[i][j] = data[i][j];
			}
		}
		
		for(int i = rowIndex + 1; i < data.length; ++i) {
			for(int j = 0; j < columns.length; ++j) {
				newData[i - 1][j] = data[i][j];
			}
		}
		
		Integer prdIndex = (Integer)data[rowIndex][5];
		if(prdIndex != null) {
			deleted.add(prdIndex);
		}
		
		data = newData;
		
		updateTotals();
	}
}
