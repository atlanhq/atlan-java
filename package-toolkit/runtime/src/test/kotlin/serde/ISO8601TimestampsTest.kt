/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package serde

import com.atlan.mock.MockAtlanTenant
import com.atlan.mock.MockConfig
import com.atlan.model.assets.Asset
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.Utils.getLogger
import com.atlan.pkg.serde.cell.CellXformer
import com.atlan.pkg.serde.cell.TimestampXformer
import java.time.format.DateTimeParseException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ISO8601TimestampsTest {
    private val logger = getLogger(this.javaClass.name)

    companion object {
        init {
            MockAtlanTenant.initializeClient()
        }

        private val config = Utils.parseConfig<MockConfig>("{}", "{}")
        private val ctx = PackageContext(config, MockAtlanTenant.client, true)
        private val EMPTY = ""
        private val NULL = null
        private val STRING_VALUE = "2024-02-15T20:41:11.565Z"
        private val LONG_VALUE = 1676629271565L
        private val INVALID_VALUE = "02/15/2024 20:41:11.565"
    }

    @Test
    fun testEmptyDecode() {
        val result = CellXformer.decode(ctx, Asset::class.java, EMPTY, Long::class.java, null, "createTime", logger)
        assertNull(result)
    }

    @Test
    fun testNullDecode() {
        val result = CellXformer.decode(ctx, Asset::class.java, NULL, Long::class.java, null, "createTime", logger)
        assertNull(result)
    }

    @Test
    fun testStringValueDecode() {
        val result = CellXformer.decode(ctx, Asset::class.java, STRING_VALUE, Long::class.java, null, "createTime", logger)
        assertTrue(result is Long)
        assertTrue(result > 0)
    }

    @Test
    fun testLongValueDecode() {
        val result = CellXformer.decode(ctx, Asset::class.java, LONG_VALUE.toString(), Long::class.java, null, "createTime", logger)
        assertTrue(result is Long)
        assertTrue(result > 0)
    }

    @Test
    fun testInvalidValueDecode() {
        assertFailsWith(DateTimeParseException::class, "Text '02/15/2024 20:41:11.565' could not be parsed at index 0") {
            CellXformer.decode(ctx, Asset::class.java, INVALID_VALUE, Long::class.java, null, "createTime", logger)
        }
    }

    @Test
    fun testEmptyEncode() {
        val result = CellXformer.encode(ctx, EMPTY, guid = "guid", dates = true)
        assertTrue(result.isBlank())
    }

    @Test
    fun testNullEncode() {
        val result = CellXformer.encode(ctx, NULL, guid = "guid", dates = true)
        assertTrue(result.isBlank())
    }

    @Test
    fun testStringValueEncode() {
        val result = CellXformer.encode(ctx, STRING_VALUE, guid = "guid", dates = true)
        assertNotNull(result)
        assertEquals(STRING_VALUE, result)
    }

    @Test
    fun testLongValueEncode() {
        val result = CellXformer.encode(ctx, LONG_VALUE, guid = "guid", dates = true)
        assertNotNull(result)
        assertEquals(TimestampXformer.encode(LONG_VALUE), result)
    }

    @Test
    fun testInvalidValueEncode() {
        val result = CellXformer.encode(ctx, INVALID_VALUE, guid = "guid", dates = true)
        assertEquals(INVALID_VALUE, result)
    }
}
