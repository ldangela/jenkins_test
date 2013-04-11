package com.renault.tools.synchronization.config.webapp.om;

import org.apache.commons.lang.StringUtils;


public class EmptyAssociationItem extends AbstractAssociationItem {

    private String label;

	private String configname;
	
	private String filename;

	EmptyAssociationItem(String label, String configname, String filename) {
	    this.label = label;
		this.configname = configname;
		this.filename = filename;
	}
    
    @Override
    public String getLabel() {
        return label;
    }

	@Override
	public String getConfigname() {
		return configname;
	}

	@Override
	public String getFilename() {
		return filename;
	}

	@Override
	public String getPropertyName() {
		return "NONE";
	}

	@Override
	public String getUnrefPropertyValue() {
		return StringUtils.EMPTY;
	}

	@Override
	public String getRefPropertyValue() {
		return StringUtils.EMPTY;
	}

	@Override
	public String getUnrefPropertyScope() {
		return StringUtils.EMPTY;
	}

	@Override
	public String getRefPropertyScope() {
		return StringUtils.EMPTY;
	}

    @Override
    public String getUnrefPropertyName() {
        return StringUtils.EMPTY;
    }

    @Override
    public String getRefPropertyName() {
        return StringUtils.EMPTY;
    }

	@Override
	public int getWeight() {
		return -1;
	}

	@Override
	public boolean isSame(AssociationItem associationItem) {
		
		if (!(associationItem instanceof EmptyAssociationItem)) {
			return false;
		}

		String aUnrefPropertyScope = formatValue(associationItem.getUnrefPropertyScope());
		String aRefPropertyScope = formatValue(associationItem.getRefPropertyScope());
		String aUnrefPropertyValue = formatValue(associationItem.getUnrefPropertyValue());
		String aRefPropertyValue = formatValue(associationItem.getRefPropertyValue());

		if (!aUnrefPropertyScope.isEmpty()) {
			return false;
		}
		
		if (!aRefPropertyScope.isEmpty()) {
			return false;			
		}
		
		if (!aUnrefPropertyValue.isEmpty()) {
			return false;			
		}
		
		if (!aRefPropertyValue.isEmpty()) {
			return false;			
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("[emptyAssociationItem:%s,%s,%s]", label, configname, filename);
	}

}
