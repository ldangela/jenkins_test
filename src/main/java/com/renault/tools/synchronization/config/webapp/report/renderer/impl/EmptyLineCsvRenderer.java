package com.renault.tools.synchronization.config.webapp.report.renderer.impl;

import com.renault.tools.synchronization.config.webapp.om.EmptyAssociationItem;

public class EmptyLineCsvRenderer extends CsvRenderer<EmptyAssociationItem> {

    private String label;

    private String configname;
    
    private String filename;

	public EmptyLineCsvRenderer(StringBuilder sb, EmptyAssociationItem emptyPropertyItem) {
		super(sb, emptyPropertyItem);
	}

	@Override
	public void render(EmptyAssociationItem component) {
		
        label = component.getLabel();
		configname = component.getConfigname();
		filename = component.getFilename();
		
		appendCsvLine();
	}
    
    private void appendCsvLine() {
        appendCsvValue(label);
	    appendCsvValue(configname);
	    appendCsvValue(filename);
	    appendCsvSeparator();
	    appendCsvSeparator();
	    appendCsvSeparator();
	    appendCsvSeparator();
	    appendCsvSeparator();
	    appendEndLine();
    }
}
