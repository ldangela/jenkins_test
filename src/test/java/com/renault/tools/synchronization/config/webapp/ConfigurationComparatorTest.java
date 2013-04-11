package com.renault.tools.synchronization.config.webapp;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.renault.tools.synchronization.config.webapp.om.AssociationCatalog;
import com.renault.tools.synchronization.config.webapp.om.AssociationCatalogFactory;
import com.renault.tools.synchronization.config.webapp.om.AssociationItem;
import com.renault.tools.synchronization.config.webapp.om.NullAssociationItem;
import com.renault.tools.synchronization.config.webapp.operation.Operation;
import com.renault.tools.synchronization.config.webapp.operation.OperationCatalog;

public class ConfigurationComparatorTest {

    private final static String CSV_DELIMITER = "#";

    private static ConfigurationComparator configurationComparator;
	
	@BeforeClass
	public static void init() {
	    configurationComparator = new ConfigurationComparator();
	}

    @Test
    public void should_have_no_difference() {

        AssociationCatalog newAssociationCatalog = createAssociationCatalog("13.1#dacia-fr#common.properties#LOCAL#LOCAL#prop.A#prop.A#value1#value2");
        AssociationCatalog oldAssociationCatalog = createAssociationCatalog("13.1#dacia-fr#common.properties#LOCAL#LOCAL#prop.A#prop.A#value1#value2");
        
        OperationCatalog operationCatalog = configurationComparator.compare("group1", newAssociationCatalog, oldAssociationCatalog);
        
        Assert.assertTrue(operationCatalog.isEmpty());
    }

    @Test
    public void should_have_one_update() {

        AssociationCatalog newAssociationCatalog = createAssociationCatalog("13.1#dacia-fr#common.properties#LOCAL#LOCAL#prop.A#prop.A#new value#value2");
        AssociationCatalog oldAssociationCatalog = createAssociationCatalog("13.1#dacia-fr#common.properties#LOCAL#LOCAL#prop.A#prop.A#value1#value2");
        
        OperationCatalog operationCatalog = configurationComparator.compare("group1", newAssociationCatalog, oldAssociationCatalog);
        
        List<Operation> operationList = operationCatalog.getOperations();
        
        Assert.assertEquals(1, operationList.size());
        Assert.assertTrue(operationList.get(0).isUpdateOperation());
        
        AssociationItem newAssociationItem = operationList.get(0).getNewAssociationItem();
        AssociationItem oldAssociationItem = operationList.get(0).getOldAssociationItem();        
        Assert.assertNotSame(newAssociationItem.getUnrefPropertyValue(), oldAssociationItem.getUnrefPropertyValue());        
    }

    @Test
    public void should_have_one_delete() {

        AssociationCatalog newAssociationCatalog = createAssociationCatalog("");
        AssociationCatalog oldAssociationCatalog = createAssociationCatalog("13.1#dacia-fr#common.properties#LOCAL#LOCAL#prop.A#prop.A#value1#value2");
        
        OperationCatalog operationCatalog = configurationComparator.compare("group1", newAssociationCatalog, oldAssociationCatalog);
        
        List<Operation> operationList = operationCatalog.getOperations();
        
        Assert.assertEquals(1, operationList.size());
        Assert.assertTrue(operationList.get(0).isDeleteOperation());
        AssociationItem newAssociationItem = operationList.get(0).getNewAssociationItem();
        Assert.assertTrue(newAssociationItem instanceof NullAssociationItem);
    }

    @Test
    public void should_have_one_creation() {

        AssociationCatalog newAssociationCatalog = createAssociationCatalog("13.1#dacia-fr#common.properties#LOCAL#LOCAL#prop.A#prop.A#value1#value2");
        AssociationCatalog oldAssociationCatalog = createAssociationCatalog("");
        
        OperationCatalog operationCatalog = configurationComparator.compare("group1", newAssociationCatalog, oldAssociationCatalog);
        
        List<Operation> operationList = operationCatalog.getOperations();
        
        Assert.assertEquals(1, operationList.size());
        Assert.assertTrue(operationList.get(0).isCreateOperation());
        AssociationItem oldAssociationItem = operationList.get(0).getOldAssociationItem();
        Assert.assertTrue(oldAssociationItem instanceof NullAssociationItem);
    }

    @Test
    public void should_have_all_differences() {

        AssociationCatalog newAssociationCatalog = createAssociationCatalog("13.1#dacia-fr#common.properties#LOCAL#LOCAL#prop.A#prop.A#new value#value2", "13.1#dacia-fr#common.properties#LOCAL#LOCAL#prop.B#prop.B#value1#value2");
        AssociationCatalog oldAssociationCatalog = createAssociationCatalog("13.1#dacia-fr#common.properties#LOCAL#LOCAL#prop.A#prop.A#value1#value2", "13.1#dacia-fr#common.properties#LOCAL#LOCAL#prop.C#prop.C#value1#value2");
        
        OperationCatalog operationCatalog = configurationComparator.compare("group1", newAssociationCatalog, oldAssociationCatalog);
        
        List<Operation> operationList = operationCatalog.getOperations();
        
        Assert.assertEquals(3, operationList.size());
        Assert.assertTrue(operationList.get(0).isCreateOperation());
        Assert.assertTrue(operationList.get(1).isDeleteOperation());
        Assert.assertTrue(operationList.get(2).isUpdateOperation());
    }

    private AssociationCatalog createAssociationCatalog(String...lines) {
        List<String> list = Arrays.asList(lines);
        return AssociationCatalogFactory.createAssociationCatalogFrom(list, CSV_DELIMITER);
    }

}
