/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Glossary
import com.atlan.pkg.PackageTest
import org.testng.ITestContext
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test import of a very simple file containing categories with the same name at a given level,
 * but in different parent paths.
 */
class SameNameCategoriesTest : PackageTest() {
    private val glossaryName = makeUnique("sncg1")

    private val testFile = "same_name_categories.csv"

    private val files = listOf(
        testFile,
        "debug.log",
    )

    private fun prepFile() {
        // Prepare a copy of the file with unique name for glossary
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised = line
                    .replace("{GLOSSARY}", glossaryName)
                output.appendText("$revised\n")
            }
        }
    }

    @BeforeClass
    fun beforeClass() {
        prepFile()
        setup(
            AssetImportCfg(
                glossariesFile = Paths.get(testDirectory, testFile).toString(),
                glossariesUpsertSemantic = "upsert",
                glossariesFailOnErrors = false,
                trackBatches = true,
            ),
        )
        Importer.main(arrayOf(testDirectory))
    }

    @Test
    fun glossaryCreated() {
        val g1 = Glossary.findByName(glossaryName)
        assertEquals(glossaryName, g1.name)
    }

    @Test
    fun categoriesCreated() {
        val g = Glossary.findByName(glossaryName)!!
        val hierarchy = g.hierarchy
        assertEquals(1, hierarchy.rootCategories.size)
        val bfs = hierarchy.breadthFirst()
        assertEquals(5, bfs.size)
        val ordered = bfs.map { it.name }.toList()
        assertTrue(listOf("root", "c1", "c2", "same", "same") == ordered || listOf("root", "c2", "c1", "same", "same") == ordered)
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
        removeGlossary(glossaryName)
        teardown(context.failedTests.size() > 0)
    }
}
