/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanDeleteType
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.util.AssetBatch
import kotlin.test.Test

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
    }

    override fun teardown() {
        removeConnection(c1, connectorType)
    }

    @Test
    fun activeAssetInFile() {
        val targetConnection = Connection.findByName(client, c1, connectorType)[0]!!
        fileHasLine(
            filename = "transformed-file.csv",
            line =
                """
                "${targetConnection.qualifiedName}/db1/sch1/tbl1","Table","ACTIVE","tbl1",,,"Must have some enrichment to be included!",,,,,,,,,,,,,,,,,
                """.trimIndent(),
        )
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
