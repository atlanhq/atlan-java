/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.cache.AbstractOffHeapCache
import com.atlan.util.AssetBatch.AssetIdentity
import java.nio.charset.StandardCharsets

/**
 * Generic class through which to cache any objects efficiently, off-heap, to avoid risking extreme
 * memory usage.
 */
class ChecksumCache(
    name: String?,
) : AbstractOffHeapCache<AssetIdentity, String>(name) {
    /** {@inheritDoc}  */
    override fun serializeKey(key: AssetIdentity): ByteArray = key.toString().toByteArray(StandardCharsets.UTF_8)

    /** {@inheritDoc}  */
    override fun deserializeKey(bytes: ByteArray): AssetIdentity = AssetIdentity.fromString(String(bytes, StandardCharsets.UTF_8))

    /** {@inheritDoc}  */
    override fun serializeValue(value: String): ByteArray = value.toByteArray(StandardCharsets.UTF_8)

    /** {@inheritDoc}  */
    override fun deserializeValue(bytes: ByteArray): String = String(bytes, StandardCharsets.UTF_8)
}
