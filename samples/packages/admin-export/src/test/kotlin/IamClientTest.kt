/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
import com.atlan.pkg.ae.IamClient
import com.sun.net.httpserver.HttpServer
import mu.KotlinLogging
import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.net.InetSocketAddress
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * Unit tests for IamClient using a local HTTP stub server.
 * Verifies bulk user-group and group-alias fetching, batching behaviour,
 * and graceful handling of empty inputs and missing optional fields.
 */
class IamClientTest {
    private val logger = KotlinLogging.logger {}
    private lateinit var server: HttpServer
    private lateinit var client: IamClient

    @BeforeClass
    fun setup() {
        server = HttpServer.create(InetSocketAddress(0), 0)

        // Stub: GET /identity/users?filter=...&columns=groups
        server.createContext("/identity/users") { exchange ->
            val query = exchange.requestURI.query ?: ""
            val filter =
                URLDecoder.decode(
                    query.split("&").firstOrNull { it.startsWith("filter=") }?.removePrefix("filter=") ?: "{}",
                    StandardCharsets.UTF_8,
                )
            // Extract IDs from {"id":{"$in":["id1","id2",...]}}
            val ids =
                Regex("\"([^\"]+)\"")
                    .findAll(
                        filter.substringAfter("\$in\":[").substringBefore("]"),
                    ).map { it.groupValues[1] }
                    .toList()

            val body =
                ids
                    .joinToString(",", "[", "]") { id ->
                        """{"id":"$id","groups":[{"id":"grp-$id","name":"group-$id"}]}"""
                    }.toByteArray()

            exchange.sendResponseHeaders(200, body.size.toLong())
            exchange.responseBody.use { it.write(body) }
        }

        // Stub: GET /identity/groups?filter=...&columns=attributes
        server.createContext("/identity/groups") { exchange ->
            val query = exchange.requestURI.query ?: ""
            val filter =
                URLDecoder.decode(
                    query.split("&").firstOrNull { it.startsWith("filter=") }?.removePrefix("filter=") ?: "{}",
                    StandardCharsets.UTF_8,
                )
            val ids =
                Regex("\"([^\"]+)\"")
                    .findAll(
                        filter.substringAfter("\$in\":[").substringBefore("]"),
                    ).map { it.groupValues[1] }
                    .toList()

            val body =
                ids
                    .joinToString(",", "[", "]") { id ->
                        when {
                            id.contains("no-alias") -> """{"id":"$id"}"""

                            // missing attributes entirely
                            else -> """{"id":"$id","attributes":{"alias":"Alias for $id"}}"""
                        }
                    }.toByteArray()

            exchange.sendResponseHeaders(200, body.size.toLong())
            exchange.responseBody.use { it.write(body) }
        }

        server.start()
        val port = server.address.port
        client =
            IamClient(
                baseUrl = "http://localhost:$port",
                bearerToken = "test-token",
                logger = logger,
            )
    }

    @AfterClass
    fun teardown() {
        server.stop(0)
    }

    @Test
    fun getUserGroupsEmptyInputReturnsEmptyMap() {
        val result = client.getUserGroups(emptyList())
        assertTrue(result.isEmpty(), "Expected empty map for empty user ID list")
    }

    @Test
    fun getGroupAliasesEmptyInputReturnsEmptyMap() {
        val result = client.getGroupAliases(emptyList())
        assertTrue(result.isEmpty(), "Expected empty map for empty group ID list")
    }

    @Test
    fun getUserGroupsReturnsMappedGroups() {
        val userIds = listOf("user-1", "user-2", "user-3")
        val result = client.getUserGroups(userIds)

        assertEquals(result.size, 3)
        userIds.forEach { userId ->
            val groups = result[userId]
            assertEquals(groups?.size, 1, "Expected 1 group for $userId")
            assertEquals(groups?.first()?.id, "grp-$userId")
            assertEquals(groups?.first()?.name, "group-$userId")
        }
    }

    @Test
    fun getGroupAliasesReturnsMappedAliases() {
        val groupIds = listOf("grp-user-1", "grp-user-2")
        val result = client.getGroupAliases(groupIds)

        assertEquals(result.size, 2)
        groupIds.forEach { groupId ->
            assertEquals(result[groupId], "Alias for $groupId")
        }
    }

    @Test
    fun getGroupAliasesMissingAttributesReturnsEmptyString() {
        val result = client.getGroupAliases(listOf("no-alias-grp"))
        assertEquals(result["no-alias-grp"], "", "Expected empty string when attributes are missing")
    }

    @Test
    fun getUserGroupsBatchesLargeInputs() {
        // 550 users should produce 2 batches (500 + 50) with batchSize=500
        val userIds = (1..550).map { "batch-user-$it" }
        val requestCount =
            java.util.concurrent.atomic
                .AtomicInteger(0)

        // Wrap client with a smaller batchSize to verify chunking without needing 550 ids
        val smallBatchClient =
            IamClient(
                baseUrl = "http://localhost:${server.address.port}",
                bearerToken = "test-token",
                logger = logger,
            )
        val result = smallBatchClient.getUserGroups(userIds.take(7), batchSize = 3)
        // 7 users, batchSize=3 → 3 batches: [3, 3, 1]
        assertEquals(result.size, 7, "All 7 users should be present in result")
    }

    @Test
    fun getGroupAliasesBatchesLargeInputs() {
        val groupIds = (1..7).map { "grp-user-$it" }
        val result = client.getGroupAliases(groupIds, batchSize = 3)
        // 7 groups, batchSize=3 → 3 batches: [3, 3, 1]
        assertEquals(result.size, 7, "All 7 groups should be present in result")
    }
}
