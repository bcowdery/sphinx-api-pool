package org.sphinx.config;

import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.ResourceBundle;

import static org.testng.Assert.*;

/**
 * ConfigurationReaderTest
 *
 * @author Brian Cowdery
 * @since 29-05-2015
 */
public class ConfigurationReaderTest {

    @Test
    public void testHasProperty() throws Exception {
        ConfigurationReader reader = new ConfigurationReader("test.properties");

        final boolean propertyExists = reader.hasProperty("test.property.string");
        assertTrue(propertyExists);
    }

    @Test
    public void testGetProperty() throws Exception {
        ConfigurationReader reader = new ConfigurationReader("test.properties");

        final String value = reader.getProperty("test.property.string");
        assertEquals(value, "string value");
    }

    @Test
    public void testGetPropertyAsInteger() throws Exception {
        ConfigurationReader reader = new ConfigurationReader("test.properties");

        final int number = reader.getPropertyAsInteger("test.property.integer");
        assertEquals(number, 123);
    }

    @Test
    public void testGetPropertyAsBoolean() throws Exception {
        ConfigurationReader reader = new ConfigurationReader("test.properties");

        final boolean boolTrue = reader.getPropertyAsBoolean("test.property.boolean.true");
        assertTrue(boolTrue);

        final boolean boolYes = reader.getPropertyAsBoolean("test.property.boolean.yes");
        assertTrue(boolYes);

        final boolean boolOn = reader.getPropertyAsBoolean("test.property.boolean.on");
        assertTrue(boolOn);

        final boolean boolInvalid = reader.getPropertyAsBoolean("test.property.boolean.invalid");
        assertFalse(boolInvalid);

        final boolean boolFalse = reader.getPropertyAsBoolean("test.property.boolean.false");
        assertFalse(boolFalse);
    }

    @Test
    public void testDefaults() throws Exception {
        ConfigurationReader reader = new ConfigurationReader();

        final String host = reader.getProperty("sphinx.dataSource.host");
        final String port = reader.getProperty("sphinx.dataSource.port");
        final String testOnBorrow = reader.getProperty("sphinx.dataSource.testOnBorrow");
        final String testOnReturn = reader.getProperty("sphinx.dataSource.testOnReturn");
        final String minIdle = reader.getProperty("sphinx.dataSource.minIdle");
        final String maxIdle = reader.getProperty("sphinx.dataSource.maxIdle");
        final String maxTotal = reader.getProperty("sphinx.dataSource.maxTotal");

        // should match expected defaults
        // @see ConfigurationReader#DEFAULTS
        assertEquals(host, "localhost");
        assertEquals(port, "9312");
        assertEquals(testOnBorrow, "false");
        assertEquals(testOnReturn, "false");
        assertEquals(minIdle, "0");
        assertEquals(maxIdle, "8");
        assertEquals(maxTotal, "8");
    }

    @Test
    public void testReadFromTestPropertiesFile() throws Exception {
        ConfigurationReader reader = new ConfigurationReader("configuration-reader-test.properties");

        final String host = reader.getProperty("sphinx.dataSource.host");
        final String port = reader.getProperty("sphinx.dataSource.port");

        // validate that we successfully read the values from the test properties file on the classpath
        assertEquals(host, "test.sphinx.org");
        assertEquals(port, "9312");
    }

    @Test
    public void testGetConfigObject() throws Exception {
        ConfigurationReader reader = new ConfigurationReader("configuration-reader-test.properties");
        BasicPooledDataSourceConfig config = reader.getConfigObject();

        // validate that the config object values match configuration-reader-test.properties
        assertEquals(config.getHost(), "test.sphinx.org");
        assertEquals(config.getPort(), 9312);
        assertEquals(config.getTestOnBorrow(), true);
        assertEquals(config.getTestOnReturn(), true);
        assertEquals(config.getMinIdle(), 0);
        assertEquals(config.getMaxIdle(), 10);
        assertEquals(config.getMaxTotal(), 10);
    }
}