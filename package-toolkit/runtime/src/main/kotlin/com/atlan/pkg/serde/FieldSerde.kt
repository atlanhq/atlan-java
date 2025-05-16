/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde

import com.atlan.cache.ReflectionCache
import com.atlan.model.assets.Asset
import com.atlan.model.fields.AtlanField
import com.atlan.model.fields.CustomMetadataField
import com.atlan.model.typedefs.AttributeDef
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.cell.CellXformer
import com.atlan.pkg.serde.cell.TimestampXformer
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
     * @param ctx context in which the custom package is running
     * @param asset from which to serialize the value
     * @param field attribute within the asset to serialize
     * @param logger through which to record any errors
     * @return the serialized form of that field's value, from that asset
     */
    fun getValueForField(
        ctx: PackageContext<*>,
        asset: Asset,
        field: AtlanField,
        logger: KLogger,
    ): String {
        val value: Any?
        val dates: Boolean
        if (field is CustomMetadataField) {
            value = asset.getCustomMetadata(field.setName, field.attributeName)
            val attrId = ctx.client.customMetadataCache.getAttrIdForName(field.setName, field.attributeName)
            val attrDef = ctx.client.customMetadataCache.getAttributeDef(attrId)
            dates = attrDef.typeName.lowercase() == "date"
        } else {
            val deserializedName = ReflectionCache.getDeserializedName(asset.javaClass, field.atlanFieldName)
            value = ReflectionCache.getValue(asset, deserializedName)
            // TODO: confirm whether we should use this field name or the deserializedName
            dates = ReflectionCache.isDate(asset.javaClass, field.atlanFieldName)
        }
        return try {
            CellXformer.encode(ctx, value, field, asset.guid, dates)
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
     * @param ctx context in which the custom package is running
     * @param value the single field's value
     * @param setter the method on the asset that will be used to set the value onto the object
     * @param logger through which to record any errors
     * @return the deserialized form of that field's value
     */
    fun getValueFromCell(
        ctx: PackageContext<*>,
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
            val assetClass = setter.declaringClass.enclosingClass
            CellXformer.decode(ctx, assetClass, value, paramClass, innerClass, fieldName, logger)
        } catch (e: Exception) {
            if (FAIL_ON_ERRORS.get()) {
                throw e
            } else {
                logger.warn { "Unable to decode value from field -- skipping ${setter.name}: $value" }
                logger.debug(e) { "Full stack trace:" }
            }
            null
        }
    }

    /**
     * Deserialize a single field's value from a row of tabular data, when that field
     * is stored as custom metadata.
     *
     * @param ctx context in which the package is running
     * @param attrDef attribute definition of the field
     * @param value the single field's value
     * @return the deserialized form of that field's value
     */
    fun getCustomMetadataValueFromString(
        ctx: PackageContext<*>,
        attrDef: AttributeDef,
        value: String?,
    ): Any? =
        if (value.isNullOrEmpty()) {
            null
        } else if (attrDef.options?.multiValueSelect == true) {
            getMultiValuedCustomMetadata(ctx, attrDef, value)
        } else {
            getSingleValuedCustomMetadata(ctx, attrDef, value)
        }

    private fun getMultiValuedCustomMetadata(
        ctx: PackageContext<*>,
        attrDef: AttributeDef,
        value: String?,
    ): List<String> =
        if (value.isNullOrEmpty()) {
            listOf()
        } else {
            value.split(CellXformer.LIST_DELIMITER).map { getSingleValuedCustomMetadata(ctx, attrDef, it.trim()).toString() }
        }

    private fun getSingleValuedCustomMetadata(
        ctx: PackageContext<*>,
        attrDef: AttributeDef,
        value: String?,
    ): Any? {
        if (value == null) {
            return null
        }
        return when (attrDef.basicType.lowercase()) {
            "boolean" -> CellXformer.decodeBoolean(value)
            "int" -> CellXformer.decodeInt(value)
            "date" -> TimestampXformer.decode(value, "unused")
            "long" -> CellXformer.decodeLong(value)
            "float" -> CellXformer.decodeDouble(value)
            else -> CellXformer.decodeString(ctx, value)
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
    fun getRefByQualifiedName(
        typeName: String,
        qualifiedName: String,
    ): Asset {
        val assetClass = Serde.getAssetClassForType(typeName)
        val method = assetClass.getMethod("refByQualifiedName", String::class.java)
        return (method.invoke(null, qualifiedName) as Asset)
    }
}
