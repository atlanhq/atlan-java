/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.workflow

import com.atlan.pkg.CustomPipeline
import com.atlan.pkg.Utils
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.widgets.BooleanInput
import com.atlan.pkg.config.widgets.DateInput
import com.atlan.pkg.config.widgets.FileCopier
import com.atlan.pkg.config.widgets.FileUploader
import com.atlan.pkg.config.widgets.NumericInput
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude

class WorkflowInputs(
    @JsonIgnore val config: UIConfig,
    @JsonIgnore val pkgName: String = "",
    @JsonIgnore val fileInputSetup: Boolean = false,
) {
    val parameters: List<NamedPair>

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val artifacts: List<NamePathS3Tuple>
    init {
        val params = mutableListOf<NamedPair>()
        val arts = mutableListOf<NamePathS3Tuple>()
        if (pkgName.isNotEmpty()) {
            params.add(NameValuePair(CustomPipeline.S3_CONFIG_PREFIX, pkgName))
            if (fileInputSetup) {
                params.add(ConfigMapEntry("is_azure_artifacts", "atlan-defaults", "azure_artifacts", "false"))
                params.add(ConfigMapEntry("cloud_provider", "atlan-defaults", "cloud", "aws"))
            }
        }
        config.properties.forEach { (k, u) ->
            when (u.ui) {
                is FileUploader.FileUploaderWidget, is FileCopier.FileCopierWidget -> {
                    arts.add(NamePathS3Tuple(k))
                    if (fileInputSetup) {
                        params.add(NameValuePair(k, "{}"))
                        params.add(NameValuePair("${k}_key", "{{= sprig.dig('fileKey', '${Utils.DEFAULT_FILE}', sprig.mustFromJson(inputs.parameters.$k)) }}"))
                        params.add(NameValuePair("${k}_id", "{{= sprig.dig('fileId', '', sprig.mustFromJson(inputs.parameters.$k)) }}"))
                    } else {
                        params.add(NameValuePair(k, Utils.DEFAULT_FILE))
                    }
                }
                is BooleanInput.BooleanInputWidget -> {
                    params.add(NameValueBoolPair(k, false))
                }
                is NumericInput.NumericInputWidget,
                is DateInput.DateInputWidget,
                -> {
                    params.add(NameValuePair(k, "-1"))
                }
                else -> params.add(NameValuePair(k, ""))
            }
        }
        parameters = params.toList()
        artifacts = if (fileInputSetup) emptyList() else arts.toList()
    }
}
