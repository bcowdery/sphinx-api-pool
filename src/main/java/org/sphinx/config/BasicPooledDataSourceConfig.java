package org.sphinx.config;

/**
 * BasicPooledDataSourceConfig
 *
 * @author Brian Cowdery
 * @since 29-05-2015
 */
public class BasicPooledDataSourceConfig {

    private final ConfigurationKey configurationKey;
    private final String host;
    private final int port;
    private final boolean testOnBorrow;
    private final boolean testOnReturn;
    private final int minIdle;
    private final int maxIdle;
    private final int maxTotal;


    public BasicPooledDataSourceConfig(String host, int port, boolean testOnBorrow, boolean testOnReturn, int minIdle, int maxIdle, int maxTotal) {
        this.configurationKey = new ConfigurationKey(host, port);
        this.host = host;
        this.port = port;
        this.testOnBorrow = testOnBorrow;
        this.testOnReturn = testOnReturn;
        this.minIdle = minIdle;
        this.maxIdle = maxIdle;
        this.maxTotal = maxTotal;
    }


    public ConfigurationKey getConfigurationKey() {
        return configurationKey;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    /**
     * Returns a config object with an updated sphinx server host and port.
     *
     * @param host sphinx host
     * @param port sphinx port
     * @return new config object with set host and port
     */
    public BasicPooledDataSourceConfig withServer(String host, int port) {
        return new BasicPooledDataSourceConfig(host, port, testOnBorrow, testOnReturn, minIdle, maxIdle, maxTotal);
    }

    /**
     * Returns a config object with updated data source options.
     *
     * @param testOnBorrow test clients on borrow from pool
     * @param testOnReturn test clients on return to pool
     * @param minIdle minimum number of idle clients to maintain in the pool
     * @param maxIdle max number of idle clients to maintain in the pool
     * @param maxTotal total number of clients (idle and active) to allow
     * @return new config object with set data source options
     */
    public BasicPooledDataSourceConfig withOptions(boolean testOnBorrow, boolean testOnReturn, int minIdle, int maxIdle, int maxTotal) {
        return new BasicPooledDataSourceConfig(host, port, testOnBorrow, testOnReturn, minIdle, maxIdle, maxTotal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicPooledDataSourceConfig that = (BasicPooledDataSourceConfig) o;

        if (port != that.port) return false;
        if (testOnBorrow != that.testOnBorrow) return false;
        if (testOnReturn != that.testOnReturn) return false;
        if (minIdle != that.minIdle) return false;
        if (maxIdle != that.maxIdle) return false;
        if (maxTotal != that.maxTotal) return false;
        return !(host != null ? !host.equals(that.host) : that.host != null);
    }

    @Override
    public int hashCode() {
        int result = host != null ? host.hashCode() : 0;
        result = 31 * result + port;
        result = 31 * result + (testOnBorrow ? 1 : 0);
        result = 31 * result + (testOnReturn ? 1 : 0);
        result = 31 * result + minIdle;
        result = 31 * result + maxIdle;
        result = 31 * result + maxTotal;
        return result;
    }
}
