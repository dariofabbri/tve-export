package it.dariofabbri.tve.export.xmltest;

import java.util.regex.Pattern;

import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceListener;
import org.w3c.dom.Node;

public class IgnoreUuidDifferenceListener implements DifferenceListener {

	private Pattern xpath = Pattern.compile(".*Messaggio.*DatiTrasmissione.*Identificativo.*text.*");
	private Pattern uuid = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
	
	@Override
	public int differenceFound(Difference difference) {

		if(
				xpath.matcher(difference.getTestNodeDetail().getXpathLocation()).matches() &&
				uuid.matcher(difference.getTestNodeDetail().getNode().getNodeValue()).matches()) {
			return DifferenceListener.RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
		}
		
		return DifferenceListener.RETURN_ACCEPT_DIFFERENCE;
	}
	
	@Override
	public void skippedComparison(Node arg0, Node arg1) {
	}
	
}
