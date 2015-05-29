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
}