/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */

package com.atlan.pkg.lftag

import com.fasterxml.jackson.annotation.JsonProperty

class LFTagPair(
    @JsonProperty("TagKey") var tagKey: String,
    @JsonProperty("TagValues") var tagValues: List<String>,
    @JsonProperty("CatalogId") var catalogId: String,
)
