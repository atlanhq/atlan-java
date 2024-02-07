/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.pkg.CustomPackage
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.ui.UIRule
import com.atlan.pkg.config.model.ui.UIStep
import com.atlan.pkg.config.model.workflow.WorkflowOutputs
import com.atlan.pkg.config.widgets.BooleanInput
import com.atlan.pkg.config.widgets.Radio
import com.atlan.pkg.config.widgets.TextInput

/**
 * Definition for the Asset Export (Basic) custom package.
 */
object PackageConfig : CustomPackage(
    "@csa/asset-export-basic",
    "Asset Export (Basic)",
    "Export assets with all enrichment that could be made against them via the Atlan UI.",
    "http://assets.atlan.com/assets/ph-cloud-arrow-down-light.svg",
    "https://solutions.atlan.com/asset-export-basic/",
    uiConfig = UIConfig(
        steps = listOf(
            UIStep(
                title = "Scope",
                description = "Assets to include",
                inputs = mapOf(
                    "export_scope" to Radio(
                        label = "Export scope",
                        required = true,
                        possibleValues = mapOf(
                            "GLOSSARIES_ONLY" to "Glossaries only",
                            "ENRICHED_ONLY" to "Enriched only",
                            "ALL" to "All",
                        ),
                        default = "ENRICHED_ONLY",
                        help = "Whether to export only those assets that were enriched by users, or all assets with the qualified name prefix.",
                    ),
                    "qn_prefix" to TextInput(
                        label = "Qualified name prefix (for assets)",
                        required = false,
                        help = "Starting value for a qualifiedName that will determine which assets to export.",
                        placeholder = "default",
                        grid = 6,
                    ),
                    "include_description" to BooleanInput(
                        label = "Include description?",
                        required = false,
                        help = "Whether to extract only user-entered description (No), or to also include system-level description (Yes).",
                        grid = 4,
                    ),
                    "include_glossaries" to BooleanInput(
                        label = "Include glossaries?",
                        required = false,
                        help = "Whether glossaries (and their terms and categories) should be exported, too.",
                        grid = 4,
                    ),
                    "asset_types_to_include" to TextInput(
                        label = "Asset types to include",
                        required = false,
                        hidden = true,
                        help = "Comma-separated list of asset types to include in the export.",
                    ),
                    "attributes_to_include" to TextInput(
                        label = "Attributes to include",
                        required = false,
                        hidden = true,
                        help = "Comma-separated list of attributes to include in the export.",
                    ),
                ),
            ),
            UIStep(
                title = "Delivery",
                description = "Where to send",
                inputs = mapOf(
                    "email_addresses" to TextInput(
                        label = "Email address(es)",
                        help = "Provide any email addresses you want the extract sent to, separated by commas.",
                        required = false,
                        placeholder = "one@example.com,two@example.com",
                    ),
                ),
            ),
        ),
        rules = listOf(
            UIRule(
                whenInputs = mapOf("export_scope" to "ENRICHED_ONLY"),
                required = listOf("qn_prefix", "include_description", "include_glossaries"),
            ),
            UIRule(
                whenInputs = mapOf("export_scope" to "ALL"),
                required = listOf("qn_prefix", "include_description", "include_glossaries"),
            ),
        ),
    ),
    containerImage = "ghcr.io/atlanhq/csa-asset-export-basic:${Atlan.VERSION}",
    classToRun = Exporter::class.java,
    outputs = WorkflowOutputs(
        mapOf(
            "debug-logs" to "/tmp/debug.log",
            "assets-csv" to "/tmp/asset-export.csv",
            "glossaries-csv" to "/tmp/glossary-export.csv",
        ),
    ),
    keywords = listOf("kotlin", "utility"),
    preview = true,
) {
    @JvmStatic
    fun main(args: Array<String>) {
        generate(this, args)
    }
}
