/* SPDX-License-Identifier: Apache-2.0
   Copyright 2026 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import com.atlan.pkg.lftag.model.ColumnLFTag
import com.atlan.pkg.lftag.model.LFTable
import com.atlan.pkg.lftag.model.LFTableInfo
import com.atlan.pkg.lftag.model.LFTagData
import com.atlan.pkg.lftag.model.LFTagPair
import org.testng.annotations.Test
import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Covers how the schema segment of each qualifiedName is derived from the Glue DatabaseName.
 * Does not need a live tenant.
 */
class CSVProducerSchemaNameTest {
    private val connectionQN = "default/athena/1234567890/example_db"
    private val connectionMap = mapOf("prod" to connectionQN)
    private val metadataMap = mapOf("security_classification" to "Data Privacy::Security Classification")
    private val tag = listOf(LFTagPair("security_classification", listOf("public"), "123456789012"))

    private fun tagData(
        databaseName: String,
        tableName: String = "tbl1",
    ) = LFTagData(
        listOf(
            LFTableInfo(
                table = LFTable(databaseName = databaseName, name = tableName),
                lfTagOnDatabase = tag,
                lfTagsOnTable = tag,
                lfTagsOnColumn = listOf(ColumnLFTag("col1", tag)),
            ),
        ),
    )

    private fun qualifiedNames(
        data: LFTagData,
        removeSchema: Boolean = false,
        keepDatabasePrefix: Boolean = false,
    ): List<String> {
        val file = File(createTempDirectory("lftag").toFile(), "out.csv")
        CSVProducer(connectionMap, metadataMap).transform(data, file.path, removeSchema, keepDatabasePrefix)
        val lines = file.readLines()
        val qnIndex = lines.first().split(',').indexOfFirst { it.trim('"') == "qualifiedName" }
        return lines.drop(1).map { it.split(',')[qnIndex].trim('"') }
    }

    @Test
    fun defaultStripsConnectionKeyFromSchema() {
        assertEquals(
            listOf(
                "$connectionQN/sales_analytics",
                "$connectionQN/sales_analytics/tbl1",
                "$connectionQN/sales_analytics/tbl1/col1",
            ),
            qualifiedNames(tagData("prod_sales_analytics")),
        )
    }

    @Test
    fun keepDatabasePrefixUsesFullDatabaseNameAsSchema() {
        assertEquals(
            listOf(
                "$connectionQN/prod_sales_analytics",
                "$connectionQN/prod_sales_analytics/tbl1",
                "$connectionQN/prod_sales_analytics/tbl1/col1",
            ),
            qualifiedNames(tagData("prod_sales_analytics"), keepDatabasePrefix = true),
        )
    }

    @Test
    fun keepDatabasePrefixWithRemoveSchemaStripsFullDatabaseName() {
        assertEquals(
            "$connectionQN/prod_sales_analytics/tbl1",
            qualifiedNames(
                tagData("prod_sales_analytics", "prod_sales_analytics.tbl1"),
                removeSchema = true,
                keepDatabasePrefix = true,
            )[1],
        )
    }

    @Test
    fun databaseNameWithoutUnderscoreIsSkippedNotThrown() {
        assertTrue(qualifiedNames(tagData("nounderscore")).isEmpty())
    }
}
