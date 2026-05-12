/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package serde

import com.atlan.pkg.serde.cell.DataTypeXformer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DataTypeXformerTest {
    private val dataTypeField = DataTypeXformer.FIELDS.first()

    // --- getPrecision / getScale / getMaxLength ---

    @Test
    fun `DECIMAL precision and scale parsed correctly`() {
        assertEquals(10, DataTypeXformer.getPrecision("DECIMAL(10,2)"))
        assertEquals(2.0, DataTypeXformer.getScale("DECIMAL(10,2)"))
    }

    @Test
    fun `VARCHAR max length parsed correctly`() {
        assertEquals(255L, DataTypeXformer.getMaxLength("VARCHAR(255)"))
    }

    @Test
    fun `VARCHAR MAX returns null`() {
        assertNull(DataTypeXformer.getMaxLength("VARCHAR(MAX)"))
    }

    @Test
    fun `plain type - all return null`() {
        assertNull(DataTypeXformer.getPrecision("INT"))
        assertNull(DataTypeXformer.getScale("INT"))
        assertNull(DataTypeXformer.getMaxLength("INT"))
    }

    @Test
    fun `null input - all return null`() {
        assertNull(DataTypeXformer.getPrecision(null))
        assertNull(DataTypeXformer.getScale(null))
        assertNull(DataTypeXformer.getMaxLength(null))
    }

    // --- decode: type extraction ---

    @Test
    fun `VARCHAR(MAX) - type decoded as VARCHAR even when max length is null`() {
        assertNull(DataTypeXformer.getMaxLength("VARCHAR(MAX)"))
        assertEquals("VARCHAR", DataTypeXformer.decode("VARCHAR(MAX)", dataTypeField))
    }

    @Test
    fun `INT - type decoded as INT even when all numeric params are null`() {
        assertNull(DataTypeXformer.getPrecision("INT"))
        assertNull(DataTypeXformer.getScale("INT"))
        assertNull(DataTypeXformer.getMaxLength("INT"))
        assertEquals("INT", DataTypeXformer.decode("INT", dataTypeField))
    }

    @Test
    fun `DECIMAL - type decoded as DECIMAL regardless of precision and scale`() {
        assertEquals("DECIMAL", DataTypeXformer.decode("DECIMAL(10,2)", dataTypeField))
    }

    @Test
    fun `VARCHAR with numeric length - type decoded as VARCHAR`() {
        assertEquals("VARCHAR", DataTypeXformer.decode("VARCHAR(255)", dataTypeField))
    }

    @Test
    fun `NVARCHAR mapped to VARCHAR on decode`() {
        assertEquals("VARCHAR", DataTypeXformer.decode("NVARCHAR(100)", dataTypeField))
    }

    @Test
    fun `lowercase type name is uppercased on decode`() {
        assertEquals("VARCHAR", DataTypeXformer.decode("varchar(255)", dataTypeField))
        assertEquals("INT", DataTypeXformer.decode("int", dataTypeField))
    }

    @Test
    fun `spurious type values decoded as null`() {
        assertNull(DataTypeXformer.decode("DATA TYPE", dataTypeField))
        assertNull(DataTypeXformer.decode("NULL", dataTypeField))
    }

    @Test
    fun `null or blank type decoded as null`() {
        assertNull(DataTypeXformer.decode(null, dataTypeField))
        assertNull(DataTypeXformer.decode("", dataTypeField))
        assertNull(DataTypeXformer.decode("   ", dataTypeField))
    }
}
