/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import org.testng.Assert.assertTrue
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Test management of Atlan tag definitions.
 */
class TagManagementTest : PackageTest("tm") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val c1 = makeUnique("c1")
    private val c1Type = AtlanConnectorType.BIGID
    private val t1 = makeUnique("t1")
    private val t2 = makeUnique("t2")

    private val assetsFile = "assets.csv"
    private val tagsFile = "tags.csv"

    private val files =
        listOf(
            assetsFile,
            tagsFile,
            "debug.log",
        )

    private fun prepFiles(connectionQN: String) {
        // Prepare copies of the files with unique names for objects
        val tagsIn = Paths.get("src", "test", "resources", tagsFile).toFile()
        val tagsOut = Paths.get(testDirectory, tagsFile).toFile()
        tagsIn.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{TAG1}}", t1)
                        .replace("{{TAG2}}", t2)
                        .replace("{{CONNECTION}}", c1)
                        .replace("{{CTYPE}}", c1Type.value)
                tagsOut.appendText("$revised\n")
            }
        }
        val assetsIn = Paths.get("src", "test", "resources", assetsFile).toFile()
        val assetsOut = Paths.get(testDirectory, assetsFile).toFile()
        assetsIn.useLines { lines ->
            lines.forEachIndexed { idx, line ->
                if (idx == 0) {
                    assetsOut.appendText("$line,atlanTags\n")
                } else {
                    val revised =
                        line
                            .replace("{{CONNECTION}}", connectionQN)
                            .replace("{{CTYPE}}", c1Type.value)
                    if (revised.contains("Table")) {
                        assetsOut.appendText("$revised,$t2\n")
                    } else if (revised.contains("View")) {
                        assetsOut.appendText("$revised,$t1 {{${c1Type.value}/$c1@@$t1??=Y}}\n")
                    } else {
                        assetsOut.appendText("$revised,\n")
                    }
                }
            }
        }
    }

    private fun createConnection(): Connection {
        val conn = Connection.creator(client, c1, c1Type).build()
        val response = conn.save(client).block()
        return response.getResult(conn)
    }

    override fun setup() {
        val connection = createConnection()
        prepFiles(connection.qualifiedName)
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, assetsFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = true,
                tagsFile = Paths.get(testDirectory, tagsFile).toString(),
                tagsFailOnErrors = true,
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        removeConnection(c1, c1Type)
        removeTag(t1)
        removeTag(t2)
    }

    private val tableAttrs: List<AtlanField> =
        listOf(
            Table.NAME,
            Table.ATLAN_TAGS,
        )

    @Test
    fun tagOnTable() {
        val c1 = Connection.findByName(client, c1, c1Type)[0]!!
        val request =
            Table
                .select(client)
                .where(Table.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(tableAttrs)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val tbl = found[0] as Table
        assertNotNull(tbl.atlanTags)
        assertEquals(1, tbl.atlanTags.size)
        assertEquals(t2, tbl.atlanTags.first().typeName)
    }

    @Test
    fun tagOnView() {
        val c1 = Connection.findByName(client, c1, c1Type)[0]!!
        val request =
            View
                .select(client)
                .where(View.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(tableAttrs)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val view = found[0] as View
        assertNotNull(view.atlanTags)
        assertEquals(1, view.atlanTags.size)
        val tag = view.atlanTags.first()
        assertEquals(t1, tag.typeName)
        assertNotNull(tag.sourceTagAttachments)
        assertEquals(1, tag.sourceTagAttachments.size)
        val sta = tag.sourceTagAttachments.first()
        assertNotNull(sta)
        assertEquals(t1, sta.sourceTagName)
        assertEquals(c1Type.value, sta.sourceTagConnectorName)
        assertEquals("${c1.qualifiedName}/$t1", sta.sourceTagQualifiedName)
        assertNotNull(sta.sourceTagValues)
        assertEquals(1, sta.sourceTagValues.size)
        assertEquals("Y", sta.sourceTagValues.first().tagAttachmentValue)
        assertNull(sta.sourceTagValues.first().tagAttachmentKey)
    }

    @Test
    fun tableFindableByTag() {
        val c1 = Connection.findByName(client, c1, c1Type)[0]!!
        val request =
            Table
                .select(client)
                .where(Table.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .tagged(listOf(t2))
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        assertTrue(found.first() is Table)
    }

    @Test
    fun viewFindableByTagValue() {
        val c1 = Connection.findByName(client, c1, c1Type)[0]!!
        val request =
            View
                .select(client)
                .where(View.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .taggedWithValue(t1, "Y", true)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        assertTrue(found.first() is View)
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
