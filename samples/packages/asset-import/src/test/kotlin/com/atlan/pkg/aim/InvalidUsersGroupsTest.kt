/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Glossary
import com.atlan.pkg.PackageTest
import org.testng.Assert.assertTrue
import org.testng.ITestContext
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test import of a glossary that has invalid users and groups defined.
 */
class InvalidUsersGroupsTest : PackageTest() {

    private val glossary1 = makeUnique("iugg1")
    private val glossary2 = makeUnique("iugg2")

    private val testFile = "input.csv"

    private val files = listOf(
        testFile,
        "debug.log",
    )

    private fun prepFile() {
        // Prepare a copy of the file with unique names for glossaries and tags
        val input = Paths.get("src", "test", "resources", "invalid-users-groups.csv").toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised = line
                    .replace("{{GLOSSARY1}}", glossary1)
                    .replace("{{GLOSSARY2}}", glossary2)
                output.appendText("$revised\n")
            }
        }
    }

    @BeforeClass
    fun beforeClass() {
        prepFile()
        setup(
            AssetImportCfg(
                assetsFile = null,
                assetsUpsertSemantic = null,
                assetsAttrToOverwrite = null,
                assetsFailOnErrors = true,
                glossariesFile = Paths.get(testDirectory, testFile).toString(),
                glossariesUpsertSemantic = "upsert",
                glossariesAttrToOverwrite = listOf(),
                glossariesFailOnErrors = false,
            ),
        )
        Importer.main(arrayOf())
    }

    @Test
    fun glossaryCreatedWithoutOwnerUser() {
        val g1 = Glossary.findByName(glossary1)
        assertNotNull(g1)
        assertEquals(glossary1, g1.name)
        assertTrue(g1.ownerUsers.isNullOrEmpty())
        assertTrue(g1.ownerGroups.isNullOrEmpty())
    }

    @Test
    fun glossaryCreatedWithoutOwnerGroup() {
        val g2 = Glossary.findByName(glossary2)
        assertNotNull(g2)
        assertEquals(glossary2, g2.name)
        assertTrue(g2.ownerUsers.isNullOrEmpty())
        assertTrue(g2.ownerGroups.isNullOrEmpty())
    }

    @Test
    fun warningsInLog() {
        assertTrue(logHasMessage("WARN", "com.atlan.pkg.aim.GlossaryImporter - Unable to decode value from field -- skipping ownerGroups: invalidGroup"))
        assertTrue(logHasMessage("WARN", "com.atlan.pkg.aim.GlossaryImporter - Unable to decode value from field -- skipping ownerUsers: invalidUser"))
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }

    @AfterClass(alwaysRun = true)
    fun afterClass(context: ITestContext) {
        Glossary.purge(Glossary.findByName(glossary1).guid)
        Glossary.purge(Glossary.findByName(glossary2).guid)
        teardown(context.failedTests.size() > 0)
    }
}
