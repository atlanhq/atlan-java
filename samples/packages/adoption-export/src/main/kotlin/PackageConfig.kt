/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.pkg.CustomPackage
import com.atlan.pkg.adoption.AdoptionExporter
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.ui.UIRule
import com.atlan.pkg.config.model.ui.UIStep
import com.atlan.pkg.config.model.workflow.WorkflowOutputs
import com.atlan.pkg.config.widgets.DateInput
import com.atlan.pkg.config.widgets.DropDown
import com.atlan.pkg.config.widgets.MultipleUsers
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
    "https://assets.atlan.com/assets/ph-heartbeat-light.svg",
    "https://solutions.atlan.com/adoption-export/",
    uiConfig = UIConfig(
        listOf(
            UIStep(
                title = "Views",
                description = "Asset views",
                inputs = mapOf(
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
                    "views_max" to NumericInput(
                        label = "Maximum assets",
                        help = "Maximum number of results to include for the most-viewed assets.",
                        required = false,
                        placeholder = "100",
                        grid = 4,
                    ),
                ),
            ),
            UIStep(
                title = "Changes",
                description = "Asset changes",
                inputs = mapOf(
                    "include_changes" to Radio(
                        label = "Include asset changes?",
                        help = "Whether to include changes to assets (Yes) or not (No).",
                        required = true,
                        possibleValues = mapOf(
                            "YES" to "Yes",
                            "NO" to "No",
                        ),
                        default = "NO",
                    ),
                    "changes_by_user" to MultipleUsers(
                        label = "Limit to user",
                        help = "Only extract changes by the selected users (leave empty for all users).",
                        required = false,
                        grid = 4,
                    ),
                    "changes_types" to DropDown(
                        label = "Limit by action",
                        help = "Only extract changes of the selected kind(s).",
                        possibleValues = mapOf(
                            "ENTITY_CREATE" to "Asset created",
                            "ENTITY_UPDATE" to "Asset updated",
                            "ENTITY_DELETE" to "Asset archived",
                            "BUSINESS_ATTRIBUTE_UPDATE" to "Custom metadata updated",
                            "CLASSIFICATION_ADD" to "Asset tagged (directly)",
                            "PROPAGATED_CLASSIFICATION_ADD" to "Asset tagged (propagated)",
                            "CLASSIFICATION_DELETE" to "Asset tag removed (directly)",
                            "PROPAGATED_CLASSIFICATION_DELETE" to "Asset tag removed (propagated)",
                            "CLASSIFICATION_UPDATE" to "Asset tag updated (directly)",
                            "PROPAGATED_CLASSIFICATION_UPDATE" to "Asset tag updated (propagated)",
                            "TERM_ADD" to "Term assigned",
                            "TERM_DELETE" to "Term unassigned",
                        ),
                        multiSelect = true,
                        required = false,
                        grid = 4,
                    ),
                    "changes_from" to DateInput(
                        label = "From date",
                        help = "Only extract changes after the specified date (leave empty for all changes).",
                        required = false,
                        min = -90, // start as far back as 90 days ago
                        max = -1, // maximum from would be yesterday
                        default = -90,
                        grid = 4,
                    ),
                    "changes_to" to DateInput(
                        label = "To date",
                        help = "Only extract changes before the specified date (leave empty for all changes).",
                        required = false,
                        min = -89, // maximum to would be 89 days ago
                        default = 0, // start with today
                        grid = 4,
                    ),
                    "changes_max" to NumericInput(
                        label = "Maximum assets",
                        help = "Maximum number of assets for which to calculate the number of changes made.",
                        required = false,
                        placeholder = "100",
                        grid = 4,
                    ),
                ),
            ),
            UIStep(
                title = "Searches",
                description = "Asset searches",
                inputs = mapOf(
                    "include_searches" to Radio(
                        label = "Include user searches?",
                        help = "Whether to include searches users have run (Yes) or not (No).",
                        required = true,
                        possibleValues = mapOf(
                            "YES" to "Yes",
                            "NO" to "No",
                        ),
                        default = "NO",
                    ),
                    "maximum_searches" to NumericInput(
                        label = "Maximum",
                        help = "Maximum number of searches to include for the most-run searches.",
                        required = false,
                        placeholder = "50",
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
                required = listOf("views_max"),
            ),
            UIRule(
                whenInputs = mapOf("include_views" to "BY_VIEWS"),
                required = listOf("views_max"),
            ),
            UIRule(
                whenInputs = mapOf("include_changes" to "YES"),
                required = listOf("changes_by_user", "changes_types", "changes_from", "changes_to", "changes_max"),
            ),
            UIRule(
                whenInputs = mapOf("include_searches" to "YES"),
                required = listOf("maximum_searches"),
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
