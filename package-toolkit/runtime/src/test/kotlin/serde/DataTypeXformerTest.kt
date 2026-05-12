/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package serde

import com.atlan.pkg.serde.cell.DataTypeXformer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DataTypeXformerTest {
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
}
