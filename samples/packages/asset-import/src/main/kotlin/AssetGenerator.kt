/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Asset
import com.atlan.pkg.serde.RowDeserialization
import com.atlan.pkg.serde.cell.AssetRefXformer
import com.atlan.util.AssetBatch
import mu.KLogger
import java.util.concurrent.atomic.AtomicLong

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
     * Batch up a complete related asset object from the provided asset and (partial) related asset details.
     *
     * @param from the asset to which another asset is to be related (should have at least its GUID and name)
     * @param relatedAssets the (partial) asset(s) that should be related to the asset, which needs to be completed
     * @param batch the batch through which to create the asset(s) / relationships
     * @param count the running count of how many relationships have been created
     * @param totalRelated the static total number of relationships anticipated
     * @param logger through which to log progress
     * @param batchSize maximum number of relationships / assets to create per API call
     */
    fun batchRelated(
        from: Asset,
        relatedAssets: Map<String, Collection<Asset>>,
        batch: AssetBatch,
        count: AtomicLong,
        totalRelated: AtomicLong,
        logger: KLogger,
        batchSize: Int,
    ) {
        AssetRefXformer.buildRelated(from, relatedAssets, batch, count, totalRelated, logger, batchSize)
    }
}
