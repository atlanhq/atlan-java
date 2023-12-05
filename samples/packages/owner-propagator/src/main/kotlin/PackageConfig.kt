/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.pkg.CustomPackage
import com.atlan.pkg.config.model.ui.UIConfig
import com.atlan.pkg.config.model.ui.UIStep
import com.atlan.pkg.config.widgets.TextInput

/**
 * Definition for the OwnerPropagator example custom package.
 */
object PackageConfig : CustomPackage(
    packageId = "@csa/owner-propagator",
    packageName = "Owner Propagator",
    description = "Propagate owners from schema downwards.",
    docsUrl = "https://solutions.atlan.com/",
    iconUrl = "https://assets.atlan.com/assets/ph-user-switch-light.svg",
    containerImage = "ghcr.io/atlanhq/csa-owner-propagator:${Atlan.VERSION}",
    classToRun = OwnerPropagator::class.java,
    uiConfig = UIConfig(
        steps = listOf(
            UIStep(
                title = "Configuration",
                description = "Owner propagation configuration",
                inputs = mapOf(
                    "qn_prefix" to TextInput(
                        label = "Qualified name prefix",
                        help = "Provide the starting name for schemas from which to propagate ownership",
                        required = false,
                        placeholder = "default/snowflake/1234567890",
                        grid = 4,
                    ),
                ),
            ),
        ),
    ),
) {
    @JvmStatic
    fun main(args: Array<String>) {
        generate(this, args)
    }
}
