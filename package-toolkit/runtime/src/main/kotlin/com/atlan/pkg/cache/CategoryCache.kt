/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.serde.cell.GlossaryCategoryXformer
import com.atlan.pkg.serde.cell.GlossaryXformer.GLOSSARY_DELIMITER
import mu.KotlinLogging

object CategoryCache : AssetCache() {
    private val logger = KotlinLogging.logger {}

    private val includesOnResults: List<AtlanField> = listOf(GlossaryCategory.NAME, GlossaryCategory.ANCHOR, GlossaryCategory.PARENT_CATEGORY)
    private val includesOnRelations: List<AtlanField> = listOf(GlossaryCategory.NAME)

    /** {@inheritDoc}  */
    override fun lookupAssetByIdentity(identity: String?): Asset? {
        TODO("Replace with a proper error: should not be implemented, as cache should be pre-loaded")
        /*val tokens = identity?.split(GLOSSARY_DELIMITER)
        if (tokens?.size == 2) {
            traverseAndCacheHierarchy(tokens[1])
        } else {
            logger.error("Unable to lookup or find categories, unexpected reference: {}", identity)
        }
        return null*/
    }

    /** {@inheritDoc}  */
    override fun lookupAssetByGuid(guid: String?): Asset? {
        TODO("Replace with a proper error: should not be implemented, as cache should be pre-loaded")
        /*try {
            val category =
                GlossaryCategory.select(true)
                    .where(GlossaryCategory.GUID.eq(guid))
                    .includesOnResults(includesOnResults)
                    .includesOnRelations(includesOnRelations)
                    .pageSize(2)
                    .stream()
                    .findFirst()
            if (category.isPresent) {
                val glossaryName = (category.get() as GlossaryCategory).anchor.name
                traverseAndCacheHierarchy(glossaryName)
            }
        } catch (e: AtlanException) {
            logger.error("Unable to lookup or find category: {}", guid, e)
        }
        return null*/
    }

    /**
     * It is likely to be more efficient (for any sizeable import) to retrieve and traverse
     * the entire hierarchy at the same time rather than recursively look things up
     * step-by-step. (Bigger initial hit, but bulk-retrieves rather than category-by-category
     * retrieves.)
     *
     * @param glossaryName name of the glossary for which to bulk-cache categories
     */
    fun traverseAndCacheHierarchy(glossaryName: String): List<GlossaryCategory> {
        val categories = mutableListOf<GlossaryCategory>()
        logger.info("Caching entire hierarchy for {}, up-front...", glossaryName)
        val glossary = GlossaryCache.getByIdentity(glossaryName)
        if (glossary is Glossary) {
            // Initial hit may be high, but for any sizeable import will be faster
            // to retrieve the entire hierarchy than recursively look it up step-by-step
            try {
                val hierarchy = glossary.getHierarchy(includesOnResults)
                hierarchy.breadthFirst().forEach { category ->
                    val parent = category.parentCategory
                    category as GlossaryCategory
                    parent?.let {
                        val parentIdentity = getIdentity(parent.guid)
                        val parentPath = parentIdentity?.split(GLOSSARY_DELIMITER)?.get(0) ?: ""
                        addByIdentity(
                            "$parentPath${GlossaryCategoryXformer.CATEGORY_DELIMITER}${category.name}$GLOSSARY_DELIMITER$glossaryName",
                            category,
                        )
                    } ?: addByIdentity("${category.name}$GLOSSARY_DELIMITER$glossaryName", category)
                    categories.add(category)
                }
            } catch (e: NotFoundException) {
                logger.warn("There are no categories in glossary {} -- nothing to cache.", glossaryName)
            }
        } else {
            logger.error(
                "Unable to find glossary {}, and therefore unable to find any categories within it.",
                glossaryName,
            )
        }
        return categories
    }

    /** {@inheritDoc}  */
    override fun getIdentityForAsset(asset: Asset): String {
        return when (asset) {
            is GlossaryCategory -> {
                // Note: this only works in this scenario because we're ensuring all
                // categories are traversed and cached before we get to this point
                getIdentity(asset.guid)!!
            }
            else -> ""
        }
    }
}
