/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.AtlanClient
import com.atlan.exception.AtlanException
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
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.round
import kotlin.system.exitProcess

object TestsCleanup {
    private val logger = Utils.getLogger(TestsCleanup.javaClass.name)

    private const val DELETION_BATCH = 20

    /**
     * Actually run the logic to clean up test assets.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        Utils.initializeContext<TestsCleanupCfg>().use { ctx ->
            val prefix = ctx.config.prefix
            if (prefix.isBlank()) {
                logger.error { "Missing required parameter - you must specify a prefix for objects to be purged." }
                exitProcess(1)
            }

            purgeGlossaries(ctx.client, prefix)
            purgeProducts(ctx.client, prefix)
            purgeDomains(ctx.client, prefix)
            purgeAssets(ctx.client, prefix)
            purgePurposes(ctx.client, prefix)
            purgePersonas(ctx.client, prefix)
            purgeCustomMetadata(ctx.client, prefix)
            purgeTags(ctx.client, prefix)
        }
    }

    private fun purgeGlossaries(
        client: AtlanClient,
        prefix: String,
    ) {
        val glossaries =
            Glossary
                .select(client, true)
                .where(Glossary.NAME.startsWith(prefix))
                .includeOnResults(Glossary.NAME)
                .stream()
                .map { AssetDetails(it.name, it.qualifiedName, it.guid) }
                .toList()
        glossaries.forEach { glossary ->
            val qn = glossary.qualifiedName
            val name = glossary.name
            val terms =
                GlossaryTerm
                    .select(client, true)
                    .where(GlossaryTerm.ANCHOR.eq(qn))
                    .stream()
                    .map { it.guid }
                    .toList()
            logger.info { "Purging ${terms.size} terms from glossary: $name" }
            purgeByGuids(client, terms)
            val categories =
                GlossaryCategory
                    .select(client, true)
                    .where(GlossaryCategory.ANCHOR.eq(qn))
                    .stream()
                    .map { it.guid }
                    .toList()
            logger.info { "Purging ${categories.size} categories from glossary: $name" }
            purgeByGuids(client, categories)
            logger.info { "Purging glossary: $name" }
            purgeByGuids(client, listOf(glossary.guid))
        }
    }

    data class AssetDetails(
        val name: String,
        val qualifiedName: String,
        val guid: String,
    )

    private fun purgeProducts(
        client: AtlanClient,
        prefix: String,
    ) {
        val list =
            DataProduct
                .select(client, true)
                .where(DataProduct.NAME.startsWith(prefix))
                .stream()
                .map { it.guid }
                .toList()
        logger.info { "Purging ${list.size} data products." }
        purgeByGuids(client, list)
    }

    private fun purgeDomains(
        client: AtlanClient,
        prefix: String,
    ) {
        val list =
            DataDomain
                .select(client, true)
                .where(DataDomain.NAME.startsWith(prefix))
                .stream()
                .map { it.guid }
                .toList()
        logger.info { "Purging ${list.size} data domains." }
        purgeByGuids(client, list)
    }

    private fun purgeAssets(
        client: AtlanClient,
        prefix: String,
    ) {
        val list =
            Connection
                .select(client, true)
                .where(Connection.NAME.startsWith(prefix))
                .stream()
                .map { AssetDetails(it.name, it.qualifiedName, it.guid) }
                .toList()
        list.forEach { connection ->
            val qn = connection.qualifiedName
            val name = connection.name
            val assets =
                client.assets
                    .select(true)
                    .where(Asset.CONNECTION_QUALIFIED_NAME.eq(qn))
                    .stream()
                    .map { it.guid }
                    .toList()
            logger.info { "Purging ${assets.size} assets from connection: $name" }
            purgeByGuids(client, assets)
            logger.info { "Purging connection: $name" }
            purgeByGuids(client, listOf(connection.guid))
        }
    }

    private fun purgePurposes(
        client: AtlanClient,
        prefix: String,
    ) {
        val list =
            Purpose
                .select(client, true)
                .where(Purpose.NAME.startsWith(prefix))
                .stream()
                .map { it.guid }
                .toList()
        logger.info { "Purging ${list.size} purposes." }
        purgeByGuids(client, list)
    }

    private fun purgePersonas(
        client: AtlanClient,
        prefix: String,
    ) {
        val list =
            Persona
                .select(client, true)
                .where(Persona.NAME.startsWith(prefix))
                .stream()
                .map { it.guid }
                .toList()
        logger.info { "Purging ${list.size} personas." }
        purgeByGuids(client, list)
    }

    private fun purgeCustomMetadata(
        client: AtlanClient,
        prefix: String,
    ) {
        val list =
            client.typeDefs
                .list(AtlanTypeCategory.CUSTOM_METADATA)
                .customMetadataDefs
                .stream()
                .filter { it.displayName.startsWith(prefix) }
                .map { TypeDefDetails(it.displayName, it.name) }
                .toList()
        list.forEach { cm ->
            val badgeQNPrefix = "badges/global/${cm.internalName}."
            val badges =
                Badge
                    .select(client, true)
                    .where(Badge.QUALIFIED_NAME.startsWith(badgeQNPrefix))
                    .stream()
                    .map { it.guid }
                    .toList()
            logger.info { "Purging ${badges.size} badges for custom metadata: ${cm.name}" }
            purgeByGuids(client, badges)
            logger.info { "Purging custom metadata: ${cm.name}" }
            try {
                getPrivilegedClient(client).use { sudo -> sudo.typeDefs.purge(cm.internalName) }
            } catch (e: AtlanException) {
                logger.error(e) { " ... failed to purge: ${cm.name}" }
            }
        }
        val enums =
            client.typeDefs
                .list(AtlanTypeCategory.ENUM)
                .enumDefs
                .stream()
                .filter { it.name.startsWith(prefix) }
                .map { TypeDefDetails(it.name, it.name) }
                .toList()
        enums.forEach { e ->
            logger.info { "Purging enum: ${e.internalName}" }
            getPrivilegedClient(client).use { sudo -> sudo.typeDefs.purge(e.internalName) }
        }
    }

    data class TypeDefDetails(
        val name: String,
        val internalName: String,
    )

    private fun purgeTags(
        client: AtlanClient,
        prefix: String,
    ) {
        val list =
            client.typeDefs
                .list(AtlanTypeCategory.ATLAN_TAG)
                .atlanTagDefs
                .stream()
                .filter { it.displayName.startsWith(prefix) }
                .map { TypeDefDetails(it.displayName, it.name) }
                .toList()
        list.forEach { cm ->
            logger.info { "Purging Atlan tag: ${cm.name}" }
            try {
                getPrivilegedClient(client).use { sudo -> sudo.typeDefs.purge(cm.internalName) }
            } catch (e: AtlanException) {
                logger.error(e) { " ... failed to purge ${cm.name}" }
            }
        }
    }

    private fun purgeByGuids(
        client: AtlanClient,
        guidList: List<String>,
    ) {
        if (guidList.isNotEmpty()) {
            val deletionType = AtlanDeleteType.PURGE
            val totalToDelete = guidList.size
            logger.info { " --- Deleting ($deletionType) $totalToDelete assets... ---" }
            val currentCount = AtomicLong(0)
            if (totalToDelete < DELETION_BATCH) {
                getPrivilegedClient(client).use { sudo -> sudo.assets.delete(guidList, deletionType) }
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
                            getPrivilegedClient(client).use { sudo -> sudo.assets.delete(batch, deletionType) }
                        }
                    }
            }
        }
    }

    private fun getPrivilegedClient(client: AtlanClient): AtlanClient = AtlanClient("INTERNAL", client.impersonate.escalate())
}
