/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde

import com.atlan.Atlan
import com.atlan.cache.ReflectionCache
import com.atlan.model.assets.Asset
import com.atlan.model.fields.AtlanField
import com.atlan.model.fields.CustomMetadataField
import com.atlan.pkg.serde.cell.CellXformer
import com.atlan.serde.Serde
import mu.KLogger
import java.lang.reflect.Method
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Static object to (de)serialize a single field's value for a given asset.
 */
object FieldSerde {
    val FAIL_ON_ERRORS = AtomicBoolean(true)

    /**
     * Serialize a single field's value from an asset object.
     *
     * @param asset from which to serialize the value
     * @param field attribute within the asset to serialize
     * @param logger through which to record any errors
     * @return the serialized form of that field's value, from that asset
     */
    fun getValueForField(
        asset: Asset,
        field: AtlanField,
        logger: KLogger,
    ): String {
        val value: Any?
        val dates: Boolean
        if (field is CustomMetadataField) {
            val client = Atlan.getDefaultClient()
            value = asset.getCustomMetadata(field.setName, field.attributeName)
            val attrId = client.customMetadataCache.getAttrIdForName(field.setName, field.attributeName)
            val attrDef = client.customMetadataCache.getAttributeDef(attrId)
            dates = attrDef.typeName.lowercase() == "date"
        } else {
            val deserializedName = ReflectionCache.getDeserializedName(asset.javaClass, field.atlanFieldName)
            value = ReflectionCache.getValue(asset, deserializedName)
            // TODO: confirm whether we should use this field name or the deserializedName
            dates = ReflectionCache.isDate(asset.javaClass, field.atlanFieldName)
        }
        return try {
            CellXformer.encode(value, asset.guid, dates)
        } catch (e: Exception) {
            if (FAIL_ON_ERRORS.get()) {
                throw e
            } else {
                logger.warn("Unable to encode value for field -- skipping {}: {}", field, asset)
                logger.debug("Full stack trace:", e)
            }
            ""
        }
    }

    /**
     * Deserialize a single field's value from a row of tabular data.
     *
     * @param value the single field's value
     * @param setter the method on the asset that will be used to set the value onto the object
     * @param logger through which to record any errors
     * @return the deserialized form of that field's value
     */
    fun getValueFromCell(
        value: String,
        setter: Method,
        logger: KLogger,
    ): Any? {
        val paramClass = ReflectionCache.getParameterOfMethod(setter)
        var innerClass: Class<*>? = null
        val fieldName = setter.name
        if (Collection::class.java.isAssignableFrom(paramClass) || Map::class.java.isAssignableFrom(paramClass)) {
            val paramType = ReflectionCache.getParameterizedTypeOfMethod(setter)
            innerClass = ReflectionCache.getClassOfParameterizedType(paramType)
        }
        return try {
            CellXformer.decode(value, paramClass, innerClass, fieldName)
        } catch (e: Exception) {
            if (FAIL_ON_ERRORS.get()) {
                throw e
            } else {
                logger.warn("Unable to decode value from field -- skipping {}: {}", setter.name, value)
                logger.debug("Full stack trace:", e)
            }
            null
        }
    }

    /**
     * Deserialize a single field's value from a row of tabular data, when that field
     * is stored as custom metadata.
     *
     * @param value the single field's value
     * @param multiValued true if the custom attribute allows multiple values, otherwise false
     * @return the deserialized form of that field's value
     */
    fun getCustomMetadataValueFromString(value: String?, multiValued: Boolean): Any? {
        return if (value.isNullOrEmpty()) {
            null
        } else if (multiValued) {
            getMultiValuedCustomMetadata(value)
        } else {
            value
        }
    }

    private fun getMultiValuedCustomMetadata(value: String?): List<String> {
        return if (value.isNullOrEmpty()) {
            listOf()
        } else {
            value.split(CellXformer.LIST_DELIMITER).map { it.trim() }
        }
    }

    /**
     * Get a builder for the provided asset type.
     *
     * @param typeName name of the asset type for which to get a builder
     * @return a builder for that asset type
     */
    fun getBuilderForType(typeName: String): Asset.AssetBuilder<*, *> {
        val assetClass = Serde.getAssetClassForType(typeName)
        val method = assetClass.getMethod("_internal")
        return (method.invoke(null) as Asset.AssetBuilder<*, *>)
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
    }

    /**
     * Get a relationship by qualifiedName for the provided asset type.
     *
     * @param typeName name of the asset type for which to get a relationship
     * @param qualifiedName of the asset for which to get a relationship
     * @return a relationship reference for that asset
     */
    fun getRefByQualifiedName(typeName: String, qualifiedName: String): Asset {
        val assetClass = Serde.getAssetClassForType(typeName)
        val method = assetClass.getMethod("refByQualifiedName", String::class.java)
        return (method.invoke(null, qualifiedName) as Asset)
    }
}
