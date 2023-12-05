/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.pkg.CustomPipeline
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.ui.UIRule
import com.atlan.pkg.config.model.ui.UIStep
import com.atlan.pkg.config.widgets.APITokenSelector
import com.atlan.pkg.config.widgets.DropDown
import com.atlan.pkg.config.widgets.Radio

object PackageConfig : CustomPipeline(
    "@csa/asset-scorer",
    "Asset Scorer",
    "Calculates a completeness score for an asset based on the context it contains.",
    "http://assets.atlan.com/assets/ph-gauge-light.svg",
    "https://solutions.atlan.com/asset-scorer/",
    uiConfig = UIConfig(
        steps = listOf(
            UIStep(
                title = "Logic",
                description = "Configure pipeline",
                inputs = mapOf(
                    "asset_types" to DropDown(
                        label = "Asset types",
                        required = true,
                        help = "Select asset types to score, or leave blank to score all asset types.",
                        possibleValues = mapOf(
                            "Table" to "Table",
                            "View" to "View",
                            "MaterialisedView" to "Materialized View",
                            "AtlasGlossaryTerm" to "Term",
                        ),
                        multiSelect = true,
                        grid = 8,
                    ),
                ),
            ),
            UIStep(
                title = "Runtime",
                description = "Execution options",
                inputs = mapOf(
                    "credential_usage" to Radio(
                        label = "Run as...",
                        required = true,
                        possibleValues = mapOf(
                            "USER" to "User (me)",
                            "TOKEN" to "API token",
                        ),
                        default = "USER",
                        help = "Select the credentials to use for running the always-on pipeline.",
                    ),
                    "api_token" to APITokenSelector(
                        label = "API token",
                        required = true,
                        help = "Select the API token for the pipeline to run as. If blank, the pipeline will run as the user configuring this workflow.",
                        grid = 4,
                    ),
                ),
            ),
        ),
        rules = listOf(
            UIRule(
                whenInputs = mapOf("credential_usage" to "TOKEN"),
                required = listOf("api_token"),
            ),
        ),
    ),
    containerImage = "ghcr.io/atlanhq/csa-asset-scorer:${Atlan.VERSION}",
    logicCommand = listOf("/dumb-init", "--", "java", "AssetScorer"),
    configCommand = listOf("/dumb-init", "--", "java", "CreateCMWriteConfig"),
    filter = "json(payload).message.operationType in [\"ENTITY_CREATE\", \"ENTITY_UPDATE\", \"BUSINESS_ATTRIBUTE_UPDATE\", \"CLASSIFICATION_ADD\", \"CLASSIFICATION_DELETE\"]",
    keywords = listOf("kotlin", "governance", "pipeline"),
    preview = true,
) {
    @JvmStatic
    fun main(args: Array<String>) {
        generate(this, args)
    }
}
