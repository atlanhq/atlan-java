/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.exception.AtlanException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Glossary
import com.atlan.model.fields.AtlanField
import com.atlan.net.HttpClient
import mu.KotlinLogging

object GlossaryCache : AssetCache() {
    private val logger = KotlinLogging.logger {}

    private val includesOnResults: List<AtlanField> = listOf(Glossary.NAME, Glossary.STATUS)

    /** {@inheritDoc}  */
    override fun lookupAssetByIdentity(identity: String?): Asset? {
        try {
            return Glossary.findByName(identity)
        } catch (e: AtlanException) {
            logger.warn { "Unable to find glossary: $identity" }
        }
        identity?.let { addToIgnore(identity) }
        return null
    }

    /** {@inheritDoc}  */
    override fun lookupAssetByGuid(guid: String?, currentAttempt: Int, maxRetries: Int): Asset? {
        try {
            val glossary =
                Glossary.select()
                    .where(Glossary.GUID.eq(guid))
                    .includesOnResults(includesOnResults)
                    .pageSize(1)
                    .stream()
                    .findFirst()
            if (glossary.isPresent) {
                return glossary.get()
            } else {
                if (currentAttempt >= maxRetries) {
                    logger.error { "No glossary found with GUID: $guid" }
                } else {
                    Thread.sleep(HttpClient.waitTime(currentAttempt).toMillis())
                    return lookupAssetByGuid(guid, currentAttempt + 1, maxRetries)
                }
            }
        } catch (e: AtlanException) {
            logger.error("Unable to lookup or find glossary: {}", guid, e)
        }
        guid?.let { addToIgnore(guid) }
        return null
    }

    /** {@inheritDoc}  */
    override fun getIdentityForAsset(asset: Asset): String {
        return asset.name
    }

    /** {@inheritDoc} */
    override fun preload() {
        logger.info { "Caching all glossaries, up-front..." }
        Glossary.select()
            .includesOnResults(includesOnResults)
            .stream(true)
            .forEach { glossary ->
                addByGuid(glossary.guid, glossary)
            }
    }
}
