/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */

import com.atlan.Atlan
import com.atlan.pkg.CustomPackage
import com.atlan.pkg.cab.Importer
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.ui.UIRule
import com.atlan.pkg.config.model.ui.UIStep
import com.atlan.pkg.config.model.workflow.WorkflowOutputs
import com.atlan.pkg.config.widgets.BooleanInput
import com.atlan.pkg.config.widgets.DropDown
import com.atlan.pkg.config.widgets.FileUploader
import com.atlan.pkg.config.widgets.NumericInput
import com.atlan.pkg.config.widgets.Radio
import com.atlan.pkg.config.widgets.TextInput

/**
 * Definition for the Cube Assets Builder custom package.
 */
object PackageConfig : CustomPackage(
    "@csa/cube-assets-builder",
    "Cube Assets Builder",
    "Build (and update) cube assets managed through a CSV file.",
    "http://assets.atlan.com/assets/ph-cube-light.svg",
    "https://solutions.atlan.com/cube-assets-builder/",
    uiConfig = UIConfig(
        steps = listOf(
            UIStep(
                title = "Assets",
                description = "Assets to import",
                inputs = mapOf(
                    "assets_import_type" to Radio(
                        label = "Import assets from",
                        required = true,
                        help = "Select how you want to provide the file containing cube assets to be imported.",
                        possibleValues = mapOf(
                            "UPLOAD" to "Direct upload",
                            "S3" to "S3 object",
                        ),
                        default = "UPLOAD",
                    ),
                    "assets_file" to FileUploader(
                        label = "Assets file",
                        fileTypes = listOf("text/csv"),
                        required = false,
                        help = "Select the file containing the cube assets to import.",
                        placeholder = "Select assets CSV file",
                    ),
                    "assets_s3_region" to TextInput(
                        label = "S3 region",
                        required = false,
                        help = "Enter the S3 region from which to retrieve the S3 object. If empty, will use the region of Atlan's own back-end storage.",
                        placeholder = "ap-south-1",
                        grid = 4,
                    ),
                    "assets_s3_bucket" to TextInput(
                        label = "S3 bucket",
                        required = false,
                        help = "Enter the S3 bucket from which to retrieve the S3 object. If empty, will use the bucket of Atlan's own back-end storage.",
                        placeholder = "bucket-name",
                        grid = 4,
                    ),
                    "assets_s3_object_key" to TextInput(
                        label = "S3 object key",
                        required = false,
                        help = "Enter the S3 object key, including the name of the object and its prefix (path) in the S3 bucket.",
                        placeholder = "some/where/file.csv",
                        grid = 8,
                    ),
                    "assets_upsert_semantic" to Radio(
                        label = "Input handling",
                        required = false,
                        possibleValues = mapOf(
                            "upsert" to "Create and update",
                            "update" to "Update only",
                        ),
                        default = "upsert",
                        help = "Whether to allow the creation of new assets from the input CSV, or ensure assets are only updated if they already exist in Atlan.",
                    ),
                    "assets_config_type" to Radio(
                        label = "Options",
                        required = true,
                        possibleValues = mapOf(
                            "default" to "Default",
                            "advanced" to "Advanced",
                        ),
                        default = "default",
                        help = "Options to optimize how assets are imported.",
                    ),
                    "assets_attr_to_overwrite" to DropDown(
                        label = "Remove attributes, if empty",
                        required = false,
                        possibleValues = mapOf(
                            "certificateStatus" to "Certificate",
                            "announcementType" to "Announcement",
                            "displayName" to "Display name",
                            "description" to "Description (system)",
                            "userDescription" to "Description (user)",
                            "ownerUsers" to "Owners (users)",
                            "ownerGroups" to "Owners (groups)",
                            "assignedTerms" to "Assigned terms",
                            "readme" to "README",
                        ),
                        help = "Select attributes you want to clear (remove) from assets if their value is blank in the provided file.",
                        multiSelect = true,
                        grid = 8,
                    ),
                    "assets_fail_on_errors" to BooleanInput(
                        label = "Fail on errors",
                        required = false,
                        help = "Whether an invalid value in a field should cause the import to fail (Yes) or log a warning, skip that value, and proceed (No).",
                    ),
                    "assets_field_separator" to TextInput(
                        label = "Field separator",
                        required = false,
                        help = "Character used to separate fields in the input file (for example, ',' or ';').",
                        placeholder = ",",
                        grid = 4,
                    ),
                    "assets_batch_size" to NumericInput(
                        label = "Batch size",
                        required = false,
                        help = "Maximum number of rows to process at a time (per API request).",
                        placeholder = "20",
                        grid = 4,
                    ),
                    "track_batches" to BooleanInput(
                        label = "Track asset details",
                        required = false,
                        hidden = true,
                        default = true,
                        help = "Whether to track details about every asset across batches (Yes) or only counts (No).",
                    ),
                ),
            ),
            UIStep(
                title = "Semantics",
                description = "Processing logic",
                inputs = mapOf(
                    "delta_semantic" to Radio(
                        label = "Delta handling",
                        required = false,
                        possibleValues = mapOf(
                            "full" to "Full replacement",
                            "delta" to "Incremental",
                        ),
                        default = "full",
                        help = "Whether to treat the input file as an initial load, full replacement (deleting any existing assets not in the file) or only incremental (no deletion of existing assets).",
                    ),
                    "delta_removal_type" to Radio(
                        label = "Removal type",
                        required = false,
                        possibleValues = mapOf(
                            "archive" to "Archive (recoverable)",
                            "purge" to "Purge (cannot be recovered)",
                        ),
                        default = "archive",
                        help = "How to delete any assets not found in the latest file.",
                    ),
                    "previous_file_direct" to TextInput(
                        label = "Previous file",
                        required = false,
                        hidden = true,
                        help = "Path to a direct file (locally) to use for delta processing.",
                    ),
                    "skip_s3" to BooleanInput(
                        label = "Skip S3",
                        required = false,
                        hidden = true,
                        default = false,
                        help = "Whether to skip S3 operations (uploading and downloading files) as part of delta processing.",
                    ),
                ),
            ),
        ),
        rules = listOf(
            UIRule(
                whenInputs = mapOf("assets_import_type" to "UPLOAD"),
                required = listOf("assets_file"),
            ),
            UIRule(
                whenInputs = mapOf("assets_import_type" to "S3"),
                required = listOf("assets_s3_region", "assets_s3_bucket", "assets_s3_object_key"),
            ),
            UIRule(
                whenInputs = mapOf("assets_config_type" to "advanced"),
                required = listOf(
                    "assets_attr_to_overwrite",
                    "assets_fail_on_errors",
                    "assets_field_separator",
                    "assets_batch_size",
                ),
            ),
            UIRule(
                whenInputs = mapOf("delta_semantic" to "full"),
                required = listOf("delta_removal_type"),
            ),
        ),
    ),
    containerImage = "ghcr.io/atlanhq/csa-cube-assets-builder:${Atlan.VERSION}",
    classToRun = Importer::class.java,
    outputs = WorkflowOutputs(mapOf("debug-logs" to "/tmp/debug.log")),
    keywords = listOf("kotlin", "utility"),
    preview = true,
) {
    @JvmStatic
    fun main(args: Array<String>) {
        generate(this, args)
    }
}
