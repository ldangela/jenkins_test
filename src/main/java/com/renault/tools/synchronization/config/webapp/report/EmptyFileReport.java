package com.renault.tools.synchronization.config.webapp.report;

import com.renault.tools.synchronization.config.webapp.association.impl.AssociationKey;
import com.renault.tools.synchronization.config.webapp.association.impl.NullAssociation;

public class EmptyFileReport extends PropReport implements Synchronizable {
    
    public EmptyFileReport(String label, String configname, String filename) {
    	super(label, filename);
        AssociationKey key = AssociationKey.create(label, configname, filename, "NONE");
    	putPropertyAssociation(key, new NullAssociation());
    }

	@Override
	public boolean isSynchronized() {
		return true;
	}

}
