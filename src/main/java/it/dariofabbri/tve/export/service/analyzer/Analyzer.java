package it.dariofabbri.tve.export.service.analyzer;

import it.dariofabbri.tve.export.model.Cliente;
import it.dariofabbri.tve.export.model.CondizioniPagamento;
import it.dariofabbri.tve.export.model.DatiFiscali;
import it.dariofabbri.tve.export.model.Documento;
import it.dariofabbri.tve.export.model.Fornitore;
import it.dariofabbri.tve.export.model.Importo;
import it.dariofabbri.tve.export.model.ImportoProdotto;
import it.dariofabbri.tve.export.model.Prezzo;
import it.dariofabbri.tve.export.model.Prodotto;
import it.dariofabbri.tve.export.model.RiferimentiBancari;
import it.dariofabbri.tve.export.model.Tassa;
import it.dariofabbri.tve.export.model.TestoLibero;
import it.dariofabbri.tve.export.service.extractor.Extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class Analyzer {

	private Extractor extractor;
	
	public Analyzer() {

		extractor = new Extractor();
	}
	

	public int getPageCount(PDDocument document) {
		
		// Passed document cannot be null.
		//
		if(document == null) {
			throw new IllegalArgumentException("Passed document cannot be null.");
		}
		
		return document.getNumberOfPages();
	}

	
	@SuppressWarnings("unchecked")
	public AnalysisResult analyze(PDDocument document) {
		
		// Passed document cannot be null.
		//
		if(document == null) {
			throw new IllegalArgumentException("Passed document cannot be null.");
		}

		// Build result object.
		//
		AnalysisResult result = new AnalysisResult();
		
		// Iterate on document pages.
		//
        List<PDPage> allPages = document.getDocumentCatalog().getAllPages();
        for(int i = 0; i < allPages.size(); ++i) {

        	PDPage page = allPages.get(i);
        	
        	// Check if the current page looks like an invoice of the expected type.
        	//
        	String s = decideInvoicePage(page);
        	if(s != null) {
        		result.addMessaggio(i, s);
        		continue;
        	}
        	
        	// Create a document object corresponding to page currently under analysis.
        	//
        	Documento documento = new Documento();
    		documento.setTipoDocumento("FATTURA");
    		documento.setProduzione("ORIGINALE");
    		documento.setValuta("EUR");
    		
    		// Extract numero fattura.
    		//
    		s = extractNumeroFattura(page);
    		if(s == null) {
    			result.addMessaggio(i, "Impossibile estrarre il numero della fattura.");
    			continue;
    		}
    		documento.setNumeroDocumento(s);
    		
    		// Extract data fattura.
    		//
    		s = extractDataFattura(page);
    		if(s == null) {
    			result.addMessaggio(i, "Impossibile estrarre la data della fattura.");
    			continue;
    		}
    		documento.setDataDocumento(s);
    		
    		// Process testo libero.
    		//
    		String[] ss = extractTestoLibero(page);
    		if(ss == null || ss.length == 0) {
    			result.addMessaggio(i, "Impossibile estrarre la testata del dettaglio della fattura.");
    			continue;
    		}
    		if(ss.length != 2) {
    			result.addMessaggio(i, "La testata del dettaglio della fattura ha una struttura inattesa (previste due righe).");
    			continue;
    		}
    		TestoLibero testoLibero = new TestoLibero();
    		testoLibero.setProgressivo(1);
    		testoLibero.setTesto1(ss[0]);
    		testoLibero.setTesto2(ss[1]);
    		documento.setTestoLibero(testoLibero);

    		// Process fornitore (only the part extracted from document,
    		// the configuration based part needs to be injected later).
    		//
    		s = extractIban(page);
    		if(s == null) {
    			result.addMessaggio(i, "Impossibile estrarre il campo IBAN dalla fattura.");
    			continue;
    		}    		
    		Fornitore fornitore = new Fornitore();
    		RiferimentiBancari riferimentiBancari = new RiferimentiBancari();
    		riferimentiBancari.setIban(s);
    		fornitore.setRiferimentiBancari(riferimentiBancari);
    		documento.setFornitore(fornitore);

    		// Process cliente.
    		//
    		Cliente cliente = new Cliente();
    		s = extractDescrizioneCliente(page);
    		if(s == null) {
    			result.addMessaggio(i, "Impossibile estrarre il nominativo del cliente dalla fattura.");
    			continue;
    		}    		
    		cliente.setDescrizione(s);
    		s = extractIndirizzoCliente(page);
    		if(s == null) {
    			result.addMessaggio(i, "Impossibile estrarre l'indirizzo del cliente dalla fattura.");
    			continue;
    		}    		
    		cliente.setIndirizzo(s);
    		s = extractCittaCliente(page);
    		if(s == null) {
    			result.addMessaggio(i, "Impossibile estrarre la città del cliente dalla fattura.");
    			continue;
    		}    		
    		cliente.setCitta(s);
    		cliente.setProvincia("RM"); // TODO: should be extracted from CAP...
    		s = extractCapCliente(page);
    		if(s == null) {
    			result.addMessaggio(i, "Impossibile estrarre il CAP del cliente dalla fattura.");
    			continue;
    		}    		
    		cliente.setCap(s);
    		cliente.setCodicePaese("ITA");
    		DatiFiscali datiFiscaliCliente = new DatiFiscali();
    		s = extractPartitaIvaCliente(page);
    		if(s == null) {
    			result.addMessaggio(i, "Impossibile estrarre il numero di partita IVA del cliente.");
    			continue;
    		}    		
    		datiFiscaliCliente.setPartitaIva(s);
    		cliente.setDatiFiscali(datiFiscaliCliente);
    		documento.setCliente(cliente);
    		
    		// Process condizioni pagamento.
    		//
    		CondizioniPagamento condizioniPagamento = new CondizioniPagamento();
    		s = extractStrumentoPagamento(page);
    		if(s == null) {
    			result.addMessaggio(i, "Impossibile estrarre lo strumento di pagamento della fattura.");
    			continue;    			
    		}
    		condizioniPagamento.setStrumento(s);
    		s = extractTerminiPagamento(page);
    		if(s == null) {
    			result.addMessaggio(i, "Impossibile estrarre i termini di pagamento della fattura.");
    			continue;    			
    		}
    		condizioniPagamento.setTermini(s);
    		Integer giorni = extractGiorniPagamento(page);
    		if(giorni == null) {
    			result.addMessaggio(i, "Impossibile estrarre i giorni di pagamento della fattura.");
    			continue;    			
    		}
    		condizioniPagamento.setGiorni(giorni);
    		documento.setCondizioniPagamento(condizioniPagamento);

    		// Process prodotti.
    		//
    		documento.setProdotti(processRows(page, i, result));

    		// Process importo.
    		//
    		Importo importo = new Importo();
    		String imponibileTotale = extractImponibileTotale(page);
    		if(imponibileTotale == null) {
    			result.addMessaggio(i, "Impossibile estrarre l'imponibile totale della fattura.");
    			continue;    			
    		}
    		importo.setImponibileTotale(imponibileTotale);
    		String importoTassaTotale = extractImportoTassaTotale(page);
    		if(importoTassaTotale == null) {
    			result.addMessaggio(i, "Impossibile estrarre l'importo totale delle tasse.");
    			continue;    			
    		}
    		importo.setImportoTotaleTassa(importoTassaTotale);
    		s = extractImportoTotale(page);
    		if(s == null) {
    			result.addMessaggio(i, "Impossibile estrarre l'importo totale della fattura.");
    			continue;    			
    		}
    		importo.setImportoTotale(s);
    		importo.setImportoTotaleEuro(s);
    		documento.setImporto(importo);

    		// Process tassa.
    		//
    		Tassa tassa = new Tassa();
    		s = extractPercentualeIvaTotale(page);
    		if(s == null) {
    			result.addMessaggio(i, "Impossibile estrarre la percentuale di IVA della fattura.");
    			continue;    			
    		}
    		tassa.setPercentualeIva(s);
    		tassa.setImponibile(imponibileTotale);
    		tassa.setImportoTassa(importoTassaTotale);
    		documento.setTassa(tassa);

    		// The extraction completed without errors, the document can be added to results.
    		//
    		result.addDocumento(documento);
        }
        
        // Return accumulated result.
        //
        return result;
	}


	private List<Prodotto> processRows(PDPage page, int pageNumber, AnalysisResult result) {
		
		String s = extractor.extractRowDescriptions(page);
		String[] rowDescriptions = s.split(System.lineSeparator());

		s = extractor.extractRowQuantities(page);
		String[] rowQuantities = s.split(System.lineSeparator());

		s = extractor.extractRowPrices(page);
		String[] rowPrices = s.split(System.lineSeparator());

		s = extractor.extractRowValues(page);
		String[] rowValues = s.split(System.lineSeparator());

		if(
				rowDescriptions.length != rowQuantities.length ||
				rowDescriptions.length != rowPrices.length ||
				rowDescriptions.length != rowValues.length) {
			
			result.addMessaggio(pageNumber, "Impossibile analizzare le righe della fattura (importi e descrizioni disallineati).");
			return null;
		}
		
		List<Prodotto> prodotti = new ArrayList<Prodotto>();
		
		for(int i = 0; i < rowDescriptions.length; ++i) {
			
			Prodotto prodotto = new Prodotto();
			prodotto.setProgressivo(i + 1);
			
			String codiceProdotto = extractCodiceProdotto(rowDescriptions[i]);
			if(s == null) {
				result.addMessaggio(pageNumber, "Impossibile estrarre il codice prodotto. Riga: " + (i + 1));
				return null;				
			}
			prodotto.setCodiceProdotto(codiceProdotto);
			
			prodotto.setCodificaProdotto("CODICE_FORNITORE");
			
			s = extractDescrizioneProdotto(rowDescriptions[i]);
			if(s == null) {
				result.addMessaggio(pageNumber, "Impossibile estrarre la descrizione del prodotto. Riga: " + (i + 1));
				return null;				
			}			
			prodotto.setDescrizione(s);
			
			s = extractQuantitaProdotto(rowQuantities[i], codiceProdotto);
			if(s == null) {
				result.addMessaggio(pageNumber, "Impossibile estrarre la quantità del prodotto. Riga: " + (i + 1));
				return null;				
			}			
			prodotto.setQuantita(s);
			
			s = extractUnitaMisuraProdotto(codiceProdotto);
			if(s == null) {
				result.addMessaggio(pageNumber, "Impossibile estrarre l'unità di misura del prodotto. Riga: " + (i + 1));
				return null;				
			}			
			prodotto.setUnitaMisura(s);
			
			ImportoProdotto importoProdotto = new ImportoProdotto();
			s = extractImponibileProdotto(rowValues[i]);
			if(s == null) {
				result.addMessaggio(pageNumber, "Impossibile estrarre l'imponibile del prodotto. Riga: " + (i + 1));
				return null;				
			}			
			importoProdotto.setImponibile(s);
			prodotto.setImporto(importoProdotto);
			
			Prezzo prezzo = new Prezzo();
			s = extractPrezzoCessioneProdotto(rowPrices[i]);
			if(s == null) {
				result.addMessaggio(pageNumber, "Impossibile estrarre il prezzo di cessione del prodotto. Riga: " + (i + 1));
				return null;				
			}			
			prezzo.setPrezzoCessione(s);
			prodotto.setPrezzo(prezzo);
			
			prodotti.add(prodotto);
		}
		
		return prodotti;
	}


	private String decideInvoicePage(PDPage page) {
		
		String s = extractor.extractHeader(page);
		
		if(!Pattern.matches(".*F A T T U R A.*", s)) {
			return "Impossibile individuare la dicitura FATTURA nel documento.";
		}
		
		if(!Pattern.matches(".*A\\s+\\d+\\s+.*", s)) {
			return "Impossibile individuare il numero fattura nel documento.";
		}
		
		return null;
	}


	private String extractNumeroFattura(PDPage page) {
		
		String s = extractor.extractHeader(page);
		
		Pattern pattern = Pattern.compile(".*A\\s+(\\d+)\\s+.*");
		Matcher matcher = pattern.matcher(s);
		
		return matcher.matches() ? matcher.group(1) : null; 
	}


	private String extractDataFattura(PDPage page) {
		
		String s = extractor.extractHeader(page);
		
		Pattern pattern = Pattern.compile(".*A\\s+\\d+\\s+(\\d{2})/(\\d{2})/(\\d{4}).*");
		Matcher matcher = pattern.matcher(s);
		
		if(!matcher.matches()) {
			return null;
		}
		
		return String.format("%s-%s-%s", matcher.group(3), matcher.group(2), matcher.group(1)); 
	}


	private String[] extractTestoLibero(PDPage page) {
		
		String s = extractor.extractFreeText(page);
		
		if(s == null || s.length() == 0) {
			return null;
		}
		
		return s.split(System.lineSeparator());
	}
	
	
	private String extractIban(PDPage page) {
		
		String s = extractor.extractIban(page);
		
		Pattern pattern = Pattern.compile("\\s*IBAN\\s+:\\s+(IT\\w+)\\W*");
		Matcher matcher = pattern.matcher(s);
		
		return matcher.matches() ? matcher.group(1) : null; 		
	}
	
	
	private String extractDescrizioneCliente(PDPage page) {
		
		String s = extractor.extractCustomer(page);
		
		if(s == null || s.length() == 0) {
			return null;
		}

		String[] ss = s.split(System.lineSeparator());
		if(ss.length != 6) {
			return null;
		}
		
		return ss[1];
	}
	
	
	private String extractIndirizzoCliente(PDPage page) {
		
		String s = extractor.extractCustomer(page);
		
		if(s == null || s.length() == 0) {
			return null;
		}

		String[] ss = s.split(System.lineSeparator());
		if(ss.length != 6) {
			return null;
		}
		
		return ss[2];
	}
	
	
	private String extractCittaCliente(PDPage page) {
		
		String s = extractor.extractCustomer(page);
		
		if(s == null || s.length() == 0) {
			return null;
		}

		String[] ss = s.split(System.lineSeparator());
		if(ss.length != 6) {
			return null;
		}
				
		Pattern pattern = Pattern.compile(".*\\d{5}\\s+(\\w+)\\W*");
		Matcher matcher = pattern.matcher(ss[3]);
		
		return matcher.matches() ? matcher.group(1) : null; 		
	}
	
	
	private String extractCapCliente(PDPage page) {
		
		String s = extractor.extractCustomer(page);
		
		if(s == null || s.length() == 0) {
			return null;
		}

		String[] ss = s.split(System.lineSeparator());
		if(ss.length != 6) {
			return null;
		}
				
		Pattern pattern = Pattern.compile(".*(\\d{5})\\s+\\w+\\W*");
		Matcher matcher = pattern.matcher(ss[3]);
		
		return matcher.matches() ? matcher.group(1) : null; 		
	}
	
	
	private String extractPartitaIvaCliente(PDPage page) {
		
		String s = extractor.extractCustomer(page);
		
		if(s == null || s.length() == 0) {
			return null;
		}

		String[] ss = s.split(System.lineSeparator());
		if(ss.length != 6) {
			return null;
		}
				
		Pattern pattern = Pattern.compile("\\D*(\\d{11})\\D*");
		Matcher matcher = pattern.matcher(ss[5]);
		
		return matcher.matches() ? matcher.group(1) : null; 		
	}
	
	
	private String extractStrumentoPagamento(PDPage page) {
		
		String s = extractor.extractPayment(page);
		
		if(s == null || s.length() == 0) {
			return null;
		}
				
		Pattern pattern = Pattern.compile(".*\\d+\\s+BON\\. \\d+ GG\\. DF\\. FM\\..*");
		Matcher matcher = pattern.matcher(s);
		return matcher.matches() ? "BONIFICO" : null; 		
	}
	
	
	private String extractTerminiPagamento(PDPage page) {
		
		String s = extractor.extractPayment(page);
		
		if(s == null || s.length() == 0) {
			return null;
		}
				
		Pattern pattern = Pattern.compile(".*\\d+\\s+BON\\. \\d+ GG\\. DF\\. FM\\..*");
		Matcher matcher = pattern.matcher(s);
		return matcher.matches() ? "FINE_MESE_DATA_FATTURA" : null; 		
	}
	
	
	private Integer extractGiorniPagamento(PDPage page) {
		
		String s = extractor.extractPayment(page);
		
		if(s == null || s.length() == 0) {
			return null;
		}
				
		Pattern pattern = Pattern.compile(".*\\d+\\s+BON\\. (\\d+) GG\\. DF\\. FM\\..*");
		Matcher matcher = pattern.matcher(s);
		return matcher.matches() ? Integer.parseInt(matcher.group(1)) : null; 		
	}
	
	
	private String extractCodiceProdotto(String s) {

		Pattern pattern = Pattern.compile(".*-\\s+([^:]+):.*");
		Matcher matcher = pattern.matcher(s);
		
		if(!matcher.matches()) {
			return null;
		}
		
		return matcher.group(1).replaceAll("\\W", "").toUpperCase();
	}
	
	
	private String extractDescrizioneProdotto(String s) {

		Pattern pattern = Pattern.compile("\\s*-\\s+(.*)");
		Matcher matcher = pattern.matcher(s);
		
		if(!matcher.matches()) {
			return null;
		}
		
		return matcher.group(1).trim().replaceAll(" {2,}", " ");
	}
	
	
	private String extractQuantitaProdotto(String s, String codiceProdotto) {

		if(codiceProdotto.equals("PIANTON")) {
			
			Pattern pattern = Pattern.compile("\\s*([0-9\\.]+)\\s+(\\d{2})\\s*");
			Matcher matcher = pattern.matcher(s);

			if(!matcher.matches()) {
				return null;
			}
			
			String intPart = matcher.group(1).replaceAll("\\.", "");
			String decPart = matcher.group(2);
			
			return String.format("%s.%04d", intPart, Integer.parseInt(decPart) * 10000 / 60);
			
		} else if(codiceProdotto.equals("PRONTOINT")) {
			
			Pattern pattern = Pattern.compile("\\s*([0-9\\.]+)\\s*");
			Matcher matcher = pattern.matcher(s);

			if(!matcher.matches()) {
				return null;
			}

			return matcher.group(1);
			
		} else {
			
			return null;
		}
	}
	
	
	private String extractUnitaMisuraProdotto(String codiceProdotto) {

		if(codiceProdotto.equals("PIANTON")) {
			
			return "ORE";
			
		} else if(codiceProdotto.equals("PRONTOINT")) {

			return "ACCESSI";
			
		} else {
			
			return null;
		}
	}
	
	
	private String extractImponibileProdotto(String s) {

		return s.trim().replaceAll("[^0-9,]", "").replaceAll(",", ".");
	}
	
	
	private String extractPrezzoCessioneProdotto(String s) {

		return s.trim().replaceAll("[^0-9,]", "").replaceAll(",", ".");
	}
	
	
	private String extractImponibileTotale(PDPage page) {
		
		String s = extractor.extractTotals(page);
		
		if(s == null || s.length() == 0) {
			return null;
		}
				
		Pattern pattern = Pattern.compile("\\s*€?\\s*([0-9\\.,]+)\\s+€?\\s*([0-9\\.,]+)\\s+([0-9]+)\\s+€?\\s*([0-9\\.,]+)\\s+€?\\s*([0-9\\.,]+)\\s*");
		Matcher matcher = pattern.matcher(s);
		
		if(!matcher.matches()) {
			return null;
		}
		
		return matcher.group(1).trim().replaceAll("[^0-9,]", "").replaceAll(",", "."); 		
	}
	
	
	private String extractImportoTassaTotale(PDPage page) {
		
		String s = extractor.extractTotals(page);
		
		if(s == null || s.length() == 0) {
			return null;
		}
				
		Pattern pattern = Pattern.compile("\\s*€?\\s*([0-9\\.,]+)\\s+€?\\s*([0-9\\.,]+)\\s+([0-9]+)\\s+€?\\s*([0-9\\.,]+)\\s+€?\\s*([0-9\\.,]+)\\s*");
		Matcher matcher = pattern.matcher(s);
		
		if(!matcher.matches()) {
			return null;
		}
		
		return matcher.group(4).trim().replaceAll("[^0-9,]", "").replaceAll(",", "."); 		
	}
	
	
	private String extractImportoTotale(PDPage page) {
		
		String s = extractor.extractTotals(page);
		
		if(s == null || s.length() == 0) {
			return null;
		}
				
		Pattern pattern = Pattern.compile("\\s*€?\\s*([0-9\\.,]+)\\s+€?\\s*([0-9\\.,]+)\\s+([0-9]+)\\s+€?\\s*([0-9\\.,]+)\\s+€?\\s*([0-9\\.,]+)\\s*");
		Matcher matcher = pattern.matcher(s);
		
		if(!matcher.matches()) {
			return null;
		}
		
		return matcher.group(5).trim().replaceAll("[^0-9,]", "").replaceAll(",", "."); 		
	}
	
	
	private String extractPercentualeIvaTotale(PDPage page) {
		
		String s = extractor.extractTotals(page);
		
		if(s == null || s.length() == 0) {
			return null;
		}
				
		Pattern pattern = Pattern.compile("\\s*€?\\s*([0-9\\.,]+)\\s+€?\\s*([0-9\\.,]+)\\s+([0-9]+)\\s+€?\\s*([0-9\\.,]+)\\s+€?\\s*([0-9\\.,]+)\\s*");
		Matcher matcher = pattern.matcher(s);
		
		if(!matcher.matches()) {
			return null;
		}
		
		return matcher.group(3).trim().replaceAll("[^0-9,]", "").replaceAll(",", "."); 		
	}
}
