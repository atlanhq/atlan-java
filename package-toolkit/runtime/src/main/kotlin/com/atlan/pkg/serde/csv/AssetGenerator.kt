/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import com.atlan.model.assets.Asset
import com.atlan.pkg.serde.RowDeserialization
import com.atlan.pkg.serde.RowDeserializer
import java.util.stream.Stream

/**
 * Interface to generate an asset object from a row of string values.
 */
interface AssetGenerator {
    /**
     * Build an asset object from the string values of a row of tabular data.
     *
     * @param row the row of values from which to build the asset
     * @param header list of field names in the same order as columns in the tabular data
     * @param typeIdx numeric index within the columns of the typeName field
     * @param qnIdx numeric index within the columns of the qualifiedName field
     * @param skipColumns columns to skip, i.e. that need to be handled in a later pass
     * @return the asset(s) built from the values on the row
     */
    fun buildFromRow(
        row: List<String>,
        header: List<String>,
        typeIdx: Int,
        qnIdx: Int,
        skipColumns: Set<String>,
    ): RowDeserialization?

    /**
     * Check whether to include this row as part of the processing (true) or not (false).
     *
     * @param row of values
     * @param header column names
     * @param typeIdx index of the typeName
     * @param qnIdx index of the qualifiedName
     * @return true if the row should be included in the import, or false if not
     */
    fun includeRow(
        row: List<String>,
        header: List<String>,
        typeIdx: Int,
        qnIdx: Int,
    ): Boolean

    /**
     * Start a builder for the asset on this row.
     *
     * @param deserializer for the row
     * @return a builder for the asset on this row
     */
    fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *>

    /**
     * Cache any created assets (optional).
     * This will be called with the results of any created assets after processing
     * a pass.
     *
     * @param list of assets that were created (minimal info for each)
     */
    fun cacheCreated(list: Stream<Asset>) {
        // Do nothing, by default
    }

    /**
     * Validate that the format of the input file matches the requirements of the package.
     *
     * @param header column names
     * @return the list of names of columns that are required but not present in the file
     */
    fun validateHeader(header: List<String>?): List<String>
}
