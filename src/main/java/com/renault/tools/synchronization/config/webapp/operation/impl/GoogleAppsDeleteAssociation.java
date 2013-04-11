package com.renault.tools.synchronization.config.webapp.operation.impl;

import java.io.IOException;

import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.util.ServiceException;
import com.renault.tools.synchronization.config.webapp.operation.Operation;

public class GoogleAppsDeleteAssociation extends GoogleAppsOperation {
    
    private ListEntry googleRow;
    
    GoogleAppsDeleteAssociation(Operation operation, ListEntry googleRow) {
        super(operation);
        this.googleRow = googleRow;
    }

	@Override
	public ListEntry operation() {
		try {
			delete(googleRow);					
		} catch (IOException e) {
			throw new RuntimeException("Cannot delete row.", e);
		} catch (ServiceException e) {			
			throw new RuntimeException("Cannot delete row.", e);
		}
		return googleRow;
	}

    private void delete(ListEntry googleRow) throws IOException, ServiceException {
        googleRow.delete();        
    }
}
