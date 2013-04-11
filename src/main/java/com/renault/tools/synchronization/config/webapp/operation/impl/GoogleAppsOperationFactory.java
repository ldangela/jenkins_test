package com.renault.tools.synchronization.config.webapp.operation.impl;

import com.google.gdata.data.spreadsheet.ListEntry;
import com.renault.tools.synchronization.config.webapp.client.google.docs.GoogleAppsClient;
import com.renault.tools.synchronization.config.webapp.operation.Operation;

public class GoogleAppsOperationFactory {

	public static GoogleAppsOperation buildOperation(Operation operation, GoogleAppsClient googleAppsClient, ListEntry googleRow) {
		if (operation.isCreateOperation()) {
			return new GoogleAppsCreateAssociation(operation, googleAppsClient);
		}
		if (operation.isUpdateOperation()) {
			return new GoogleAppsUpdateAssociation(operation, googleRow);
		}
		if (operation.isDeleteOperation()) {
			return new GoogleAppsDeleteAssociation(operation, googleRow);
		}
		throw new IllegalStateException("Cannot build operation.");
    }

    public static GoogleAppsOperation buildCreateOperation(Operation operation, GoogleAppsClient googleAppsClient) {
        return new GoogleAppsCreateAssociation(operation, googleAppsClient);
    }

    public static GoogleAppsOperation buildDeleteOperation(Operation operation, ListEntry googleRow) {
        return new GoogleAppsDeleteAssociation(operation, googleRow);
    }

    public static GoogleAppsOperation buildUpdateOperation(Operation operation, ListEntry googleRow) {
        return new GoogleAppsUpdateAssociation(operation, googleRow);
    }
	
    public static GoogleAppsOperation buildMovementDiary(Operation operation, GoogleAppsClient googleAppsClient) {
        return new GoogleAppsCreateMovementDiary(operation, googleAppsClient);
    }
}
