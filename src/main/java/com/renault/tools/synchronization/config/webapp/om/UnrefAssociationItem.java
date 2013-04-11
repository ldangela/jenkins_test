package com.renault.tools.synchronization.config.webapp.om;

import com.renault.tools.synchronization.config.webapp.association.Association;

public class UnrefAssociationItem extends AbstractAssociationItem {

    private String label;

	private String configname;
	
	private String filename;

	private Association association;

	UnrefAssociationItem(String label, String configname, String filename, Association association) {
	    this.label = label;
		this.configname = configname;
		this.filename = filename;
		this.association = association;
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
		return association.getUnrefPropertyContainer().getName();
	}

	@Override
	public String getUnrefPropertyScope() {
		return association.getUnrefPropertyContainer().getScope();
	}
	
	@Override
	public String getUnrefPropertyValue() {
		return association.getUnrefPropertyContainer().getValue();
	}

	@Override
	public String getRefPropertyScope() {
		return association.getRefPropertyContainer().getScope();
	}

	@Override
	public String getRefPropertyValue() {
		return association.getRefPropertyContainer().getValue();
	}

	@Override
	public int getWeight() {
		return 1;
	}

	@Override
	public boolean isSame(AssociationItem associationItem) {
		
		if (!(associationItem instanceof UnrefAssociationItem)) {
			return false;
		}

        String aRefPropertyScope = formatValue(associationItem.getRefPropertyScope());
		String aUnrefPropertyScope = formatValue(associationItem.getUnrefPropertyScope());
		String aUnrefPropertyValue = formatValue(associationItem.getUnrefPropertyValue());

		String unrefPropertyScope = formatValue(getUnrefPropertyScope());
		String unrefPropertyValue = formatValue(getUnrefPropertyValue());
		String refPropertyValue = formatValue(getRefPropertyValue());
		
		if (!unrefPropertyScope.equals(aUnrefPropertyScope)) {
			return false;
		}
		
		if (!aRefPropertyScope.isEmpty()) {
			return false;			
		}
		
		if (!unrefPropertyValue.equalsIgnoreCase(aUnrefPropertyValue)) {
			return false;			
		}
		
		if (!refPropertyValue.isEmpty()) {
			return false;			
		}
		return true;
	}

    @Override
    public String getUnrefPropertyName() {
        return association.getUnrefPropertyContainer().getName();
    }

    @Override
    public String getRefPropertyName() {
        return null;
    }

	@Override
	public String toString() {
		String associationString = association.toString();
		return String.format("[unrefAssociationItem:%s,%s,%s,%s]", label, configname, filename, associationString);
	}
}
