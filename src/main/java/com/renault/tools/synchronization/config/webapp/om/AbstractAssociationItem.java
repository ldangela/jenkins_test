package com.renault.tools.synchronization.config.webapp.om;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractAssociationItem implements AssociationItem {

    AbstractAssociationItem() {
        
    }
    
    protected String formatValue(String value) {
        return StringUtils.trimToEmpty(value);
    }

}
