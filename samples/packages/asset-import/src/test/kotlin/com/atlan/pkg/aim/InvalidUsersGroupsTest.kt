/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Glossary
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import org.testng.Assert.assertTrue
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test import of a glossary that has invalid users and groups defined.
 */
class InvalidUsersGroupsTest : PackageTest("iug") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val glossary1 = makeUnique("g1")
    private val glossary2 = makeUnique("g2")

    private val testFile = "input.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFile() {
        // Prepare a copy of the file with unique names for glossaries and tags
        val input = Paths.get("src", "test", "resources", "invalid-users-groups.csv").toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{GLOSSARY1}}", glossary1)
                        .replace("{{GLOSSARY2}}", glossary2)
                output.appendText("$revised\n")
            }
        }
    }

    override fun setup() {
        prepFile()
        runCustomPackage(
            AssetImportCfg(
                glossariesFile = Paths.get(testDirectory, testFile).toString(),
                glossariesUpsertSemantic = "upsert",
                glossariesAttrToOverwrite = listOf(),
                glossariesConfig = "advanced",
                glossariesFailOnErrors = false,
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        Glossary.purge(client, Glossary.findByName(client, glossary1).guid)
        Glossary.purge(client, Glossary.findByName(client, glossary2).guid)
    }

    @Test
    fun glossaryCreatedWithoutOwnerUser() {
        val g1 = Glossary.findByName(client, glossary1)
        assertNotNull(g1)
        assertEquals(glossary1, g1.name)
        assertTrue(g1.ownerUsers.isNullOrEmpty())
        assertTrue(g1.ownerGroups.isNullOrEmpty())
    }

    @Test
    fun glossaryCreatedWithoutOwnerGroup() {
        val g2 = Glossary.findByName(client, glossary2)
        assertNotNull(g2)
        assertEquals(glossary2, g2.name)
        assertTrue(g2.ownerUsers.isNullOrEmpty())
        assertTrue(g2.ownerGroups.isNullOrEmpty())
    }

    @Test
    fun warningsInLog() {
        assertTrue(logHasMessage("WARN", "com.atlan.pkg.aim.Importer trace_id:  span_id:  trace_flags:  - Unable to decode value from field -- skipping ownerGroups: invalidGroup"))
        assertTrue(logHasMessage("WARN", "com.atlan.pkg.aim.Importer trace_id:  span_id:  trace_flags:  - Unable to decode value from field -- skipping ownerUsers: invalidUser"))
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }
}
