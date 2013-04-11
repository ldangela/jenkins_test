package com.renault.tools.synchronization.config.webapp.association.impl;

import com.renault.tools.synchronization.config.webapp.association.Association;
import com.renault.tools.synchronization.config.webapp.om.IPropertyContainer;
import com.renault.tools.synchronization.config.webapp.om.NullPropertyContainer;

public abstract class AbstractAssociation implements Association {

    private IPropertyContainer unrefPropertyContainer;

    private IPropertyContainer refPropertyContainer;

    protected AbstractAssociation(IPropertyContainer unrefPropertyContainer, IPropertyContainer refPropertyContainer) {
        this.unrefPropertyContainer = unrefPropertyContainer;
        this.refPropertyContainer = refPropertyContainer;
    }

    @Override
    public IPropertyContainer getUnrefPropertyContainer() {
        return unrefPropertyContainer;        
    }

    @Override
    public IPropertyContainer getRefPropertyContainer() {
        return refPropertyContainer;
    }
    
    public boolean isSuccessAssociation() {
    	return refPropertyContainer != null && unrefPropertyContainer != null;
    }

    @Override
    public String getPropertyName() {
    	if (!(refPropertyContainer instanceof NullPropertyContainer)) {
    		return refPropertyContainer.getName();
    	}
    	if (!(unrefPropertyContainer instanceof NullPropertyContainer)) {
    		return unrefPropertyContainer.getName();
    	}
    	throw new IllegalStateException("PropertyAssociation must have at least one propertyContainer.");
    }

	@Override
	public String toString() {
		String refPropertyContainer = getRefPropertyContainer().toString();
		String unrefPropertyContainer = getUnrefPropertyContainer().toString();
		return String.format("[association:[unrefPropertyContainer:%s];[refPropertyContainer:%s]]", unrefPropertyContainer, refPropertyContainer);
	}
}
