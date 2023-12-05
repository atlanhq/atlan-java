/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.pkg.CustomPackage
import com.atlan.pkg.config.model.ui.UIConfig
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
                    "include_glossaries" to BooleanInput(
                        label = "Include glossaries?",
                        required = true,
                        help = "Whether glossaries (and their terms and categories) should be exported, too.",
                        grid = 4,
                    ),
                ),
            ),
        ),
    ),
    containerImage = "ghcr.io/atlanhq/csa-asset-export-basic:${Atlan.VERSION}",
    containerCommand = listOf("/dumb-init", "--", "java", "Exporter", "/tmp"),
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
