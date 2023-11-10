/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.pkg.CustomPackage
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.ui.UIRule
import com.atlan.pkg.config.model.ui.UIStep
import com.atlan.pkg.config.model.workflow.WorkflowOutputs
import com.atlan.pkg.config.widgets.NumericInput
import com.atlan.pkg.config.widgets.Radio
import com.atlan.pkg.config.widgets.TextInput

/**
 * Definition for the Asset Export (Basic) custom package.
 */
object AssetExportBasicPkg : CustomPackage(
    "@csa/asset-export-basic",
    "Asset Export (Basic)",
    "Export assets with all enrichment that could be made against them via the Atlan UI.",
    "http://assets.atlan.com/assets/ph-cloud-arrow-down-light.svg",
    "https://solutions.atlan.com/asset-export-basic/",
    uiConfig = UIConfig(
        steps = listOf(
            UIStep(
                title = "Configuration",
                description = "Export configuration",
                inputs = mapOf(
                    "export_scope" to Radio(
                        label = "Export scope",
                        required = true,
                        possibleValues = mapOf(
                            "ENRICHED_ONLY" to "Enriched only",
                            "ALL" to "All",
                        ),
                        default = "ENRICHED_ONLY",
                        help = "Whether to export only those assets that were enriched by users, or all assets with the qualified name prefix.",
                    ),
                    "qn_prefix" to TextInput(
                        label = "Qualified name prefix",
                        required = false,
                        help = "Starting value for a qualifiedName that will determine which assets to export.",
                        placeholder = "default",
                        grid = 6,
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
                    "batch_size" to NumericInput(
                        label = "Batch size",
                        required = false,
                        help = "Maximum number of results to process at a time (per API request).",
                        placeholder = "50",
                        grid = 4,
                    ),
                ),
            ),
        ),
        rules = listOf(
            UIRule(
                whenInputs = mapOf("control_config_strategy" to "advanced"),
                required = listOf("batch_size"),
            ),
        ),
    ),
    containerImage = "ghcr.io/atlanhq/csa-asset-export-basic:${Atlan.VERSION}",
    containerImagePullPolicy = "Always",
    containerCommand = listOf("/dumb-init", "--", "java", "ExporterKt"),
    outputs = WorkflowOutputs(
        mapOf(
            "debug-logs" to "/tmp/debug.log",
            "assets-csv" to "/tmp/asset-export.csv",
        ),
    ),
    keywords = listOf("kotlin", "utility"),
    preview = true,
) {
    @JvmStatic
    fun main(args: Array<String>) {
        createPackageFiles("generated-packages/asset-export-basic")
    }
}
