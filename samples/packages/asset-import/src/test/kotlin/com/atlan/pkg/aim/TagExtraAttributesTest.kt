/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Connection
import com.atlan.model.assets.SourceTag
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanIcon
import com.atlan.model.enums.AtlanTagColor
import com.atlan.model.enums.TagIconType
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test import of Atlan tags with extra attributes via CSV columns.
 * This tests the ability to set arbitrary SourceTag attributes (like tagCustomConfiguration)
 * through CSV columns that are not explicitly handled by the importer.
 */
class TagExtraAttributesTest : PackageTest("tea") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val c1 = makeUnique("c1")
    private val c1Type = AtlanConnectorType.BIGID
    private val t1 = makeUnique("t1")

    private lateinit var connection: Connection

    private val tagsFile = "tags_with_extra_attrs.csv"

    private val files =
        listOf(
            tagsFile,
            "debug.log",
        )

    private fun prepFile() {
        // Prepare a copy of the file with unique names for objects
        val tagsIn = Paths.get("src", "test", "resources", tagsFile).toFile()
        val tagsOut = Paths.get(testDirectory, tagsFile).toFile()
        tagsIn.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{TAG1}}", t1)
                        .replace("{{CONNECTION}}", c1)
                        .replace("{{CTYPE}}", c1Type.value)
                tagsOut.appendText("$revised\n")
            }
        }
    }

    private fun createConnection(): Connection {
        val conn = Connection.creator(client, c1, c1Type).build()
        val response = conn.save(client).block()
        return response.getResult(conn)
    }

    override fun setup() {
        connection = createConnection()
        prepFile()
        runCustomPackage(
            AssetImportCfg(
                tagsFile = Paths.get(testDirectory, tagsFile).toString(),
                tagsFailOnErrors = true,
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        removeConnection(c1, c1Type)
        removeTag(t1)
    }

    @Test
    fun tagExists() {
        val tag = client.atlanTagCache.getByName(t1)
        assertNotNull(tag)
        assertEquals(t1, tag.displayName)
        assertEquals("Icon tag with extra config", tag.description)
        assertEquals(AtlanTagColor.GREEN.value, tag.options.color)
        assertEquals(AtlanIcon.RECYCLE, tag.options.iconName)
        assertEquals(TagIconType.ICON, tag.options.iconType)
    }

    @Test
    fun sourceTagHasExtraAttribute() {
        val request =
            SourceTag
                .select(client)
                .where(SourceTag.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(SourceTag.NAME.eq(t1))
                .includeOnResults(SourceTag.TAG_CUSTOM_CONFIGURATION)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val sourceTag = found[0] as SourceTag
        assertNotNull(sourceTag.tagCustomConfiguration)
        assertEquals("{\"customKey\": \"customValue\"}", sourceTag.tagCustomConfiguration)
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
