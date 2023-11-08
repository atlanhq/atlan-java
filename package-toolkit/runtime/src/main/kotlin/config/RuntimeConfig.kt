/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package config

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * All runtime configuration comes through these settings:
 *
 * @param userId user through whom to impersonate any activities
 * @param agent type of agent being used (for example, workflow)
 * @param agentId unique ID of the agent
 * @param agentPackageName unique name of the package that runs the agent
 * @param agentWorkflowId unique instance of the run of the package
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class RuntimeConfig(
    @JsonProperty("user-id") val userId: String?,
    @JsonProperty("x-atlan-agent") val agent: String?,
    @JsonProperty("x-atlan-agent-id") val agentId: String?,
    @JsonProperty("x-atlan-agent-package-name") val agentPackageName: String?,
    @JsonProperty("x-atlan-agent-workflow-id") val agentWorkflowId: String?,
)
