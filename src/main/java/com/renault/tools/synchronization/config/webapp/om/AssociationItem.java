package com.renault.tools.synchronization.config.webapp.om;


public interface AssociationItem extends AssociationComponent {

    String getLabel();

	String getConfigname();
	
	String getFilename();
	
	String getPropertyName();

    String getUnrefPropertyName();
    
    String getRefPropertyName();

	String getUnrefPropertyScope();

	String getRefPropertyScope();
	
	String getUnrefPropertyValue();
	
	String getRefPropertyValue();
	
	int getWeight();
	
	boolean isSame(AssociationItem associationItem);

}
