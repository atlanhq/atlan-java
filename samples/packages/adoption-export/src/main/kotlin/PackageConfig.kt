/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.pkg.CustomPackage
import com.atlan.pkg.adoption.AdoptionExporter
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.ui.UIRule
import com.atlan.pkg.config.model.ui.UIStep
import com.atlan.pkg.config.model.workflow.WorkflowOutputs
import com.atlan.pkg.config.widgets.BooleanInput
import com.atlan.pkg.config.widgets.NumericInput
import com.atlan.pkg.config.widgets.Radio
import com.atlan.pkg.config.widgets.TextInput

/**
 * Definition for the Adoption Export custom package.
 */
object PackageConfig : CustomPackage(
    "@csa/adoption-export",
    "Adoption Export",
    "Exports key details about Atlan adoption from a tenant.",
    "https://assets.atlan.com/assets/ph-trend-up-light.svg",
    "https://solutions.atlan.com/adoption-export/",
    uiConfig = UIConfig(
        listOf(
            UIStep(
                title = "Scope",
                description = "What to include",
                inputs = mapOf(
                    "include_searches" to BooleanInput(
                        label = "Include searches?",
                        help = "Whether to include searches users have run (Yes) or not (No).",
                        required = true,
                        grid = 4,
                    ),
                    "maximum_searches" to NumericInput(
                        label = "Maximum",
                        help = "Maximum number of searches to include for the most-run searches.",
                        required = false,
                        placeholder = "50",
                        grid = 4,
                    ),
                    "include_changes" to BooleanInput(
                        label = "Include asset changes?",
                        help = "Whether to include changes to assets (Yes) or not (No).",
                        required = true,
                        grid = 4,
                    ),
                    "include_views" to Radio(
                        label = "Include most-viewed assets?",
                        help = "If including most-viewed assets, whether to include number of distinct users or raw view count as more important.",
                        required = true,
                        possibleValues = mapOf(
                            "BY_USERS" to "By most unique users",
                            "BY_VIEWS" to "By raw views",
                            "NONE" to "No",
                        ),
                        default = "BY_VIEWS",
                    ),
                    "maximum_assets" to NumericInput(
                        label = "Maximum assets",
                        help = "Maximum number of results to include for the most-viewed assets.",
                        required = false,
                        placeholder = "100",
                        grid = 4,
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
                whenInputs = mapOf("include_views" to "BY_USERS"),
                required = listOf("maximum_assets"),
            ),
            UIRule(
                whenInputs = mapOf("include_views" to "BY_VIEWS"),
                required = listOf("maximum_assets"),
            ),
        ),
    ),
    containerImage = "ghcr.io/atlanhq/csa-adoption-export:${Atlan.VERSION}",
    classToRun = AdoptionExporter::class.java,
    outputs = WorkflowOutputs(
        mapOf(
            "debug-logs" to "/tmp/debug.log",
            "admin-export" to "/tmp/adoption-export.xlsx",
        ),
    ),
    keywords = listOf("kotlin", "utility", "adoption", "export"),
    preview = true,
) {
    @JvmStatic
    fun main(args: Array<String>) {
        generate(this, args)
    }
}
