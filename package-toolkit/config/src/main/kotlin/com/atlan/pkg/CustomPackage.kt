/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg

import com.atlan.Atlan
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.config.model.ConfigMap
import com.atlan.pkg.config.model.PackageDefinition
import com.atlan.pkg.config.model.WorkflowTemplate
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.workflow.WorkflowContainer
import com.atlan.pkg.config.model.workflow.WorkflowOutputs
import com.atlan.pkg.config.model.workflow.WorkflowTemplateDefinition
import com.atlan.pkg.config.widgets.BooleanInput
import com.atlan.pkg.config.widgets.ConnectionCreator
import com.atlan.pkg.config.widgets.ConnectorTypeSelector
import com.atlan.pkg.config.widgets.DateInput
import com.atlan.pkg.config.widgets.DropDown
import com.atlan.pkg.config.widgets.MultipleGroups
import com.atlan.pkg.config.widgets.MultipleUsers
import com.atlan.pkg.config.widgets.NumericInput
import com.atlan.util.StringUtils
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.core.util.Separators
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
 * @param classToRun the class to run when the container executes (if this is supplied, containerCommand will be built for you)
 * @param containerCommand the full command to run in the container image, as a list rather than spaced (must be provided if you have not specified the class above)
 * @param containerImagePullPolicy (optional) override the default IfNotPresent policy
 * @param outputs (optional) any outputs that the custom package logic is expected to produce
 * @param keywords (optional) list of any keyword labels to apply to the package
 * @param allowSchedule (optional) whether to allow the package to be scheduled (default, true) or only run immediately (false)
 * @param certified (optional) whether the package should be listed as certified (default, true) or not (false)
 * @param preview (optional) whether the package should be labeled as an early preview in the UI (true) or not (default, false)
 * @param connectorType (optional) if the package needs to configure a connector, specify its type here
 * @param category name of the pill under which the package should be categorized in the marketplace in the UI
 */
open class CustomPackage(
    private val packageId: String,
    private val packageName: String,
    private val description: String,
    private val iconUrl: String,
    private val docsUrl: String,
    private val uiConfig: UIConfig,
    private val containerImage: String,
    private val classToRun: Class<*>? = null,
    private val containerCommand: List<String> = listOf(),
    private val containerImagePullPolicy: String = if (Atlan.VERSION.endsWith("SNAPSHOT")) "Always" else "IfNotPresent",
    private val outputs: WorkflowOutputs? = null,
    private val keywords: List<String> = listOf(),
    private val allowSchedule: Boolean = true,
    private val certified: Boolean = true,
    private val preview: Boolean = false,
    private val connectorType: AtlanConnectorType? = null,
    private val category: String = "custom",
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
        category,
    )

    @JsonIgnore val name = packageId.replace("@", "").replace("/", "-")
    private val configMap = ConfigMap(name, uiConfig)
    private val command: List<String>
    private val args: List<String>
    private val workflowTemplate: WorkflowTemplate

    init {
        val cmdList = if (classToRun != null) {
            listOf("/dumb-init", "--", "java", classToRun.canonicalName)
        } else {
            containerCommand
        }
        command = listOf(cmdList[0])
        args = cmdList.subList(1, cmdList.size)
        workflowTemplate = WorkflowTemplate(
            name,
            WorkflowTemplateDefinition(
                config = uiConfig,
                container = WorkflowContainer(
                    uiConfig,
                    containerImage,
                    command = command,
                    args = args,
                    containerImagePullPolicy,
                ),
                outputs = outputs,
                pkgName = name,
            ),
        )
    }

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
        return json.writer(pp).writeValueAsString(pkg)
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

    /**
     * Generates a data class from the package's UI configuration items.
     */
    fun cfgDataClass(className: String): String {
        val builder = StringBuilder()
        builder.append(
            """
            /* SPDX-License-Identifier: Apache-2.0
               Copyright 2023 Atlan Pte. Ltd. */
            import com.atlan.model.assets.Connection
            import com.atlan.pkg.CustomConfig
            import com.atlan.pkg.model.ConnectorAndConnections
            import com.atlan.pkg.serde.WidgetSerde
            import com.fasterxml.jackson.annotation.JsonAutoDetect
            import com.fasterxml.jackson.annotation.JsonProperty
            import com.fasterxml.jackson.databind.annotation.JsonDeserialize
            import com.fasterxml.jackson.databind.annotation.JsonSerialize
            import javax.annotation.processing.Generated;

            /**
             * Expected configuration for the $packageName custom package.
             */
            @Generated("com.atlan.pkg.CustomPackage")
            @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
            data class $className(
            """.trimIndent(),
        ).append("\n")
        uiConfig.properties.forEach { (k, u) ->
            var type = "String"
            when (u.ui) {
                is DropDown.DropDownWidget,
                is MultipleGroups.MultipleGroupsWidget,
                is MultipleUsers.MultipleUsersWidget,
                -> {
                    builder.append("    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)\n")
                    builder.append("    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)\n")
                    type = "List<String>"
                }
                is ConnectionCreator.ConnectionCreatorWidget -> {
                    builder.append("    @JsonDeserialize(using = WidgetSerde.ConnectionDeserializer::class)\n")
                    builder.append("    @JsonSerialize(using = WidgetSerde.ConnectionSerializer::class)\n")
                    type = "Connection"
                }
                is ConnectorTypeSelector.ConnectorTypeSelectorWidget -> {
                    builder.append("    @JsonDeserialize(using = WidgetSerde.ConnectorAndConnectionsDeserializer::class)\n")
                    builder.append("    @JsonSerialize(using = WidgetSerde.ConnectorAndConnectionsSerializer::class)\n")
                    type = "ConnectorAndConnections"
                }
                is BooleanInput.BooleanInputWidget -> {
                    type = "Boolean"
                }
                is NumericInput.NumericInputWidget -> {
                    type = "Number"
                }
                is DateInput.DateInputWidget -> {
                    type = "Long"
                }
            }
            builder.append("    @JsonProperty(\"$k\") val ${StringUtils.getLowerCamelCase(k)}: $type? = null,\n")
        }
        builder.append(
            """
            ) : CustomConfig()
            """.trimIndent(),
        ).append("\n")
        return builder.toString()
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
        val pp: DefaultPrettyPrinter = DefaultPrettyPrinter().withSeparators(Separators.createDefaultInstance().withObjectFieldValueSpacing(Separators.Spacing.AFTER))

        fun generate(pkg: CustomPackage, args: Array<String>) {
            when (args[0]) {
                "package" -> createPackageFiles(pkg, args.drop(1))
                "config" -> createConfigClass(pkg, args.drop(1))
            }
        }

        fun createPackageFiles(pkg: CustomPackage, args: List<String>): String {
            val path = args[0]
            val prefix = when {
                path.endsWith(File.separator) -> path
                else -> "$path${File.separator}"
            } + pkg.name + File.separator
            File(prefix).mkdirs()
            File(prefix + "index.js").writeText(pkg.indexJS())
            File(prefix + "package.json").writeText(pkg.packageJSON())
            File(prefix + "configmaps").mkdirs()
            File(prefix + "templates").mkdirs()
            File(prefix + "configmaps" + File.separator + "default.yaml").writeText(pkg.configMapYAML())
            File(prefix + "templates" + File.separator + "default.yaml").writeText(pkg.workflowTemplateYAML())
            return prefix
        }

        fun createConfigClass(pkg: CustomPackage, args: List<String>) {
            val path = args[0]
            val className = "${StringUtils.getUpperCamelCase(pkg.packageName)}Cfg"
            val filename = when {
                path.isEmpty() -> path
                path.endsWith(File.separator) -> path
                else -> path
            } + File.separator + "$className.kt"
            File(filename).writeText(pkg.cfgDataClass(className))
        }
    }
}
