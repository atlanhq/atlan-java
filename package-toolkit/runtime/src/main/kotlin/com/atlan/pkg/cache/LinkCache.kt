/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.model.assets.Asset
import com.atlan.model.assets.Link
import com.atlan.model.enums.AtlanStatus
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * Cache for links, since these have purely generated qualifiedNames (a UUID).
 * Note that this entire cache relies on first being preloaded -- otherwise nothing will every be found in it.
 */
class LinkCache(
    val ctx: PackageContext<*>,
) : AssetCache<Link>(ctx, "link") {
    private val logger = Utils.getLogger(this.javaClass.name)

    private val byAssetGuid: MutableMap<String, MutableSet<String>> = ConcurrentHashMap()
    private val placeholderToRandom: MutableMap<String, String> = ConcurrentHashMap()

    private val includesOnResults: List<AtlanField> = listOf(Link.NAME, Link.STATUS, Link.LINK, Link.ASSET)
    private val includesOnRelations: List<AtlanField> = listOf(Asset.GUID)

    /** {@inheritDoc} */
    override fun lookupByName(name: String?): Unit = throw IllegalStateException("Link cache can only be preloaded en-masse, not retrieved link-by-link.")

    /** {@inheritDoc} */
    override fun lookupById(id: String?): Unit = throw IllegalStateException("Link cache can only be preloaded en-masse, not retrieved link-by-link.")

    /**
     * Retrieve the pre-existing links for a particular asset.
     * Note: these links may have made-up UUIDs, so should never be used as-is for updates (always trim them
     * first, or use their contents in an updater method, instead).
     *
     * @param guid of the asset for which to retrieve pre-existing links
     * @return the set of (minimal) links that already exist on the asset
     */
    fun getByAssetGuid(guid: String): Set<Link> = byAssetGuid.getOrDefault(guid, setOf()).map { getById(it, false) }.toSet()

    /**
     * Add a link to the cache.
     *
     * @param link to add to the cache
     */
    fun add(link: Link) {
        link.asset?.let {
            val linkId =
                if (link.guid.startsWith("-")) {
                    val uuid = UUID.randomUUID().toString()
                    placeholderToRandom[link.guid] = uuid
                    uuid
                } else {
                    link.guid
                }
            val ref = (link.asset as Asset).trimToReference()
            val url = link.link
            val assetGuid = link.asset.guid
            val minimal =
                link
                    .trimToRequired()
                    .asset(ref)
                    .link(url)
                    .name(link.name)
                    .status(AtlanStatus.ACTIVE)
                    .build()
            cache(linkId, getIdentityForAsset(minimal), minimal)
            if (!byAssetGuid.containsKey(assetGuid)) {
                byAssetGuid[assetGuid] = ConcurrentHashMap.newKeySet()
            }
            byAssetGuid[assetGuid]?.add(linkId)
        }
    }

    /**
     * Replace a link in the cache (one we have resolved its real GUID).
     *
     * @param originalGuid the original (placeholder) GUID to replace
     * @param link to replace in the cache
     */
    fun replace(
        originalGuid: String,
        link: Link,
    ) {
        link.asset?.let {
            val assetGuid = link.asset.guid
            val linkId =
                if (originalGuid.startsWith("-")) {
                    placeholderToRandom.getOrDefault(originalGuid, link.guid)
                } else {
                    link.guid
                }
            byAssetGuid[assetGuid]?.remove(linkId)
            add(link)
        }
    }

    /** {@inheritDoc}  */
    override fun getIdentityForAsset(asset: Link): String = "${asset.name}=${asset.link}@${asset.asset.guid}"

    /** {@inheritDoc} */
    override fun refreshCache() {
        val count = Link.select(client).count()
        logger.info { "Caching all $count links, up-front..." }
        Link
            .select(client)
            .includesOnResults(includesOnResults)
            .includesOnRelations(includesOnRelations)
            .stream(true)
            .forEach { link ->
                add(link as Link)
            }
    }
}
