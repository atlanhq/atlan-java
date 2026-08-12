/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
import com.atlan.model.admin.AtlanGroup
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.sgc.ScimGroupCleanup
import kotlin.test.Test

/**
 * Test the SCIM Group Cleanup utility.
 */
class ScimGroupCleanupTest : PackageTest("sgc") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val testGroupName = "test-scim-cleanup-group"

    override fun setup() {
        // Create a test group for diagnostic testing
        val testGroup =
            AtlanGroup
                .creator(testGroupName)
                .build()

        try {
            val groupId = testGroup.create(client)
            logger.info { "Created test group with ID: $groupId" }
        } catch (e: Exception) {
            logger.warn { "Test group may already exist or creation failed: ${e.message}" }
        }

        // Run the package in diagnostic mode
        runCustomPackage(
            ScimGroupCleanupCfg(
                groupName = testGroupName,
                operationMode = "DIAGNOSTIC",
                recreateGroup = false,
            ),
            ScimGroupCleanup::main,
        )
    }

    @Test
    fun testDiagnosticCompleted() {
        // The setup already runs the diagnostic, just verify log is clean
        validateErrorFreeLog()
    }

    override fun teardown() {
        // Clean up test group
        try {
            val groups = AtlanGroup.get(client, testGroupName)
            groups?.forEach { group ->
                AtlanGroup.delete(client, group.id)
                logger.info { "Deleted test group: ${group.id}" }
            }
        } catch (e: Exception) {
            logger.warn { "Failed to clean up test group: ${e.message}" }
        }
    }
}
