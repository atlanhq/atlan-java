/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag.model

import com.fasterxml.jackson.annotation.JsonProperty

class LFTable(
    @JsonProperty("CatalogId") var catalogId: String? = null,
    @JsonProperty("DatabaseName") var databaseName: String,
    @JsonProperty("Name") var name: String,
)
