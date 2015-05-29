package org.sphinx.pool;

import org.sphinx.util.StringUtils;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.sphinx.api.SphinxClient;

import java.io.InvalidObjectException;

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
     * @throws InvalidObjectException if the socket connection could not be closed
     */
    @Override
    public void passivateObject(PooledObject<SphinxClient> p) throws InvalidObjectException {

        // The current implementation of SphinxClient will only return false on Close() when the
        // socket is already closed. This isn't a major issue except that it means the socket was
        // closed by some other mechanism than this pool factory. Don't trust rogue clients that have
        // been manipulated by outside forces!!

        if (!p.getObject().Close()) {
            throw new InvalidObjectException("Could not close client, object is invalid and cannot be passivated.");
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

        boolean valid = !sphinxClient.IsConnectError();

        if (!valid && !StringUtils.isNullOrEmpty(sphinxClient.GetLastError())) {
            throw new PooledObjectFactoryException(sphinxClient.GetLastError());
        }

        return valid;
    }
}
