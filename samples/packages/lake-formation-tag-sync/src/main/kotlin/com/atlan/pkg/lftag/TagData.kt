/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import com.fasterxml.jackson.annotation.JsonProperty

class TagData(
    @JsonProperty("TableList") var tableList: List<TableInfo>,
) {
    val tagValuesByTagKey = mutableMapOf<String, MutableSet<String>>()
    init {
        tableList.forEach { tableInfo ->
            tableInfo.getTagValuesByTagKey(tagValuesByTagKey)
        }
    }
}
