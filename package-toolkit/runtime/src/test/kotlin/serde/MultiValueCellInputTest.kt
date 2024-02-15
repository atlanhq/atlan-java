/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package serde

import com.atlan.model.assets.Asset
import com.atlan.model.core.AtlanTag
import com.atlan.pkg.serde.cell.CellXformer
import java.util.SortedSet
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MultiValueCellInputTest {

    companion object {
        private val EMPTY = ""
        private val NULL = null
        private val ONE_VALUE = "Test<<PROPAGATED"
        private val MULTI_VALUE_N = "Test1<<PROPAGATED\nTest2<<PROPAGATED"
        private val MULTI_VALUE_RN = "Test1<<PROPAGATED\r\nTest2<<PROPAGATED"
    }

    @Test
    fun testEmpty() {
        val result = CellXformer.decode(Asset::class.java, EMPTY, SortedSet::class.java, AtlanTag::class.java, "atlanTags")
        assertNull(result)
    }

    @Test
    fun testNull() {
        val result = CellXformer.decode(Asset::class.java, NULL, SortedSet::class.java, AtlanTag::class.java, "atlanTags")
        assertNull(result)
    }

    @Test
    fun testOneValue() {
        val result = CellXformer.decode(Asset::class.java, ONE_VALUE, SortedSet::class.java, AtlanTag::class.java, "atlanTags")
        assertTrue(result is Collection<*>)
        assertTrue(result.isEmpty())
    }

    @Test
    fun testMultiValueN() {
        val result = CellXformer.decode(Asset::class.java, MULTI_VALUE_N, SortedSet::class.java, AtlanTag::class.java, "atlanTags")
        assertTrue(result is Collection<*>)
        assertTrue(result.isEmpty())
    }

    @Test
    fun testMultiValueRN() {
        val result = CellXformer.decode(Asset::class.java, MULTI_VALUE_RN, SortedSet::class.java, AtlanTag::class.java, "atlanTags")
        assertTrue(result is Collection<*>)
        assertTrue(result.isEmpty())
    }
}
