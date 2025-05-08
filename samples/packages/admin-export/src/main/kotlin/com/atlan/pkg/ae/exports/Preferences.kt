/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.ae.exports

import AdminExportCfg
import com.atlan.exception.AtlanException
import com.atlan.exception.NotFoundException
import com.atlan.pkg.PackageContext
import mu.KLogger

object Preferences {
    /**
     * Retrieve the set of names of custom metadata that should be denied.
     *
     * @param ctx package configuration
     * @param type the object type through which preferences are configured
     * @param cmGuids unique identifiers (GUIDs) of the custom metadata sets to deny
     * @param logger through which to log
     */
    fun getCustomMetadataToDeny(
        ctx: PackageContext<AdminExportCfg>,
        type: String,
        cmGuids: Set<String>,
        logger: KLogger,
    ): Set<String> {
        val denyCustomMetadata = mutableSetOf<String>()
        cmGuids.forEach { cmGuid ->
            try {
                val cmName = ctx.client.customMetadataCache.getNameForId(cmGuid) ?: ""
                if (cmName.isNotBlank()) {
                    denyCustomMetadata.add(cmName)
                }
            } catch (e: NotFoundException) {
                logger.warn { "Custom metadata associated with $type no longer exists -- skipping: $cmGuid" }
                logger.debug(e) { "Details:" }
            } catch (e: AtlanException) {
                logger.error(e) { "Custom metadata associated with $type could not be looked up -- skipping." }
            }
        }
        return denyCustomMetadata.toSet()
    }
}
