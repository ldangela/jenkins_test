
package com.renault.tools.synchronization.config.webapp.report;

import java.util.ArrayList;
import java.util.List;


public class ConfigReport implements Synchronizable {

    private String configname;
    
    private String referenceConfigName;

    private List<Synchronizable> noFileReports = new ArrayList<Synchronizable>();

    private List<Synchronizable> propReports = new ArrayList<Synchronizable>();

    private List<Synchronizable> scopeReports = new ArrayList<Synchronizable>();

    public ConfigReport(String configName, String referenceConfigName) {
    	this.configname = configName;
    	this.referenceConfigName = referenceConfigName;    	
    }
    
    public void addNoFileReport(Synchronizable report) {
        noFileReports.add(report);
    }
    
    public void addPropertiesReport(Synchronizable report) {
    	propReports.add(report);
    }

    public void addScopeReport(Synchronizable report) {
        scopeReports.add(report);
    }
    
    @Override
    public boolean isSynchronized() {
    	for (Synchronizable propReport : propReports) {
    		boolean result = propReport.isSynchronized();
    		if (!result) {
    			return false;
    		}
    	}
    	return true;
    }
    
    public String getConfigname() {
        return configname;
    }
    
    public String getReferenceConfigname() {
        return referenceConfigName;
    }
    
    public List<PropReport> getPropReports() {
        List<PropReport> result = new ArrayList<PropReport>();
        for (Synchronizable propReport : propReports) {
            result.add((PropReport) propReport);
        }
        return result;
    }

    public List<EmptyFileReport> getNoFileReports() {
        List<EmptyFileReport> result = new ArrayList<EmptyFileReport>();
        for (Synchronizable noFileReport : noFileReports) {
            result.add((EmptyFileReport) noFileReport);
        }
        return result;
    }
    
    public boolean containsFile(String filename) {
        for (Synchronizable noFileReport : noFileReports) {
            String f = ((EmptyFileReport)noFileReport).getFilename();
            if (filename.equals(f)) {
                return false;                
            }
        }
        return true; 
    }
}
