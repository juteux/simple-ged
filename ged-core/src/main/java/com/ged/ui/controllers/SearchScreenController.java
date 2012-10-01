package com.ged.ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


import com.ged.models.GedDocument;
import com.ged.models.GedDocumentFile;
import com.ged.services.GedDocumentService;
import com.ged.ui.screens.SearchingScreen;
import com.ged.ui.screens.SoftwareScreen;
import com.tools.DateHelper;
import com.tools.hibernate.HibernateUtil;

/**
 * The controller of the search document screen
 * 
 * @author xavier
 *
 */
public class SearchScreenController implements KeyListener, ActionListener, MouseListener {

	private static final Logger logger = Logger.getLogger(SearchScreenController.class);
	
	/**
	 * The controlled object
	 */
	private SearchingScreen searchingScreen;
	
	
	public SearchScreenController(SearchingScreen searchingScreen) {
		this.searchingScreen = searchingScreen;
	}
	

	@Override
	public void keyReleased(KeyEvent arg0) {
		searchingScreen.getBtnSearch().setEnabled(
				! searchingScreen.getInputSearchedPattern().getText().isEmpty()
		);
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == 10) { // enter
			search();
		}
	}
	
	@Override
	public void keyTyped(KeyEvent arg0) {	
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == searchingScreen.getBtnReturn()) {
			searchingScreen.gotoScreen(SoftwareScreen.Screen.WELCOME_SCEEN);
		}
		else if (e.getSource() == searchingScreen.getBtnSearch()) {
			search();
		}
		else {
			logger.warn("Not implemented yet, see SearchScreenController.actionPerformed");
		}
	}

	
	/**
	 * Launch the research (threaded)
	 */
	private void search() {
		
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {

				String[] words =  searchingScreen.getInputSearchedPattern().getText().split(" ");
				boolean searchInDescription = searchingScreen.getCheckboxDescription().isSelected();
				
				Session session = HibernateUtil.getSessionFactory().openSession();
				
				Set<GedDocument> results =  new HashSet<GedDocument>();
				
				// ---
				
				Criteria criteriaDocument = session.createCriteria(GedDocument.class);
				for (String word : words) {
					criteriaDocument.add(Restrictions.like("name", "%" + word + "%").ignoreCase());
				}
				
				// add founded documents
				@SuppressWarnings("unchecked")
				List<GedDocument> docs = criteriaDocument.list();
				results.addAll(docs);
				
				// ---
				
				if ( searchInDescription ) {
					Criteria criteriaDocument2 = session.createCriteria(GedDocument.class);
					for (String word : words) {
						criteriaDocument2.add(Restrictions.like("description", "%" + word + "%").ignoreCase());
					}
					
					@SuppressWarnings("unchecked")
					List<GedDocument> docs2 = criteriaDocument2.list();
					results.addAll(docs2);
				}
				
				// ---
				
				Criteria criteriaFile = session.createCriteria(GedDocumentFile.class);
				for (String word : words) {
					criteriaFile.add(Restrictions.like("relativeFilePath", "%" + word + "%").ignoreCase());
				}
				
				// add founded files
				@SuppressWarnings("unchecked")
				List<GedDocumentFile> files = criteriaFile.list();  
				for (GedDocumentFile file : files) {
					results.add(file.getDocument());
				}
				
				// ---

				DefaultTableModel tableModel = searchingScreen.getTableResultsModel();
				
				while (tableModel.getRowCount() > 0) {
					tableModel.removeRow(0);
				}
				
				for (GedDocument doc : results) {
					
					for (GedDocumentFile f : doc.getDocumentFiles()) {
						
						tableModel.addRow(
								new Object[] {
										f.getRelativeFilePath(),
										doc.getName(),
										DateHelper.calendarToString(doc.getDate()),
										doc.getDescription()
								}
							);
					}
				}
				
			}
			
		});
		
		t.start();
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		
		if (arg0.getSource() == searchingScreen.getTableResults()) {
			
			int row = searchingScreen.getTableResults().getSelectedRow();
			if (row == -1) {
				return;
			}
	
			searchingScreen.getDocumentPreview().setGedDocument(GedDocumentService.findDocumentbyFilePath(
					//searchingScreen.getTableResults().getCellRenderer(row, 0)
					searchingScreen.getTableResults().getValueAt(row, 0).toString()
			));
			
		}
		
	}
	
}
