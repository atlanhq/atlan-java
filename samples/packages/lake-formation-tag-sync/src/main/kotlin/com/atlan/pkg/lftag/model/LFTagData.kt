/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag.model

import com.fasterxml.jackson.annotation.JsonProperty

class LFTagData(
    @JsonProperty("TableList") var tableList: List<LFTableInfo>,
) {
    val tagValuesByTagKey = mutableMapOf<String, MutableSet<String>>()

    init {
        tableList.forEach { tableInfo ->
            tableInfo.getTagValuesByTagKey(tagValuesByTagKey)
        }
    }
}
