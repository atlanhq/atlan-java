/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.exception.AtlanException
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.fields.AtlanField
import com.atlan.net.HttpClient
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.cell.GlossaryXformer

class TermCache(
    val ctx: PackageContext<*>,
) : AssetCache<GlossaryTerm>(ctx, "term") {
    private val logger = Utils.getLogger(this.javaClass.name)

    private val includesOnResults: List<AtlanField> = listOf(GlossaryTerm.NAME, GlossaryTerm.ANCHOR)
    private val includesOnRelations: List<AtlanField> = listOf(Glossary.NAME)

    /** {@inheritDoc} */
    override fun lookupByName(name: String?) {
        val result = lookupByIdentity(name)
        if (result != null) cache(result.guid, name, result)
    }

    /** {@inheritDoc}  */
    private fun lookupByIdentity(identity: String?): GlossaryTerm? {
        val tokens = identity?.split(GlossaryXformer.GLOSSARY_DELIMITER)
        if (tokens?.size == 2) {
            val termName = tokens[0]
            val glossaryName = tokens[1]
            val glossary = ctx.glossaryCache.getByIdentity(glossaryName)
            if (glossary != null) {
                try {
                    val term =
                        GlossaryTerm
                            .select(client)
                            .where(GlossaryTerm.NAME.eq(termName))
                            .where(GlossaryTerm.ANCHOR.eq(glossary.qualifiedName))
                            .includesOnResults(includesOnResults)
                            .includeOnResults(GlossaryTerm.STATUS)
                            .includesOnRelations(includesOnRelations)
                            .pageSize(1)
                            .stream()
                            .findFirst()
                    if (term.isPresent) {
                        return term.get() as GlossaryTerm
                    }
                } catch (e: AtlanException) {
                    logger.warn { "Unable to lookup or find term: $identity" }
                    logger.debug(e) { "Full stack trace:" }
                }
            } else {
                logger.warn { "Unable to find glossary $glossaryName for term reference: $identity" }
            }
        } else {
            logger.warn { "Unable to lookup or find term, unexpected reference: $identity" }
        }
        identity?.let { addToIgnore(identity) }
        return null
    }

    /** {@inheritDoc} */
    override fun lookupById(id: String?) {
        val result = lookupById(id, 0, ctx.client.maxNetworkRetries)
        if (result != null) cache(result.guid, getIdentityForAsset(result), result)
    }

    /** {@inheritDoc}  */
    private fun lookupById(
        guid: String?,
        currentAttempt: Int,
        maxRetries: Int,
    ): GlossaryTerm? {
        try {
            val term =
                GlossaryTerm
                    .select(client)
                    .where(GlossaryTerm.GUID.eq(guid))
                    .includesOnResults(includesOnResults)
                    .includesOnRelations(includesOnRelations)
                    .pageSize(1)
                    .stream()
                    .findFirst()
            if (term.isPresent) {
                return term.get() as GlossaryTerm
            } else {
                if (currentAttempt >= maxRetries) {
                    logger.warn { "No term found with GUID: $guid" }
                } else {
                    Thread.sleep(HttpClient.waitTime(currentAttempt).toMillis())
                    return lookupById(guid, currentAttempt + 1, maxRetries)
                }
            }
        } catch (e: AtlanException) {
            logger.warn { "Unable to lookup or find term: $guid" }
            logger.debug(e) { "Full stack trace:" }
        }
        guid?.let { addToIgnore(guid) }
        return null
    }

    /** {@inheritDoc}  */
    override fun getIdentityForAsset(asset: GlossaryTerm): String =
        asset.anchor?.name?.let { glossaryName ->
            "${asset.name}${GlossaryXformer.GLOSSARY_DELIMITER}$glossaryName"
        } ?: throw IllegalStateException("Term found with no anchor: ${asset.toJson(client)})")

    /** {@inheritDoc} */
    override fun refreshCache() {
        val count = GlossaryTerm.select(client).count()
        logger.info { "Caching all $count terms, up-front..." }
        GlossaryTerm
            .select(client)
            .includesOnResults(includesOnResults)
            .includesOnRelations(includesOnRelations)
            .stream(true)
            .forEach { term ->
                term as GlossaryTerm
                cache(term.guid, getIdentityForAsset(term), term)
            }
    }
}
