package com.renault.tools.synchronization.config.webapp.operation.impl;

import com.renault.tools.synchronization.config.webapp.om.AssociationItem;
import com.renault.tools.synchronization.config.webapp.operation.Operation;
import com.renault.tools.synchronization.config.webapp.operation.OperationActionEnum;

public class UpdateOperation implements Operation {

    private String groupName;

    private AssociationItem newAssociationItem;
    
    private AssociationItem oldAssociationItem;
    
    UpdateOperation(String groupName, AssociationItem oldAssociationItem, AssociationItem newAssociationItem) {
        this.groupName = groupName;
        this.oldAssociationItem = oldAssociationItem;
        this.newAssociationItem = newAssociationItem;
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
		return true;
	}

	@Override
	public boolean isCreateOperation() {
		return false;
	}

	@Override
	public boolean isDeleteOperation() {
		return false;
	}

    @Override
    public OperationActionEnum getAction() {
        return OperationActionEnum.UPDATE;
    }

	@Override
	public String toString() {
		String oldAssociationItemString = oldAssociationItem.toString();
		String newAssociationItemString = newAssociationItem.toString();
		return String.format("[updateOperation:%s,%s]", oldAssociationItemString, newAssociationItemString);
	}

    @Override
    public String getGroupName() {
        return groupName;
    }

}
