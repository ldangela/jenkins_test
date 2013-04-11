package com.renault.tools.synchronization.config.webapp.report;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.renault.tools.synchronization.config.webapp.association.Association;
import com.renault.tools.synchronization.config.webapp.association.impl.AssociationKey;

public class PropReport implements Synchronizable {

    private String label;

    private String filename;

    private Map<AssociationKey, Association> propertyAssociationsMap = Maps.newHashMap();

    public PropReport(String label, String filename) {
        this.label = label;
        this.filename = filename;
    }
    
    public boolean containsPropertyAssociation(String propertyName) {
    	Collection<Association> propertyAssociations = propertyAssociationsMap.values();
    	for (Association propertyAssociation : propertyAssociations) {
    		if (propertyAssociation.getPropertyName().equals(propertyName)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public void putPropertyAssociation(AssociationKey key, Association propertyAssociation) {
       	propertyAssociationsMap.put(key, propertyAssociation);
    }

    @Override
    public boolean isSynchronized() {
    	List<Association> list = getIncompleteAssociations();
    	return list.isEmpty();
    }

    public String getLabel() {
        return label;
    }

    public String getFilename() {
        return filename;
    }

    public List<Association> getAllAssociations() {
        return Lists.newArrayList(propertyAssociationsMap.values());
    }

    public List<Association> getRefAssociations() {
    	Map<AssociationKey, Association> mapFiltered = Maps.filterValues(propertyAssociationsMap, new RefPredicate());
        return Lists.newArrayList(mapFiltered.values());
    }

    public List<Association> getUnrefAssociations() {
    	Map<AssociationKey, Association> mapFiltered = Maps.filterValues(propertyAssociationsMap, new UnrefPredicate());
        return Lists.newArrayList(mapFiltered.values());
    }

    public List<Association> getCompleteAssociations() {
    	Map<AssociationKey, Association> mapFiltered = Maps.filterValues(propertyAssociationsMap, new CompletePredicate());
        return Lists.newArrayList(mapFiltered.values());
    }

    private List<Association> getIncompleteAssociations() {
    	Map<AssociationKey, Association> mapFiltered = Maps.filterValues(propertyAssociationsMap, new IncompletePredicate());
        return Lists.newArrayList(mapFiltered.values());
    }

    private class RefPredicate implements Predicate<Association> {
    	
        public boolean apply(Association propertyAssociation) {
            return propertyAssociation.hasRefProperty() && !propertyAssociation.hasUnrefProperty();
        }
    };

    private class UnrefPredicate implements Predicate<Association> {
    	
        public boolean apply(Association propertyAssociation) {
            return !propertyAssociation.hasRefProperty() && propertyAssociation.hasUnrefProperty();
        }
    };

    private class CompletePredicate implements Predicate<Association> {
    	
        public boolean apply(Association propertyAssociation) {
            return propertyAssociation.hasRefProperty() && propertyAssociation.hasUnrefProperty();
        }
    };

    private class IncompletePredicate implements Predicate<Association> {
    	
        public boolean apply(Association propertyAssociation) {
            return !propertyAssociation.hasRefProperty() || !propertyAssociation.hasUnrefProperty();
        }
    };
}
