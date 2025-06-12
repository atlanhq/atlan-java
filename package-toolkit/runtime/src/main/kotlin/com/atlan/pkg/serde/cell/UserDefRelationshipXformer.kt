/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.model.assets.Asset
import com.atlan.model.relations.RelationshipAttributes
import com.atlan.model.relations.UserDefRelationship
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.cell.AssetRefXformer.getSemantic

/**
 * Static object to transform user-defined relationship references.
 */
object UserDefRelationshipXformer {
    const val KEY_VAL_DELIMITER = "|||"
    const val PROPERTIES_START = " {{"
    const val PROPERTIES_END = "}}"
    const val KEY_VAL_SEPARATOR = "="

    val USER_DEF_RELN_FIELDS =
        setOf(
            Asset.USER_DEF_RELATIONSHIP_TOS.atlanFieldName,
            Asset.USER_DEF_RELATIONSHIP_FROMS.atlanFieldName,
        )

    /**
     * Encodes (serializes) a user-defined relationship reference into a string form.
     *
     * @param ctx context in which the package is running
     * @param baseEncoding of the related asset
     * @param relationshipAttrs the relationship-level attributes
     * @return the string-encoded form for that asset
     */
    fun encode(
        ctx: PackageContext<*>,
        baseEncoding: String,
        relationshipAttrs: RelationshipAttributes,
    ): String {
        val extras =
            relationshipAttrs.all
                .map { (key, value) ->
                    "$key$KEY_VAL_SEPARATOR$value"
                }.joinToString(KEY_VAL_DELIMITER)
        return "$baseEncoding$PROPERTIES_START$extras$PROPERTIES_END"
    }

    /**
     * Decodes (deserializes) a string form into a user-defined relationship reference object.
     *
     * @param ctx context in which the package is running
     * @param assetRef the string form to be decoded
     * @param fieldName the name of the field containing the string-encoded value
     * @return the user-defined relationship reference represented by the string
     */
    fun decode(
        ctx: PackageContext<*>,
        assetRef: String,
        fieldName: String,
    ): Asset =
        when (fieldName) {
            in USER_DEF_RELN_FIELDS -> {
                val (extendedRef, semantic) = getSemantic(assetRef)
                val (ref, properties) = getRefAndProperties(extendedRef)
                val term =
                    ctx.termCache
                        .getByIdentity(ref)
                        ?.trimToReference()
                        ?: throw NoSuchElementException("Term not found (in $fieldName): $assetRef")
                val toLabel = properties.getOrDefault("toTypeLabel", "")
                val fromLabel = properties.getOrDefault("fromTypeLabel", "")
                UserDefRelationship
                    .builder()
                    .toTypeLabel(toLabel)
                    .fromTypeLabel(fromLabel)
                    .userDefRelationshipTo(term, semantic) as Asset
            }
            else -> AssetRefXformer.decode(ctx, assetRef, fieldName)
        }

    private fun getRefAndProperties(extendedRef: String): Pair<String, Map<String, String>> =
        when {
            extendedRef.contains(PROPERTIES_START) -> {
                val baseRef = extendedRef.substringBefore(PROPERTIES_START)
                val inner = extendedRef.substringAfter(PROPERTIES_START).substringBefore(PROPERTIES_END).trim()
                val tokens = inner.split(KEY_VAL_DELIMITER)
                val propertyMap = mutableMapOf<String, String>()
                tokens.forEach { token ->
                    val innerTokens = token.split(KEY_VAL_SEPARATOR)
                    val key = innerTokens[0].trim()
                    val value = token.substringAfter("$key$KEY_VAL_SEPARATOR").trim()
                    propertyMap[key] = value
                }
                Pair(
                    baseRef,
                    propertyMap,
                )
            }
            else -> Pair(extendedRef, emptyMap())
        }
}
