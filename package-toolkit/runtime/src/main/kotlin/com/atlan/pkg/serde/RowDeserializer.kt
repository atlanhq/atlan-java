/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde

import com.atlan.cache.ReflectionCache
import com.atlan.model.assets.Asset
import com.atlan.model.core.CustomMetadataAttributes
import com.atlan.pkg.serde.RowSerde.CM_HEADING_DELIMITER
import com.atlan.pkg.serde.cell.AssetRefXformer
import mu.KLogger
import java.util.concurrent.ThreadLocalRandom

/**
 * Class to generally deserialize an asset object from a row of tabular data.
 * Note: at least the qualifiedName and type of the asset must be present in every row.
 *
 * @param heading the list of field names, in the order they appear as columns in the tabular data
 * @param row values for each field in a single row, representing a single asset
 * @param typeIdx the numeric index for the type in the list of columns
 * @param qnIdx the numeric index for the qualifiedName in the list of columns
 * @param typeName the name of the type of asset on the row (calculated from typeIdx and row, if not specified)
 * @param qualifiedName the qualifiedName of the asset on the row (calculated from qnIdx and row, if not specified)
 * @param logger through which to record any problems
 * @param skipColumns columns to skip, i.e. that need to be processed in a later pass
 */
class RowDeserializer(
    private val heading: List<String>,
    private val row: List<String>,
    private val typeIdx: Int = -1,
    private val qnIdx: Int = -1,
    private val typeName: String = row.getOrElse(typeIdx) { "" },
    private val qualifiedName: String = row.getOrElse(qnIdx) { "" },
    private val logger: KLogger,
    private val skipColumns: Set<String>,
) {
    /**
     * Actually deserialize the provided inputs into a builder for an asset object.
     *
     * @return the builders, for the primary asset object (already-populated with the metadata from the row of tabular data)
     *         and any related asset builders (for example, for READMEs, Links, or other assets that were denormalized in the tabular form)
     */
    @Suppress("UNCHECKED_CAST")
    fun getAssets(): RowDeserialization? {
        if (typeName.isBlank() || qualifiedName.isBlank()) {
            logger.warn("No qualifiedName or typeName found on row, cannot deserialize: {}", row)
        } else {
            val builder = FieldSerde.getBuilderForType(typeName)
            builder.guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            val deserialization = RowDeserialization(RowDeserialization.AssetIdentity(typeName, qualifiedName), builder)
            val customMetadataMap = mutableMapOf<String, CustomMetadataAttributes.CustomMetadataAttributesBuilder<*, *>>()
            for (i in heading.indices) {
                val fieldName = heading[i]
                if (!skipColumns.contains(fieldName)) {
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
                                val value = FieldSerde.getValueFromCell(rValue, setter, logger)
                                if (value != null) {
                                    if (AssetRefXformer.requiresHandling(fieldName, value)) {
                                        if (value is Collection<*>) {
                                            deserialization.related[fieldName] = value as Collection<Asset>
                                        } else {
                                            deserialization.related[fieldName] = listOf(value as Asset)
                                        }
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
