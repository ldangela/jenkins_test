package com.renault.tools.synchronization.config.webapp.report.renderer.impl;

import com.renault.tools.synchronization.config.webapp.om.AssociationComponent;
import com.renault.tools.synchronization.config.webapp.report.renderer.Renderer;


public abstract class CsvRenderer<T extends AssociationComponent> implements Renderer {
	
    private static final String TAB = "\t";

    private static final String END_LINE = "\n";

    private static final String SCV_SEPARATOR = "#";

    protected StringBuilder sb;
    
	private T component;
	
	protected CsvRenderer(StringBuilder sb, T component) {
		this.sb = sb;
		this.component = component;
	}

	@Override
	public void render() {
		render(component);
	}
	
	abstract void render(T component);

    public void appendValue(String value) {
        sb.append(value);
    }

    public void appendUpperValue(String value) {
        sb.append(value.toUpperCase());
    }

    public void appendTrimValue(String value) {
        sb.append(value.trim());
    }

    public void appendCsvTrimValue(String value) {
        sb.append(value.trim());
        sb.append(SCV_SEPARATOR);
    }

    public void appendCsvValue(String value) {
        sb.append(value);
        sb.append(SCV_SEPARATOR);
    }

    public void appendCsvUpperValue(String value) {
        sb.append(value.toUpperCase());
        sb.append(SCV_SEPARATOR);
    }

    public void appendCsvSeparator() {
        sb.append(SCV_SEPARATOR);
    }

    public void appendEndLine() {
        sb.append(END_LINE);
    }

    public void appendLine(String line) {
        sb.append(line);
        sb.append(END_LINE);        
    }

    public void appendTabLine(String line) {
        sb.append(TAB);
        sb.append(line);
        sb.append(END_LINE);        
    }
    
    public void append2TabsLine(String line) {
        sb.append(TAB);
        sb.append(TAB);
        sb.append(line);
        sb.append(END_LINE);        
    }

}
