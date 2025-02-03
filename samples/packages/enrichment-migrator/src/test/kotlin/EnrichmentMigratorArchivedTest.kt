/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanDeleteType
import com.atlan.model.enums.AtlanStatus
import com.atlan.model.search.IndexSearchResponse
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.aim.Importer
import com.atlan.util.AssetBatch
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test migration of asset metadata.
 */
class EnrichmentMigratorArchivedTest : PackageTest("a") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val c1 = makeUnique("c1")
    private val connectorType = AtlanConnectorType.AWS_SITE_WISE

    private val files =
        listOf(
            "asset-export.csv",
            "transformed-file.csv",
            "debug.log",
        )

    private fun createConnections() {
        Connection
            .creator(client, c1, connectorType)
            .build()
            .save(client)
            .block()
    }

    private fun createAssets() {
        val connection1 = Connection.findByName(client, c1, connectorType)[0]!!
        AssetBatch(client, 20).use { batch ->
            val db1 = Database.creator("db1", connection1.qualifiedName).build()
            batch.add(db1)
            val sch1 = Schema.creator("sch1", db1).build()
            batch.add(sch1)
            val tbl1 =
                Table
                    .creator("tbl1", sch1)
                    .userDescription("Must have some enrichment to be included!")
                    .build()
            batch.add(tbl1)
            batch.flush()
        }
    }

    private fun archiveTable() {
        val connection = Connection.findByName(client, c1, connectorType)?.get(0)?.qualifiedName!!
        val request =
            Table
                .select(client)
                .where(Table.QUALIFIED_NAME.startsWith(connection))
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val guids =
            response
                .stream()
                .map { it.guid }
                .toList()
        client.assets.delete(guids, AtlanDeleteType.SOFT).block()
    }

    override fun setup() {
        createConnections()
        createAssets()
        archiveTable()
        val connection = Connection.findByName(client, c1, connectorType)?.get(0)?.qualifiedName!!
        runCustomPackage(
            EnrichmentMigratorCfg(
                sourceConnection = listOf(connection),
                targetConnection = listOf(connection),
                failOnErrors = false,
                includeArchived = true,
            ),
            EnrichmentMigrator::main,
        )
        runCustomPackage(
            AssetImportCfg(
                assetsFile = "$testDirectory${File.separator}transformed-file.csv",
                assetsUpsertSemantic = "update",
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        removeConnection(c1, connectorType)
    }

    @Test
    fun activeAssetInFile() {
        val targetConnection = Connection.findByName(client, c1, connectorType)[0]!!
        fileHasLineStartingWith(
            filename = "transformed-file.csv",
            line =
                """
                "${targetConnection.qualifiedName}/db1/sch1/tbl1","Table","ACTIVE","tbl1",,,"Must have some enrichment to be included!"
                """.trimIndent(),
        )
    }

    @Test
    fun activeAsset() {
        val targetConnection = Connection.findByName(client, c1, connectorType)[0]!!
        val request =
            Table
                .select(client)
                .where(Table.QUALIFIED_NAME.startsWith(targetConnection.qualifiedName))
                .includeOnResults(Table.STATUS)
                .toRequest()
        var count = 0
        var status = AtlanStatus.DELETED
        var response: IndexSearchResponse? = null
        while (status == AtlanStatus.DELETED && count < (client.maxNetworkRetries * 2)) {
            response = retrySearchUntil(request, 1)
            val list = response.stream().toList()
            assertTrue(list.isNotEmpty())
            assertEquals(1, list.size)
            status = list[0].status
            count++
        }
        if (status != AtlanStatus.ACTIVE) {
            logger.error { "Exact request ($count): ${request.toJson(client)}" }
            logger.error { "Exact response ($count): ${response?.rawJsonObject}" }
        }
        assertEquals(AtlanStatus.ACTIVE, status)
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
