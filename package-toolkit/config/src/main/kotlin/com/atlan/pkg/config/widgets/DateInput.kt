/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.widgets

/**
 * Widget that allows you to enter or select a date (not including time) from a calendar,
 * and returns the epoch-based number representing that selected date in seconds.
 *
 * @param label name to show in the UI for the widget
 * @param required whether a value must be selected to proceed with the UI setup
 * @param hidden whether the widget will be shown in the UI (false) or not (true)
 * @param help informational text to place in a hover-over to describe the use of the input
 * @param min an offset from today (0) that indicates how far back in the calendar can be selected (-1 is yesterday, 1 is tomorrow, and so on)
 * @param max an offset from today (0) that indicates how far forward in the calendar can be selected (-1 is yesterday, 1 is tomorrow, and so on)
 * @param default an offset from today that indicates the default date that should be selected in the calendar (0 is today, -1 is yesterday, 1 is tomorrow, and so on)
 * @param start TBC
 * @param grid sizing of the input on the UI (8 is full-width, 4 is half-width)
 */
class DateInput(
    label: String,
    required: Boolean = false,
    hidden: Boolean = false,
    help: String = "",
    min: Int = -14,
    max: Int = 0,
    default: Int = 0,
    start: Int = 1,
    grid: Int = 8,
) : UIElement(
    "number",
    required,
    DateInputWidget(
        label,
        hidden,
        help,
        start,
        min,
        max,
        default,
        grid,
    ),
) {
    class DateInputWidget(
        label: String,
        hidden: Boolean = false,
        help: String = "",
        val min: Int = -14,
        val max: Int = 0,
        val default: Int = 0,
        val start: Int = 1,
        grid: Int = 8,
    ) : Widget(
        "date",
        label = label,
        hidden = hidden,
        help = help,
        grid = grid,
    )
}
