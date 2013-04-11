package com.renault.tools.synchronization.config.webapp.association.impl;

import com.renault.tools.synchronization.config.webapp.om.PropertyContainer;

public class CompleteAssociation extends AbstractAssociation {

    public CompleteAssociation(PropertyContainer unrefPropertyContainer, PropertyContainer refPropertyContainer) {
        super(unrefPropertyContainer, refPropertyContainer);
    }

	@Override
	public boolean hasRefProperty() {
		return true;
	}

	@Override
	public boolean hasUnrefProperty() {
		return true;
	}

}
