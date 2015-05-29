package org.sphinx.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.sphinx.api.ISphinxClient;
import org.sphinx.api.SphinxClient;
import org.sphinx.config.BasicPooledDataSourceConfig;

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

    public PooledSphinxDataSource(BasicPooledDataSourceConfig config) {
        this(new PooledSphinxClientFactory(config.getHost(), config.getPort()), config);
    }

    public PooledSphinxDataSource(BasePooledObjectFactory<SphinxClient> factory) {
        this(new GenericObjectPool<SphinxClient>(factory));
    }

    public PooledSphinxDataSource(BasePooledObjectFactory<SphinxClient> factory, BasicPooledDataSourceConfig config) {
        this(new GenericObjectPool<SphinxClient>(factory), config);
    }

    public PooledSphinxDataSource(GenericObjectPool<SphinxClient> pool) {
        this.pool = pool;
    }

    public PooledSphinxDataSource(GenericObjectPool<SphinxClient> pool, BasicPooledDataSourceConfig config) {
        this.pool = pool;

        if (config != null) {
            setTestOnBorrow(config.getTestOnBorrow());
            setTestOnReturn(config.getTestOnReturn());
            setMinIdle(config.getMinIdle());
            setMaxIdle(config.getMaxIdle());
            setMaxTotal(config.getMaxTotal());
        }
    }


    /**
     * Returns whether objects borrowed from the pool will be validated before being returned from the getSphinxClient()
     * method. Validation is performed by the validateObject() method of the factory associated with the pool. If the
     * object fails to validate, it will be removed from the pool and destroyed, and a new attempt will be made to
     * borrow an object from the pool.
     *
     * The default value is "true".
     *
     * @return true if objects are validated before being returned from the getSphinxClient() method
     */
    public boolean getTestOnBorrow() {
        return pool.getTestOnBorrow();
    }

    /**
     * Sets whether objects borrowed from the pool will be validated before being returned from the getSphinxClient()
     * method. Validation is performed by the validateObject() method of the factory associated with the pool. If the
     * object fails to validate, it will be removed from the pool and destroyed, and a new attempt will be made to
     * borrow an object from the pool.

     * @param testOnBorrow true if objects should be validated before being returned from the borrowObject() method
     */
    public void setTestOnBorrow(boolean testOnBorrow) {
        pool.setTestOnBorrow(testOnBorrow);
    }

    /**
     * Returns whether objects borrowed from the pool will be validated when they are returned to the pool. Validation
     * is performed by the validateObject() method of the factory associated with the pool. Returning objects that fail
     * validation are destroyed rather then being returned the pool.
     *
     * The default value is "true".
     *
     * @return true if objects are validated before being returned from the getSphinxClient() method
     */
    public boolean getTestOnReturn() {
        return pool.getTestOnReturn();
    }

    /**
     * Sets whether objects borrowed from the pool will be validated when they are returned to the pool. Validation is
     * performed by the validateObject() method of the factory associated with the pool. Returning objects that fail
     * validation are destroyed rather then being returned the pool.

     * @param testOnReturn true if objects are validated on return to the pool via the returnObject() method
     */
    public void setTestOnReturn(boolean testOnReturn) {
        pool.setTestOnReturn(testOnReturn);
    }

    /**
     * Returns the maximum amount of time (in milliseconds) the borrowObject() method should block before throwing an
     * exception when the pool is exhausted. When less than 0, the getSphinxClient() method may block indefinitely.
     *
     * @return the maximum number of milliseconds getSphinxClient() will block.
     */
    public long getMaxWaitMillis() {
        return pool.getMaxWaitMillis();
    }

    /**
     * Sets the maximum amount of time (in milliseconds) the getSphinxClient() method should block before throwing an
     * exception when the pool is exhausted. When less than 0, the getSphinxClient() method may block indefinitely.
     *
     * @param maxWaitMillis the maximum number of milliseconds borrowObject() will block or negative for indefinitely.
     */
    public void setMaxWaitMillis(long maxWaitMillis) {
        pool.setMaxWaitMillis(maxWaitMillis);
    }

    /**
     * Returns the cap on the number of "idle" instances in the pool.
     *
     * @return number of idle instances
     */
    public int getMaxIdle() {
        return pool.getMaxIdle();
    }

    /**
     * Set  the number of "idle" instances in the pool. If maxIdle is set too low on heavily loaded systems it is
     * possible you will see objects being destroyed and almost immediately new objects being created. This is a
     * result of the active threads momentarily returning objects faster than they are requesting them them, causing
     * the number of idle objects to rise above maxIdle. The best value for maxIdle for heavily loaded system will
     * vary but the default is a good starting point.
     *
     * @param maxIdle the cap on the number of "idle" instances in the pool. Use -1 for unlimited.
     */
    public void setMaxIdle(int maxIdle) {
        pool.setMaxIdle(maxIdle);
    }

    /**
     * Returns the target for the minimum number of idle objects to maintain in the pool.
     *
     * @return the minimum number of objects.
     */
    public int getMinIdle() {
        return pool.getMinIdle();
    }

    /**
     * Sets the target for the minimum number of idle objects to maintain in the pool. This setting only has an effect
     * if it is positive and BaseGenericObjectPool.getTimeBetweenEvictionRunsMillis() is greater than zero. If this
     * is the case, an attempt is made to ensure that the pool has the required minimum number of instances during
     * idle object eviction runs.
     *
     * If the configured value of minIdle is greater than the configured value for maxIdle then the value of maxIdle will be used instead.
     *
     * @param minIdle The minimum number of objects.
     */
    public void setMinIdle(int minIdle) {
        pool.setMinIdle(minIdle);
    }

    /**
     * Returns the maximum number of objects that can be allocated by the pool (checked out to clients, or idle
     * awaiting checkout) at a given time. When negative, there is no limit to the number of objects that can be
     * managed by the pool at one time.
     *
     * @return the cap on the total number of object instances managed by the pool.
     */
    public int getMaxTotal() {
        return pool.getMaxTotal();
    }

    /**
     * Sets the cap on the number of objects that can be allocated by the pool (checked out to clients, or idle
     * awaiting checkout) at a given time. Use a negative value for no limit.
     *
     * @param maxTotal The cap on the total number of object instances managed by the pool. Use -1 for unlimited.
     */
    public void setMaxTotal(int maxTotal) {
        pool.setMaxTotal(maxTotal);
    }

    /**
     * Returns the configured connection URL for creating new instances.
     *
     * @see PooledSphinxClientFactory#getHost()
     * @return host url for new instances.
     */
    public String getHost() {
        return getFactory().getHost();
    }

    /**
     * Sets the conception host to be used for creating new instances. Note that changing the host URL
     * will invalidate the existing pool, new clients will be created on demand using updated host value.
     *
     * @see PooledSphinxClientFactory#setHost(String)
     * @param host host URL
     */
    public void setHost(String host) {
        getFactory().setHost(host);
        pool.clear();
    }

    /**
     * Returns the configured connection port for creating new instances.
     *
     * @see PooledSphinxClientFactory#getPort()
     * @return port for new instances.
     */
    public int getPort() {
        return getFactory().getPort();
    }

    /**
     * Sets the conception port to be used for creating new instances. Note that changing the connection
     * port will invalidate the existing pool, new clients will be created on demand using updated port value.
     *
     * @see PooledSphinxClientFactory#setPort(int)
     * @param port port number
     */
    public void setPort(int port) {
        getFactory().setPort(port);
        pool.clear();
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
            throw new RuntimeException("Unable to retrieve sphinx client from the pool", e);
        }
    }
}
