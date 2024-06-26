/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import com.fasterxml.jackson.annotation.JsonProperty

class Table(
    @JsonProperty("CatalogId") var catalogId: String,
    @JsonProperty("DatabaseName") var databaseName: String,
    @JsonProperty("Name") var name: String,
)
