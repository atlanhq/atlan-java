/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg

import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.config.model.ConfigMap
import com.atlan.pkg.config.model.PackageDefinition
import com.atlan.pkg.config.model.WorkflowTemplate
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.workflow.WorkflowContainer
import com.atlan.pkg.config.model.workflow.WorkflowOutputs
import com.atlan.pkg.config.model.workflow.WorkflowTemplateDefinition
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File

/**
 * Single class through which you can define a custom package.
 *
 * @param packageId unique identifier for the package, including its namespace
 * @param packageName display name for the package, as it should be shown in the UI
 * @param description description for the package, as it should be shown in the UI
 * @param iconUrl link to an icon to use for the package, as it should be shown in the UI
 * @param docsUrl link to an online document describing the package
 * @param uiConfig configuration for the UI of the custom package
 * @param containerImage container image to run the logic of the custom package
 * @param containerCommand the full command to run in the container image, as a list rather than spaced
 * @param containerImagePullPolicy (optional) override the default IfNotPresent policy
 * @param outputs (optional) any outputs that the custom package logic is expected to produce
 * @param keywords (optional) list of any keyword labels to apply to the package
 * @param allowSchedule (optional) whether to allow the package to be scheduled (default, true) or only run immediately (false)
 * @param certified (optional) whether the package should be listed as certified (default, true) or not (false)
 * @param preview (optional) whether the package should be labeled as an early preview in the UI (true) or not (default, false)
 * @param connectorType (optional) if the package needs to configure a connector, specify its type here
 */
open class CustomPackage(
    private val packageId: String,
    private val packageName: String,
    private val description: String,
    private val iconUrl: String,
    private val docsUrl: String,
    private val uiConfig: UIConfig,
    private val containerImage: String,
    private val containerCommand: List<String>,
    private val containerImagePullPolicy: String = "IfNotPresent",
    private val outputs: WorkflowOutputs? = null,
    private val keywords: List<String> = listOf(),
    private val allowSchedule: Boolean = true,
    private val certified: Boolean = true,
    private val preview: Boolean = false,
    private val connectorType: AtlanConnectorType? = null,
) {
    private val pkg = PackageDefinition(
        packageId,
        packageName,
        description,
        iconUrl,
        docsUrl,
        keywords,
        allowSchedule,
        certified,
        preview,
        connectorType,
    )
    private val name = packageId.replace("@", "").replace("/", "-")
    private val configMap = ConfigMap(name, uiConfig)
    private val workflowTemplate = WorkflowTemplate(
        name,
        WorkflowTemplateDefinition(
            uiConfig,
            WorkflowContainer(
                uiConfig,
                containerImage,
                command = listOf(containerCommand[0]),
                args = containerCommand.subList(1, containerCommand.size),
                containerImagePullPolicy,
            ),
            outputs,
            name,
        ),
    )

    /**
     * Retrieve the JavaScript for the index.js of the custom package.
     *
     * @return index.js content
     */
    fun indexJS(): String {
        return """
            function dummy() {
                console.log("don't call this.")
            }
            module.exports = dummy;
        """.trimIndent()
    }

    /**
     * Retrieve the JSON for the package.json of the custom package.
     *
     * @return package.json content
     */
    fun packageJSON(): String {
        return json.writerWithDefaultPrettyPrinter().writeValueAsString(pkg)
    }

    /**
     * Retrieve the YAML for the ConfigMap of the custom package.
     *
     * @return configmaps/default.yaml content
     */
    fun configMapYAML(): String {
        return yaml.writeValueAsString(configMap)
    }

    /**
     * Retrieve the YAML for the WorkflowTemplate of the custom package.
     *
     * @return templates/default.yaml content
     */
    fun workflowTemplateYAML(): String {
        return yaml.writeValueAsString(workflowTemplate)
    }

    companion object {
        val yaml = YAMLMapper.builder()
            .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
            .disable(YAMLGenerator.Feature.SPLIT_LINES)
            .enable(YAMLGenerator.Feature.INDENT_ARRAYS_WITH_INDICATOR)
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
            .enable(YAMLGenerator.Feature.LITERAL_BLOCK_STYLE)
            .build()
            .registerKotlinModule()
        val json: ObjectMapper = jacksonObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)

        fun createPackageFiles(pkg: CustomPackage, path: String = "generated-packages") {
            val prefix = when {
                path.isEmpty() -> path
                path.endsWith(File.separator) -> path
                else -> path + File.separator
            } + File.separator + pkg.name + File.separator
            File(prefix).mkdirs()
            File(prefix + "index.js").writeText(pkg.indexJS())
            File(prefix + "package.json").writeText(pkg.packageJSON())
            File(prefix + "configmaps").mkdirs()
            File(prefix + "templates").mkdirs()
            File(prefix + "configmaps" + File.separator + "default.yaml").writeText(pkg.configMapYAML())
            File(prefix + "templates" + File.separator + "default.yaml").writeText(pkg.workflowTemplateYAML())
        }
    }
}
