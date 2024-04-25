/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataDomain
import com.atlan.pkg.cache.DataDomainCache

/**
 * Static object to transform data domain references.
 */
object DataDomainXformer {

    const val DATA_DOMAIN_DELIMITER = "@"

    /**
     * Encodes (serializes) a data domain reference into a string form.
     *
     * @param asset to be encoded
     * @return the string-encoded form for that asset
     */
    fun encode(asset: Asset): String {
        return when (asset) {
            is DataDomain -> {
                val dataDomain = DataDomainCache.getByGuid(asset.guid)
                if (dataDomain is DataDomain) {
                    DataDomainCache.getIdentity(dataDomain.guid) ?: ""
                } else {
                    ""
                }
            }
            else -> AssetRefXformer.encode(asset)
        }
    }

    /**
     * Decodes (deserializes) a string form into a category reference object.
     *
     * @param assetRef the string form to be decoded
     * @param fieldName the name of the field containing the string-encoded value
     * @return the category reference represented by the string
     */
    fun decode(
        assetRef: String,
        fieldName: String,
    ): Asset {
        return when (fieldName) {
            DataDomain.PARENT_DOMAIN.atlanFieldName -> {
                DataDomainCache.getByIdentity(assetRef)?.trimToReference()
                    ?: throw NoSuchElementException("Parent domain $assetRef not found.")
            }
            else -> AssetRefXformer.decode(assetRef, fieldName)
        }
    }
}
