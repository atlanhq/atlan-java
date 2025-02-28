/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import org.testng.Assert.assertTrue
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * Test management of Atlan tag definitions.
 */
class GlossariesWithAssetsTest : PackageTest("gwa") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val g1 = makeUnique("g1")
    private val testFile = "gtc_with_assets.csv"

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
                    line.replace("{{GLOSSARY}}", g1)
                gtcOut.appendText("$revised,\n")
            }
        }
    }

    override fun setup() {
        prepFiles()
        runCustomPackage(
            AssetImportCfg(
                glossariesFile = Paths.get(testDirectory, testFile).toString(),
                glossariesUpsertSemantic = "upsert",
                glossariesFailOnErrors = true,
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        removeGlossary(g1)
    }

    private val gtcAttrs: List<AtlanField> =
        listOf(
            GlossaryTerm.NAME,
            GlossaryTerm.ASSIGNED_ENTITIES,
        )

    @Test
    fun glossaryStillCreated() {
        val glossary = Glossary.findByName(client, g1)
        assertNotNull(glossary)
    }

    @Test
    fun termCreatedWithoutEntities() {
        val term = GlossaryTerm.findByName(client, "Term1", g1, gtcAttrs)!!
        val entities = term.assignedEntities
        assertTrue(entities == null || entities.isEmpty())
    }

    @Test
    fun warningLogged() {
        logHasMessage(
            "WARN",
            """
            Found asset assignments in the glossary input file. Due to the order in which files are loaded, term <> asset assignments should only be provided in the assets file. Any found in the glossary file will be skipped.
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
