package org.sphinx.pool;

import org.apache.commons.pool2.ObjectPool;
import org.sphinx.api.ISphinxClient;
import org.sphinx.api.SphinxClient;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.SocketException;

import static org.testng.Assert.*;
import static org.mockito.Mockito.*;

/**
 * SphinxClientProxyTest
 *
 * @author Brian Cowdery
 * @since 28-05-2015
 */
@Test(groups = "mock")
public class SphinxClientProxyTest {

    private SphinxClient client = mock(SphinxClient.class);
    private ObjectPool<SphinxClient> pool = mock(ObjectPool.class);

    // class under test
    private SphinxClientProxy proxy;

    @BeforeMethod
    public void setup() {
        proxy = new SphinxClientProxy(client, pool);
    }

    @AfterMethod
    public void resetMocks() {
        reset(client, pool);
    }

    /**
     * Tests that getDelegate() returns the underlying SphinxClient
     * @throws Exception
     */
    @Test
    public void testGetDelegate() throws Exception {
        ISphinxClient delegate = proxy.getDelegate();
        assertSame(delegate, client);
    }

    /**
     * Test that getDelegate() will throw an exception after the underlying SphinxClient
     * has been closed and returned to the pool.
     *
     * @throws Exception
     */
    @Test(expectedExceptions = IllegalStateException.class)
    public void testGetDelegateAfterClose() throws Exception {
        proxy.Close();
        proxy.getDelegate(); // throw IllegalStateException
    }

    /**
     * Tests that Close() returns the underlying SphinxClient back to the pool
     * and invalidates the proxy.
     *
     * @throws Exception
     */
    @Test
    public void testClose() throws Exception {
        proxy.Close();
        verify(pool).returnObject(client);
    }

    /**
     * Tests that Close() will throw a runtime exception if there is an issue returning
     * the underlying SphinxClient back to the pool.
     *
     * @throws Exception
     */
    @Test(expectedExceptions = RuntimeException.class)
    public void testCloseException() throws Exception {
        doThrow(new SocketException("Could not close socket")).when(pool).returnObject(client);
        proxy.Close(); // rethrow as RuntimeException
    }
}