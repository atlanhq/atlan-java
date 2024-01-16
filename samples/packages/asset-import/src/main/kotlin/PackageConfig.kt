/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */

import com.atlan.Atlan
import com.atlan.pkg.CustomPackage
import com.atlan.pkg.aim.Importer
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.ui.UIStep
import com.atlan.pkg.config.model.workflow.WorkflowOutputs
import com.atlan.pkg.config.widgets.BooleanInput
import com.atlan.pkg.config.widgets.DropDown
import com.atlan.pkg.config.widgets.FileUploader
import com.atlan.pkg.config.widgets.Radio

/**
 * Definition for the Asset Import custom package.
 */
object PackageConfig : CustomPackage(
    "@csa/asset-import",
    "Asset Import",
    "Import assets from a CSV file.",
    "http://assets.atlan.com/assets/ph-cloud-arrow-up-light.svg",
    "https://solutions.atlan.com/asset-import/",
    uiConfig = UIConfig(
        steps = listOf(
            UIStep(
                title = "Assets",
                description = "Assets to import",
                inputs = mapOf(
                    "assets_file" to FileUploader(
                        label = "Assets file",
                        fileTypes = listOf("text/csv"),
                        required = false,
                        help = "Select the file containing assets to import, produced by one of the Asset Export packages.",
                        placeholder = "Select assets CSV file",
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
                    "assets_upsert_semantic" to Radio(
                        label = "Input handling",
                        required = false,
                        possibleValues = mapOf(
                            "upsert" to "Create and update",
                            "update" to "Update only",
                        ),
                        default = "update",
                        help = "Whether to allow the creation of new assets from the input CSV, or ensure assets are only updated if they already exist in Atlan.",
                    ),
                    "assets_fail_on_errors" to BooleanInput(
                        label = "Fail on errors",
                        required = false,
                        help = "Whether an invalid value in a field should cause the import to fail (Yes) or log a warning, skip that value, and proceed (No).",
                    ),
                    "assets_case_sensitive" to BooleanInput(
                        label = "Case-sensitive match for updates",
                        required = false,
                        help = "Whether to use case-sensitive matching when running in update-only mode (Yes) or try case-insensitive matching (No).",
                    ),
                ),
            ),
            UIStep(
                title = "Glossaries",
                description = "Glossaries to import",
                inputs = mapOf(
                    "glossaries_file" to FileUploader(
                        label = "Glossaries file",
                        fileTypes = listOf("text/csv"),
                        required = false,
                        help = "Select the file containing glossaries, categories and terms to import, produced by one of the Asset Export packages.",
                        placeholder = "Select glossaries CSV file",
                    ),
                    "glossaries_attr_to_overwrite" to DropDown(
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
                        help = "Select attributes you want to clear (remove) from glossaries, categories and terms if their value is blank in the provided file.",
                        multiSelect = true,
                        grid = 8,
                    ),
                    "glossaries_upsert_semantic" to Radio(
                        label = "Input handling",
                        required = false,
                        possibleValues = mapOf(
                            "upsert" to "Create and update",
                            "update" to "Update only",
                        ),
                        default = "upsert",
                        help = "Whether to allow the creation of new glossaries, categories and terms from the input CSV, or ensure these are only updated if they already exist in Atlan.",
                    ),
                    "glossaries_fail_on_errors" to BooleanInput(
                        label = "Fail on errors",
                        required = false,
                        help = "Whether an invalid value in a field should cause the import to fail (Yes) or log a warning, skip that value, and proceed (No).",
                    ),
                ),
            ),
        ),
    ),
    containerImage = "ghcr.io/atlanhq/csa-asset-import:${Atlan.VERSION}",
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
