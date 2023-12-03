/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.pkg.CustomPackage
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.ui.UIStep
import com.atlan.pkg.config.model.workflow.WorkflowOutputs
import com.atlan.pkg.config.widgets.BooleanInput
import com.atlan.pkg.config.widgets.TextInput

/**
 * Definition for the MetadataImpactReport custom package.
 */
object PackageConfig : CustomPackage(
    "@csa/metadata-impact-report",
    "Metadata Impact Report",
    "Produce a detailed report of different areas of potential impact, based on the metadata available.",
    "http://assets.atlan.com/assets/ph-projector-screen-chart-light.svg",
    "https://solutions.atlan.com/metadata-impact-report/",
    uiConfig = UIConfig(
        steps = listOf(
            UIStep(
                title = "Configuration",
                description = "Report configuration",
                inputs = mapOf(
                    "glossary_name" to TextInput(
                        label = "Glossary name",
                        required = true,
                        help = "Name of the glossary in which to store the metadata metrics.",
                        placeholder = "Metadata metrics",
                    ),
                    // TODO: Once data mesh is available we can create data product
                    //  with the necessary search query for the detailed filters, and
                    //  could put them all under an Impact domain (and link the term
                    //  defining their metric to the data product detailing all the assets
                    //  that match the metric)
                    "include_details" to BooleanInput(
                        label = "Include details",
                        required = true,
                        help = "Whether to include detailed results (Yes), or only the headline metrics (No) in the Excel file produced.",
                    ),
                ),
            ),
        ),
    ),
    containerImage = "ghcr.io/atlanhq/csa-metadata-impact-report:${Atlan.VERSION}",
    containerImagePullPolicy = "Always",
    containerCommand = listOf("/dumb-init", "--", "java", "com.atlan.pkg.mdir.MetadataImpactReport"),
    outputs = WorkflowOutputs(
        mapOf(
            "debug-logs" to "/tmp/debug.log",
            "mdir" to "/tmp/mdir.xlsx",
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
