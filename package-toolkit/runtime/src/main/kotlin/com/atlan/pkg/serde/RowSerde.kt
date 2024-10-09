/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde

import com.atlan.cache.ReflectionCache
import com.atlan.model.assets.Asset
import com.atlan.model.fields.AtlanField
import com.atlan.model.fields.CustomMetadataField

/**
 * Factory methods for dealing with row-based (de)serialization, for tabular data.
 */
object RowSerde {
    const val CM_HEADING_DELIMITER = "::"

    /**
     * Retrieve the name to use for the header for a particular field, assuming a particular type of asset.
     *
     * @param field for which to determine the header name
     * @param assetClass (optional) asset class in which to assume the field is defined (defaults to general Asset)
     * @return the name of the header to use for that field
     */
    fun getHeaderForField(
        field: AtlanField,
        assetClass: Class<*> = Asset::class.java,
    ): String {
        return if (field is CustomMetadataField) {
            // For custom metadata, translate the header to human-readable names
            field.setName + CM_HEADING_DELIMITER + field.attributeName
        } else {
            // Use renamed fields for deserialization, if available
            getHeaderForField(field.atlanFieldName, assetClass)
        }
    }

    /**
     * Retrieve the name to use for the header for a particular field, assuming a particular type of asset.
     *
     * @param field for which to determine the header name
     * @param assetClass (optional) asset class in which to assume the field is defined (defaults to general Asset)
     * @return the name of the header to use for that field
     */
    fun getHeaderForField(
        fieldName: String,
        assetClass: Class<*> = Asset::class.java,
    ): String {
        return ReflectionCache.getDeserializedName(assetClass, fieldName)
    }
}
