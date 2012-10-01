package com.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class gives the current date/time, according to the pattern attribute.
 * 
 * It's useful to get an unique date pattern, to avoid duplicate temporary file (with the same name) for example
 * 
 * @author xavier
 *
 */
public class DateTokenGetter {

	private static SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");
	
	
	/**
	 * Get the current date, according to the defined pattern
	 */
	public static String getToken() {
		return timeStampFormat.format(new Date());
	}
	
}
