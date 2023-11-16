/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.widgets

/**
 * Widget that allows you to generate a unique key that could be used for securing an exchange
 * or other unique identification purposes, and provides buttons to regenerate the key or copy its
 * text. Will return the generated key as clear text.
 *
 * @param label name to show in the UI for the widget
 * @param required whether a value must be selected to proceed with the UI setup
 * @param hidden whether the widget will be shown in the UI (false) or not (true)
 * @param help informational text to place in a hover-over to describe the use of the input
 * @param grid sizing of the input on the UI (8 is full-width, 4 is half-width)
 */
class KeygenInput(
    label: String,
    required: Boolean = false,
    hidden: Boolean = false,
    help: String = "",
    grid: Int = 8,
) : UIElement(
    "string",
    required,
    KeygenInputWidget(
        label,
        hidden,
        help,
        grid,
    ),
) {
    class KeygenInputWidget(
        label: String,
        hidden: Boolean = false,
        help: String = "",
        grid: Int = 8,
    ) : Widget(
        "keygen",
        label = label,
        hidden = hidden,
        help = help,
        grid = grid,
    )
}
