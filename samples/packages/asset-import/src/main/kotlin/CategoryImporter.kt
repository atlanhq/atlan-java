/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import Importer.clearField
import com.atlan.model.assets.Asset
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.cache.CategoryCache
import com.atlan.pkg.serde.RowDeserialization
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.cell.GlossaryCategoryXformer
import com.atlan.pkg.serde.cell.GlossaryXformer
import mu.KotlinLogging
import kotlin.math.max
import kotlin.system.exitProcess

/**
 * Import categories (only) into Atlan from a provided CSV file.
 *
 * Only the categories and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param filename name of the file to import
 * @param attrsToOverwrite list of fields that should be overwritten in Atlan, if their value is empty in the CSV
 * @param updateOnly if true, only update an asset (first check it exists), if false allow upserts (create if it does not exist)
 * @param batchSize maximum number of records to save per API request
 */
class CategoryImporter(
    private val filename: String,
    private val attrsToOverwrite: List<AtlanField>,
    private val updateOnly: Boolean,
    private val batchSize: Int,
) : GTCImporter(
    filename = filename,
    attrsToOverwrite = attrsToOverwrite,
    updateOnly = updateOnly,
    batchSize = batchSize,
    cache = CategoryCache,
    typeNameFilter = GlossaryCategory.TYPE_NAME,
) {
    private val logger = KotlinLogging.logger {}
    private var levelToProcess = 0
    private var foundAny = true

    /** {@inheritDoc} */
    override fun import() {
        cache.preload()
        // Import categories by level, top-to-bottom, and stop when we hit a level with no categories
        logger.info("Loading categories in multiple passes, by level...")
        while (foundAny) {
            foundAny = false
            levelToProcess += 1
            logger.info("--- Loading level {} categories... ---", levelToProcess)
            CSVReader(filename, updateOnly).use { csv ->
                val start = System.currentTimeMillis()
                val anyFailures = csv.streamRows(this, batchSize, logger)
                logger.info("Total time taken: {} ms", System.currentTimeMillis() - start)
                if (anyFailures) {
                    logger.error("Some errors detected, failing the workflow.")
                    exitProcess(1)
                }
                cacheCreated(csv.created)
            }
        }
    }

    /** {@inheritDoc} */
    override fun cacheCreated(map: Map<String, Asset>) {
        lookupAndCache(map)
    }

    /**
     * Translate a row of CSV values into a term object, overwriting any attributes that were empty
     * in the CSV with blank values, per the job configuration.
     *
     * @param row of values in the CSV
     * @param header names of columns (and their position) in the header of the CSV
     * @param typeIdx numeric index of the column containing the typeName of the asset in the row
     * @param qnIdx numeric index of the column containing the qualifiedName of the asset in the row
     * @return the deserialized asset object(s)
     */
    override fun buildFromRow(row: List<String>, header: List<String>, typeIdx: Int, qnIdx: Int): RowDeserialization? {
        if (includeRow(row, header, typeIdx, qnIdx)) {
            val cacheId = getFallbackQualifiedName(row, header, typeIdx, qnIdx)
            logger.info(" ... processing: {}", cacheId)
            val qualifiedName = cache.getByIdentity(cacheId)?.qualifiedName ?: cacheId
            // Deserialize the objects represented in that row (could be more than one due to flattening
            // of in particular things like READMEs and Links)
            val assets = RowDeserializer(header, row, typeIdx, qnIdx, qualifiedName).getAssets()
            if (assets != null) {
                val builder = assets.primary
                val candidate = builder.build()
                candidate as GlossaryCategory
                if (qualifiedName == cacheId) {
                    // Cache miss, so this category will be created and thus needs to be cached after processing
                    // this level
                    builder.qualifiedName(cacheId)
                }
                val identity = RowDeserialization.AssetIdentity(candidate.typeName, qualifiedName)
                // Then apply any field clearances based on attributes configured in the job
                for (field in attrsToOverwrite) {
                    clearField(field, candidate, builder)
                    // If there are no related assets
                    if (!assets.related.containsKey(field.atlanFieldName)) {
                        assets.delete.add(field)
                    }
                }
                foundAny = true
                return RowDeserialization(identity, builder, assets.related, assets.delete)
            }
        }
        return null
    }

    /** {@inheritDoc} */
    override fun includeRow(row: List<String>, header: List<String>, typeIdx: Int, qnIdx: Int): Boolean {
        val nameIdx = header.indexOf(GlossaryCategory.NAME.atlanFieldName)
        val parentIdx = header.indexOf(GlossaryCategory.PARENT_CATEGORY.atlanFieldName)
        val anchorIdx = header.indexOf(GlossaryCategory.ANCHOR.atlanFieldName)

        val maxBound = max(typeIdx, max(nameIdx, max(parentIdx, anchorIdx)))
        if (maxBound > row.size || row[typeIdx] != typeNameFilter) {
            // If any of the columns are beyond the size of the row, or the row
            // represents something other than a category, short-circuit
            return false
        }
        val categoryLevel = if (row[parentIdx].isBlank()) {
            1
        } else {
            val parentPath = row[parentIdx].split(GlossaryXformer.GLOSSARY_DELIMITER)[0]
            parentPath.split(GlossaryCategoryXformer.CATEGORY_DELIMITER).size + 1
        }
        if (categoryLevel != levelToProcess) {
            // If this category is a different level than we are currently processing,
            // short-circuit
            return false
        }
        return row[typeIdx] == typeNameFilter
    }

    /** {@inheritDoc} */
    override fun getFallbackQualifiedName(row: List<String>, header: List<String>, typeIdx: Int, qnIdx: Int): String {
        val nameIdx = header.indexOf(GlossaryCategory.NAME.atlanFieldName)
        val parentIdx = header.indexOf(GlossaryCategory.PARENT_CATEGORY.atlanFieldName)
        val anchorIdx = header.indexOf(GlossaryCategory.ANCHOR.atlanFieldName)
        val glossaryName = row[anchorIdx]
        val categoryPath = if (row[parentIdx].isBlank()) {
            row[nameIdx]
        } else {
            "${row[parentIdx]}${GlossaryCategoryXformer.CATEGORY_DELIMITER}${row[nameIdx]}"
        }
        return "$categoryPath${GlossaryXformer.GLOSSARY_DELIMITER}$glossaryName"
    }
}
