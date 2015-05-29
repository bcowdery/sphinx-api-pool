package org.sphinx.util;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * StringUtilsTest
 *
 * @author Brian Cowdery
 * @since 28-05-2015
 */
public class StringUtilsTest {

    @Test
    public void testIsNullOrEmpty() throws Exception {
        assertTrue(StringUtils.isNullOrEmpty(""));
        assertTrue(StringUtils.isNullOrEmpty(" "));
        assertTrue(StringUtils.isNullOrEmpty("  "));
        assertTrue(StringUtils.isNullOrEmpty(null));
    }
}