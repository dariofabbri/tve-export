package it.dariofabbri.tve.export.service.extractor;

import java.awt.Rectangle;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFTextStripperByArea;

public class Extractor {

	private PDFTextStripperByArea stripper;
	
	public Extractor() {
		
		try {
			stripper = new PDFTextStripperByArea();
			
		} catch (IOException e) {
			
			throw new RuntimeException("Exception caught while creating text stripper.", e);
		}
		
        stripper.setSortByPosition(true);

        // Set up regions.
        //
        stripper.addRegion("header", new Rectangle(282, 233, 313, 32));
        stripper.addRegion("freetext", new Rectangle(0, 306, 595, 42));
        stripper.addRegion("iban", new Rectangle(272, 263, 323, 25));
        stripper.addRegion("customer", new Rectangle(0, 125, 180, 100));
        stripper.addRegion("payment", new Rectangle(0, 260, 260, 32));
        stripper.addRegion("rowdescriptions", new Rectangle(0, 350, 320, 370));
        stripper.addRegion("rowqtys", new Rectangle(320, 350, 80, 370));
        stripper.addRegion("rowprices", new Rectangle(400, 350, 90, 370));
        stripper.addRegion("rowvalues", new Rectangle(490, 350, 105, 370));
        stripper.addRegion("totals", new Rectangle(0, 720, 595, 50));
	}
	
	
	public String extractHeader(PDPage page) {
		
		return extractRegion(page, "header");
	}
	
	
	public String extractFreeText(PDPage page) {
		
		return extractRegion(page, "freetext");
	}
	
	
	public String extractIban(PDPage page) {
		
		return extractRegion(page, "iban");
	}
	
	
	public String extractCustomer(PDPage page) {
		
		return extractRegion(page, "customer");
	}
	
	
	public String extractPayment(PDPage page) {
		
		return extractRegion(page, "payment");
	}
	
	
	public String extractRowDescriptions(PDPage page) {
		
		return extractRegion(page, "rowdescriptions");
	}
	
	
	public String extractRowQuantities(PDPage page) {
		
		return extractRegion(page, "rowqtys");
	}
	
	
	public String extractRowPrices(PDPage page) {
		
		return extractRegion(page, "rowprices");
	}
	
	
	public String extractRowValues(PDPage page) {
		
		return extractRegion(page, "rowvalues");
	}
	
	
	public String extractTotals(PDPage page) {
		
		return extractRegion(page, "totals");
	}
	
	
	private String extractRegion(PDPage page, String region) {
		
		try {
			stripper.extractRegions(page);
			
		} catch (IOException e) {
			
			throw new RuntimeException("Exception caught while extracting text.", e);
		}
		
        String s = stripper.getTextForRegion(region);
        return s != null ? s.trim() : null;
	}
	
}
