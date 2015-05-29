package org.sphinx.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.sphinx.api.ISphinxClient;
import org.sphinx.api.SphinxClient;

/**
 * Pooled data source for sphinx clients.
 *
 * This data source maintains an thread-safe {@link org.apache.commons.pool2.ObjectPool} of
 * sphinx clients. Socket connections are formally established when a sphinx client is borrowed from
 * the pool and closed upon return. Care should be taken to call {@link ISphinxClient#Close()} when
 * the connection is no longer in use, to return the client to the pool.
 *
 * Best practice is to attempt to establish an pool where the number of active sphinx clients (concurrent
 * connections) does not exceed the <code>max_children</code> Sphinx configuration setting. This ensures
 * that the pool can serve the demands of the application without exceeding the maximum possible number
 * of connections allowed by Sphinx. Setting the pool limits too low will cause contention in the
 * application, blocking while the pool waits for connections to return to the pool.
 *
 * @see PooledSphinxClientFactory
 *
 * @author Brian Cowdery
 * @since 28-05-2015
 */
public class PooledSphinxDataSource {

    private GenericObjectPool<SphinxClient> pool;


    public PooledSphinxDataSource() {
        this(new PooledSphinxClientFactory());
    }

    public PooledSphinxDataSource(String host, int port) {
        this(new PooledSphinxClientFactory(host, port));
    }

    public PooledSphinxDataSource(BasePooledObjectFactory<SphinxClient> factory) {
        this(new GenericObjectPool<SphinxClient>(factory));
    }

    public PooledSphinxDataSource(GenericObjectPool<SphinxClient> pool) {
        this.pool = pool;
        this.setPoolDefaults();
    }


    private void setPoolDefaults() {
        pool.setTestOnBorrow(true);
        pool.setTestOnReturn(true);
        pool.setTestOnCreate(false); // SphinxClient constructor has no logic, no point in testing on create
    }

    public boolean getTestOnBorrow() {
        return pool.getTestOnBorrow();
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        pool.setTestOnBorrow(testOnBorrow);
    }

    public boolean getTestOnReturn() {
        return pool.getTestOnReturn();
    }

    public void setTestOnReturn(boolean testOnReturn) {
        pool.setTestOnReturn(testOnReturn);
    }

    public int getMaxIdle() {
        return pool.getMaxIdle();
    }

    public void setMaxIdle(int maxIdle) {
        pool.setMaxIdle(maxIdle);
    }

    public int getMinIdle() {
        return pool.getMinIdle();
    }

    public void setMinIdle(int minIdle) {
        pool.setMinIdle(minIdle);
    }

    public int getMaxTotal() {
        return pool.getMaxTotal();
    }

    public void setMaxTotal(int maxTotal) {
        pool.setMaxTotal(maxTotal);
    }

    public String getHost() {
        return getFactory().getHost();
    }

    public void setHost(String host) {
        getFactory().setHost(host);
    }

    public int getPort() {
        return getFactory().getPort();
    }

    public void setPort(int port) {
        getFactory().setPort(port);
    }

    /**
     * Returns the object factory backing the pool.
     *
     * @return pooled object factory
     */
    public PooledSphinxClientFactory getFactory() {
        return (PooledSphinxClientFactory) pool.getFactory();
    }

    /**
     * Returns the number of clients currently borrowed from the pool. This count is
     * roughly the number of active connections/sockets open to the sphinx server.
     *
     * @return number of active clients checked out of the pool.
     */
    public int getNumActive() {
        return pool.getNumActive();
    }

    /**
     * Returns the number of idle clients in the pool ready for use.
     *
     * @return number of idle clients
     */
    public int getNumIdle() {
        return pool.getNumIdle();
    }

    /**
     * Fetches a managed instance of {@link ISphinxClient} from the pool. The object must
     * be closed by the caller to return it to the pool when finished.
     *
     * @see SphinxClientProxy#Close()
     *
     * @return sphinx client
     */
    public ISphinxClient getSphinxClient() {
        try {
            return new SphinxClientProxy(pool.borrowObject(), pool);
        } catch (Exception e) {
            throw new RuntimeException("Unable to retrieve sphinx client from pool", e);
        }
    }
}
