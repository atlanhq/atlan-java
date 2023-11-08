/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.widgets

/**
 * Widget that allows you to upload a file, and returns the GUID-based name of the file (as it is
 * renamed after upload).
 *
 * @param label name to show in the UI for the widget
 * @param fileTypes list of the mime-types of files that should be accepted
 * @param required whether a value must be selected to proceed with the UI setup
 * @param hidden whether the widget will be shown in the UI (false) or not (true)
 * @param help informational text to place in a hover-over to describe the use of the input
 * @param placeholder placeholder example text to place within the widget to exemplify its use
 */
class FileUploader(
    label: String,
    fileTypes: List<String>,
    required: Boolean = false,
    hidden: Boolean = false,
    help: String = "",
    placeholder: String = "",
) : UIElement(
    type = "string",
    required,
    FileUploaderWidget(
        label,
        fileTypes,
        hidden,
        help,
        placeholder,
    ),
) {
    private class FileUploaderWidget(
        label: String,
        fileTypes: List<String>,
        hidden: Boolean = false,
        help: String = "",
        placeholder: String = "",
    ) : Widget(
        "fileUpload",
        label,
        hidden,
        help,
        placeholder,
    ) {
        val accept: List<String> = fileTypes
    }
}
