/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import com.atlan.model.assets.Asset

/**
 * Interface to generate a row of string values from an asset object.
 */
interface RowGenerator {
    /**
     * Generate an iterable set of values for a tabular row of data from an asset object.
     *
     * @param asset the asset from which to generate the values
     * @return the values, as an iterable set of strings
     */
    fun buildFromAsset(asset: Asset): Iterable<String>
}
