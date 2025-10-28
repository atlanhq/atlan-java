/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Asset
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import kotlin.concurrent.thread

/**
 * Static object to transform (really to validate) role references.
 */
object RoleXformer {
    val logger = Utils.getLogger(this.javaClass.name)
    val FIELDS =
        setOf(
            Asset.ADMIN_ROLES.atlanFieldName,
        )

    /**
     * Encodes (serializes) a role into a string form.
     *
     * @param role to be encoded
     * @return the string-encoded form for that role
     */
    fun encode(role: String): String = role

    /**
     * Decodes (deserializes) a string form into a validated role GUID.
     *
     * @param ctx connectivity to the Atlan tenant
     * @param roleRef the string form to be decoded
     * @param fieldName the name of the field containing the string-encoded value
     * @return the role GUID corresponding to the string
     */
    fun decode(
        ctx: PackageContext<*>,
        roleRef: String,
        fieldName: String,
    ): String {
        return when (fieldName) {
            in FIELDS -> {
                return try {
                    // Try to look up the role reference by role ID
                    ctx.client.roleCache.getIdForSid(roleRef)
                } catch (e: NotFoundException) {
                    logger.debug(e) { "Unable to find role by secondary ID, will retry as name: $roleRef" }
                    try {
                        // Try again, this time looking up the role by name
                        ctx.client.roleCache.getIdForName(roleRef)
                    } catch (e: NotFoundException) {
                        throw NoSuchElementException("Role name is not known to Atlan (in $fieldName): $roleRef", e)
                    }
                }
            }
            else -> roleRef
        }
    }
}
