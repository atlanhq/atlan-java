/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag.model

import com.fasterxml.jackson.annotation.JsonProperty

class LFTableInfo(
    @JsonProperty("Table") var table: LFTable,
    @JsonProperty("LFTagOnDatabase") var lfTagOnDatabase: List<LFTagPair> = listOf(),
    @JsonProperty("LFTagsOnTable") var lfTagsOnTable: List<LFTagPair> = listOf(),
    @JsonProperty("LFTagsOnColumns") var lfTagsOnColumn: List<ColumnLFTag> = listOf(),
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
