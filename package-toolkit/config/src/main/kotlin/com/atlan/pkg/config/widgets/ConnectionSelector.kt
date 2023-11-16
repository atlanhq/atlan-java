/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.widgets

/**
 * Widget that allows you to select an existing connection from a drop-down list,
 * and returns the qualified name of the selected connection.
 *
 * @param label name to show in the UI for the widget
 * @param required whether a value must be selected to proceed with the UI setup
 * @param hidden whether the widget will be shown in the UI (false) or not (true)
 * @param help informational text to place in a hover-over to describe the use of the input
 * @param placeholder example text to place within the widget to exemplify its use
 * @param grid sizing of the input on the UI (8 is full-width, 4 is half-width)
 * @param start TBC
 */
class ConnectionSelector(
    label: String,
    required: Boolean = false,
    hidden: Boolean = false,
    help: String = "",
    placeholder: String = "",
    grid: Int = 4,
    start: Int = 1,
) : UIElement(
    type = "string",
    required,
    ConnectionSelectorWidget(
        label,
        hidden,
        help,
        placeholder,
        grid,
        start,
    ),
) {
    class ConnectionSelectorWidget(
        label: String,
        hidden: Boolean = false,
        help: String = "",
        placeholder: String = "",
        grid: Int = 4,
        val start: Int = 1,
    ) : Widget(
        "connectionSelector",
        label,
        hidden,
        help,
        placeholder,
        grid,
    )
}
