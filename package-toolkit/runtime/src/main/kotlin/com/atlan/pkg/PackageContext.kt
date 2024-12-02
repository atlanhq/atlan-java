/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg

import com.atlan.AtlanClient
import com.atlan.pkg.cache.AssetCache
import com.atlan.pkg.cache.CategoryCache
import com.atlan.pkg.cache.ConnectionCache
import com.atlan.pkg.cache.DataDomainCache
import com.atlan.pkg.cache.DataProductCache
import com.atlan.pkg.cache.GlossaryCache
import com.atlan.pkg.cache.LinkCache
import com.atlan.pkg.cache.TermCache
import java.io.IOException

/**
 * Context for a custom package, including its tenant connectivity (client), package-specific configuration,
 * and any further asset caches for the package.
 *
 * @param config package-specific configuration
 * @param client connectivity to the Atlan tenant
 * @param reusedClient whether the client in this context is reused (if so, will not be automatically closed)
 */
class PackageContext<T : CustomConfig>(
    val config: T,
    val client: AtlanClient,
    private val reusedClient: Boolean = false,
) : AutoCloseable {
    val glossaryCache = GlossaryCache(this)
    val termCache = TermCache(this)
    val categoryCache = CategoryCache(this)
    val connectionCache = ConnectionCache(this)
    val dataDomainCache = DataDomainCache(this)
    val dataProductCache = DataProductCache(this)
    val linkCache = LinkCache(this)

    /** {@inheritDoc} */
    override fun close() {
        var err: IOException? = closeCache(glossaryCache, null)
        err = closeCache(termCache, err)
        err = closeCache(categoryCache, err)
        err = closeCache(connectionCache, err)
        err = closeCache(dataDomainCache, err)
        err = closeCache(dataProductCache, err)
        err = closeCache(linkCache, err)
        if (!reusedClient) {
            try {
                client.close()
            } catch (e: IOException) {
                if (err != null) {
                    err.addSuppressed(e)
                } else {
                    err = e
                }
            }
        }
        if (err != null) {
            throw err
        }
    }

    private fun closeCache(
        cache: AssetCache<*>,
        previous: IOException?,
    ): IOException? {
        var running = previous
        try {
            cache.close()
        } catch (e: IOException) {
            if (running != null) {
                running.addSuppressed(e)
            } else {
                running = e
            }
        }
        return running
    }
}
