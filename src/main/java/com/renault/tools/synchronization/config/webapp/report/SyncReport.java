package com.renault.tools.synchronization.config.webapp.report;

import java.util.ArrayList;
import java.util.List;


public class SyncReport implements Synchronizable {

    private List<Synchronizable> confReports = new ArrayList<Synchronizable>();
        
    public void addConfigurationSynchronizationReport(Synchronizable report) {
    	confReports.add(report);
    }

    @Override
    public boolean isSynchronized() {
    	for (Synchronizable propReport : confReports) {
    		boolean result = propReport.isSynchronized();
    		if (!result) {
    			return false;
    		}
    	}
    	return true;
    }
    
    public List<ConfigReport> getConfigReports() {
        List<ConfigReport> result = new ArrayList<ConfigReport>();
        for (Synchronizable confReport : confReports) {
            result.add((ConfigReport) confReport);
        }
        return result;
    }
}
