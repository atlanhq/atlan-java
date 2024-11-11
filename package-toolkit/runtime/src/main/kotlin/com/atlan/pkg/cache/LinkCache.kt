/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.model.assets.Asset
import com.atlan.model.assets.Link
import com.atlan.model.fields.AtlanField
import mu.KotlinLogging
import java.util.concurrent.ConcurrentHashMap

/**
 * Cache for links, since these have purely generated qualifiedNames (a UUID).
 * Note that this entire cache relies on first being preloaded -- otherwise nothing will every be found in it.
 */
object LinkCache : AssetCache<Link>() {
    private val logger = KotlinLogging.logger {}

    private val byAssetGuid: MutableMap<String, MutableSet<String>> = ConcurrentHashMap()

    private val includesOnResults: List<AtlanField> = listOf(Link.NAME, Link.STATUS, Link.LINK, Link.ASSET)
    private val includesOnRelations: List<AtlanField> = listOf(Asset.GUID)

    /** {@inheritDoc} */
    override fun lookupByName(name: String?) {
        throw IllegalStateException("Link cache can only be preloaded en-masse, not retrieved link-by-link.")
    }

    /** {@inheritDoc} */
    override fun lookupById(id: String?) {
        throw IllegalStateException("Link cache can only be preloaded en-masse, not retrieved link-by-link.")
    }

    /**
     * Retrieve the pre-existing links for a particular asset.
     *
     * @param guid of the asset for which to retrieve pre-existing links
     * @return the set of (minimal) links that already exist on the asset
     */
    fun getByAssetGuid(guid: String): Set<Link> {
        return byAssetGuid.getOrDefault(guid, setOf()).map { getById(it, false) }.toSet()
    }

    /**
     * Add a link to the cache.
     *
     * @param link to add to the cache
     */
    fun add(link: Link) {
        link.asset?.let {
            val ref = (link.asset as Asset).trimToReference()
            val url = link.link
            val assetGuid = link.asset.guid
            val minimal = link.trimToRequired().asset(ref).link(url).name(link.name).build()
            cache(minimal.guid, getIdentityForAsset(minimal), minimal)
            if (!byAssetGuid.containsKey(assetGuid)) {
                byAssetGuid[assetGuid] = ConcurrentHashMap.newKeySet()
            }
            byAssetGuid[assetGuid]?.add(link.guid)
        }
    }

    /** {@inheritDoc}  */
    override fun getIdentityForAsset(asset: Link): String {
        return "${asset.name}=${asset.link}@${asset.asset.guid}"
    }

    /** {@inheritDoc} */
    override fun refreshCache() {
        val request =
            Link.select()
                .includesOnResults(includesOnResults)
                .includesOnRelations(includesOnRelations)
                .pageSize(1)
                .toRequest()
        val response = request.search()
        logger.info { "Caching all ${response?.approximateCount ?: 0} links, up-front..." }
        initializeOffHeap("link", response?.approximateCount?.toInt() ?: 0, response?.assets[0] as Link, Link::class.java)
        Link.select()
            .includesOnResults(includesOnResults)
            .includesOnRelations(includesOnRelations)
            .stream(true)
            .forEach { link ->
                add(link as Link)
            }
    }
}
