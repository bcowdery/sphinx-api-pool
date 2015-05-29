package org.sphinx.config;

import org.sphinx.SphinxClientManager;
import org.sphinx.pool.PooledSphinxDataSource;

import java.io.Serializable;

/**
 * Serializable key for registering {@link PooledSphinxDataSource} instance with the
 * {@link SphinxClientManager}.
 *
 * @author Brian Cowdery
 * @since 28-05-2015
 */
public class ConfigurationKey implements Serializable {

    private String host;
    private int port;

    public ConfigurationKey(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfigurationKey that = (ConfigurationKey) o;

        if (port != that.port) return false;
        return !(host != null ? !host.equals(that.host) : that.host != null);
    }

    @Override
    public int hashCode() {
        int result = host != null ? host.hashCode() : 0;
        result = 31 * result + port;
        return result;
    }
}
