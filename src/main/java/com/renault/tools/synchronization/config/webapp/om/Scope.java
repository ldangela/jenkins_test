package com.renault.tools.synchronization.config.webapp.om;

import org.apache.commons.lang.StringUtils;

public class Scope {

    private String label;

    private String value;

    Scope(String label, String value) {
        this.label = label;
        this.value = StringUtils.trimToEmpty(value);
    }
    
    public String getLabel() {
        return label;
    }
    
    public String getValue() {
        return value;
    }
}
