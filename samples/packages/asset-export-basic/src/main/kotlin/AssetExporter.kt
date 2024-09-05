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
import com.atlan.model.fields.SearchableField
import com.atlan.model.search.FluentSearch
import com.atlan.pkg.serde.RowSerde
import com.atlan.pkg.serde.RowSerializer
import com.atlan.pkg.serde.csv.CSVWriter
import com.atlan.pkg.serde.csv.RowGenerator
import mu.KotlinLogging
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * Export assets from Atlan, in one of two modes (defined by EXPORT_SCOPE environment variable):
 * - ENRICHED_ONLY — will only export assets that have had some UI-controlled field enriched (default)
 * - ALL — will export all assets
 *
 * In both cases the overall scope of assets to include is restricted by the qualifiedName prefix
 * specified by the QN_PREFIX environment variable.
 *
 * @param ctx context containing the resolved configuration
 * @param filename name of the file into which to export assets
 * @param batchSize maximum number of assets to request per API call
 */
class AssetExporter(
    private val ctx: Exporter.Context,
    private val filename: String,
    private val batchSize: Int,
) : RowGenerator {
    private val logger = KotlinLogging.logger {}

    fun export() {
        val assets =
            getAssetsToExtract()
                .pageSize(batchSize)
                .includesOnRelations(getRelatedAttributesToExtract())
                .includesOnResults(getFieldsToExtract())

        CSVWriter(filename).use { csv ->
            val headerNames =
                Stream.of(Asset.QUALIFIED_NAME, Asset.TYPE_NAME)
                    .map(AtlanField::getAtlanFieldName)
                    .collect(Collectors.toList())
            headerNames.addAll(
                getFieldsToExtract().stream()
                    .map { f -> RowSerde.getHeaderForField(f) }
                    .collect(Collectors.toList()),
            )
            csv.writeHeader(headerNames)
            val start = System.currentTimeMillis()
            csv.streamAssets(assets.stream(true), this, assets.count(), batchSize, logger)
            logger.info { "Total time taken: ${System.currentTimeMillis() - start} ms" }
        }
    }

    private fun getAssetsToExtract(): FluentSearch.FluentSearchBuilder<*, *> {
        val builder =
            Atlan.getDefaultClient().assets
                .select(ctx.includeArchived)
                .where(Asset.QUALIFIED_NAME.startsWith(ctx.assetsQualifiedNamePrefix))
                .whereNot(Asset.SUPER_TYPE_NAMES.`in`(listOf(IAccessControl.TYPE_NAME, INamespace.TYPE_NAME)))
                .whereNot(Asset.TYPE_NAME.`in`(listOf(AuthPolicy.TYPE_NAME, Procedure.TYPE_NAME, AtlanQuery.TYPE_NAME)))
        if (ctx.assetsExportScope == "ENRICHED_ONLY") {
            builder
                .whereSome(Asset.DISPLAY_NAME.hasAnyValue())
                .whereSome(Asset.CERTIFICATE_STATUS.hasAnyValue())
                .whereSome(Asset.USER_DESCRIPTION.hasAnyValue())
                .whereSome(Asset.ANNOUNCEMENT_TYPE.hasAnyValue())
                .whereSome(Asset.ASSIGNED_TERMS.hasAnyValue())
                .whereSome(Asset.ATLAN_TAGS.hasAnyValue())
                .whereSome(Asset.README.hasAny())
                .whereSome(Asset.LINKS.hasAny())
                .whereSome(Asset.STARRED_BY.hasAnyValue())
                .minSomes(1)
            if (ctx.includeDescription) {
                builder.whereSome(Asset.DESCRIPTION.hasAnyValue())
            }
            for (cmField in ctx.cmFields) {
                builder.whereSome(cmField.hasAnyValue())
            }
        }
        if (ctx.limitToAssets.isNotEmpty()) {
            builder.where(Asset.TYPE_NAME.`in`(ctx.limitToAssets))
        }
        return builder
    }

    private fun getFieldsToExtract(): MutableList<AtlanField> {
        return if (ctx.limitToAttributes.isNotEmpty()) {
            ctx.limitToAttributes.map { SearchableField(it, it) }.toMutableList()
        } else {
            getAttributesToExtract(ctx.includeDescription, ctx.cmFields)
        }
    }

    companion object {
        fun getAttributesToExtract(
            includeDesc: Boolean,
            cmFields: List<CustomMetadataField>,
        ): MutableList<AtlanField> {
            val attributeList: MutableList<AtlanField> =
                if (includeDesc) {
                    mutableListOf(
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
                } else {
                    mutableListOf(
                        Asset.NAME,
                        Asset.DISPLAY_NAME,
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
                }
            for (cmField in cmFields) {
                attributeList.add(cmField)
            }
            return attributeList
        }

        fun getRelatedAttributesToExtract(): MutableList<AtlanField> {
            // Needed for:
            // - asset referencing
            // - Link embedding
            // - README embedding
            // - assigned term containment
            return mutableListOf(
                Asset.QUALIFIED_NAME,
                Asset.NAME,
                Asset.DESCRIPTION,
                Link.LINK,
                GlossaryTerm.ANCHOR,
            )
        }
    }

    /**
     * Generate a set of values for a row of CSV, based on the provided asset.
     *
     * @param asset the asset from which to generate the values
     * @return the values, as an iterable set of strings
     */
    override fun buildFromAsset(asset: Asset): Iterable<String> {
        return RowSerializer(asset, getFieldsToExtract(), logger).getRow()
    }
}
