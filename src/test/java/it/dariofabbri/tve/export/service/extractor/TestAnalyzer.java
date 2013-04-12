package it.dariofabbri.tve.export.service.extractor;

import it.dariofabbri.tve.export.model.DatiFiscali;
import it.dariofabbri.tve.export.model.DatiTrasmissione;
import it.dariofabbri.tve.export.model.Documento;
import it.dariofabbri.tve.export.model.Fornitore;
import it.dariofabbri.tve.export.model.Messaggio;
import it.dariofabbri.tve.export.model.RiferimentoDocumento;
import it.dariofabbri.tve.export.service.analyzer.AnalysisResult;
import it.dariofabbri.tve.export.service.analyzer.Analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import junit.framework.Assert;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TestAnalyzer extends XMLTestCase {

	private PDDocument document;
	
	public TestAnalyzer() throws IOException {

		URL url = this.getClass().getResource("/pdf/201302.pdf");
		document = PDDocument.load(url);
	}

	@Test
	public void test1() {
		
		Analyzer analyzer = new Analyzer();
		AnalysisResult result = analyzer.analyze(document);
		
		Assert.assertNotNull(result);
		for(Pair<Integer, String> messaggio : result.getMessaggi()) {
			System.out.println(String.format("%d - %s", messaggio.getLeft(), messaggio.getRight()));
		}
		Assert.assertTrue(result.getMessaggi().size() == 0);		
	}
	
	
	@Test
	public void test2() throws JAXBException, SAXException, IOException {
		
		Analyzer analyzer = new Analyzer();
		AnalysisResult ar = analyzer.analyze(document);

		List<Documento> documenti = new ArrayList<Documento>();
		Documento documento = ar.getDocumenti().get(0);
		
		Fornitore fornitore = documento.getFornitore();
		fornitore.setDescrizione("TVE VIGILANZA L. S.r.l.");
		fornitore.setDescrizione2("ISTITUTO di VIGILANZA");
		fornitore.setIndirizzo("Via del Podere Fiume n. 4");
		fornitore.setCitta("ROMA");
		fornitore.setProvincia("RM");
		fornitore.setCap("00168");
		fornitore.setCodicePaese("ITA");
		
		DatiFiscali datiFiscaliFornitore = new DatiFiscali();
		datiFiscaliFornitore.setPartitaIva("07147091008");
		datiFiscaliFornitore.setCapitaleSociale("10000€");
		datiFiscaliFornitore.setNumeroRegistroImprese("1013892");
		fornitore.setDatiFiscali(datiFiscaliFornitore);

		documento.setFornitore(fornitore);
		
		RiferimentoDocumento riferimentoDocumento = new RiferimentoDocumento();
		riferimentoDocumento.setTipoDocumento("SERVIZI");
		riferimentoDocumento.setDataDocumento("");
		riferimentoDocumento.setNumeroDocumento("");
		documento.setRiferimentoDocumento(riferimentoDocumento);

		
		documenti.add(documento);
		
		Messaggio messaggio = new Messaggio();
		messaggio.setDocumenti(documenti);
				
		DatiTrasmissione datiTrasmissione = new DatiTrasmissione();
		datiTrasmissione.setIdentificativo("752cff7f-6701-44fc-aa96-ccfddb9e68a4");
		datiTrasmissione.setDataCreazione("2013-02-28");
		datiTrasmissione.setDataTrasmissione("2013-02-28");
		messaggio.setDatiTrasmissione(datiTrasmissione);

		
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
	
	
	@Test
	public void test3() throws JAXBException, SAXException, IOException {
		
		Analyzer analyzer = new Analyzer();
		AnalysisResult ar = analyzer.analyze(document);

		Assert.assertTrue(ar.getDocumenti().size() == 4);
		
		for(Documento documento : ar.getDocumenti()) {
			System.out.println(String.format("%s - %s", documento.getNumeroDocumento(), documento.getDataDocumento()));
		}
	}
}
