package it.dariofabbri.tve.export.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class DecimalUtils {

	private static DecimalFormat df;
	
	static {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		dfs.setGroupingSeparator(',');
		
		df = new DecimalFormat("###.00");
		df.setDecimalFormatSymbols(dfs);
		df.setGroupingUsed(false);
	}
	
	public static BigDecimal makeBigDecimalFromString(String s) {
		
		BigDecimal bd = new BigDecimal(s);
		return bd;
	}

	public static String makeString(BigDecimal bd) {
		
		return df.format(bd);
	}
}
