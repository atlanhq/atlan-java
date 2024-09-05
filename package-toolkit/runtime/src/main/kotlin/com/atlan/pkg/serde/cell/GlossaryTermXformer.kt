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
    val TERM_TO_TERM_FIELDS =
        setOf(
            GlossaryTerm.SEE_ALSO.atlanFieldName,
            GlossaryTerm.PREFERRED_TERMS.atlanFieldName,
            GlossaryTerm.PREFERRED_TO_TERMS.atlanFieldName,
            GlossaryTerm.SYNONYMS.atlanFieldName,
            GlossaryTerm.ANTONYMS.atlanFieldName,
            GlossaryTerm.TRANSLATED_TERMS.atlanFieldName,
            GlossaryTerm.TRANSLATION_TERMS.atlanFieldName,
            GlossaryTerm.VALID_VALUES_FOR.atlanFieldName,
            GlossaryTerm.VALID_VALUES.atlanFieldName,
            GlossaryTerm.CLASSIFIES.atlanFieldName,
            GlossaryTerm.IS_A.atlanFieldName,
            GlossaryTerm.REPLACED_BY.atlanFieldName,
            GlossaryTerm.REPLACEMENT_TERMS.atlanFieldName,
        )

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
            "assignedTerms", in TERM_TO_TERM_FIELDS,
            ->
                TermCache.getByIdentity(assetRef)?.trimToReference()
                    ?: throw NoSuchElementException("Term $assetRef not found (via $fieldName).")
            else -> AssetRefXformer.decode(assetRef, fieldName)
        }
    }
}
