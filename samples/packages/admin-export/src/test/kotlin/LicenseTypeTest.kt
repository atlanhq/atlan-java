/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.admin.AtlanUser
import com.atlan.pkg.ae.exports.Users
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

/**
 * Unit tests for license-type (workspace role) resolution in the Users export.
 *
 * Regression coverage for CSA-449: the export must never emit a raw "$"-prefixed system
 * identifier (e.g. "$guest") in the "License type" column, regardless of which fields the
 * IAM/Heracles response populates.
 */
class LicenseTypeTest {
    private fun user(
        roleName: String? = null,
        roleDescription: String? = null,
        workspaceRole: String? = null,
    ): AtlanUser {
        val builder = AtlanUser.builder()
        if (roleName != null || roleDescription != null) {
            builder.assignedRole(
                AtlanUser.SubRole
                    .builder()
                    .name(roleName)
                    .description(roleDescription)
                    .build(),
            )
        }
        if (workspaceRole != null) builder.workspaceRole(workspaceRole)
        return builder.build()
    }

    @Test
    fun prefersDescriptionWhenPresent() {
        // Most users: backend returns the display name directly.
        assertEquals(Users.licenseTypeOf(user(roleName = "\$guest", roleDescription = "Guest")), "Guest")
        assertEquals(Users.licenseTypeOf(user(roleName = "\$admin", roleDescription = "Admin")), "Admin")
    }

    @Test
    fun humanizesNameWhenDescriptionNull() {
        // IAG row 14 path: assignedRole present, description missing.
        assertEquals(Users.licenseTypeOf(user(roleName = "\$guest")), "Guest")
        assertEquals(Users.licenseTypeOf(user(roleName = "\$admin")), "Admin")
        assertEquals(Users.licenseTypeOf(user(roleName = "\$member")), "Member")
    }

    @Test
    fun humanizesNameWhenDescriptionBlank() {
        assertEquals(Users.licenseTypeOf(user(roleName = "\$guest", roleDescription = "")), "Guest")
    }

    @Test
    fun fallsBackToWorkspaceRoleWhenNoAssignedRole() {
        // fs3 reproducer / IAG row 19: assignedRole null, only workspaceRole available.
        assertEquals(Users.licenseTypeOf(user(workspaceRole = "\$guest")), "Guest")
    }

    @Test
    fun neverEmitsDollarPrefix() {
        // The core invariant: no resolution path leaks a "$"-prefixed identifier.
        listOf(
            user(roleName = "\$guest"),
            user(workspaceRole = "\$guest"),
            user(roleName = "\$guest", roleDescription = ""),
        ).forEach { u ->
            val result = Users.licenseTypeOf(u)
            assert(!result.startsWith("$")) { "License type leaked raw identifier: $result" }
        }
    }

    @Test
    fun preservesCustomRoleDisplayNames() {
        // Custom roles flow through the description branch unchanged (no "$" prefix to strip).
        assertEquals(Users.licenseTypeOf(user(roleName = "wf-admin", roleDescription = "Workflow Admin")), "Workflow Admin")
    }

    @Test
    fun emptyWhenNothingAvailable() {
        assertEquals(Users.licenseTypeOf(user()), "")
    }
}
