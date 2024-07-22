/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.AtlanClient
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Badge
import com.atlan.model.assets.Connection
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.assets.Persona
import com.atlan.model.assets.Purpose
import com.atlan.model.enums.AtlanDeleteType
import com.atlan.model.enums.AtlanTypeCategory
import com.atlan.pkg.Utils
import mu.KotlinLogging
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.round
import kotlin.system.exitProcess

object TestsCleanup {
    private val logger = KotlinLogging.logger {}

    private const val PRIVILEGED_CLIENT = "pc"
    private const val DELETION_BATCH = 20
    private lateinit var client: AtlanClient

    /**
     * Actually run the logic to clean up test assets.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val config = Utils.setPackageOps<TestsCleanupCfg>()

        val prefix = Utils.getOrDefault(config.prefix, "")
        if (prefix.isBlank()) {
            logger.error { "Missing required parameter - you must specify a prefix for objects to be purged." }
            exitProcess(1)
        }

        client = Atlan.getDefaultClient()

        purgeGlossaries(prefix)
        purgeProducts(prefix)
        purgeDomains(prefix)
        purgeAssets(prefix)
        purgePurposes(prefix)
        purgePersonas(prefix)
        purgeCustomMetadata(prefix)
        purgeTags(prefix)
    }

    private fun purgeGlossaries(prefix: String) {
        val glossaries = Glossary.select()
            .where(Glossary.NAME.startsWith(prefix))
            .includeOnResults(Glossary.NAME)
            .stream()
            .map { AssetDetails(it.name, it.qualifiedName, it.guid) }
            .toList()
        glossaries.forEach { glossary ->
            val qn = glossary.qualifiedName
            val name = glossary.name
            val terms = GlossaryTerm.select()
                .where(GlossaryTerm.ANCHOR.eq(qn))
                .stream()
                .map { it.guid }
                .toList()
            logger.info { "Purging ${terms.size} terms from glossary: $name" }
            purgeByGuids(terms)
            val categories = GlossaryCategory.select()
                .where(GlossaryCategory.ANCHOR.eq(qn))
                .stream()
                .map { it.guid }
                .toList()
            logger.info { "Purging ${categories.size} categories from glossary: $name" }
            purgeByGuids(categories)
            logger.info { "Purging glossary: $name" }
            purgeByGuids(listOf(glossary.guid))
        }
    }

    data class AssetDetails(val name: String, val qualifiedName: String, val guid: String)

    private fun purgeProducts(prefix: String) {
        val list = DataProduct.select()
            .where(DataProduct.NAME.startsWith(prefix))
            .stream()
            .map { it.guid }
            .toList()
        logger.info { "Purging ${list.size} data products." }
        purgeByGuids(list)
    }

    private fun purgeDomains(prefix: String) {
        val list = DataDomain.select()
            .where(DataDomain.NAME.startsWith(prefix))
            .stream()
            .map { it.guid }
            .toList()
        logger.info { "Purging ${list.size} data domains." }
        purgeByGuids(list)
    }

    private fun purgeAssets(prefix: String) {
        val list = Connection.select()
            .where(Connection.NAME.startsWith(prefix))
            .stream()
            .map { AssetDetails(it.name, it.qualifiedName, it.guid) }
            .toList()
        list.forEach { connection ->
            val qn = connection.qualifiedName
            val name = connection.name
            val assets = client.assets.select()
                .where(Asset.CONNECTION_QUALIFIED_NAME.eq(qn))
                .stream()
                .map { it.guid }
                .toList()
            logger.info { "Purging ${assets.size} assets from connection: $name" }
            purgeByGuids(assets)
            logger.info { "Purging connection: $name" }
            purgeByGuids(listOf(connection.guid))
        }
    }

    private fun purgePurposes(prefix: String) {
        val list = Purpose.select()
            .where(Purpose.NAME.startsWith(prefix))
            .stream()
            .map { it.guid }
            .toList()
        logger.info { "Purging ${list.size} purposes." }
        purgeByGuids(list)
    }

    private fun purgePersonas(prefix: String) {
        val list = Persona.select()
            .where(Persona.NAME.startsWith(prefix))
            .stream()
            .map { it.guid }
            .toList()
        logger.info { "Purging ${list.size} personas." }
        purgeByGuids(list)
    }

    private fun purgeCustomMetadata(prefix: String) {
        val enums = client.typeDefs.list(AtlanTypeCategory.ENUM)
            .enumDefs
            .stream()
            .filter { it.name.startsWith(prefix) }
            .map { TypeDefDetails(it.name, it.name) }
            .toList()
        enums.forEach { e ->
            logger.info { "Purging enum: ${e.internalName}" }
            getPrivilegedClient().typeDefs.purge(e.internalName)
        }
        val list = client.typeDefs.list(AtlanTypeCategory.CUSTOM_METADATA)
            .customMetadataDefs
            .stream()
            .filter { it.displayName.startsWith(prefix) }
            .map { TypeDefDetails(it.displayName, it.name) }
            .toList()
        list.forEach { cm ->
            val badges = Badge.select()
                .where(Badge.QUALIFIED_NAME.startsWith(Badge.generateQualifiedName(cm.name, "")))
                .stream()
                .map { it.guid }
                .toList()
            logger.info { "Purging ${badges.size} badges for custom metadata: ${cm.name}" }
            purgeByGuids(badges)
            logger.info { "Purging custom metadata: ${cm.name}" }
            getPrivilegedClient().typeDefs.purge(cm.internalName)
        }
    }

    data class TypeDefDetails(val name: String, val internalName: String)

    private fun purgeTags(prefix: String) {
        val list = client.typeDefs.list(AtlanTypeCategory.ATLAN_TAG)
            .atlanTagDefs
            .stream()
            .filter { it.displayName.startsWith(prefix) }
            .map { TypeDefDetails(it.displayName, it.name) }
            .toList()
        list.forEach { cm ->
            logger.info { "Purging Atlan tag: ${cm.name}" }
            getPrivilegedClient().typeDefs.purge(cm.internalName)
        }
    }

    private fun purgeByGuids(guidList: List<String>) {
        if (guidList.isNotEmpty()) {
            val deletionType = AtlanDeleteType.PURGE
            val totalToDelete = guidList.size
            logger.info { " --- Deleting ($deletionType) $totalToDelete assets... ---" }
            val currentCount = AtomicLong(0)
            if (totalToDelete < DELETION_BATCH) {
                getPrivilegedClient().assets.delete(guidList, deletionType)
            } else {
                // Delete in parallel
                guidList
                    .asSequence()
                    .chunked(DELETION_BATCH)
                    .toList()
                    .parallelStream()
                    .forEach { batch ->
                        val i = currentCount.getAndAdd(DELETION_BATCH.toLong())
                        logger.info { " ... next batch of $DELETION_BATCH (${round((i.toDouble() / totalToDelete) * 100)}%)" }
                        if (batch.isNotEmpty()) {
                            getPrivilegedClient().assets.delete(batch, deletionType)
                        }
                    }
            }
        }
    }

    private fun getPrivilegedClient(): AtlanClient {
        val privileged = Atlan.getClient("INTERNAL", PRIVILEGED_CLIENT)
        privileged.apiToken = client.impersonate.escalate()
        return privileged
    }
}
