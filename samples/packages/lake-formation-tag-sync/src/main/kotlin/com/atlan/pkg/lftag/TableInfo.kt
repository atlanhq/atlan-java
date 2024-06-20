/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import com.fasterxml.jackson.annotation.JsonProperty

class TableInfo(
    @JsonProperty("Table") var table: Table,
    @JsonProperty("LFTagOnDatabase") var lfTagOnDatabase: List<LFTagPair>,
    @JsonProperty("LFTagsOnTable") var lfTagsOnTable: List<LFTagPair>,
    @JsonProperty("LFTagsOnColumns") var lfTagsOnColumn: List<ColumnLFTag>,
)
