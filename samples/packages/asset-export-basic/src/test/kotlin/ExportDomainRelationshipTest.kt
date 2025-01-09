/* SPDX-License-Identifier: Apache-2.0
   Copyright 2025 Atlan Pte. Ltd. */
import com.atlan.model.assets.*
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanDeleteType
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.util.AssetBatch
import org.testng.annotations.Test

/*
 * Test export of domain and asset relationships
 */
class ExportDomainRelationshipTest : PackageTest("edr") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val c1 = makeUnique("c1")
    private val connectorType = AtlanConnectorType.SNOWFLAKE

    private val files =
        listOf(
            "asset-export.csv",
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
        val dmn1 = DataDomain.creator(c1).build()
        val response = dmn1.save(client)
        val domain = response.getResult(dmn1)
        AssetBatch(client, 50).use { batch ->
            val db1 = Database.creator("db1", connection1.qualifiedName).build()
            val sch1 = Schema.creator("sch1", db1).build()
            val tbl1 = Table.creator("tbl1", sch1)
                .userDescription("Export Domain Relationship Test Table Java SDK")
                .build()
            batch.add(db1)
            batch.add(sch1)
            batch.add(tbl1)
            batch.flush()
            val update = Table.updater(tbl1.qualifiedName, tbl1.name)
                .domainGUIDs(listOf(domain.guid))
                .build()
            batch.add(update)
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
        client.assets.delete(guids, AtlanDeleteType.HARD).block()
        val domain = DataDomain.select(client).where(DataDomain.NAME.startsWith(c1)).toRequest()
        val domainResponse = retrySearchUntil(domain, 1)
        val domainGuids =
            domainResponse
                .stream()
                .map { it.guid }
                .toList()
        client.assets.delete(domainGuids, AtlanDeleteType.HARD).block()
    }

    override fun setup() {
        createConnections()
        createAssets()
        runCustomPackage(AssetExportBasicCfg(
                exportScope = "",
                qnPrefix = Connection.findByName(client, c1, connectorType)?.get(0)?.qualifiedName!!,
                includeGlossaries = false,
                includeProducts = false,
            ),
            Exporter::main,
        )
        Thread.sleep(15000)
    }

    override fun teardown() {
        archiveTable()
        removeConnection(c1, connectorType)
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
