package com.renault.tools.synchronization.config.webapp.report.renderer.impl;

import com.renault.tools.synchronization.config.webapp.om.RefAssociationItem;

public class RefLineCsvRenderer extends CsvRenderer<RefAssociationItem> {

    private String label;

    private String configname;
    
    private String filename;
    
    private String propertyName;
    
    private String refPropertyScope;
    
    private String refPropertyValue;
    
	public RefLineCsvRenderer(StringBuilder sb, RefAssociationItem refPropertyItem) {
		super(sb, refPropertyItem);
	}

	@Override
	public void render(RefAssociationItem component) {

	    label = component.getLabel();
		configname = component.getConfigname();
		filename = component.getFilename();
		propertyName = component.getPropertyName();
		refPropertyScope = component.getRefPropertyScope();
		refPropertyValue = component.getRefPropertyValue();
		
		appendCsvLine();
	}
    
    private void appendCsvLine() {
        appendCsvValue(label);
	    appendCsvValue(configname);
	    appendCsvValue(filename);
	    appendCsvSeparator();
	    appendCsvValue(refPropertyScope);
	    appendCsvSeparator();
	    appendCsvValue(propertyName);
	    appendCsvSeparator();
	    appendTrimValue(refPropertyValue);                    
	    appendEndLine();
    }
}
