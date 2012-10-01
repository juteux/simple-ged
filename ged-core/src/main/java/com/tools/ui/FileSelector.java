package com.tools.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import net.miginfocom.swing.MigLayout;

import com.tools.listeners.FileChangedListener;


/**
 * A file selector, a text field for path and a browsing button
 */

public abstract class FileSelector extends AbstractWidget {

	private static final long serialVersionUID = 1L;

	/**
	 * To signal to listeners when selection changed
	 */
	protected EventListenerList listenerList;
	
	/**
	 * The popup title (when the user will click the browse button)
	 */
    private String windowsTitle;

    /**
     * Path to the default folder (when the user wants to change selected file)
     */
    private String dirPath; 

    /**
     * The widget which display current selection
     */
    private JTextField selectionLineEdit; 

    /**
     * The browsing button
     */
    private JButton btnBrowse;

	/**
	 * @param windowsTitle
	 * 				The popup title (when the user will click the browse button)
	 */
	public FileSelector(String windowsTitle) {
		super();
		this.windowsTitle = windowsTitle;

		listenerList = new EventListenerList();
		dirPath = ".";
		
		// Instantiate
		btnBrowse = new JButton("...");
		selectionLineEdit = new JTextField();
		
		// add actions
		btnBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openSelectionPopup();
			}
		});
		
		selectionLineEdit.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				validSelection();
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
		
		// layouting ...
		MigLayout layout = new MigLayout(	
				"wrap",
				"[grow,fill,center][]",	// columns
				"[grow,fill,center][]"	// rows
			);
		
		JPanel container = new JPanel(layout);
		
		container.add(selectionLineEdit);
		container.add(btnBrowse);
		
		add(container);
	}

	/**
	 * Add some listener
	 */
	public void addFileChangedListener(FileChangedListener l) {
        this.listenerList.add(FileChangedListener.class, l);
	} 
	
	/**
	 *  Remove some listener 
	 */
	public void removeFileChangedListener(FileChangedListener l) {
		listenerList.remove(FileChangedListener.class, l);
	} 
	
	/**
	 * Display the given file path in text field
	 * @param path
	 */
	public void setCurrentFilePath(String path) {
		selectionLineEdit.setText(path);
		validSelection();
	}
	
	/**
	 * Is the current file path valid ?
	 * @return
	 * 		True if the file path is valid, false otherwise
	 */
	public abstract boolean isValidPath();
	
	/**
	 * Open the file selector popup
	 */
	public abstract void openSelectionPopup();
	
	/**
	 * Update the UI, according to the value returned by isValidPath()
	 */
	protected void validSelection() {
		selectionLineEdit.setForeground(isValidPath() ? Color.BLACK : Color.RED);
		
		// send the good news to listeners
		FileChangedListener[] listeners = (FileChangedListener [])listenerList.getListeners(FileChangedListener.class);
		FileChangedEventObject e = new FileChangedEventObject(this);
		e.setValid(isValidPath());
		for (FileChangedListener fcl : listeners) {
			fcl.newFileSelected(e);
		}
	}
	
	
	/**
	 * Return the current file path, current selection
	 */
	public String getFilePath() {
		return selectionLineEdit.getText();
	}

	/**
	 * @param windowsTitle the windowsTitle to set
	 */
	public void setWindowsTitle(String windowsTitle) {
		this.windowsTitle = windowsTitle;
	}

	/**
	 * Set the default directory path (when browsing)
	 */
	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}

	/**
	 * Fix button browse label
	 */
	public void setBtnBrowseLabel(String label) {
		btnBrowse.setText(label);
	}

	/**
	 * @return the windowsTitle
	 */
	public String getWindowsTitle() {
		return windowsTitle;
	}

	/**
	 * Warning : This is the path where the dialog popup is opened, not the current selected path !
	 * 
	 * @return the dirPath. 
	 */
	public String getDirPath() {
		return dirPath;
	}
	
}
