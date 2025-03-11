/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryTerm
import com.atlan.pkg.PackageContext
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Test import of a very simple file containing categories with the same name at a given level,
 * but in different parent paths.
 */
class SameNameCategoriesTest : PackageTest("snc") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private lateinit var ctx: PackageContext<AssetImportCfg>
    private val glossaryName = makeUnique("g1")

    private val testFile = "same_name_categories.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFile() {
        // Prepare a copy of the file with unique name for glossary
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{GLOSSARY}", glossaryName)
                output.appendText("$revised\n")
            }
        }
    }

    override fun setup() {
        prepFile()
        val config =
            AssetImportCfg(
                glossariesFile = Paths.get(testDirectory, testFile).toString(),
                glossariesUpsertSemantic = "upsert",
                glossariesFailOnErrors = false,
                trackBatches = true,
            )
        runCustomPackage(
            config,
            Importer::main,
        )
        ctx = PackageContext(config, client, true)
    }

    override fun teardown() {
        removeGlossary(glossaryName)
        ctx.close()
    }

    @Test
    fun glossaryCreated() {
        val g1 = Glossary.findByName(client, glossaryName)
        assertEquals(glossaryName, g1.name)
    }

    @Test
    fun categoriesCreated() {
        val g = Glossary.findByName(client, glossaryName)!!
        val hierarchy = g.getHierarchy(client)
        assertEquals(1, hierarchy.rootCategories.size)
        val bfs = hierarchy.breadthFirst()
        assertEquals(5, bfs.size)
        val ordered = bfs.map { it.name }.toList()
        assertTrue(listOf("root", "c1", "c2", "same", "same") == ordered || listOf("root", "c2", "c1", "same", "same") == ordered)
    }

    @Test
    fun termsCreated() {
        val g = Glossary.findByName(client, glossaryName)!!
        val t1 = GlossaryTerm.findByNameFast(client, "t1", g.qualifiedName, listOf(GlossaryTerm.CATEGORIES))
        assertNotNull(t1)
        assertNotNull(t1.categories)
        assertEquals(1, t1.categories.size)
        val c1Guid = t1.categories.first().guid
        val t2 = GlossaryTerm.findByNameFast(client, "t2", g.qualifiedName, listOf(GlossaryTerm.CATEGORIES))
        assertNotNull(t2)
        assertNotNull(t2.categories)
        assertEquals(1, t2.categories.size)
        val c2Guid = t2.categories.first().guid
        assertNotEquals(c1Guid, c2Guid)
    }

    @Test
    fun categoriesProperlyCached() {
        ctx.categoryCache.forceRefresh() // Note: refresh to ensure that loading the cache from scratch it still resolves properly
        val cat1 = ctx.categoryCache.getByIdentity("root@c1@same@@@$glossaryName")
        assertNotNull(cat1)
        val cat2 = ctx.categoryCache.getByIdentity("root@c2@same@@@$glossaryName")
        assertNotNull(cat2)
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
