/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.widgets

/**
 * Widget that allows you to select an existing API token from a drop-down list,
 * and returns the GUID of the selected API token.
 * Note: currently only API tokens that were created by the user configuring the workflow
 * will appear in the drop-down list.
 *
 * @param label name to show in the UI for the widget
 * @param required whether a value must be selected to proceed with the UI setup
 * @param hidden whether the widget will be shown in the UI (false) or not (true)
 * @param help informational text to place in a hover-over to describe the use of the input
 * @param grid sizing of the input on the UI (8 is full-width, 4 is half-width)
 */
class APITokenSelector(
    label: String,
    required: Boolean = false,
    hidden: Boolean = false,
    help: String = "",
    grid: Int = 4,
) : UIElement(
    type = "string",
    required,
    APITokenSelectorWidget(
        label,
        hidden,
        help,
        grid,
    ),
) {
    class APITokenSelectorWidget(
        label: String,
        hidden: Boolean = false,
        help: String = "",
        grid: Int = 4,
    ) : Widget(
        "apiTokenSelect",
        label,
        hidden,
        help,
        grid = grid,
    )
}
