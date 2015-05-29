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