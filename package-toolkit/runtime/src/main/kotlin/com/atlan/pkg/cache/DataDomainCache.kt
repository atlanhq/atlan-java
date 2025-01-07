/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import co.elastic.clients.elasticsearch._types.SortOrder
import com.atlan.exception.AtlanException
import com.atlan.model.assets.DataDomain
import com.atlan.model.fields.AtlanField
import com.atlan.net.HttpClient
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.cell.DataDomainXformer

class DataDomainCache(val ctx: PackageContext<*>) : AssetCache<DataDomain>(ctx, "domain") {
    private val logger = Utils.getLogger(this.javaClass.name)

    private val includesOnResults: List<AtlanField> = listOf(DataDomain.NAME, DataDomain.STATUS, DataDomain.PARENT_DOMAIN, DataDomain.PARENT_DOMAIN_QUALIFIED_NAME)

    /** {@inheritDoc} */
    override fun lookupByName(name: String?) {
        // Do nothing: domain cache can only be preloaded en-masse, not retrieved domain-by-domain.
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
    ): DataDomain? {
        try {
            val dataDomain =
                DataDomain.select(client)
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
                // Note: this MUST bypass the read lock, since it is called within a write lock
                // (otherwise, this will become a deadlock -- the read will wait until write lock is released,
                // which will never happen because it is waiting on this read operation to complete.)
                getIdentity(asset.parentDomain.guid, true)
            }
        return if (parentIdentity.isNullOrBlank()) {
            asset.name
        } else {
            "$parentIdentity${DataDomainXformer.DATA_DOMAIN_DELIMITER}${asset.name}"
        }
    }

    /** {@inheritDoc} */
    override fun refreshCache() {
        val count = DataDomain.select(client).count()
        logger.info { "Caching all $count data domains, up-front..." }
        DataDomain.select(client)
            .includesOnResults(includesOnResults)
            .sort(DataDomain.PARENT_DOMAIN_QUALIFIED_NAME.order(SortOrder.Desc))
            .whereNot(DataDomain.PARENT_DOMAIN_QUALIFIED_NAME.hasAnyValue())
            .stream(true)
            .forEach { dataDomain ->
                dataDomain as DataDomain
                cache(dataDomain.guid, getIdentityForAsset(dataDomain), dataDomain)
            }
        // Note: this should NOT be streamed in parallel, as we could otherwise have situations
        // where a parent and child are processed at the same time (in separate threads) and
        // therefore the child becomes unable to resolve its parent's identity.
        DataDomain.select(client)
            .includesOnResults(includesOnResults)
            .where(DataDomain.PARENT_DOMAIN_QUALIFIED_NAME.hasAnyValue())
            .sort(DataDomain.PARENT_DOMAIN_QUALIFIED_NAME.order(SortOrder.Asc))
            .stream(false)
            .forEach { dataDomain ->
                dataDomain as DataDomain
                cache(dataDomain.guid, getIdentityForAsset(dataDomain), dataDomain)
            }
    }
}
