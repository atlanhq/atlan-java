/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.util;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

import com.atlan.model.assets.Connection;
import java.util.Locale;
import org.testng.annotations.Test;

public class StringUtilsTest {

    final String qualifiedName1 = "default/s3/1234567890/aws:arn::somewhere/something/many/more/slashes.csv";
    final String qualifiedName2 = "default/sap-hana/1234567890/DATABASE/SCHEMA/table/column";
    final String qualifiedNameRelaxed1 = "default/custom-connector/custom.example.com:1234/DB/SCH/table/col";

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
        final String t1 = "getAtlanTags";
        final String t2 = "setQualifiedName";
        assertEquals(StringUtils.getFieldNameFromMethodName(t1), "atlanTags");
        assertEquals(StringUtils.getFieldNameFromMethodName(t2), "qualifiedName");
    }

    @Test
    void getConnectionQualifiedName() {
        final String t1 = "default/s3/1234567890";
        final String invalid = "default/mongo/someName/and/then/more";
        assertEquals(StringUtils.getConnectionQualifiedName(qualifiedName1), t1);
        assertNull(StringUtils.getConnectionQualifiedName(invalid));
        assertNull(StringUtils.getConnectionQualifiedName(t1));
        final String t2 = "default/sap-hana/1234567890";
        assertEquals(StringUtils.getConnectionQualifiedName(qualifiedName2), t2);
    }

    @Test
    void getRelaxedConnectionQualifiedName() {
        final String t1 = "default/custom-connector/custom.example.com:1234";
        assertEquals(StringUtils.getConnectionQualifiedName(qualifiedNameRelaxed1, true), t1);
        assertNull(StringUtils.getConnectionQualifiedName(t1, true));
        final String t2 = "custom-connector";
        assertEquals(Connection.getConnectorFromQualifiedName(qualifiedNameRelaxed1), t2);
    }

    @Test
    void encodeDecodeContent() {
        final String test = "<h1>This is a test</h1><p>With some HTML...</p>";
        final String encoded = StringUtils.encodeContent(test);
        assertNotNull(encoded);
        assertNotEquals(test, encoded);
        final String decoded = StringUtils.decodeContent(encoded);
        assertNotNull(decoded);
        assertEquals(decoded, test);
    }

    @Test
    void snakeCaseSimple() {
        final String test = "SomeName";
        final String snakeCaseU = StringUtils.getUpperSnakeCase(test);
        assertEquals(snakeCaseU, "SOME_NAME");
        final String snakeCaseL = StringUtils.getLowerSnakeCase(test);
        assertEquals(snakeCaseL, snakeCaseU.toLowerCase(Locale.ROOT));
    }

    @Test
    void snakeCaseAcronymInMiddle() {
        final String test = "domainGUIDs";
        String snakeCaseU = StringUtils.getUpperSnakeCase(test);
        assertEquals(snakeCaseU, "DOMAIN_GUIDS");
        String snakeCaseL = StringUtils.getLowerSnakeCase(test);
        assertEquals(snakeCaseL, snakeCaseU.toLowerCase(Locale.ROOT));
        final String test2 = "assetPolicyGUIDs";
        snakeCaseU = StringUtils.getUpperSnakeCase(test2);
        assertEquals(snakeCaseU, "ASSET_POLICY_GUIDS");
        snakeCaseL = StringUtils.getLowerSnakeCase(test2);
        assertEquals(snakeCaseL, snakeCaseU.toLowerCase(Locale.ROOT));
        final String test3 = "nonCompliantAssetPolicyGUIDs";
        snakeCaseU = StringUtils.getUpperSnakeCase(test3);
        assertEquals(snakeCaseU, "NON_COMPLIANT_ASSET_POLICY_GUIDS");
        snakeCaseL = StringUtils.getLowerSnakeCase(test3);
        assertEquals(snakeCaseL, snakeCaseU.toLowerCase(Locale.ROOT));
    }

    @Test
    void snakeCaseAcronymAtEnd() {
        final String test = "dbtRawSQL";
        final String snakeCaseU = StringUtils.getLowerSnakeCase(test);
        assertEquals(snakeCaseU, "dbt_raw_sql");
        final String snakeCaseL = StringUtils.getLowerSnakeCase(test);
        assertEquals(snakeCaseL, snakeCaseU.toLowerCase(Locale.ROOT));
    }

    @Test
    void validConnectionQN() {
        final String test = "default/snowflake/1234567890";
        assertTrue(StringUtils.isValidConnectionQN(test));
    }

    @Test
    void invalidConnectionQN_tooShort() {
        final String test = "default/snowflake/123456789";
        assertFalse(StringUtils.isValidConnectionQN(test));
    }

    @Test
    void validConnectionQN_customType() {
        final String test = "default/xyz/1234567890";
        assertTrue(StringUtils.isValidConnectionQN(test));
        assertEquals(Connection.getConnectorFromQualifiedName(test), "xyz");
    }

    @Test
    void invalidConnectionQN_justWrong() {
        final String test = "MyConnection";
        assertFalse(StringUtils.isValidConnectionQN(test));
    }

    @Test
    void validRelaxedQN() {
        final String qn = StringUtils.getConnectionQualifiedName(qualifiedNameRelaxed1, true);
        assertNotNull(qn);
        assertFalse(qn.isEmpty());
        assertTrue(StringUtils.isValidConnectionQN(qn, true));
    }
}
