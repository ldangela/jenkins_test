package com.renault.tools.synchronization.config.webapp.factory;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.renault.tools.synchronization.config.webapp.om.AssociationItem;
import com.renault.tools.synchronization.config.webapp.om.AssociationItemFactory;
import com.renault.tools.synchronization.config.webapp.om.CompleteAssociationItem;
import com.renault.tools.synchronization.config.webapp.om.EmptyAssociationItem;
import com.renault.tools.synchronization.config.webapp.om.RefAssociationItem;
import com.renault.tools.synchronization.config.webapp.om.UnrefAssociationItem;

public class AssociationItemFactoryTest {

    private static final String CSV_DELIMITER = "#";
    
	@Test
	public void should_create_a_complete_associationItem_from_string() {
		AssociationItem associationItem = AssociationItemFactory.createAssociationItem(CSV_DELIMITER, "label#configname#filename#LOCAL#LOCAL_COMMON#propertyName#propertyName#unrefValue#refValue");
		
		Assert.assertTrue(associationItem instanceof CompleteAssociationItem);
		Assert.assertEquals("configname", associationItem.getConfigname());
		Assert.assertEquals("filename", associationItem.getFilename());
		Assert.assertEquals("propertyName", associationItem.getPropertyName());
		Assert.assertEquals("LOCAL", associationItem.getUnrefPropertyScope());
		Assert.assertEquals("LOCAL_COMMON", associationItem.getRefPropertyScope());
		Assert.assertEquals("unrefValue", associationItem.getUnrefPropertyValue());
		Assert.assertEquals("refValue", associationItem.getRefPropertyValue());
	}

	@Test
	public void should_create_a_unref_associationItem_from_string() {
		AssociationItem associationItem = AssociationItemFactory.createAssociationItem(CSV_DELIMITER, "label#configname#filename#LOCAL##propertyName##unrefValue#");
		
		Assert.assertTrue(associationItem instanceof UnrefAssociationItem);
		Assert.assertEquals("configname", associationItem.getConfigname());
		Assert.assertEquals("filename", associationItem.getFilename());
		Assert.assertEquals("propertyName", associationItem.getPropertyName());
		Assert.assertEquals("LOCAL", associationItem.getUnrefPropertyScope());
		Assert.assertEquals(StringUtils.EMPTY, associationItem.getRefPropertyScope());
		Assert.assertEquals("unrefValue", associationItem.getUnrefPropertyValue());
		Assert.assertEquals(StringUtils.EMPTY, associationItem.getRefPropertyValue());
	}

	@Test
	public void should_create_a_ref_associationItem_from_string() {
		AssociationItem associationItem = AssociationItemFactory.createAssociationItem(CSV_DELIMITER, "label#configname#filename##LOCAL##propertyName##refValue");
		
		Assert.assertTrue(associationItem instanceof RefAssociationItem);
		Assert.assertEquals("configname", associationItem.getConfigname());
		Assert.assertEquals("filename", associationItem.getFilename());
		Assert.assertEquals("propertyName", associationItem.getPropertyName());
		Assert.assertEquals(StringUtils.EMPTY, associationItem.getUnrefPropertyScope());
		Assert.assertEquals("LOCAL", associationItem.getRefPropertyScope());
		Assert.assertEquals(StringUtils.EMPTY, associationItem.getUnrefPropertyValue());
		Assert.assertEquals("refValue", associationItem.getRefPropertyValue());
	}

	@Test
	public void should_create_a_empty_associationItem_from_string() {
		AssociationItem associationItem = AssociationItemFactory.createAssociationItem(CSV_DELIMITER, "label#configname#filename######");
		
		Assert.assertTrue(associationItem instanceof EmptyAssociationItem);
		Assert.assertEquals("configname", associationItem.getConfigname());
		Assert.assertEquals("filename", associationItem.getFilename());
		Assert.assertEquals("NONE", associationItem.getPropertyName());
		Assert.assertEquals(StringUtils.EMPTY, associationItem.getUnrefPropertyScope());
		Assert.assertEquals(StringUtils.EMPTY, associationItem.getRefPropertyScope());
		Assert.assertEquals(StringUtils.EMPTY, associationItem.getUnrefPropertyValue());
		Assert.assertEquals(StringUtils.EMPTY, associationItem.getRefPropertyValue());
	}
}
