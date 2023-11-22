/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.exception.AtlanException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.fields.AtlanField
import com.atlan.net.HttpClient
import com.atlan.pkg.serde.cell.GlossaryXformer
import mu.KotlinLogging

object TermCache : AssetCache() {
    private val logger = KotlinLogging.logger {}

    private val includesOnResults: List<AtlanField> = listOf(GlossaryTerm.NAME, GlossaryTerm.ANCHOR)
    private val includesOnRelations: List<AtlanField> = listOf(Glossary.NAME)

    /** {@inheritDoc}  */
    override fun lookupAssetByIdentity(identity: String?): Asset? {
        val tokens = identity?.split(GlossaryXformer.GLOSSARY_DELIMITER)
        if (tokens?.size == 2) {
            val termName = tokens[0]
            val glossaryName = tokens[1]
            val glossary = GlossaryCache.getByIdentity(glossaryName)
            if (glossary != null) {
                try {
                    val term =
                        GlossaryTerm.select(true)
                            .where(GlossaryTerm.NAME.eq(termName))
                            .where(GlossaryTerm.ANCHOR.eq(glossary.qualifiedName))
                            .includesOnResults(includesOnResults)
                            .includeOnResults(GlossaryTerm.STATUS)
                            .includesOnRelations(includesOnRelations)
                            .pageSize(2)
                            .stream()
                            .findFirst()
                    if (term.isPresent) {
                        return term.get()
                    }
                } catch (e: AtlanException) {
                    logger.error("Unable to lookup or find term: {}", identity, e)
                }
            } else {
                logger.error("Unable to find glossary {} for term reference: {}", glossaryName, identity)
            }
        } else {
            logger.error("Unable to lookup or find term, unexpected reference: {}", identity)
        }
        return null
    }

    /** {@inheritDoc}  */
    override fun lookupAssetByGuid(guid: String?, currentAttempt: Int, maxRetries: Int): Asset? {
        try {
            val term =
                GlossaryTerm.select(true)
                    .where(GlossaryTerm.GUID.eq(guid))
                    .includesOnResults(includesOnResults)
                    .includesOnRelations(includesOnRelations)
                    .pageSize(2)
                    .stream()
                    .findFirst()
            if (term.isPresent) {
                return term.get()
            } else {
                if (currentAttempt >= maxRetries) {
                    logger.error("No term found with GUID: {}", guid)
                } else {
                    Thread.sleep(HttpClient.waitTime(currentAttempt).toMillis())
                    return lookupAssetByGuid(guid, currentAttempt + 1, maxRetries)
                }
            }
        } catch (e: AtlanException) {
            logger.error("Unable to lookup or find term: {}", guid, e)
        }
        return null
    }

    /** {@inheritDoc}  */
    override fun getIdentityForAsset(asset: Asset): String {
        return when (asset) {
            is GlossaryTerm -> {
                "${asset.name}${GlossaryXformer.GLOSSARY_DELIMITER}${asset.anchor.name}"
            }
            else -> ""
        }
    }

    /** {@inheritDoc} */
    override fun preload() {
        logger.info("Caching all terms, up-front...")
        GlossaryTerm.select()
            .includesOnResults(includesOnResults)
            .includesOnRelations(includesOnRelations)
            .stream(true)
            .forEach { term ->
                addByGuid(term.guid, term)
            }
    }
}
