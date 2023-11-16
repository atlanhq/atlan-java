/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.widgets

/**
 * Widget that allows you to select from a drop-down of provided options.
 *
 * @param label name to show in the UI for the widget
 * @param possibleValues map of option keys to the value that will be display for each option in the drop-down
 * @param required whether a value must be selected to proceed with the UI setup
 * @param hidden whether the widget will be shown in the UI (false) or not (true)
 * @param help informational text to place in a hover-over to describe the use of the input
 * @param multiSelect whether multiple options can be selected (true) or only a single option (false)
 * @param grid sizing of the input on the UI (8 is full-width, 4 is half-width)
 */
class DropDown(
    label: String,
    possibleValues: Map<String, String>,
    required: Boolean = false,
    hidden: Boolean = false,
    help: String = "",
    multiSelect: Boolean = false,
    grid: Int = 8,
) : UIElementWithEnum(
    type = "string",
    required,
    possibleValues,
    ui = DropDownWidget(
        label,
        if (multiSelect) "multiple" else "",
        hidden,
        help,
        grid,
    ),
) {
    class DropDownWidget(
        label: String,
        val mode: String,
        hidden: Boolean = false,
        help: String = "",
        grid: Int,
    ) : Widget(
        "select",
        label,
        hidden,
        help,
        grid = grid,
    )
}
