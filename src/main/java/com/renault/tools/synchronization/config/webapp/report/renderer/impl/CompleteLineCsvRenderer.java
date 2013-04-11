package com.renault.tools.synchronization.config.webapp.report.renderer.impl;

import com.renault.tools.synchronization.config.webapp.om.CompleteAssociationItem;

public class CompleteLineCsvRenderer extends CsvRenderer<CompleteAssociationItem> {

    private String label;
    
    private String configname;
    
    private String filename;
    
    private String propertyName;
    
    private String unrefPropertyScope;
    
    private String refPropertyScope;
    
    private String unrefPropertyValue;
    
    private String refPropertyValue;

	public CompleteLineCsvRenderer(StringBuilder sb,CompleteAssociationItem completeItem) {
		super(sb, completeItem);
	}
	
	@Override
	public void render(CompleteAssociationItem component) {

        label = component.getLabel();
		configname = component.getConfigname();
		filename = component.getFilename();
		propertyName = component.getPropertyName();
		unrefPropertyScope = component.getUnrefPropertyScope();
		refPropertyScope = component.getRefPropertyScope();
		unrefPropertyValue = component.getUnrefPropertyValue();
		refPropertyValue = component.getRefPropertyValue();

		appendCsvLine();
	}
    
    private void appendCsvLine() {
        appendCsvValue(label);
	    appendCsvValue(configname);
	    appendCsvValue(filename);
	    appendCsvValue(unrefPropertyScope);
	    appendCsvValue(refPropertyScope);
	    appendCsvValue(propertyName);
	    appendCsvValue(propertyName);
	    appendCsvTrimValue(unrefPropertyValue);                    
	    appendTrimValue(refPropertyValue);                    
	    appendEndLine();
	}
}
