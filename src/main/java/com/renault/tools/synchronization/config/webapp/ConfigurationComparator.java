package com.renault.tools.synchronization.config.webapp;

import java.util.List;

import org.apache.log4j.Logger;

import com.renault.tools.synchronization.config.webapp.association.impl.AssociationKey;
import com.renault.tools.synchronization.config.webapp.om.AssociationCatalog;
import com.renault.tools.synchronization.config.webapp.om.AssociationItem;
import com.renault.tools.synchronization.config.webapp.operation.Operation;
import com.renault.tools.synchronization.config.webapp.operation.OperationCatalog;
import com.renault.tools.synchronization.config.webapp.operation.OperationKey;
import com.renault.tools.synchronization.config.webapp.operation.impl.OperationFactory;

public class ConfigurationComparator {
 
    private static final Logger LOGGER = Logger.getLogger(ConfigurationComparator.class);

	private OperationCatalog operationCatalog = new OperationCatalog();
	
    public OperationCatalog compare(String groupName, AssociationCatalog newAssociationCatalog, AssociationCatalog oldAssociationCatalog) {

        List<AssociationItem> oldAssociationItems = oldAssociationCatalog.getSortedAssociationItems();
        for (AssociationItem oldAssociationItem : oldAssociationItems) {
            
            String label = oldAssociationItem.getLabel();
            String configname = oldAssociationItem.getConfigname();
            String filename = oldAssociationItem.getFilename();
            String propertyName = oldAssociationItem.getPropertyName();
            
            AssociationKey associationKey = AssociationKey.create(label, configname, filename, propertyName);
            AssociationItem newAssociationItem = newAssociationCatalog.containsAssociationItem(associationKey);
            OperationKey operationKey = OperationKey.create(label, configname, filename, propertyName);
            if (newAssociationItem == null) {
                Operation operation = OperationFactory.buildDeleteOperation(groupName, oldAssociationItem);
                LOGGER.debug("DELETE : " + operation.toString());
                operationCatalog.putOperation(operationKey, operation);
                continue;
            }
            
            if (!newAssociationItem.isSame(oldAssociationItem)) {
                Operation operation = OperationFactory.buildUpdateOperation(groupName, oldAssociationItem, newAssociationItem);
                LOGGER.debug("UPDATE : " + operation.toString());
                operationCatalog.putOperation(operationKey, operation);
            }
        }

        List<AssociationItem> newAssociationItems = newAssociationCatalog.getSortedAssociationItems();
        for (AssociationItem newAssociationItem : newAssociationItems) {
            
            String label = newAssociationItem.getLabel();
            String configname = newAssociationItem.getConfigname();
            String filename = newAssociationItem.getFilename();
            String propertyName = newAssociationItem.getPropertyName();
            
            AssociationKey associationKey = AssociationKey.create(label, configname, filename, propertyName);
            AssociationItem oldAssociationItem = oldAssociationCatalog.containsAssociationItem(associationKey);
            OperationKey operationKey = OperationKey.create(label, configname, filename, propertyName);
            if (oldAssociationItem == null) {
                Operation operation = OperationFactory.buildCreateOperation(groupName, newAssociationItem);
                LOGGER.debug("CREATE : " + operation.toString());
                operationCatalog.putOperation(operationKey, operation);
            }
        }
        
        return operationCatalog;
    }
}
