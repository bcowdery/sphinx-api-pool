package org.sphinx.pool;

/**
 * Exceptions thrown by the pooled object factory. Can be used to propagate errors up
 * to through pool for application handling.
 *
 * @author Brian Cowdery
 * @since 29-05-2015
 */
public class PooledObjectFactoryException extends RuntimeException {

    public PooledObjectFactoryException(String message) {
        super(message);
    }

    public PooledObjectFactoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
