/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.cache.ReflectionCache
import com.atlan.model.assets.Asset
import com.atlan.model.fields.AtlanField
import com.atlan.model.fields.SearchableField
import com.atlan.pkg.Utils
import com.atlan.pkg.cache.LinkCache
import com.atlan.serde.Serde
import mu.KotlinLogging
import java.lang.reflect.InvocationTargetException
import kotlin.system.exitProcess

/**
 * Actually run the importer.
 * Note: all parameters should be passed through environment variables.
 */
object Importer {
    private val logger = KotlinLogging.logger {}

    @JvmStatic
    fun main(args: Array<String>) {
        val config = Utils.setPackageOps<AssetImportCfg>()

        val batchSize = 20
        val assetsFilename = Utils.getOrDefault(config.assetsFile, "")
        val glossariesFilename = Utils.getOrDefault(config.glossariesFile, "")
        val assetAttrsToOverwrite =
            attributesToClear(Utils.getOrDefault(config.assetsAttrToOverwrite, listOf()).toMutableList(), "assets")
        val glossaryAttrsToOverwrite =
            attributesToClear(Utils.getOrDefault(config.glossariesAttrToOverwrite, listOf()).toMutableList(), "glossaries")
        val assetsUpdateOnly = Utils.getOrDefault(config.assetsUpsertSemantic, "update") == "update"
        val glossariesUpdateOnly = Utils.getOrDefault(config.glossariesUpsertSemantic, "update") == "update"

        if (glossariesFilename.isBlank() && assetsFilename.isBlank()) {
            logger.error("No input file was provided for either glossaries or assets.")
            exitProcess(1)
        }

        LinkCache.preload()

        if (glossariesFilename.isNotBlank()) {
            logger.info("Importing glossaries...")
            val glossaryImporter =
                GlossaryImporter(glossariesFilename, glossaryAttrsToOverwrite, glossariesUpdateOnly, batchSize)
            glossaryImporter.import()
            logger.info("Importing categories...")
            val categoryImporter =
                CategoryImporter(glossariesFilename, glossaryAttrsToOverwrite, glossariesUpdateOnly, batchSize)
            categoryImporter.import()
            logger.info("Importing terms...")
            val termImporter =
                TermImporter(glossariesFilename, glossaryAttrsToOverwrite, glossariesUpdateOnly, batchSize)
            termImporter.import()
        }
        if (assetsFilename.isNotBlank()) {
            logger.info("Importing assets...")
            val assetImporter = AssetImporter(assetsFilename, assetAttrsToOverwrite, assetsUpdateOnly, batchSize)
            assetImporter.import()
        }
    }

    /**
     * Determine which (if any) attributes should be cleared (removed) if they are empty in the input file.
     *
     * @param attrNames the list of attribute names provided through the configuration
     * @param fileInfo a descriptor to qualify for which file the attributes are being set
     * @return parsed list of attribute names to be cleared
     */
    private fun attributesToClear(attrNames: MutableList<String>, fileInfo: String): List<AtlanField> {
        if (attrNames.contains(Asset.CERTIFICATE_STATUS.atlanFieldName)) {
            attrNames.add(Asset.CERTIFICATE_STATUS_MESSAGE.atlanFieldName)
        }
        if (attrNames.contains(Asset.ANNOUNCEMENT_TYPE.atlanFieldName)) {
            attrNames.add(Asset.ANNOUNCEMENT_TITLE.atlanFieldName)
            attrNames.add(Asset.ANNOUNCEMENT_MESSAGE.atlanFieldName)
        }
        logger.info("Adding attributes to be cleared, if blank (for {}): {}", fileInfo, attrNames)
        val attrFields = mutableListOf<AtlanField>()
        for (name in attrNames) {
            attrFields.add(SearchableField(name, name))
        }
        return attrFields
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
}
