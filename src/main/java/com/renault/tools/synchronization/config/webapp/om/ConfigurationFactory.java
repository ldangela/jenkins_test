package com.renault.tools.synchronization.config.webapp.om;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;


public class ConfigurationFactory {

    public static Configuration buildBasicConfiguration(String label, String configName, String basedir, String configDirname) {
        return buildConfiguration(label, configName, basedir, configDirname);
    }
    
	public static Configuration buildBackofficeConfiguration(String label, String configName, String basedir, String configDirname) {
		Configuration configuration = buildConfiguration(label, configName, basedir, configDirname);
		configuration.putCommonPropertiesFile("shared.properties", new File(basedir + "/eplatform/infrastructure/backoffice/backoffice-webapp/src/main/webapp/WEB-INF/shared/shared.properties"));
		configuration.putCommonPropertiesFile("common.properties", new File(basedir + "/eplatform/infrastructure/backoffice/backoffice-webapp/src/main/webapp/WEB-INF/config/common.properties"));
		configuration.putCommonPropertiesFile("common-cas.properties", new File(basedir + "/eplatform/infrastructure/backoffice/backoffice-webapp/src/main/webapp/WEB-INF/config/common-cas.properties"));
		return configuration;
	}

	public static Configuration buildFrontofficeConfiguration(String label, String configName, String basedir, String configDirname) {
		Configuration configuration = buildConfiguration(label, configName, basedir, configDirname);
		configuration.putCommonPropertiesFile("shared.properties", new File(basedir + "/eplatform/infrastructure/frontoffice/frontoffice-webapp/src/main/webapp/WEB-INF/shared/shared.properties"));
		return configuration;
	}

	/**
	 * Return a a generic configuration object.  
	 * 
	 * @param configName
	 * @param basedir
	 * @param configDirname
	 * @return
	 */
	public static Configuration buildConfiguration(String label, String configName, String basedir, String configDirname) {
    	Configuration configuration = new Configuration(configDirname, label, configName);
    	
    	Collection<File> files = getPropertiesFiles(basedir + configDirname);
    	for (File file : files) {
    		String key = file.getName();
    		configuration.putPropertiesFile(key, file);
    	}
    	return configuration;
    }
	
    /**
     * Return the list of properties files (except for the file common.properties)
     * 
     * @param dirname
     * @return
     */
    @SuppressWarnings("unchecked")
	private static Collection<File> getPropertiesFiles(String dirname) {
    	File dir = new File(dirname);
    	if (!dir.exists() || !dir.isDirectory()) {
    		throw new IllegalArgumentException(dir + " doesn't exist or is not a directory."); 
    	}
    	return FileUtils.listFiles(dir, new String[] {"properties"}, false);
    }

}