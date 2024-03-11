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
import org.testng.ITestContext
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test migration of asset metadata.
 */
class EnrichmentMigratorTest : PackageTest() {

    private val c1 = makeUnique("emc1")
    private val c2 = makeUnique("emc2")
    private val cm1 = makeUnique("emcm")
    private val now = Instant.now().toEpochMilli()

    private val files = listOf(
        "asset-export.csv",
        "debug.log",
        "CSA_EM_transformed_0.csv"
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
    }

    private fun createCustomMetadata() {
        CustomMetadataDef.creator(cm1)
            .attributeDef(AttributeDef.of("dateSingle", AtlanCustomAttributePrimitiveType.DATE, false))
            .build()
            .create()
    }

    private fun createAssets() {
        val client = Atlan.getDefaultClient()
        val connection1 = Connection.findByName(c1, AtlanConnectorType.HIVE)[0]!!
        val connection2 = Connection.findByName(c2, AtlanConnectorType.ESSBASE)[0]!!
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

    @BeforeClass
    fun beforeClass() {
        createConnections()
        createCustomMetadata()
        createAssets()
        setup(
            EnrichmentMigratorCfg(
                sourceConnection = listOf(Connection.findByName(c1, AtlanConnectorType.HIVE)?.get(0)?.qualifiedName!!),
                targetConnection = listOf(Connection.findByName(c2, AtlanConnectorType.ESSBASE)?.get(0)?.qualifiedName!!),
                failOnErrors = false,
                cmLimitType = "INCLUDE",
                customMetadata = "$cm1::dateSingle",
            ),
        )
        EnrichmentMigrator.main(arrayOf(testDirectory))
    }

    @Test
    fun datesOnTarget() {
        val targetConnection = Connection.findByName(c2, AtlanConnectorType.ESSBASE)[0]!!
        val client = Atlan.getDefaultClient()
        Table.select()
            .where(Table.QUALIFIED_NAME.startsWith(targetConnection.qualifiedName))
            .includeOnResults(CustomMetadataField.of(client, cm1, "dateSingle"))
            .stream()
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
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }

    @AfterClass(alwaysRun = true)
    fun afterClass(context: ITestContext) {
        val client = Atlan.getDefaultClient()
        removeConnection(c1, AtlanConnectorType.HIVE)
        removeConnection(c2, AtlanConnectorType.ESSBASE)
        client.typeDefs.purge(client.customMetadataCache.getIdForName(cm1))
        teardown(context.failedTests.size() > 0)
    }
}
