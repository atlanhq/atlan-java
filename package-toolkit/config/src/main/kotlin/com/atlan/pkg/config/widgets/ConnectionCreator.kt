/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.widgets

/**
 * Widget that allows you to create a new connection by providing a name and list of admins,
 * and returns a string representation of the connection object that should be created.
 *
 * @param label name to show in the UI for the widget
 * @param required whether a value must be selected to proceed with the UI setup
 * @param hidden whether the widget will be shown in the UI (false) or not (true)
 * @param help informational text to place in a hover-over to describe the use of the input
 * @param placeholder example text to place within the widget to exemplify its use
 */
class ConnectionCreator(
    label: String,
    required: Boolean = false,
    hidden: Boolean = false,
    help: String = "",
    placeholder: String = "",
) : UIElement(
    type = "string",
    required,
    ConnectionCreatorWidget(
        label,
        hidden,
        help,
        placeholder,
    ),
) {
    private class ConnectionCreatorWidget(
        label: String,
        hidden: Boolean = false,
        help: String = "",
        placeholder: String = "",
    ) : Widget(
        "connection",
        label,
        hidden,
        help,
        placeholder,
    )
}
