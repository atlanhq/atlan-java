/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.workflow

import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("name", "value")
class NameValuePair(
    val name: String,
    val value: String,
) : NamedPair(name)
