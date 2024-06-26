/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import com.fasterxml.jackson.annotation.JsonProperty

class TableInfo(
    @JsonProperty("Table") var table: Table,
    @JsonProperty("LFTagOnDatabase") var lfTagOnDatabase: List<LFTagPair>,
    @JsonProperty("LFTagsOnTable") var lfTagsOnTable: List<LFTagPair>,
    @JsonProperty("LFTagsOnColumns") var lfTagsOnColumn: List<ColumnLFTag>,
) {
    fun getTagValuesByTagKey(tagValuesByTagKey: MutableMap<String, MutableSet<String>>): Map<String, Set<String>> {
        lfTagOnDatabase.forEach { lfTagPair ->
            extracted(lfTagPair, tagValuesByTagKey)
        }
        lfTagsOnTable.forEach { lfTagPair ->
            extracted(lfTagPair, tagValuesByTagKey)
        }
        lfTagsOnColumn.forEach { columnLfTags ->
            columnLfTags.lfTags.forEach { lfTagPair ->
                extracted(lfTagPair, tagValuesByTagKey)
            }
        }
        return tagValuesByTagKey
    }

    private fun extracted(
        lfTagPair: LFTagPair,
        tagValuesByTagKey: MutableMap<String, MutableSet<String>>,
    ) {
        val tagKey = lfTagPair.tagKey
        if (!tagValuesByTagKey.containsKey(tagKey)) {
            tagValuesByTagKey[tagKey] = lfTagPair.tagValues.toMutableSet()
        } else {
            tagValuesByTagKey[tagKey]?.addAll(lfTagPair.tagValues)
        }
    }
}
