/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package cache

import com.atlan.cache.OffHeapAssetCache
import com.atlan.mock.MockAtlanTenant
import com.atlan.model.assets.Table
import com.atlan.pkg.Utils
import com.atlan.pkg.Utils.getLogger
import com.atlan.pkg.cache.PersistentConnectionCache
import com.atlan.pkg.objectstore.ObjectStorageSyncer
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import kotlin.io.path.createTempDirectory
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Regression coverage for CSA-516: building a connection cache for a connection that has no
 * pre-existing cache blob in object storage (the "new connection" path) must succeed.
 */
class ConnectionCacheNoPriorTest {
    private val logger = getLogger(this.javaClass.name)

    companion object {
        init {
            MockAtlanTenant.initializeClient()
        }
    }

    private val workDir = createTempDirectory("csa-516-").toFile()

    @AfterTest
    fun cleanup() {
        workDir.deleteRecursively()
    }

    private fun singleAssetCache(
        cacheName: String,
        connectionQN: String,
    ): OffHeapAssetCache {
        val cache = OffHeapAssetCache(MockAtlanTenant.client, cacheName)
        cache.add(
            Table
                ._internal()
                .guid("f47ac10b-58cc-4372-a567-0e02b2c3d479")
                .qualifiedName("$connectionQN/db/schema/tbl")
                .name("tbl")
                .connectionQualifiedName(connectionQN)
                .build(),
        )
        return cache
    }

    private fun cacheFileFor(connectionQN: String): File = Paths.get(workDir.path, "connection-cache", "$connectionQN.sqlite").toFile()

    /**
     * Happy path: no prior cache blob exists in the (local) backing store, so a brand-new cache is
     * created from scratch and populated. This is the exact scenario that fails in production for
     * a connection that has never had a cache before.
     */
    @Test
    fun createsNewCacheWhenNonePreexists() {
        val connectionQN = "default/temp/1783632307"
        singleAssetCache("added-local", connectionQN).use { added ->
            Utils.updateConnectionCache(
                client = MockAtlanTenant.client,
                added = added,
                removed = null,
                fallback = workDir.path,
            )
        }
        val cacheFile = cacheFileFor(connectionQN)
        assertTrue(cacheFile.exists(), "Expected a new connection cache at ${cacheFile.path}")
        val assets = PersistentConnectionCache(cacheFile.path).listAssets()
        assertEquals(1, assets.size)
        assertEquals(Table.TYPE_NAME, assets[0].typeName)
    }

    /**
     * Regression (CSA-516): a failed download can leave a partial/empty file behind at the local
     * cache path -- the ADLS and GCS SDKs both create the local file before the transfer runs, so a
     * missing blob leaves a leftover. SQLite must not be handed that leftover; the leftover has to be
     * removed so the cache is created from a clean slate. Before the fix this threw an
     * SQLiteException from PersistentConnectionCache.<init> ("file is not a database" / SQLITE_IOERR).
     */
    @Test
    fun recoversWhenFailedDownloadLeavesPartialFile() {
        val connectionQN = "default/custom/1783632333"
        val leftoverLeavingStore =
            object : ObjectStorageSyncer {
                override fun downloadFrom(
                    remoteKey: String,
                    localFile: String,
                ) {
                    // Mimic ADLS readToFile / GCS FileOutputStream: create the local file, then fail.
                    val local = File(localFile)
                    local.parentFile.mkdirs()
                    local.writeText("not a sqlite database -- partial download leftover")
                    throw IOException("simulated missing blob: $remoteKey")
                }

                override fun uploadTo(
                    localFile: String,
                    remoteKey: String,
                ) {
                    val target = Paths.get(workDir.path, remoteKey).toFile()
                    target.parentFile.mkdirs()
                    File(localFile).copyTo(target, overwrite = true)
                }

                override fun copyFrom(
                    prefix: String,
                    localDirectory: String,
                ): List<String> = throw NotImplementedError()

                override fun copyLatestFrom(
                    prefix: String,
                    extension: String,
                    localDirectory: String,
                ): String = throw NotImplementedError()

                override fun copyTo(
                    localDirectory: String,
                    prefix: String,
                ): Boolean = throw NotImplementedError()
            }
        singleAssetCache("added-partial", connectionQN).use { added ->
            Utils.updateConnectionCache(
                client = MockAtlanTenant.client,
                sync = leftoverLeavingStore,
                added = added,
                removed = null,
                fallback = workDir.path,
            )
        }
        val cacheFile = cacheFileFor(connectionQN)
        assertTrue(cacheFile.exists(), "Expected the cache to be created despite the failed download")
        val assets = PersistentConnectionCache(cacheFile.path).listAssets()
        assertEquals(1, assets.size)
        assertEquals(Table.TYPE_NAME, assets[0].typeName)
    }
}
