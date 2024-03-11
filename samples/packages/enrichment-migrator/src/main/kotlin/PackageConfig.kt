/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.model.assets.Asset
import com.atlan.pkg.CustomPackage
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.ui.UIRule
import com.atlan.pkg.config.model.ui.UIStep
import com.atlan.pkg.config.model.workflow.WorkflowOutputs
import com.atlan.pkg.config.widgets.BooleanInput
import com.atlan.pkg.config.widgets.ConnectionSelector
import com.atlan.pkg.config.widgets.DropDown
import com.atlan.pkg.config.widgets.NumericInput
import com.atlan.pkg.config.widgets.Radio
import com.atlan.pkg.config.widgets.TextInput

/**
 * Definition for the Enrichment Migrator custom package.
 */
object PackageConfig : CustomPackage(
    "@csa/enrichment-migrator",
    "Enrichment Migrator",
    "Migrate enrichment from one set of assets to another.",
    "http://assets.atlan.com/assets/ph-flow-arrow-light.svg",
    "https://solutions.atlan.com/enrichment-migrator/",
    uiConfig = UIConfig(
        steps = listOf(
            UIStep(
                title = "Assets",
                description = "Assets to include",
                inputs = mapOf(
                    "source_connection" to ConnectionSelector(
                        label = "Source",
                        required = true,
                        help = "Connection from which to extract the enriched metadata.",
                        grid = 4,
                    ),
                    "source_qn_prefix" to TextInput(
                        label = "Limit to (prefix)",
                        required = false,
                        help = "Further limit the assets in the source connection by this qualifiedName (not including the connection portion of the qualifiedName).",
                        grid = 4,
                    ),
                    "target_connection" to ConnectionSelector(
                        label = "Target",
                        required = true,
                        help = "Connection into which to load the enriched metadata.",
                        grid = 4,
                        multiSelect = true,
                    ),
                    "config_type" to Radio(
                        label = "Options",
                        required = true,
                        possibleValues = mapOf(
                            "default" to "Default",
                            "advanced" to "Advanced",
                        ),
                        default = "default",
                        help = "Options to optimize how assets are imported.",
                    ),
                    "fail_on_errors" to BooleanInput(
                        label = "Fail on errors",
                        required = false,
                        help = "Whether an invalid value in a field should cause the import to fail (Yes) or log a warning, skip that value, and proceed (No).",
                    ),
                    "field_separator" to TextInput(
                        label = "Field separator",
                        required = false,
                        help = "Character used to separate fields in the input file (for example, ',' or ';').",
                        placeholder = ",",
                        grid = 4,
                    ),
                    "batch_size" to NumericInput(
                        label = "Batch size",
                        required = false,
                        help = "Maximum number of rows to process at a time (per API request).",
                        placeholder = "20",
                        grid = 4,
                    ),
                ),
            ),
            UIStep(
                title = "Attributes",
                description = "Attributes to migrate",
                inputs = mapOf(
                    "limit_type" to Radio(
                        label = "Limit to",
                        required = true,
                        help = "Select whether to limit the attributes to migrate to only those selected, or to exclude only those selected (by default all enriched attributes will be migrated).",
                        possibleValues = mapOf(
                            "INCLUDE" to "Only the following",
                            "EXCLUDE" to "All enrichment except",
                        ),
                        default = "EXCLUDE",
                    ),
                    "attributes_list" to DropDown(
                        label = "Attributes",
                        required = false,
                        possibleValues = mapOf(
                            Asset.DESCRIPTION.atlanFieldName to "System description",
                            Asset.USER_DESCRIPTION.atlanFieldName to "User-provided description",
                            Asset.DISPLAY_NAME.atlanFieldName to "Display name",
                            Asset.OWNER_USERS.atlanFieldName to "Owners (users)",
                            Asset.OWNER_GROUPS.atlanFieldName to "Owners (groups)",
                            Asset.CERTIFICATE_STATUS.atlanFieldName to "Certificate (status)",
                            Asset.CERTIFICATE_STATUS_MESSAGE.atlanFieldName to "Certificate (message)",
                            Asset.ANNOUNCEMENT_TYPE.atlanFieldName to "Announcement (type)",
                            Asset.ANNOUNCEMENT_TITLE.atlanFieldName to "Announcement (title)",
                            Asset.ANNOUNCEMENT_MESSAGE.atlanFieldName to "Announcement (message)",
                            "assignedTerms" to "Assigned terms",
                            "atlanTags" to "Atlan tags",
                            Asset.LINKS.atlanFieldName to "Links",
                            Asset.README.atlanFieldName to "README",
                        ),
                        help = "Attributes to include or exclude (based on option selected above).",
                        multiSelect = true,
                        grid = 8,
                    ),
                    "cm_limit_type" to Radio(
                        label = "Limit custom metadata to",
                        required = true,
                        help = "Select whether to limit the custom metadata attributes to migrate to only those selected, or to exclude only those selected (by default all custom metadata will be migrated).",
                        possibleValues = mapOf(
                            "INCLUDE" to "Only the following",
                            "EXCLUDE" to "All custom metadata except",
                        ),
                        default = "EXCLUDE",
                    ),
                    "custom_metadata" to TextInput(
                        label = "Custom metadata",
                        required = false,
                        help = "Custom metadata to include or exclude (based on option selected above), pipe-separated attributes with each in the form of 'Custom Metadata Set::Attribute Name'.",
                        placeholder = "Data Quality::Completeness|Data Quality::Accuracy",
                        grid = 8,
                    ),
                ),
            ),
        ),
        rules = listOf(
            UIRule(
                whenInputs = mapOf("config_type" to "advanced"),
                required = listOf(
                    "fail_on_errors",
                    "field_separator",
                    "batch_size",
                ),
            ),
        ),
    ),
    containerImage = "ghcr.io/atlanhq/csa-enrichment-migrator:${Atlan.VERSION}",
    classToRun = EnrichmentMigrator::class.java,
    outputs = WorkflowOutputs(
        mapOf(
            "debug-logs" to "/tmp/debug.log",
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
