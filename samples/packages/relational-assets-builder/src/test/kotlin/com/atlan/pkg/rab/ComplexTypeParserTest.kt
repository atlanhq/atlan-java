/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Unit tests for ComplexTypeParser — no Atlan tenant required.
 */
class ComplexTypeParserTest {
    @Test
    fun `plain type returns null`() {
        assertNull(ComplexTypeParser.extractStructFields("VARCHAR(128)"))
        assertNull(ComplexTypeParser.extractStructFields("INT"))
        assertNull(ComplexTypeParser.extractStructFields("DECIMAL(10,2)"))
    }

    @Test
    fun `STRUCT with colon-separated fields`() {
        val result = ComplexTypeParser.extractStructFields("STRUCT<city:STRING, zip:INT>")
        assertNotNull(result)
        assertNull(result.syntheticNode)
        assertEquals(2, result.fields.size)
        assertEquals(ComplexTypeParser.FieldDefinition("city", "STRING"), result.fields[0])
        assertEquals(ComplexTypeParser.FieldDefinition("zip", "INT"), result.fields[1])
    }

    @Test
    fun `STRUCT with space-separated fields`() {
        val result = ComplexTypeParser.extractStructFields("STRUCT<city STRING, zip INT>")
        assertNotNull(result)
        assertNull(result.syntheticNode)
        assertEquals(2, result.fields.size)
        assertEquals("city", result.fields[0].name)
        assertEquals("STRING", result.fields[0].rawType)
    }

    @Test
    fun `STRUCT case-insensitive`() {
        val result = ComplexTypeParser.extractStructFields("struct<name:string>")
        assertNotNull(result)
        assertEquals(1, result.fields.size)
        assertEquals("name", result.fields[0].name)
        assertEquals("string", result.fields[0].rawType)
    }

    @Test
    fun `ARRAY of STRUCT returns items synthetic node`() {
        val result = ComplexTypeParser.extractStructFields("ARRAY<STRUCT<id:INT, email:STRING>>")
        assertNotNull(result)
        assertEquals("items", result.syntheticNode)
        assertEquals(2, result.fields.size)
        assertEquals(ComplexTypeParser.FieldDefinition("id", "INT"), result.fields[0])
        assertEquals(ComplexTypeParser.FieldDefinition("email", "STRING"), result.fields[1])
    }

    @Test
    fun `MAP with STRUCT value returns values synthetic node`() {
        val result = ComplexTypeParser.extractStructFields("MAP<STRING, STRUCT<key:STRING, value:DOUBLE>>")
        assertNotNull(result)
        assertEquals("values", result.syntheticNode)
        assertEquals(2, result.fields.size)
        assertEquals(ComplexTypeParser.FieldDefinition("key", "STRING"), result.fields[0])
        assertEquals(ComplexTypeParser.FieldDefinition("value", "DOUBLE"), result.fields[1])
    }

    @Test
    fun `ARRAY of non-STRUCT returns null`() {
        assertNull(ComplexTypeParser.extractStructFields("ARRAY<STRING>"))
        assertNull(ComplexTypeParser.extractStructFields("ARRAY<INT>"))
    }

    @Test
    fun `MAP with non-STRUCT value returns null`() {
        assertNull(ComplexTypeParser.extractStructFields("MAP<STRING, INT>"))
    }

    @Test
    fun `nested STRUCT within STRUCT field type is preserved for recursion`() {
        val result = ComplexTypeParser.extractStructFields("STRUCT<outer:STRUCT<inner:STRING, count:INT>, label:STRING>")
        assertNotNull(result)
        assertNull(result.syntheticNode)
        assertEquals(2, result.fields.size)
        assertEquals("outer", result.fields[0].name)
        assertEquals("STRUCT<inner:STRING, count:INT>", result.fields[0].rawType)
        assertEquals("label", result.fields[1].name)
        assertEquals("STRING", result.fields[1].rawType)

        // Recursively parse the inner STRUCT
        val nested = ComplexTypeParser.extractStructFields(result.fields[0].rawType)
        assertNotNull(nested)
        assertNull(nested.syntheticNode)
        assertEquals(2, nested.fields.size)
        assertEquals(ComplexTypeParser.FieldDefinition("inner", "STRING"), nested.fields[0])
        assertEquals(ComplexTypeParser.FieldDefinition("count", "INT"), nested.fields[1])
    }

    @Test
    fun `STRUCT field with DECIMAL type preserves parentheses`() {
        val result = ComplexTypeParser.extractStructFields("STRUCT<amount:DECIMAL(10,2), name:STRING>")
        assertNotNull(result)
        assertEquals(2, result.fields.size)
        assertEquals(ComplexTypeParser.FieldDefinition("amount", "DECIMAL(10,2)"), result.fields[0])
        assertEquals(ComplexTypeParser.FieldDefinition("name", "STRING"), result.fields[1])
    }

    @Test
    fun `ARRAY of ARRAY of STRUCT combines synthetic nodes`() {
        val result = ComplexTypeParser.extractStructFields("ARRAY<ARRAY<STRUCT<id:INT>>>")
        assertNotNull(result)
        assertEquals("items/items", result.syntheticNode)
        assertEquals(1, result.fields.size)
        assertEquals("id", result.fields[0].name)
    }

    @Test
    fun `whitespace trimmed from field names and types`() {
        val result = ComplexTypeParser.extractStructFields("STRUCT< city : STRING , zip : INT >")
        assertNotNull(result)
        assertEquals("city", result.fields[0].name)
        assertEquals("STRING", result.fields[0].rawType)
        assertEquals("zip", result.fields[1].name)
        assertEquals("INT", result.fields[1].rawType)
    }
}
