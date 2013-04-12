package it.dariofabbri.tve.export.service.extractor;

import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFTextStripperByArea;
import org.junit.Before;
import org.junit.Test;

public class TestExtraction {

	PDDocument document;
	
	@Before
	public void loadDocument() throws IOException {
		
		URL url = this.getClass().getResource("/pdf/201302.pdf");
		document = PDDocument.load(url);
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void test1() throws IOException {
		
		PDFTextStripperByArea stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition( true );

        Rectangle rect = new Rectangle(0, 0, 150, 250);
        stripper.addRegion("dest1", rect);

        List<PDPage> allPages = document.getDocumentCatalog().getAllPages();
        for(PDPage page : allPages) {

            System.out.println();
        	System.out.println("Processing page: " + page);
            stripper.extractRegions(page);
            System.out.println("Text in the area:" + rect);
            System.out.println(stripper.getTextForRegion("dest1"));
        }
	}
}
