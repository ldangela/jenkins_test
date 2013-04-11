package com.renault.tools.synchronization.config.webapp.om;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.renault.tools.synchronization.config.webapp.ConfigurationSynchronizationTest;

public class ConfigurationTest {
    
    private final static String FILE_SEPARATOR = "/";

    private static String basedir;

    private static Configuration configuration;
    
    @BeforeClass
    public static void init() {
        String path = ConfigurationSynchronizationTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        basedir = StringUtils.substringBefore(path, FILE_SEPARATOR + "target") + FILE_SEPARATOR + "src" + FILE_SEPARATOR + "test" + FILE_SEPARATOR + "resources";
        configuration = ConfigurationFactory.buildConfiguration("label", "configName", basedir, "/config-I");
    }

    @Test
    public void should_keep_backslash() {
        configuration.putCommonPropertiesFile("file.properties", new File(basedir + "/config-I/file.properties"));
        PropertyContainer propContainer = configuration.getPropertyContainer("file.properties", "prop.A");
        Assert.assertEquals("path\\path", propContainer.getValue());
    }

    @Test
    public void should_no_support_multilines_value() {
        configuration.putCommonPropertiesFile("file.properties", new File(basedir + "/config-I/file.properties"));
        PropertyContainer propContainer = configuration.getPropertyContainer("file.properties", "prop.B");
        Assert.assertEquals("value1,\\", propContainer.getValue());
    }
}
