/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.Atlan
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Asset

/**
 * Static object to transform (really to validate) group references.
 */
object GroupXformer {

    val FIELDS = setOf(
        Asset.OWNER_GROUPS.atlanFieldName,
        Asset.ADMIN_GROUPS.atlanFieldName,
        Asset.VIEWER_GROUPS.atlanFieldName,
    )

    /**
     * Encodes (serializes) a group into a string form.
     *
     * @param group to be encoded
     * @return the string-encoded form for that group
     */
    fun encode(group: String): String {
        return group
    }

    /**
     * Decodes (deserializes) a string form into a validated group name.
     *
     * @param groupRef the string form to be decoded
     * @param fieldName the name of the field containing the string-encoded value
     * @return the group name corresponding to the string
     */
    fun decode(
        groupRef: String,
        fieldName: String,
    ): String {
        return when (fieldName) {
            in FIELDS -> {
                try {
                    // Try to look up the user reference by username
                    Atlan.getDefaultClient().groupCache.getIdForName(groupRef)
                    return groupRef
                } catch (e: NotFoundException) {
                    try {
                        // Try again, this time looking up the group by its alias
                        val idFromAlias = Atlan.getDefaultClient().groupCache.getIdForAlias(groupRef)
                        // And if found by alias, return the group name (since that's what we require)
                        return Atlan.getDefaultClient().groupCache.getNameForId(idFromAlias)
                    } catch (e: NotFoundException) {
                        throw NoSuchElementException("Group name / alias $groupRef is not known to Atlan.", e)
                    }
                }
            }
            else -> groupRef
        }
    }
}
