package it.dariofabbri.tve.export.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TestMarshalling extends XMLTestCase {

	@Test
	public void test104() throws JAXBException, SAXException, IOException {
		
		Messaggio messaggio = buildDocumento104();
		
	    JAXBContext jaxbContext = JAXBContext.newInstance(messaggio.getClass());
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	 
	    StringWriter result = new StringWriter();
	    jaxbMarshaller.marshal(messaggio, result);
	    String xml = result.toString();

	    
		InputStream is = this.getClass().getResourceAsStream("/xml/INV_F07229_20130228.xml");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

	    XMLUnit.setIgnoreWhitespace(true);
	    XMLUnit.setIgnoreComments(true);
		Diff diff = compareXML(reader, xml);
		if(!diff.identical()) {
			System.out.println("KO");
			System.out.println("-----------------------------------------------------------------");
			System.out.println(xml);
			System.out.println("-----------------------------------------------------------------");
			System.out.println(diff.toString());
			throw new RuntimeException("XMLs were different.");
		}
		else {
			System.out.println("OK");
			System.out.println(xml);
		}
	}
	
	
	private Messaggio buildDocumento104() {
		
		Messaggio messaggio = new Messaggio();
		
		DatiTrasmissione datiTrasmissione = new DatiTrasmissione();
		datiTrasmissione.setIdentificativo("752cff7f-6701-44fc-aa96-ccfddb9e68a4");
		datiTrasmissione.setDataCreazione("2013-02-28");
		datiTrasmissione.setDataTrasmissione("2013-02-28");
		messaggio.setDatiTrasmissione(datiTrasmissione);
		
		Documento documento = new Documento();
		documento.setTipoDocumento("FATTURA");
		documento.setProduzione("ORIGINALE");
		documento.setNumeroDocumento("104");
		documento.setDataDocumento("2013-02-04");
		documento.setValuta("EUR");
		
		TestoLibero testoLibero = new TestoLibero();
		testoLibero.setProgressivo(1);
		testoLibero.setTesto1("Ci pregiamo sottoporVi la presente fattura per i seguenti servizi :");
		testoLibero.setTesto2("(periodo Gennaio 2013)");
		documento.setTestoLibero(testoLibero);
		
		RiferimentoDocumento riferimentoDocumento = new RiferimentoDocumento();
		riferimentoDocumento.setTipoDocumento("SERVIZI");
		riferimentoDocumento.setDataDocumento("");
		riferimentoDocumento.setNumeroDocumento("");
		documento.setRiferimentoDocumento(riferimentoDocumento);

		Fornitore fornitore = new Fornitore();
		fornitore.setDescrizione("TVE VIGILANZA L. S.r.l.");
		fornitore.setDescrizione2("ISTITUTO di VIGILANZA");
		fornitore.setIndirizzo("Via del Podere Fiume n. 4");
		fornitore.setCitta("ROMA");
		fornitore.setProvincia("RM");
		fornitore.setCap("00168");
		fornitore.setCodicePaese("ITA");
		documento.setFornitore(fornitore);
		
		DatiFiscali datiFiscaliFornitore = new DatiFiscali();
		datiFiscaliFornitore.setPartitaIva("07147091008");
		datiFiscaliFornitore.setCapitaleSociale("10000€");
		datiFiscaliFornitore.setNumeroRegistroImprese("1013892");
		fornitore.setDatiFiscali(datiFiscaliFornitore);
		
		RiferimentiBancari riferimentiBancari = new RiferimentiBancari();
		riferimentiBancari.setIban("IT14E0503403239000000216029");
		fornitore.setRiferimentiBancari(riferimentiBancari);
		
		Cliente cliente = new Cliente();
		cliente.setDescrizione("A.S.L. RM A");
		cliente.setIndirizzo("Via Ariosto, 3-9");
		cliente.setCitta("ROMA");
		cliente.setProvincia("RM");
		cliente.setCap("00185");
		cliente.setCodicePaese("ITA");
		documento.setCliente(cliente);
		
		DatiFiscali datiFiscaliCliente = new DatiFiscali();
		datiFiscaliCliente.setPartitaIva("04735671002");
		cliente.setDatiFiscali(datiFiscaliCliente);
		
		CondizioniPagamento condizioniPagamento = new CondizioniPagamento();
		condizioniPagamento.setStrumento("BONIFICO");
		condizioniPagamento.setTermini("FINE_MESE_DATA_FATTURA");
		condizioniPagamento.setGiorni(90);
		documento.setCondizioniPagamento(condizioniPagamento);
		

		documento.setProdotti(new ArrayList<Prodotto>());
		
		// Prodotto #1.
		//
		Prodotto prodotto = new Prodotto();
		prodotto.setProgressivo(1);
		prodotto.setCodiceProdotto("PIANTON");
		prodotto.setCodificaProdotto("CODICE_FORNITORE");
		prodotto.setDescrizione("Pianton.: Via Lampedusa, 23");
		prodotto.setQuantita("88.0000");
		prodotto.setUnitaMisura("ORE");
		documento.getProdotti().add(prodotto);
		
		ImportoProdotto importoProdotto = new ImportoProdotto();
		importoProdotto.setImponibile("1452.00");
		prodotto.setImporto(importoProdotto);
		
		Prezzo prezzo = new Prezzo();
		prezzo.setPrezzoCessione("16.50");
		prodotto.setPrezzo(prezzo);
		
		
		// Prodotto #2.
		//
		prodotto = new Prodotto();
		prodotto.setProgressivo(2);
		prodotto.setCodiceProdotto("PIANTON");
		prodotto.setCodificaProdotto("CODICE_FORNITORE");
		prodotto.setDescrizione("Pianton.: Via Dina Galli, 3");
		prodotto.setQuantita("231.5000");
		prodotto.setUnitaMisura("ORE");
		documento.getProdotti().add(prodotto);
		
		importoProdotto = new ImportoProdotto();
		importoProdotto.setImponibile("5257.36");
		prodotto.setImporto(importoProdotto);
		
		prezzo = new Prezzo();
		prezzo.setPrezzoCessione("22.71");
		prodotto.setPrezzo(prezzo);
		
		
		// Prodotto #3.
		//
		prodotto = new Prodotto();
		prodotto.setProgressivo(3);
		prodotto.setCodiceProdotto("PIANTON");
		prodotto.setCodificaProdotto("CODICE_FORNITORE");
		prodotto.setDescrizione("Pianton.: Via Dina Galli, 8");
		prodotto.setQuantita("49.5000");
		prodotto.setUnitaMisura("ORE");
		documento.getProdotti().add(prodotto);
		
		importoProdotto = new ImportoProdotto();
		importoProdotto.setImponibile("1124.14");
		prodotto.setImporto(importoProdotto);
		
		prezzo = new Prezzo();
		prezzo.setPrezzoCessione("22.71");
		prodotto.setPrezzo(prezzo);
		
		
		// Prodotto #4.
		//
		prodotto = new Prodotto();
		prodotto.setProgressivo(4);
		prodotto.setCodiceProdotto("PIANTON");
		prodotto.setCodificaProdotto("CODICE_FORNITORE");
		prodotto.setDescrizione("Pianton.: SERT - Via Montesacro, 8");
		prodotto.setQuantita("321.5000");
		prodotto.setUnitaMisura("ORE");
		documento.getProdotti().add(prodotto);
		
		importoProdotto = new ImportoProdotto();
		importoProdotto.setImponibile("7301.26");
		prodotto.setImporto(importoProdotto);
		
		prezzo = new Prezzo();
		prezzo.setPrezzoCessione("22.71");
		prodotto.setPrezzo(prezzo);
		
		
		// Prodotto #5.
		//
		prodotto = new Prodotto();
		prodotto.setProgressivo(5);
		prodotto.setCodiceProdotto("PIANTON");
		prodotto.setCodificaProdotto("CODICE_FORNITORE");
		prodotto.setDescrizione("Pianton.: SERT - Largo Rovani,");
		prodotto.setQuantita("178.0000");
		prodotto.setUnitaMisura("ORE");
		documento.getProdotti().add(prodotto);
		
		importoProdotto = new ImportoProdotto();
		importoProdotto.setImponibile("4042.38");
		prodotto.setImporto(importoProdotto);
		
		prezzo = new Prezzo();
		prezzo.setPrezzoCessione("22.71");
		prodotto.setPrezzo(prezzo);
		
		
		// Prodotto #6.
		//
		prodotto = new Prodotto();
		prodotto.setProgressivo(6);
		prodotto.setCodiceProdotto("PRONTOINT");
		prodotto.setCodificaProdotto("CODICE_FORNITORE");
		prodotto.setDescrizione("Pronto int.: n. 1 passaggio notturno presso Via D. Nicodemi, 105");
		prodotto.setQuantita("31.00");
		prodotto.setUnitaMisura("ACCESSI");
		documento.getProdotti().add(prodotto);
		
		importoProdotto = new ImportoProdotto();
		importoProdotto.setImponibile("29.45");
		prodotto.setImporto(importoProdotto);
		
		prezzo = new Prezzo();
		prezzo.setPrezzoCessione("0.95");
		prodotto.setPrezzo(prezzo);
		
		
		// Prodotto #7.
		//
		prodotto = new Prodotto();
		prodotto.setProgressivo(7);
		prodotto.setCodiceProdotto("PRONTOINT");
		prodotto.setCodificaProdotto("CODICE_FORNITORE");
		prodotto.setDescrizione("Pronto int.: n. 1 passaggio notturno presso Via Lampedusa, 23");
		prodotto.setQuantita("31.00");
		prodotto.setUnitaMisura("ACCESSI");
		documento.getProdotti().add(prodotto);
		
		importoProdotto = new ImportoProdotto();
		importoProdotto.setImponibile("29.45");
		prodotto.setImporto(importoProdotto);
		
		prezzo = new Prezzo();
		prezzo.setPrezzoCessione("0.95");
		prodotto.setPrezzo(prezzo);
		
		
		// Prodotto #8.
		//
		prodotto = new Prodotto();
		prodotto.setProgressivo(8);
		prodotto.setCodiceProdotto("PRONTOINT");
		prodotto.setCodificaProdotto("CODICE_FORNITORE");
		prodotto.setDescrizione("Pronto int.: n. 1 passaggio notturno presso Via D. Galli, 3/8");
		prodotto.setQuantita("31.00");
		prodotto.setUnitaMisura("ACCESSI");
		documento.getProdotti().add(prodotto);
		
		importoProdotto = new ImportoProdotto();
		importoProdotto.setImponibile("29.45");
		prodotto.setImporto(importoProdotto);
		
		prezzo = new Prezzo();
		prezzo.setPrezzoCessione("0.95");
		prodotto.setPrezzo(prezzo);
		

		
		Importo importo = new Importo();
		importo.setImponibileTotale("19265.51");
		importo.setImportoTotaleTassa("4045.76");
		importo.setImportoTotale("23311.27");
		importo.setImportoTotaleEuro("23311.27");
		documento.setImporto(importo);
		
		Tassa tassa = new Tassa();
		tassa.setPercentualeIva("21");
		tassa.setImponibile("19265.51");
		tassa.setImportoTassa("4045.76");
		documento.setTassa(tassa);
		
		messaggio.setDocumenti(new ArrayList<Documento>());
		messaggio.getDocumenti().add(documento);

		return messaggio;
	}
}
