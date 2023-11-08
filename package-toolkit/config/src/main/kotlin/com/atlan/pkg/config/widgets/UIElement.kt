/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.widgets

import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("type", "required", "ui")
abstract class UIElement(
    val type: String,
    val required: Boolean,
    val ui: Widget,
)
