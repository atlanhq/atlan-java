/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.model.admin.AtlanGroup
import com.atlan.pkg.PackageTest
import mu.KotlinLogging
import org.testng.Assert.assertNotNull
import kotlin.test.Test
import kotlin.test.assertNotEquals

/**
 * Test the SCIM Group Cleanup utility.
 */
class ScimGroupCleanupTest : PackageTest() {
    private val logger = KotlinLogging.logger {}

    override val config =
        """
        {
            "group_name": "test-scim-cleanup-group",
            "operation_mode": "DIAGNOSTIC",
            "recreate_group": false
        }
        """.trimIndent()

    override fun setup() {
        // Create a test group for diagnostic testing
        val testGroup =
            AtlanGroup
                .creator("test-scim-cleanup-group")
                .build()

        try {
            val groupId = testGroup.create(Atlan.getDefaultClient())
            logger.info { "Created test group with ID: $groupId" }
        } catch (e: Exception) {
            logger.warn { "Test group may already exist or creation failed: ${e.message}" }
        }
    }

    @Test
    fun testDiagnosticMode() {
        // Run the package in diagnostic mode
        runCustomPackage(
            config,
            ScimGroupCleanup::main.javaClass.enclosingMethod,
        )

        // Verify test group still exists after diagnostic
        val groups = AtlanGroup.get(Atlan.getDefaultClient(), "test-scim-cleanup-group")
        assertNotNull(groups)
        assertNotEquals(0, groups.size)
    }

    override fun teardown() {
        // Clean up test group
        try {
            val groups = AtlanGroup.get(Atlan.getDefaultClient(), "test-scim-cleanup-group")
            groups?.forEach { group ->
                AtlanGroup.delete(Atlan.getDefaultClient(), group.id)
                logger.info { "Deleted test group: ${group.id}" }
            }
        } catch (e: Exception) {
            logger.warn { "Failed to clean up test group: ${e.message}" }
        }
    }
}
