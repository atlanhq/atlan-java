/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.assets.IGlossaryCategory
import com.atlan.model.assets.IGlossaryTerm
import com.atlan.model.assets.ILink
import com.atlan.model.assets.Link
import com.atlan.model.assets.Readme
import com.atlan.model.core.AtlanTag
import com.atlan.model.enums.AtlanAnnouncementType
import com.atlan.model.enums.AtlanIcon
import com.atlan.model.enums.AtlanTagColor
import com.atlan.model.enums.CertificateStatus
import com.atlan.model.fields.AtlanField
import com.atlan.model.typedefs.AtlanTagDef
import com.atlan.net.RequestOptions
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.serde.csv.ThreadSafeWriter
import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRecord
import de.siegmar.fastcsv.writer.CsvWriter
import de.siegmar.fastcsv.writer.LineDelimiter
import de.siegmar.fastcsv.writer.QuoteStrategies
import org.testng.Assert.assertNull
import org.testng.Assert.assertTrue
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test import of a glossary and its inter-related contents.
 */
class ImportGlossariesTest : PackageTest("ig") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val glossary1 = makeUnique("g1")
    private val glossary2 = makeUnique("g2")
    private val tag1 = makeUnique("t1")
    private val tag2 = makeUnique("t2")

    private val testFile = "input.csv"
    private val revisedFile = "with_desc.csv"

    private val files =
        listOf(
            testFile,
            revisedFile,
            "debug.log",
        )

    private fun prepFile() {
        // Prepare a copy of the file with unique names for glossaries and tags
        val input = Paths.get("src", "test", "resources", "glossary.csv").toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{GLOSSARY1}}", glossary1)
                        .replace("{{GLOSSARY2}}", glossary2)
                        .replace("{{TAG1}}", tag1)
                        .replace("{{TAG2}}", tag2)
                output.appendText("$revised\n")
            }
        }
    }

    private fun modifyFile() {
        val input = Paths.get(testDirectory, testFile)
        val output = Paths.get(testDirectory, revisedFile)
        val builder =
            CsvReader
                .builder()
                .fieldSeparator(',')
                .quoteCharacter('"')
                .skipEmptyLines(true)
                .allowMissingFields(false)
                .allowExtraFields(false)
        val reader = builder.ofCsvRecord(input)
        val header: List<String> = CSVXformer.getHeader(input.toString(), ',')
        val tagsIdx = header.indexOf("atlanTags")
        val descriptionIdx = header.indexOf(Asset.DESCRIPTION.atlanFieldName)
        CsvWriter
            .builder()
            .fieldSeparator(',')
            .quoteCharacter('"')
            .quoteStrategy(QuoteStrategies.NON_EMPTY)
            .lineDelimiter(LineDelimiter.PLATFORM)
            .build(ThreadSafeWriter(output.toString()))
            .use { out ->
                var count = 0
                reader.stream().forEach { r: CsvRecord ->
                    val fields = r.fields.toMutableList()
                    fields.removeAt(tagsIdx)
                    if (count > 0) {
                        fields[descriptionIdx] = "Now with a description."
                    }
                    out.writeRecord(fields)
                    count++
                }
            }
    }

    private fun createTags() {
        val maxNetworkRetries = 30
        val t1 = AtlanTagDef.creator(tag1, AtlanIcon.AIRPLANE, AtlanTagColor.GREEN).build()
        val t2 = AtlanTagDef.creator(tag2, AtlanIcon.ROBOT, AtlanTagColor.RED).build()
        client.typeDefs.create(
            listOf(t1, t2),
            RequestOptions.from(client).maxNetworkRetries(maxNetworkRetries).build(),
        )
    }

    private val glossaryAttrs: List<AtlanField> =
        listOf(
            Glossary.NAME,
            Glossary.USER_DESCRIPTION,
            Glossary.OWNER_USERS,
            Glossary.OWNER_GROUPS,
            Glossary.CERTIFICATE_STATUS,
            Glossary.CERTIFICATE_STATUS_MESSAGE,
        )

    private val categoryAttrs: List<AtlanField> =
        listOf(
            GlossaryCategory.NAME,
            GlossaryCategory.ANCHOR,
            GlossaryCategory.PARENT_CATEGORY,
            GlossaryCategory.USER_DESCRIPTION,
            GlossaryCategory.OWNER_USERS,
            GlossaryCategory.OWNER_GROUPS,
        )

    private val termAttrs: List<AtlanField> =
        listOf(
            GlossaryTerm.NAME,
            GlossaryTerm.ANCHOR,
            GlossaryTerm.CATEGORIES,
            GlossaryTerm.DESCRIPTION,
            GlossaryTerm.USER_DESCRIPTION,
            GlossaryTerm.OWNER_USERS,
            GlossaryTerm.OWNER_GROUPS,
            GlossaryTerm.CERTIFICATE_STATUS,
            GlossaryTerm.CERTIFICATE_STATUS_MESSAGE,
            GlossaryTerm.ANNOUNCEMENT_TYPE,
            GlossaryTerm.ANNOUNCEMENT_TITLE,
            GlossaryTerm.ANNOUNCEMENT_MESSAGE,
            GlossaryTerm.ATLAN_TAGS,
            GlossaryTerm.LINKS,
            GlossaryTerm.README,
            GlossaryTerm.SEE_ALSO,
            GlossaryTerm.PREFERRED_TERMS,
            GlossaryTerm.SYNONYMS,
            GlossaryTerm.ANTONYMS,
            GlossaryTerm.TRANSLATED_TERMS,
            GlossaryTerm.VALID_VALUES_FOR,
            GlossaryTerm.CLASSIFIES,
        )

    override fun setup() {
        prepFile()
        createTags()
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
        removeGlossary(glossary1)
        removeGlossary(glossary2)
        removeTag(tag1)
        removeTag(tag2)
    }

    @Test(groups = ["aim.ig.create"])
    fun glossary1Created() {
        val g1 = Glossary.findByName(client, glossary1, glossaryAttrs)
        assertNotNull(g1)
        assertEquals(glossary1, g1.name)
        assertEquals("Test glossary for asset import package.", g1.userDescription)
        assertEquals(setOf("chris"), g1.ownerUsers)
        assertEquals(setOf("admins"), g1.ownerGroups)
        assertEquals(CertificateStatus.VERIFIED, g1.certificateStatus)
    }

    @Test(groups = ["aim.ig.create"])
    fun glossary2Created() {
        val g2 = Glossary.findByName(client, glossary2, glossaryAttrs)
        assertNotNull(g2)
        assertEquals(glossary2, g2.name)
        assertEquals("Test glossary for asset import package.", g2.userDescription)
        assertTrue(g2.ownerUsers.isNullOrEmpty())
        assertEquals(setOf("admins"), g2.ownerGroups)
        assertEquals(CertificateStatus.DRAFT, g2.certificateStatus)
        assertEquals("With a message!", g2.certificateStatusMessage)
    }

    @Test(groups = ["aim.ig.create"])
    fun categoriesCreatedG1() {
        val g1 = Glossary.findByName(client, glossary1)!!
        val request =
            GlossaryCategory
                .select(client)
                .where(GlossaryCategory.ANCHOR.eq(g1.qualifiedName))
                .includesOnResults(categoryAttrs)
                .includeOnRelations(Glossary.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 4)
        val chris = client.userCache.getByEmail("chris@atlan.com").username
        val g1categories = response.assets
        assertEquals(4, g1categories.size)
        g1categories.forEach { category ->
            category as GlossaryCategory
            assertEquals(glossary1, category.anchor?.name)
            when (category.name) {
                "Cat1" -> {
                    assertEquals("Test category 1 for asset import package.", category.userDescription)
                    assertTrue(category.ownerUsers.isNullOrEmpty())
                    assertTrue(category.ownerGroups.isNullOrEmpty())
                }

                "Cat2" -> {
                    assertEquals("Test category 2 for asset import package.", category.userDescription)
                    assertEquals(setOf("chris", chris), category.ownerUsers)
                    assertTrue(category.ownerGroups.isNullOrEmpty())
                }

                "Cat1.a" -> {
                    assertEquals("Test category 1.a for asset import package (unordered).", category.userDescription)
                    assertEquals(setOf("chris"), category.ownerUsers)
                    assertTrue(category.ownerGroups.isNullOrEmpty())
                }

                "Cat1.a.i" -> {
                    assertEquals("Test category 1.a.i for asset import package (unordered).", category.userDescription)
                    assertTrue(category.ownerUsers.isNullOrEmpty())
                    assertTrue(category.ownerGroups.isNullOrEmpty())
                }
            }
        }
    }

    @Test(groups = ["aim.ig.create"])
    fun categoriesCreatedG2() {
        val g2 = Glossary.findByName(client, glossary2)!!
        val request =
            GlossaryCategory
                .select(client)
                .where(GlossaryCategory.ANCHOR.eq(g2.qualifiedName))
                .includesOnResults(categoryAttrs)
                .includeOnRelations(Glossary.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 3)
        val g2categories = response.assets
        assertEquals(3, g2categories.size)
        g2categories.forEach { category ->
            category as GlossaryCategory
            assertEquals(glossary2, category.anchor?.name)
            when (category.name) {
                "Cat3" -> {
                    assertEquals("Test category 3 for asset import package.", category.userDescription)
                    assertTrue(category.ownerUsers.isNullOrEmpty())
                    assertEquals(setOf("admins"), category.ownerGroups)
                }

                "Cat4" -> {
                    assertEquals("Test category 4 for asset import package.", category.userDescription)
                    assertTrue(category.ownerUsers.isNullOrEmpty())
                    assertTrue(category.ownerGroups.isNullOrEmpty())
                }

                "Cat4.a" -> {
                    assertEquals("Test category 4.a for asset import package (unordered).", category.userDescription)
                    assertTrue(category.ownerUsers.isNullOrEmpty())
                    assertTrue(category.ownerGroups.isNullOrEmpty())
                }
            }
        }
    }

    @Test(groups = ["aim.ig.create"])
    fun termsCreatedG1() {
        val g1 = Glossary.findByName(client, glossary1)!!
        val request =
            GlossaryTerm
                .select(client)
                .where(GlossaryTerm.ANCHOR.eq(g1.qualifiedName))
                .includesOnResults(termAttrs)
                .includeOnRelations(Glossary.NAME)
                .includeOnRelations(Readme.DESCRIPTION)
                .includeOnRelations(Link.LINK)
                .toRequest()
        val response = retrySearchUntil(request, 4)
        val g1terms = response.assets
        assertEquals(4, g1terms.size)
        g1terms.forEach { term ->
            term as GlossaryTerm
            assertEquals(glossary1, term.anchor?.name)
            when (term.name) {
                "Term1" -> validateTerm1(term)
                "TermA" -> validateTermA(term)
                "TermC" -> validateTermC(term)
                "Term3" -> validateTerm3(term)
            }
        }
    }

    @Test(groups = ["aim.ig.create"])
    fun termsCreatedG2() {
        val g2 = Glossary.findByName(client, glossary2)!!
        val request =
            GlossaryTerm
                .select(client)
                .where(GlossaryTerm.ANCHOR.eq(g2.qualifiedName))
                .includesOnResults(termAttrs)
                .includeOnRelations(Glossary.NAME)
                .includeOnRelations(Readme.DESCRIPTION)
                .includeOnRelations(Link.LINK)
                .toRequest()
        val response = retrySearchUntil(request, 3)
        val g2terms = response.assets
        assertEquals(3, g2terms.size)
        g2terms.forEach { term ->
            term as GlossaryTerm
            assertEquals(glossary2, term.anchor?.name)
            when (term.name) {
                "Term2" -> {
                    assertTrue(term.categories.isNullOrEmpty())
                    assertEquals("Test term 2 for asset import package (no categories).", term.userDescription)
                    assertTrue(term.ownerUsers.isNullOrEmpty())
                    assertEquals(setOf("admins"), term.ownerGroups)
                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
                    assertTrue(term.certificateStatusMessage.isNullOrEmpty())
                    assertNull(term.announcementType)
                    assertNull(term.announcementTitle)
                    assertNull(term.announcementMessage)
                    assertTrue(term.atlanTags.isNullOrEmpty())
                    assertTrue(term.links.isNullOrEmpty())
                    assertNotNull(term.readme)
                    assertEquals("<h2>This is term2.</h2>", term.readme.description)
                    assertEquals(1, term.seeAlso.size)
                    assertEquals(setOf("Term3"), term.seeAlso.map(IGlossaryTerm::getName).toSet())
                    assertEquals(1, term.preferredTerms.size)
                    assertEquals(setOf("TermB"), term.preferredTerms.map(IGlossaryTerm::getName).toSet())
                    assertEquals(1, term.synonyms.size)
                    assertEquals(setOf("TermC"), term.synonyms.map(IGlossaryTerm::getName).toSet())
                    assertTrue(term.antonyms.isNullOrEmpty())
                    assertTrue(term.translatedTerms.isNullOrEmpty())
                    assertTrue(term.validValuesFor.isNullOrEmpty())
                    assertTrue(term.classifies.isNullOrEmpty())
                }

                "TermB" -> {
                    assertEquals(1, term.categories.size)
                    assertEquals(setOf("Cat3"), term.categories.map(IGlossaryCategory::getName).toSet())
                    assertEquals("Test term B for asset import package (single category).", term.userDescription)
                    assertTrue(term.ownerUsers.isNullOrEmpty())
                    assertTrue(term.ownerGroups.isNullOrEmpty())
                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
                    assertEquals("With a message!", term.certificateStatusMessage)
                    assertNull(term.announcementType)
                    assertNull(term.announcementTitle)
                    assertNull(term.announcementMessage)
                    assertTrue(term.atlanTags.isNullOrEmpty())
                    assertEquals(1, term.links.size)
                    assertEquals(setOf("Example"), term.links.map(ILink::getName).toSet())
                    assertEquals(setOf("https://www.example.com"), term.links.map(ILink::getLink).toSet())
                    assertNull(term.readme)
                    assertTrue(term.seeAlso.isNullOrEmpty())
                    assertTrue(term.preferredTerms.isNullOrEmpty())
                    assertTrue(term.synonyms.isNullOrEmpty())
                    assertEquals(1, term.antonyms.size)
                    assertEquals(setOf("TermA"), term.antonyms.map(IGlossaryTerm::getName).toSet())
                    assertTrue(term.translatedTerms.isNullOrEmpty())
                    assertTrue(term.validValuesFor.isNullOrEmpty())
                    assertEquals(1, term.classifies.size)
                    assertEquals(setOf("Term3"), term.classifies.map(IGlossaryTerm::getName).toSet())
                }

                "TermD" -> {
                    assertEquals(1, term.categories.size)
                    assertEquals(setOf("Cat4"), term.categories.map(IGlossaryCategory::getName).toSet())
                    assertEquals("Test term D for asset import package (single category).", term.userDescription)
                    assertTrue(term.ownerUsers.isNullOrEmpty())
                    assertTrue(term.ownerGroups.isNullOrEmpty())
                    assertEquals(CertificateStatus.DEPRECATED, term.certificateStatus)
                    assertEquals("With a message.", term.certificateStatusMessage)
                    assertEquals(AtlanAnnouncementType.ISSUE, term.announcementType)
                    assertEquals("Do not use", term.announcementTitle)
                    assertEquals("A more detailed message on why…", term.announcementMessage)
                    assertTrue(term.atlanTags.isNullOrEmpty())
                    assertTrue(term.links.isNullOrEmpty())
                    assertNull(term.readme)
                    assertTrue(term.seeAlso.isNullOrEmpty())
                    assertTrue(term.preferredTerms.isNullOrEmpty())
                    assertTrue(term.synonyms.isNullOrEmpty())
                    assertTrue(term.antonyms.isNullOrEmpty())
                    assertTrue(term.translatedTerms.isNullOrEmpty())
                    assertEquals(1, term.validValuesFor.size)
                    assertEquals(setOf("Term1"), term.validValuesFor.map(IGlossaryTerm::getName).toSet())
                    assertTrue(term.classifies.isNullOrEmpty())
                }
            }
        }
    }

    @Test(groups = ["aim.ig.runUpdate"], dependsOnGroups = ["aim.ig.create"])
    fun upsertIG() {
        modifyFile()
        runCustomPackage(
            AssetImportCfg(
                glossariesFile = Paths.get(testDirectory, revisedFile).toString(),
                glossariesUpsertSemantic = "upsert",
                glossariesFailOnErrors = true,
            ),
            Importer::main,
        )
        // Allow Elastic index to become consistent
        Thread.sleep(10000)
    }

    @Test(groups = ["aim.ig.update"], dependsOnGroups = ["aim.ig.runUpdate"])
    fun tagsUnchanged() {
        val g1 = Glossary.findByName(client, glossary1)!!
        val request =
            GlossaryTerm
                .select(client)
                .where(GlossaryTerm.ANCHOR.eq(g1.qualifiedName))
                .includesOnResults(termAttrs)
                .includeOnRelations(Glossary.NAME)
                .includeOnRelations(Readme.DESCRIPTION)
                .includeOnRelations(Link.LINK)
                .toRequest()
        val response = retrySearchUntil(request, 4)
        val g1terms = response.assets
        assertEquals(4, g1terms.size)
        g1terms.forEach { term ->
            term as GlossaryTerm
            assertEquals(glossary1, term.anchor?.name)
            when (term.name) {
                "Term1" -> validateTerm1(term)
                "TermA" -> validateTermA(term)
                "TermC" -> validateTermC(term)
                "Term3" -> validateTerm3(term)
            }
        }
    }

    @Test(groups = ["aim.ig.update"], dependsOnGroups = ["aim.ig.runUpdate"])
    fun descriptionsAdded() {
        val g1 = Glossary.findByName(client, glossary1)!!
        val request =
            GlossaryTerm
                .select(client)
                .where(GlossaryTerm.ANCHOR.eq(g1.qualifiedName))
                .where(GlossaryTerm.DESCRIPTION.hasAnyValue())
                .includesOnResults(termAttrs)
                .includeOnRelations(Glossary.NAME)
                .includeOnRelations(Readme.DESCRIPTION)
                .includeOnRelations(Link.LINK)
                .toRequest()
        val response = retrySearchUntil(request, 4)
        val g1terms = response.assets
        assertEquals(4, g1terms.size)
        g1terms.forEach { term ->
            term as GlossaryTerm
            assertEquals(glossary1, term.anchor?.name)
            assertEquals("Now with a description.", term.description)
        }
    }

    private fun validateTerm1(term: GlossaryTerm) {
        assertEquals(2, term.categories.size)
        assertEquals(setOf("Cat1.a", "Cat1.a.i"), term.categories.map(IGlossaryCategory::getName).toSet())
        assertEquals("Test term 1 for asset import package (multiple categories).", term.userDescription)
        assertEquals(setOf("chris"), term.ownerUsers)
        assertTrue(term.ownerGroups.isNullOrEmpty())
        assertEquals(CertificateStatus.DRAFT, term.certificateStatus)
        assertTrue(term.certificateStatusMessage.isNullOrEmpty())
        assertEquals(AtlanAnnouncementType.WARNING, term.announcementType)
        assertEquals("Careful", term.announcementTitle)
        assertEquals("This is only a test.", term.announcementMessage)
        assertEquals(1, term.atlanTags.size)
        assertEquals(setOf(tag1), term.atlanTags.map(AtlanTag::getTypeName).toSet())
        assertEquals(2, term.links.size)
        assertEquals(setOf("Customer", "Example"), term.links.map(ILink::getName).toSet())
        assertEquals(setOf("https://en.wikipedia.org/wiki/Customer", "https://www.example.com"), term.links.map(ILink::getLink).toSet())
        assertNotNull(term.readme)
        assertEquals("<h1>This is term1!</h1>", term.readme.description)
        assertEquals(1, term.seeAlso.size)
        assertEquals(setOf("Term3"), term.seeAlso.map(IGlossaryTerm::getName).toSet())
        assertEquals(1, term.preferredTerms.size)
        assertEquals(setOf("TermA"), term.preferredTerms.map(IGlossaryTerm::getName).toSet())
        assertTrue(term.synonyms.isNullOrEmpty())
        assertTrue(term.antonyms.isNullOrEmpty())
        assertTrue(term.translatedTerms.isNullOrEmpty())
        assertTrue(term.validValuesFor.isNullOrEmpty())
        assertTrue(term.classifies.isNullOrEmpty())
    }

    private fun validateTermA(term: GlossaryTerm) {
        assertEquals(1, term.categories.size)
        assertEquals(setOf("Cat2"), term.categories.map(IGlossaryCategory::getName).toSet())
        assertEquals("Test term A for asset import package (single category).", term.userDescription)
        val chris = client.userCache.getByEmail("chris@atlan.com").username
        assertEquals(setOf("chris", chris), term.ownerUsers)
        assertTrue(term.ownerGroups.isNullOrEmpty())
        assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
        assertTrue(term.certificateStatusMessage.isNullOrEmpty())
        assertEquals(AtlanAnnouncementType.INFORMATION, term.announcementType)
        assertEquals("Look!", term.announcementTitle)
        assertEquals("Just a test.", term.announcementMessage)
        assertEquals(1, term.atlanTags.size)
        assertEquals(setOf(tag2), term.atlanTags.map(AtlanTag::getTypeName).toSet())
        assertTrue(term.links.isNullOrEmpty())
        assertNull(term.readme)
        assertTrue(term.seeAlso.isNullOrEmpty())
        assertTrue(term.preferredTerms.isNullOrEmpty())
        assertTrue(term.synonyms.isNullOrEmpty())
        assertEquals(1, term.antonyms.size)
        assertEquals(setOf("TermB"), term.antonyms.map(IGlossaryTerm::getName).toSet())
        assertTrue(term.translatedTerms.isNullOrEmpty())
        assertTrue(term.validValuesFor.isNullOrEmpty())
        assertTrue(term.classifies.isNullOrEmpty())
    }

    private fun validateTermC(term: GlossaryTerm) {
        assertTrue(term.categories.isNullOrEmpty())
        assertEquals("Test term C for asset import package (no categories).", term.userDescription)
        assertTrue(term.ownerUsers.isNullOrEmpty())
        assertTrue(term.ownerGroups.isNullOrEmpty())
        assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
        assertTrue(term.certificateStatusMessage.isNullOrEmpty())
        assertNull(term.announcementType)
        assertNull(term.announcementTitle)
        assertNull(term.announcementMessage)
        assertTrue(term.atlanTags.isNullOrEmpty())
        assertTrue(term.links.isNullOrEmpty())
        assertNull(term.readme)
        assertTrue(term.seeAlso.isNullOrEmpty())
        assertTrue(term.preferredTerms.isNullOrEmpty())
        assertEquals(1, term.synonyms.size)
        assertEquals(setOf("Term2"), term.synonyms.map(IGlossaryTerm::getName).toSet())
        assertTrue(term.antonyms.isNullOrEmpty())
        assertTrue(term.translatedTerms.isNullOrEmpty())
        assertEquals(1, term.validValuesFor.size)
        assertEquals(setOf("Term1"), term.validValuesFor.map(IGlossaryTerm::getName).toSet())
        assertTrue(term.classifies.isNullOrEmpty())
    }

    private fun validateTerm3(term: GlossaryTerm) {
        assertTrue(term.categories.isNullOrEmpty())
        assertEquals("Test term 3 for asset import package (no categories).", term.userDescription)
        assertTrue(term.ownerUsers.isNullOrEmpty())
        assertTrue(term.ownerGroups.isNullOrEmpty())
        assertNull(term.certificateStatus)
        assertTrue(term.certificateStatusMessage.isNullOrEmpty())
        assertNull(term.announcementType)
        assertNull(term.announcementTitle)
        assertNull(term.announcementMessage)
        assertEquals(2, term.atlanTags.size)
        assertEquals(setOf(tag1, tag2), term.atlanTags.map(AtlanTag::getTypeName).toSet())
        assertTrue(term.links.isNullOrEmpty())
        assertNotNull(term.readme)
        assertEquals("<h3>This is term3…</h3>", term.readme.description)
        assertEquals(2, term.seeAlso.size)
        assertEquals(setOf("Term1", "Term2"), term.seeAlso.map(IGlossaryTerm::getName).toSet())
        assertTrue(term.preferredTerms.isNullOrEmpty())
        assertTrue(term.synonyms.isNullOrEmpty())
        assertTrue(term.antonyms.isNullOrEmpty())
        assertEquals(2, term.translatedTerms.size)
        assertEquals(setOf("TermA", "TermB"), term.translatedTerms.map(IGlossaryTerm::getName).toSet())
        assertTrue(term.validValuesFor.isNullOrEmpty())
        assertTrue(term.classifies.isNullOrEmpty())
    }

    @Test(dependsOnGroups = ["aim.ig.*"])
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test(dependsOnGroups = ["aim.ig.*"])
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
