/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag.model

import com.fasterxml.jackson.annotation.JsonProperty
import mu.KotlinLogging

class LFTableInfo(
    @JsonProperty("Table") var table: LFTable,
    @JsonProperty("LFTagOnDatabase") var lfTagOnDatabase: List<LFTagPair>,
    @JsonProperty("LFTagsOnTable") var lfTagsOnTable: List<LFTagPair>?,
    @JsonProperty("LFTagsOnColumns") var lfTagsOnColumn: List<ColumnLFTag>?,
) {
    private val logger = KotlinLogging.logger {}

    fun getTagValuesByTagKey(tagValuesByTagKey: MutableMap<String, MutableSet<String>>): Map<String, Set<String>> {
        lfTagOnDatabase.forEach { lfTagPair ->
            extracted(lfTagPair, tagValuesByTagKey)
        }
        if (lfTagsOnTable == null) {
            logger.warn { "'LFTagsOnTable' missing for Table: ${table.name}." }
        } else {
            lfTagsOnTable?.forEach { lfTagPair ->
                extracted(lfTagPair, tagValuesByTagKey)
            }
        }
        if (lfTagsOnColumn == null) {
            logger.warn { "'LFTagsOnTable' missing for Table: ${table.name}." }
        } else {
            lfTagsOnColumn?.forEach { columnLfTags ->
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
