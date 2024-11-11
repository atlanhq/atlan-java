/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.Atlan
import com.atlan.exception.AtlanException
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.fields.AtlanField
import com.atlan.net.HttpClient
import com.atlan.pkg.serde.cell.GlossaryXformer
import mu.KotlinLogging

object TermCache : AssetCache<GlossaryTerm>() {
    private val logger = KotlinLogging.logger {}

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
            val glossary = GlossaryCache.getByIdentity(glossaryName)
            if (glossary != null) {
                try {
                    val term =
                        GlossaryTerm.select()
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
        val result = lookupById(id, 0, Atlan.getDefaultClient().maxNetworkRetries)
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
                GlossaryTerm.select()
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
    override fun getIdentityForAsset(asset: GlossaryTerm): String {
        return "${asset.name}${GlossaryXformer.GLOSSARY_DELIMITER}${asset.anchor.name}"
    }

    /** {@inheritDoc} */
    override fun refreshCache() {
        val request =
            GlossaryTerm.select()
                .includesOnResults(includesOnResults)
                .includesOnRelations(includesOnRelations)
                .pageSize(1)
                .toRequest()
        val response = request.search()
        logger.info { "Caching all ${response.approximateCount ?: 0} terms, up-front..." }
        initializeOffHeap("term", response?.approximateCount?.toInt() ?: 0, response?.assets?.get(0) as GlossaryTerm, GlossaryTerm::class.java)
        GlossaryTerm.select()
            .includesOnResults(includesOnResults)
            .includesOnRelations(includesOnRelations)
            .stream(true)
            .forEach { term ->
                term as GlossaryTerm
                cache(term.guid, getIdentityForAsset(term), term)
            }
    }
}
