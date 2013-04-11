package com.renault.tools.synchronization.config.webapp.report.renderer.impl;

import com.renault.tools.synchronization.config.webapp.om.UnrefAssociationItem;

public class UnrefLineCsvRenderer extends CsvRenderer<UnrefAssociationItem> {

    private String label;
    
    private String configname;
    
    private String filename;
    
    private String propertyName;
    
    private String unrefPropertyScope;
    
    private String unrefPropertyValue;

	public UnrefLineCsvRenderer(StringBuilder sb, UnrefAssociationItem unrefPropertyItem) {
		super(sb, unrefPropertyItem);
	}

	@Override
	public void render(UnrefAssociationItem component) {
		
        label = component.getLabel();
		configname = component.getConfigname();
		filename = component.getFilename();
		propertyName = component.getPropertyName();
		unrefPropertyScope = component.getUnrefPropertyScope();
		unrefPropertyValue = component.getUnrefPropertyValue();
		
		appendCsvLine();		
	}
 
    private void appendCsvLine() {
        appendCsvValue(label);
	    appendCsvValue(configname);
	    appendCsvValue(filename);
	    appendCsvValue(unrefPropertyScope);
	    appendCsvSeparator();
	    appendCsvValue(propertyName);
	    appendCsvSeparator();
	    appendCsvTrimValue(unrefPropertyValue);                    
	    appendEndLine();
    }
}
