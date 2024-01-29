/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg

import com.atlan.Atlan
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.config.model.Pipeline
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.workflow.S3Artifact
import com.atlan.pkg.config.model.workflow.WorkflowOutputs
import java.io.File

/**
 * Single class through which you can define a custom pipeline.
 *
 * @param packageId unique identifier for the pipeline, including its namespace
 * @param packageName display name for the pipeline, as it should be shown in the UI
 * @param description description for the pipeline, as it should be shown in the UI
 * @param iconUrl link to an icon to use for the pipeline, as it should be shown in the UI
 * @param docsUrl link to an online document describing the pipeline
 * @param uiConfig configuration for the UI of the custom pipeline
 * @param logicClass the class to run when the pipeline executes (if this is supplied, logicCommand will be built for you)
 * @param logicCommand command to run the pipeline's custom processing logic, as a list rather than string
 * @param configCommand (optional) command to run to sync the pipeline's configuration, as a list rather than string
 * @param containerImage container image to run the logic of the custom pipeline
 * @param containerImagePullPolicy (optional) override the default IfNotPresent policy
 * @param filter (optional) sprig expression used to filter the messages that will be processed by the pipeline
 * @param minMemory (optional) minimum amount of memory to allocate to the custom logic for the pipeline, in megabytes (default: 128)
 * @param maxMemory (optional) maximum amount of memory to allocate to the custom logic for the pipeline, in megabytes (default: 256)
 * @param keywords (optional) list of any keyword labels to apply to the package
 * @param certified (optional) whether the package should be listed as certified (default, true) or not (false)
 * @param preview (optional) whether the package should be labeled as an early preview in the UI (true) or not (default, false)
 * @param connectorType (optional) if the package needs to configure a connector, specify its type here
 * @param category name of the pill under which the package should be categorized in the marketplace in the UI
 */
open class CustomPipeline(
    packageId: String,
    packageName: String,
    description: String,
    iconUrl: String,
    docsUrl: String,
    uiConfig: UIConfig,
    containerImage: String,
    private val logicClass: Class<*>? = null,
    private val logicCommand: List<String> = listOf(),
    private val configClass: Class<*>? = null,
    private val configCommand: List<String> = listOf("/dumb-init", "--", "java", "com.atlan.pkg.events.WriteConfig"),
    containerImagePullPolicy: String = if (Atlan.VERSION.endsWith("SNAPSHOT")) "Always" else "IfNotPresent",
    private val filter: String = "",
    private val minMemory: Int = 128,
    private val maxMemory: Int = 256,
    keywords: List<String> = listOf(),
    certified: Boolean = true,
    preview: Boolean = false,
    connectorType: AtlanConnectorType? = null,
    category: String = "always-on",
) : CustomPackage(
    packageId = packageId,
    packageName = packageName,
    description = description,
    iconUrl = iconUrl,
    docsUrl = docsUrl,
    uiConfig = uiConfig,
    containerImage = containerImage,
    classToRun = configClass,
    containerCommand = configCommand,
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
    allowSchedule = false,
    certified = certified,
    preview = preview,
    connectorType = connectorType,
    category = category,
) {
    private val pipeline = Pipeline(
        packageId,
        name,
        containerImage,
        logicClass,
        logicCommand,
        containerImagePullPolicy,
        description,
        filter,
        minMemory,
        maxMemory,
    )

    /**
     * Retrieve the YAML for the WorkflowTemplate of the custom package.
     *
     * @return templates/default.yaml content
     */
    fun pipelineYAML(): String {
        return yaml.writeValueAsString(pipeline)
    }

    companion object {
        const val S3_CONFIG_PREFIX = "output_prefix"
        fun generate(pkg: CustomPipeline, args: Array<String>) {
            CustomPackage.generate(pkg, args)
            if (args[0] == "package") {
                createPipelineFiles(pkg, args.drop(1))
            }
        }
        fun createPipelineFiles(pkg: CustomPipeline, args: List<String>) {
            val prefix = createPackageFiles(pkg, args)
            File(prefix + "pipelines").mkdirs()
            File(prefix + "pipelines" + File.separator + "default.yaml").writeText(pkg.pipelineYAML())
        }
    }
}
