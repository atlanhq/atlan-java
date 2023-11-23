/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.Atlan
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Asset

/**
 * Static object to transform (really to validate) role references.
 */
object RoleXformer {

    val FIELDS = setOf(
        Asset.ADMIN_ROLES.atlanFieldName,
    )

    /**
     * Encodes (serializes) a role into a string form.
     *
     * @param role to be encoded
     * @return the string-encoded form for that role
     */
    fun encode(role: String): String {
        return role
    }

    /**
     * Decodes (deserializes) a string form into a validated role GUID.
     *
     * @param roleRef the string form to be decoded
     * @param fieldName the name of the field containing the string-encoded value
     * @return the role GUID corresponding to the string
     */
    fun decode(
        roleRef: String,
        fieldName: String,
    ): String {
        return when (fieldName) {
            in FIELDS -> {
                try {
                    // Try to look up the user reference by username
                    Atlan.getDefaultClient().roleCache.getIdForName(roleRef)
                    return roleRef
                } catch (e: NotFoundException) {
                    throw NoSuchElementException("Role name $roleRef is not known to Atlan.", e)
                }
            }
            else -> roleRef
        }
    }
}
