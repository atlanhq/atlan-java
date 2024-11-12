/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.Atlan
import com.atlan.exception.AtlanException
import com.atlan.model.assets.Glossary
import com.atlan.model.fields.AtlanField
import com.atlan.net.HttpClient
import mu.KotlinLogging

object GlossaryCache : AssetCache<Glossary>(
    "glossary",
    Glossary.creator("Glossary Name")
        .userDescription("Could be empty")
        .build(),
    Glossary::class.java,
) {
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
            return Glossary.findByName(identity)
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
                Glossary.select()
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
        val request =
            Glossary.select()
                .includesOnResults(includesOnResults)
                .pageSize(1)
                .toRequest()
        val response = request.search()
        logger.info { "Caching all ${response.approximateCount ?: 0} glossaries, up-front..." }
        resetOffHeap(response)
        Glossary.select()
            .includesOnResults(includesOnResults)
            .stream(true)
            .forEach { glossary ->
                glossary as Glossary
                cache(glossary.guid, getIdentityForAsset(glossary), glossary)
            }
    }
}
