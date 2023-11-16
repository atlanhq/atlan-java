/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.widgets

/**
 * Widget that allows you to choose multiple groups,
 * and returns an array of group names that were selected.
 *
 * @param label name to show in the UI for the widget
 * @param required whether a value must be selected to proceed with the UI setup
 * @param hidden whether the widget will be shown in the UI (false) or not (true)
 * @param help informational text to place in a hover-over to describe the use of the input
 * @param grid sizing of the input on the UI (8 is full-width, 4 is half-width)
 */
class MultipleGroups(
    label: String,
    required: Boolean = false,
    hidden: Boolean = false,
    help: String = "",
    grid: Int = 8,
) : UIElement(
    "string",
    required,
    MultipleGroupsWidget(
        label,
        hidden,
        help,
        grid,
    ),
) {
    class MultipleGroupsWidget(
        label: String,
        hidden: Boolean = false,
        help: String = "",
        grid: Int = 8,
    ) : Widget(
        "groupMultiple",
        label = label,
        hidden = hidden,
        help = help,
        grid = grid,
    )
}
