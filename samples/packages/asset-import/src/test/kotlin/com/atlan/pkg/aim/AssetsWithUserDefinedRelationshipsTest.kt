/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanDeleteType
import com.atlan.model.fields.AtlanField
import com.atlan.model.relations.UserDefRelationship
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import org.testng.Assert.assertTrue
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test management of user-defined relationships amongst arbitrary assets (not terms).
 */
class AssetsWithUserDefinedRelationshipsTest : PackageTest("awudr") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val db1 = makeUnique("db1")
    private val db2 = makeUnique("db2")
    private val testFile = "assets_with_udrs.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFiles() {
        // Prepare copies of the files with unique names for objects
        val gtcIn = Paths.get("src", "test", "resources", testFile).toFile()
        val gtcOut = Paths.get(testDirectory, testFile).toFile()
        gtcIn.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{DBNAME1}", db1)
                        .replace("{DBNAME2}", db2)
                gtcOut.appendText("$revised,\n")
            }
        }
    }

    override fun setup() {
        prepFiles()
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = true,
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        val connection = Connection.findByName(client, "production", AtlanConnectorType.SNOWFLAKE)[0]!!
        val dbs =
            Database
                .select(client)
                .where(Database.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(Database.NAME.`in`(listOf(db1, db2)))
                .stream()
                .map { it.guid }
                .toList()
        client.assets.delete(dbs, AtlanDeleteType.PURGE)
    }

    private val dbAttrs: List<AtlanField> =
        listOf(
            Database.NAME,
            Database.USER_DEF_RELATIONSHIP_TOS,
            Database.USER_DEF_RELATIONSHIP_FROMS,
        )

    @Test
    fun db1Created() {
        val db = databasesCreated(db1)
        val related = db.userDefRelationshipFroms
        assertNotNull(related)
        assertEquals(1, related.size)
        val relnAttrs = related.first().relationshipAttributes
        assertTrue(relnAttrs is UserDefRelationship)
        val udr = relnAttrs as UserDefRelationship
        assertEquals("Is copied to", udr.toTypeLabel)
        assertEquals("Is copied from", udr.fromTypeLabel)
    }

    @Test
    fun db2Created() {
        val db = databasesCreated(db2)
        val related = db.userDefRelationshipTos
        assertNotNull(related)
        assertEquals(1, related.size)
        val relnAttrs = related.first().relationshipAttributes
        assertTrue(relnAttrs is UserDefRelationship)
        val udr = relnAttrs as UserDefRelationship
        assertEquals("Is copied to", udr.toTypeLabel)
        assertEquals("Is copied from", udr.fromTypeLabel)
    }

    private fun databasesCreated(name: String): Database {
        val connection = Connection.findByName(client, "production", AtlanConnectorType.SNOWFLAKE)[0]!!
        val db =
            Database
                .select(client)
                .where(Database.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(Database.NAME.eq(name))
                .includesOnResults(dbAttrs)
                .stream()
                .map { it as Database }
                .toList()
        assertEquals(1, db.size)
        assertEquals(name, db[0].name)
        return db[0]
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
