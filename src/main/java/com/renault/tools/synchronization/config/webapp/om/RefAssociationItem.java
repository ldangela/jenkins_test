package com.renault.tools.synchronization.config.webapp.om;

import com.renault.tools.synchronization.config.webapp.association.Association;

public class RefAssociationItem extends AbstractAssociationItem {

    private String label;

	private String configname;
	
	private String filename;

	private Association association;

	RefAssociationItem(String label, String configname, String filename, Association association) {
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
		return association.getRefPropertyContainer().getName();
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
	public String getUnrefPropertyScope() {
		return association.getUnrefPropertyContainer().getScope();
	}

	@Override
	public String getRefPropertyScope() {
		return association.getRefPropertyContainer().getScope();
	}

	@Override
	public int getWeight() {
		return 2;
	}

	@Override
	public boolean isSame(AssociationItem associationItem) {
				
		if (!(associationItem instanceof RefAssociationItem)) {
			return false;
		}

		String aUnrefPropertyScope = formatValue(associationItem.getUnrefPropertyScope());
		String aRefPropertyScope = formatValue(associationItem.getRefPropertyScope());
		String aUnrefPropertyValue = formatValue(associationItem.getUnrefPropertyValue());
		String aRefPropertyValue = formatValue(associationItem.getRefPropertyValue());

		String refPropertyScope = this.getRefPropertyScope();
		String refPropertyValue = this.getRefPropertyValue();
		
		if (!aUnrefPropertyScope.isEmpty()) {
			return false;
		}
		
		if (!refPropertyScope.equals(aRefPropertyScope)) {
			return false;			
		}
		
		if (!aUnrefPropertyValue.isEmpty()) {
			return false;			
		}
		
		if (!refPropertyValue.equalsIgnoreCase(aRefPropertyValue)) {
			return false;			
		}
		return true;
	}


    @Override
    public String getUnrefPropertyName() {
        return null;
    }

    @Override
    public String getRefPropertyName() {
        return association.getRefPropertyContainer().getName();
    }

	@Override
	public String toString() {
		String associationString = association.toString();
		return String.format("[refAssociationItem:%s,%s,%s,%s]", label, configname, filename, associationString);
	}
}
