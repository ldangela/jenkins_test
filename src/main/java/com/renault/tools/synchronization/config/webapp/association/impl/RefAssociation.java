package com.renault.tools.synchronization.config.webapp.association.impl;

import com.renault.tools.synchronization.config.webapp.om.NullPropertyContainer;
import com.renault.tools.synchronization.config.webapp.om.PropertyContainer;

public class RefAssociation extends AbstractAssociation {

    public RefAssociation(PropertyContainer refPropertyContainer) {
        super(new NullPropertyContainer(), refPropertyContainer);
    }

	@Override
	public boolean hasRefProperty() {
		return true;
	}

	@Override
	public boolean hasUnrefProperty() {
		return false;
	}
}
