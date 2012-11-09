package fr.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.simple.ged.connector.plugins.SimpleGedPlugin;
import com.simple.ged.connector.plugins.SimpleGedPluginException;
import com.simple.ged.connector.plugins.SimpleGedPluginProperty;

/**
 * The demo plugin just copy the file designed by file_to_copy in the selected ged directory
 * 
 * @author xavier
 *
 */
public class DemoPlugin extends SimpleGedPlugin {

	@Override
	public void doGet() throws SimpleGedPluginException {
		System.out.println("Demo plugin launched...");
		
		try{
			java.io.FileInputStream sourceFile = new java.io.FileInputStream(new File(getPropertyValue("file_to_copy")));
			
			try{
				java.io.FileOutputStream destinationFile = null;
				
				try{
					destinationFile = new FileOutputStream(getDestinationFile());
					
					byte buffer[] = new byte[512 * 1024];
					int readCount;
					
					while ((readCount = sourceFile.read(buffer)) != -1){
						destinationFile.write(buffer, 0, readCount);
					}
				}
				catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
				finally {
					destinationFile.close();
				}
			} finally {
				sourceFile.close();
			}
		} catch (IOException e){
			System.out.println("DemoPlugin : fatal error :-/");
			e.printStackTrace();
			throw new SimpleGedPluginException("The demo plugin doesn't work !");
		}
		
		System.out.println("End of demo plugin");
	}
	
	
	// for testing
	public static void main(String[] arg) {
		
		// Instantiate our plugin
		SimpleGedPlugin p = new DemoPlugin();
		
		// create properties list 
		List<SimpleGedPluginProperty> properties = new ArrayList<SimpleGedPluginProperty>();
		
		// create the required properties
		SimpleGedPluginProperty file_to_copy  = new SimpleGedPluginProperty();
		file_to_copy.setPropertyKey("file_to_copy");
		file_to_copy.setPropertyValue("D:\\foo.txt");
		
		// add the property in list
		properties.add(file_to_copy);
		
		// set properties list to our plugin
		p.setProperties(properties);
		
		// define destination file for the try
		p.setDestinationFile("D:\\toto.txt");
		
		// finally, try our plugin
		try {
			p.doGet();
		} catch (SimpleGedPluginException e) {
			System.out.println("Epic fail :");
			e.printStackTrace();
		}
	}
}
