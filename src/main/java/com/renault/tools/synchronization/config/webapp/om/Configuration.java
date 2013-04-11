package com.renault.tools.synchronization.config.webapp.om;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;


public class Configuration {

    private String configDirname;

    private String label;

	private String configName;
	
	private Map<String, Properties> propertiesMap = new HashMap<String, Properties>();

	private Map<String, Properties> commonPropertiesMap = new HashMap<String, Properties>();

    private Map<String, String> commonAbsoluteNames = new HashMap<String, String>();

	Configuration(String configDirname, String label, String configName) {
	    this.configDirname = configDirname;
	    this.label = label;
	    this.configName = configName;
	}

    public String getConfigDirname() {
        return configDirname;
    }

    public String getLabel() {
        return label;
    }
    
	public String getConfigName() {
		return configName;
	}
	
	public void putPropertiesFile(String filename, File propertiesFile) {
		Properties properties = loadProperties(propertiesFile.getAbsolutePath());
		if (isCommonPropertiesFile(propertiesFile)) {
			putCommonPropertiesFile(propertiesFile);
		}
		propertiesMap.put(filename, properties);
	}

	public void putCommonPropertiesFile(String filename, File propertiesFile) {
	    String absolutePath = propertiesFile.getAbsolutePath();
		Properties properties = loadProperties(propertiesFile.getAbsolutePath());
		commonPropertiesMap.put(filename, properties);
		commonAbsoluteNames.put(filename, absolutePath);
	}
	
	private boolean isCommonPropertiesFile(File propertiesFile) {
		return "common.properties".equals(propertiesFile.getName());
	}
	
	private void putCommonPropertiesFile(File propertiesFile) {
        String absolutePath = propertiesFile.getAbsolutePath();
		Properties properties = loadProperties(absolutePath);
		String pathfile = propertiesFile.getPath();
		commonPropertiesMap.put(pathfile, properties);
        commonAbsoluteNames.put(pathfile, absolutePath);
	}
	
	public boolean containsPropertiesFile(String key) {
		return propertiesMap.containsKey(key);
	}

	public Properties getProperties(String key) {
		return propertiesMap.get(key);
	}

	public Set<String> getPropertiesFileNames() {
		return propertiesMap.keySet();
	}

    public PropertyContainer getPropertyContainer(String filename, String propertyName) {
        
        Scope scope = getScope(filename, propertyName);
        if (scope == null) {
            return null;
        }
        return PropertyContainer.createProperty(propertyName, scope);
    }

    private Scope getScope(String filename, String propertyName) {
        
        Properties localProperties = propertiesMap.get(filename);
        if (localProperties.containsKey(propertyName)) {
            String propertyValue = (String) localProperties.get(propertyName);
            if ("common.properties".equals(filename)) {               
                return ScopeFactory.buildLocalCommonScope(propertyValue);               
            }
            return ScopeFactory.buildLocalScope(propertyValue);
        }

        for (Entry<String, Properties> entry : commonPropertiesMap.entrySet()) {
            String absolutePath = commonAbsoluteNames.get(entry.getKey());
            Properties commonProperties = entry.getValue();            
            if (commonProperties.containsKey(propertyName)) {
                String propertyValue = (String) commonProperties.get(propertyName);                
                return ScopeFactory.buildCommonScope(absolutePath, propertyValue);
            }
        }
        return null;
    }

	public Set<PropertyContainer> getPropertyContainers(String filename) {
	    
		Properties properties = propertiesMap.get(filename);
		Iterator<Entry<Object, Object>> ite = properties.entrySet().iterator();
		
		Set<PropertyContainer> result = new HashSet<PropertyContainer>();
		while (ite.hasNext()) {
		    Entry<Object, Object> entry = ite.next();
		    String propertyName = entry.getKey().toString();
		    PropertyContainer propertyContainer = getPropertyContainer(filename, propertyName);
		    if (propertyContainer != null) {
		        result.add(propertyContainer);
		    }
		}
		return result;
	}

	private Properties loadProperties(String filename) {
	
        Properties properties = new Properties();
        try {
            File file = new File(filename);
            File tmpfile = createTmpPropertiesFile(FileUtils.openInputStream(file));
            properties.load(FileUtils.openInputStream(tmpfile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File " + filename + " for configuration " + configName + " not found.", e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
	}

	/**
	 * Used to double backslash char. 
	 * 
	 * @param fin
	 * @return
	 * @throws Exception
	 */
	private File createTmpPropertiesFile(FileInputStream fin) throws Exception {
    
    	int ch;
    	StringBuilder sb = new StringBuilder();

    	try {
    	    while ((ch = fin.read()) != -1) {
    	        char c = (char) ch;
    	        if (c == '\\') {
    	            sb.append("\\\\");
    	        } else {
                    sb.append(c);    	            
    	        }
        	}
	    } finally {
	        fin.close();
	    }
    	
        File tmpfile = File.createTempFile("work", ".properties");      
    	FileUtils.writeStringToFile(tmpfile, sb.toString());
    	return tmpfile;
	}
}
