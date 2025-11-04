/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg

import com.atlan.AtlanClient
import com.atlan.cache.OffHeapAssetCache
import com.atlan.cache.OffHeapFailureCache
import com.atlan.model.core.AtlanCloseable
import com.atlan.pkg.cache.CategoryCache
import com.atlan.pkg.cache.ConnectionCache
import com.atlan.pkg.cache.DataDomainCache
import com.atlan.pkg.cache.DataProductCache
import com.atlan.pkg.cache.GlossaryCache
import com.atlan.pkg.cache.LinkCache
import com.atlan.pkg.cache.TermCache
import com.atlan.pkg.cache.TypeDefCache
import com.atlan.pkg.serde.csv.ImportResults
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Context for a custom package, including its tenant connectivity (client), package-specific configuration,
 * and any further asset caches for the package.
 *
 * @param config package-specific configuration
 * @param client connectivity to the Atlan tenant
 * @param reusedClient whether the client in this context is reused (if so, will not be automatically closed)
 */
class PackageContext<T : CustomConfig<T>>(
    val config: T,
    val client: AtlanClient,
    private val reusedClient: Boolean = false,
) : AtlanCloseable {
    val startTS = System.currentTimeMillis()
    val glossaryCache = GlossaryCache(this)
    val termCache = TermCache(this)
    val categoryCache = CategoryCache(this)
    val connectionCache = ConnectionCache(this)
    val dataDomainCache = DataDomainCache(this)
    val dataProductCache = DataProductCache(this)
    val linkCache = LinkCache(this)
    val typeDefCache = TypeDefCache(this)
    val processedResults = ImportResults(false,
        ImportResults.Details(
            mutableMapOf(),
            mutableMapOf(),
            OffHeapAssetCache(client, "pp-created"),
            OffHeapAssetCache(client, "pp-updated"),
            OffHeapAssetCache(client, "pp-restored"),
            OffHeapAssetCache(client, "pp-skipped"),
            OffHeapFailureCache(client, "pp-failed"),
            0L,
            0L,
            0L
        ), ImportResults.Details(
            mutableMapOf(),
            mutableMapOf(),
            OffHeapAssetCache(client, "pr-created"),
            OffHeapAssetCache(client, "pr-updated"),
            OffHeapAssetCache(client, "pr-restored"),
            OffHeapAssetCache(client, "pr-skipped"),
            OffHeapFailureCache(client, "pr-failed"),
            0L,
            0L,
            0L
        ))
    val caseSensitive = AtomicBoolean(true)

    /** {@inheritDoc} */
    override fun close() {
        AtlanCloseable.close(glossaryCache)
        AtlanCloseable.close(termCache)
        AtlanCloseable.close(categoryCache)
        AtlanCloseable.close(connectionCache)
        AtlanCloseable.close(dataDomainCache)
        AtlanCloseable.close(dataProductCache)
        AtlanCloseable.close(linkCache)
        AtlanCloseable.close(typeDefCache)
        if (!reusedClient) {
            AtlanCloseable.close(client)
        }
        AtlanCloseable.close(processedResults)
    }
}
