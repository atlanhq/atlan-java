/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.model.assets.Asset
import com.atlan.model.assets.GlossaryTerm
import com.atlan.pkg.cache.TermCache
import com.atlan.pkg.serde.cell.GlossaryXformer.GLOSSARY_DELIMITER

/**
 * Static object to transform term references.
 */
object GlossaryTermXformer {
    /**
     * Encodes (serializes) a term reference into a string form.
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
                    "${term.name}$GLOSSARY_DELIMITER${term.anchor.name}"
                } else {
                    ""
                }
            }
            else -> AssetRefXformer.encode(asset)
        }
    }

    /**
     * Decodes (deserializes) a string form into a term reference object.
     *
     * @param assetRef the string form to be decoded
     * @param fieldName the name of the field containing the string-encoded value
     * @return the term reference represented by the string
     */
    fun decode(
        assetRef: String,
        fieldName: String,
    ): Asset {
        return when (fieldName) {
            "assignedTerms", "seeAlso", "preferredTerms", "preferredToTerms",
            "synonyms", "antonyms", "translatedTerms", "translationTerms",
            "validValuesFor", "validValues", "classifies", "isA", "replacedBy",
            "replacementTerms",
            -> TermCache.getByIdentity(assetRef)?.trimToReference() ?: throw IllegalStateException("Term $assetRef not found (via $fieldName).")
            else -> AssetRefXformer.decode(assetRef, fieldName)
        }
    }
}
