/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import com.atlan.model.assets.AtlanCollection
import com.atlan.model.assets.Column
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Table
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test import of a very simple file containing categories with the same name at a given level,
 * but in different parent paths.
 */
class OrderingTest {
    @Test
    fun test() {
        val types = setOf("xyz", Column.TYPE_NAME, AtlanCollection.TYPE_NAME, Table.TYPE_NAME, Connection.TYPE_NAME)
        val order = AssetImporter.getLoadOrder(types)
        assertEquals(listOf(AtlanCollection.TYPE_NAME, Table.TYPE_NAME, Column.TYPE_NAME, "xyz"), order)
    }
}
