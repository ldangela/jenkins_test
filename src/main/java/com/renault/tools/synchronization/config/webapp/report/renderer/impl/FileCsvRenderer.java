package com.renault.tools.synchronization.config.webapp.report.renderer.impl;

import java.util.List;

import com.renault.tools.synchronization.config.webapp.om.CompleteAssociationItem;
import com.renault.tools.synchronization.config.webapp.om.EmptyAssociationItem;
import com.renault.tools.synchronization.config.webapp.om.AssociationCatalog;
import com.renault.tools.synchronization.config.webapp.om.AssociationItem;
import com.renault.tools.synchronization.config.webapp.om.RefAssociationItem;
import com.renault.tools.synchronization.config.webapp.om.UnrefAssociationItem;
import com.renault.tools.synchronization.config.webapp.report.renderer.Renderer;

public class FileCsvRenderer extends CsvRenderer<AssociationCatalog> {

	private boolean showCompleteLine;
		
	public static FileCsvRenderer createDeltaFileCsvRender(StringBuilder sb, AssociationCatalog catalog) {
		return new FileCsvRenderer(sb, catalog, false);
	}
	
	public static FileCsvRenderer createCompleteFileCsvRender(StringBuilder sb, AssociationCatalog catalog) {
		return new FileCsvRenderer(sb, catalog, true);		
	}
	
	private FileCsvRenderer(StringBuilder sb, AssociationCatalog catalog, boolean showCompleteLine) {
		super(sb, catalog);
		this.showCompleteLine = showCompleteLine;
	}

	@Override
	public void render(AssociationCatalog catalog) {
		appendHeader();

		Renderer renderer = null;

		List<AssociationItem> associationItems = catalog.getSortedAssociationItems();
		for (AssociationItem associationItem : associationItems) {
			
			if (associationItem instanceof CompleteAssociationItem) {
				if (!showCompleteLine) {
					continue;
				}
				renderer = new CompleteLineCsvRenderer(sb, (CompleteAssociationItem) associationItem);
			}
			if (associationItem instanceof RefAssociationItem) {
				renderer = new RefLineCsvRenderer(sb, (RefAssociationItem) associationItem);
			}
			if (associationItem instanceof UnrefAssociationItem) {
				renderer = new UnrefLineCsvRenderer(sb, (UnrefAssociationItem) associationItem);
			}
			if (associationItem instanceof EmptyAssociationItem) {
				renderer = new EmptyLineCsvRenderer(sb, (EmptyAssociationItem) associationItem);
			}
			renderer.render();
		}
	}

    private void appendHeader() {
        appendCsvUpperValue("Label");
        appendCsvUpperValue("Configuration name");
        appendCsvUpperValue("File name");
        appendCsvUpperValue("Property scope");
        appendCsvUpperValue("Reference property scope");       
        appendCsvUpperValue("Property name");
        appendCsvUpperValue("Reference property name");
        appendCsvUpperValue("Property value");       
        appendUpperValue("Reference property value");       
        appendEndLine();
    }
}
