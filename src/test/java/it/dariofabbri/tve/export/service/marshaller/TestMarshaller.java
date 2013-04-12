package it.dariofabbri.tve.export.service.marshaller;

import it.dariofabbri.tve.export.model.Documento;
import it.dariofabbri.tve.export.service.analyzer.AnalysisResult;
import it.dariofabbri.tve.export.service.analyzer.Analyzer;
import it.dariofabbri.tve.export.xmltest.IgnoreUuidDifferenceListener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TestMarshaller extends XMLTestCase {

	private PDDocument document;
	
	public TestMarshaller() throws IOException {

		URL url = this.getClass().getResource("/pdf/201302.pdf");
		document = PDDocument.load(url);
	}

	@Test
	public void test1() throws SAXException, IOException {
		
		GregorianCalendar gc = new GregorianCalendar(2013, 1, 28);
		
		Analyzer analyzer = new Analyzer();
		AnalysisResult ar = analyzer.analyze(document);

		List<Documento> documenti = new ArrayList<Documento>();
		Documento documento = ar.getDocumenti().get(0);
		documenti.add(documento);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Marshaller marshaller = new Marshaller();
		marshaller.generateXml(documenti, gc.getTime(), baos);

	    InputStream is = this.getClass().getResourceAsStream("/xml/INV_F07229_20130228.xml");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

	    XMLUnit.setIgnoreWhitespace(true);
	    XMLUnit.setIgnoreComments(true);
	    
		Diff diff = compareXML(reader, new String(baos.toByteArray()));
		diff.overrideDifferenceListener(new IgnoreUuidDifferenceListener());
		if(!diff.similar()) {
			System.out.println("KO");
			System.out.println("-----------------------------------------------------------------");
			System.out.println(new String(baos.toByteArray()));
			System.out.println("-----------------------------------------------------------------");
			System.out.println(diff.toString());
			throw new RuntimeException("XMLs were different.");
		}
		else {
			System.out.println("OK");
			System.out.println(new String(baos.toByteArray()));
		}

	}
	
}
