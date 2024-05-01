/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanDeleteType
import com.atlan.model.enums.AtlanStatus
import com.atlan.pkg.PackageTest
import com.atlan.util.AssetBatch
import org.testng.ITestContext
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test migration of asset metadata.
 */
class EnrichmentMigratorArchivedTest : PackageTest() {

    private val c1 = makeUnique("ema1")
    private val connectorType = AtlanConnectorType.COCKROACHDB

    private val files = listOf(
        "asset-export.csv",
        "debug.log",
    )

    private fun createConnections() {
        val client = Atlan.getDefaultClient()
        Connection.creator(c1, connectorType, listOf(client.roleCache.getIdForName("\$admin")), null, null)
            .build()
            .save()
            .block()
    }

    private fun createAssets() {
        val client = Atlan.getDefaultClient()
        val connection1 = Connection.findByName(c1, connectorType)[0]!!
        val batch = AssetBatch(client, 20)
        val db1 = Database.creator("db1", connection1.qualifiedName).build()
        batch.add(db1)
        val sch1 = Schema.creator("sch1", db1).build()
        batch.add(sch1)
        val tbl1 = Table.creator("tbl1", sch1)
            .userDescription("Must have some enrichment to be included!")
            .build()
        batch.add(tbl1)
        batch.flush()
    }

    private fun archiveTable() {
        val connection = Connection.findByName(c1, connectorType)?.get(0)?.qualifiedName!!
        val request = Table.select()
            .where(Table.QUALIFIED_NAME.startsWith(connection))
            .toRequest()
        val response = retrySearchUntil(request, 1)
        val guids = response.stream()
            .map { it.guid }
            .toList()
        Atlan.getDefaultClient().assets.delete(guids, AtlanDeleteType.SOFT).block()
    }

    @BeforeClass
    fun beforeClass() {
        createConnections()
        createAssets()
        archiveTable()
        val connection = Connection.findByName(c1, connectorType)?.get(0)?.qualifiedName!!
        setup(
            EnrichmentMigratorCfg(
                sourceConnection = listOf(connection),
                targetConnection = listOf(connection),
                failOnErrors = false,
                includeArchived = true,
            ),
        )
        EnrichmentMigrator.main(arrayOf(testDirectory))
        Thread.sleep(10000)
    }

    @Test
    fun activeAssetMigrated() {
        val targetConnection = Connection.findByName(c1, connectorType)[0]!!
        val request = Table.select()
            .where(Table.QUALIFIED_NAME.startsWith(targetConnection.qualifiedName))
            .includeOnResults(Table.STATUS)
            .toRequest()
        var count = 0
        var status = AtlanStatus.DELETED
        while (status == AtlanStatus.DELETED && count < Atlan.getDefaultClient().maxNetworkRetries) {
            val response = retrySearchUntil(request, 1)
            val list = response.stream().toList()
            assertTrue(list.isNotEmpty())
            assertEquals(1, list.size)
            status = list[0].status
            count++
        }
        assertEquals(AtlanStatus.ACTIVE, status)
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
        val targetConnection = Connection.findByName(c1, connectorType)[0]!!
        val filename = targetConnection.qualifiedName.replace("/", "_")
        validateFilesExist(listOf("CSA_EM_transformed_$filename.csv"))
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }

    @AfterClass(alwaysRun = true)
    fun afterClass(context: ITestContext) {
        removeConnection(c1, connectorType)
        teardown(context.failedTests.size() > 0)
    }
}
