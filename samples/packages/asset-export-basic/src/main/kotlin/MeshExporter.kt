/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.model.assets.Link
import com.atlan.model.fields.AtlanField
import com.atlan.model.fields.CustomMetadataField
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.RowSerde
import com.atlan.pkg.serde.RowSerializer
import com.atlan.pkg.serde.csv.CSVWriter
import com.atlan.pkg.serde.csv.RowGenerator
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * Export data mesh assets (domains and products) from Atlan.
 *
 * @param ctx context in which the package is running
 * @param filename name of the file into which to export assets
 * @param batchSize maximum number of assets to request per API call
 * @param cmFields list of all custom metadata fields
 */
class MeshExporter(
    private val ctx: PackageContext<AssetExportBasicCfg>,
    private val filename: String,
    private val batchSize: Int,
    private val cmFields: List<CustomMetadataField>,
) : RowGenerator {
    private val logger = Utils.getLogger(this.javaClass.name)

    fun export() {
        CSVWriter(filename).use { csv ->
            val headerNames =
                Stream
                    .of(Asset.QUALIFIED_NAME, Asset.TYPE_NAME)
                    .map(AtlanField::getAtlanFieldName)
                    .collect(Collectors.toList())
            headerNames.addAll(
                getAttributesToExtract()
                    .stream()
                    .map { f -> RowSerde.getHeaderForField(f) }
                    .collect(Collectors.toList()),
            )
            csv.writeHeader(headerNames)
            val start = System.currentTimeMillis()

            // Retrieve all domains up-front
            val domains =
                DataDomain
                    .select(ctx.client, ctx.config.includeArchived)
                    .pageSize(batchSize)
                    .includesOnResults(getAttributesToExtract())
                    .includesOnRelations(getRelatedAttributesToExtract())
                    .stream(true)
                    .toList()
            logger.info { "Appending ${domains.size} domains..." }
            csv.appendAssets(domains, this, domains.size.toLong(), batchSize, logger)

            // And finally extract all the data products
            val products =
                DataProduct
                    .select(ctx.client, ctx.config.includeArchived)
                    .pageSize(batchSize)
                    .includesOnResults(getAttributesToExtract())
                    .includesOnRelations(getRelatedAttributesToExtract())

            csv.streamAssets(products.stream(true), this, products.count(), batchSize, logger)
            logger.info { "Total time taken: ${System.currentTimeMillis() - start} ms" }
        }
    }

    private fun getAttributesToExtract(): MutableList<AtlanField> {
        val attributeList: MutableList<AtlanField> =
            mutableListOf(
                Asset.NAME,
                DataDomain.PARENT_DOMAIN,
                DataProduct.DATA_DOMAIN,
                Asset.ASSET_COVER_IMAGE,
                Asset.ASSET_THEME_HEX,
                Asset.ASSET_ICON,
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
                Asset.ATLAN_TAGS,
                Asset.LINKS,
                Asset.README,
                Asset.STARRED_DETAILS,
                DataProduct.DAAP_CRITICALITY,
                DataProduct.DAAP_SENSITIVITY,
                DataProduct.OUTPUT_PORTS,
                DataProduct.DATA_PRODUCT_ASSETS_PLAYBOOK_FILTER,
                DataProduct.DATA_PRODUCT_ASSETS_DSL,
                DataProduct.DAAP_VISIBILITY,
                DataProduct.DAAP_VISIBILITY_USERS,
                DataProduct.DAAP_VISIBILITY_GROUPS,
                DataProduct.DATA_PRODUCT_SCORE_VALUE,
                DataProduct.DATA_PRODUCT_SCORE_UPDATED_AT,
            )
        for (cmField in cmFields) {
            attributeList.add(cmField)
        }
        return attributeList
    }

    private fun getRelatedAttributesToExtract(): MutableList<AtlanField> {
        // Needed for:
        // - asset referencing
        // - Link embedding
        // - README embedding
        return mutableListOf(
            Asset.QUALIFIED_NAME,
            Asset.NAME,
            Asset.DESCRIPTION,
            Link.LINK,
        )
    }

    /**
     * Generate a set of values for a row of CSV, based on the provided asset.
     *
     * @param asset the asset from which to generate the values
     * @return the values, as an iterable set of strings
     */
    override fun buildFromAsset(asset: Asset): Iterable<String> = RowSerializer(ctx, asset, getAttributesToExtract(), logger).getRow()
}
