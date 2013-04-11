package com.renault.tools.synchronization.config.webapp.operation.impl;

import com.google.gdata.data.spreadsheet.ListEntry;
import com.renault.tools.synchronization.config.webapp.client.google.docs.GoogleAppsClient;
import com.renault.tools.synchronization.config.webapp.om.AssociationItem;
import com.renault.tools.synchronization.config.webapp.operation.Operation;

public class GoogleAppsCreateAssociation extends GoogleAppsOperation {
    
    private GoogleAppsClient googleClient;
    
    GoogleAppsCreateAssociation(Operation operation, GoogleAppsClient googleClient) {
        super(operation);
        this.googleClient = googleClient;
    }

	@Override
	public ListEntry operation() {
	    ListEntry googleRow = new ListEntry();
		try {		    
			create(googleRow);					
		} catch (Exception e) {
			throw new RuntimeException("Cannot update row.", e);
		}
		return googleRow;
	}
    
    private void create(ListEntry googleRow) throws Exception {
        AssociationItem newAssociationItem = getNewAssociationItem();

        String label = convertValue(newAssociationItem.getLabel());
        String configname = convertValue(newAssociationItem.getConfigname());
        String filename = convertValue(newAssociationItem.getFilename());
        String unrefPropertyName = convertValue(newAssociationItem.getUnrefPropertyName());
        String refPropertyName = convertValue(newAssociationItem.getRefPropertyName());
        String unrefPropertyScope = convertValue(newAssociationItem.getUnrefPropertyScope());
        String refPropertyScope = convertValue(newAssociationItem.getRefPropertyScope());
        String unrefPropertyValue = convertValue(newAssociationItem.getUnrefPropertyValue());
        String refPropertyValue = convertValue(newAssociationItem.getRefPropertyValue());
        
        googleRow.getCustomElements().setValueLocal("label", label);
        googleRow.getCustomElements().setValueLocal("configurationname", configname);
        googleRow.getCustomElements().setValueLocal("filename", filename);
        googleRow.getCustomElements().setValueLocal("propertyname", unrefPropertyName);
        googleRow.getCustomElements().setValueLocal("referencepropertyname", refPropertyName);
        googleRow.getCustomElements().setValueLocal("propertyscope", unrefPropertyScope);
        googleRow.getCustomElements().setValueLocal("referencepropertyscope", refPropertyScope);
        googleRow.getCustomElements().setValueLocal("propertyvalue", unrefPropertyValue);
        googleRow.getCustomElements().setValueLocal("referencepropertyvalue", refPropertyValue);
        
        String spreadsheet = googleClient.getCurrentDocumentContext().getSpreadsheet();
        String worksheet = googleClient.getCurrentDocumentContext().getWorksheet();
        googleClient.insertRow(spreadsheet, worksheet, googleRow);
    }
}
