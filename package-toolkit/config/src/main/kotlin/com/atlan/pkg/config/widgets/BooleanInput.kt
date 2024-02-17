/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.widgets

/**
 * Widget that allows you to choose either "Yes" or "No",
 * and returns the value that was selected.
 *
 * @param label name to show in the UI for the widget
 * @param required whether a value must be selected to proceed with the UI setup
 * @param hidden whether the widget will be shown in the UI (false) or not (true)
 * @param default the default value to use for this boolean input
 * @param help informational text to place in a hover-over to describe the use of the input
 * @param grid sizing of the input on the UI (8 is full-width, 4 is half-width)
 */
class BooleanInput(
    label: String,
    required: Boolean = false,
    hidden: Boolean = false,
    default: Boolean = false,
    help: String = "",
    grid: Int = 8,
) : UIElement(
    "boolean",
    required,
    BooleanInputWidget(
        label,
        hidden,
        default,
        help,
        grid,
    ),
) {
    class BooleanInputWidget(
        label: String,
        hidden: Boolean = false,
        val default: Boolean = false,
        help: String = "",
        grid: Int = 8,
    ) : Widget(
        "boolean",
        label = label,
        hidden = hidden,
        help = help,
        grid = grid,
    )
}
