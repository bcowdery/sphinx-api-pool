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

    private final String configLocation;
    private final String host;
    private final Integer port;

    public ConfigurationKey(String configLocation) {
        this.configLocation = configLocation;
        this.host = null;
        this.port = null;
    }

    public ConfigurationKey(String host, int port) {
        this.configLocation = null;
        this.host = host;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfigurationKey that = (ConfigurationKey) o;

        if (configLocation != null ? !configLocation.equals(that.configLocation) : that.configLocation != null) return false;
        if (host != null ? !host.equals(that.host) : that.host != null) return false;
        return !(port != null ? !port.equals(that.port) : that.port != null);
    }

    @Override
    public int hashCode() {
        int result = configLocation != null ? configLocation.hashCode() : 0;
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        return result;
    }
}
