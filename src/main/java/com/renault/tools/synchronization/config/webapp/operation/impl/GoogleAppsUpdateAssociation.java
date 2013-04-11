package com.renault.tools.synchronization.config.webapp.operation.impl;

import java.io.IOException;

import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.util.ServiceException;
import com.renault.tools.synchronization.config.webapp.om.AssociationItem;
import com.renault.tools.synchronization.config.webapp.operation.Operation;

public class GoogleAppsUpdateAssociation extends GoogleAppsOperation {

    private ListEntry googleRow;

    GoogleAppsUpdateAssociation(Operation operation, ListEntry googleRow) {
        super(operation);
        this.googleRow = googleRow;
    }

	@Override
	public ListEntry operation() {
		try {
			update(googleRow);
		} catch (IOException e) {
			throw new RuntimeException("Cannot update row.", e);
		} catch (ServiceException e) {			
			throw new RuntimeException("Cannot update row.", e);
		}
		return googleRow;
	}

    private void update(ListEntry googleRow) throws IOException, ServiceException {
        AssociationItem newAssociationItem = getNewAssociationItem();
        AssociationItem oldAssociationItem = getOldAssociationItem();
        
        String oldUnrefPropertyScope = convertValue(oldAssociationItem.getUnrefPropertyScope());
        String oldRefPropertyScope = convertValue(oldAssociationItem.getRefPropertyScope());
        String oldUnrefPropertyValue = convertValue(oldAssociationItem.getUnrefPropertyValue());
        String oldRefPropertyValue = convertValue(oldAssociationItem.getRefPropertyValue());
        
        String newUnrefPropertyScope = convertValue(newAssociationItem.getUnrefPropertyScope());
        String newRefPropertyScope = convertValue(newAssociationItem.getRefPropertyScope());
        String newUnrefPropertyValue = convertValue(newAssociationItem.getUnrefPropertyValue());
        String newRefPropertyValue = convertValue(newAssociationItem.getRefPropertyValue());

        if (!oldUnrefPropertyScope.equals(newUnrefPropertyScope)) {
            googleRow.getCustomElements().setValueLocal("propertyscope", newUnrefPropertyScope);
        }
        if (!oldRefPropertyScope.equals(newRefPropertyScope)) {
            googleRow.getCustomElements().setValueLocal("referencepropertyscope", newRefPropertyScope);
        }
        if (!oldUnrefPropertyValue.equalsIgnoreCase(newUnrefPropertyValue)) {
            googleRow.getCustomElements().setValueLocal("propertyvalue", newUnrefPropertyValue);
        }
        if (!oldRefPropertyValue.equalsIgnoreCase(newRefPropertyValue)) {        
            googleRow.getCustomElements().setValueLocal("referencepropertyvalue", newRefPropertyValue);
        }
        googleRow.update();
    }
}
