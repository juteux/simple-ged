package com.ged.ui.preview_widgets;

import java.awt.Dimension;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import org.apache.log4j.Logger;

import com.ged.Profile;
import com.ged.tools.PrintingHelper;
import com.sun.pdfview.GedPDFViewer;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPrintPage;


/**
 * Previewer for PDF files
 * @author xavier
 *
 */
public class PdfFilePreviewer extends AbstractDocumentPreviewer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(PdfFilePreviewer.class);
	
	/**
	 * Maximum widget size
	 */
	private Dimension maximumSize;
	
	
	public PdfFilePreviewer(String absoluteFilePath, Dimension maximumSize) {
		super(absoluteFilePath);
		this.maximumSize = maximumSize;
	}

	@Override
	public void load() throws CannotCreatePreviewerException {
        final GedPDFViewer viewer = new GedPDFViewer(maximumSize);
        try {
			viewer.doOpen(getAbsoluteFilePath());
		} catch (Exception e) {
			throw new CannotCreatePreviewerException();
		}
        add(viewer);
        
        // show first page quick fix
        new Thread(new Runnable() {	
			@Override
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				} finally {
					viewer.doFirst();
				}
			}
		}).start();
	}

	@Override
	public boolean isPrintable() {
		return true;
	}
	
	@Override
	public void print() {
		
		try {
			File input = new File(absoluteFilePath);
			FileInputStream fis = new FileInputStream(input);
			FileChannel fc = fis.getChannel();
			ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
	
			PDFFile curFile=null;
			PDFPrintPage pages=null;
			curFile = new PDFFile(bb); // Create PDF Print Page
			pages = new PDFPrintPage(curFile);
			PrinterJob pjob = PrinterJob.getPrinterJob();
	
			PrintService[] services = PrinterJob.lookupPrintServices();
			for(PrintService ps:services){
			    String pName = ps.getName();
			    if(pName.equals(Profile.getInstance().getDefaultPrinterName())){
			        pjob.setPrintService(ps);
			        logger.info("Printing on : " + pName);
			        break;
			    }
			}
	
			pjob.setJobName(absoluteFilePath);
			Book book = new Book();
			PageFormat pformat = PrinterJob.getPrinterJob().defaultPage();
			book.append(pages, pformat, curFile.getNumPages());
			pjob.setPageable(book);
	
			// print
			PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
	
	
			// Print it
			pjob.print(aset);
			
		} catch (Exception e) {
			PrintingHelper.showPrintPopupErrorMessage("Impossible d'imprimer le fichier : " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
}

