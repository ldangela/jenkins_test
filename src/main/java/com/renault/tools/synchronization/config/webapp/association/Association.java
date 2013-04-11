package com.renault.tools.synchronization.config.webapp.association;

import com.renault.tools.synchronization.config.webapp.om.IPropertyContainer;

public interface Association {

    IPropertyContainer getRefPropertyContainer();

    IPropertyContainer getUnrefPropertyContainer();
	
	String getPropertyName();
	
	boolean hasRefProperty();
	
	boolean hasUnrefProperty();
		
}
