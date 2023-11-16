/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.model.assets.Asset
import com.atlan.model.assets.AtlanQuery
import com.atlan.model.assets.AuthPolicy
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.assets.IAccessControl
import com.atlan.model.assets.INamespace
import com.atlan.model.assets.Link
import com.atlan.model.assets.Procedure
import com.atlan.model.fields.AtlanField
import com.atlan.model.fields.CustomMetadataField
import com.atlan.model.search.FluentSearch
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.RowSerde
import com.atlan.pkg.serde.RowSerializer
import mu.KotlinLogging
import java.io.File
import java.util.stream.Collectors
import java.util.stream.Stream

private val logger = KotlinLogging.logger {}

/**
 * Actually run the export, taking all settings from environment variables.
 * Note: all parameters should be passed through environment variables.
 */
fun main() {
    val config = Utils.setPackageOps<AssetExportBasicCfg>()
    val exporter = Exporter(config)
    exporter.export()
}

/**
 * Export assets from Atlan, in one of two modes (defined by EXPORT_SCOPE environment variable):
 * - ENRICHED_ONLY — will only export assets that have had some UI-controlled field enriched (default)
 * - ALL — will export all assets
 *
 * In both cases the overall scope of assets to include is restricted by the qualifiedName prefix
 * specified by the QN_PREFIX environment variable.
 *
 * @param config configuration to use for the exporter, typically driven by environment variables
 */
class Exporter(private val config: AssetExportBasicCfg) : RowGenerator {

    private val batchSize = Utils.getOrDefault(config.batchSize, 50)
    private val filename = "tmp" + File.separator + "asset-export.csv"

    fun export() {
        val assets = getAssetsToExtract()
            .pageSize(batchSize)
            .includesOnResults(getAttributesToExtract())
            .includeOnRelations(Asset.QUALIFIED_NAME)
            .includesOnRelations(getRelatedAttributesToExtract())

        CSVWriter(filename).use { csv ->
            val headerNames = Stream.of(Asset.QUALIFIED_NAME, Asset.TYPE_NAME)
                .map(AtlanField::getAtlanFieldName)
                .collect(Collectors.toList())
            headerNames.addAll(
                getAttributesToExtract().stream()
                    .map { f -> RowSerde.getHeaderForField(f) }
                    .collect(Collectors.toList()),
            )
            csv.writeHeader(headerNames)
            val start = System.currentTimeMillis()
            csv.streamAssets(assets.stream(true), this, assets.count(), batchSize, logger)
            logger.info("Total time taken: {} ms", System.currentTimeMillis() - start)
        }
    }

    private fun getAssetsToExtract(): FluentSearch.FluentSearchBuilder<*, *> {
        val scope = Utils.getOrDefault(config.exportScope, "ENRICHED_ONLY")
        val builder = Atlan.getDefaultClient().assets
            .select()
            .where(Asset.QUALIFIED_NAME.startsWith(Utils.getOrDefault(config.qnPrefix, "default")))
            .whereNot(FluentSearch.superTypes(listOf(IAccessControl.TYPE_NAME, INamespace.TYPE_NAME)))
            .whereNot(FluentSearch.assetTypes(listOf(AuthPolicy.TYPE_NAME, Procedure.TYPE_NAME, AtlanQuery.TYPE_NAME)))
        if (scope == "ENRICHED_ONLY") {
            builder
                .whereSome(Asset.CERTIFICATE_STATUS.hasAnyValue())
                .whereSome(Asset.DESCRIPTION.hasAnyValue())
                .whereSome(Asset.USER_DESCRIPTION.hasAnyValue())
                .whereSome(Asset.ANNOUNCEMENT_TYPE.hasAnyValue())
                .whereSome(Asset.ASSIGNED_TERMS.hasAnyValue())
                .whereSome(Asset.ATLAN_TAGS.hasAnyValue())
                .whereSome(Asset.README.hasAny())
                .whereSome(Asset.LINKS.hasAny())
                .whereSome(Asset.STARRED_BY.hasAnyValue())
                .minSomes(1)
            for (cmField in CustomMetadataFields.all) {
                builder.whereSome(cmField.hasAnyValue())
            }
        }
        return builder
    }

    private fun getAttributesToExtract(): MutableList<AtlanField> {
        val attributeList: MutableList<AtlanField> = mutableListOf(
            Asset.NAME,
            Asset.DISPLAY_NAME,
            Asset.DESCRIPTION,
            Asset.USER_DESCRIPTION,
            Asset.OWNER_USERS,
            Asset.OWNER_GROUPS,
            Asset.CERTIFICATE_STATUS,
            Asset.CERTIFICATE_STATUS_MESSAGE,
            Asset.ANNOUNCEMENT_TYPE,
            Asset.ANNOUNCEMENT_TITLE,
            Asset.ANNOUNCEMENT_MESSAGE,
            Asset.ASSIGNED_TERMS,
            Asset.ATLAN_TAGS,
            Asset.LINKS,
            Asset.README,
            Asset.STARRED_DETAILS,
        )
        for (cmField in CustomMetadataFields.all) {
            attributeList.add(cmField)
        }
        return attributeList
    }

    private fun getRelatedAttributesToExtract(): MutableList<AtlanField> {
        return mutableListOf(
            Asset.QUALIFIED_NAME, // for asset referencing
            Asset.NAME, // for Link embedding
            Asset.DESCRIPTION, // for README embedding
            Link.LINK, // for Link embedding
            GlossaryTerm.ANCHOR, // for assigned term containment
        )
    }

    /**
     * Generate a set of values for a row of CSV, based on the provided asset.
     *
     * @param asset the asset from which to generate the values
     * @return the values, as an iterable set of strings
     */
    override fun buildFromAsset(asset: Asset): Iterable<String> {
        return RowSerializer(asset, getAttributesToExtract()).getRow()
    }
}

/**
 * Singleton for capturing all the custom metadata fields that exist in the tenant.
 */
object CustomMetadataFields {

    val all: List<CustomMetadataField>
    init {
        all = loadCustomMetadataFields()
    }

    /**
     * Retrieve all custom metadata fields for attributes that exist in the tenant.
     *
     * @return a list of all custom metadata fields defined in the tenant
     */
    private fun loadCustomMetadataFields(): List<CustomMetadataField> {
        val customMetadataDefs = Atlan.getDefaultClient().customMetadataCache
            .getAllCustomAttributes(false, true)
        val fields = mutableListOf<CustomMetadataField>()
        for ((setName, attributes) in customMetadataDefs) {
            for (attribute in attributes) {
                fields.add(CustomMetadataField(Atlan.getDefaultClient(), setName, attribute.displayName))
            }
        }
        return fields
    }
}
