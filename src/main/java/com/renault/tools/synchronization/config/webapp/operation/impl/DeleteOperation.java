package com.renault.tools.synchronization.config.webapp.operation.impl;

import com.renault.tools.synchronization.config.webapp.om.AssociationItem;
import com.renault.tools.synchronization.config.webapp.om.AssociationItemFactory;
import com.renault.tools.synchronization.config.webapp.operation.Operation;
import com.renault.tools.synchronization.config.webapp.operation.OperationActionEnum;

public class DeleteOperation implements Operation {
    
    private String groupName;

    private AssociationItem newAssociationItem;

    private AssociationItem oldAssociationItem;
        
    DeleteOperation(String groupName, AssociationItem oldAssociationItem) {
        this.groupName = groupName;
        this.newAssociationItem = AssociationItemFactory.createNullAssociationItem();
        this.oldAssociationItem = oldAssociationItem;
    }

    @Override
    public AssociationItem getOldAssociationItem() {
        return oldAssociationItem;
    }

    @Override
    public AssociationItem getNewAssociationItem() {
        return newAssociationItem;
    }

	@Override
	public boolean isUpdateOperation() {
		return false;
	}

	@Override
	public boolean isCreateOperation() {
		return false;
	}

	@Override
	public boolean isDeleteOperation() {
		return true;
	}

    @Override
    public OperationActionEnum getAction() {
        return OperationActionEnum.DELETE;
    }

	@Override
	public String toString() {
		String oldAssociationItemString = oldAssociationItem.toString();
		return String.format("[deleteOperation:%s]", oldAssociationItemString);
	}

    @Override
    public String getGroupName() {
        return groupName;
    }

}
