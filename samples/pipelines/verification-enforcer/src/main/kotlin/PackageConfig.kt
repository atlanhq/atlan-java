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
import com.atlan.pkg.config.widgets.TextInput

object PackageConfig : CustomPipeline(
    "@csa/verification-enforcer",
    "Verification Enforcer",
    "Ensures any asset marked VERIFIED meets a minimum standard, or if not it is automatically reverted to DRAFT.",
    "https://assets.atlan.com/assets/ph-seal-check-light.svg",
    "https://solutions.atlan.com/verification-enforcer/",
    uiConfig = UIConfig(
        steps = listOf(
            UIStep(
                title = "Logic",
                description = "Configure pipeline",
                inputs = mapOf(
                    "asset_types" to DropDown(
                        label = "Asset types",
                        required = true,
                        help = "Select all the asset types to enforce verification against.",
                        possibleValues = mapOf(
                            "Table" to "Table",
                            "View" to "View",
                            "MaterialisedView" to "Materialized View",
                            "AtlasGlossaryTerm" to "Term",
                            "AtlasGlossaryCategory" to "Category",
                            "AtlasGlossary" to "Glossary",
                        ),
                        multiSelect = true,
                        grid = 8,
                    ),
                    "must_haves" to DropDown(
                        label = "Must have",
                        required = true,
                        help = "Select all the characteristics an asset must have to be verified.",
                        possibleValues = mapOf(
                            "description" to "Description",
                            "lineage" to "Lineage",
                            "owner" to "Owner",
                            "readme" to "README",
                            "tag" to "Tag",
                            "term" to "Term",
                        ),
                        multiSelect = true,
                        grid = 8,
                    ),
                    "enforcement_message" to TextInput(
                        label = "Enforcement message",
                        required = false,
                        help = "Message to include when an asset is reverted to DRAFT by the pipeline.",
                        placeholder = VerificationEnforcer.DEFAULT_ENFORCEMENT_MESSAGE,
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
    containerImage = "ghcr.io/atlanhq/csa-verification-enforcer:${Atlan.VERSION}",
    logicClass = VerificationEnforcer::class.java,
    filter = "json(payload).message.operationType in [\"ENTITY_CREATE\", \"ENTITY_UPDATE\"] && json(payload).message.entity.attributes.certificateStatus in [\"VERIFIED\"]",
    keywords = listOf("kotlin", "governance", "pipeline"),
    preview = true,
) {
    @JvmStatic
    fun main(args: Array<String>) {
        generate(this, args)
    }
}
