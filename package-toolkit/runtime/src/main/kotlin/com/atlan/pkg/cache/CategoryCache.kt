/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.exception.AtlanException
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.fields.AtlanField
import com.atlan.net.HttpClient
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.cell.GlossaryCategoryXformer
import com.atlan.pkg.serde.cell.GlossaryXformer.GLOSSARY_DELIMITER

class CategoryCache(
    val ctx: PackageContext<*>,
) : AssetCache<GlossaryCategory>(ctx, "category") {
    private val logger = Utils.getLogger(this.javaClass.name)

    private val includesOnResults: List<AtlanField> = listOf(GlossaryCategory.NAME, GlossaryCategory.ANCHOR, GlossaryCategory.PARENT_CATEGORY)
    private val includesOnRelations: List<AtlanField> = listOf(Glossary.NAME)

    /** {@inheritDoc} */
    override fun lookupByName(name: String?) {
        // Do nothing: category cache can only be preloaded en-masse, not retrieved category-by-category.
    }

    /** {@inheritDoc} */
    override fun lookupById(id: String?) {
        val result = lookupById(id, 0, ctx.client.maxNetworkRetries)
        if (result != null) cache(result.guid, getIdentityForAsset(result), result)
    }

    /** {@inheritDoc}  */
    private fun lookupById(
        guid: String?,
        currentAttempt: Int,
        maxRetries: Int,
    ): GlossaryCategory? {
        try {
            val category =
                GlossaryCategory
                    .select(client)
                    .where(GlossaryCategory.GUID.eq(guid))
                    .includesOnResults(includesOnResults)
                    .includeOnResults(GlossaryTerm.STATUS)
                    .includesOnRelations(includesOnRelations)
                    .pageSize(1)
                    .stream()
                    .findFirst()
            if (category.isPresent) {
                return category.get() as GlossaryCategory
            } else {
                if (currentAttempt >= maxRetries) {
                    logger.warn { "No category found with GUID: $guid" }
                } else {
                    Thread.sleep(HttpClient.waitTime(currentAttempt).toMillis())
                    return lookupById(guid, currentAttempt + 1, maxRetries)
                }
            }
        } catch (e: AtlanException) {
            logger.warn { "Unable to lookup or find category: $guid" }
            logger.debug(e) { "Full stack trace:" }
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
    private fun traverseAndCacheHierarchy(glossaryName: String): List<GlossaryCategory> = this.traverseAndCacheHierarchy(glossaryName, emptyList())

    /**
     * It is likely to be more efficient (for any sizeable import) to retrieve and traverse
     * the entire hierarchy at the same time rather than recursively look things up
     * step-by-step. (Bigger initial hit, but bulk-retrieves rather than category-by-category
     * retrieves.)
     *
     * @param glossaryName name of the glossary for which to bulk-cache categories
     * @param attributes (checked) to retrieve for each category in the hierarchy
     */
    fun traverseAndCacheHierarchy(
        glossaryName: String,
        attributes: List<AtlanField>,
        relatedAttributes: List<AtlanField> = listOf(),
    ): List<GlossaryCategory> {
        val categories = mutableListOf<GlossaryCategory>()
        logger.info { "Caching entire hierarchy for $glossaryName, up-front..." }
        val glossary = ctx.glossaryCache.getByIdentity(glossaryName)
        if (glossary is Glossary) {
            // Initial hit may be high, but for any sizeable import will be faster
            // to retrieve the entire hierarchy than recursively look it up step-by-step
            try {
                val hierarchy = glossary.getHierarchy(client, includesOnResults + attributes, relatedAttributes)
                hierarchy.breadthFirst().forEach { category ->
                    val parent = category.parentCategory
                    category as GlossaryCategory
                    parent?.let {
                        // Note: this MUST bypass the read lock, since it is called within a write lock
                        // (otherwise, this will become a deadlock -- the read will wait until write lock is released,
                        // which will never happen because it is waiting on this read operation to complete.)
                        val parentIdentity = getIdentity(parent.guid, true)
                        val parentPath = parentIdentity?.split(GLOSSARY_DELIMITER)?.get(0) ?: ""
                        cache(
                            category.guid,
                            "$parentPath${GlossaryCategoryXformer.CATEGORY_DELIMITER}${category.name}$GLOSSARY_DELIMITER$glossaryName",
                            category,
                        )
                    } ?: cache(
                        category.guid,
                        "${category.name}$GLOSSARY_DELIMITER$glossaryName",
                        category,
                    )
                    categories.add(category)
                }
            } catch (e: NotFoundException) {
                logger.warn { "There are no categories in glossary $glossaryName -- nothing to cache." }
                logger.debug(e) { "Full details:" }
            }
        } else {
            logger.warn {
                "Unable to find glossary $glossaryName, and therefore unable to find any categories within it."
            }
        }
        return categories
    }

    /** {@inheritDoc}  */
    override fun getIdentityForAsset(asset: GlossaryCategory): String {
        // Note: this only works as long as we always ensure that categories are loaded
        // in level-order (parents before children)
        val parentIdentity =
            if (asset.parentCategory == null) {
                ""
            } else {
                getIdentity(asset.parentCategory.guid)
            }
        return if (parentIdentity.isNullOrBlank()) {
            asset.anchor?.name?.let { glossaryName ->
                "${asset.name}${GLOSSARY_DELIMITER}$glossaryName"
            } ?: throw IllegalStateException("Category found with no anchor: ${asset.toJson(client)})")
        } else {
            val parentPath = parentIdentity.split(GLOSSARY_DELIMITER)[0]
            "$parentPath${GlossaryCategoryXformer.CATEGORY_DELIMITER}${asset.name}$GLOSSARY_DELIMITER${asset.anchor.name}"
        }
    }

    /** {@inheritDoc} */
    override fun refreshCache() {
        val count = GlossaryCategory.select(client).count()
        logger.info { "Caching all $count categories, up-front..." }
        Glossary
            .select(client)
            .includeOnResults(Glossary.NAME)
            .stream(true)
            .forEach { glossary ->
                traverseAndCacheHierarchy(glossary.name)
            }
    }
}
