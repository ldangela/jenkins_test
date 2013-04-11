package com.renault.tools.synchronization.config.webapp.association.impl;

import com.renault.tools.synchronization.config.webapp.om.NullPropertyContainer;
import com.renault.tools.synchronization.config.webapp.om.PropertyContainer;

public class UnrefAssociation extends AbstractAssociation {

    public UnrefAssociation(PropertyContainer propertyContainer) {
        super(propertyContainer, new NullPropertyContainer());
    }

	@Override
	public boolean hasRefProperty() {
		return false;
	}

	@Override
	public boolean hasUnrefProperty() {
		return true;
	}
}
