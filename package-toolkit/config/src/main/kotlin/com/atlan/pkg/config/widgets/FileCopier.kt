/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.widgets

/**
 * Widget that allows you to copy a file from the tenant's S3 bucket, and returns the name of the file
 * (as it is named in S3).
 *
 * @param label name to show in the UI for the widget
 * @param required whether a value must be entered to proceed with the UI setup
 * @param hidden whether the widget will be shown in the UI (false) or not (true)
 * @param help informational text to place in a hover-over to describe the use of the input
 * @param placeholder placeholder example text to place within the widget to exemplify its use
 * @param grid sizing of the input on the UI (8 is full-width, 4 is half-width)
 */
class FileCopier(
    label: String,
    required: Boolean = false,
    hidden: Boolean = false,
    help: String = "",
    placeholder: String = "",
    grid: Int = 8,
) : UIElement(
    type = "string",
    required,
    FileCopierWidget(
        label,
        hidden,
        help,
        placeholder,
        grid,
    ),
) {
    class FileCopierWidget(
        label: String,
        hidden: Boolean = false,
        help: String = "",
        placeholder: String = "",
        grid: Int = 8,
    ) : Widget(
        "input",
        label,
        hidden,
        help,
        placeholder,
        grid,
    )
}
