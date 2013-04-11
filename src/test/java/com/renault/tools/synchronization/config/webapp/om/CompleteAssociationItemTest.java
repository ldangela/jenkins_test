package com.renault.tools.synchronization.config.webapp.om;

import org.junit.Assert;
import org.junit.Test;

import com.renault.tools.synchronization.config.webapp.association.impl.CompleteAssociation;

public class CompleteAssociationItemTest {

	@Test
	public void should_creat_a_complete_association() {
		PropertyContainer unrefPropertyContainer = PropertyContainer.createProperty("propertyName", ScopeFactory.buildLocalScope("unrefValue"));
		PropertyContainer refPropertyContainer = PropertyContainer.createProperty("propertyName", ScopeFactory.buildLocalCommonScope("refValue"));
		CompleteAssociation association = new CompleteAssociation(unrefPropertyContainer, refPropertyContainer);
		CompleteAssociationItem associationItem = new CompleteAssociationItem("label", "configname", "filename", association);
		
		Assert.assertEquals("configname", associationItem.getConfigname());
		Assert.assertEquals("filename", associationItem.getFilename());
		Assert.assertEquals("propertyName", associationItem.getPropertyName());
		Assert.assertEquals("LOCAL_COMMON", associationItem.getRefPropertyScope());
		Assert.assertEquals("LOCAL", associationItem.getUnrefPropertyScope());
		Assert.assertEquals("refValue", associationItem.getRefPropertyValue());
		Assert.assertEquals("unrefValue", associationItem.getUnrefPropertyValue());
	}
}
