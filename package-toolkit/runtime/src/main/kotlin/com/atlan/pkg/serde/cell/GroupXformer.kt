/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.AtlanClient
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Asset

/**
 * Static object to transform (really to validate) group references.
 */
object GroupXformer {
    val FIELDS =
        setOf(
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
    fun encode(group: String): String = group

    /**
     * Decodes (deserializes) a string form into a validated group name.
     *
     * @param client connectivity to the Atlan tenant
     * @param groupRef the string form to be decoded
     * @param fieldName the name of the field containing the string-encoded value
     * @return the group name corresponding to the string
     */
    fun decode(
        client: AtlanClient,
        groupRef: String,
        fieldName: String,
    ): String {
        return when (fieldName) {
            in FIELDS -> {
                try {
                    // Try to look up the user reference by username
                    client.groupCache.getIdForName(groupRef)
                    return groupRef
                } catch (e: NotFoundException) {
                    try {
                        // Try again, this time looking up the group by its alias
                        val idFromAlias = client.groupCache.getIdForAlias(groupRef)
                        // And if found by alias, return the group name (since that's what we require)
                        return client.groupCache.getNameForId(idFromAlias)
                    } catch (e: NotFoundException) {
                        throw NoSuchElementException("Group name / alias is not known to Atlan (in $fieldName): $groupRef", e)
                    }
                }
            }
            else -> groupRef
        }
    }
}
