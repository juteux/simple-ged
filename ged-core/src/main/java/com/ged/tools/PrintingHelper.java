package com.ged.tools;

import java.io.FileInputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.ged.Profile;

/**
 * This class is a printing helper
 * 
 * @author xavier
 *
 */
public class PrintingHelper {

	
	private static final Logger logger = Logger.getLogger(PrintingHelper.class);
	
	
	/**
	 * This method print the given document 
	 * 
	 * It use the default printer
	 * 
	 * @param filePath
	 * 			The document file path
	 * @param documentType
	 * 			The file type
	 */
	public static void printFile(String filePath, DocFlavor documentType) {
		
		try {
			PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
			pras.add(new Copies(1));

			PrintService pss[] = PrintServiceLookup.lookupPrintServices(documentType, pras);

			if (pss.length == 0) {
				showPrintPopupErrorMessage("Impossible d'imprimer, aucune imprimante compatible n'est détectée !");
				return;
			}
				
			PrintService ps = pss[0];
			for (PrintService p : pss) {
				if (p.getName().equals(Profile.getInstance().getDefaultPrinterName())) {
					ps = p;
					break;
				}
			}
			logger.info("Printing to " + ps);

			DocPrintJob job = ps.createPrintJob();

			FileInputStream fin = new FileInputStream(filePath);
			Doc doc = new SimpleDoc(fin, documentType, null);

			job.print(doc, pras);

			fin.close();
		} catch (Exception e) {
			showPrintPopupErrorMessage("Impossible de lancer l'impression du document !");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public static void showPrintPopupErrorMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Erreur", JOptionPane.ERROR_MESSAGE);
	}
	
}
