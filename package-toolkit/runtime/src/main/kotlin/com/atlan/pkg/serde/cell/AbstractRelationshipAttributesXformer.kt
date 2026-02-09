/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.model.relations.RelationshipAttributes
import com.atlan.pkg.PackageContext

/**
 * Static object to transform relationship-level attribute details.
 */
abstract class AbstractRelationshipAttributesXformer {
    companion object {
        const val KEY_VAL_DELIMITER = "|||"
        const val PROPERTIES_START = " {{"
        const val PROPERTIES_END = "}}"
        const val KEY_VAL_SEPARATOR = "="

        /**
         * Parse the asset reference and extra relationship-level attributes from a string formm.
         *
         * @param extendedRef the full string containing the asset reference and relationship-level attributes
         * @return a tuple (pair) with the base asset reference and a map of attributes and values
         */
        fun getRefAndProperties(extendedRef: String): Pair<String, Map<String, String>> =
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

                else -> {
                    Pair(extendedRef, emptyMap())
                }
            }
    }

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
}
