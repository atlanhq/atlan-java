/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.widgets

/**
 * Widget that allows you to select a single user,
 * and returns the username of the selected user.
 *
 * @param label name to show in the UI for the widget
 * @param required whether a value must be selected to proceed with the UI setup
 * @param hidden whether the widget will be shown in the UI (false) or not (true)
 * @param help informational text to place in a hover-over to describe the use of the input
 * @param grid sizing of the input on the UI (8 is full-width, 4 is half-width)
 */
class SingleUser(
    label: String,
    required: Boolean = false,
    hidden: Boolean = false,
    help: String = "",
    grid: Int = 8,
) : UIElement(
    "string",
    required,
    SingleUserWidget(
        label,
        hidden,
        help,
        grid,
    ),
) {
    class SingleUserWidget(
        label: String,
        hidden: Boolean = false,
        help: String = "",
        grid: Int = 8,
    ) : Widget(
        "users",
        label = label,
        hidden = hidden,
        help = help,
        grid = grid,
    )
}
