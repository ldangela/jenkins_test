package com.renault.tools.synchronization.config.webapp.om;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.renault.tools.synchronization.config.webapp.association.Association;
import com.renault.tools.synchronization.config.webapp.association.impl.AssociationKey;
import com.renault.tools.synchronization.config.webapp.association.impl.CompleteAssociation;
import com.renault.tools.synchronization.config.webapp.association.impl.RefAssociation;
import com.renault.tools.synchronization.config.webapp.association.impl.UnrefAssociation;

public class AssociationFileTest {

	@Test
	public void should_sort_associationItems_with_1_config_and_1_filename() {
		AssociationCatalog associationCatalog = new AssociationCatalog();
		
		putUnrefAssociationItem(associationCatalog, "label", "configname", "filename", "unref.property", "unref.value");
		putCompleteAssociationItem(associationCatalog, "label", "configname", "filename", "complete.property", "unref.value", "ref.value");
		putRefAssociationItem(associationCatalog, "label", "configname", "filename", "ref.property", "ref.value");
		
		List<AssociationItem> associationItems = associationCatalog.getSortedAssociationItems();
		
		assertAssociationItem(associationItems.get(0), "configname", "filename", "unref.property");
		assertAssociationItem(associationItems.get(1), "configname", "filename", "ref.property");
		assertAssociationItem(associationItems.get(2), "configname", "filename", "complete.property");
	}

	@Test
	public void should_sort_associationItems_with_1_config_and_2_filenames() {
		AssociationCatalog associationCatalog = new AssociationCatalog();
		
		putRefAssociationItem(associationCatalog, "label", "configname.A", "filename.B", "ref.property.B", "ref.value.B");
		putRefAssociationItem(associationCatalog, "label", "configname.A", "filename.A", "ref.property.A", "ref.value.A");
		putCompleteAssociationItem(associationCatalog, "label", "configname.A", "filename.B", "complete.property.B", "unref.value.B", "ref.value.B");
		putCompleteAssociationItem(associationCatalog, "label", "configname.A", "filename.A", "complete.property.A", "unref.value.A", "ref.value.A");
		putUnrefAssociationItem(associationCatalog, "label", "configname.A", "filename.B", "unref.property.B", "unref.value.B");
		putUnrefAssociationItem(associationCatalog, "label", "configname.A", "filename.A", "unref.property.A", "unref.value.A");
		
		List<AssociationItem> associationItems = associationCatalog.getSortedAssociationItems();
		
		assertAssociationItem(associationItems.get(0), "configname.A", "filename.A", "unref.property.A");
		assertAssociationItem(associationItems.get(1), "configname.A", "filename.A", "ref.property.A");
		assertAssociationItem(associationItems.get(2), "configname.A", "filename.A", "complete.property.A");
		assertAssociationItem(associationItems.get(3), "configname.A", "filename.B", "unref.property.B");
		assertAssociationItem(associationItems.get(4), "configname.A", "filename.B", "ref.property.B");
		assertAssociationItem(associationItems.get(5), "configname.A", "filename.B", "complete.property.B");
	}

	@Test
	public void should_sort_associationItems_with_2_config_and_2_filenames() {
		AssociationCatalog associationCatalog = new AssociationCatalog();
		
		putRefAssociationItem(associationCatalog, "label", "configname.A", "filename.B", "ref.property.B", "ref.value.B");
		putCompleteAssociationItem(associationCatalog, "label", "configname.A", "filename.A", "complete.property.A", "unref.value.A", "ref.value.A");
		putRefAssociationItem(associationCatalog, "label", "configname.B", "filename.A", "ref.property", "ref.value");
		putUnrefAssociationItem(associationCatalog, "label", "configname.A", "filename.A", "unref.property.A", "unref.value.A");
		putCompleteAssociationItem(associationCatalog, "label", "configname.B", "filename.A", "complete.property", "unref.value", "ref.value");
		putCompleteAssociationItem(associationCatalog, "label", "configname.A", "filename.B", "complete.property.B", "unref.value.B", "ref.value.B");
		putUnrefAssociationItem(associationCatalog, "label", "configname.B", "filename.A", "unref.property", "unref.value");
		putRefAssociationItem(associationCatalog, "label", "configname.A", "filename.A", "ref.property.A", "ref.value.A");
		putUnrefAssociationItem(associationCatalog, "label", "configname.A", "filename.B", "unref.property.B", "unref.value.B");
		
		List<AssociationItem> associationItems = associationCatalog.getSortedAssociationItems();
		
		assertAssociationItem(associationItems.get(0), "configname.A", "filename.A", "unref.property.A");
		assertAssociationItem(associationItems.get(1), "configname.A", "filename.A", "ref.property.A");
		assertAssociationItem(associationItems.get(2), "configname.A", "filename.A", "complete.property.A");
		assertAssociationItem(associationItems.get(3), "configname.A", "filename.B", "unref.property.B");
		assertAssociationItem(associationItems.get(4), "configname.A", "filename.B", "ref.property.B");
		assertAssociationItem(associationItems.get(5), "configname.A", "filename.B", "complete.property.B");
		assertAssociationItem(associationItems.get(6), "configname.B", "filename.A", "unref.property");
		assertAssociationItem(associationItems.get(7), "configname.B", "filename.A", "ref.property");
		assertAssociationItem(associationItems.get(8), "configname.B", "filename.A", "complete.property");
	}

	private void assertAssociationItem(AssociationItem associationItem, String configname, String filename, String propertyName) {
		Assert.assertEquals(configname, associationItem.getConfigname());
		Assert.assertEquals(filename, associationItem.getFilename());
		Assert.assertEquals(propertyName, associationItem.getPropertyName());		
	}
	
	private void putCompleteAssociationItem(AssociationCatalog associationFile, String label, String configname, String filename, String propertyName, String unrefPropertyValue, String refPropertyValue) {

		AssociationKey key = AssociationKey.create(label, configname, filename, propertyName);
		associationFile.putAssociationItem(key, createCompleteAssociationItem(label, configname, filename, propertyName, unrefPropertyValue, refPropertyValue));
	}
	
	private void putUnrefAssociationItem(AssociationCatalog associationFile, String label, String configname, String filename, String propertyName, String propertyValue) {

		AssociationKey key = AssociationKey.create(label, configname, filename, propertyName);
		associationFile.putAssociationItem(key, createUnrefAssociationItem(label, configname, filename, propertyName, propertyValue));
	}
	
	private void putRefAssociationItem(AssociationCatalog associationFile, String label, String configname, String filename, String propertyName, String propertyValue) {

		AssociationKey key = AssociationKey.create(label, configname, filename, propertyName);
		associationFile.putAssociationItem(key, createRefAssociationItem(label, configname, filename, propertyName, propertyValue));
	}
	
	private AssociationItem createCompleteAssociationItem(String label, String configname, String filename, String propertyName, String unrefPropertyValue, String refPropertyValue) {
		
		PropertyContainer unrefPropertyContainer = PropertyContainer.createProperty(propertyName, ScopeFactory.buildLocalScope(unrefPropertyValue));
		PropertyContainer refPropertyContainer = PropertyContainer.createProperty(propertyName, ScopeFactory.buildLocalScope(refPropertyValue));
		Association association = new CompleteAssociation(unrefPropertyContainer, refPropertyContainer);
		return new CompleteAssociationItem(label, configname, filename, association);
	}

	private AssociationItem createUnrefAssociationItem(String label, String configname, String filename, String propertyName, String propertyValue) {
		
		PropertyContainer unrefPropertyContainer = PropertyContainer.createProperty(propertyName, ScopeFactory.buildLocalScope(propertyValue));
		Association association = new UnrefAssociation(unrefPropertyContainer);
		return new UnrefAssociationItem(label, configname, filename, association);
	}

	private AssociationItem createRefAssociationItem(String label, String configname, String filename, String propertyName, String propertyValue) {
		
		PropertyContainer refPropertyContainer = PropertyContainer.createProperty(propertyName, ScopeFactory.buildLocalScope(propertyValue));
		Association association = new RefAssociation(refPropertyContainer);
		return new RefAssociationItem(label, configname, filename, association);
	}
}
