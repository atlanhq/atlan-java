/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.ae

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KLogger
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets

/**
 * Client for the Redis-backed IAM identity API (/api/service/identity/...).
 * Bulk-fetches user group memberships and group aliases, replacing the
 * previous per-user Keycloak calls that fail at high user volumes (60K+).
 *
 * Auth uses the escalated service account Bearer token obtained via
 * [com.atlan.api.ImpersonationEndpoint.escalate], which uses CLIENT_ID +
 * CLIENT_SECRET from argo-client-creds via client_credentials grant.
 *
 * @param baseUrl effective base URL for identity endpoints
 * @param bearerToken escalated service account Bearer token (from client.impersonate.escalate())
 * @param logger logger to use for progress messages
 */
class IamClient(
    private val baseUrl: String,
    private val bearerToken: String,
    private val logger: KLogger,
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class GroupRef(
        val id: String = "",
        val name: String = "",
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    private data class IamUserResponse(
        val id: String = "",
        val groups: List<GroupRef> = emptyList(),
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    private data class IamGroupAttributes(
        val alias: String? = null,
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    private data class IamGroupResponse(
        val id: String = "",
        val attributes: IamGroupAttributes? = null,
    )

    private val http = HttpClient.newHttpClient()
    private val mapper = jacksonObjectMapper()

    private val authHeader = "Bearer $bearerToken"

    /**
     * Bulk-fetch group memberships for the given user IDs.
     * Returns a map of userId to list of GroupRef (id + Keycloak group name).
     * Issues one GET /identity/users request per batch of [batchSize] IDs.
     */
    fun getUserGroups(
        userIds: List<String>,
        batchSize: Int = 500,
    ): Map<String, List<GroupRef>> {
        if (userIds.isEmpty()) return emptyMap()
        logger.info { "Bulk-fetching group memberships for ${userIds.size} users in batches of $batchSize..." }
        val result = mutableMapOf<String, List<GroupRef>>()
        userIds.chunked(batchSize).forEachIndexed { i, batch ->
            logger.debug { "  user-groups batch ${i + 1} (${batch.size} users)" }
            val url = "$baseUrl/identity/users?${encode("filter", buildInFilter(batch))}&columns=groups"
            get<List<IamUserResponse>>(url).forEach { user -> result[user.id] = user.groups }
        }
        logger.info { "Group memberships fetched for ${result.size} users." }
        return result
    }

    /**
     * Bulk-fetch group aliases for the given group IDs.
     * Returns a map of groupId to alias (display name).
     * Issues one GET /identity/groups request per batch of [batchSize] IDs.
     */
    fun getGroupAliases(
        groupIds: List<String>,
        batchSize: Int = 500,
    ): Map<String, String> {
        if (groupIds.isEmpty()) return emptyMap()
        logger.info { "Bulk-fetching aliases for ${groupIds.size} groups in batches of $batchSize..." }
        val result = mutableMapOf<String, String>()
        groupIds.chunked(batchSize).forEachIndexed { i, batch ->
            logger.debug { "  group-aliases batch ${i + 1} (${batch.size} groups)" }
            val url = "$baseUrl/identity/groups?${encode("filter", buildInFilter(batch))}&columns=attributes"
            get<List<IamGroupResponse>>(url).forEach { group ->
                result[group.id] = group.attributes?.alias ?: ""
            }
        }
        logger.info { "Aliases fetched for ${result.size} groups." }
        return result
    }

    private fun buildInFilter(ids: List<String>): String {
        val idList = ids.joinToString(",") { "\"$it\"" }
        return "{\"id\":{\"\$in\":[$idList]}}"
    }

    private fun encode(
        name: String,
        value: String,
    ): String = "$name=${URLEncoder.encode(value, StandardCharsets.UTF_8)}"

    private inline fun <reified T> get(url: String): T {
        val request =
            HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .header("Authorization", authHeader)
                .header("Accept", "application/json")
                .header("x-endpoint-type", "utility")
                .GET()
                .build()
        val response = http.send(request, HttpResponse.BodyHandlers.ofString())
        check(response.statusCode() == 200) {
            "IAM API request failed [${response.statusCode()}] for URL: $url - ${response.body()}"
        }
        return mapper.readValue(response.body())
    }
}
