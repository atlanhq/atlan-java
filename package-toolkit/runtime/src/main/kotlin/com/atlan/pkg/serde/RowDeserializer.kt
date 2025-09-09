/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde

import com.atlan.cache.ReflectionCache
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Asset.AssetBuilder
import com.atlan.model.core.CustomMetadataAttributes
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.FieldSerde.FAIL_ON_ERRORS
import com.atlan.pkg.serde.RowSerde.CM_HEADING_DELIMITER
import com.atlan.pkg.serde.cell.AssetRefXformer
import com.atlan.pkg.serde.cell.DataDomainXformer
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.serde.Serde
import com.atlan.util.AssetBatch
import mu.KLogger
import java.util.concurrent.ThreadLocalRandom

/**
 * Class to generally deserialize an asset object from a row of tabular data.
 * Note: at least the qualifiedName and type of the asset must be present in every row.
 *
 * @param ctx context in which the package is running
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
    private val ctx: PackageContext<*>,
    val heading: List<String>,
    val row: List<String>,
    private val typeIdx: Int = -1,
    private val qnIdx: Int = -1,
    val typeName: String = CSVXformer.trimWhitespace(row.getOrElse(typeIdx) { "" }),
    val qualifiedName: String = CSVXformer.trimWhitespace(row.getOrElse(qnIdx) { "" }),
    private val logger: KLogger,
    private val skipColumns: Set<String>,
) {
    /**
     * Actually deserialize the provided inputs into a builder for an asset object.
     *
     * @return the builders, for the primary asset object (already-populated with the metadata from the row of tabular data)
     *         and any related asset builders (for example, for READMEs, Links, or other assets that were denormalized in the tabular form)
     */
    fun getAssets(): RowDeserialization? {
        if (typeName.isBlank() || qualifiedName.isBlank()) {
            logger.warn("No qualifiedName or typeName found on row, cannot deserialize: {}", row)
        } else {
            val builder =
                FieldSerde
                    .getBuilderForType(typeName)
                    .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                    .qualifiedName(qualifiedName)
            return getAssets(builder)
        }
        return null
    }

    /**
     * Actually deserialize the provided inputs into a builder for an asset object.
     *
     * @param builder for the primary asset represented by the row
     * @return the builders, for the primary asset object (already-populated with the metadata from the row of tabular data)
     *         and any related asset builders (for example, for READMEs, Links, or other assets that were denormalized in the tabular form)
     */
    @Suppress("UNCHECKED_CAST")
    fun getAssets(builder: AssetBuilder<*, *>): RowDeserialization? {
        val partial = builder.build()
        if (partial.typeName.isNullOrBlank() || partial.qualifiedName.isNullOrBlank()) {
            logger.warn("No qualifiedName or typeName found in builder, cannot deserialize: {}", row)
        } else {
            val deserialization = RowDeserialization(AssetBatch.AssetIdentity(partial.typeName, partial.qualifiedName), builder)
            val customMetadataMap = mutableMapOf<String, CustomMetadataAttributes.CustomMetadataAttributesBuilder<*, *>>()
            for (i in heading.indices) {
                val fieldName = heading[i]
                if (!skipColumns.contains(fieldName)) {
                    if (fieldName.isNotEmpty()) {
                        val value = getValue(fieldName)
                        if (fieldName.contains(CM_HEADING_DELIMITER) && value != null) {
                            // Custom metadata field...
                            val tokens = fieldName.split(CM_HEADING_DELIMITER)
                            val setName = tokens[0]
                            val attrName = tokens[1]
                            if (!customMetadataMap.containsKey(setName)) {
                                customMetadataMap[setName] = CustomMetadataAttributes.builder()
                            }
                            customMetadataMap[setName]!!.attribute(attrName, value)
                        } else if (value != null) {
                            // "Normal" field...
                            val setter = ReflectionCache.getSetter(builder.javaClass, fieldName)
                            if (setter != null) {
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
                                    if (fieldName == Asset.DOMAIN_GUIDS.atlanFieldName) {
                                        if (value is ArrayList<*> && value.isNotEmpty() && value[0] is String) {
                                            val dataDomain = DataDomainXformer.decode(ctx, value[0] as String, fieldName)
                                            ReflectionCache.setValue(builder, fieldName, listOf(dataDomain.guid))
                                        }
                                    } else {
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

    /**
     * Retrieve the decoded cell value for the specified field -- AFTER it has been transformed.
     *
     * @param fieldName of the field to retrieve
     * @return the decoded value for that cell
     */
    fun getValue(fieldName: String): Any? {
        val rValue = getRawValue(fieldName)
        return if (fieldName.contains(CM_HEADING_DELIMITER)) {
            // Custom metadata field...
            val cache = ctx.client.customMetadataCache
            val tokens = fieldName.split(CM_HEADING_DELIMITER)
            val setName = tokens[0]
            val attrName = tokens[1]
            val attrId =
                try {
                    cache.getAttrIdForName(setName, attrName)
                } catch (e: Exception) {
                    if (FAIL_ON_ERRORS.get()) {
                        throw e
                    } else {
                        logger.warn { "Unable to find specified attribute, will skip its value: $fieldName" }
                        logger.debug(e) { "Full stack trace:" }
                    }
                    null
                }
            if (attrId != null) {
                val attrDef = cache.getAttributeDef(attrId)
                return FieldSerde.getCustomMetadataValueFromString(ctx, setName, attrDef, rValue, logger)
            } else {
                // If we cannot translate via the attribute def, return no value
                null
            }
        } else {
            // "Normal" field...
            val setter = ReflectionCache.getSetter(Serde.getBuilderClassForType(typeName), fieldName)
            if (setter != null) {
                FieldSerde.getValueFromCell(ctx, rValue, setter, logger)
            } else {
                // If this isn't a "real" field, return it as a simple string
                rValue
            }
        }
    }

    /**
     * Retrieve the raw cell value for the specified field -- without ANY decoding applied to it.
     *
     * @param fieldName of the field to retrieve
     * @return the raw, un-decoded value for that cell
     */
    fun getRawValue(fieldName: String): String {
        if (fieldName.isNotBlank()) {
            val i = heading.indexOf(fieldName)
            if (i >= 0) {
                return CSVXformer.trimWhitespace(row[i])
            }
        }
        return ""
    }
}
