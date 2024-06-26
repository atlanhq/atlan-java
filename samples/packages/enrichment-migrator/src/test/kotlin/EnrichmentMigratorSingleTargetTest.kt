/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.core.CustomMetadataAttributes
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanCustomAttributePrimitiveType
import com.atlan.model.fields.CustomMetadataField
import com.atlan.model.typedefs.AttributeDef
import com.atlan.model.typedefs.CustomMetadataDef
import com.atlan.pkg.PackageTest
import com.atlan.util.AssetBatch
import mu.KotlinLogging
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test migration of asset metadata.
 */
class EnrichmentMigratorSingleTargetTest : PackageTest() {
    override val logger = KotlinLogging.logger {}

    private val c1 = makeUnique("emstc1")
    private val c2 = makeUnique("emstc2")
    private val cm1 = makeUnique("emstcm")
    private val now = Instant.now().toEpochMilli()

    private val files = listOf(
        "asset-export.csv",
        "debug.log",
    )

    private fun createConnections() {
        Connection.creator(c1, AtlanConnectorType.MSSQL)
            .build()
            .save()
            .block()
        Connection.creator(c2, AtlanConnectorType.POSTGRES)
            .build()
            .save()
            .block()
    }

    private fun createCustomMetadata() {
        CustomMetadataDef.creator(cm1)
            .attributeDef(AttributeDef.of("dateSingle", AtlanCustomAttributePrimitiveType.DATE, false))
            .build()
            .create()
    }

    private fun createAssets() {
        val client = Atlan.getDefaultClient()
        val connection1 = Connection.findByName(c1, AtlanConnectorType.MSSQL)[0]!!
        val connection2 = Connection.findByName(c2, AtlanConnectorType.POSTGRES)[0]!!
        val batch = AssetBatch(client, 20)
        val db1 = Database.creator("db1", connection1.qualifiedName).build()
        batch.add(db1)
        val db2 = Database.creator("db1", connection2.qualifiedName).build()
        batch.add(db2)
        val sch1 = Schema.creator("sch1", db1).build()
        batch.add(sch1)
        val sch2 = Schema.creator("sch1", db2).build()
        batch.add(sch2)
        val tbl1 = Table.creator("tbl1", sch1)
            .customMetadata(
                cm1,
                CustomMetadataAttributes.builder()
                    .attribute("dateSingle", now)
                    .build(),
            )
            .build()
        batch.add(tbl1)
        val tbl2 = Table.creator("tbl1", sch2).build()
        batch.add(tbl2)
        batch.flush()
    }

    override fun setup() {
        createConnections()
        createCustomMetadata()
        createAssets()
        setup(
            EnrichmentMigratorCfg(
                sourceConnection = listOf(Connection.findByName(c1, AtlanConnectorType.MSSQL)?.get(0)?.qualifiedName!!),
                targetConnection = listOf(Connection.findByName(c2, AtlanConnectorType.POSTGRES)?.get(0)?.qualifiedName!!),
                failOnErrors = false,
                cmLimitType = "INCLUDE",
                customMetadata = "$cm1::dateSingle",
            ),
        )
        EnrichmentMigrator.main(arrayOf(testDirectory))
        Thread.sleep(15000)
    }

    override fun teardown() {
        val client = Atlan.getDefaultClient()
        removeConnection(c1, AtlanConnectorType.MSSQL)
        removeConnection(c2, AtlanConnectorType.POSTGRES)
        client.typeDefs.purge(client.customMetadataCache.getIdForName(cm1))
    }

    @Test
    fun datesOnTarget() {
        val targetConnection = Connection.findByName(c2, AtlanConnectorType.POSTGRES)[0]!!
        val client = Atlan.getDefaultClient()
        val cmField = CustomMetadataField.of(client, cm1, "dateSingle")
        val request = Table.select()
            .where(Table.QUALIFIED_NAME.startsWith(targetConnection.qualifiedName))
            .where(cmField.hasAnyValue())
            .includeOnResults(cmField)
            .toRequest()
        val response = retrySearchUntil(request, 1)
        response.stream()
            .forEach {
                val cm = it.customMetadataSets
                assertNotNull(cm)
                val attrs = cm[cm1]?.attributes
                assertNotNull(attrs)
                assertEquals(1, attrs.size)
                assertEquals(now, attrs["dateSingle"])
            }
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
        val targetConnection = Connection.findByName(c2, AtlanConnectorType.POSTGRES)[0]!!
        val filename = targetConnection.qualifiedName.replace("/", "_")
        validateFilesExist(listOf("CSA_EM_transformed_$filename.csv"))
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
