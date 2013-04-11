package com.renault.tools.synchronization.config.webapp.operation.impl;

import com.renault.tools.synchronization.config.webapp.om.AssociationItem;
import com.renault.tools.synchronization.config.webapp.operation.Operation;

public class OperationFactory {

    public static Operation buildUpdateOperation(String groupName, AssociationItem oldAssociationItem, AssociationItem newAssociationItem) {
        return new UpdateOperation(groupName, oldAssociationItem, newAssociationItem);
    }

    public static Operation buildCreateOperation(String groupName, AssociationItem newAssociationItem) {
        return new CreateOperation(groupName, newAssociationItem);        
    }

    public static Operation buildDeleteOperation(String groupName, AssociationItem oldAssociationItem) {
        return new DeleteOperation(groupName,oldAssociationItem);
    }
}
