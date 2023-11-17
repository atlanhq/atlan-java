/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.model.assets.Asset
import com.atlan.model.assets.Glossary
import com.atlan.pkg.cache.GlossaryCache

/**
 * Static object to transform glossary assignment references.
 */
object GlossaryXformer {

    const val GLOSSARY_DELIMITER = "@@@"

    /**
     * Encodes (serializes) a glossary reference into a string form.
     *
     * @param asset to be encoded
     * @return the string-encoded form for that asset
     */
    fun encode(asset: Asset): String {
        return when (asset) {
            is Glossary -> {
                val glossary = GlossaryCache.getByGuid(asset.guid)
                if (glossary is Glossary) {
                    glossary.name
                } else {
                    ""
                }
            }
            else -> AssetRefXformer.encode(asset)
        }
    }

    /**
     * Decodes (deserializes) a string form into a glossary reference object.
     *
     * @param assetRef the string form to be decoded
     * @param fieldName the name of the field containing the string-encoded value
     * @return the glossary reference represented by the string
     */
    fun decode(
        assetRef: String,
        fieldName: String,
    ): Asset {
        return when (fieldName) {
            "anchor" -> GlossaryCache.getByIdentity(assetRef)?.trimToReference()!!
            else -> AssetRefXformer.decode(assetRef, fieldName)
        }
    }
}
