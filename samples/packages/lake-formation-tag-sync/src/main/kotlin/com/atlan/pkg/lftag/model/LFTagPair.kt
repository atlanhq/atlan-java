/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */

package com.atlan.pkg.lftag.model

import com.fasterxml.jackson.annotation.JsonProperty

class LFTagPair(
    @JsonProperty("TagKey") val tagKey: String,
    @JsonProperty("TagValues") val tagValues: List<String>,
    @JsonProperty("CatalogId") val catalogId: String,
)
