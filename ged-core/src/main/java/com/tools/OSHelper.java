package com.tools;

/**
 * Help tools for OS managing
 * 
 * 
 * @author xavier
 *
 */
public class OSHelper {

	/**
	 * @return the os name
	 */
	public static String getOSName() {
		return System.getProperty("os.name");
	}
	
	
	/**
	 * Am I running on windows OS ?
	 * 
	 * @return True if I'm running on windows, false otherwise.
	 */
	public static boolean isWindowsOs() {
		return System.getProperty("os.name").toLowerCase().startsWith("windows");
	}
}
