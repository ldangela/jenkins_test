package com.renault.tools.synchronization.config.webapp.operation.impl;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

import com.google.gdata.data.spreadsheet.ListEntry;
import com.renault.tools.synchronization.config.webapp.client.google.docs.GoogleAppsClient;
import com.renault.tools.synchronization.config.webapp.om.AssociationItem;
import com.renault.tools.synchronization.config.webapp.operation.Operation;

public class GoogleAppsCreateMovementDiary extends GoogleAppsOperation {
    
    private GoogleAppsClient googleClient;

    GoogleAppsCreateMovementDiary(Operation operation, GoogleAppsClient googleClient) {
        super(operation);
        this.googleClient = googleClient;
    }

	@Override
	public ListEntry operation() {
	    ListEntry googleRow = new ListEntry();
		try {
			create(googleRow);
		} catch (Exception e) {
			throw new RuntimeException("Cannot create diary movement row.", e);
		}
		return googleRow;
	}
    
    private void create(ListEntry googleRow) throws Exception {
        AssociationItem oldAssociationItem = getOldAssociationItem();
        AssociationItem newAssociationItem = getNewAssociationItem();

        String label = convertValue(getLabel());
        String groupName = convertValue(getGroupName());
        String action = convertValue(getAction().name());

        String oldConfigname = convertValue(oldAssociationItem.getConfigname());
        String oldFilename = convertValue(oldAssociationItem.getFilename());
        String oldUnrefPropertyName = convertValue(oldAssociationItem.getUnrefPropertyName());
        String oldRefPropertyName = convertValue(oldAssociationItem.getRefPropertyName());
        String oldUnrefPropertyScope = convertValue(oldAssociationItem.getUnrefPropertyScope());
        String oldRefPropertyScope = convertValue(oldAssociationItem.getRefPropertyScope());
        String oldUnrefPropertyValue = convertValue(oldAssociationItem.getUnrefPropertyValue());
        String oldRefPropertyValue = convertValue(oldAssociationItem.getRefPropertyValue());

        String newConfigname = convertValue(newAssociationItem.getConfigname());
        String newFilename = convertValue(newAssociationItem.getFilename());
        String newUnrefPropertyName = convertValue(newAssociationItem.getUnrefPropertyName());
        String newRefPropertyName = convertValue(newAssociationItem.getRefPropertyName());
        String newUnrefPropertyScope = convertValue(newAssociationItem.getUnrefPropertyScope());
        String newRefPropertyScope = convertValue(newAssociationItem.getRefPropertyScope());
        String newUnrefPropertyValue = convertValue(newAssociationItem.getUnrefPropertyValue());
        String newRefPropertyValue = convertValue(newAssociationItem.getRefPropertyValue());
        
        googleRow.getCustomElements().setValueLocal("updatedate", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        googleRow.getCustomElements().setValueLocal("label", label);
        googleRow.getCustomElements().setValueLocal("groupname", groupName);
        googleRow.getCustomElements().setValueLocal("action", action);

        googleRow.getCustomElements().setValueLocal("oldconfigurationname", oldConfigname);
        googleRow.getCustomElements().setValueLocal("oldfilename", oldFilename);
        googleRow.getCustomElements().setValueLocal("oldpropertyscope", oldUnrefPropertyScope);
        googleRow.getCustomElements().setValueLocal("oldreferencepropertyscope", oldRefPropertyScope);
        googleRow.getCustomElements().setValueLocal("oldpropertyname", oldUnrefPropertyName);
        googleRow.getCustomElements().setValueLocal("oldreferencepropertyname", oldRefPropertyName);
        googleRow.getCustomElements().setValueLocal("oldpropertyvalue", oldUnrefPropertyValue);
        googleRow.getCustomElements().setValueLocal("oldreferencepropertyvalue", oldRefPropertyValue);

        googleRow.getCustomElements().setValueLocal("newconfigurationname", newConfigname);
        googleRow.getCustomElements().setValueLocal("newfilename", newFilename);
        googleRow.getCustomElements().setValueLocal("newpropertyscope", newUnrefPropertyScope);
        googleRow.getCustomElements().setValueLocal("newreferencepropertyscope", newRefPropertyScope);
        googleRow.getCustomElements().setValueLocal("newpropertyname", newUnrefPropertyName);
        googleRow.getCustomElements().setValueLocal("newreferencepropertyname", newRefPropertyName);
        googleRow.getCustomElements().setValueLocal("newpropertyvalue", newUnrefPropertyValue);
        googleRow.getCustomElements().setValueLocal("newreferencepropertyvalue", newRefPropertyValue);

        String spreadsheet = googleClient.getCurrentDocumentContext().getDiarySpreadsheet();
        String worksheet = googleClient.getCurrentDocumentContext().getDiaryWorksheet();
        googleClient.insertRow(spreadsheet, worksheet, googleRow);
    }
}
