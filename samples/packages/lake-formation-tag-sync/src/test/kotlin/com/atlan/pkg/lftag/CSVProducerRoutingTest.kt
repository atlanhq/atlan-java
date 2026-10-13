/* SPDX-License-Identifier: Apache-2.0
   Copyright 2026 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import com.atlan.pkg.lftag.model.ColumnLFTag
import com.atlan.pkg.lftag.model.LFTable
import com.atlan.pkg.lftag.model.LFTableInfo
import com.atlan.pkg.lftag.model.LFTagData
import com.atlan.pkg.lftag.model.LFTagPair
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.testng.annotations.Test
import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * Covers routing each table to one of several candidate connections for a connection map key.
 * Does not need a live tenant.
 */
class CSVProducerRoutingTest {
    private val conn1 = "default/redshift/1111111111/db"
    private val conn2 = "default/redshift/2222222222/db"
    private val metadataMap = mapOf("security_classification" to "Data Privacy::Security Classification")
    private val tag = listOf(LFTagPair("security_classification", listOf("public"), "123456789012"))

    private fun tagData(vararg tables: String) =
        LFTagData(
            tables.map {
                LFTableInfo(
                    table = LFTable(databaseName = "dev_sales", name = it),
                    lfTagOnDatabase = tag,
                    lfTagsOnTable = tag,
                    lfTagsOnColumn = listOf(ColumnLFTag("col1", tag)),
                )
            },
        )

    private fun qualifiedNames(
        connectionMap: Map<String, List<String>>,
        data: LFTagData,
        locator: CSVProducer.TableLocator?,
    ): List<String> {
        val file = File(createTempDirectory("lftag").toFile(), "out.csv")
        CSVProducer(connectionMap, metadataMap, locator).transform(data, file.path)
        val lines = file.readLines()
        val qnIndex = lines.first().split(',').indexOfFirst { it.trim('"') == "qualifiedName" }
        return lines.drop(1).map { it.split(',')[qnIndex].trim('"') }
    }

    @Test
    fun singleCandidateIsWrittenWithoutLookup() {
        val locator = CSVProducer.TableLocator { throw AssertionError("lookup must not run for a single candidate") }
        assertEquals(
            listOf("$conn1/sales", "$conn1/sales/tbl1", "$conn1/sales/tbl1/col1"),
            qualifiedNames(mapOf("dev" to listOf(conn1)), tagData("tbl1"), locator),
        )
    }

    @Test
    fun tableIsWrittenToTheOneCandidateThatHoldsIt() {
        val existing = setOf("$conn1/sales/tbl1", "$conn2/sales/tbl2")
        val locator = CSVProducer.TableLocator { qns -> qns.filter { it in existing }.toSet() }
        assertEquals(
            listOf(
                "$conn1/sales",
                "$conn1/sales/tbl1",
                "$conn1/sales/tbl1/col1",
                "$conn2/sales",
                "$conn2/sales/tbl2",
                "$conn2/sales/tbl2/col1",
            ),
            qualifiedNames(mapOf("dev" to listOf(conn1, conn2)), tagData("tbl1", "tbl2"), locator),
        )
    }

    @Test
    fun tableFoundNowhereIsNotWritten() {
        val locator = CSVProducer.TableLocator { emptySet() }
        assertTrue(qualifiedNames(mapOf("dev" to listOf(conn1, conn2)), tagData("tbl1"), locator).isEmpty())
    }

    @Test
    fun tableFoundUnderSeveralCandidatesIsNotWritten() {
        val locator = CSVProducer.TableLocator { qns -> qns.toSet() }
        assertTrue(qualifiedNames(mapOf("dev" to listOf(conn1, conn2)), tagData("tbl1"), locator).isEmpty())
    }

    @Test
    fun connectionMapAcceptsStringsAndLists() {
        val root = jacksonObjectMapper().readTree("""{"dev": "$conn1", "val": ["$conn1", "$conn2", "$conn1"]}""")
        assertEquals(
            mapOf("dev" to listOf(conn1), "val" to listOf(conn1, conn2)),
            CSVProducer.parseConnectionMap(root),
        )
    }

    @Test
    fun connectionMapRejectsOtherValues() {
        val mapper = jacksonObjectMapper()
        listOf("""{"dev": []}""", """{"dev": 1}""", """{"dev": ["$conn1", 2]}""", """["$conn1"]""").forEach {
            assertFailsWith<IllegalArgumentException> { CSVProducer.parseConnectionMap(mapper.readTree(it)) }
        }
    }
}
