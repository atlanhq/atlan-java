/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag
import LakeFormationTagSyncCfg
import com.atlan.Atlan
import com.atlan.model.assets.Column
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanCustomAttributePrimitiveType
import com.atlan.model.typedefs.AttributeDef
import com.atlan.model.typedefs.CustomMetadataDef
import com.atlan.model.typedefs.EnumDef
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.util.AssetBatch
import org.testng.ITestContext
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.io.File
import kotlin.test.assertEquals

private const val DATABASE_NAME = "db_test"

private const val attr1 = "Security Classification"

private const val attr2 = "Privacy Sensitivity"

private const val attr3 = "Data Load Method"

private const val CONNECTION_MAP_JSON = "connection_map.json"

private const val PREFIX = "stuff"

private const val METADATA_MAP_JSON = "metadata_map.json"

class LakeTagSynchronizerTest : PackageTest() {
    private val c1 = makeUnique("lftagdb")
    private var connectionQualifiedName = ""
    private var tableGuid = ""
    private var columnGuid = ""
    private val cm1 = makeUnique("lftcm")
    private val enum1 = makeUnique("lfenum")

    private fun createConnections() {
        Connection.creator(c1, AtlanConnectorType.REDSHIFT)
            .build()
            .save()
            .block()
    }

    private fun createAssets() {
        val client = Atlan.getDefaultClient()
        val connection1 = Connection.findByName(c1, AtlanConnectorType.REDSHIFT)[0]!!
        connectionQualifiedName = connection1.qualifiedName
        val batch = AssetBatch(client, 20)
        val db = Database.creator(DATABASE_NAME, connection1.qualifiedName).build()
        batch.add(db)
        val sch = Schema.creator("sch", db).build()
        batch.add(sch)
        val tbl = Table.creator("tbl1", sch).build()
        batch.add(tbl)
        val column = Column.creator("col1", tbl, 1).build()
        batch.add(column)
        val response = batch.flush()
        response.getCreatedAssets(Table::class.java).forEach { table ->
            this.tableGuid = table.guid
        }
        response.getCreatedAssets(Column::class.java).forEach { column ->
            this.columnGuid = column.guid
        }
    }

    private fun createConnectionMap() {
        File("$testDirectory/$CONNECTION_MAP_JSON").bufferedWriter().use { out ->
            out.write("{\"dev\":\"$connectionQualifiedName/$DATABASE_NAME\"}")
        }
    }

    private fun uploadFiles() {
        Utils.uploadOutputFile("$testDirectory/$CONNECTION_MAP_JSON", PREFIX, CONNECTION_MAP_JSON)
        File("$testDirectory/$CONNECTION_MAP_JSON").delete()
        Utils.uploadOutputFile("$testDirectory/$METADATA_MAP_JSON", PREFIX, METADATA_MAP_JSON)
        File("$testDirectory/$METADATA_MAP_JSON").delete()
        Utils.uploadOutputFile("./src/test/resources/sample.json", PREFIX, "sample.json")
    }

    private fun createEnums() {
        val enumDef = EnumDef.creator(
            enum1,
            listOf("public", "non-pi", "fullhistory"),
        )
            .build()
        enumDef.create()
    }
    private fun createCustomMetadata() {
        createEnums()
        CustomMetadataDef.creator(cm1)
            .attributeDef(AttributeDef.of(attr1, AtlanCustomAttributePrimitiveType.OPTIONS, enum1, false))
            .attributeDef(AttributeDef.of(attr2, AtlanCustomAttributePrimitiveType.OPTIONS, enum1, false))
            .attributeDef(AttributeDef.of(attr3, AtlanCustomAttributePrimitiveType.OPTIONS, enum1, false))
            .build()
            .create()
    }

    private fun createMetadataMap() {
        File("$testDirectory/$METADATA_MAP_JSON").printWriter().use { out ->
            out.println("{")
            out.println("  \"security_classification\": \"$cm1::$attr1\",")
            out.println("  \"privacy_sensitivity\": \"$cm1::$attr2\",")
            out.println("  \"data_load_method\": \"$cm1::$attr3\"")
            out.println("}")
        }
    }

    @BeforeClass
    fun beforeClass() {
        createConnections()
        createAssets()
        createConnectionMap()
        createCustomMetadata()
        createMetadataMap()
        uploadFiles()
        setup(
            LakeFormationTagSyncCfg(
                "CLOUD",
                "s3",
                PREFIX,
                null,
                null,
                null,
            ),
        )
        LakeTagSynchronizer.main(arrayOf(testDirectory))
        Thread.sleep(15000)
    }

    @Test
    fun validateTableTagged() {
        val table = Table.get(tableGuid)
        val attribute1 = table.getCustomMetadata(cm1, attr1)
        assertEquals("public", attribute1)
        val attribute3 = table.getCustomMetadata(cm1, attr3)
        assertEquals("fullhistory", attribute3)
    }

    @Test
    fun validateFilesCreated() {
        validateFilesExist(listOf("debug.log", "$CONNECTION_MAP_JSON", "$METADATA_MAP_JSON", "sample.json", "sample.csv"))
    }

    @AfterClass(alwaysRun = true)
    fun afterClass(context: ITestContext) {
        val client = Atlan.getDefaultClient()
        removeConnection(c1, AtlanConnectorType.REDSHIFT)
        client.typeDefs.purge(client.customMetadataCache.getIdForName(cm1))
        EnumDef.purge(enum1)
        teardown((context.passedTests.size() > 0) && (context.failedTests.size() < 1))
    }
}
