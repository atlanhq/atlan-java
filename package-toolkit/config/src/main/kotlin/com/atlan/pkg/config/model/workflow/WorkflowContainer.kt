/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.workflow

import com.atlan.pkg.config.model.ui.UIConfig
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
        val nestedConfig = mutableListOf<String>()
        val builder = mutableListOf<NamedPair>()
        builder.add(NameValuePair("ATLAN_BASE_URL", "INTERNAL"))
        builder.add(NameValuePair("ATLAN_USER_ID", "{{=sprig.dig('labels', 'workflows', 'argoproj', 'io/creator', '', workflow)}}"))
        builder.add(NameValuePair("X_ATLAN_AGENT", "workflow"))
        builder.add(NameValuePair("X_ATLAN_AGENT_ID", "{{workflow.name}}"))
        builder.add(NameValuePair("X_ATLAN_AGENT_PACKAGE_NAME", "{{=sprig.dig('annotations', 'package', 'argoproj', 'io/name', '', workflow)}}"))
        builder.add(NameValuePair("X_ATLAN_AGENT_WORKFLOW_ID", "{{=sprig.dig('labels', 'workflows', 'argoproj', 'io/workflow-template', '', workflow)}}"))
        builder.add(NamedSecret("CLIENT_ID", "argo-client-creds", "login"))
        builder.add(NamedSecret("CLIENT_SECRET", "argo-client-creds", "password"))
        config.properties.forEach { (k, u) ->
            if (u.ui.widget == "fileUpload") {
                builder.add(NameValuePair(k.uppercase(), NamePathS3Tuple(k).path))
            } else {
                builder.add(NameValuePair(k.uppercase(), "{{inputs.parameters.$k}}"))
            }
            nestedConfig.add(k)
        }
        builder.add(NestedConfig("NESTED_CONFIG", nestedConfig))
        env = builder.toList()
    }
}
