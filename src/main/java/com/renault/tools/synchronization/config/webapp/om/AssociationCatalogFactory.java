package com.renault.tools.synchronization.config.webapp.om;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.renault.tools.synchronization.config.webapp.association.Association;
import com.renault.tools.synchronization.config.webapp.association.impl.AssociationKey;
import com.renault.tools.synchronization.config.webapp.report.ConfigReport;
import com.renault.tools.synchronization.config.webapp.report.EmptyFileReport;
import com.renault.tools.synchronization.config.webapp.report.PropReport;
import com.renault.tools.synchronization.config.webapp.report.SyncReport;

public class AssociationCatalogFactory {

    public static AssociationCatalog createAssociationCatalogFrom(ListFeed listFeed, int jump) {
        
        AssociationCatalog associationCatalog = new AssociationCatalog();
        
        int numline = 1;
        for (ListEntry googleRow : listFeed.getEntries()) {
            
            if (numline <= jump) {
                numline++;
                continue;
            }

            String label = googleRow.getCustomElements().getValue("label");
            String configname = googleRow.getCustomElements().getValue("configurationname");
            String filename = googleRow.getCustomElements().getValue("filename");
            String unrefPropertyScope = googleRow.getCustomElements().getValue("propertyscope");
            String refPropertyScope = googleRow.getCustomElements().getValue("referencepropertyscope");
            String unrefPropertyName = googleRow.getCustomElements().getValue("propertyname");
            String refPropertyName = googleRow.getCustomElements().getValue("referencepropertyname");
            String unrefPropertyValue = googleRow.getCustomElements().getValue("propertyvalue");
            String refPropertyValue = googleRow.getCustomElements().getValue("referencepropertyvalue");
            
            AssociationItem associationItem = AssociationItemFactory.createAssociationItem(label, configname, filename, unrefPropertyScope, refPropertyScope, unrefPropertyName, refPropertyName, unrefPropertyValue, refPropertyValue);

            String propertyName = getPropertyName(unrefPropertyName, refPropertyName);
            AssociationKey key = AssociationKey.create(label, configname, filename, propertyName);
            associationCatalog.putAssociationItem(key, associationItem);
            
            numline++;
        }
        
        return associationCatalog;        
    }

    private static String getPropertyName(String unrefPropertyName, String refPropertyName) {
        if (unrefPropertyName != null) {
            return unrefPropertyName;
        }
        if (refPropertyName != null) {
            return refPropertyName;
        }
        return "NONE";
    }
    
    public static AssociationCatalog createAssociationCatalogFrom(File csvFile, String csvDelimiter, int jump) {
        return load(csvFile, csvDelimiter, jump);
    }

	public static AssociationCatalog createAssociationCatalogFrom(SyncReport syncReport) {
        
		AssociationCatalog associationCatalog = new AssociationCatalog();
		
        List<ConfigReport> configReportList = syncReport.getConfigReports();  
        for (ConfigReport configReport : configReportList) {

            String configname = configReport.getConfigname();

            List<EmptyFileReport> noFileReportList = configReport.getNoFileReports();  
            for (EmptyFileReport noFileReport : noFileReportList) {
                
                String label = noFileReport.getLabel();
                String filename = noFileReport.getFilename();
                
                AssociationKey key = AssociationKey.create(label, configname, filename, "NONE");
                AssociationItem associationItem = AssociationItemFactory.createEmptyAssociationItem(label, configname, filename);
                associationCatalog.putAssociationItem(key, associationItem);
            }

            List<PropReport> propReportList = configReport.getPropReports();
            for (PropReport propReport : propReportList) {
                
                String label = propReport.getLabel();
                String filename = propReport.getFilename();
                if (!configReport.containsFile(filename)) {
                    continue;
                }

                // in unref config file
                List<Association> unrefAssociations = propReport.getUnrefAssociations();
                for (Association association : unrefAssociations) {
                    AssociationKey key = AssociationKey.create(label, configname, filename, association.getPropertyName());
                    AssociationItem associationItem = new UnrefAssociationItem(label, configname, filename, association);
                    associationCatalog.putAssociationItem(key, associationItem);
                }
    
                // in ref config file
                List<Association> refAssociations = propReport.getRefAssociations();
                for (Association association : refAssociations) {
                    AssociationKey key = AssociationKey.create(label, configname, filename, association.getPropertyName());
                    AssociationItem associationItem = new RefAssociationItem(label, configname, filename, association);
                    associationCatalog.putAssociationItem(key, associationItem);
                }

                // in both files
            	List<Association> completeAssociations = propReport.getCompleteAssociations();
                for (Association association : completeAssociations) {
                    AssociationKey key = AssociationKey.create(label, configname, filename, association.getPropertyName());
                    AssociationItem associationItem = new CompleteAssociationItem(label, configname, filename, association);
                    associationCatalog.putAssociationItem(key, associationItem);
                }
            }
        }
        return associationCatalog;
	}
	
    @SuppressWarnings("unchecked")
    private static AssociationCatalog load(File csvFile, String csvDelimiter, int jump) {
        
        AssociationCatalog associationCatalog = null;
        try {
            
            List<String> lines = FileUtils.readLines(csvFile);
            for (int i=0;i<jump;i++) {
                lines.remove(0);
            }
            associationCatalog = createAssociationCatalogFrom(lines, csvDelimiter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return associationCatalog;
    }
    
    public static AssociationCatalog createAssociationCatalogFrom(List<String> lines, String csvDelimiter) {
        
        AssociationCatalog associationCatalog = new AssociationCatalog();

        for (String line : lines) {            
            line = StringUtils.trimToNull(line);

            if (line == null) {
                continue;
            }
            
            AssociationItem associationItem = AssociationItemFactory.createAssociationItem(csvDelimiter, line);
            
            String label = associationItem.getLabel();
            String configname = associationItem.getConfigname();
            String filename = associationItem.getFilename();
            String propertyName = associationItem.getPropertyName();
            AssociationKey key = AssociationKey.create(label, configname, filename, propertyName);
            
            associationCatalog.putAssociationItem(key, associationItem);
        }
        
        return associationCatalog;
    }
}
