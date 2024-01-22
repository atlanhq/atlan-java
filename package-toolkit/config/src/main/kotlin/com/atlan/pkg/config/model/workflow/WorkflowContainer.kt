/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.workflow

import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.widgets.FileCopier
import com.atlan.pkg.config.widgets.FileUploader
import com.atlan.pkg.config.widgets.Widget
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("image", "imagePullPolicy", "command", "args", "env")
class WorkflowContainer(
    @JsonIgnore val config: UIConfig,
    val image: String,
    val command: List<String>,
    val args: List<String> = listOf(),
    val imagePullPolicy: String = "IfNotPresent",
) {
    val env: List<NamedPair>
    init {
        val nestedConfig = mutableMapOf<String, Widget>()
        val builder = mutableListOf<NamedPair>()
        builder.add(NameValuePair("ATLAN_BASE_URL", "INTERNAL"))
        builder.add(NameValuePair("ATLAN_USER_ID", "{{=sprig.dig('labels', 'workflows', 'argoproj', 'io/creator', '', workflow)}}"))
        builder.add(NameValuePair("X_ATLAN_AGENT", "workflow"))
        builder.add(NameValuePair("X_ATLAN_AGENT_ID", "{{workflow.name}}"))
        builder.add(NameValuePair("X_ATLAN_AGENT_PACKAGE_NAME", "{{=sprig.dig('annotations', 'package', 'argoproj', 'io/name', '', workflow)}}"))
        builder.add(NameValuePair("X_ATLAN_AGENT_WORKFLOW_ID", "{{=sprig.dig('labels', 'workflows', 'argoproj', 'io/workflow-template', '', workflow)}}"))
        builder.add(ConfigMapEntry("AWS_S3_BUCKET_NAME", "atlan-defaults", "bucket"))
        builder.add(ConfigMapEntry("AWS_S3_REGION", "atlan-defaults", "region"))
        builder.add(NamedSecret("CLIENT_ID", "argo-client-creds", "login"))
        builder.add(NamedSecret("CLIENT_SECRET", "argo-client-creds", "password"))
        builder.add(NamedSecret("SMTP_HOST", "support-smtp-creds", "host"))
        builder.add(NamedSecret("SMTP_PORT", "support-smtp-creds", "port"))
        builder.add(NamedSecret("SMTP_FROM", "support-smtp-creds", "from"))
        builder.add(NamedSecret("SMTP_USER", "support-smtp-creds", "login"))
        builder.add(NamedSecret("SMTP_PASS", "workflow-parameter-store", "smtp_password"))
        config.properties.forEach { (k, u) ->
            when (u.ui) {
                is FileUploader.FileUploaderWidget, is FileCopier.FileCopierWidget -> {
                    builder.add(NameValuePair(k.uppercase(), NamePathS3Tuple(k).path))
                }
                else -> {
                    builder.add(NameValuePair(k.uppercase(), "{{inputs.parameters.$k}}"))
                }
            }
            nestedConfig[k] = u.ui
        }
        builder.add(NestedConfig("NESTED_CONFIG", nestedConfig))
        env = builder.toList()
    }
}
