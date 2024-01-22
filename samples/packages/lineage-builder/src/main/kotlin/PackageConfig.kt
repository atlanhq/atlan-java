/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */

import com.atlan.Atlan
import com.atlan.pkg.CustomPackage
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.ui.UIRule
import com.atlan.pkg.config.model.ui.UIStep
import com.atlan.pkg.config.model.workflow.WorkflowOutputs
import com.atlan.pkg.config.widgets.BooleanInput
import com.atlan.pkg.config.widgets.FileUploader
import com.atlan.pkg.config.widgets.Radio
import com.atlan.pkg.config.widgets.TextInput
import com.atlan.pkg.lb.Loader

/**
 * Definition for the Lineage Builder custom package.
 */
object PackageConfig : CustomPackage(
    "@csa/lineage-builder",
    "Lineage Builder",
    "Build lineage from a CSV file.",
    "http://assets.atlan.com/assets/ph-tree-structure-light.svg",
    "https://solutions.atlan.com/lineage-builder/",
    uiConfig = UIConfig(
        steps = listOf(
            UIStep(
                title = "Lineage",
                description = "Lineage to import",
                inputs = mapOf(
                    "lineage_import_type" to Radio(
                        label = "Import lineage from",
                        required = true,
                        help = "Select how you want to provide the file containing lineage details to be imported.",
                        possibleValues = mapOf(
                            "UPLOAD" to "Direct upload",
                            "S3" to "S3 object",
                        ),
                        default = "UPLOAD",
                    ),
                    "lineage_file" to FileUploader(
                        label = "Lineage file",
                        fileTypes = listOf("text/csv"),
                        required = true,
                        help = "Select the file containing lineage to import.",
                        placeholder = "Select lineage CSV file",
                    ),
                    "lineage_s3_region" to TextInput(
                        label = "S3 region",
                        required = false,
                        help = "Enter the S3 region from which to retrieve the S3 object. If empty, will use the region of Atlan's own back-end storage.",
                        placeholder = "ap-south-1",
                        grid = 4,
                    ),
                    "lineage_s3_bucket" to TextInput(
                        label = "S3 bucket",
                        required = false,
                        help = "Enter the S3 bucket from which to retrieve the S3 object. If empty, will use the bucket of Atlan's own back-end storage.",
                        placeholder = "bucket-name",
                        grid = 4,
                    ),
                    "lineage_s3_object_key" to TextInput(
                        label = "S3 object key",
                        required = true,
                        help = "Enter the S3 object key, including the name of the object and its prefix (path) in the S3 bucket.",
                        placeholder = "some/where/file.csv",
                        grid = 8,
                    ),
                    "lineage_upsert_semantic" to Radio(
                        label = "Unknown asset handling",
                        required = false,
                        possibleValues = mapOf(
                            "update" to "Skip them",
                            "partial" to "Create partial assets",
                            "upsert" to "Create full assets",
                        ),
                        default = "partial",
                        help = "How to handle assets that do not yet exist in Atlan.",
                    ),
                    "lineage_fail_on_errors" to BooleanInput(
                        label = "Fail on errors",
                        required = false,
                        help = "Whether an invalid value in a field should cause the import to fail (Yes) or log a warning, skip that value, and proceed (No).",
                    ),
                    "lineage_case_sensitive" to BooleanInput(
                        label = "Case-sensitive match for assets",
                        required = false,
                        help = "Whether to use case-sensitive matching for assets (Yes) or try case-insensitive matching (No).",
                    ),
                ),
            ),
        ),
        rules = listOf(
            UIRule(
                whenInputs = mapOf("lineage_import_type" to "UPLOAD"),
                required = listOf("lineage_file"),
            ),
            UIRule(
                whenInputs = mapOf("lineage_import_type" to "S3"),
                required = listOf("lineage_s3_region", "lineage_s3_bucket", "lineage_s3_object_key"),
            ),
        ),
    ),
    containerImage = "ghcr.io/atlanhq/csa-asset-import:${Atlan.VERSION}",
    classToRun = Loader::class.java,
    outputs = WorkflowOutputs(mapOf("debug-logs" to "/tmp/debug.log")),
    keywords = listOf("kotlin", "utility"),
    preview = true,
) {
    @JvmStatic
    fun main(args: Array<String>) {
        generate(this, args)
    }
}
