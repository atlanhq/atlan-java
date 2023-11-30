/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.pkg.CustomPackage
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.ui.UIRule
import com.atlan.pkg.config.model.ui.UIStep
import com.atlan.pkg.config.model.workflow.WorkflowOutputs
import com.atlan.pkg.config.widgets.DropDown
import com.atlan.pkg.config.widgets.Radio
import com.atlan.pkg.config.widgets.TextInput

/**
 * Definition for the DuplicateDetector custom package.
 */
object PackageConfig : CustomPackage(
    "@csa/duplicate-detector",
    "Duplicate Detector",
    "Detect possible duplicate tables, views and materialized views based on their columns.",
    "http://assets.atlan.com/assets/ph-copy-light.svg",
    "https://solutions.atlan.com/duplicate-detector/",
    uiConfig = UIConfig(
        steps = listOf(
            UIStep(
                title = "Configuration",
                description = "Duplicate detection configuration",
                inputs = mapOf(
                    "glossary_name" to TextInput(
                        label = "Glossary name",
                        required = true,
                        help = "Name for the glossary where the duplicate sets of assets will be recorded and tracked.",
                        placeholder = "Duplicate assets",
                        grid = 4,
                    ),
                    "qn_prefix" to TextInput(
                        label = "Qualified name prefix",
                        required = false,
                        help = "Starting value for a qualifiedName that will determine which assets to check for duplicates.",
                        placeholder = "default",
                        grid = 4,
                    ),
                    "control_config_strategy" to Radio(
                        label = "Options",
                        required = true,
                        possibleValues = mapOf(
                            "default" to "Default",
                            "advanced" to "Advanced",
                        ),
                        default = "default",
                        help = "Options to optimize how the utility runs.",
                    ),
                    "asset_types" to DropDown(
                        label = "Asset types",
                        required = false,
                        possibleValues = mapOf(
                            "Table" to "Table",
                            "View" to "View",
                            "MaterialisedView" to "Materialized view",
                        ),
                        help = "Select asset types to check for duplicates.",
                        multiSelect = true,
                        grid = 4,
                    ),
                ),
            ),
        ),
        rules = listOf(
            UIRule(
                whenInputs = mapOf("control_config_strategy" to "advanced"),
                required = listOf("asset_types"),
            ),
        ),
    ),
    containerImage = "ghcr.io/atlanhq/csa-duplicate-detector:${Atlan.VERSION}",
    containerImagePullPolicy = "Always",
    containerCommand = listOf("/dumb-init", "--", "java", "DuplicateDetector"),
    outputs = WorkflowOutputs(mapOf("debug-logs" to "/tmp/debug.log")),
    keywords = listOf("kotlin", "utility"),
    preview = true,
) {
    @JvmStatic
    fun main(args: Array<String>) {
        generate(this, args)
    }
}
