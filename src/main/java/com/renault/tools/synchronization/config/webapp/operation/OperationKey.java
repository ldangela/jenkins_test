package com.renault.tools.synchronization.config.webapp.operation;

public final class OperationKey {
 
    private final String discriminator;
    
	private final String configname;
	
	private final String filename;
	
	private final String propertyName;
	
	public static OperationKey create(String discriminator, String configname, String filename, String propertyName) {
		return new OperationKey(discriminator, configname, filename, propertyName);
	}
	
	private OperationKey(String discriminator, String configname, String filename, String propertyName) {
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
		return configname.hashCode() + filename.hashCode() + propertyName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof OperationKey)) {
			return false;
		}
		
		OperationKey key = (OperationKey) obj;
		return configname.equals(key.getConfigname()) && filename.equals(key.getFilename()) && propertyName.equals(key.getPropertyName());
	}

	@Override
	public String toString() {
		return String.format("[operationKey:%s,%s,%s]", configname, filename, propertyName);
	}	
}
