/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.assets.Link
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.cache.CategoryCache
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.RowSerde
import mu.KotlinLogging
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * Export glossary assets from Atlan (including terms and categories).
 *
 * @param filename name of the file into which to export assets
 * @param batchSize maximum number of assets to request per API call
 */
class GlossaryExporter(
    private val filename: String,
    private val batchSize: Int,
) : RowGenerator {

    private val logger = KotlinLogging.logger {}

    fun export() {
        CSVWriter(filename).use { csv ->
            // TODO: qualifiedName is not a good way to do this for glossary objects...
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

            // Retrieve all glossaries up-front
            val glossaries = Glossary.select()
                .pageSize(batchSize)
                .includesOnResults(getAttributesToExtract())
                .includesOnRelations(getRelatedAttributesToExtract())
                .stream(true)
                .toList()
            logger.info("Appending {} glossaries...", glossaries.size)
            csv.appendAssets(glossaries, this, glossaries.size.toLong(), batchSize, logger)

            // Then extract all categories, per glossary, up-front (caching them
            // as we go, for later reference)
            glossaries.parallelStream().forEach {
                val categories = CategoryCache.traverseAndCacheHierarchy(it.name)
                if (categories.isNotEmpty()) {
                    logger.info("Appending {} categories from {}...", categories.size, it.name)
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
            val assets = GlossaryTerm.select()
                .pageSize(batchSize)
                .includesOnResults(getAttributesToExtract())
                .includesOnRelations(getRelatedAttributesToExtract())

            csv.streamAssets(assets.stream(true), this, assets.count(), batchSize, logger)
            logger.info("Total time taken: {} ms", System.currentTimeMillis() - start)
        }
    }

    private fun getAttributesToExtract(): MutableList<AtlanField> {
        val attributeList: MutableList<AtlanField> = mutableListOf(
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
        )
    }

    /**
     * Generate a set of values for a row of CSV, based on the provided asset.
     *
     * @param asset the asset from which to generate the values
     * @return the values, as an iterable set of strings
     */
    override fun buildFromAsset(asset: Asset): Iterable<String> {
        return GlossaryRowSerializer(asset, getAttributesToExtract()).getRow()
    }

    /**
     * Class to serialize glossary assets into a row of tabular data.
     * Note: this replaces the general asset row serializer to handle nuances of glossary objects.
     *
     * @param asset the asset to be serialized
     * @param fields the full list of fields to be serialized from the asset, in the order they should be serialized
     */
    class GlossaryRowSerializer(private val asset: Asset, private val fields: List<AtlanField>) {
        /**
         * Actually serialize the provided inputs into a list of string values.
         *
         * @return the list of string values giving a row-based tabular representation of the asset
         */
        fun getRow(): Iterable<String> {
            val row = mutableListOf<String>()
            row.add(FieldSerde.getValueForField(asset, Asset.QUALIFIED_NAME))
            row.add(FieldSerde.getValueForField(asset, Asset.TYPE_NAME))
            for (field in fields) {
                if (field != Asset.QUALIFIED_NAME && field != Asset.TYPE_NAME) {
                    if (asset !is GlossaryTerm && field == GlossaryTerm.CATEGORIES) {
                        // Only serialize the categories attribute for terms, no other
                        // glossary object types
                        row.add("")
                    } else {
                        row.add(FieldSerde.getValueForField(asset, field))
                    }
                }
            }
            return row
        }
    }
}
