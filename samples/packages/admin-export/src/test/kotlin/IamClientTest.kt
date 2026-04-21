/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
import com.atlan.api.IdentityEndpoint
import com.sun.net.httpserver.HttpServer
import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.net.InetSocketAddress
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * Unit tests for IdentityEndpoint using a local HTTP stub server.
 * Verifies bulk user-group and group-alias fetching, batching behaviour,
 * and graceful handling of empty inputs and missing optional fields.
 */
class IamClientTest {
    private lateinit var server: HttpServer
    private lateinit var endpoint: IdentityEndpoint

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
            val ids =
                Regex("\"([^\"]+)\"")
                    .findAll(filter.substringAfter("\$in\":[").substringBefore("]"))
                    .map { it.groupValues[1] }
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
                    .findAll(filter.substringAfter("\$in\":[").substringBefore("]"))
                    .map { it.groupValues[1] }
                    .toList()
            val body =
                ids
                    .joinToString(",", "[", "]") { id ->
                        if (id.contains("no-alias")) {
                            """{"id":"$id"}"""
                        } else {
                            """{"id":"$id","attributes":{"alias":"Alias for $id"}}"""
                        }
                    }.toByteArray()
            exchange.sendResponseHeaders(200, body.size.toLong())
            exchange.responseBody.use { it.write(body) }
        }

        server.start()
        val port = server.address.port
        endpoint = IdentityEndpoint.forTesting("http://localhost:$port", "test-token")
    }

    @AfterClass
    fun teardown() {
        server.stop(0)
    }

    @Test
    fun getUserGroupsEmptyInputReturnsEmptyMap() {
        val result = endpoint.getUserGroups(emptyList())
        assertTrue(result.isEmpty(), "Expected empty map for empty user ID list")
    }

    @Test
    fun getGroupAliasesEmptyInputReturnsEmptyMap() {
        val result = endpoint.getGroupAliases(emptyList())
        assertTrue(result.isEmpty(), "Expected empty map for empty group ID list")
    }

    @Test
    fun getUserGroupsReturnsMappedGroups() {
        val userIds = listOf("user-1", "user-2", "user-3")
        val result = endpoint.getUserGroups(userIds)
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
        val result = endpoint.getGroupAliases(groupIds)
        assertEquals(result.size, 2)
        groupIds.forEach { groupId ->
            assertEquals(result[groupId], "Alias for $groupId")
        }
    }

    @Test
    fun getGroupAliasesMissingAttributesReturnsEmptyString() {
        val result = endpoint.getGroupAliases(listOf("no-alias-grp"))
        assertEquals(result["no-alias-grp"], "", "Expected empty string when attributes are missing")
    }

    @Test
    fun getUserGroupsBatchesLargeInputs() {
        // 7 users with batchSize=3 → 3 batches: [3, 3, 1]
        val userIds = (1..7).map { "user-$it" }
        val result = endpoint.getUserGroups(userIds, 3)
        assertEquals(result.size, 7, "All 7 users should be present in result")
    }

    @Test
    fun getGroupAliasesBatchesLargeInputs() {
        // 7 groups with batchSize=3 → 3 batches: [3, 3, 1]
        val groupIds = (1..7).map { "grp-user-$it" }
        val result = endpoint.getGroupAliases(groupIds, 3)
        assertEquals(result.size, 7, "All 7 groups should be present in result")
    }
}
