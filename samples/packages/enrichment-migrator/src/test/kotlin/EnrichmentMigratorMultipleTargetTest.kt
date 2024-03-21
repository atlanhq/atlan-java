/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import com.atlan.util.AssetBatch
import org.testng.ITestContext
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test migration of asset metadata.
 */
class EnrichmentMigratorMultipleTargetTest : PackageTest() {

    private val c1 = makeUnique("emmc1")
    private val c2 = makeUnique("emmc2")
    private val c3 = makeUnique("emmc3")

    private val files = listOf(
        "asset-export.csv",
        "debug.log",
    )

    private fun createConnections() {
        val client = Atlan.getDefaultClient()
        Connection.creator(c1, AtlanConnectorType.HIVE, listOf(client.roleCache.getIdForName("\$admin")), null, null)
            .build()
            .save()
            .block()
        Connection.creator(c2, AtlanConnectorType.ESSBASE, listOf(client.roleCache.getIdForName("\$admin")), null, null)
            .build()
            .save()
            .block()
        Connection.creator(c3, AtlanConnectorType.ESSBASE, listOf(client.roleCache.getIdForName("\$admin")), null, null)
            .build()
            .save()
            .block()
    }

    private fun createAssets() {
        val client = Atlan.getDefaultClient()
        val connection1 = Connection.findByName(c1, AtlanConnectorType.HIVE)[0]!!
        val connection2 = Connection.findByName(c2, AtlanConnectorType.ESSBASE)[0]!!
        val connection3 = Connection.findByName(c3, AtlanConnectorType.ESSBASE)[0]!!
        val batch = AssetBatch(client, 20)
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
        val tbl1 = Table.creator("tbl1", sch1)
            .description("Some description.")
            .build()
        batch.add(tbl1)
        val tbl2 = Table.creator("tbl1", sch2).build()
        batch.add(tbl2)
        val tbl3 = Table.creator("tbl1", sch3).build()
        batch.add(tbl3)
        batch.flush()
    }

    @BeforeClass
    fun beforeClass() {
        createConnections()
        createAssets()
        setup(
            EnrichmentMigratorCfg(
                sourceConnection = listOf(Connection.findByName(c1, AtlanConnectorType.HIVE)?.get(0)?.qualifiedName!!),
                targetConnection = listOf(
                    Connection.findByName(c2, AtlanConnectorType.ESSBASE)?.get(0)?.qualifiedName!!,
                    Connection.findByName(c3, AtlanConnectorType.ESSBASE)?.get(0)?.qualifiedName!!,
                ),
                failOnErrors = false,
                limitType = "INCLUDE",
                attributesList = listOf("description"),
            ),
        )
        EnrichmentMigrator.main(arrayOf(testDirectory))
        Thread.sleep(5000)
    }

    @Test
    fun datesOnTarget() {
        val targetConnection = Connection.findByName(c2, AtlanConnectorType.ESSBASE)[0]!!
        Table.select()
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
        val t1 = Connection.findByName(c2, AtlanConnectorType.ESSBASE)[0]!!
        val t2 = Connection.findByName(c3, AtlanConnectorType.ESSBASE)[0]!!
        val f1 = t1.qualifiedName.replace("/", "_")
        val f2 = t2.qualifiedName.replace("/", "_")
        validateFilesExist(listOf("CSA_EM_transformed_$f1.csv", "CSA_EM_transformed_$f2.csv"))
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }

    @AfterClass(alwaysRun = true)
    fun afterClass(context: ITestContext) {
        removeConnection(c1, AtlanConnectorType.HIVE)
        removeConnection(c2, AtlanConnectorType.ESSBASE)
        removeConnection(c3, AtlanConnectorType.ESSBASE)
        teardown(context.failedTests.size() > 0)
    }
}
