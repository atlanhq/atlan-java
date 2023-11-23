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
object LinkCache {
    private val logger = KotlinLogging.logger {}

    private val byAssetGuid: MutableMap<String, MutableSet<Link>> = ConcurrentHashMap()

    private val includesOnResults: List<AtlanField> = listOf(Link.NAME, Link.STATUS, Link.LINK, Link.ASSET)
    private val includesOnRelations: List<AtlanField> = listOf(Asset.GUID)

    /**
     * Retrieve the pre-existing links for a particular asset.
     *
     * @param guid of the asset for which to retrieve pre-existing links
     * @return the set of (minimal) links that already exist on the asset
     */
    fun getByAssetGuid(guid: String): Set<Link> {
        return byAssetGuid.getOrDefault(guid, setOf())
    }

    /**
     * Preload a cache of all links that exist in Atlan, across all assets.
     * (This should generally be more efficient than looking up links asset-by-asset.)
     */
    fun preload() {
        logger.info { "Caching all ${Link.select().count()} links, up-front..." }
        Link.select()
            .includesOnResults(includesOnResults)
            .includesOnRelations(includesOnRelations)
            .stream(true)
            .forEach { link ->
                add(link as Link)
            }
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
            if (!byAssetGuid.containsKey(assetGuid)) {
                byAssetGuid[assetGuid] = ConcurrentHashMap.newKeySet()
            }
            byAssetGuid[assetGuid]?.add(minimal)
        }
    }
}
