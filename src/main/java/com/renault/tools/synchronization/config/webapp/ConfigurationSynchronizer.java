package com.renault.tools.synchronization.config.webapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.renault.tools.synchronization.config.webapp.association.Association;
import com.renault.tools.synchronization.config.webapp.association.impl.CompleteAssociation;
import com.renault.tools.synchronization.config.webapp.association.impl.AssociationKey;
import com.renault.tools.synchronization.config.webapp.association.impl.RefAssociation;
import com.renault.tools.synchronization.config.webapp.association.impl.UnrefAssociation;
import com.renault.tools.synchronization.config.webapp.om.Configuration;
import com.renault.tools.synchronization.config.webapp.om.PropertyContainer;
import com.renault.tools.synchronization.config.webapp.report.ConfigReport;
import com.renault.tools.synchronization.config.webapp.report.EmptyFileReport;
import com.renault.tools.synchronization.config.webapp.report.PropReport;
import com.renault.tools.synchronization.config.webapp.report.SyncReport;
import com.renault.tools.synchronization.config.webapp.report.Synchronizable;

public class ConfigurationSynchronizer {

    private Map<String, Configuration> configurationMap = new HashMap<String, Configuration>();
    
    private Configuration referenceConfiguration;

    public ConfigurationSynchronizer(List<Configuration> configurations, Configuration referenceConfiguration) {
    	loadConfigurations(configurations);
    	this.referenceConfiguration = referenceConfiguration;
    }
    
    private void loadConfigurations(List<Configuration> configurations) {
    	for (Configuration configuration : configurations) {
    		String configName = configuration.getConfigName();
    		configurationMap.put(configName, configuration);
    	}
    }
    
    public SyncReport synchronize() {
        SyncReport syncReport = new SyncReport();
    	for (Configuration configuration : configurationMap.values()) {
    	    compareConfiguration(syncReport, configuration, referenceConfiguration);
    	}
    	return syncReport;
    }
    
    /**
     * Compare a configuration with reference configuration.
     * 
     * @param configuration
     * @param referenceConfiguration
     */
    private void compareConfiguration(SyncReport syncReport, Configuration configuration, Configuration referenceConfiguration) {

        String label = configuration.getLabel();
    	ConfigReport configReport = new ConfigReport(configuration.getConfigName(), referenceConfiguration.getConfigName());
    	Set<String> filenames = referenceConfiguration.getPropertiesFileNames();
    	for (String filename : filenames) {
    		if (configuration.containsPropertiesFile(filename)) {
    			compareProperties(configReport, label, filename, configuration, referenceConfiguration);
    		} else {
    			Synchronizable noneFileReport = new EmptyFileReport(label, configuration.getConfigName(), filename);
        		configReport.addNoFileReport(noneFileReport);
    		}
    	}
    	syncReport.addConfigurationSynchronizationReport(configReport);
    }

    /**
     * Compare a properties file with his reference properties file. 
     * 
     * @param configReport
     * @param properties
     * @param referenceProperties
     */
    private void compareProperties(ConfigReport configReport, String label, String filename, Configuration configuration, Configuration referenceConfiguration) {
    	
        PropReport propReport = new PropReport(label, filename);

        String configname = referenceConfiguration.getConfigName();
    	Set<PropertyContainer> referenceProperties = referenceConfiguration.getPropertyContainers(filename);
        for (PropertyContainer referencePropertyContainer : referenceProperties) {
            String referencePropertyName = referencePropertyContainer.getName();
        	AssociationKey key = AssociationKey.create(label, configname, filename, referencePropertyName);
            PropertyContainer propertyContainer = configuration.getPropertyContainer(filename, referencePropertyName);
            if (propertyContainer == null) {
            	Association propertyAssociation = new RefAssociation(referencePropertyContainer);
                propReport.putPropertyAssociation(key, propertyAssociation);
            } else {
            	Association propertyAssociation = new CompleteAssociation(propertyContainer, referencePropertyContainer);
                propReport.putPropertyAssociation(key, propertyAssociation);
            }
        }

    	Set<PropertyContainer> properties = configuration.getPropertyContainers(filename);
        for (PropertyContainer propertyContainer : properties) {
            String propertyName = propertyContainer.getName();
        	AssociationKey key = AssociationKey.create(label, configname, filename, propertyName);
            PropertyContainer referencePropertyContainer = referenceConfiguration.getPropertyContainer(filename, propertyName);
            if (referencePropertyContainer == null) {
            	Association propertyAssociation = new UnrefAssociation(propertyContainer);
                propReport.putPropertyAssociation(key, propertyAssociation);
            } else {
            	Association propertyAssociation = new CompleteAssociation(propertyContainer, referencePropertyContainer);
                propReport.putPropertyAssociation(key, propertyAssociation);
            }
        }
        
        configReport.addPropertiesReport(propReport);
    }
}
