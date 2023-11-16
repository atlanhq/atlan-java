/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.widgets

/**
 * Widget that allows you to select just one option from a set of options, and returns the key of
 * the selected option.
 * Typically, this is used to control mutually exclusive options in the UI.
 *
 * @param label name to show in the UI for the widget
 * @param possibleValues map of option keys to the value that will be display for each option in the UI
 * @param default the default value to select in the UI, given as the string key of the option
 * @param required whether a value must be selected to proceed with the UI setup
 * @param hidden whether the widget will be shown in the UI (false) or not (true)
 * @param help informational text to place in a hover-over to describe the use of the input
 * @param placeholder example text to place within the widget to exemplify its use
 */
class Radio(
    label: String,
    possibleValues: Map<String, String>,
    default: String,
    required: Boolean = false,
    hidden: Boolean = false,
    help: String = "",
    placeholder: String = "",
) : UIElementWithEnum(
    type = "string",
    required,
    possibleValues,
    default,
    RadioWidget(
        label,
        hidden,
        help,
        placeholder,
    ),
) {
    class RadioWidget(
        label: String,
        hidden: Boolean = false,
        help: String = "",
        placeholder: String = "",
    ) : Widget(
        "radio",
        label,
        hidden,
        help,
        placeholder,
    )
}
