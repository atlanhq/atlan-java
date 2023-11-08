/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.ui

import com.atlan.pkg.config.widgets.UIElement
import com.fasterxml.jackson.annotation.JsonIgnore

class UIStep(
    val title: String,
    @JsonIgnore val inputs: Map<String, UIElement>,
    val description: String = "",
) {
    val id = title.replace(" ", "_").lowercase()

    // Note: iterate here to ensure insertion-entry ordering
    val properties: List<String> = inputs.map { it.key }.toList()
}
