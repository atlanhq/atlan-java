/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import co.elastic.clients.elasticsearch._types.SortOrder
import com.atlan.Atlan
import com.atlan.exception.AtlanException
import com.atlan.model.assets.DataDomain
import com.atlan.model.fields.AtlanField
import com.atlan.net.HttpClient
import com.atlan.pkg.serde.cell.DataDomainXformer
import mu.KotlinLogging

object DataDomainCache : AssetCache<DataDomain>() {
    private val logger = KotlinLogging.logger {}

    private val includesOnResults: List<AtlanField> = listOf(DataDomain.NAME, DataDomain.STATUS, DataDomain.PARENT_DOMAIN, DataDomain.PARENT_DOMAIN_QUALIFIED_NAME)

    /** {@inheritDoc} */
    override fun lookupByName(name: String?) {
        throw IllegalStateException("Domain cache can only be preloaded en-masse, not retrieved domain-by-domain.")
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
    ): DataDomain? {
        try {
            val dataDomain =
                DataDomain.select()
                    .where(DataDomain.GUID.eq(guid))
                    .includesOnResults(includesOnResults)
                    .pageSize(1)
                    .stream()
                    .findFirst()
            if (dataDomain.isPresent) {
                return dataDomain.get() as DataDomain
            } else {
                if (currentAttempt >= maxRetries) {
                    logger.warn { "No data domain found with GUID: $guid" }
                } else {
                    Thread.sleep(HttpClient.waitTime(currentAttempt).toMillis())
                    return lookupById(guid, currentAttempt + 1, maxRetries)
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
    override fun getIdentityForAsset(asset: DataDomain): String {
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

    /** {@inheritDoc} */
    override fun refreshCache() {
        val request =
            DataDomain.select()
                .includesOnResults(includesOnResults)
                .pageSize(1)
                .toRequest()
        val response = request.search()
        logger.info { "Caching all ${response.approximateCount ?: 0} data domains, up-front..." }
        initializeOffHeap("datadomain", response?.approximateCount?.toInt() ?: 0, response?.assets[0] as DataDomain, DataDomain::class.java)
        DataDomain.select()
            .includesOnResults(includesOnResults)
            .sort(DataDomain.PARENT_DOMAIN_QUALIFIED_NAME.order(SortOrder.Desc))
            .whereNot(DataDomain.PARENT_DOMAIN_QUALIFIED_NAME.hasAnyValue())
            .stream(true)
            .forEach { dataDomain ->
                dataDomain as DataDomain
                cache(dataDomain.guid, getIdentityForAsset(dataDomain), dataDomain)
            }
        DataDomain.select()
            .includesOnResults(includesOnResults)
            .where(DataDomain.PARENT_DOMAIN_QUALIFIED_NAME.hasAnyValue())
            .sort(DataDomain.PARENT_DOMAIN_QUALIFIED_NAME.order(SortOrder.Asc))
            .stream(true)
            .forEach { dataDomain ->
                dataDomain as DataDomain
                cache(dataDomain.guid, getIdentityForAsset(dataDomain), dataDomain)
            }
    }
}
