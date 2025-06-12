/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.assets.Link
import com.atlan.model.fields.AtlanField
import com.atlan.model.fields.CustomMetadataField
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.RowSerde
import com.atlan.pkg.serde.csv.CSVWriter
import com.atlan.pkg.serde.csv.RowGenerator
import mu.KLogger
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * Export glossary assets from Atlan (including terms and categories).
 *
 * @param ctx context in which the package is running
 * @param filename name of the file into which to export assets
 * @param batchSize maximum number of assets to request per API call
 * @param cmFields list of all custom metadata fields
 */
class GlossaryExporter(
    private val ctx: PackageContext<AssetExportBasicCfg>,
    private val filename: String,
    private val batchSize: Int,
    private val cmFields: List<CustomMetadataField>,
) : RowGenerator {
    private val logger = Utils.getLogger(this.javaClass.name)

    fun export() {
        CSVWriter(filename).use { csv ->
            // TODO: qualifiedName is not a good way to do this for glossary objects...
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

            // Retrieve all glossaries up-front
            val glossaries =
                Glossary
                    .select(ctx.client, ctx.config.includeArchived)
                    .pageSize(batchSize)
                    .includesOnResults(getAttributesToExtract())
                    .includesOnRelations(getRelatedAttributesToExtract())
                    .stream(true)
                    .toList()
            logger.info { "Appending ${glossaries.size} glossaries..." }
            csv.appendAssets(glossaries, this, glossaries.size.toLong(), batchSize, logger)

            // Then extract all categories, per glossary, up-front (caching them
            // as we go, for later reference)
            glossaries.parallelStream().forEach {
                val categories = ctx.categoryCache.traverseAndCacheHierarchy(it.name, getAttributesToExtract(), getRelatedAttributesToExtract())
                if (categories.isNotEmpty()) {
                    logger.info { "Appending ${categories.size} categories from ${it.name}..." }
                    csv.appendAssets(
                        categories,
                        this,
                        categories.size.toLong(),
                        batchSize,
                        logger,
                    )
                }
            }

            // And finally extract all the terms
            val assets =
                GlossaryTerm
                    .select(ctx.client, ctx.config.includeArchived)
                    .pageSize(batchSize)
                    .includesOnResults(getAttributesToExtract())
                    .includesOnRelations(getRelatedAttributesToExtract())
                    .includeRelationshipAttributes(true)

            csv.streamAssets(assets.stream(true), this, assets.count(), batchSize, logger)
            logger.info { "Total time taken: ${System.currentTimeMillis() - start} ms" }
        }
    }

    private fun getAttributesToExtract(): MutableList<AtlanField> {
        val attributeList: MutableList<AtlanField> =
            mutableListOf(
                Asset.NAME,
                GlossaryTerm.ANCHOR,
                GlossaryCategory.PARENT_CATEGORY,
                GlossaryTerm.CATEGORIES,
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
                GlossaryTerm.SEE_ALSO,
                GlossaryTerm.PREFERRED_TERMS,
                GlossaryTerm.SYNONYMS,
                GlossaryTerm.ANTONYMS,
                GlossaryTerm.TRANSLATED_TERMS,
                GlossaryTerm.VALID_VALUES_FOR,
                GlossaryTerm.CLASSIFIES,
                Asset.USER_DEF_RELATIONSHIP_TOS,
                Asset.USER_DEF_RELATIONSHIP_FROMS,
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
    override fun buildFromAsset(asset: Asset): Iterable<String> = GlossaryRowSerializer(ctx, asset, getAttributesToExtract(), logger).getRow()

    /**
     * Class to serialize glossary assets into a row of tabular data.
     * Note: this replaces the general asset row serializer to handle nuances of glossary objects.
     *
     * @param ctx context in which the custom package is running
     * @param asset the asset to be serialized
     * @param fields the full list of fields to be serialized from the asset, in the order they should be serialized
     * @param logger through which to record any problems
     */
    class GlossaryRowSerializer(
        private val ctx: PackageContext<AssetExportBasicCfg>,
        private val asset: Asset,
        private val fields: List<AtlanField>,
        private val logger: KLogger,
    ) {
        /**
         * Actually serialize the provided inputs into a list of string values.
         *
         * @return the list of string values giving a row-based tabular representation of the asset
         */
        fun getRow(): Iterable<String> {
            val row = mutableListOf<String>()
            row.add(FieldSerde.getValueForField(ctx, asset, Asset.QUALIFIED_NAME, logger))
            row.add(FieldSerde.getValueForField(ctx, asset, Asset.TYPE_NAME, logger))
            for (field in fields) {
                if (field != Asset.QUALIFIED_NAME && field != Asset.TYPE_NAME) {
                    if (asset !is GlossaryTerm && field == GlossaryTerm.CATEGORIES) {
                        // Only serialize the categories attribute for terms, no other
                        // glossary object types
                        row.add("")
                    } else {
                        row.add(FieldSerde.getValueForField(ctx, asset, field, logger))
                    }
                }
            }
            return row
        }
    }
}
