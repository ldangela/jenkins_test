package com.renault.tools.synchronization.config.webapp.om;

import com.renault.tools.synchronization.config.webapp.association.Association;

public class CompleteAssociationItem extends AbstractAssociationItem {

    private String label;

	private String configname;
	
	private String filename;
	
	private Association association;
	
	CompleteAssociationItem(String label, String configname, String filename, Association association) {
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
	public String getUnrefPropertyScope() {
		return association.getUnrefPropertyContainer().getScope();
	}

	@Override
	public String getRefPropertyScope() {
		return association.getRefPropertyContainer().getScope();
	}

	@Override
	public String getUnrefPropertyValue() {
		return association.getUnrefPropertyContainer().getValue();
	}

	@Override
	public String getRefPropertyValue() {
		return association.getRefPropertyContainer().getValue();
	}

	@Override
	public int getWeight() {
		return 5;
	}

	@Override
	public String getPropertyName() {
		return association.getUnrefPropertyContainer().getName();
	}

	@Override
	public boolean isSame(AssociationItem associationItem) {
		
		if (!(associationItem instanceof CompleteAssociationItem)) {
			return false;
		}
		
		String aUnrefPropertyScope = formatValue(associationItem.getUnrefPropertyScope());
		String aRefPropertyScope = formatValue(associationItem.getRefPropertyScope());
		String aUnrefPropertyValue = formatValue(associationItem.getUnrefPropertyValue());
		String aRefPropertyValue = formatValue(associationItem.getRefPropertyValue());

		String unrefPropertyScope = formatValue(getUnrefPropertyScope());
		String refPropertyScope = formatValue(getRefPropertyScope());
		String unrefPropertyValue = formatValue(getUnrefPropertyValue());
		String refPropertyValue = formatValue(getRefPropertyValue());

		if (!unrefPropertyScope.equals(aUnrefPropertyScope)) {
			return false;
		}
		
		if (!refPropertyScope.equals(aRefPropertyScope)) {
			return false;			
		}
		
		if (!unrefPropertyValue.equalsIgnoreCase(aUnrefPropertyValue)) {
			return false;			
		}
		
		if (!refPropertyValue.equalsIgnoreCase(aRefPropertyValue)) {
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
        return association.getRefPropertyContainer().getName();
    }

	@Override
	public String toString() {
		String associationString = association.toString();
		return String.format("[completeAssociationItem:%s, %s,%s,%s]", label, configname, filename, associationString);
	}

}
