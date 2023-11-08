/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde

import com.atlan.cache.ReflectionCache
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Asset.AssetBuilder
import com.atlan.model.core.CustomMetadataAttributes
import com.atlan.model.core.CustomMetadataAttributes.CustomMetadataAttributesBuilder
import com.atlan.model.fields.AtlanField
import com.atlan.model.fields.CustomMetadataField
import com.atlan.pkg.serde.cell.AssetRefXformer
import com.atlan.pkg.serde.cell.CellXformer
import com.atlan.pkg.serde.cell.CellXformer.LIST_DELIMITER
import com.atlan.serde.Serde
import mu.KotlinLogging
import java.lang.reflect.Method
import java.util.concurrent.ThreadLocalRandom

private val log = KotlinLogging.logger {}

const val CM_HEADING_DELIMITER = "::"

/**
 * Retrieve the name to use for the header for a particular field.
 *
 * @param field for which to determine the header name
 * @return the name of the header to use for that field
 */
fun getHeaderForField(field: AtlanField): String {
    return getHeaderForField(field, Asset::class.java)
}

/**
 * Retrieve the name to use for the header for a particular field, assuming a particular type of asset.
 *
 * @param field for which to determine the header name
 * @param assetClass asset class in which to assume the field is defined
 * @return the name of the header to use for that field
 */
fun getHeaderForField(
    field: AtlanField,
    assetClass: Class<*>,
): String {
    return if (field is CustomMetadataField) {
        // For custom metadata, translate the header to human-readable names
        field.setName + CM_HEADING_DELIMITER + field.attributeName
    } else {
        // Use renamed fields for deserialization, if available
        ReflectionCache.getDeserializedName(assetClass, field.atlanFieldName)
    }
}

/**
 * Class to generally serialize an asset object into a row of tabular data.
 * Note: this will always serialize the qualifiedName and type of the asset as the first two columns.
 *
 * @param asset the asset to be serialized
 * @param fields the full list of fields to be serialized from the asset, in the order they should be serialized
 */
class RowSerializer(private val asset: Asset, private val fields: List<AtlanField>) {
    /**
     * Actually serialize the provided inputs into a list of string values.
     *
     * @return the list of string values giving a row-based tabular representation of the asset
     */
    fun getRow(): Iterable<String> {
        val row = mutableListOf<String>()
        row.add(FieldSerde.getValueForField(asset, Asset.QUALIFIED_NAME))
        row.add(FieldSerde.getValueForField(asset, Asset.TYPE_NAME))
        for (field in fields) {
            if (field != Asset.QUALIFIED_NAME && field != Asset.TYPE_NAME) {
                row.add(FieldSerde.getValueForField(asset, field))
            }
        }
        return row
    }
}

data class AssetIdentity(val typeName: String, val qualifiedName: String)

data class RowDeserialization(
    val identity: AssetIdentity,
    val primary: AssetBuilder<*, *>,
    /** Field name > related asset */
    val related: MutableMap<String, Asset> = mutableMapOf(),
    /** Asset identity > relationship fields */
    val delete: MutableSet<AtlanField> = mutableSetOf(),
)

/**
 * Class to generally deserialize an asset object from a row of tabular data.
 * Note: at least the qualifiedName and type of the asset must be present in every row.
 *
 * @param heading the list of field names, in the order they appear as columns in the tabular data
 * @param row values for each field in a single row, representing a single asset
 * @param typeIdx the numeric index for the type in the list of columns
 * @param qnIdx the numeric index for the qualifiedName in the list of columns
 */
class RowDeserializer(private val heading: List<String>, private val row: List<String>, private val typeIdx: Int, private val qnIdx: Int) {
    /**
     * Actually deserialize the provided inputs into a builder for an asset object.
     *
     * @return the builders, for the primary asset object (already-populated with the metadata from the row of tabular data)
     *         and any related asset builders (for example, for READMEs, Links, or other assets that were denormalized in the tabular form)
     */
    fun getAssets(): RowDeserialization? {
        val typeName = row.getOrElse(typeIdx) { "" }
        val qualifiedName = row.getOrElse(qnIdx) { "" }
        if (typeName == "" || qualifiedName == "") {
            log.warn("No qualifiedName or typeName found on row, cannot deserialize: {}", row)
        } else {
            val assetClass = Serde.getAssetClassForType(typeName)
            val method = assetClass.getMethod("_internal")
            val builder = method.invoke(null) as AssetBuilder<*, *>
            builder.guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            val deserialization = RowDeserialization(AssetIdentity(typeName, qualifiedName), builder)
            val customMetadataMap = mutableMapOf<String, CustomMetadataAttributesBuilder<*, *>>()
            for (i in heading.indices) {
                val fieldName = heading[i]
                if (fieldName.isNotEmpty()) {
                    val rValue = row[i]
                    if (fieldName.contains(CM_HEADING_DELIMITER)) {
                        // Custom metadata field...
                        val tokens = fieldName.split(CM_HEADING_DELIMITER)
                        val setName = tokens[0]
                        val attrName = tokens[1]
                        if (!customMetadataMap.containsKey(setName)) {
                            customMetadataMap[setName] = CustomMetadataAttributes.builder()
                        }
                        val value: Any? = FieldSerde.getCustomMetadataValueFromString(rValue)
                        customMetadataMap[setName]!!.attribute(attrName, value)
                    } else {
                        // "Normal" field...
                        val setter = ReflectionCache.getSetter(builder.javaClass, fieldName)
                        if (setter != null) {
                            val value = FieldSerde.getValueFromCell(rValue, setter)
                            if (value != null) {
                                if (AssetRefXformer.requiresHandling(value)) {
                                    deserialization.related[fieldName] = value as Asset
                                } else {
                                    // Only set the value on the asset directly if it does not require
                                    // special handling, otherwise leave it to the special handling
                                    // to set the value (later)
                                    ReflectionCache.setValue(builder, fieldName, value)
                                }
                            }
                        }
                    }
                }
            }
            if (customMetadataMap.isNotEmpty()) {
                for ((key, value) in customMetadataMap) {
                    builder.customMetadata(key, value.build())
                }
            }
            return deserialization
        }
        return null
    }
}

/**
 * Static object to (de)serialize a single field's value for a given asset.
 */
private object FieldSerde {
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
        } else if (value.contains(LIST_DELIMITER)) {
            getMultiValuedCustomMetadata(value)
        } else {
            value
        }
    }

    private fun getMultiValuedCustomMetadata(value: String?): List<String> {
        return if (value.isNullOrEmpty()) {
            listOf()
        } else {
            value.split(LIST_DELIMITER)
        }
    }
}
