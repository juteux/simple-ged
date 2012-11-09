package com.simple.ged.ui.listeners;

import java.util.EventListener;

/**
 * 
 * Action fired by QuickSearchBar
 * 
 * @author xavier
 *
 */
public interface QuickSearchListener extends EventListener {

	/**
	 * You need to search the given pattern
	 * 
	 * @param pattern
	 * 				The pattern entered by the user
	 */
	void search(String pattern);
	
}
