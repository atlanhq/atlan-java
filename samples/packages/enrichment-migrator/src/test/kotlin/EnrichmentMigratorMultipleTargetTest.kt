/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import com.atlan.util.AssetBatch
import mu.KotlinLogging
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test migration of asset metadata.
 */
class EnrichmentMigratorMultipleTargetTest : PackageTest("mt") {
    override val logger = KotlinLogging.logger {}

    private val c1 = makeUnique("c1")
    private val c2 = makeUnique("c2")
    private val c3 = makeUnique("c3")
    private val c1Type = AtlanConnectorType.REDIS
    private val c2Type = AtlanConnectorType.STARBURST_GALAXY
    private val c3Type = AtlanConnectorType.STARBURST_GALAXY

    private val files =
        listOf(
            "asset-export.csv",
            "debug.log",
        )

    private fun createConnections() {
        AssetBatch(client, 5).use { batch ->
            batch.add(Connection.creator(client, c1, c1Type).build())
            batch.add(Connection.creator(client, c2, c2Type).build())
            batch.add(Connection.creator(client, c3, c3Type).build())
            batch.flush()
        }
    }

    private fun createAssets() {
        val connection1 = Connection.findByName(client, c1, c1Type)[0]!!
        val connection2 = Connection.findByName(client, c2, c2Type)[0]!!
        val connection3 = Connection.findByName(client, c3, c3Type)[0]!!
        AssetBatch(client, 20).use { batch ->
            val db1 = Database.creator("db1", connection1.qualifiedName).build()
            batch.add(db1)
            val db2 = Database.creator("db1", connection2.qualifiedName).build()
            batch.add(db2)
            val db3 = Database.creator("db1", connection3.qualifiedName).build()
            batch.add(db3)
            val sch1 = Schema.creator("sch1", db1).build()
            batch.add(sch1)
            val sch2 = Schema.creator("sch1", db2).build()
            batch.add(sch2)
            val sch3 = Schema.creator("sch1", db3).build()
            batch.add(sch3)
            val tbl1 =
                Table.creator("tbl1", sch1)
                    .description("Some description.")
                    .build()
            batch.add(tbl1)
            val tbl2 = Table.creator("tbl1", sch2).build()
            batch.add(tbl2)
            val tbl3 = Table.creator("tbl1", sch3).build()
            batch.add(tbl3)
            batch.flush()
        }
    }

    override fun setup() {
        createConnections()
        createAssets()
        Thread.sleep(15000)
        runCustomPackage(
            EnrichmentMigratorCfg(
                sourceConnection = listOf(Connection.findByName(client, c1, c1Type)?.get(0)?.qualifiedName!!),
                targetConnection =
                    listOf(
                        Connection.findByName(client, c2, c2Type)?.get(0)?.qualifiedName!!,
                        Connection.findByName(client, c3, c3Type)?.get(0)?.qualifiedName!!,
                    ),
                failOnErrors = false,
                limitType = "INCLUDE",
                attributesList = listOf("description"),
            ),
            EnrichmentMigrator::main,
        )
        Thread.sleep(15000)
    }

    override fun teardown() {
        removeConnection(c1, c1Type)
        removeConnection(c2, c2Type)
        removeConnection(c3, c3Type)
    }

    @Test
    fun datesOnTarget() {
        val targetConnection = Connection.findByName(client, c2, c2Type)[0]!!
        Table.select(client)
            .where(Table.QUALIFIED_NAME.startsWith(targetConnection.qualifiedName))
            .includeOnResults(Table.DESCRIPTION)
            .stream()
            .forEach {
                assertEquals("Some description.", it.description)
            }
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
        val t1 = Connection.findByName(client, c2, c2Type)[0]!!
        val t2 = Connection.findByName(client, c3, c3Type)[0]!!
        val f1 = t1.qualifiedName.replace("/", "_")
        val f2 = t2.qualifiedName.replace("/", "_")
        validateFilesExist(listOf("CSA_EM_transformed_$f1.csv", "CSA_EM_transformed_$f2.csv"))
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
