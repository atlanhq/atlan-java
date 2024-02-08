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
import com.atlan.pkg.mdir.Reporter

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
                title = "Outputs",
                description = "Report outputs",
                inputs = mapOf(
                    "include_glossary" to Radio(
                        label = "Generate glossary?",
                        required = true,
                        possibleValues = mapOf(
                            "TRUE" to "Yes",
                            "FALSE" to "No (Excel only)",
                        ),
                        default = "TRUE",
                        help = "Whether to generate a glossary of metadata metrics used in the report. (An Excel will always be generated.)",
                    ),
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
            UIStep(
                title = "Delivery",
                description = "Where to send",
                inputs = mapOf(
                    "email_addresses" to TextInput(
                        label = "Email address(es)",
                        help = "Provide any email addresses you want the report sent to, separated by commas.",
                        required = false,
                        placeholder = "one@example.com,two@example.com",
                    ),
                ),
            ),
        ),
        rules = listOf(
            UIRule(
                whenInputs = mapOf("include_glossary" to "TRUE"),
                required = listOf("glossary_name"),
            ),
        ),
    ),
    containerImage = "ghcr.io/atlanhq/csa-metadata-impact-report:${Atlan.VERSION}",
    classToRun = Reporter::class.java,
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
