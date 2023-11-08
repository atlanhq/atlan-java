/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.exception.AtlanException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Glossary
import mu.KotlinLogging

object GlossaryCache : AssetCache() {
    private val logger = KotlinLogging.logger {}

    /** {@inheritDoc}  */
    override fun lookupAssetByIdentity(identity: String?): Asset? {
        try {
            return Glossary.findByName(identity)
        } catch (e: AtlanException) {
            logger.error("Unable to lookup or find glossary: {}", identity, e)
        }
        return null
    }

    /** {@inheritDoc}  */
    override fun lookupAssetByGuid(guid: String?): Asset? {
        try {
            val glossary =
                Glossary.select()
                    .where(Glossary.GUID.eq(guid))
                    .includeOnResults(Glossary.NAME)
                    .pageSize(2)
                    .stream()
                    .findFirst()
            if (glossary.isPresent) {
                return glossary.get()
            }
        } catch (e: AtlanException) {
            logger.error("Unable to lookup or find glossary: {}", guid, e)
        }
        return null
    }

    /** {@inheritDoc}  */
    override fun getIdentityForAsset(asset: Asset): String {
        return asset.name
    }
}
