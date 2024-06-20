/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import com.fasterxml.jackson.annotation.JsonProperty

class ColumnLFTag(
    @JsonProperty("Name") var name: String,
    @JsonProperty("LFTags") var lfTags: List<LFTagPair>,
)
