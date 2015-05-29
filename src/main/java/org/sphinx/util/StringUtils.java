package org.sphinx.util;

/**
 * Simple string utilities.
 *
 * @author Brian Cowdery
 * @since 28-05-2015
 */
public class StringUtils {

    /**
     * Returns true if the string is null or empty (contains only whitespace).
     * @param str value to test
     * @return true if string is null or empty;
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || "".equals(str.trim());
    }
}
