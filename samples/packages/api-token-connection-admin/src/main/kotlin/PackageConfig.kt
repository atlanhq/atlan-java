/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.pkg.CustomPackage
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.ui.UIStep
import com.atlan.pkg.config.model.workflow.WorkflowOutputs
import com.atlan.pkg.config.widgets.APITokenSelector
import com.atlan.pkg.config.widgets.ConnectionSelector

/**
 * Definition for the APITokenConnectionAdmin custom package.
 */
object PackageConfig : CustomPackage(
    "@csa/api-token-connection-admin",
    "API Token Connection Admin",
    "Assigns an API token as a connection admin for an existing connection.",
    "http://assets.atlan.com/assets/ph-key-light.svg",
    "https://solutions.atlan.com/api-token-connection-admin/",
    uiConfig = UIConfig(
        steps = listOf(
            UIStep(
                title = "Configuration",
                description = "Configuration",
                inputs = mapOf(
                    "connection_qualified_name" to ConnectionSelector(
                        label = "Connection",
                        required = true,
                        help = "Select the connection to add the API token to as a connection admin.",
                        placeholder = "default",
                        grid = 4,
                    ),
                    "api_token_guid" to APITokenSelector(
                        label = "API token",
                        required = true,
                        help = "Select the API token to add to the connection as an admin.",
                        grid = 4,
                    ),
                ),
            ),
        ),
    ),
    containerImage = "ghcr.io/atlanhq/csa-api-token-connection-admin:${Atlan.VERSION}",
    classToRun = ApiTokenConnectionAdmin::class.java,
    outputs = WorkflowOutputs(mapOf("debug-logs" to "/tmp/debug.log")),
    keywords = listOf("kotlin", "utility"),
    preview = true,
) {
    @JvmStatic
    fun main(args: Array<String>) {
        generate(this, args)
    }
}
