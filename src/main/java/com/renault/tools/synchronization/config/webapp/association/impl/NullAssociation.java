package com.renault.tools.synchronization.config.webapp.association.impl;

import com.renault.tools.synchronization.config.webapp.om.NullPropertyContainer;

public class NullAssociation extends AbstractAssociation {

    public NullAssociation() {
        super(new NullPropertyContainer(), new NullPropertyContainer());
    }

	@Override
	public boolean hasRefProperty() {
		return false;
	}

	@Override
	public boolean hasUnrefProperty() {
		return false;
	}
}
