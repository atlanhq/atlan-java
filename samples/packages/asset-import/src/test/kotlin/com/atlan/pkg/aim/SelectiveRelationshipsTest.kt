/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.AtlanCollection
import com.atlan.model.assets.AtlanQuery
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanDeleteType
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import org.testng.Assert.assertTrue
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

/**
 * Test selective management of tag associations.
 */
class SelectiveRelationshipsTest : PackageTest("sr") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val snowflake = Connection.findByName(client, "development", AtlanConnectorType.SNOWFLAKE)?.get(0)!!
    private val db1 = makeUnique("db1")
    private val sch1 = makeUnique("sch1")
    private val tbl1 = makeUnique("tbl1")
    private val tbl2 = makeUnique("tbl2")
    private val tbl3 = makeUnique("tbl3")
    private val col1 = makeUnique("col1")
    private val query1 = makeUnique("q1")
    private val query2 = makeUnique("q2")
    private val query3 = makeUnique("q3")
    private lateinit var collectionQN: String
    private lateinit var query1QN: String
    private lateinit var query2QN: String
    private lateinit var query3QN: String

    private val testFile = "selective_relationships.csv"

    private fun prepFile() {
        // Prepare a copy of the file with unique names for glossaries and tags
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{CONNECTION}}", snowflake.qualifiedName)
                        .replace("{{DB}}", db1)
                        .replace("{{SCH}}", sch1)
                        .replace("{{TBL1}}", tbl1)
                        .replace("{{TBL2}}", tbl2)
                        .replace("{{TBL3}}", tbl3)
                        .replace("{{Q1}}", query1QN)
                        .replace("{{Q2}}", query2QN)
                        .replace("{{Q3}}", query3QN)
                output.appendText("$revised\n")
            }
        }
    }

    private fun createDatabasesAndQueries() {
        val collection = AtlanCollection.creator(client, col1).build()
        val responseC = client.assets.save(collection)
        assertNotNull(responseC)
        assertEquals(1, responseC.createdAssets.size)
        val collectionR = responseC.getResult(collection)
        collectionQN = collectionR.qualifiedName
        val q1 = AtlanQuery.creator(query1, collectionR).build()
        val q2 = AtlanQuery.creator(query2, collectionR).build()
        val q3 = AtlanQuery.creator(query3, collectionR).build()
        val responseQ = client.assets.save(listOf(q1, q2, q3))
        assertNotNull(responseQ)
        assertEquals(3, responseQ.createdAssets.size)
        query1QN = responseQ.getResult(q1).qualifiedName
        query2QN = responseQ.getResult(q2).qualifiedName
        query3QN = responseQ.getResult(q3).qualifiedName

        val db =
            Database
                .creator(db1, snowflake.qualifiedName)
                .build()
        val sch =
            Schema
                .creator(sch1, db)
                .build()
        val t1 =
            Table
                .creator(tbl1, sch)
                .query(AtlanQuery.refByQualifiedName(query1QN))
                .build()
        val t2 =
            Table
                .creator(tbl2, sch)
                .query(AtlanQuery.refByQualifiedName(query2QN))
                .build()
        val t3 =
            Table
                .creator(tbl3, sch)
                .build()
        val response = client.assets.save(listOf(db, sch, t1, t2, t3))
        assertNotNull(response)
        assertEquals(5, response.createdAssets.size)
    }

    override fun setup() {
        createDatabasesAndQueries()
        prepFile()
    }

    override fun teardown() {
        val guidsQ =
            AtlanQuery
                .select(client)
                .where(AtlanQuery.COLLECTION_QUALIFIED_NAME.eq(collectionQN))
                .includeOnResults(AtlanQuery.COLLECTION_QUALIFIED_NAME)
                .includeOnResults(AtlanQuery.PARENT_QUALIFIED_NAME)
                .stream()
                .map { it.guid }
                .toList()
        if (guidsQ.isNotEmpty()) {
            logger.info { " ... purging ${guidsQ.size} queries..." }
            client.assets.delete(guidsQ, AtlanDeleteType.PURGE)
        }
        val guidC =
            AtlanCollection
                .select(client)
                .where(Asset.QUALIFIED_NAME.eq(collectionQN))
                .stream()
                .map { it.guid }
                .toList()
        if (guidC.isNotEmpty()) {
            logger.info { " ... purging ${guidC.size} collections..." }
            client.assets.delete(guidC, AtlanDeleteType.PURGE)
        }
        val guids =
            client.assets
                .select()
                .where(Asset.TYPE_NAME.`in`(listOf(Database.TYPE_NAME, Schema.TYPE_NAME, Table.TYPE_NAME)))
                .where(Asset.CONNECTION_QUALIFIED_NAME.eq(snowflake.qualifiedName))
                .where(Asset.NAME.`in`(listOf(db1, sch1, tbl1, tbl2, tbl3)))
                .stream()
                .map { it.guid }
                .toList()
        logger.info { " ... purging ${guids.size} assets..." }
        client.assets.delete(guids, AtlanDeleteType.PURGE)
    }

    @Test(groups = ["aim.sr.prereq"])
    fun tbl1WithQ1() {
        val table = getTable(tbl1)
        assertNotNull(table.queries)
        assertEquals(1, table.queries.size)
        assertEquals(query1, table.queries.first().name)
    }

    @Test(groups = ["aim.sr.prereq"])
    fun tbl2WithQ2() {
        val table = getTable(tbl2)
        assertNotNull(table.queries)
        assertEquals(1, table.queries.size)
        assertEquals(query2, table.queries.first().name)
    }

    @Test(groups = ["aim.sr.prereq"])
    fun tbl3WithoutQueries() {
        val table = getTable(tbl3)
        assertTrue(table.queries == null || table.queries.isEmpty())
    }

    @Test(groups = ["aim.sr.run"], dependsOnGroups = ["aim.sr.prereq"])
    fun runImport() {
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "update",
                assetsConfig = "advanced",
                assetsFailOnErrors = false,
            ),
            Importer::main,
        )
    }

    @Test(groups = ["aim.sr.test"], dependsOnGroups = ["aim.sr.run"])
    fun tbl1WithExtraQ() {
        val table = getTable(tbl1)
        assertNotNull(table.queries)
        assertFalse(table.queries.isEmpty())
        assertEquals(2, table.queries.size)
        assertEquals(setOf(query1, query2), table.queries.map { it.name }.toSet())
    }

    @Test(groups = ["aim.sr.test"], dependsOnGroups = ["aim.sr.run"])
    fun tbl2WithNoQ() {
        val table = getTable(tbl2)
        assertTrue(table.queries == null || table.queries.isEmpty())
    }

    @Test(groups = ["aim.sr.test"], dependsOnGroups = ["aim.sr.run"])
    fun tbl3WithSingleTag() {
        val table = getTable(tbl3)
        assertNotNull(table.queries)
        assertFalse(table.queries.isEmpty())
        assertEquals(1, table.queries.size)
        assertEquals(setOf(query3), table.queries.map { it.name }.toSet())
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }

    private fun getTable(tableName: String): Table {
        val request =
            Table
                .select(client)
                .where(Table.CONNECTION_QUALIFIED_NAME.eq(snowflake.qualifiedName))
                .where(Table.NAME.eq(tableName))
                .includeOnResults(Table.QUERIES)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1L)
        assertNotNull(response)
        assertNotNull(response.assets)
        assertEquals(1, response.assets.size)
        val table = response.assets[0]
        assertNotNull(table)
        assertEquals(tableName, table.name)
        assertTrue(table is Table)
        return table as Table
    }
}
