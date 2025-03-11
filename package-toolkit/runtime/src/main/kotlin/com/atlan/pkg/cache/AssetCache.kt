/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.cache.AbstractMassCache
import com.atlan.model.assets.Asset
import com.atlan.model.enums.AtlanStatus
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.stream.Stream

/**
 * Utility class for lazy-loading a cache of assets based on some human-constructable identity.
 *
 * @param ctx context for the custom package
 * @param cacheName name indicating the contents of this cache
 */
abstract class AssetCache<T : Asset>(
    ctx: PackageContext<*>,
    cacheName: String,
) : AbstractMassCache<T>(
        ctx.client,
        cacheName,
    ) {
    private val logger = Utils.getLogger(this.javaClass.name)

    private var preloaded = AtomicBoolean(false)
    private val ignore: MutableMap<String, String?> = ConcurrentHashMap()

    init {
        this.bulkRefresh.set(false)
    }

    /**
     * Retrieve an asset from the cache by its human-readable identity, lazily-loading it on any cache misses.
     *
     * @param identity of the asset to retrieve
     * @return the asset with the specified identity, or null if no such asset is in the cache
     */
    fun getByIdentity(identity: String): T? {
        if (ignore.containsKey(identity)) return null
        if (!isNameKnown(identity)) lookupByName(identity)
        if (!isNameKnown(identity)) return null
        return getByName(identity, false) as T
    }

    /**
     * Retrieve an asset from the cache by its globally-unique identifier, lazily-loading it on any cache misses.
     *
     * @param guid unique identifier (GUID) of the asset to retrieve
     * @return the asset with the specified GUID, or null if no such asset is in the cache
     */
    fun getByGuid(guid: String): T? {
        if (ignore.containsKey(guid)) return null
        if (!isIdKnown(guid)) lookupById(guid)
        if (!isIdKnown(guid)) return null
        return getById(guid, false) as T
    }

    /**
     * Retrieve the unique identity of an asset from the cache by its globally-unique identifier.
     * Note: this allows you to bypass the read lock, in order to avoid potential deadlock situations,
     * however you should ONLY do this if you know PRECISELY that you are still controlling the ordering
     * of reads and writes (as concurrency safety will be bypassed when the read lock is bypassed) -- if
     * you are not careful you may get a cache miss which would otherwise have been a cache hit.
     *
     * @param guid unique identifier (GUID) of the asset to retrieve
     * @param bypassReadLock whether to bypass the read lock (necessary if we're reading while inside a write lock)
     * @return the identity of the specified GUID, or null if no identity could be found
     */
    fun getIdentity(
        guid: String,
        bypassReadLock: Boolean = false,
    ): String? = getNameFromId(guid, bypassReadLock)

    /**
     * Mark the provided asset identity as one to ignore.
     *
     * @param id any identity for the asset, either GUID or string identity
     */
    protected fun addToIgnore(id: String) {
        ignore[id] = id
    }

    /** {@inheritDoc} */
    override fun cache(
        id: String,
        name: String?,
        asset: T?,
    ) {
        if (asset != null && !isArchived(id, asset)) {
            val identity = name ?: getIdentityForAsset(asset)
            super.cache(id, identity, asset)
            ignore.remove(id)
            ignore.remove(identity)
        }
    }

    /**
     * List all the assets held in the cache.
     *
     * @return the set of all assets in the cache
     */
    protected fun listAll(): Stream<Map.Entry<String, T>> = entrySet()

    /**
     * Check whether the asset is archived, and if so mark it to be ignored.
     *
     * @param id any identity for the asset, either GUID or string identity
     * @param asset the asset to check
     * @return true if the asset is archived, false otherwise
     */
    private fun isArchived(
        id: String,
        asset: T,
    ): Boolean =
        if (asset.status != AtlanStatus.ACTIVE) {
            logger.warn { "Unable to cache archived asset: $id" }
            addToIgnore(id)
            true
        } else {
            false
        }

    /**
     * Create a unique, reconstructable identity for the provided asset.
     *
     * @param asset for which to construct the identity
     * @return the identity of the asset
     */
    abstract fun getIdentityForAsset(asset: T): String

    /** Preload the cache (will only act once, in case called multiple times on the same cache) */
    @Synchronized
    fun preload() {
        if (!preloaded.get()) {
            refreshIfNeeded()
            preloaded.set(true)
        }
    }
}
