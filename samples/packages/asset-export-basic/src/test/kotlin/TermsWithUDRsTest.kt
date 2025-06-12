/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.relations.Reference
import com.atlan.model.relations.UserDefRelationship
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import kotlin.test.Test

/**
 * Test export of only glossaries, no assets.
 */
class TermsWithUDRsTest : PackageTest("twudr") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val g1 = makeUnique("g1")
    private lateinit var glossaryQN: String
    private lateinit var term1QN: String
    private lateinit var term2QN: String

    private val files =
        listOf(
            "glossary-export.csv",
            "debug.log",
            "asset-export.csv",
        )

    private fun prep() {
        val gloss = Glossary.creator(g1).build()
        val rGloss = gloss.save(client)
        val glossary = rGloss.getResult(gloss)
        val term1 = GlossaryTerm.creator("term1", glossary.guid).build()
        val term2 = GlossaryTerm.creator("term2", glossary.guid)
            .userDefRelationshipTo(
                UserDefRelationship
                    .builder()
                    .toTypeLabel("relates to")
                    .fromTypeLabel("related from")
                    .userDefRelationshipTo(term1.trimToReference(), Reference.SaveSemantic.REPLACE)
            )
            .build()
        val response = client.assets.save(listOf(term1, term2))
        glossaryQN = glossary.qualifiedName
        term1QN = response.getResult(term1).qualifiedName
        term2QN = response.getResult(term2).qualifiedName
    }

    override fun setup() {
        prep()
        runCustomPackage(
            AssetExportBasicCfg(
                exportScope = "GLOSSARIES_ONLY",
                qnPrefix = "",
                includeGlossaries = false,
            ),
            Exporter::main,
        )
    }

    override fun teardown() {
        removeGlossary(g1)
    }

    @Test
    fun containsUserDefinedRelationshipDetails() {
        fileHasLineStartingWith(
            "glossary-export.csv",
            """
                "$term1QN","AtlasGlossaryTerm","term1","$g1",,,,,,,,,,,,,,,,,,,,,,,,,"term2@@@$g1 {{fromTypeLabel=Related from|||toTypeLabel=Relates to}}"
            """.trimIndent()
        )
        fileHasLineStartingWith(
            "glossary-export.csv",
            """
                "$term2QN","AtlasGlossaryTerm","term2","$g1",,,,,,,,,,,,,,,,,,,,,,,,"term1@@@$g1 {{fromTypeLabel=Related from|||toTypeLabel=Relates to}}"
            """.trimIndent()
        )
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files.subList(0, files.size - 1))
        validateFileExistsButEmpty(files.subList(2, files.size))
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
