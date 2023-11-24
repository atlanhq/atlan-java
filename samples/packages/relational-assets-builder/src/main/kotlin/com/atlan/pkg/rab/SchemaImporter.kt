/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import com.atlan.model.assets.Asset
import com.atlan.model.assets.Schema
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.serde.RowDeserializer
import mu.KotlinLogging

/**
 * Import schemas into Atlan from a provided CSV file.
 *
 * Only the schemas and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param preprocessed details of the preprocessed CSV file
 * @param attrsToOverwrite list of fields that should be overwritten in Atlan, if their value is empty in the CSV
 * @param updateOnly if true, only update an asset (first check it exists), if false allow upserts (create if it does not exist)
 * @param batchSize maximum number of records to save per API request
 * @param connectionImporter that was used to import connections
 */
class SchemaImporter(
    private val preprocessed: Importer.PreprocessedCsv,
    private val attrsToOverwrite: List<AtlanField>,
    private val updateOnly: Boolean,
    private val batchSize: Int,
    private val connectionImporter: ConnectionImporter,
) : AssetImporter(
    preprocessed.preprocessedFile,
    attrsToOverwrite,
    updateOnly,
    batchSize,
    Schema.TYPE_NAME,
    KotlinLogging.logger {},
) {
    /** {@inheritDoc} */
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val name = deserializer.getValue(Schema.SCHEMA_NAME.atlanFieldName)?.let { it as String } ?: ""
        val connectionQN = connectionImporter.getBuilder(deserializer).build().qualifiedName
        val qnDetails = getQualifiedNameDetails(deserializer.row, deserializer.heading, typeNameFilter)
        val databaseQN = "$connectionQN/${qnDetails.parentPartialQN}"
        return Schema.creator(name, databaseQN)
            .tableCount(preprocessed.qualifiedNameToTableCount[qnDetails.uniqueQN]?.toInt())
            .viewCount(preprocessed.qualifiedNameToViewCount[qnDetails.uniqueQN]?.toInt())
    }
}
