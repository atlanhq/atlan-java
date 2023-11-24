/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import com.atlan.model.assets.Asset
import com.atlan.pkg.serde.RowDeserialization
import com.atlan.pkg.serde.RowDeserializer

/**
 * Interface to generate an asset object from a row of string values.
 */
interface AssetGenerator {
    /**
     * Build an asset object from the string values of a row of tabular data.
     *
     * @param row the row of values from which to build the asset
     * @param header list of field names in the same order as columns in the tabular data
     * @param typeName index of the typeName column
     * @param skipColumns columns to skip, i.e. that need to be handled in a later pass
     * @return the asset(s) built from the values on the row
     */
    fun buildFromRow(row: List<String>, header: List<String>, typeName: Int, skipColumns: Set<String>): RowDeserialization?

    /**
     * Check whether to include this row as part of the processing (true) or not (false).
     *
     * @param row of values
     * @param header column names
     * @param typeIdx index of the typeName
     * @return true if the row should be included in the import, or false if not
     */
    fun includeRow(row: List<String>, header: List<String>, typeIdx: Int): Boolean

    /**
     * Start a builder for the asset on this row.
     *
     * @param row of values
     * @param header column names
     * @return a builder for the asset on this row
     */
    fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *>

    /**
     * Cache any created assets (optional).
     * This will be called with the results of any created assets after processing
     * a pass.
     *
     * @param map from GUID to asset that was created
     */
    fun cacheCreated(map: Map<String, Asset>) {
        // Do nothing, by default
    }
}
