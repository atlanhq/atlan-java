/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde

import com.atlan.model.assets.Asset
import com.atlan.model.fields.AtlanField
import com.atlan.util.AssetBatch

/**
 * Data class to encapsulate a deserialized row of data from a tabular format.
 *
 * @param identity unique identity of the asset
 * @param primary the primary asset represented by the row of tabular data
 * @param related a map from field name to the related asset(s) that are embedded for that field
 * @param delete a set of fields representing values that should be cleared (deleted) from an asset
 */
data class RowDeserialization(
    val identity: AssetBatch.AssetIdentity,
    val primary: Asset.AssetBuilder<*, *>,
    val related: MutableMap<String, Collection<Asset>> = mutableMapOf(),
    val delete: MutableSet<AtlanField> = mutableSetOf(),
)
