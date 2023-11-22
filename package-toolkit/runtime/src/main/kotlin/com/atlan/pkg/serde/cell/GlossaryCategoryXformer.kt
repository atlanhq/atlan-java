/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.model.assets.Asset
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.assets.GlossaryTerm
import com.atlan.pkg.cache.CategoryCache

/**
 * Static object to transform category references.
 */
object GlossaryCategoryXformer {

    const val CATEGORY_DELIMITER = "@"

    /**
     * Encodes (serializes) a category reference into a string form.
     *
     * @param asset to be encoded
     * @return the string-encoded form for that asset
     */
    fun encode(asset: Asset): String {
        return when (asset) {
            is GlossaryCategory -> {
                val category = CategoryCache.getByGuid(asset.guid)
                if (category is GlossaryCategory) {
                    CategoryCache.getIdentity(category.guid) ?: ""
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
            GlossaryCategory.PARENT_CATEGORY.atlanFieldName -> {
                CategoryCache.getByIdentity(assetRef)?.trimToReference()
                    ?: throw NoSuchElementException("Parent category $assetRef not found.")
            }
            GlossaryTerm.CATEGORIES.atlanFieldName -> {
                CategoryCache.getByIdentity(assetRef)?.trimToReference()
                    ?: throw NoSuchElementException("Category relationship $assetRef not found.")
            }
            else -> AssetRefXformer.decode(assetRef, fieldName)
        }
    }
}
