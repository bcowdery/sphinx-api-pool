package org.sphinx;

import org.sphinx.config.ConfigurationKey;
import org.sphinx.pool.PooledSphinxDataSource;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * SphinxClientManagerTest
 *
 * @author Brian Cowdery
 * @since 28-05-2015
 */
public class SphinxClientManagerTest {

    @Test
    public void testStoreDataSourceOnConstructor() {
        SphinxClientManager manager1 = new SphinxClientManager("localhost", 9312);
        PooledSphinxDataSource dataSource1 = manager1.getDataSource();

        SphinxClientManager manager2 = new SphinxClientManager("localhost", 9312);
        PooledSphinxDataSource dataSource2 = manager2.getDataSource();

        // verify that the datasource is the same object in all future instances of the manager
        assertSame(dataSource1, dataSource2, "Subsequent instances should return the same data source");
    }

    @Test
    public void testStoreAndLookup() {
        PooledSphinxDataSource dataSource = new PooledSphinxDataSource();
        ConfigurationKey key = SphinxClientManager.registerDataSource(dataSource);

        // verify that we can lookup the registered datasource using the configuration key
        PooledSphinxDataSource actual = SphinxClientManager.getDataSource(key);
        assertSame(actual, dataSource, "Should return the same instance from storage");
    }

    @Test
    public void testStoreAndLookupByHostAndPort() {
        PooledSphinxDataSource dataSource = new PooledSphinxDataSource();
        dataSource.setHost("localhost");
        dataSource.setPort(9312);

        SphinxClientManager.registerDataSource(dataSource);

        // verify that we can lookup the registered datasource by hostname & port
        PooledSphinxDataSource actual = SphinxClientManager.getDataSource("localhost", 9312);
        assertSame(actual, dataSource, "Should return the same instance from storage");
    }

    @Test
    public void testStoreAndLookupByConfigLocation() {
        PooledSphinxDataSource dataSource = new PooledSphinxDataSource();
        dataSource.setHost("localhost");
        dataSource.setPort(9312);

        ConfigurationKey key = new ConfigurationKey("my-configuration.properties");

        SphinxClientManager.registerDataSource(key, dataSource);

        // verify that we can lookup the registered datasource by hostname & port
        PooledSphinxDataSource actual = SphinxClientManager.getDataSource(key);
        assertSame(actual, dataSource, "Should return the same instance from storage");
    }

    @Test
    public void testBuildAndLookupByConfigLocation() {
        PooledSphinxDataSource dataSource = SphinxClientManager.getDataSource("configuration-reader-test.properties");

        // verify that the data source was constructed with the values from the properties file
        assertEquals("test.sphinx.org", dataSource.getHost());
        assertEquals(9312, dataSource.getPort());
        assertEquals(true, dataSource.getTestOnBorrow());
        assertEquals(true, dataSource.getTestOnReturn());
        assertEquals(0, dataSource.getMinIdle());
        assertEquals(10, dataSource.getMaxIdle());
        assertEquals(10, dataSource.getMaxTotal());

        // verify that we can lookup the registered datasource by config location
        PooledSphinxDataSource actual = SphinxClientManager.getDataSource("configuration-reader-test.properties");
        assertSame(actual, dataSource, "Should return the same instance from storage");
    }

    @Test
    public void testConstructorWithConfigLocation() {
        PooledSphinxDataSource dataSource = new SphinxClientManager("configuration-reader-test.properties").getDataSource();
        PooledSphinxDataSource dataSource2 = new SphinxClientManager("configuration-reader-test.properties").getDataSource();

        assertSame(dataSource, dataSource2);
    }

    @Test
    public void testConstructorWithHostAndPort() {
        final String host = "localhost";
        final int port = 9312;

        PooledSphinxDataSource dataSource = new SphinxClientManager(host, port).getDataSource();
        PooledSphinxDataSource dataSource2 = new SphinxClientManager(host, port).getDataSource();

        assertSame(dataSource, dataSource2);
    }
}