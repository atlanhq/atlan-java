/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde

import com.atlan.cache.ReflectionCache
import com.atlan.model.assets.Asset
import com.atlan.model.fields.AtlanField
import com.atlan.model.fields.CustomMetadataField
import com.atlan.pkg.serde.cell.CellXformer
import java.lang.reflect.Method

/**
 * Static object to (de)serialize a single field's value for a given asset.
 */
internal object FieldSerde {
    /**
     * Serialize a single field's value from an asset object.
     *
     * @param asset from which to serialize the value
     * @param field attribute within the asset to serialize
     * @return the serialized form of that field's value, from that asset
     */
    fun getValueForField(
        asset: Asset,
        field: AtlanField,
    ): String {
        val value =
            if (field is CustomMetadataField) {
                asset.getCustomMetadata(field.setName, field.attributeName)
            } else {
                val deserializedName = ReflectionCache.getDeserializedName(asset.javaClass, field.atlanFieldName)
                ReflectionCache.getValue(asset, deserializedName)
            }
        return CellXformer.encode(asset.guid, value)
    }

    /**
     * Deserialize a single field's value from a row of tabular data.
     *
     * @param value the single field's value
     * @param setter the method on the asset that will be used to set the value onto the object
     * @return the deserialized form of that field's value
     */
    fun getValueFromCell(
        value: String,
        setter: Method,
    ): Any? {
        val paramClass = ReflectionCache.getParameterOfMethod(setter)
        var innerClass: Class<*>? = null
        val fieldName = setter.name
        if (Collection::class.java.isAssignableFrom(paramClass) || Map::class.java.isAssignableFrom(paramClass)) {
            val paramType = ReflectionCache.getParameterizedTypeOfMethod(setter)
            innerClass = ReflectionCache.getClassOfParameterizedType(paramType)
        }
        return CellXformer.decode(value, paramClass, innerClass, fieldName)
    }

    /**
     * Deserialize a single field's value from a row of tabular data, when that field
     * is stored as custom metadata.
     *
     * @param value the single field's value
     * @return the deserialized form of that field's value
     */
    fun getCustomMetadataValueFromString(value: String?): Any? {
        return if (value.isNullOrEmpty()) {
            null
        } else if (value.contains(CellXformer.LIST_DELIMITER)) {
            getMultiValuedCustomMetadata(value)
        } else {
            value
        }
    }

    private fun getMultiValuedCustomMetadata(value: String?): List<String> {
        return if (value.isNullOrEmpty()) {
            listOf()
        } else {
            value.split(CellXformer.LIST_DELIMITER)
        }
    }
}
