/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.model.assets.Asset
import com.atlan.model.assets.GlossaryTerm
import com.atlan.pkg.cache.TermCache
import mu.KotlinLogging

/**
 * Static object to transform term assignment references.
 */
object AssignedTermXformer {
    private val logger = KotlinLogging.logger {}

    const val TERM_GLOSSARY_DELIMITER = "@@@"

    /**
     * Encodes (serializes) a term assignment into a string form.
     *
     * @param asset to be encoded
     * @return the string-encoded form for that asset
     */
    fun encode(asset: Asset): String {
        // Handle some assets as direct embeds
        return when (asset) {
            is GlossaryTerm -> {
                val term = TermCache.getByGuid(asset.guid)
                if (term is GlossaryTerm) {
                    "${term.name}$TERM_GLOSSARY_DELIMITER${term.anchor.name}"
                } else {
                    logger.error("Unable to find any term with GUID: {}", asset.guid)
                    ""
                }
            }
            else -> AssetRefXformer.encode(asset)
        }
    }

    /**
     * Decodes (deserializes) a string form into a term assignment object.
     *
     * @param assetRef the string form to be decoded
     * @param fieldName the name of the field containing the string-encoded value
     * @return the term assignment represented by the string
     */
    fun decode(
        assetRef: String,
        fieldName: String,
    ): Asset {
        return when (fieldName) {
            "assignedTerms" -> TermCache.getByIdentity(assetRef)?.trimToReference()!!
            else -> AssetRefXformer.decode(assetRef, fieldName)
        }
    }
}
