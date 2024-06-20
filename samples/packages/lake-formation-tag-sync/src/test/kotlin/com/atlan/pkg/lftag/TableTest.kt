/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.testng.annotations.Test
import kotlin.test.assertEquals
private val mapper = jacksonObjectMapper()

class TableTest {

    @Test
    fun whenDeserializableTableThenSuccess() {
        var json = """
        {
            "CatalogId": "614518280298",
            "DatabaseName": "dev_atlan_dev",
            "Name": "stg_customer_categories"
        }
        """.trimIndent()
        val table = mapper.readValue(json, Table::class.java)
        assertEquals("614518280298", table.catalogId)
        assertEquals("dev_atlan_dev", table.databaseName)
        assertEquals("stg_customer_categories", table.name)
    }
}
