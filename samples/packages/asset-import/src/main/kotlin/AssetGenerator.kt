/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Asset
import com.atlan.pkg.serde.RowDeserialization

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
     * @return the asset(s) built from the values on the row
     */
    fun buildFromRow(row: List<String>, header: List<String>, typeIdx: Int, qnIdx: Int): RowDeserialization?

    /**
     * Build a complete related asset object from the provided asset and (partial) related asset details.
     *
     * @param asset the asset to which another asset is to be related (should have at least its GUID and name)
     * @param related the (partial) asset that should be related to the asset, which needs to be completed
     * @return a completed related asset that can be idempotently saved
     */
    fun buildRelated(asset: Asset, related: Asset): Asset
}
