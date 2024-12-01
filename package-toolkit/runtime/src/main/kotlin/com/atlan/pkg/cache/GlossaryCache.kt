/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.Atlan
import com.atlan.exception.AtlanException
import com.atlan.model.assets.Glossary
import com.atlan.model.fields.AtlanField
import com.atlan.net.HttpClient
import com.atlan.pkg.PackageContext
import mu.KotlinLogging

class GlossaryCache(val ctx: PackageContext<*>) : AssetCache<Glossary>(ctx, "glossary") {
    private val logger = KotlinLogging.logger {}

    private val includesOnResults: List<AtlanField> = listOf(Glossary.NAME, Glossary.STATUS)

    /** {@inheritDoc} */
    override fun lookupByName(name: String?) {
        val result = lookupByIdentity(name)
        if (result != null) cache(result.guid, name, result)
    }

    /** {@inheritDoc}  */
    private fun lookupByIdentity(identity: String?): Glossary? {
        try {
            return Glossary.findByName(client, identity)
        } catch (e: AtlanException) {
            logger.warn { "Unable to find glossary: $identity" }
            logger.debug(e) { "Full details: " }
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
    ): Glossary? {
        try {
            val glossary =
                Glossary.select(client)
                    .where(Glossary.GUID.eq(guid))
                    .includesOnResults(includesOnResults)
                    .pageSize(1)
                    .stream()
                    .findFirst()
            if (glossary.isPresent) {
                return glossary.get() as Glossary
            } else {
                if (currentAttempt >= maxRetries) {
                    logger.warn { "No glossary found with GUID: $guid" }
                } else {
                    Thread.sleep(HttpClient.waitTime(currentAttempt).toMillis())
                    return lookupById(guid, currentAttempt + 1, maxRetries)
                }
            }
        } catch (e: AtlanException) {
            logger.warn { "Unable to lookup or find glossary: $guid" }
            logger.debug(e) { "Full stack trace:" }
        }
        guid?.let { addToIgnore(guid) }
        return null
    }

    /** {@inheritDoc}  */
    override fun getIdentityForAsset(asset: Glossary): String {
        return asset.name
    }

    /** {@inheritDoc} */
    override fun refreshCache() {
        val count = Glossary.select(client).count()
        logger.info { "Caching all $count glossaries, up-front..." }
        Glossary.select(client)
            .includesOnResults(includesOnResults)
            .stream(true)
            .forEach { glossary ->
                glossary as Glossary
                cache(glossary.guid, getIdentityForAsset(glossary), glossary)
            }
    }
}
