package com.renault.tools.synchronization.config.webapp.association.impl;

public final class AssociationKey {

    private final String discriminator;

	private final String configname;
	
	private final String filename;
	
	private final String propertyName;
	
	public static AssociationKey create(String discriminator, String configname, String filename, String propertyName) {
		return new AssociationKey(discriminator, configname, filename, propertyName);
	}
	
	private AssociationKey(String discriminator, String configname, String filename, String propertyName) { 
	    this.discriminator = discriminator;
		this.configname = configname;
		this.filename = filename;
		this.propertyName = propertyName;
	}
    
    public String getDiscriminator() {
        return discriminator;
    }
	
	public String getConfigname() {
		return configname;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getPropertyName() {
		return propertyName;
	}

	@Override
	public int hashCode() {
		return discriminator.hashCode() + configname.hashCode() + filename.hashCode() + propertyName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AssociationKey)) {
			return false;
		}
		
		AssociationKey key = (AssociationKey) obj;
		return discriminator.equals(key.getDiscriminator()) && configname.equals(key.getConfigname()) && filename.equals(key.getFilename()) && propertyName.equals(key.getPropertyName());
	}

	@Override
	public String toString() {
		return String.format("[associationKey:%s,%s,%s,%s]", discriminator, configname, filename, propertyName);
	}	
}
