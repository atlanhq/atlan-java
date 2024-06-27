/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag.model

import com.fasterxml.jackson.annotation.JsonProperty

class ColumnLFTag(
    @JsonProperty("Name") val name: String,
    @JsonProperty("LFTags") val lfTags: List<LFTagPair>,
)
