package com.renault.tools.synchronization.config.webapp.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.renault.tools.synchronization.config.webapp.ConfigurationComparator;
import com.renault.tools.synchronization.config.webapp.ConfigurationSynchronizer;
import com.renault.tools.synchronization.config.webapp.client.google.docs.GoogleAppsClient;
import com.renault.tools.synchronization.config.webapp.om.AssociationCatalog;
import com.renault.tools.synchronization.config.webapp.om.AssociationCatalogFactory;
import com.renault.tools.synchronization.config.webapp.om.Configuration;
import com.renault.tools.synchronization.config.webapp.om.ConfigurationFactory;
import com.renault.tools.synchronization.config.webapp.operation.OperationCatalog;
import com.renault.tools.synchronization.config.webapp.report.SyncReport;

/**
 * Check all webapps configurations (folder with *.properties files). There are 4 groups :
 *  - renault frontoffice (we use the renault-fr/frontoffice/.../config as reference)  
 *  - renault backoffice  (we use the renault-fr/backoffice/.../config as reference)
 *  - dacia frontoffice   (we use the dacia-fr/frontoffice/.../config as reference)    
 *  - dacia backoffice    (we use the dacia-fr/backoffice/.../config as reference)
 * 
 * We take into account all properties in shared.properties, common.properties or common-cas.properties files in eplatform/infrastructure/../config.
 * 
 * TODO separate this unit test from the tool synchronization-config-webapp
 * 
 * @author dangela
 *
 */
public class RenaultSynchronizationConfigWebappTest {

    private static final Logger LOGGER = Logger.getLogger(RenaultSynchronizationConfigWebappTest.class);

    private final static String FILE_SEPARATOR = "/";

    private static final String ARG_LABEL = "synchronization-config-webapp-label";

    private static final String ARG_CSV_BASEDIR = "csvBasedir";

    private static GoogleAppsClient googleAppsClient;

	private static String basedir;

    private static String csvBasedir;

    private static String label;

	@BeforeClass
	public static void init() throws Exception {
		String path = RenaultSynchronizationConfigWebappTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		basedir = StringUtils.substringBefore(path, FILE_SEPARATOR + "tools" + FILE_SEPARATOR + "synchronization-config-webapp");
		googleAppsClient = new GoogleAppsClient("laurent.dangela@fullsix.com", "S1aSnb3a");
		initParams();
	}
	
	private static void initParams() {

	    String argValue = System.getProperty(ARG_CSV_BASEDIR);
        if (StringUtils.trimToNull(argValue) == null) {
            throw new IllegalArgumentException("The csvBasedir parameter " + ARG_CSV_BASEDIR + " is mandatory.");
        }
        csvBasedir = argValue;
        LOGGER.info("arg csvBasedir used : " + csvBasedir);        

        argValue = System.getProperty(ARG_LABEL);
        if (StringUtils.trimToNull(argValue) == null) {
            throw new IllegalArgumentException("The label parameter " + ARG_LABEL + " is mandatory.");
        }
        label = argValue;
        LOGGER.info("arg label used : " + label);        
	}
	
	@Test
	public void should_be_synchronized_dacia_backoffice_configurations() throws Exception {

        List<Configuration> configurations = new ArrayList<Configuration>();
        loadDaciaBackofficeConfigurations(configurations, basedir, label);
	    
	    boolean result = isSynchronize(configurations, "Dacia backoffice", "Dacia backoffice", "Config properties", "dacia-fr", "/dacia-fr/backoffice/src/main/webapp/WEB-INF/config", "dacia-bo-synchro-config-webapp-tool.csv");
		Assert.assertTrue(result);
	}
	
	@Test
	public void should_be_synchronized_dacia_frontoffice_configurations() throws Exception {

        List<Configuration> configurations = new ArrayList<Configuration>();
        loadDaciaFrontofficeConfigurations(configurations, basedir, label);

        boolean result = isSynchronize(configurations, "Dacia frontoffice", "Dacia frontoffice", "Config properties", "dacia-fr", "/dacia-fr/frontoffice/src/main/webapp/WEB-INF/config", "dacia-fo-synchro-config-webapp-tool.csv");
        Assert.assertTrue(result);
	}

	@Test
	public void should_be_synchronized_renault_backoffice_configurations() throws Exception {

        List<Configuration> configurations = new ArrayList<Configuration>();
        loadRenaultBackofficeConfigurations(configurations, basedir, label);

        boolean result = isSynchronize(configurations, "Renault backoffice", "Renault backoffice", "Config properties", "renault-fr", "/renault-fr/backoffice/src/main/webapp/WEB-INF/config", "renault-bo-synchro-config-webapp-tool.csv");
        Assert.assertTrue(result);
	}

	@Test
	public void should_be_synchronized_renault_frontoffice_configurations() throws Exception {

        List<Configuration> configurations = new ArrayList<Configuration>();
        loadRenaultFrontofficeConfigurations(configurations, basedir, label);

        boolean result = isSynchronize(configurations, "Renault frontoffice", "Renault frontoffice", "Config properties", "renault-fr", "/renault-fr/frontoffice/src/main/webapp/WEB-INF/config", "renault-fo-synchro-config-webapp-tool.csv");
        Assert.assertTrue(result);
	}

	/**
	 * Check if configurations are synchronized.
	 * Find differences between the configurations. 
	 * Update Google spreadsheets if there is at least one changement
	 * Save csv file.
	 * 
	 * @param configurations
	 * @param spreadsheet
	 * @param worksheet
	 * @param configname
	 * @param configDirname
	 * @param csvFilename
	 * @return
	 * @throws Exception
	 */
    private boolean isSynchronize(List<Configuration> configurations, String groupName, String spreadsheet, String worksheet, String configname, String configDirname, String csvFilename) throws Exception {

        Configuration referenceConfiguration = ConfigurationFactory.buildBackofficeConfiguration(label, configname, basedir, configDirname);
        ConfigurationSynchronizer synchronizer = new ConfigurationSynchronizer(configurations, referenceConfiguration);
        SyncReport syncReport = synchronizer.synchronize();
        
        if (!syncReport.isSynchronized()) {
            AssociationCatalog localAssociationCatalog = AssociationCatalogFactory.createAssociationCatalogFrom(syncReport);

            File csvFile = new File(csvBasedir + FILE_SEPARATOR + csvFilename);
            AssociationCatalog googleAssociationCatalog = AssociationCatalogFactory.createAssociationCatalogFrom(csvFile, "#", 1);

            OperationCatalog operationCatalog = compareWithGoogleDocuments(groupName, localAssociationCatalog, googleAssociationCatalog);
            
            if (!operationCatalog.isEmpty()) {
                googleAppsClient.updateSynchroConfigWebappWorksheet(spreadsheet, worksheet, operationCatalog, 1);
            }

            File newCsvFile = new File(csvBasedir + FILE_SEPARATOR + csvFilename);            
            localAssociationCatalog.saveAll(newCsvFile);
        }
        
        return syncReport.isSynchronized();
    }

    private OperationCatalog compareWithGoogleDocuments(String groupName, AssociationCatalog newAssociationCatalog, AssociationCatalog oldAssociationCatalog) throws Exception {

        ConfigurationComparator comparator = new ConfigurationComparator();
        return comparator.compare(groupName, newAssociationCatalog, oldAssociationCatalog);
    }    

    private static void loadRenaultFrontofficeConfigurations(List<Configuration> configurations, String basedir, String label) {
//   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "lmt", basedir, "/lmt/lmt-frontoffice-gwt/lmt-frontoffice-mygwt/src/main/webapp/WEB-INF/config"));
        configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-fr", basedir, "/renault-fr/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-at", basedir, "/renault-at/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-be", basedir, "/renault-be/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-br", basedir, "/renault-br/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-central", basedir, "/renault-central/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-ch", basedir, "/renault-ch/frontoffice/src/main/webapp/WEB-INF/config"));
//   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-cms-v4", basedir, "/renault-cms-v4/client/webapp/src/main/webapp/WEB-INF"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-cz", basedir, "/renault-cz/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-de", basedir, "/renault-de/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-dk", basedir, "/renault-dk/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-dz", basedir, "/renault-dz/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-es", basedir, "/renault-es/frontoffice/src/main/webapp/WEB-INF/config"));
//   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-esc-portal", basedir, "/renault-esc-portal/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-hr", basedir, "/renault-hr/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-hu", basedir, "/renault-hu/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-ie", basedir, "/renault-ie/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-it", basedir, "/renault-it/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-lu", basedir, "/renault-lu/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-ma", basedir, "/renault-ma/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-master", basedir, "/renault-master/frontoffice/src/main/webapp/WEB-INF/config"));
//   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-multimedia", basedir, "/renault-multimedia/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-mx", basedir, "/renault-mx/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-nl", basedir, "/renault-nl/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-pl", basedir, "/renault-pl/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-pt", basedir, "/renault-pt/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-ro", basedir, "/renault-ro/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-ru", basedir, "/renault-ru/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-se", basedir, "/renault-se/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-si", basedir, "/renault-si/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-sk", basedir, "/renault-sk/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-tr", basedir, "/renault-tr/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-ua", basedir, "/renault-ua/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-uk", basedir, "/renault-uk/frontoffice/src/main/webapp/WEB-INF/config"));
//   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration("renault-webstore", basedir, "/renault-webstore/webstore-frontoffice/src/main/webapp/WEB-INF/config"));
//   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration("rplug", basedir, "/rplug/frontoffice/src/main/webapp/WEB-INF/config"));
   		// présence d'un fichier shared.properties
//   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration("rtb", basedir, "/rtb/renault-fr-rtb/frontoffice/src/main/webapp/WEB-INF/config"));
//   		configurations.add(ConfigurationFactory.buildFrontofficeConfiguration("toprenault", basedir, "/toprenault/toprenault-frontoffice/src/main/webapp/WEB-INF/config"));
    }
    
    private static void loadRenaultBackofficeConfigurations(List<Configuration> configurations, String basedir, String label) {
    	// pas de fichier properties
//          configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dailyreport", basedir, "/dailyreport-backoffice/src/main/webapp/WEB-INF"));
//          configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "flyingblue", basedir, "/flyingblue/flyingblue-backoffice/src/main/webapp/WEB-INF/shell/config"));
//  		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "leadqualif", basedir, "/leadqualif/leadqualif-backoffice/src/main/webapp/WEB-INF/config"));
//   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "lmt", basedir, "/lmt/lmt-backoffice/src/main/webapp/WEB-INF/config"));
//   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "network-backoffice", basedir, "/network-backoffice/src/main/webapp/WEB-INF/config"));
//   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "portail-backoffice", basedir, "/portail-backoffice/src/main/webapp/WEB-INF/config"));
        configurations.add(ConfigurationFactory.buildFrontofficeConfiguration(label, "renault-fr", basedir, "/renault-fr/backoffice/src/main/webapp/WEB-INF/config"));        
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-at", basedir, "/renault-at/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-be", basedir, "/renault-be/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-br", basedir, "/renault-br/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-central", basedir, "/renault-central/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-ch", basedir, "/renault-ch/backoffice/src/main/webapp/WEB-INF/config"));
//   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-cms-v4", basedir, "/renault-cms-v4/client/webapp/src/main/webapp/WEB-INF"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-cz", basedir, "/renault-cz/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-de", basedir, "/renault-de/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-dk", basedir, "/renault-dk/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-dz", basedir, "/renault-dz/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-es", basedir, "/renault-es/backoffice/src/main/webapp/WEB-INF/config"));
//   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-esc-portal", basedir, "/renault-esc-portal/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-hr", basedir, "/renault-hr/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-hu", basedir, "/renault-hu/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-ie", basedir, "/renault-ie/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-it", basedir, "/renault-it/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-lu", basedir, "/renault-lu/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-ma", basedir, "/renault-ma/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-master", basedir, "/renault-master/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-mx", basedir, "/renault-mx/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-nl", basedir, "/renault-nl/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-pl", basedir, "/renault-pl/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-pt", basedir, "/renault-pt/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-ro", basedir, "/renault-ro/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-ru", basedir, "/renault-ru/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-se", basedir, "/renault-se/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-si", basedir, "/renault-si/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-sk", basedir, "/renault-sk/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-tr", basedir, "/renault-tr/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-ua", basedir, "/renault-ua/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-uk", basedir, "/renault-uk/backoffice/src/main/webapp/WEB-INF/config"));
//   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "renault-webstore", basedir, "/renault-webstore/webstore-backoffice/src/main/webapp/WEB-INF/config"));
//   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "rplug", basedir, "/rplug/backoffice/src/main/webapp/WEB-INF/config"));
//   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "toprenault", basedir, "/toprenault/toprenault-backoffice/src/main/webapp/WEB-INF/config"));
    }

    private static void loadDaciaBackofficeConfigurations(List<Configuration> configurations, String basedir, String label) {
        configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-fr", basedir, "/dacia-fr/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-at", basedir, "/dacia-at/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-be", basedir, "/dacia-be/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-central", basedir, "/dacia-central/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-ch", basedir, "/dacia-ch/backoffice/src/main/webapp/WEB-INF/config"));
//   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-cms", basedir, "/dacia-cms-v4/client/webapp/src/main/webapp/WEB-INF"));
//   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-cms-v4", basedir, "/dacia-cms-v4/client/webapp/src/main/webapp/WEB-INF"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-cz", basedir, "/dacia-cz/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-de", basedir, "/dacia-de/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-dz", basedir, "/dacia-dz/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-es", basedir, "/dacia-es/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-hr", basedir, "/dacia-hr/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-hu", basedir, "/dacia-hu/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-ie", basedir, "/dacia-ie/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-it", basedir, "/dacia-it/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-lu", basedir, "/dacia-lu/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-ma", basedir, "/dacia-ma/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-master", basedir, "/dacia-master/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-nl", basedir, "/dacia-nl/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-pl", basedir, "/dacia-pl/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-pt", basedir, "/dacia-pt/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-ro", basedir, "/dacia-ro/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-si", basedir, "/dacia-si/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-sk", basedir, "/dacia-sk/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-tr", basedir, "/dacia-tr/backoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-uk", basedir, "/dacia-uk/backoffice/src/main/webapp/WEB-INF/config"));
    }

    private static void loadDaciaFrontofficeConfigurations(List<Configuration> configurations, String basedir, String label) {
        configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-fr", basedir, "/dacia-fr/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-at", basedir, "/dacia-at/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-be", basedir, "/dacia-be/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-central", basedir, "/dacia-central/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-ch", basedir, "/dacia-ch/frontoffice/src/main/webapp/WEB-INF/config"));
//   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-cms", basedir, "/dacia-cms-v4/client/webapp/src/main/webapp/WEB-INF"));
//   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-cms-v4", basedir, "/dacia-cms-v4/client/webapp/src/main/webapp/WEB-INF"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-cz", basedir, "/dacia-cz/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-de", basedir, "/dacia-de/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-dz", basedir, "/dacia-dz/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-es", basedir, "/dacia-es/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-hr", basedir, "/dacia-hr/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-hu", basedir, "/dacia-hu/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-ie", basedir, "/dacia-ie/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-it", basedir, "/dacia-it/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-lu", basedir, "/dacia-lu/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-ma", basedir, "/dacia-ma/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-master", basedir, "/dacia-master/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-nl", basedir, "/dacia-nl/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-pl", basedir, "/dacia-pl/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-pt", basedir, "/dacia-pt/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-ro", basedir, "/dacia-ro/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-si", basedir, "/dacia-si/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-sk", basedir, "/dacia-sk/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-tr", basedir, "/dacia-tr/frontoffice/src/main/webapp/WEB-INF/config"));
   		configurations.add(ConfigurationFactory.buildBackofficeConfiguration(label, "dacia-uk", basedir, "/dacia-uk/frontoffice/src/main/webapp/WEB-INF/config"));
    }
}
