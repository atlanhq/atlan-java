/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Asset
import com.atlan.model.assets.AtlanQuery
import com.atlan.model.assets.AuthPolicy
import com.atlan.model.assets.IAccessControl
import com.atlan.model.assets.INamespace
import com.atlan.model.assets.Procedure
import com.atlan.model.fields.AtlanField
import com.atlan.model.search.FluentSearch
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.cell.DataDomainXformer
import com.atlan.pkg.serde.csv.CSVWriter
import com.atlan.pkg.serde.csv.RowGenerator
import java.util.SortedSet
import java.util.stream.Stream


/**
 * Export domain relationships from Atlan.
 *
 * @param ctx context in which the package is running
 * @param filename name of the file into which to export domain relationships
 * @param batchSize maximum number of assets to request per API call
 */
class DomainRelationshipExporter(
    private val ctx: PackageContext<AssetExportBasicCfg>,
    private val filename: String,
    private val batchSize: Int,
) : RowGenerator {
    private val logger = Utils.getLogger(this.javaClass.name)

    fun export() {
        val headerNames = getFieldsToExtract()
        val assets =
            getAssetsToExtract()
                .pageSize(batchSize)
                .includesOnResults(headerNames)

        CSVWriter(filename).use { csv ->
            // Write header with field names
            csv.writeHeader(headerNames.map { it.atlanFieldName })

            // Assuming assets is a FluentSearch object or similar, we retrieve the stream of assets
            val processedStream: Stream<Asset> = assets.stream(false)

            // Process each asset using forEach, and collect the modified assets
            processedStream.forEach { asset ->
                // Preprocess the domainGUIDs field (2nd field)
                val modifiedDomainGuids = preprocessGuid(asset.domainGUIDs)
                // combine and write to CSV
                csv.writeRecord(listOf(asset.qualifiedName, modifiedDomainGuids?.joinToString(";")))
            }
        }
    }

    private fun preprocessGuid(guidSet: SortedSet<String>?): List<String?>? {
        // iterate over the set and modify each GUID
        val modifiedGuids =
            guidSet?.map { guid ->
                // Modify the GUID if it exists, otherwise return null
                if (guid != null) {
                    // Modify the GUID
                    DataDomainXformer.encodeFromGuid(ctx, guid)
                } else {
                    null
                }
            }

        return modifiedGuids?.toList()
    }

    private fun getAssetsToExtract(): FluentSearch.FluentSearchBuilder<*, *> {
        val builder =
            ctx.client.assets
                .select(ctx.config.includeArchived)
                .whereNot(Asset.SUPER_TYPE_NAMES.`in`(listOf(IAccessControl.TYPE_NAME, INamespace.TYPE_NAME)))
                .whereNot(Asset.TYPE_NAME.`in`(listOf(AuthPolicy.TYPE_NAME, Procedure.TYPE_NAME, AtlanQuery.TYPE_NAME)))
        val qnPrefixes = Utils.getAsList(ctx.config.qnPrefixes).ifEmpty { listOf(ctx.config.qnPrefix) }
        if (qnPrefixes.size > 1) {
            val assetsQueryBuilder = FluentSearch._internal()
            qnPrefixes.forEach {
                assetsQueryBuilder.whereSome(Asset.QUALIFIED_NAME.startsWith(it))
            }
            builder.where(assetsQueryBuilder.build().toQuery())
        } else {
            builder.where(Asset.QUALIFIED_NAME.startsWith(qnPrefixes.first()))
        }
        if (ctx.config.exportScope == "ENRICHED_ONLY") {
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
            if (ctx.config.includeDescription) {
                builder.whereSome(Asset.DESCRIPTION.hasAnyValue())
            }
        }
        val limitToAssets = Utils.getAsList(ctx.config.assetTypesToInclude)
        if (limitToAssets.isNotEmpty()) {
            builder.where(Asset.TYPE_NAME.`in`(limitToAssets))
        }
        return builder
    }

    private fun getFieldsToExtract(): List<AtlanField> {
        val attributeList: MutableList<AtlanField> =
            mutableListOf(
                Asset.QUALIFIED_NAME,
                Asset.DOMAIN_GUIDS,
            )
        return attributeList
    }

    override fun buildFromAsset(asset: Asset): Iterable<String> {
        return DomainRelationshipRowSerializer(asset).getRow()
    }

    class DomainRelationshipRowSerializer(
        private val asset: Asset,
    ) {
        fun getRow(): Iterable<String> {
            return listOf(asset.qualifiedName, asset.domainGUIDs.joinToString(","))
        }
    }
}
