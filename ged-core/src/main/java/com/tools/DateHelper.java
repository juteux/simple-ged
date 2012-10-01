package com.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This class provides tools for date manipulation
 * 
 * @author xavier
 *
 */
public class DateHelper {

	/**
	 * Convert the given calendar to a string which represent the date (formatted as dd/mm/yyyy)
	 */
	public static final String calendarToString(Calendar cal) {
		StringBuffer ret = new StringBuffer();
		
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if (day < 10) {
			ret.append("0");
		}
		ret.append(day);
		
		ret.append("/");
		
		int month = cal.get(Calendar.MONTH) + 1;
		if (month < 10) {
			ret.append("0");
		}
		ret.append(month);
		
		ret.append("/");
		
		ret.append(cal.get(Calendar.YEAR));
		
		return ret.toString();
	}
	
	/**
	 * Convert the given date to a string which represent the date (formatted as dd/mm/yyyy)
	 */
	public static final String calendarToString(Date date) {
		if (date == null) {
			return null;
		}
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		return calendarToString(cal);
	}
	
	
	/**
	 * Is the given date valid ?
	 * 
	 * @param inDate
	 *            A string with format dd/MM/yyyy
	 * 
	 * @return True if the date is valid, false otherwise
	 */
	public static boolean isValidDate(String inDate) {

		if (inDate == null)
			return false;

		// set the format to use as a constructor argument
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		if (inDate.trim().length() != dateFormat.toPattern().length())
			return false;

		dateFormat.setLenient(false);

		try {
			// parse the inDate parameter
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}
	
	/**
	 * Return the month name, according to in value [0-11[
	 */
	public static String getMonthName(int month) {
		switch (month) {
		
		case 0 :
			return "janvier";
			
		case 1 :
			return "février";
			
		case 2 :
			return "mars";
			
		case 3 :
			return "avril";
			
		case 4 :
			return "mai";	
			
		case 5 :
			return "juin";
			
		case 6 :
			return "juillet";
			
		case 7 :
			return "août";
			
		case 8 :
			return "septembre";
			
		case 9 :
			return "octobre";

		case 10 :
			return "novembre";
			
		case -1 :
		case 11 :
			return "décembre";
		}

		return "";
	}
}
