package com.atlan.util;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class StringUtilsTest {

    @Test
    void containsWhitespace() {
        assertTrue(StringUtils.containsWhitespace("this is a test"));
        assertTrue(StringUtils.containsWhitespace("\n"));
        assertFalse(StringUtils.containsWhitespace("testing"));
        assertFalse(StringUtils.containsWhitespace(""));
    }

    @Test
    void secureCompare() {
        final String t1 = "abc";
        final String t2 = "abc";
        final String t3 = "def";
        assertTrue(StringUtils.secureCompare(t1, t1));
        assertTrue(StringUtils.secureCompare(t1, t2));
        assertFalse(StringUtils.secureCompare(t2, t3));
    }

    @Test
    void getFieldNameFromMethodName() {
        String t1 = "getClassifications";
        String t2 = "setQualifiedName";
        assertEquals(StringUtils.getFieldNameFromMethodName(t1), "classifications");
        assertEquals(StringUtils.getFieldNameFromMethodName(t2), "qualifiedName");
    }
}
