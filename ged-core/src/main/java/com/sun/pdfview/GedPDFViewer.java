/*
 * $Id: PDFViewer.java,v 1.10 2009-08-07 23:18:33 tomoke Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * 
 * Adapted by Xavier for simple GED.
 */
package com.sun.pdfview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import net.miginfocom.swing.MigLayout;

import com.sun.pdfview.action.GoToAction;
import com.sun.pdfview.action.PDFAction;

/**
 * A PDF Viewer, adapted from sun demo
 */
public class GedPDFViewer extends JPanel
        implements KeyListener, TreeSelectionListener,
        PageChangeListener {

	
	private static final Logger logger = Logger.getLogger(GedPDFViewer.class);
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    /** The current PDFFile */
    PDFFile curFile;
    /** the name of the current document */
    String docName;
    /** The split between thumbs and page */
    JSplitPane split;
    /** The thumbnail scroll pane */
    JScrollPane thumbscroll;
    /** The thumbnail display */
    ThumbPanel thumbs;
    /** The page display */
    PagePanel page;
    /** The full screen page display, or null if not in full screen mode */
    PagePanel fspp;

    //    Thread anim;
    /** The current page number (starts at 0), or -1 if no page */
    int curpage = 0;
    /** the full screen button */
    JToggleButton fullScreenButton;
    /** the current page number text field */
    JTextField pageField;
    /** the full screen window, or null if not in full screen mode */
    FullScreenWindow fullScreen;
    /** the root of the outline, or null if there is no outline */
    OutlineNode outline = null;
    /** The page format for printing */
    PageFormat pformat = PrinterJob.getPrinterJob().defaultPage();
    /** flag to indicate when a newly added document has been announced */
    Flag docWaiter;
    /** a thread that pre-loads the next page for faster response */
    PagePreparer pagePrep;
    /** the window containing the pdf outline, or null if one doesn't exist */
    JDialog olf;

    /**
     * utility method to get an icon from the resources of this class
     * @param name the name of the icon
     * @return the icon, or null if the icon wasn't found.
     */
    public Icon getIcon(String name) {
        Icon icon = null;
        URL url = null;
        try {
            url = getClass().getResource(name);
            icon = new ImageIcon(url);
        } catch (Exception e) {
            logger.error("Couldn't find " + getClass().getName() + "/" + name);
            e.printStackTrace();
        }
        return icon;
    }

    class ThumbAction extends AbstractAction
            implements PropertyChangeListener {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		boolean isOpen = true;

        public ThumbAction() {
            super("Hide thumbnails");
        }

        public void propertyChange(PropertyChangeEvent evt) {
            int v = ((Integer) evt.getNewValue()).intValue();
            if (v <= 1) {
                isOpen = false;
                putValue(ACTION_COMMAND_KEY, "Show thumbnails");
                putValue(NAME, "Show thumbnails");
            } else {
                isOpen = true;
                putValue(ACTION_COMMAND_KEY, "Hide thumbnails");
                putValue(NAME, "Hide thumbnails");
            }
        }

        public void actionPerformed(ActionEvent evt) {
            doThumbs(!isOpen);
        }
    }
    ThumbAction thumbAction = new ThumbAction();

    Action nextAction = new AbstractAction("Next", getIcon("gfx/next.gif")) {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent evt) {
            doNext();
        }
    };
    Action firstAction = new AbstractAction("First", getIcon("gfx/first.gif")) {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent evt) {
            doFirst();
        }
    };
    Action lastAction = new AbstractAction("Last", getIcon("gfx/last.gif")) {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent evt) {
            doLast();
        }
    };
    Action prevAction = new AbstractAction("Prev", getIcon("gfx/prev.gif")) {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent evt) {
            doPrev();
        }
    };

    
    private Dimension maximumSize;
    
    /**
     * Create a new PDFViewer based on a user, with or without a thumbnail
     * panel.
     * @param useThumbs true if the thumb panel should exist, false if not.
     */
    public GedPDFViewer(Dimension maximumSize) {
        super();
        this.maximumSize = maximumSize;
        init();
    }

    /**
     * Initialize this PDFViewer by creating the GUI.
     */
    protected void init() {
        page = new PagePanel();
        page.addKeyListener(this);

        page.setMaximumSize(new Dimension((int) maximumSize.getWidth(), (int) (maximumSize.getHeight()-40)));
        page.setPreferredSize(new Dimension((int) maximumSize.getWidth(), (int) (maximumSize.getHeight()-40)));
        
        MigLayout layout = new MigLayout("wrap", "[grow,fill,center]", "");
        JPanel container = new JPanel(layout);

        JToolBar toolbar = new JToolBar();
        
        toolbar.setMaximumSize(new Dimension((int) maximumSize.getWidth() - 375, 40));
        toolbar.setPreferredSize(new Dimension((int) maximumSize.getWidth() - 375, 40));
        
        toolbar.setFloatable(false);

        JButton jb;

        jb = new JButton(firstAction);
        jb.setText("");
        toolbar.add(jb);
        jb = new JButton(prevAction);
        jb.setText("");
        toolbar.add(jb);
        pageField = new JTextField("-", 3);
        //	pageField.setEnabled(false);
        pageField.setMaximumSize(new Dimension(45, 32));
        pageField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                doPageTyped();
            }
        });
        toolbar.add(pageField);
        jb = new JButton(nextAction);
        jb.setText("");
        toolbar.add(jb);
        jb = new JButton(lastAction);
        jb.setText("");
        toolbar.add(jb);

        container.add(toolbar);
        container.add(page);
        
        add(container);
    }

    /**
     * Changes the displayed page, desyncing if we're not on the
     * same page as a presenter.
     * @param pagenum the page to display
     */
    public void gotoPage(int pagenum) {
        if (pagenum < 0) {
            pagenum = 0;
        } else if (pagenum >= curFile.getNumPages()) {
            pagenum = curFile.getNumPages() - 1;
        }
        forceGotoPage(pagenum);
    }

    /**
     * Changes the displayed page.
     * @param pagenum the page to display
     */
    public void forceGotoPage(int pagenum) {
        if (pagenum <= 0) {
            pagenum = 0;
        } else if (pagenum >= curFile.getNumPages()) {
            pagenum = curFile.getNumPages() - 1;
        }

        curpage = pagenum;

        // update the page text field
        pageField.setText(String.valueOf(curpage + 1));

        // fetch the page and show it in the appropriate place
        PDFPage pg = curFile.getPage(pagenum + 1);
        if (fspp != null) {
            fspp.showPage(pg);
            fspp.requestFocus();
        } else {
            page.showPage(pg);
            page.requestFocus();
        }

        // stop any previous page prepper, and start a new one
        if (pagePrep != null) {
            pagePrep.quit();
        }
        pagePrep = new PagePreparer(pagenum);
        pagePrep.start();

        setEnabling();
    }

    /**
     * A class to pre-cache the next page for better UI response
     */
    class PagePreparer extends Thread {

        int waitforPage;
        int prepPage;

        /**
         * Creates a new PagePreparer to prepare the page after the current
         * one.
         * @param waitforPage the current page number, 0 based 
         */
        public PagePreparer(int waitforPage) {
            setDaemon(true);
            setName(getClass().getName());

            this.waitforPage = waitforPage;
            this.prepPage = waitforPage + 1;
        }

        public void quit() {
            waitforPage = -1;
        }

        public void run() {
            Dimension size = null;
            Rectangle2D clip = null;

            // wait for the current page

            if (fspp != null) {
                fspp.waitForCurrentPage();
                size = fspp.getCurSize();
                clip = fspp.getCurClip();
            } else if (page != null) {
                page.waitForCurrentPage();
                size = page.getCurSize();
                clip = page.getCurClip();
            }

            if (waitforPage == curpage) {
                // don't go any further if the user changed pages.

                PDFPage pdfPage = curFile.getPage(prepPage + 1, true);
                if (pdfPage != null && waitforPage == curpage) {
                    // don't go any further if the user changed pages


                    pdfPage.getImage(size.width, size.height, clip, null, true, true);

                }
            }
        }
    }

    /**
     * Enable or disable all of the actions based on the current state.
     */
    public void setEnabling() {
        boolean fileavailable = curFile != null;
        boolean pageshown = ((fspp != null) ? fspp.getPage() != null : page.getPage() != null);
        
        pageField.setEnabled(fileavailable);
        prevAction.setEnabled(pageshown);
        nextAction.setEnabled(pageshown);
        firstAction.setEnabled(fileavailable);
        lastAction.setEnabled(fileavailable);
    }

    /**
     * open a URL to a PDF file. The file is read in and processed
     * with an in-memory buffer.
     *
     * @param url
     * @throws java.io.IOException
     */
    public void openFile(URL url) throws IOException {
        URLConnection urlConnection = url.openConnection();
        int contentLength = urlConnection.getContentLength();
        InputStream istr = urlConnection.getInputStream();
        byte[] byteBuf = new byte[contentLength];
        int offset = 0;
        int read = 0;
        while (read >= 0) {
            read = istr.read(byteBuf, offset, contentLength - offset);
            if (read > 0) {
                offset += read;
            }
        }
        if (offset != contentLength) {
            throw new IOException("Could not read all of URL file.");
        }
        ByteBuffer buf = ByteBuffer.allocate(contentLength);
        buf.put(byteBuf);
        openPDFByteBuffer(buf, url.toString(), url.getFile());
    }

    /**
     * <p>Open a specific pdf file.  Creates a DocumentInfo from the file,
     * and opens that.</p>
     *
     * <p><b>Note:</b> Mapping the file locks the file until the PDFFile
     * is closed.</p>
     *
     * @param file the file to open
     * @throws IOException
     */
    public void openFile(File file) throws IOException {
        // first open the file for random access
        RandomAccessFile raf = new RandomAccessFile(file, "r");

        // extract a file channel
        FileChannel channel = raf.getChannel();

        // now memory-map a byte-buffer
        ByteBuffer buf =
                channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        openPDFByteBuffer(buf, file.getPath(), file.getName());
    }

    /**
     * <p>Open a specific pdf file.  Creates a DocumentInfo from the file,
     * and opens that.</p>
     *
     * <p><b>Note:</b> By not memory mapping the file its contents are
     * not locked down while PDFFile is open.</p>
     *
     * @param file the file to open
     */
    public void openFileUnMapped(File file) throws IOException {
        DataInputStream istr = null;
        try {
            //load a pdf from a byte buffer
            // avoid using a RandomAccessFile but fill a ByteBuffer directly
            istr = new DataInputStream(new FileInputStream(file));
            long len = file.length();
            if (len > Integer.MAX_VALUE) {
                throw new IOException("File too long to decode: " + file.getName());
            }
            int contentLength = (int) len;
            byte[] byteBuf = new byte[contentLength];
            int offset = 0;
            int read = 0;
            while (read >= 0) {
                read = istr.read(byteBuf, offset, contentLength - offset);
                if (read > 0) {
                    offset += read;
                }
            }
            ByteBuffer buf = ByteBuffer.allocate(contentLength);
            buf.put(byteBuf);
            openPDFByteBuffer(buf, file.getPath(), file.getName());
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (istr != null) {
                try {
                    istr.close();
                } catch (Exception e) {
                    // ignore error on close
                }
            }
        }
    }

    /**
     * open the ByteBuffer data as a PDFFile and start to process it.
     *
     * @param buf
     * @param path
     */
    private void openPDFByteBuffer(ByteBuffer buf, String path, String name) {

        // create a PDFFile from the data
        PDFFile newfile = null;
        try {
            newfile = new PDFFile(buf);
        } catch (IOException ioe) {
            openError(path + " doesn't appear to be a PDF file." +
                      "\n: " + ioe.getMessage ());
            return;
        }

        // set up our document
        this.curFile = newfile;
        docName = name;

        setEnabling();

        // display page 1.
        forceGotoPage(0);

        // if the PDF has an outline, display it.
        try {
            outline = curFile.getOutline();
        } catch (IOException ioe) {
        }
        if (outline != null) {
            if (outline.getChildCount() > 0) {
                olf = new JDialog();
                olf.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                olf.setLocation(this.getLocation());
                JTree jt = new JTree(outline);
                jt.setRootVisible(false);
                jt.addTreeSelectionListener(this);
                JScrollPane jsp = new JScrollPane(jt);
                olf.getContentPane().add(jsp);
                olf.pack();
                olf.setVisible(true);
            } else {
                if (olf != null) {
                    olf.setVisible(false);
                    olf = null;
                }
            }
        }
    }

    /**
     * Display a dialog indicating an error.
     */
    public void openError(String message) {
        JOptionPane.showMessageDialog(split, message, "Error opening file",
                JOptionPane.ERROR_MESSAGE);
    }
    /**
     * A file filter for PDF files.
     */
    FileFilter pdfFilter = new FileFilter() {

        public boolean accept(File f) {
            return f.isDirectory() || f.getName().endsWith(".pdf");
        }

        public String getDescription() {
            return "Choose a PDF file";
        }
    };
    private File prevDirChoice;

    /**
     * Ask the user for a PDF file to open from the local file system
     */
    public void doOpen() {
        try {
            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(prevDirChoice);
            fc.setFileFilter(pdfFilter);
            fc.setMultiSelectionEnabled(false);
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    prevDirChoice = fc.getSelectedFile();
                    openFile(fc.getSelectedFile());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(split,
                    "Opening files from your local " +
                    "disk is not available\nfrom the " +
                    "Java Web Start version of this " +
                    "program.\n",
                    "Error opening directory",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Open a local file, given a string filename
     * @param name the name of the file to open
     * @throws Exception 
     */
    public void doOpen(String name) throws Exception {
        try {
            @SuppressWarnings("unused")
			URL url = new URL(name);
            openFile(new URL(name));
        } catch (IOException ioe) {
            try {
                openFile(new File(name));
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new Exception();
            }
        }
    }

    /**
     * Posts the Page Setup dialog
     */
    public void doPageSetup() {
        PrinterJob pjob = PrinterJob.getPrinterJob();
        pformat = pjob.pageDialog(pformat);
    }

    /**
     * A thread for printing in.
     */
    class PrintThread extends Thread {

        PDFPrintPage ptPages;
        PrinterJob ptPjob;

        public PrintThread(PDFPrintPage pages, PrinterJob pjob) {
            ptPages = pages;
            ptPjob = pjob;
            setName(getClass().getName());
        }

        public void run() {
            try {
                ptPages.show(ptPjob);
                ptPjob.print();
            } catch (PrinterException pe) {
                JOptionPane.showMessageDialog(GedPDFViewer.this,
                        "Printing Error: " + pe.getMessage(),
                        "Print Aborted",
                        JOptionPane.ERROR_MESSAGE);
            }
            ptPages.hide();
        }
    }

    /**
     * Print the current document.
     */
    public void doPrint() {
        PrinterJob pjob = PrinterJob.getPrinterJob();
        pjob.setJobName(docName);
        Book book = new Book();
        PDFPrintPage pages = new PDFPrintPage(curFile);
        book.append(pages, pformat, curFile.getNumPages());

        pjob.setPageable(book);
        if (pjob.printDialog()) {
            new PrintThread(pages, pjob).start();
        }
    }

    /**
     * Turns off zooming; makes the page fit in the window
     */
    public void doFitInWindow() {
        if (fspp == null) {
            page.useZoomTool(false);
            page.setClip(null);
        }
    }

    /**
     * Shows or hides the thumbnails by moving the split pane divider
     */
    public void doThumbs(boolean show) {
        if (show) {
            split.setDividerLocation((int) thumbs.getPreferredSize().width +
                    (int) thumbscroll.getVerticalScrollBar().
                    getWidth() + 4);
        } else {
            split.setDividerLocation(0);
        }
    }

    /**
     * Goes to the next page
     */
    public void doNext() {
        gotoPage(curpage + 1);
    }

    /**
     * Goes to the previous page
     */
    public void doPrev() {
        gotoPage(curpage - 1);
    }

    /**
     * Goes to the first page
     */
    public void doFirst() {
        gotoPage(0);
    }

    /**
     * Goes to the last page
     */
    public void doLast() {
        gotoPage(curFile.getNumPages() - 1);
    }

    /**
     * Goes to the page that was typed in the page number text field
     */
    public void doPageTyped() {
        int pagenum = -1;
        try {
            pagenum = Integer.parseInt(pageField.getText()) - 1;
        } catch (NumberFormatException nfe) {
        }
        if (pagenum >= curFile.getNumPages()) {
            pagenum = curFile.getNumPages() - 1;
        }
        if (pagenum >= 0) {
            if (pagenum != curpage) {
                gotoPage(pagenum);
            }
        } else {
            pageField.setText(String.valueOf(curpage));
        }
    }

    /**
     * Runs the FullScreenMode change in another thread
     */
    class PerformFullScreenMode implements Runnable {

        boolean force;

        public PerformFullScreenMode(boolean forcechoice) {
            force = forcechoice;
        }

        public void run() {
            fspp = new PagePanel();
            fspp.setBackground(Color.black);
            page.showPage(null);
            fullScreen = new FullScreenWindow(fspp, force);
            fspp.addKeyListener(GedPDFViewer.this);
            gotoPage(curpage);
        }
    }

    /**
     * Handle a key press for navigation
     */
    public void keyPressed(KeyEvent evt) {
        int code = evt.getKeyCode();
        if (code == KeyEvent.VK_LEFT) {
            doPrev();
        } else if (code == KeyEvent.VK_RIGHT) {
            doNext();
        } else if (code == KeyEvent.VK_UP) {
            doPrev();
        } else if (code == KeyEvent.VK_DOWN) {
            doNext();
        } else if (code == KeyEvent.VK_HOME) {
            doFirst();
        } else if (code == KeyEvent.VK_END) {
            doLast();
        } else if (code == KeyEvent.VK_PAGE_UP) {
            doPrev();
        } else if (code == KeyEvent.VK_PAGE_DOWN) {
            doNext();
        } else if (code == KeyEvent.VK_SPACE) {
            doNext();
        }
    }

    /**
     * Combines numeric key presses to build a multi-digit page number.
     */
    class PageBuilder implements Runnable {

        int value = 0;
        long timeout;
        Thread anim;
        static final long TIMEOUT = 500;

        /** add the digit to the page number and start the timeout thread */
        public synchronized void keyTyped(int keyval) {
            value = value * 10 + keyval;
            timeout = System.currentTimeMillis() + TIMEOUT;
            if (anim == null) {
                anim = new Thread(this);
                anim.setName(getClass().getName());
                anim.start();
            }
        }

        /**
         * waits for the timeout, and if time expires, go to the specified
         * page number
         */
        public void run() {
            long now, then;
            synchronized (this) {
                now = System.currentTimeMillis();
                then = timeout;
            }
            while (now < then) {
                try {
                    Thread.sleep(timeout - now);
                } catch (InterruptedException ie) {
                }
                synchronized (this) {
                    now = System.currentTimeMillis();
                    then = timeout;
                }
            }
            synchronized (this) {
                gotoPage(value - 1);
                anim = null;
                value = 0;
            }
        }
    }
    PageBuilder pb = new PageBuilder();

    public void keyReleased(KeyEvent evt) {
    }

    /**
     * gets key presses and tries to build a page if they're numeric
     */
    public void keyTyped(KeyEvent evt) {
        char key = evt.getKeyChar();
        if (key >= '0' && key <= '9') {
            int val = key - '0';
            pb.keyTyped(val);
        }
    }

    /**
     * Someone changed the selection of the outline tree.  Go to the new
     * page.
     */
    public void valueChanged(TreeSelectionEvent e) {
        if (e.isAddedPath()) {
            OutlineNode node = (OutlineNode) e.getPath().getLastPathComponent();
            if (node == null) {
                return;
            }

            try {
                PDFAction action = node.getAction();
                if (action == null) {
                    return;
                }

                if (action instanceof GoToAction) {
                    PDFDestination dest = ((GoToAction) action).getDestination();
                    if (dest == null) {
                        return;
                    }

                    PDFObject page = dest.getPage();
                    if (page == null) {
                        return;
                    }

                    int pageNum = curFile.getPageNumber(page);
                    if (pageNum >= 0) {
                        gotoPage(pageNum);
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
