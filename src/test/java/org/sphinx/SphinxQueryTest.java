package org.sphinx;

import org.sphinx.api.ISphinxClient;
import org.sphinx.api.SphinxResult;
import org.sphinx.pool.PooledSphinxDataSource;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * SphinxQueryTest
 *
 * @author Brian Cowdery
 * @since 29-05-2015
 */
@Test(enabled = false, description = "Live integration test, not part of automated test suite.")
public class SphinxQueryTest {

    @Test
    public void testSphinxQuery() throws Exception {
        PooledSphinxDataSource dataSource = new PooledSphinxDataSource();
        dataSource.setHost("190.10.14.168");
        dataSource.setPort(9312);
        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(true);
        dataSource.setMinIdle(0);
        dataSource.setMaxIdle(8);
        dataSource.setMaxTotal(8);

        ISphinxClient client = dataSource.getSphinxClient();
        assertNotNull(client);

        SphinxResult result = client.Query("SELECT name, postdate FROM releases WHERE MATCH('Futurama')");
        assertNotNull(result);
    }
}
