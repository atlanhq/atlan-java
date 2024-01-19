/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import com.atlan.cache.ReflectionCache
import com.atlan.model.assets.Asset
import com.atlan.model.fields.AtlanField
import com.atlan.model.fields.SearchableField
import com.atlan.pkg.serde.RowDeserialization
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.serde.Serde
import mu.KLogger
import java.lang.reflect.InvocationTargetException
import kotlin.system.exitProcess

/**
 * Import assets into Atlan from a provided CSV file.
 *
 * Only the assets and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param filename name of the file to import
 * @param logger through which to record progress and any errors
 * @param typeNameFilter name of the types that should be processed (primarily useful for multi-pass loads)
 * @param attrsToOverwrite list of fields that should be overwritten in Atlan, if their value is empty in the CSV
 * @param updateOnly if true, only update an asset (first check it exists), if false allow upserts (create if it does not exist)
 * @param batchSize maximum number of records to save per API request
 * @param trackBatches if true, minimal details about every asset created or updated is tracked (if false, only counts of each are tracked)
 * @param caseSensitive (only applies when updateOnly is true) attempt to match assets case-sensitively (true) or case-insensitively (false)
 */
abstract class CSVImporter(
    private val filename: String,
    protected val logger: KLogger,
    protected val typeNameFilter: String = "",
    private val attrsToOverwrite: List<AtlanField> = listOf(),
    private val updateOnly: Boolean = false,
    private val batchSize: Int = 20,
    private val trackBatches: Boolean = true,
    private val caseSensitive: Boolean = true,
) : AssetGenerator {

    /**
     * Actually run the import.
     *
     * @param columnsToSkip (optional) columns in the CSV file to skip when loading (primarily useful for multi-pass loads)
     * @return details about the results of the import
     */
    open fun import(columnsToSkip: Set<String> = setOf()): ImportResults? {
        CSVReader(filename, updateOnly, trackBatches, caseSensitive).use { csv ->
            val start = System.currentTimeMillis()
            val results = csv.streamRows(this, batchSize, logger, columnsToSkip)
            logger.info { "Total time taken: ${System.currentTimeMillis() - start} ms" }
            if (results.anyFailures) {
                logger.error { "Some errors detected, failing the workflow." }
                exitProcess(1)
            }
            cacheCreated(results.primary.created ?: listOf())
            return results
        }
    }

    /** {@inheritDoc} */
    override fun buildFromRow(row: List<String>, header: List<String>, typeIdx: Int, qnIdx: Int, skipColumns: Set<String>): RowDeserialization? {
        // Deserialize the objects represented in that row (could be more than one due to flattening
        // of in particular things like READMEs and Links)
        if (includeRow(row, header, typeIdx, qnIdx)) {
            val typeName = typeNameFilter.ifBlank {
                row.getOrElse(typeIdx) { "" }
            }
            val qualifiedName = row.getOrElse(qnIdx) { "" }
            val deserializer = RowDeserializer(
                heading = header,
                row = row,
                typeIdx = typeIdx,
                qnIdx = qnIdx,
                typeName = typeName,
                qualifiedName = qualifiedName,
                logger = logger,
                skipColumns = skipColumns,
            )
            val assets = deserializer.getAssets(getBuilder(deserializer))
            if (assets != null) {
                val builder = assets.primary
                val candidate = builder.build()
                val identity = RowDeserialization.AssetIdentity(candidate.typeName, candidate.qualifiedName)
                // Then apply any field clearances based on attributes configured in the job
                for (field in attrsToOverwrite) {
                    clearField(field, candidate, builder)
                    // If there are no related assets
                    if (!assets.related.containsKey(field.atlanFieldName)) {
                        assets.delete.add(field)
                    }
                }
                return RowDeserialization(identity, builder, assets.related, assets.delete)
            }
        }
        return null
    }

    /** {@inheritDoc} */
    override fun includeRow(row: List<String>, header: List<String>, typeIdx: Int, qnIdx: Int): Boolean {
        return row[typeIdx] == typeNameFilter
    }

    /**
     * Check if the provided field should be cleared, and if so clear it.
     *
     * @param field to check if it is empty and should be cleared
     * @param candidate the asset on which to check whether the field is empty (or not)
     * @param builder the builder against which to clear the field
     * @return true if the field was cleared, false otherwise
     */
    internal fun clearField(field: AtlanField, candidate: Asset, builder: Asset.AssetBuilder<*, *>): Boolean {
        try {
            val getter = ReflectionCache.getGetter(
                Serde.getAssetClassForType(candidate.typeName),
                field.atlanFieldName,
            )
            val value = getter.invoke(candidate)
            if (value == null ||
                (Collection::class.java.isAssignableFrom(value.javaClass) && (value as Collection<*>).isEmpty())
            ) {
                builder.nullField(field.atlanFieldName)
                return true
            }
        } catch (e: ClassNotFoundException) {
            logger.error(
                "Unknown type {} — cannot clear {}.",
                candidate.typeName,
                field.atlanFieldName,
                e,
            )
        } catch (e: IllegalAccessException) {
            logger.error(
                "Unable to clear {} on: {}::{}",
                field.atlanFieldName,
                candidate.typeName,
                candidate.qualifiedName,
                e,
            )
        } catch (e: InvocationTargetException) {
            logger.error(
                "Unable to clear {} on: {}::{}",
                field.atlanFieldName,
                candidate.typeName,
                candidate.qualifiedName,
                e,
            )
        }
        return false
    }

    companion object {
        /**
         * Determine which (if any) attributes should be cleared (removed) if they are empty in the input file.
         *
         * @param attrNames the list of attribute names provided through the configuration
         * @param fileInfo a descriptor to qualify for which file the attributes are being set
         * @param logger through which to record information
         * @return parsed list of attribute names to be cleared
         */
        fun attributesToClear(attrNames: MutableList<String>, fileInfo: String, logger: KLogger): List<AtlanField> {
            if (attrNames.contains(Asset.CERTIFICATE_STATUS.atlanFieldName)) {
                attrNames.add(Asset.CERTIFICATE_STATUS_MESSAGE.atlanFieldName)
            }
            if (attrNames.contains(Asset.ANNOUNCEMENT_TYPE.atlanFieldName)) {
                attrNames.add(Asset.ANNOUNCEMENT_TITLE.atlanFieldName)
                attrNames.add(Asset.ANNOUNCEMENT_MESSAGE.atlanFieldName)
            }
            logger.info { "Adding attributes to be cleared, if blank (for $fileInfo): $attrNames" }
            val attrFields = mutableListOf<AtlanField>()
            for (name in attrNames) {
                attrFields.add(SearchableField(name, name))
            }
            return attrFields
        }
    }
}
