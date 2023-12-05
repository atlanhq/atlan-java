/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.pkg.CustomPackage
import com.atlan.pkg.ae.AdminExporter
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.ui.UIStep
import com.atlan.pkg.config.model.workflow.WorkflowOutputs
import com.atlan.pkg.config.widgets.BooleanInput
import com.atlan.pkg.config.widgets.DropDown

/**
 * Definition for the Admin Export custom package.
 */
object PackageConfig : CustomPackage(
    "@csa/admin-export",
    "Admin Export",
    "Exports key administrative details from a tenant.",
    "https://assets.atlan.com/assets/ph-user-circle-gear-light.svg",
    "https://solutions.atlan.com/admin-export/",
    uiConfig = UIConfig(
        listOf(
            UIStep(
                title = "Scope",
                description = "What to include",
                inputs = mapOf(
                    "objects_to_include" to DropDown(
                        label = "Objects to include",
                        possibleValues = mapOf(
                            "users" to "Users",
                            "groups" to "Groups",
                            "personas" to "Personas",
                            "purposes" to "Purposes",
                            "policies" to "Policies",
                        ),
                        help = "Select the objects you want to include in the exported Excel file.",
                        multiSelect = true,
                        required = true,
                    ),
                    "include_native_policies" to BooleanInput(
                        label = "Include out-of-the-box policies?",
                        help = "Whether to include the out-of-the-box policies (Yes) or only those policies you can manage directly (No).",
                        required = false,
                    ),
                ),
            ),
        ),
    ),
    containerImage = "ghcr.io/atlanhq/csa-admin-export:${Atlan.VERSION}",
    classToRun = AdminExporter::class.java,
    outputs = WorkflowOutputs(
        mapOf(
            "debug-logs" to "/tmp/debug.log",
            "admin-export" to "/tmp/admin-export.xlsx",
        ),
    ),
    keywords = listOf("kotlin", "utility", "admin", "export"),
    preview = true,
) {
    @JvmStatic
    fun main(args: Array<String>) {
        generate(this, args)
    }
}
