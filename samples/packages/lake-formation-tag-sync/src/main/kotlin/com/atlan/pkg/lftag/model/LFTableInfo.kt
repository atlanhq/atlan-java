/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag.model

import com.atlan.pkg.Utils
import com.fasterxml.jackson.annotation.JsonProperty

class LFTableInfo(
    @JsonProperty("Table") var table: LFTable,
    @JsonProperty("LFTagOnDatabase") var lfTagOnDatabase: List<LFTagPair> = emptyList(),
    @JsonProperty("LFTagsOnTable") var lfTagsOnTable: List<LFTagPair> = emptyList(),
    @JsonProperty("LFTagsOnColumns") var lfTagsOnColumn: List<ColumnLFTag> = emptyList(),
) {
    private val logger = Utils.getLogger(this.javaClass.name)

    fun getTagValuesByTagKey(tagValuesByTagKey: MutableMap<String, MutableSet<String>>): Map<String, Set<String>> {
        if (lfTagOnDatabase.isEmpty()) {
            logger.warn { "'lfTagOnDatabase' missing for Table: ${table.name}." }
        } else {
            lfTagOnDatabase.forEach { lfTagPair ->
                extracted(lfTagPair, tagValuesByTagKey)
            }
        }
        if (lfTagsOnTable.isEmpty()) {
            logger.warn { "'LFTagsOnTable' missing for Table: ${table.name}." }
        } else {
            lfTagsOnTable.forEach { lfTagPair ->
                extracted(lfTagPair, tagValuesByTagKey)
            }
        }
        if (lfTagsOnColumn.isEmpty()) {
            logger.warn { "'LFTagsOnTable' missing for Table: ${table.name}." }
        } else {
            lfTagsOnColumn.forEach { columnLfTags ->
                columnLfTags.lfTags.forEach { lfTagPair ->
                    extracted(lfTagPair, tagValuesByTagKey)
                }
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
