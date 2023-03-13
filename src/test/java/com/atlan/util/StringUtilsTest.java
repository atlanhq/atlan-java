/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.util;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class StringUtilsTest {

    final String qualifiedName1 = "default/s3/1234567890/aws:arn::somewhere/something/many/more/slashes.csv";
    final String qualifiedName2 = "default/sap-hana/1234567890/DATABASE/SCHEMA/table/column";

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

    @Test
    void getConnectionQualifiedName() {
        String t1 = "default/s3/1234567890";
        String invalid = "default/mongo/someName/and/then/more";
        assertEquals(StringUtils.getConnectionQualifiedName(qualifiedName1), t1);
        assertNull(StringUtils.getConnectionQualifiedName(invalid));
        assertNull(StringUtils.getConnectionQualifiedName(t1));
        String t2 = "default/sap-hana/1234567890";
        assertEquals(StringUtils.getConnectionQualifiedName(qualifiedName2), t2);
    }

    @Test
    void encodeDecodeContent() {
        final String test = "<h1>This is a test</h1><p>With some HTML...</p>";
        String encoded = StringUtils.encodeContent(test);
        assertNotNull(encoded);
        assertNotEquals(test, encoded);
        String decoded = StringUtils.decodeContent(encoded);
        assertNotNull(decoded);
        assertEquals(decoded, test);
    }
}
