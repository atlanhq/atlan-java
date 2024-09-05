/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import co.elastic.clients.elasticsearch._types.SortOrder
import com.atlan.exception.AtlanException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataDomain
import com.atlan.model.fields.AtlanField
import com.atlan.net.HttpClient
import com.atlan.pkg.serde.cell.DataDomainXformer
import mu.KotlinLogging

object DataDomainCache : AssetCache() {
    private val logger = KotlinLogging.logger {}

    private val includesOnResults: List<AtlanField> = listOf(DataDomain.NAME, DataDomain.STATUS, DataDomain.PARENT_DOMAIN, DataDomain.PARENT_DOMAIN_QUALIFIED_NAME)

    /** {@inheritDoc}  */
    override fun lookupAssetByIdentity(identity: String?): Asset? {
        // Always return null, as the cache should always be preloaded
        return null
    }

    /** {@inheritDoc}  */
    override fun lookupAssetByGuid(
        guid: String?,
        currentAttempt: Int,
        maxRetries: Int,
    ): Asset? {
        try {
            val dataDomain =
                DataDomain.select()
                    .where(DataDomain.GUID.eq(guid))
                    .includesOnResults(includesOnResults)
                    .pageSize(1)
                    .stream()
                    .findFirst()
            if (dataDomain.isPresent) {
                return dataDomain.get()
            } else {
                if (currentAttempt >= maxRetries) {
                    logger.warn { "No data domain found with GUID: $guid" }
                } else {
                    Thread.sleep(HttpClient.waitTime(currentAttempt).toMillis())
                    return lookupAssetByGuid(guid, currentAttempt + 1, maxRetries)
                }
            }
        } catch (e: AtlanException) {
            logger.warn { "Unable to lookup or find data domain: $guid" }
            logger.debug(e) { "Full stack trace:" }
        }
        guid?.let { addToIgnore(guid) }
        return null
    }

    /** {@inheritDoc}  */
    override fun getIdentityForAsset(asset: Asset): String {
        return when (asset) {
            is DataDomain -> {
                // Note: this only works as long as we always ensure that domains are loaded
                // in level-order (parents before children)
                val parentIdentity =
                    if (asset.parentDomain == null) {
                        ""
                    } else {
                        getIdentity(asset.parentDomain.guid)
                    }
                return if (parentIdentity.isNullOrBlank()) {
                    asset.name
                } else {
                    "$parentIdentity${DataDomainXformer.DATA_DOMAIN_DELIMITER}${asset.name}"
                }
            }
            else -> ""
        }
    }

    /** {@inheritDoc} */
    override fun preload() {
        logger.info { "Caching all data domains, up-front..." }
        DataDomain.select()
            .includesOnResults(includesOnResults)
            .sort(DataDomain.PARENT_DOMAIN_QUALIFIED_NAME.order(SortOrder.Desc))
            .whereNot(DataDomain.PARENT_DOMAIN_QUALIFIED_NAME.hasAnyValue())
            .stream(true)
            .forEach { dataDomain ->
                addByGuid(dataDomain.guid, dataDomain)
            }
        DataDomain.select()
            .includesOnResults(includesOnResults)
            .where(DataDomain.PARENT_DOMAIN_QUALIFIED_NAME.hasAnyValue())
            .sort(DataDomain.PARENT_DOMAIN_QUALIFIED_NAME.order(SortOrder.Asc))
            .stream(true)
            .forEach { dataDomain ->
                addByGuid(dataDomain.guid, dataDomain)
            }
    }
}
