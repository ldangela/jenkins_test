package com.renault.tools.synchronization.config.webapp.om;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.renault.tools.synchronization.config.webapp.association.impl.AssociationKey;
import com.renault.tools.synchronization.config.webapp.report.renderer.impl.FileCsvRenderer;

public class AssociationCatalog implements AssociationComponent {

	Map<AssociationKey, AssociationItem> associationItemMap = Maps.newHashMap();
	
	AssociationCatalog() {
	    
	}
	
	public List<AssociationItem> getSortedAssociationItems() {
		 List<AssociationItem> associationItems = Lists.newArrayList(associationItemMap.values());
  	   	 Collections.sort(associationItems , new ItemComparator()); 
		 return associationItems;
	}
	
	public void putAssociationItem(AssociationKey key, AssociationItem associationItem) {
		associationItemMap.put(key, associationItem);
	}
    
    public AssociationItem containsAssociationItem(AssociationKey key) {
        return associationItemMap.get(key);
    }
    
    public AssociationItem containsAssociationItem(String label, String configname, String filename, String propertyName) {
    	AssociationKey key = AssociationKey.create(label, configname, filename, propertyName);
    	return associationItemMap.get(key);
    }

    private class ItemComparator implements Comparator<AssociationItem> {

        public int compare(AssociationItem association1, AssociationItem association2) {
            int c = association1.getLabel().compareTo(association2.getLabel());
            if (c == 0) {
                c = association1.getConfigname().compareTo(association2.getConfigname());
            }
            if (c == 0) {
               c = association1.getFilename().compareTo(association2.getFilename());
            }
            if (c == 0) {
	            c = Integer.valueOf(association1.getWeight()).compareTo(association2.getWeight());
            }
            return c;
        }

    }

    public void saveAll(File csvFile) {
        StringBuilder sb = new StringBuilder();
        FileCsvRenderer.createCompleteFileCsvRender(sb, this).render();
        String content = sb.toString();
        try {
            FileUtils.writeStringToFile(csvFile, content, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Cannot save associationCatalog in " + csvFile.getAbsolutePath(), e);
        }
    }
    
    public void saveDelta(File csvFile) {
        StringBuilder sb = new StringBuilder();
        FileCsvRenderer.createDeltaFileCsvRender(sb, this).render();
        String content = sb.toString();
        try {
            FileUtils.writeStringToFile(csvFile, content, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Cannot save delta associationCatalog in " + csvFile.getAbsolutePath(), e);
        }
    }
}