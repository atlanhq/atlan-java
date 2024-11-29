/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import com.atlan.AtlanClient
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Schema
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.util.DeltaProcessor
import mu.KotlinLogging

/**
 * Import schemas into Atlan from a provided CSV file.
 *
 * Only the schemas and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param client connectivity to the Atlan tenant
 * @param delta the processor containing any details about file deltas
 * @param preprocessed details of the preprocessed CSV file
 * @param attrsToOverwrite list of fields that should be overwritten in Atlan, if their value is empty in the CSV
 * @param creationHandling what to do with assets that do not exist (create full, partial, or ignore)
 * @param batchSize maximum number of records to save per API request
 * @param connectionImporter that was used to import connections
 * @param trackBatches if true, minimal details about every asset created or updated is tracked (if false, only counts of each are tracked)
 * @param fieldSeparator character to use to separate fields (for example ',' or ';')
 * @param failOnErrors if true, fail if errors are encountered, otherwise continue processing
 */
class SchemaImporter(
    client: AtlanClient,
    private val delta: DeltaProcessor,
    private val preprocessed: Importer.Results,
    private val attrsToOverwrite: List<AtlanField>,
    private val creationHandling: AssetCreationHandling,
    private val batchSize: Int,
    private val connectionImporter: ConnectionImporter,
    trackBatches: Boolean,
    fieldSeparator: Char,
    private val failOnErrors: Boolean = true,
) : AssetImporter(
        client,
        delta,
        preprocessed.preprocessedFile,
        attrsToOverwrite,
        creationHandling,
        batchSize,
        Schema.TYPE_NAME,
        KotlinLogging.logger {},
        trackBatches,
        fieldSeparator,
        failOnErrors,
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
