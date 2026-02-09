/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.model.assets.Asset
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryTerm
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.cell.AssetRefXformer.getSemantic

/**
 * Static object to transform glossary assignment references.
 */
object GlossaryXformer {
    const val GLOSSARY_DELIMITER = "@@@"

    /**
     * Encodes (serializes) a glossary reference into a string form.
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
            is Glossary -> {
                val glossary = ctx.glossaryCache.getByGuid(asset.guid)
                if (glossary is Glossary) {
                    glossary.name
                } else {
                    ""
                }
            }

            else -> {
                AssetRefXformer.encode(ctx, asset)
            }
        }

    /**
     * Decodes (deserializes) a string form into a glossary reference object.
     *
     * @param ctx context in which the package is running
     * @param assetRef the string form to be decoded
     * @param fieldName the name of the field containing the string-encoded value
     * @return the glossary reference represented by the string
     */
    fun decode(
        ctx: PackageContext<*>,
        assetRef: String,
        fieldName: String,
    ): Asset =
        when (fieldName) {
            GlossaryTerm.ANCHOR.atlanFieldName -> {
                val (ref, semantic) = getSemantic(assetRef)
                ctx.glossaryCache
                    .getByIdentity(ref)
                    ?.trimToReference()
                    ?.toBuilder()
                    ?.semantic(semantic)
                    ?.build()
                    ?: throw NoSuchElementException("Parent glossary not found (in $fieldName): $assetRef")
            }

            else -> {
                AssetRefXformer.decode(ctx, assetRef, fieldName)
            }
        }
}
