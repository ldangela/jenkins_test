package com.renault.tools.synchronization.config.webapp.operation;

import com.renault.tools.synchronization.config.webapp.om.AssociationItem;

public interface Operation {

    AssociationItem getOldAssociationItem();

    AssociationItem getNewAssociationItem();
    
    String getGroupName();

    OperationActionEnum getAction();

    boolean isUpdateOperation();

    boolean isCreateOperation();

    boolean isDeleteOperation();
}
