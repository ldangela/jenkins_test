package com.renault.tools.synchronization.config.webapp.om;

import org.apache.commons.lang.StringUtils;

public class NullPropertyContainer implements IPropertyContainer {
    
    @Override
    public String getName() {
        return "NONE";        
    }

    @Override
    public String getValue() {
        return StringUtils.EMPTY;        
    }

    @Override
    public String getScope() {
        return StringUtils.EMPTY;        
    }
    
	@Override
	public String toString() {
		return "[propertyContainer:NONE]";
	}

}
