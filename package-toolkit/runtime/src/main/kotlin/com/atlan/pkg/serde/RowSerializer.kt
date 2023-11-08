/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde

import com.atlan.model.assets.Asset
import com.atlan.model.fields.AtlanField

/**
 * Class to generally serialize an asset object into a row of tabular data.
 * Note: this will always serialize the qualifiedName and type of the asset as the first two columns.
 *
 * @param asset the asset to be serialized
 * @param fields the full list of fields to be serialized from the asset, in the order they should be serialized
 */
class RowSerializer(private val asset: Asset, private val fields: List<AtlanField>) {
    /**
     * Actually serialize the provided inputs into a list of string values.
     *
     * @return the list of string values giving a row-based tabular representation of the asset
     */
    fun getRow(): Iterable<String> {
        val row = mutableListOf<String>()
        row.add(FieldSerde.getValueForField(asset, Asset.QUALIFIED_NAME))
        row.add(FieldSerde.getValueForField(asset, Asset.TYPE_NAME))
        for (field in fields) {
            if (field != Asset.QUALIFIED_NAME && field != Asset.TYPE_NAME) {
                row.add(FieldSerde.getValueForField(asset, field))
            }
        }
        return row
    }
}
