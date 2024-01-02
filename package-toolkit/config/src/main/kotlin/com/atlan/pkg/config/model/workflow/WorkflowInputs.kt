/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.workflow

import com.atlan.model.workflow.NameValuePair
import com.atlan.pkg.CustomPipeline
import com.atlan.pkg.Utils
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.widgets.BooleanInput
import com.atlan.pkg.config.widgets.DateInput
import com.atlan.pkg.config.widgets.FileCopier
import com.atlan.pkg.config.widgets.FileUploader
import com.atlan.pkg.config.widgets.NumericInput
import com.fasterxml.jackson.annotation.JsonIgnore

class WorkflowInputs(
    @JsonIgnore val config: UIConfig,
    @JsonIgnore val pkgName: String = "",
) {
    val parameters: List<NameValuePair>
    val artifacts: List<NamePathS3Tuple>
    init {
        val params = mutableListOf<NameValuePair>()
        val arts = mutableListOf<NamePathS3Tuple>()
        if (pkgName.isNotEmpty()) {
            params.add(NameValuePair.of(CustomPipeline.S3_CONFIG_PREFIX, pkgName))
        }
        config.properties.forEach { (k, u) ->
            when (u.ui) {
                is FileUploader.FileUploaderWidget, is FileCopier.FileCopierWidget -> {
                    arts.add(NamePathS3Tuple(k))
                    params.add(NameValuePair.of(k, Utils.DEFAULT_FILE))
                }
                is BooleanInput.BooleanInputWidget -> {
                    params.add(NameValuePair.of(k, false))
                }
                is NumericInput.NumericInputWidget,
                is DateInput.DateInputWidget,
                -> {
                    params.add(NameValuePair.of(k, "-1"))
                }
                else -> params.add(NameValuePair.of(k, ""))
            }
        }
        parameters = params.toList()
        artifacts = arts.toList()
    }
}
