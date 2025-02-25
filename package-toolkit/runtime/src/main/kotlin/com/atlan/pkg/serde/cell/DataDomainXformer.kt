/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.pkg.PackageContext

/**
 * Static object to transform data domain references.
 */
object DataDomainXformer {
    const val DATA_PRODUCT_DELIMITER = "@@@"
    const val DATA_DOMAIN_DELIMITER = "@"

    /**
     * Encodes (serializes) a data domain reference into a string form.
     *
     * @param ctx context in which the package is running
     * @param asset to be encoded
     * @return the string-encoded form for that asset
     */
    fun encode(
        ctx: PackageContext<*>,
        asset: Asset,
    ): String =
        when (asset) {
            is DataDomain -> {
                val dataDomain = ctx.dataDomainCache.getByGuid(asset.guid)
                if (dataDomain is DataDomain) {
                    ctx.dataDomainCache.getIdentity(dataDomain.guid) ?: ""
                } else {
                    ""
                }
            }
            else -> AssetRefXformer.encode(ctx, asset)
        }

    /**
     * Encodes (serializes) a data domain reference into a string form.
     *
     * @param ctx context in which the package is running
     * @param domainGuid the GUID of the domain to be encoded
     * @return the string-encoded form for that asset
     */
    fun encodeFromGuid(
        ctx: PackageContext<*>,
        domainGuid: String,
    ): String {
        val dataDomain = ctx.dataDomainCache.getByGuid(domainGuid)
        return if (dataDomain is DataDomain) {
            ctx.dataDomainCache.getIdentity(dataDomain.guid) ?: ""
        } else {
            ""
        }
    }

    /**
     * Decodes (deserializes) a string form into a data domain reference object.
     *
     * @param ctx context in which the package is running
     * @param assetRef the string form to be decoded
     * @param fieldName the name of the field containing the string-encoded value
     * @return the data domain reference represented by the string
     */
    fun decode(
        ctx: PackageContext<*>,
        assetRef: String,
        fieldName: String,
    ): Asset =
        when (fieldName) {
            DataDomain.PARENT_DOMAIN.atlanFieldName, DataProduct.DATA_DOMAIN.atlanFieldName, Asset.DOMAIN_GUIDS.atlanFieldName -> {
                ctx.dataDomainCache.getByIdentity(assetRef)?.trimToReference()
                    ?: throw NoSuchElementException("Domain $assetRef not found (in $fieldName).")
            }
            else -> AssetRefXformer.decode(ctx, assetRef, fieldName)
        }
}
