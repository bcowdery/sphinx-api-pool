package org.sphinx.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Reads the data source configuration from an external properties file.
 *
 * <code>
 *      BasicPooledDataSourceConfig config = new ConfigurationReader("pool.properties").getConfigObject();
 * </code>
 *
 * @author Brian Cowdery
 * @since 29-05-2015
 */
public class ConfigurationReader {

    private final String path;
    private final Configuration config;


    public ConfigurationReader(String path) {
        this.path = path;
        this.config = load(path);
    }


    public String getPath() {
        return path;
    }

    private Configuration load(String path) {
        try {
            return new PropertiesConfiguration(path);
        } catch (ConfigurationException e) {
            throw new RuntimeException("Could not read properties file: " + path, e);
        }
    }

    /**
     * Returns the property file config as a BasicPooledDataSourceConfig object.
     *
     * @return config object
     */
    public BasicPooledDataSourceConfig getConfigObject() {
        final String host = config.getString("sphinx.dataSource.host");
        final int port = config.getInt("sphinx.dataSource.port");
        final boolean testOnBorrow = config.getBoolean("sphinx.dataSource.testOnBorrow");
        final boolean testOnReturn = config.getBoolean("sphinx.dataSource.testOnReturn");
        final int minIdle = config.getInt("sphinx.dataSource.minIdle");
        final int maxIdle = config.getInt("sphinx.dataSource.maxIdle");
        final int maxTotal = config.getInt("sphinx.dataSource.maxTotal");

        return new BasicPooledDataSourceConfig(host, port, testOnBorrow, testOnReturn, minIdle, maxIdle, maxTotal);
    }
}
