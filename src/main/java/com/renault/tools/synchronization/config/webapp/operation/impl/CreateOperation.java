package com.renault.tools.synchronization.config.webapp.operation.impl;

import com.renault.tools.synchronization.config.webapp.om.AssociationItem;
import com.renault.tools.synchronization.config.webapp.om.AssociationItemFactory;
import com.renault.tools.synchronization.config.webapp.operation.Operation;
import com.renault.tools.synchronization.config.webapp.operation.OperationActionEnum;

public class CreateOperation implements Operation {

    private String groupName;

    private AssociationItem newAssociationItem;

    private AssociationItem oldAssociationItem;

    CreateOperation(String groupName, AssociationItem newAssociationItem) {
        this.groupName = groupName;
        this.newAssociationItem = newAssociationItem;
        this.oldAssociationItem = AssociationItemFactory.createNullAssociationItem();
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
		return true;
	}

	@Override
	public boolean isDeleteOperation() {
		return false;
	}

    @Override
    public OperationActionEnum getAction() {
        return OperationActionEnum.CREATE;
    }

	@Override
	public String toString() {
		String newAssociationItemString = newAssociationItem.toString();
		return String.format("[createOperation:{%s}]", newAssociationItemString);
	}

    @Override
    public String getGroupName() {
        return groupName;
    }

}
