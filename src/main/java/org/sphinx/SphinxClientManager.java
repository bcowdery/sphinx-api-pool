package org.sphinx;

import org.sphinx.config.BasicPooledDataSourceConfig;
import org.sphinx.config.ConfigurationKey;
import org.sphinx.config.ConfigurationReader;
import org.sphinx.pool.PooledSphinxDataSource;
import org.sphinx.api.ISphinxClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages pooled {@link PooledSphinxDataSource} configurations.
 *
 * A better solution is to manage instance of SphinxClientDataSource using a IoC container (such as Spring)
 * which can do a better job of configuring and managing the life-cycle of beans.
 *
 * @author Brian Cowdery
 * @since 28-05-2015
 */
public class SphinxClientManager {

    // storage for configured pools
    private static final Map<ConfigurationKey, PooledSphinxDataSource> dataSourceStore = new HashMap<ConfigurationKey, PooledSphinxDataSource>();

    private PooledSphinxDataSource dataSource;


    public SphinxClientManager() {
        this.dataSource = getDataSource("sphinx-api-pool.properties");
    }

    public SphinxClientManager(String configLocation) {
        this.dataSource = getDataSource(configLocation);
    }

    public SphinxClientManager(String host, int port) {
        this.dataSource = getDataSource(host, port);
    }

    public SphinxClientManager(PooledSphinxDataSource dataSource) {
        registerDataSource(dataSource);
        this.dataSource = dataSource;
    }


    /**
     * Returns the data source for this instance of the client manager.
     * @return data source
     */
    public PooledSphinxDataSource getDataSource() {
        return dataSource;
    }

    /**
     * Returns a sphinx client from the pooled data source.
     * @return sphinx client
     */
    public ISphinxClient getSphinxClient() {
        return dataSource.getSphinxClient();
    }

    /**
     * Lookup a data source by configuration. If the data source does not exist a new
     * instance will be created with the given host and port.
     *
     * @param host sphinx host
     * @param port sphinx port
     * @return data source
     */
    public static PooledSphinxDataSource getDataSource(String host, int port) {
        ConfigurationKey key = new ConfigurationKey(host, port);
        PooledSphinxDataSource dataSource = dataSourceStore.get(key);

        if (dataSource == null) {
            dataSource = new PooledSphinxDataSource(host, port);
            dataSourceStore.put(key, dataSource);
        }

        return dataSource;
    }

    /**
     * Lookup a data source by configuration. If the data source does not exist the
     * configuration will be read from the given location and a new instance created.
     *
     * @param configLocation location of the data source config property file
     * @return data source
     */
    public static PooledSphinxDataSource getDataSource(String configLocation) {
        ConfigurationKey key = new ConfigurationKey(configLocation);
        PooledSphinxDataSource dataSource = dataSourceStore.get(key);

        if (dataSource == null) {
            ConfigurationReader reader = new ConfigurationReader(configLocation);
            BasicPooledDataSourceConfig config = reader.getConfigObject();

            dataSource = new PooledSphinxDataSource(config);
            dataSourceStore.put(key, dataSource);
        }

        return dataSource;
    }

    /**
     * Lookup a data source by configuration. Returns null if no data source can
     * be found for the given key.
     *
     * @param key configuration key for lookup
     * @return data source
     */
    public static PooledSphinxDataSource getDataSource(ConfigurationKey key) {
        return dataSourceStore.get(key);
    }

    /**
     * Register a data source with the client manager.
     *
     * @param dataSource data source
     * @return configuration key for future lookup
     */
    public static ConfigurationKey registerDataSource(PooledSphinxDataSource dataSource) {
        ConfigurationKey key = new ConfigurationKey(dataSource.getHost(), dataSource.getPort());
        dataSourceStore.put(key, dataSource);
        return key;
    }
}
