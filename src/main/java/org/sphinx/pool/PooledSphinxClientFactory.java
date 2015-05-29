package org.sphinx.pool;

import org.sphinx.util.StringUtils;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.sphinx.api.SphinxClient;

/**
 * Object factory for creating pooled {@link SphinxClient} instances.
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


    /**
     * Returns the sphinx host URL.
     * @return sphinx host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the sphinx host URL.
     * @param host sphinx host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Returns the sphinx port.
     * @return sphinx port
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets the sphinx port.
     * @param port sphinx port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Creates a new instance of {@link SphinxClient} with the configured host and port.
     *
     * @return new instance of the sphinx client
     * @throws Exception
     */
    @Override
    public SphinxClient create() throws Exception {
        return StringUtils.isNullOrEmpty(host) ? new SphinxClient() : new SphinxClient(host, port);
    }

    /**
     * Wraps a given instance of the {@link SphinxClient} as a pooled object.
     *
     * @param sphinxClient object to wrap
     * @return pooled object
     */
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
        if (!p.getObject().Close()) {
            p.invalidate(); // error occurred while closing socket, invalidate the object
        }
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
     * Tests the client instance for connection errors and ensures that it is safe to be returned by the pool.
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
