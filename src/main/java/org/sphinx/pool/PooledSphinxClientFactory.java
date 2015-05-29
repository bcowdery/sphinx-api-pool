package org.sphinx.pool;

import org.sphinx.util.StringUtils;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.sphinx.api.SphinxClient;

/**
 * Object factory for creating pooled {@linked SphinxClient} instances.
 *
 * @author Brian Cowdery
 * @since 28-05-2015
 */
public class PooledSphinxClientFactory extends BasePooledObjectFactory<SphinxClient> {

    private String host;
    private int port;


    public PooledSphinxClientFactory() {
    }

    public PooledSphinxClientFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public SphinxClient create() throws Exception {
        return StringUtils.isNullOrEmpty(host) ? new SphinxClient() : new SphinxClient(host, port);
    }

    @Override
    public PooledObject<SphinxClient> wrap(SphinxClient sphinxClient) {
        return new DefaultPooledObject<SphinxClient>(sphinxClient);
    }

    /**
     * Closes an open sphinx socket connection so that the pooled client
     * can be safely returned to the pool.
     *
     * @param p pooled object
     * @throws Exception
     */
    @Override
    public void passivateObject(PooledObject<SphinxClient> p) throws Exception {
        p.getObject().Close();
    }

    /**
     * Restores a pooled client and establishes an open socket connection
     * so that the client can be used.
     *
     * @param p pooled object
     * @throws Exception
     */
    @Override
    public void activateObject(PooledObject<SphinxClient> p) throws Exception {
        p.getObject().Open();
    }

    /**
     * Destroys a pooled client that is no longer needed by the pool, and ensures that any
     * open socket connections are safely closed.
     *
     * @param p pooled object
     * @throws Exception
     */
    @Override
    public void destroyObject(PooledObject<SphinxClient> p) throws Exception {
        p.getObject().Close();
    }

    /**
     * Validate that the object is safe to return to the pool and has not incurred any connection errors
     * when activating. This method will also be invoked on return if the pool setting
     * {@link PooledSphinxDataSource#getTestOnReturn()} is set to true, forcing eviction from the pool
     * if an issue was encountered during use.
     *
     * @param p pooled object
     * @return true if valid, false if a connection error would prevent this object from being used again.
     */
    @Override
    public boolean validateObject(PooledObject<SphinxClient> p) {
        SphinxClient sphinxClient = p.getObject();
        return sphinxClient.IsConnectError() || !StringUtils.isNullOrEmpty(sphinxClient.GetLastError());
    }
}
