/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.AtlanClient
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Asset
import com.atlan.pkg.PackageContext

/**
 * Static object to transform (really to validate) user references.
 */
object UserXformer {
    val FIELDS =
        setOf(
            Asset.OWNER_USERS.atlanFieldName,
            Asset.ADMIN_USERS.atlanFieldName,
            Asset.VIEWER_USERS.atlanFieldName,
            Asset.SOURCE_READ_RECENT_USERS.atlanFieldName,
            Asset.SOURCE_READ_TOP_USERS.atlanFieldName,
        )

    /**
     * Encodes (serializes) a user into a string form.
     *
     * @param user to be encoded
     * @return the string-encoded form for that user
     */
    fun encode(user: String): String = user

    /**
     * Decodes (deserializes) a string form into a validated username.
     *
     * @param ctx context of the running package
     * @param userRef the string form to be decoded
     * @param fieldName the name of the field containing the string-encoded value
     * @return the username corresponding to the string
     */
    fun decode(
        ctx: PackageContext<*>,
        userRef: String,
        fieldName: String,
    ): String {
        return when (fieldName) {
            in FIELDS -> {
                try {
                    // Try to look up the user reference by username
                    ctx.client.userCache.getIdForName(userRef, ctx.startTS)
                    return userRef
                } catch (e: NotFoundException) {
                    try {
                        // Try again, this time looking up the user by email
                        val idFromEmail = ctx.client.userCache.getIdForEmail(userRef, ctx.startTS)
                        // And if found by email, return the username (since that's what we require)
                        return ctx.client.userCache.getNameForId(idFromEmail, ctx.startTS)
                    } catch (e: NotFoundException) {
                        throw NoSuchElementException("Username / email address is not known to Atlan (in $fieldName): $userRef", e)
                    }
                }
            }
            else -> userRef
        }
    }
}
