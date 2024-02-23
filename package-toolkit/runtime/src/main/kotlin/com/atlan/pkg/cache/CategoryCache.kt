/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.exception.AtlanException
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.fields.AtlanField
import com.atlan.net.HttpClient
import com.atlan.pkg.serde.cell.GlossaryCategoryXformer
import com.atlan.pkg.serde.cell.GlossaryXformer.GLOSSARY_DELIMITER
import mu.KotlinLogging

object CategoryCache : AssetCache() {
    private val logger = KotlinLogging.logger {}

    private val includesOnResults: List<AtlanField> = listOf(GlossaryCategory.NAME, GlossaryCategory.ANCHOR, GlossaryCategory.PARENT_CATEGORY)
    private val includesOnRelations: List<AtlanField> = listOf(Glossary.NAME)

    /** {@inheritDoc}  */
    override fun lookupAssetByIdentity(identity: String?): Asset? {
        // Always return null, as the cache should always be preloaded
        return null
    }

    /** {@inheritDoc}  */
    override fun lookupAssetByGuid(guid: String?, currentAttempt: Int, maxRetries: Int): Asset? {
        try {
            val category =
                GlossaryCategory.select()
                    .where(GlossaryCategory.GUID.eq(guid))
                    .includesOnResults(includesOnResults)
                    .includeOnResults(GlossaryTerm.STATUS)
                    .includesOnRelations(includesOnRelations)
                    .pageSize(1)
                    .stream()
                    .findFirst()
            if (category.isPresent) {
                return category.get()
            } else {
                if (currentAttempt >= maxRetries) {
                    logger.error { "No category found with GUID: $guid" }
                } else {
                    Thread.sleep(HttpClient.waitTime(currentAttempt).toMillis())
                    return lookupAssetByGuid(guid, currentAttempt + 1, maxRetries)
                }
            }
        } catch (e: AtlanException) {
            logger.error("Unable to lookup or find category: {}", guid, e)
        }
        guid?.let { addToIgnore(guid) }
        return null
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
        logger.info { "Caching entire hierarchy for $glossaryName, up-front..." }
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
                logger.warn { "There are no categories in glossary $glossaryName -- nothing to cache." }
            }
        } else {
            logger.error {
                "Unable to find glossary $glossaryName, and therefore unable to find any categories within it."
            }
        }
        return categories
    }

    /** {@inheritDoc}  */
    override fun getIdentityForAsset(asset: Asset): String {
        return when (asset) {
            is GlossaryCategory -> {
                // Note: this only works as long as we always ensure that categories are loaded
                // in level-order (parents before children)
                val parentIdentity = if (asset.parentCategory == null) {
                    ""
                } else {
                    getIdentity(asset.parentCategory.guid)
                }
                return if (parentIdentity.isNullOrBlank()) {
                    "${asset.name}${GLOSSARY_DELIMITER}${asset.anchor.name}"
                } else {
                    val parentPath = parentIdentity.split(GLOSSARY_DELIMITER)[0]
                    "$parentPath${GlossaryCategoryXformer.CATEGORY_DELIMITER}${asset.name}$GLOSSARY_DELIMITER${asset.anchor.name}"
                }
            }
            else -> ""
        }
    }

    /** {@inheritDoc} */
    override fun preload() {
        logger.info { "Caching all categories, up-front..." }
        Glossary.select()
            .includeOnResults(Glossary.NAME)
            .stream(true)
            .forEach { glossary ->
                traverseAndCacheHierarchy(glossary.name)
            }
    }
}
