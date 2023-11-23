/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import Importer.clearField
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.serde.RowDeserialization
import com.atlan.pkg.serde.RowDeserializer
import mu.KotlinLogging
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
 * @param attrsToOverwrite list of fields that should be overwritten in Atlan, if their value is empty in the CSV
 * @param updateOnly if true, only update an asset (first check it exists), if false allow upserts (create if it does not exist)
 * @param batchSize maximum number of records to save per API request
 */
class AssetImporter(
    private val filename: String,
    private val attrsToOverwrite: List<AtlanField>,
    private val updateOnly: Boolean,
    private val batchSize: Int,
) : AssetGenerator {
    private val logger = KotlinLogging.logger {}

    fun import() {
        CSVReader(filename, updateOnly).use { csv ->
            val start = System.currentTimeMillis()
            val anyFailures = csv.streamRows(this, batchSize, logger)
            logger.info { "Total time taken: ${System.currentTimeMillis() - start} ms" }
            if (anyFailures) {
                logger.error { "Some errors detected, failing the workflow." }
                exitProcess(1)
            }
        }
    }

    /**
     * Translate a row of CSV values into an asset object, overwriting any attributes that were empty
     * in the CSV with blank values, per the job configuration.
     *
     * @param row of values in the CSV
     * @param header names of columns (and their position) in the header of the CSV
     * @param typeIdx numeric index of the column containing the typeName of the asset in the row
     * @param qnIdx numeric index of the column containing the qualifiedName of the asset in the row
     * @param skipColumns columns to skip, i.e. that need to be processed in a later pass
     * @return the deserialized asset object(s)
     */
    override fun buildFromRow(row: List<String>, header: List<String>, typeIdx: Int, qnIdx: Int, skipColumns: Set<String>): RowDeserialization? {
        // Deserialize the objects represented in that row (could be more than one due to flattening
        // of in particular things like READMEs and Links)
        val assets = RowDeserializer(header, row, typeIdx, qnIdx, logger, skipColumns).getAssets()
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
        return null
    }

    /** {@inheritDoc} */
    override fun includeRow(row: List<String>, header: List<String>, typeIdx: Int, qnIdx: Int): Boolean {
        return true
    }
}
