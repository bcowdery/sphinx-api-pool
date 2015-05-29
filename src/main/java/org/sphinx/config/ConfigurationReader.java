package org.sphinx.config;

import org.sphinx.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * ConfigurationReader
 *
 * @author Brian Cowdery
 * @since 29-05-2015
 */
public class ConfigurationReader {

    private static final Pattern BOOLEAN_PATTERN = Pattern.compile("^(yes|on|true)$", Pattern.CASE_INSENSITIVE);

    private static final Properties DEFAULTS;
    static {
        DEFAULTS = new Properties();
        DEFAULTS.setProperty("sphinx.dataSource.host", "localhost");
        DEFAULTS.setProperty("sphinx.dataSource.port", "9312");
        DEFAULTS.setProperty("sphinx.dataSource.testOnBorrow", "true");
        DEFAULTS.setProperty("sphinx.dataSource.testOnReturn", "true");
        DEFAULTS.setProperty("sphinx.dataSource.minIdle", "0");
        DEFAULTS.setProperty("sphinx.dataSource.maxIdle", "8");
        DEFAULTS.setProperty("sphinx.dataSource.maxTotal", "8");
    }


    private String path;
    private Properties properties;


    public ConfigurationReader(String path) {
        this.path = path;
        load();
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Returns the property file configuration as a BasicPooledDataSourceConfig object.
     *
     * @return config object
     */
    public BasicPooledDataSourceConfig getConfigObject() {
        final String host = getProperty("sphinx.dataSource.host");
        final int port = getPropertyAsInteger("sphinx.dataSource.port");
        final boolean testOnBorrow = getPropertyAsBoolean("sphinx.dataSource.testOnBorrow");
        final boolean testOnReturn = getPropertyAsBoolean("sphinx.dataSource.testOnReturn");
        final int minIdle = getPropertyAsInteger("sphinx.dataSource.minIdle");
        final int maxIdle = getPropertyAsInteger("sphinx.dataSource.maxIdle");
        final int maxTotal = getPropertyAsInteger("sphinx.dataSource.maxTotal");

        return new BasicPooledDataSourceConfig(host, port, testOnBorrow, testOnReturn, minIdle, maxIdle, maxTotal);
    }

    /**
     * Returns true if the property exists, false if not.
     *
     * @param key property key
     * @return true if property exists, false if not.
     */
    public boolean hasProperty(String key) {
        return properties.get(key) != null;
    }

    /**
     * Returns the property for the given key. If the value does not exist in the
     * external properties file, a default value will be returned, or null if no default
     * is defined.
     *
     * @param key property key
     * @return property value, null if property and default not found.
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        return StringUtils.isNullOrEmpty(value) ? null : value;
    }

    /**
     * Returns the property for the given key as an Integer. If the value does not
     * exist in the external properties file, a default value will be returned, or
     * null if no default is defined.
     *
     * This method will return null if the property value cannot be parsed as an Integer.
     *
     * @param key property key
     * @return property value, null if property and default not found.
     */
    public Integer getPropertyAsInteger(String key) {
        String value = getProperty(key);
        try {
            return (value != null ? Integer.valueOf(value) : null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Returns the property for the given key as a Date. If the value does not
     * exist in the external properties file, a default value will be returned, or
     * null if no default is defined.
     *
     * Property values matching "on", "yes", or "true" (case insensitive) are parsed
     * as Boolean.TRUE, all other values are parsed as Boolean.FALSE.
     *
     * @param key property key
     * @return property value, null if property and default not found.
     */
    public Boolean getPropertyAsBoolean(String key) {
        String value = getProperty(key);
        return  value != null ? BOOLEAN_PATTERN.matcher(value.trim()).find() : null;
    }

    /**
     * Load configuration properties from the external properties file.
     *
     * @throws IllegalArgumentException if the {@link #path} cannot be read.
     */
    public void load() {
        properties = new Properties(DEFAULTS);

        try {
            properties.load(new FileInputStream(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read properties file from path " + path, e);
        }
    }
}
