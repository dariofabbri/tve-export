package it.dariofabbri.tve.export.util;

import java.util.regex.Pattern;

public class ValidationUtils {

	private static Pattern decimalPattern = Pattern.compile("^\\d+(\\.\\d+)?$");
	
	public static boolean isValidQuantity(String s) {
		return isValidDecimal(s);
	}
	
	public static boolean isValidPrice(String s) {
		return isValidDecimal(s);
	}
	
	public static boolean isValidDecimal(String s) {
		return decimalPattern.matcher(s).matches();
	}
}
