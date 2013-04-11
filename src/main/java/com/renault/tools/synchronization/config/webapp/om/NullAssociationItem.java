package com.renault.tools.synchronization.config.webapp.om;

import org.apache.commons.lang.StringUtils;

public class NullAssociationItem extends AbstractAssociationItem {

	NullAssociationItem() {
	}

    @Override
    public String getLabel() {
        return StringUtils.EMPTY;
    }

	@Override
	public String getConfigname() {
		return StringUtils.EMPTY;
	}

	@Override
	public String getFilename() {
		return StringUtils.EMPTY;
	}

	@Override
	public String getPropertyName() {
		return StringUtils.EMPTY;
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
	public int getWeight() {
		return 0;
	}

	@Override
	public boolean isSame(AssociationItem associationItem) {
		return true;
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
	public String toString() {
		return "[noneAssociationItem:NONE]";
	}

}
