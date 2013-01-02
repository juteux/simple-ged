package com.simple.ged.update;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides tools for an easiest update =)
 * 
 * @author xavier
 *
 */
public final class UpdateHelper {

	private static final Logger logger = LoggerFactory.getLogger(UpdateHelper.class);
	
	
	/**
	 * Should not be instantiated
	 */
	private UpdateHelper() {
	}
	
	
	/**
	 * Get the version number in the hosted xml descriptor
	 * 
	 * @param address
	 * 				The address of the online xml
	 * 
	 * @return
	 * 				The version, as the string but should be a float or "0" if the connection failed
	 */
	public static String getVersionNumber(String address) {
		
		String onlineVersion = "0";
		
		try {
			URL xmlUrl = new URL(address);
			
			URLConnection urlConnection = xmlUrl.openConnection();
			urlConnection.setUseCaches(false);
			
			urlConnection.connect();
			
			InputStream stream = urlConnection.getInputStream();
						
			SAXBuilder sxb = new SAXBuilder();
			Document xmlDocument = null;
			
			try {
				xmlDocument = sxb.build(stream);
			} catch (Exception e) {
				logger.error("Could not parse xml document " + address, e);
			}
			
			// get root
			Element root = xmlDocument.getRootElement();
			
			// get online version, should be only one loop
			for (Object child : root.getChildren("number")) {
				onlineVersion = ((Element)child).getText();
			}
			
		} catch (Exception e) {
			logger.error("Could not get xml document" + address, e);
		}

		return onlineVersion;
	}
	
	
	/**
	 * Get the list of files to download which are listed in the online xml
	 * 
	 * 
	 * @return
	 * 			The map of files to download as <url, file>
	 */
	public static Map<String, String> getFilesToDownloadMap(String address) {
		Map<String, String> fileToDownload = new HashMap<String, String>();
		
		try {
			URL xmlUrl = new URL(address);
			
			URLConnection urlConnection = xmlUrl.openConnection();
			urlConnection.setUseCaches(false);
			
			urlConnection.connect();
			
			InputStream stream = urlConnection.getInputStream();
						
			SAXBuilder sxb = new SAXBuilder();
			Document xmlDocument = null;
			
			try {
				xmlDocument = sxb.build(stream);
			} catch (Exception e) {
				logger.error("Could not parse xml file", e);
			}
			
			// get root
			Element root = xmlDocument.getRootElement();
			
			Element files = root.getChild("files");
			
			// get online version, should be only one loop
			for (Object child : files.getChildren("file")) {
				Element file = (Element)child;
				
				String url  = file.getChildText("url");
				String dest = file.getChildText("destination");
	
				fileToDownload.put(url, dest);
			}
			
		} catch (Exception e) {
			logger.error("Could not get file list", e);
		}
		
		return fileToDownload;
	}
	
	
	/**
	 * Download and replace file
	 * 
	 * @see http://baptiste-wicht.developpez.com/tutoriels/java/update/
	 */
	public static void downloadAndReplaceFile(String onlineFileUrl, String localFilePath) {
		URLConnection connection = null;
		InputStream is = null;
		FileOutputStream destinationFile = null;
		
		try { 
	        URL url = new URL(onlineFileUrl);
			connection = url.openConnection( );
	        
			int length = connection.getContentLength();

			if(length == -1){
				throw new IOException("Fichier vide");
	       	}

			is = new BufferedInputStream(connection.getInputStream());

			byte[] data = new byte[length];

			int currentBit = 0;
			int deplacement = 0;
			
			while(deplacement < length){
				currentBit = is.read(data, deplacement, data.length-deplacement);	
				if(currentBit == -1) {
					break;	
				}
				deplacement += currentBit;
			}

			if(deplacement != length){
				throw new IOException("Le fichier n'a pas été lu en entier (seulement " 
					+ deplacement + " sur " + length + ")");
			}		
		
			destinationFile = new FileOutputStream(localFilePath); 

			destinationFile.write(data);

			destinationFile.flush();

	      } catch (MalformedURLException e) { 
	    	  logger.error("Wrong url : " + onlineFileUrl, e); 
	      } catch (IOException e) { 
	    	  logger.error("Could not read source file", e);
	      } finally {
	    	  try {
	    		  is.close();
				  destinationFile.close();
	    	  } catch (IOException e) {
	    		  logger.error("Could not write target file", e);
	    	  }
	      }
	}
	
}
