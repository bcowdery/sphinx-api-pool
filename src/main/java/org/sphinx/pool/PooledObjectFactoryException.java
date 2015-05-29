package org.sphinx.pool;

/**
 * PooledObjectFactoryException
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
