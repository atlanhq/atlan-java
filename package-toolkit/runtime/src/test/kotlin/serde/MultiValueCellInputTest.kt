/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package serde

import com.atlan.mock.MockAtlanTenant
import com.atlan.mock.MockConfig
import com.atlan.model.assets.Asset
import com.atlan.model.core.AtlanTag
import com.atlan.model.enums.AtlanCustomAttributePrimitiveType
import com.atlan.model.typedefs.AttributeDef
import com.atlan.model.typedefs.AttributeDefOptions
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.Utils.getLogger
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.cell.CellXformer
import java.util.SortedSet
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MultiValueCellInputTest {
    private val logger = getLogger(this.javaClass.name)

    companion object {
        private val config = Utils.parseConfig<MockConfig>("{}", "{}")
        private val ctx = PackageContext(config, MockAtlanTenant.client, true)
        private val EMPTY = ""
        private val NULL = null
        private val ONE_VALUE = "Test<<PROPAGATED"
        private val MULTI_VALUE_N = "Test1<<PROPAGATED\nTest2<<PROPAGATED"
        private val MULTI_VALUE_RN = "Test1<<PROPAGATED\r\nTest2<<PROPAGATED"
        private val MULTI_VALUE_WITH_NEWLINES = "${CellXformer.encode(ctx, "Here's something\nwith a newline.")}\n${CellXformer.encode(ctx, "Here's something without a newline.")}"
    }

    @Test
    fun testEmpty() {
        val result = CellXformer.decode(ctx, Asset::class.java, EMPTY, SortedSet::class.java, AtlanTag::class.java, "atlanTags", logger)
        assertNull(result)
    }

    @Test
    fun testNull() {
        val result = CellXformer.decode(ctx, Asset::class.java, NULL, SortedSet::class.java, AtlanTag::class.java, "atlanTags", logger)
        assertNull(result)
    }

    @Test
    fun testOneValue() {
        val result = CellXformer.decode(ctx, Asset::class.java, ONE_VALUE, SortedSet::class.java, AtlanTag::class.java, "atlanTags", logger)
        assertTrue(result is Collection<*>)
        assertTrue(result.isEmpty())
    }

    @Test
    fun testMultiValueN() {
        val result = CellXformer.decode(ctx, Asset::class.java, MULTI_VALUE_N, SortedSet::class.java, AtlanTag::class.java, "atlanTags", logger)
        assertTrue(result is Collection<*>)
        assertTrue(result.isEmpty())
    }

    @Test
    fun testMultiValueRN() {
        val result = CellXformer.decode(ctx, Asset::class.java, MULTI_VALUE_RN, SortedSet::class.java, AtlanTag::class.java, "atlanTags", logger)
        assertTrue(result is Collection<*>)
        assertTrue(result.isEmpty())
    }

    @Test
    fun testMultiValueWithNewlines() {
        val result = CellXformer.decode(ctx, Asset::class.java, MULTI_VALUE_WITH_NEWLINES, List::class.java, String::class.java, "Custom::Metadata", logger)
        assertTrue(result is List<*>)
        assertTrue(result.isNotEmpty())
        assertEquals("Here's something\nwith a newline.", result[0])
        assertEquals("Here's something without a newline.", result[1])
    }

    @Test
    fun testMultiValueNewlineCustomMetadata() {
        val attrDef = AttributeDef.of(ctx.client, "Test", AtlanCustomAttributePrimitiveType.STRING, null, true, AttributeDefOptions.builder().build())
        val result = FieldSerde.getCustomMetadataValueFromString(ctx, attrDef, MULTI_VALUE_WITH_NEWLINES)
        assertTrue(result is List<*>)
        assertTrue(result.isNotEmpty())
        assertEquals("Here's something\nwith a newline.", result[0])
        assertEquals("Here's something without a newline.", result[1])
    }
}
