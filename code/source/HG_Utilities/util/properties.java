package HG_Utilities.util;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Set;
// --- <<IS-END-IMPORTS>> ---

public final class properties

{
	// ---( internal utility methods )---

	final static properties _instance = new properties();

	static properties _newInstance() { return new properties(); }

	static properties _cast(Object o) { return (properties)o; }

	// ---( server methods )---




	public static final void getPropertiesList (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getPropertiesList)>> ---
		// @sigtype java 3.5
		// [i] field:1:required keys
		// [o] field:1:required values
		
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String[]	keys = IDataUtil.getStringArray( pipelineCursor, "keys" );
		pipelineCursor.destroy();
		
		if (keys == null || keys.length == 0) {
		    throw new ServiceException("Input 'keys' cannot be null or empty.");
		}
		
		// Initialize an array for storing corresponding values
		String[] values = new String[keys.length];
		 // Loop through the keys and retrieve the values from globalProperties
		for (int i = 0; i < keys.length; i++) {
		    String key = keys[i];
		    String value = globalProperties.getProperty(key);
		    values[i] = (value != null) ? value : ""; // Handle missing keys
		}
		
		// pipeline
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		IDataUtil.put( pipelineCursor_1, "values", values );
		pipelineCursor_1.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void getProperty (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getProperty)>> ---
		// @sigtype java 3.5
		// [i] field:0:required key
		// [o] field:0:required value
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	key = IDataUtil.getString( pipelineCursor, "key" );
		pipelineCursor.destroy();
		String value = null;
		if (globalProperties != null) {
		    value = globalProperties.getProperty(key);
		}
		// pipeline
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		IDataUtil.put( pipelineCursor_1, "value", value );
		pipelineCursor_1.destroy();
			
		// --- <<IS-END>> ---

                
	}



	public static final void loadProperties (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(loadProperties)>> ---
		// @sigtype java 3.5
		// [i] field:0:required packageName
		// [o] field:0:required returnMessage
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	packageName = IDataUtil.getString( pipelineCursor, "packageName" );
		pipelineCursor.destroy();
		String returnMessage;
		
		//Get server Path
		IData output = IDataFactory.create();
		
		try {
		    // Retrieve the path to the packages directory
		    output = Service.doInvoke("wm.server.query", "getServerPaths", null);
		} catch (Exception e) {
		    throw new ServiceException(e);
		}
		// Extract the packages directory path
				IDataCursor outputCursor = output.getCursor();
				String packagesDir = IDataUtil.getString(outputCursor, "packagesDir");
				outputCursor.destroy();
				
				// Construct the properties file path
				String tempPropertyFilePath =packagesDir + "\\" + packageName + "\\config\\" + packageName + ".properties";
		
		
				// Create a static Properties object to store the configurations
			    if (globalProperties == null) {
			        globalProperties = new Properties();
			    }
		
			    try (FileInputStream fis = new FileInputStream(tempPropertyFilePath)) {
			        // Load the properties file into memory
			        globalProperties.load(fis);
			        returnMessage="Config loaded successfully!";
			    } catch (Exception e) {
			        throw new ServiceException("Error loading properties file: " + e.getMessage());
			    }
		// pipeline
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		IDataUtil.put( pipelineCursor_1, "returnMessage", returnMessage+" :"+packagesDir+" :"+tempPropertyFilePath );
		pipelineCursor_1.destroy();			
		// --- <<IS-END>> ---

                
	}



	public static final void viewAllProperties (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(viewAllProperties)>> ---
		// @sigtype java 3.5
		// [o] field:1:required kayValuePair
		// Check if globalProperties is loaded
		if (globalProperties == null || globalProperties.isEmpty()) {
		    throw new ServiceException("No properties are loaded in memory. Please call loadConfig first.");
		}
		
		// Get all keys from globalProperties
		Set<Object> keys = globalProperties.keySet();
		String[] keyValuePairs = new String[keys.size()];
		
		// Iterate through all keys and build key=value pairs
		int index = 0;
		for (Object key : keys) {
		    String value = globalProperties.getProperty((String) key);
		    keyValuePairs[index++] = key + "=" + value;
		}
		
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
		IDataUtil.put( pipelineCursor, "kayValuePair", keyValuePairs );
		pipelineCursor.destroy();
		
			
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	private static Properties globalProperties = new Properties();
	// --- <<IS-END-SHARED>> ---
}

