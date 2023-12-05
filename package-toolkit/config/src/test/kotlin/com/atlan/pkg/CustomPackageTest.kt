/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg

import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.ui.UIRule
import com.atlan.pkg.config.model.ui.UIStep
import com.atlan.pkg.config.model.workflow.WorkflowOutputs
import com.atlan.pkg.config.widgets.ConnectionCreator
import com.atlan.pkg.config.widgets.ConnectionSelector
import com.atlan.pkg.config.widgets.Radio
import com.atlan.pkg.config.widgets.TextInput
import kotlin.test.Test
import kotlin.test.assertNotNull

class CustomPackageTest {

    companion object {
        private val uiConfig = UIConfig(
            steps = listOf(
                UIStep(
                    title = "Configuration",
                    description = "OpenAPI spec configuration",
                    inputs = mapOf(
                        "spec_url" to TextInput(
                            label = "Specification URL",
                            required = true,
                            help = "Full URL to the JSON form of the OpenAPI specification.",
                            placeholder = "https://petstore3.swagger.io/api/v3/openapi.json",
                        ),
                    ),
                ),
                UIStep(
                    title = "Connection",
                    description = "Connection details",
                    inputs = mapOf(
                        "connection_usage" to Radio(
                            label = "Connection",
                            required = true,
                            possibleValues = mapOf(
                                "CREATE" to "Create",
                                "REUSE" to "Reuse",
                            ),
                            default = "REUSE",
                            help = "Whether to create a new connection to hold these API assets, or reuse an existing connection.",
                        ),
                        "connection" to ConnectionCreator(
                            label = "Connection",
                            required = true,
                            help = "Enter details for a new connection to be created.",
                            placeholder = "Swagger",
                        ),
                        "connection_qualified_name" to ConnectionSelector(
                            label = "Connection",
                            required = true,
                            help = "Select an existing connection to load assets into.",
                        ),
                    ),
                ),
            ),
            rules = listOf(
                UIRule(
                    whenInputs = mapOf("connection_usage" to "REUSE"),
                    required = listOf("connection_qualified_name"),
                ),
                UIRule(
                    whenInputs = mapOf("connection_usage" to "CREATE"),
                    required = listOf("connection"),
                ),
            ),
        )

        private val pkg = CustomPackage(
            "@csa/openapi-spec-loader",
            "OpenAPI Spec Loader",
            "Loads API specs and paths from an OpenAPI (v3) definition.",
            "http://assets.atlan.com/assets/apispec.png",
            "https://developer.atlan.com/samples/loaders/openapi/",
            uiConfig,
            "ghcr.io/atlanhq/atlan-kotlin-samples:0.3.0",
            containerCommand = listOf("/dumb-init", "--", "java", "OpenAPISpecLoaderKt"),
            outputs = WorkflowOutputs(mapOf("debug-logs" to "/tmp/debug.log")),
            keywords = listOf("kotlin", "crawler", "openapi"),
            preview = true,
            connectorType = AtlanConnectorType.API,
        )
    }

    @Test
    fun serializeIndex() {
        val index = pkg.indexJS()
        assertNotNull(index)
        print(index)
    }

    @Test
    fun serializePackage() {
        val packageJSON = pkg.packageJSON()
        assertNotNull(packageJSON)
        print(packageJSON)
    }

    @Test
    fun serializeConfig() {
        val configMap = pkg.configMapYAML()
        assertNotNull(configMap)
        print(configMap)
    }

    @Test
    fun serializeWorkflow() {
        val workflowTemplate = pkg.workflowTemplateYAML()
        assertNotNull(workflowTemplate)
        print(workflowTemplate)
    }
}
