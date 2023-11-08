/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.widgets

/**
 * Widget that allows you to enter an arbitrary number into a single-line text input field,
 * and returns the value of the number that was entered.
 *
 * @param label name to show in the UI for the widget
 * @param required whether a value must be selected to proceed with the UI setup
 * @param hidden whether the widget will be shown in the UI (false) or not (true)
 * @param help informational text to place in a hover-over to describe the use of the input
 * @param placeholder example text to place within the widget to exemplify its use
 * @param grid sizing of the input on the UI (8 is full-width, 4 is half-width)
 */
class NumericInput(
    label: String,
    required: Boolean = false,
    hidden: Boolean = false,
    help: String = "",
    placeholder: String = "",
    grid: Int = 8,
) : UIElement(
    "number",
    required,
    NumericInputWidget(
        label,
        hidden,
        help,
        placeholder,
        grid,
    ),
) {
    private class NumericInputWidget(
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
