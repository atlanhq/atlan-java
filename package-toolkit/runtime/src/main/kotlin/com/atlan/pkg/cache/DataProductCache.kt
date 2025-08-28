/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.exception.AtlanException
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.model.fields.AtlanField
import com.atlan.net.HttpClient
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.cell.DataDomainXformer

class DataProductCache(
    val ctx: PackageContext<*>,
) : AssetCache<DataProduct>(ctx, "product") {
    private val logger = Utils.getLogger(this.javaClass.name)

    private val includesOnResults: List<AtlanField> = listOf(DataProduct.NAME, DataProduct.DATA_DOMAIN)
    private val includesOnRelations: List<AtlanField> = listOf(DataDomain.NAME)

    /** {@inheritDoc} */
    override fun lookupByName(name: String?) {
        val result = lookupByIdentity(name)
        if (result != null) cache(result.guid, name, result)
    }

    /** {@inheritDoc}  */
    private fun lookupByIdentity(identity: String?): DataProduct? {
        val tokens = identity?.split(DataDomainXformer.DATA_PRODUCT_DELIMITER)
        if (tokens?.size == 2) {
            val productName = tokens[0]
            val domainIdentity = tokens[1]
            val domain = ctx.dataDomainCache.getByIdentity(domainIdentity)
            if (domain != null) {
                try {
                    val request =
                        DataProduct
                            .select(client)
                            .where(DataProduct.NAME.eq(productName))
                            .includesOnResults(includesOnResults)
                            .includeOnResults(DataProduct.STATUS)
                            .includesOnRelations(includesOnRelations)
                            .pageSize(50)
                            .toRequest()
                    var response = request.search(ctx.client)
                    while (response != null && response.assets?.isNotEmpty() ?: false) {
                        for (candidate in response) {
                            val dp = candidate as DataProduct
                            val domId = dp.dataDomain?.let { DataDomainXformer.encode(ctx, it as DataDomain) } ?: DataDomainXformer.NO_DOMAIN_SENTINEL
                            if (domId == domainIdentity) {
                                // Short-circuit as soon as we find a data product in the appropriate domain
                                return dp
                            }
                        }
                        response = response.nextPage
                    }
                    return null
                } catch (e: AtlanException) {
                    logger.warn { "Unable to lookup or find data product: $identity" }
                    logger.debug(e) { "Full stack trace:" }
                }
            } else {
                logger.warn { "Unable to find domain $domainIdentity for data product reference: $identity" }
            }
        } else {
            logger.warn { "Unable to lookup or find data product, unexpected reference: $identity" }
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
    ): DataProduct? {
        try {
            val dp =
                DataProduct
                    .select(client)
                    .where(DataProduct.GUID.eq(guid))
                    .includesOnResults(includesOnResults)
                    .includesOnRelations(includesOnRelations)
                    .pageSize(1)
                    .stream()
                    .findFirst()
            if (dp.isPresent) {
                return dp.get() as DataProduct
            } else {
                if (currentAttempt >= maxRetries) {
                    logger.warn { "No data product found with GUID: $guid" }
                } else {
                    Thread.sleep(HttpClient.waitTime(currentAttempt).toMillis())
                    return lookupById(guid, currentAttempt + 1, maxRetries)
                }
            }
        } catch (e: AtlanException) {
            logger.warn { "Unable to lookup or find data product: $guid" }
            logger.debug(e) { "Full stack trace:" }
        }
        guid?.let { addToIgnore(guid) }
        return null
    }

    /** {@inheritDoc}  */
    override fun getIdentityForAsset(asset: DataProduct): String {
        val domain = asset.dataDomain?.let { DataDomainXformer.encode(ctx, it as DataDomain) } ?: DataDomainXformer.NO_DOMAIN_SENTINEL
        return "${asset.name}${DataDomainXformer.DATA_PRODUCT_DELIMITER}$domain"
    }

    /** {@inheritDoc} */
    override fun refreshCache() {
        val count = DataProduct.select(client).count()
        logger.info { "Caching all $count data products, up-front..." }
        DataProduct
            .select(client)
            .includesOnResults(includesOnResults)
            .includesOnRelations(includesOnRelations)
            .stream(true)
            .forEach { dp ->
                dp as DataProduct
                cache(dp.guid, getIdentityForAsset(dp), dp)
            }
    }
}
