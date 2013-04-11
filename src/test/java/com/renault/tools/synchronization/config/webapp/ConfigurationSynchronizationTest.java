package com.renault.tools.synchronization.config.webapp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.renault.tools.synchronization.config.webapp.om.Configuration;
import com.renault.tools.synchronization.config.webapp.om.ConfigurationFactory;
import com.renault.tools.synchronization.config.webapp.report.ConfigReport;
import com.renault.tools.synchronization.config.webapp.report.PropReport;
import com.renault.tools.synchronization.config.webapp.report.SyncReport;

public class ConfigurationSynchronizationTest {

	private final static String FILE_SEPARATOR = "/";

    private final static boolean IS_SYNCRONIZED = true;

    private final static boolean IS_UNSYNCRONIZED = false;

	private static String basedir;

    private Configuration referenceConfiguration;

	private List<Configuration> otherConfigurations;
	
	@BeforeClass
	public static void init() {
		String path = ConfigurationSynchronizationTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		basedir = StringUtils.substringBefore(path, FILE_SEPARATOR + "target") + FILE_SEPARATOR + "src" + FILE_SEPARATOR + "test" + FILE_SEPARATOR + "resources";
	}

    @Before
    public void initConfiguration() {
        referenceConfiguration = null;
        otherConfigurations = new ArrayList<Configuration>();
    }

    @Test
    public void should_be_synchronized_with_otherConfiguration_empty() {

        // load configurations        
        loadReferenceConfigurationWithSharedFile("label", "config-A-REF");
        loadOtherConfigurationWithSharedFile("label", "config-A");
        
        // run
        SyncReport report = runSynchronisation();
        
        // assertions
        assertSyncReport(report, 1, IS_SYNCRONIZED);        
        ConfigReport confReport = report.getConfigReports().get(0);
        assertConfReport(confReport, "config-A", "config-A-REF", 0);
    }
    
	@Test
	public void should_be_unsynchronized_with_property_not_found_in_referenceConfiguration() {

        // load configurations        
        loadReferenceConfigurationWithSharedFile("label", "config-B-REF");
        loadOtherConfigurationWithSharedFile("label", "config-B");
        
        // run
		SyncReport report = runSynchronisation();
		
        // assertions
		assertSyncReport(report, 1, IS_UNSYNCRONIZED);
		ConfigReport confReport = report.getConfigReports().get(0);
		assertConfReport(confReport, "config-B", "config-B-REF", 1);
        assertPropReport(confReport.getPropReports().get(0), "file1.properties", new String[] {}, new String[] {"prop.A"});
	}

    @Test
    public void should_be_unsynchronized_with_property_not_found_in_otherConfiguration() {

        // load configurations        
        loadReferenceConfigurationWithSharedFile("label", "config-C-REF");
        loadOtherConfigurationWithSharedFile("label", "config-C");
        
        // run
        SyncReport report = runSynchronisation();
                
        // assertions
        assertSyncReport(report, 1, IS_UNSYNCRONIZED);        
        ConfigReport confReport = report.getConfigReports().get(0);
        assertConfReport(confReport, "config-C", "config-C-REF", 1);
        assertPropReport(confReport.getPropReports().get(0), "file1.properties", new String[] {"prop.A"}, new String[] {});
    }

    @Test
    public void should_be_synchronized_with_shared_property_in_referenceConfiguration() {

        // load configurations        
        loadReferenceConfigurationWithSharedFile("label", "config-D-REF");
        loadOtherConfigurationWithSharedFile("label", "config-D");
        
        // run
        SyncReport report = runSynchronisation();

        // assertions
        assertSyncReport(report, 1, IS_SYNCRONIZED);
    }

    @Test
    public void should_be_synchronized_with_shared_property_in_otherConfiguration() {

        // load configurations        
        loadReferenceConfigurationWithSharedFile("label", "config-E-REF");
        loadOtherConfigurationWithSharedFile("label", "config-E");
        
        // run
        SyncReport report = runSynchronisation();
        
        // assertions        
        assertSyncReport(report, 1, IS_SYNCRONIZED);
    }

    @Test
    public void should_be_synchronized_with_several_shared_files_in_otherConfiguration() {

        // load configurations        
        loadReferenceConfigurationWithSharedAndCommonFiles("label", "config-F-REF");
        loadOtherConfigurationWithSharedAndCommonFiles("label", "config-F");
        
        // run
        SyncReport report = runSynchronisation();
        
        // assertions        
        assertSyncReport(report, 1, IS_SYNCRONIZED);
    }

    @Test
    public void should_be_unsynchronized_with_property_in_the_referenceConfiguration_common_local() {

        // load configurations        
        loadReferenceConfigurationWithSharedAndCommonFiles("label", "config-G-REF");
        loadOtherConfigurationWithSharedAndCommonFiles("label", "config-G");
        
        // run
        SyncReport report = runSynchronisation();
        
        // assertions
        assertSyncReport(report, 1, IS_UNSYNCRONIZED);
        ConfigReport confReport = report.getConfigReports().get(0);
        assertConfReport(confReport, "config-G", "config-G-REF", 2);
        assertPropReport(confReport.getPropReports().get(0), "common.properties", new String[] {"prop.LOCAL_COMMON"}, new String[] {});
        assertPropReport(confReport.getPropReports().get(1), "file1.properties", new String[] {}, new String[] {});
    }

    @Test
    public void should_be_unsynchronized_with_property_in_the_otherConfiguration_common_local() {

        // load configurations        
        loadReferenceConfigurationWithSharedAndCommonFiles("label", "config-H-REF");
        loadOtherConfigurationWithSharedAndCommonFiles("label", "config-H");
        
        // run
        SyncReport report = runSynchronisation();
        
        // assertions
        assertSyncReport(report, 1, IS_UNSYNCRONIZED);
        ConfigReport confReport = report.getConfigReports().get(0);
        assertConfReport(confReport, "config-H", "config-H-REF", 2);
        assertPropReport(confReport.getPropReports().get(0), "common.properties", new String[] {}, new String[] {"prop.LOCAL_COMMON"});
        assertPropReport(confReport.getPropReports().get(1), "file1.properties", new String[] {}, new String[] {});
    }

    @Test(expected = RuntimeException.class)
    public void should_throws_exception_if_referenceConfiguration_not_found() throws Exception {

        // try to load referenced configuration        
        loadReferenceConfigurationWithSharedAndCommonFiles("LABEL", "XXX");
    }

    private SyncReport runSynchronisation() {
        ConfigurationSynchronizer synchronizer = new ConfigurationSynchronizer(otherConfigurations, referenceConfiguration);
        return synchronizer.synchronize();
    }
    
    private void loadOtherConfigurationWithSharedFile(String label, String configname) {
        Configuration configuration = ConfigurationFactory.buildBasicConfiguration(label, configname, basedir, "/" + configname);
        addSharedFileTo(configuration);
        otherConfigurations.add(configuration);
    }

    private void loadReferenceConfigurationWithSharedFile(String label, String configname) {
        referenceConfiguration = ConfigurationFactory.buildBasicConfiguration(label, configname, basedir, FILE_SEPARATOR + configname);
        addSharedFileTo(referenceConfiguration);
    }
    
    private void loadOtherConfigurationWithSharedAndCommonFiles(String label, String configname) {
        Configuration configuration = ConfigurationFactory.buildBasicConfiguration(label, configname, basedir, "/" + configname);
        addSharedFileTo(configuration);
        addCommonFileTo(configuration);
        otherConfigurations.add(configuration);
    }

    private void loadReferenceConfigurationWithSharedAndCommonFiles(String label, String configname) {
        referenceConfiguration = ConfigurationFactory.buildBasicConfiguration(label, configname, basedir, FILE_SEPARATOR + configname);
        addSharedFileTo(referenceConfiguration);
        addCommonFileTo(referenceConfiguration);
    }

	/**
	 * Add shared properties file to configuration.
	 * 
	 * @param configuration
	 */
	private static void addSharedFileTo(Configuration configuration) {
	    configuration.putCommonPropertiesFile("shared.properties", new File(basedir + "/shared.properties"));
	}

    /**
     * Add common properties file to configuration.
     * 
     * @param configuration
     */
    private static void addCommonFileTo(Configuration configuration) {
        configuration.putCommonPropertiesFile("common.properties", new File(basedir + "/common.properties"));
    }

	private void assertSyncReport(SyncReport syncReport, int nbrConfReports, boolean isSynchronized) {
        Assert.assertEquals(nbrConfReports, syncReport.getConfigReports().size());
        Assert.assertEquals(isSynchronized, syncReport.isSynchronized());
	}
	   
	private void assertConfReport(ConfigReport confReport, String configname, String referenceConfigName, int nbrPropReports) {
        Assert.assertEquals(configname, confReport.getConfigname());
        Assert.assertEquals(referenceConfigName, confReport.getReferenceConfigname());
        Assert.assertEquals(nbrPropReports, confReport.getPropReports().size());
	}

    private void assertPropReport(PropReport propReport, String filename, String[] propNameInRef, String[] propNameNotInRef) {
        Assert.assertEquals(filename, propReport.getFilename());
        
        Assert.assertEquals(propNameInRef.length, propReport.getRefAssociations().size());
        for (String propertyName : propNameInRef) {
            Assert.assertTrue(propReport.containsPropertyAssociation(propertyName));
        }
        
        Assert.assertEquals(propNameNotInRef.length, propReport.getUnrefAssociations().size());
        for (String propName : propNameNotInRef) {
            Assert.assertTrue(propReport.containsPropertyAssociation(propName));
        }
    }
    
}
