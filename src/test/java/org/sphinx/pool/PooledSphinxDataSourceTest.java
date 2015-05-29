package org.sphinx.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.sphinx.api.ISphinxClient;
import org.sphinx.api.SphinxClient;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
import static org.mockito.Mockito.*;

/**
 * PooledSphinxDataSourceTest
 *
 * @author Brian Cowdery
 * @since 28-05-2015
 */
public class PooledSphinxDataSourceTest {

    private GenericObjectPool<SphinxClient> pool = mock(GenericObjectPool.class);

    @AfterMethod
    public void resetMocks() {
        reset(pool);
    }


    /**
     * Test that data source configuration options are passed down to the factory.
     *
     * @throws Exception
     */
    @Test
    public void testHostAndPort() throws Exception {
        final String host = "localhost";
        final int port = 9312;

        PooledSphinxDataSource dataSource = new PooledSphinxDataSource();
        dataSource.setHost(host);
        dataSource.setPort(port);

        PooledSphinxClientFactory factory = dataSource.getFactory();
        assertEquals(factory.getHost(), host);
        assertEquals(factory.getPort(), port);
    }

    /**
     * Test getting a client from the pool is managed correctly.
     *
     * @throws Exception
     */
    @Test
    public void testGetClient() throws Exception {
        PooledSphinxDataSource dataSource = new PooledSphinxDataSource();

        // get client from pool
        ISphinxClient client = dataSource.getSphinxClient();
        assertNotNull(client);
        assertEquals(dataSource.getNumActive(), 1);

        // return to pool
        client.Close();
        assertEquals(dataSource.getNumActive(), 0);
    }

    /**
     * Test that a client borrowed from the pool is returned when closed.
     *
     * @throws Exception
     */
    @Test(groups = "mock")
    public void testEnsureBorrowAndClose() throws Exception {
        PooledSphinxDataSource dataSource = new PooledSphinxDataSource(pool);

        SphinxClient delegate = new SphinxClient();
        when(pool.borrowObject()).thenReturn(delegate);

        // verify borrow from pool on get
        ISphinxClient client = dataSource.getSphinxClient();
        verify(pool).borrowObject();

        // verify return to pool on client close
        client.Close();
        verify(pool).returnObject(delegate);
    }

    /**
     * Test that borrowed connections are tested for errors when setTestOnBorrow() is set to true.
     *
     * @throws Exception
     */
    @Test(groups = "mock")
    public void testEnableTestOnBorrow() throws Exception {
        PooledSphinxClientFactory factory = spy(new PooledSphinxClientFactory());
        PooledSphinxDataSource dataSource = new PooledSphinxDataSource(factory);
        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(false);

        ISphinxClient client = dataSource.getSphinxClient();

        // verify that the validate method was called on the factory
        verify(factory, times(1)).validateObject(any(PooledObject.class));
    }

    /**
     * Test that returned connections are tested for errors when setTestOnReturn() is set to true.
     *
     * @throws Exception
     */
    @Test(groups = "mock")
    public void testEnableTestOnReturn() throws Exception {
        PooledSphinxClientFactory factory = spy(new PooledSphinxClientFactory());
        PooledSphinxDataSource dataSource = new PooledSphinxDataSource(factory);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(true);

        ISphinxClient client = dataSource.getSphinxClient();
        client.Close();

        // verify that the validate method was called on the factory
        verify(factory, times(1)).validateObject(any(PooledObject.class));
    }

    /**
     * Test what happens when the pool continuously fails to produce a valid instance of the sphinx client.
     *
     * @throws Exception
     */
    @Test
    public void testClientFailValidation() throws Exception {
        PooledSphinxClientFactory factory = spy(new PooledSphinxClientFactory());
        PooledSphinxDataSource dataSource = new PooledSphinxDataSource(factory);
        dataSource.setTestOnBorrow(true);

        doReturn(false).when(factory).validateObject(any(PooledObject.class));

        // try and get from pool, object should be invalid.
        // this really only happens if after several tries, all attempts are invalid
        try {
            dataSource.getSphinxClient();
        } catch (Exception e) {
            assertEquals(e.getCause().getMessage(), "Unable to validate object");
        }
    }
}