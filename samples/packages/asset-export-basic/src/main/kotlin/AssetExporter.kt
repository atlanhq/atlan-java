/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.cache.ReflectionCache
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
import com.atlan.serde.Serde
import mu.KotlinLogging

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
        val fieldsToExtract = getFieldsToExtract()
        val assets =
            getAssetsToExtract()
                .pageSize(batchSize)
                .includesOnRelations(getRelatedAttributesToExtract())
                .includesOnResults(fieldsToExtract)
        val headerNames = mutableListOf(Asset.QUALIFIED_NAME.atlanFieldName, Asset.TYPE_NAME.atlanFieldName)
        headerNames.addAll(fieldsToExtract.map { RowSerde.getHeaderForField(it) })

        CSVWriter(filename).use { csv ->
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
                .whereNot(Asset.SUPER_TYPE_NAMES.`in`(listOf(IAccessControl.TYPE_NAME, INamespace.TYPE_NAME)))
                .whereNot(Asset.TYPE_NAME.`in`(listOf(AuthPolicy.TYPE_NAME, Procedure.TYPE_NAME, AtlanQuery.TYPE_NAME)))
        if (ctx.assetsQualifiedNamePrefixes.size > 1) {
            val assetsQueryBuilder = FluentSearch._internal()
            ctx.assetsQualifiedNamePrefixes.forEach {
                assetsQueryBuilder.whereSome(Asset.QUALIFIED_NAME.startsWith(it))
            }
            builder.where(assetsQueryBuilder.build().toQuery())
        } else {
            builder.where(Asset.QUALIFIED_NAME.startsWith(ctx.assetsQualifiedNamePrefixes.first()))
        }
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

    private fun getFieldsToExtract(): List<AtlanField> {
        return if (ctx.allAttributes) {
            val uniqueFieldNames = mutableSetOf<String>()
            ctx.limitToAssets.forEach { assetType ->
                val clazz = Serde.getAssetClassForType(assetType)
                val fields = ReflectionCache.getFieldNames(clazz)
                val toInclude =
                    fields
                        .filter { includeProperties.contains(it) || ReflectionCache.isAttribute(clazz, it) }
                        .map { ReflectionCache.getSerializedName(clazz, it) }
                uniqueFieldNames.addAll(toInclude)
            }
            // Create an interim searchable AtlanField for the field, for all other methods to be reusable
            uniqueFieldNames
                .filter { it != Asset.QUALIFIED_NAME.atlanFieldName }
                .map { SearchableField(it, it) }
        } else if (ctx.limitToAttributes.isNotEmpty()) {
            ctx.limitToAttributes.map { SearchableField(it, it) }.toList()
        } else {
            getAttributesToExtract(ctx.includeDescription, ctx.cmFields)
        }
    }

    companion object {
        fun getAttributesToExtract(
            includeDesc: Boolean,
            cmFields: List<CustomMetadataField>,
        ): List<AtlanField> {
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

        fun getRelatedAttributesToExtract(): List<AtlanField> {
            // Needed for:
            // - asset referencing
            // - Link embedding
            // - README embedding
            // - assigned term containment
            return listOf(
                Asset.QUALIFIED_NAME,
                Asset.NAME,
                Asset.DESCRIPTION,
                Link.LINK,
                GlossaryTerm.ANCHOR,
            )
        }

        // Top-level properties (non-attributes) to include when exporting ALL attributes
        val includeProperties =
            setOf(
                "atlanTags",
                "status",
                "createdBy",
                "updatedBy",
                "createTime",
                "updateTime",
            )
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
