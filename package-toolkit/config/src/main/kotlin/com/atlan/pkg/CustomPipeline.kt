/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg

import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.workflow.S3Artifact
import com.atlan.pkg.config.model.workflow.WorkflowOutputs

/**
 * Single class through which you can define a custom pipeline.
 *
 * @param packageId unique identifier for the pipeline, including its namespace
 * @param packageName display name for the pipeline, as it should be shown in the UI
 * @param description description for the pipeline, as it should be shown in the UI
 * @param iconUrl link to an icon to use for the pipeline, as it should be shown in the UI
 * @param docsUrl link to an online document describing the pipeline
 * @param uiConfig configuration for the UI of the custom pipeline
 * @param containerCommand (optional) command to run to sync the pipeline's configuration, as a list rather than string
 * @param containerImage container image to run the logic of the custom pipeline
 * @param containerImagePullPolicy (optional) override the default IfNotPresent policy
 * @param keywords (optional) list of any keyword labels to apply to the package
 * @param allowSchedule (optional) whether to allow the package to be scheduled (default, true) or only run immediately (false)
 * @param certified (optional) whether the package should be listed as certified (default, true) or not (false)
 * @param preview (optional) whether the package should be labeled as an early preview in the UI (true) or not (default, false)
 * @param connectorType (optional) if the package needs to configure a connector, specify its type here
 */
open class CustomPipeline(
    packageId: String,
    packageName: String,
    description: String,
    iconUrl: String,
    docsUrl: String,
    uiConfig: UIConfig,
    containerImage: String,
    containerCommand: List<String> = listOf("/dumb-init", "--", "java", "WriteConfig"),
    containerImagePullPolicy: String = "IfNotPresent",
    keywords: List<String> = listOf(),
    allowSchedule: Boolean = true,
    certified: Boolean = true,
    preview: Boolean = false,
    connectorType: AtlanConnectorType? = null,
) : CustomPackage(
    packageId = packageId,
    packageName = packageName,
    description = description,
    iconUrl = iconUrl,
    docsUrl = docsUrl,
    uiConfig = uiConfig,
    containerImage = containerImage,
    containerCommand = containerCommand,
    containerImagePullPolicy = containerImagePullPolicy,
    outputs = WorkflowOutputs(
        files = mapOf(
            "debug-logs" to "/tmp/debug.log",
        ),
        s3Objects = listOf(
            S3Artifact("config"),
            S3Artifact("runtime"),
        ),
    ),
    keywords = keywords,
    allowSchedule = allowSchedule,
    certified = certified,
    preview = preview,
    connectorType = connectorType,
) {
    companion object {
        const val S3_CONFIG_PREFIX = "output_prefix"
    }
}
