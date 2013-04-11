package com.renault.tools.synchronization.config.webapp.operation.impl;

import org.apache.commons.lang.StringUtils;

import com.google.gdata.data.spreadsheet.ListEntry;
import com.renault.tools.synchronization.config.webapp.om.AssociationItem;
import com.renault.tools.synchronization.config.webapp.om.NullAssociationItem;
import com.renault.tools.synchronization.config.webapp.operation.Operation;
import com.renault.tools.synchronization.config.webapp.operation.OperationActionEnum;

public abstract class GoogleAppsOperation {

	private Operation operation;
	
	protected GoogleAppsOperation(Operation operation) {
		this.operation = operation;
	}
	
	public abstract ListEntry operation();
	
    protected AssociationItem getOldAssociationItem() {
    	return operation.getOldAssociationItem();
    }

    protected AssociationItem getNewAssociationItem() {
    	return operation.getNewAssociationItem();    	
    }

    protected boolean isUpdateOperation() {
    	return operation.isUpdateOperation();
    }

    protected boolean isCreateOperation() {
    	return operation.isCreateOperation();
    }

    protected boolean isDeleteOperation() {
    	return operation.isDeleteOperation();
    }

    protected String convertValue(String value) {
    	return StringUtils.trimToEmpty(value);
    }
    
    public OperationActionEnum getAction() {
        return operation.getAction();
    }

    public String getLabel() {
        if (!(operation.getOldAssociationItem() instanceof NullAssociationItem)) {
            return operation.getOldAssociationItem().getLabel();
        }
        return operation.getNewAssociationItem().getLabel();
    }

    public String getGroupName() {
        return operation.getGroupName();
    }
}
