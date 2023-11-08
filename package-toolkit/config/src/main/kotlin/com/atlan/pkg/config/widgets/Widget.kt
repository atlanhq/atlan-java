/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.widgets

abstract class Widget(
    val widget: String,
    val label: String,
    val hidden: Boolean = false,
    val help: String = "",
    val placeholder: String = "",
    val grid: Int = 8,
)
