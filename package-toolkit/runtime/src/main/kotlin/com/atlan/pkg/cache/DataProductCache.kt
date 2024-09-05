/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.exception.AtlanException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.model.fields.AtlanField
import com.atlan.net.HttpClient
import com.atlan.pkg.serde.cell.DataDomainXformer
import com.atlan.pkg.serde.cell.GlossaryXformer
import mu.KotlinLogging

object DataProductCache : AssetCache() {
    private val logger = KotlinLogging.logger {}

    private val includesOnResults: List<AtlanField> = listOf(DataProduct.NAME, DataProduct.DATA_DOMAIN)
    private val includesOnRelations: List<AtlanField> = listOf(DataDomain.NAME)

    /** {@inheritDoc}  */
    override fun lookupAssetByIdentity(identity: String?): Asset? {
        val tokens = identity?.split(DataDomainXformer.DATA_PRODUCT_DELIMITER)
        if (tokens?.size == 2) {
            val productName = tokens[0]
            val domainIdentity = tokens[1]
            val domain = DataDomainCache.getByIdentity(domainIdentity)
            if (domain != null) {
                try {
                    val request =
                        DataProduct.select()
                            .where(DataProduct.NAME.eq(productName))
                            .includesOnResults(includesOnResults)
                            .includeOnResults(DataProduct.STATUS)
                            .includesOnRelations(includesOnRelations)
                            .pageSize(50)
                            .toRequest()
                    var response = request.search()
                    while (response != null && response.approximateCount > 0) {
                        for (candidate in response) {
                            val dp = candidate as DataProduct
                            val domId = DataDomainXformer.encode(dp.dataDomain as DataDomain)
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

    /** {@inheritDoc}  */
    override fun lookupAssetByGuid(
        guid: String?,
        currentAttempt: Int,
        maxRetries: Int,
    ): Asset? {
        try {
            val dp =
                DataProduct.select()
                    .where(DataProduct.GUID.eq(guid))
                    .includesOnResults(includesOnResults)
                    .includesOnRelations(includesOnRelations)
                    .pageSize(1)
                    .stream()
                    .findFirst()
            if (dp.isPresent) {
                return dp.get()
            } else {
                if (currentAttempt >= maxRetries) {
                    logger.warn { "No data product found with GUID: $guid" }
                } else {
                    Thread.sleep(HttpClient.waitTime(currentAttempt).toMillis())
                    return lookupAssetByGuid(guid, currentAttempt + 1, maxRetries)
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
    override fun getIdentityForAsset(asset: Asset): String {
        return when (asset) {
            is DataProduct -> {
                "${asset.name}${GlossaryXformer.GLOSSARY_DELIMITER}${DataDomainXformer.encode(asset.dataDomain as DataDomain)}"
            }
            else -> ""
        }
    }

    /** {@inheritDoc} */
    override fun preload() {
        logger.info { "Caching all data products, up-front..." }
        DataProduct.select()
            .includesOnResults(includesOnResults)
            .includesOnRelations(includesOnRelations)
            .stream(true)
            .forEach { dp ->
                addByGuid(dp.guid, dp)
            }
    }
}
