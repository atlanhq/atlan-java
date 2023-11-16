/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.cache.ReflectionCache
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Asset.AssetBuilder
import com.atlan.model.fields.AtlanField
import com.atlan.model.fields.SearchableField
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.MultiSelectDeserializer
import com.atlan.pkg.serde.RowDeserialization
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.cell.AssetRefXformer
import com.atlan.serde.Serde
import mu.KotlinLogging
import java.lang.reflect.InvocationTargetException
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}

/**
 * Actually run the importer.
 * Note: all parameters should be passed through environment variables.
 */
fun main() {
    val config = Utils.setPackageOps<AssetImportCfg>()
    val importer = Importer(config)
    importer.import()
}

/**
 * Import assets into Atlan from a provided CSV file.
 *
 * Only the assets and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param config configuration to use for the importer, typically driven by environment variables
 */
class Importer(private val config: AssetImportCfg) : AssetGenerator {

    private val batchSize = Utils.getOrDefault(config.batchSize, 50)
    private val filename = Utils.getOrDefault(config.uploadedFile, "")
    private val updateOnly = Utils.getOrDefault(config.upsertSemantic, "update") == "update"
    private val attrsToOverwrite = attributesToClear()

    fun import() {
        CSVReader(filename, updateOnly).use { csv ->
            val start = System.currentTimeMillis()
            val anyFailures = csv.streamRows(this, batchSize, logger)
            logger.info("Total time taken: {} ms", System.currentTimeMillis() - start)
            if (anyFailures) {
                logger.error("Some errors detected, failing the workflow.")
                exitProcess(1)
            }
        }
    }

    /**
     * Determine which (if any) attributes should be cleared (removed) if they are empty in the input file.
     *
     * @return parsed list of attribute names to be cleared
     */
    private fun attributesToClear(): List<AtlanField> {
        val attrNames: MutableList<String> = MultiSelectDeserializer.deserialize(Utils.getEnvVar("ATTR_TO_OVERWRITE", "")).toMutableList()
        if (attrNames.contains(Asset.CERTIFICATE_STATUS.atlanFieldName)) {
            attrNames.add(Asset.CERTIFICATE_STATUS_MESSAGE.atlanFieldName)
        }
        if (attrNames.contains(Asset.ANNOUNCEMENT_TYPE.atlanFieldName)) {
            attrNames.add(Asset.ANNOUNCEMENT_TITLE.atlanFieldName)
            attrNames.add(Asset.ANNOUNCEMENT_MESSAGE.atlanFieldName)
        }
        logger.info("Adding attributes to be cleared, if blank: {}", attrNames)
        val attrFields = mutableListOf<AtlanField>()
        for (name in attrNames) {
            attrFields.add(SearchableField(name, name))
        }
        return attrFields
    }

    /**
     * Translate a row of CSV values into an asset object, overwriting any attributes that were empty
     * in the CSV with blank values, per the job configuration.
     *
     * @param row of values in the CSV
     * @param header names of columns (and their position) in the header of the CSV
     * @param typeIdx numeric index of the column containing the typeName of the asset in the row
     * @param qnIdx numeric index of the column containing the qualifiedName of the asset in the row
     * @return the deserialized asset object(s)
     */
    override fun buildFromRow(row: List<String>, header: List<String>, typeIdx: Int, qnIdx: Int): RowDeserialization? {
        // Deserialize the objects represented in that row (could be more than one due to flattening
        // of in particular things like READMEs and Links)
        val assets = RowDeserializer(header, row, typeIdx, qnIdx).getAssets()
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

    /**
     * Build a complete related asset object from the provided asset and (partial) related asset details.
     *
     * @param asset the asset to which another asset is to be related (should have at least its GUID and name)
     * @param related the (partial) asset that should be related to the asset, which needs to be completed
     * @return a completed related asset that can be idempotently saved
     */
    override fun buildRelated(asset: Asset, related: Asset): Asset {
        return AssetRefXformer.getRelated(asset, related)
    }

    /**
     * Check if the provided field should be cleared, and if so clear it.
     *
     * @param field to check if it is empty and should be cleared
     * @param candidate the asset on which to check whether the field is empty (or not)
     * @param builder the builder against which to clear the field
     * @return true if the field was cleared, false otherwise
     */
    private fun clearField(field: AtlanField, candidate: Asset, builder: AssetBuilder<*, *>): Boolean {
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
}
