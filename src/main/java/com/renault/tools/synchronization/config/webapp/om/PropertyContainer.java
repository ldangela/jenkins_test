package com.renault.tools.synchronization.config.webapp.om;

public class PropertyContainer implements IPropertyContainer {

    private String name;
    
    private Scope scope;

    public static PropertyContainer createProperty(String propertyName, Scope propertyScope) {
        return new PropertyContainer(propertyName, propertyScope);
    }

    private PropertyContainer(String name, Scope scope) {
        this.name = name;
        this.scope = scope;
    }
    
    @Override
    public String getName() {
        return name;        
    }

    @Override
    public String getValue() {
        return scope.getValue();        
    }

    @Override
    public String getScope() {
        return scope.getLabel();        
    }    

	@Override
	public String toString() {
	    String name = getName();
		String scope = getScope();
		String value = getValue();
		return String.format("[propertyContainer:%s,%s,%s]", name, scope, value);
	}

}
