/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.model.assets.Asset
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.assets.GlossaryTerm
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.cell.AssetRefXformer.getSemantic

/**
 * Static object to transform category references.
 */
object GlossaryCategoryXformer {
    const val CATEGORY_DELIMITER = "@"

    /**
     * Encodes (serializes) a category reference into a string form.
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
            is GlossaryCategory -> {
                val category = ctx.categoryCache.getByGuid(asset.guid)
                if (category is GlossaryCategory) {
                    ctx.categoryCache.getIdentity(category.guid) ?: ""
                } else {
                    ""
                }
            }

            else -> {
                AssetRefXformer.encode(ctx, asset)
            }
        }

    /**
     * Decodes (deserializes) a string form into a category reference object.
     *
     * @param ctx context in which the package is running
     * @param assetRef the string form to be decoded
     * @param fieldName the name of the field containing the string-encoded value
     * @return the category reference represented by the string
     */
    fun decode(
        ctx: PackageContext<*>,
        assetRef: String,
        fieldName: String,
    ): Asset =
        when (fieldName) {
            GlossaryCategory.PARENT_CATEGORY.atlanFieldName -> {
                val (ref, semantic) = getSemantic(assetRef)
                ctx.categoryCache
                    .getByIdentity(ref)
                    ?.trimToReference()
                    ?.toBuilder()
                    ?.semantic(semantic)
                    ?.build()
                    ?: throw NoSuchElementException("Parent category not found (in $fieldName): $assetRef")
            }

            GlossaryTerm.CATEGORIES.atlanFieldName -> {
                val (ref, semantic) = getSemantic(assetRef)
                ctx.categoryCache
                    .getByIdentity(assetRef)
                    ?.trimToReference()
                    ?.toBuilder()
                    ?.semantic(semantic)
                    ?.build()
                    ?: throw NoSuchElementException("Category relationship not found (in $fieldName): $assetRef")
            }

            else -> {
                AssetRefXformer.decode(ctx, assetRef, fieldName)
            }
        }
}
