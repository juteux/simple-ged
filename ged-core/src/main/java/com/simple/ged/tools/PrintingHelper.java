package com.simple.ged.tools;

import java.io.FileInputStream;
import java.util.Properties;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.Profile;

import fr.xmichel.javafx.dialog.Dialog;
import fr.xmichel.toolbox.tools.PropertiesHelper;

/**
 * This class is a printing helper
 * 
 * @author xavier
 *
 */
public class PrintingHelper {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(PrintingHelper.class);
	
	/**
	 * Properties
	 */
	private static final Properties properties = PropertiesHelper.getInstance().getProperties();
	
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
				logger.error("Cannot print file : no PrintService detected");
				Dialog.showError(properties.getProperty("error"), "Impossible d'imprimer, aucune imprimante compatible n'est détectée !");
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
			logger.error("Cannot print file", e);
			Dialog.showThrowable(properties.getProperty("error"), "Impossible de lancer l'impression du document", e);
		}
	}
	
}
