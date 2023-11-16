/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.widgets

/**
 * Widget that allows you to select from the types of connectors that exist in the tenant
 * (for example "Snowflake"), without needing to select a specific instance of a connection
 * (for example, the "production" connection for Snowflake). Will return a string-encoded
 * object giving the connection type that was selected and a list of all connections in the tenant
 * that have that type.
 *
 * @param label name to show in the UI for the widget
 * @param required whether a value must be selected to proceed with the UI setup
 * @param hidden whether the widget will be shown in the UI (false) or not (true)
 * @param help informational text to place in a hover-over to describe the use of the input
 * @param grid sizing of the input on the UI (8 is full-width, 4 is half-width)
 * @param start TBC
 */
class ConnectorTypeSelector(
    label: String,
    required: Boolean = false,
    hidden: Boolean = false,
    help: String = "",
    grid: Int = 4,
    start: Int = 1,
) : UIElement(
    type = "string",
    required,
    ConnectorTypeSelectorWidget(
        label,
        hidden,
        help,
        grid,
        start,
    ),
) {
    class ConnectorTypeSelectorWidget(
        label: String,
        hidden: Boolean = false,
        help: String = "",
        grid: Int = 4,
        val start: Int = 1,
    ) : Widget(
        "sourceConnectionSelector",
        label = label,
        hidden = hidden,
        help = help,
        grid = grid,
    )
}
