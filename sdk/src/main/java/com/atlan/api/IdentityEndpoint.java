/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.ApiConnectionException;
import com.atlan.exception.ApiException;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.model.admin.IamGroupRef;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * API endpoints for the Redis-backed IAM identity service (/identity/...).
 * Provides bulk lookups for user group memberships and group aliases,
 * replacing per-user Keycloak calls that fail at high user volumes.
 */
public class IdentityEndpoint extends HeraclesEndpoint {

    private static final String USERS_ENDPOINT = "/identity/users";
    private static final String GROUPS_ENDPOINT = "/identity/groups";
    private static final int DEFAULT_BATCH_SIZE = 500;

    private final HttpClient http = HttpClient.newHttpClient();

    // Cached once per instance — workflows are short-lived
    private volatile String cachedToken = null;

    // Set only in test instances to override production base URL and JSON mapper
    private final String testBaseUrl;
    private final ObjectMapper testMapper;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class IamUserResponse {
        String id;
        List<IamGroupRef> groups;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class IamGroupAttributes {
        String alias;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class IamGroupResponse {
        String id;
        IamGroupAttributes attributes;
    }

    /** Production constructor — wired in via AtlanClient. */
    public IdentityEndpoint(AtlanClient client) {
        super(client);
        this.testBaseUrl = null;
        this.testMapper = null;
    }

    /** Test-only constructor — bypasses AtlanClient and Keycloak token fetch. */
    private IdentityEndpoint(String testBaseUrl, String bearerToken) {
        super(null);
        this.testBaseUrl = testBaseUrl;
        this.testMapper = new ObjectMapper();
        this.cachedToken = bearerToken;
    }

    /**
     * Creates a test instance that routes all identity calls to the given base URL
     * and uses the supplied bearer token, without requiring a live AtlanClient.
     *
     * @param baseUrl base URL of the stub server (e.g. "http://localhost:8080")
     * @param bearerToken token to use in the Authorization header
     * @return a test-configured IdentityEndpoint
     */
    public static IdentityEndpoint forTesting(String baseUrl, String bearerToken) {
        return new IdentityEndpoint(baseUrl, bearerToken);
    }

    @Override
    protected String getBaseUrl() throws ApiConnectionException {
        return testBaseUrl != null ? testBaseUrl : super.getBaseUrl();
    }

    private String getAuthToken() throws AtlanException {
        if (cachedToken == null) {
            cachedToken = client.impersonate.escalate();
        }
        return "Bearer " + cachedToken;
    }

    /**
     * Bulk-fetch group memberships for the given user IDs.
     *
     * @param userIds list of user IDs to look up
     * @return map of userId to list of IamGroupRef (id + Keycloak group name)
     * @throws AtlanException on any API communication issue
     */
    public Map<String, List<IamGroupRef>> getUserGroups(List<String> userIds) throws AtlanException {
        return getUserGroups(userIds, DEFAULT_BATCH_SIZE);
    }

    /**
     * Bulk-fetch group memberships for the given user IDs.
     *
     * @param userIds list of user IDs to look up
     * @param batchSize number of IDs per request
     * @return map of userId to list of IamGroupRef (id + Keycloak group name)
     * @throws AtlanException on any API communication issue
     */
    public Map<String, List<IamGroupRef>> getUserGroups(List<String> userIds, int batchSize) throws AtlanException {
        if (userIds == null || userIds.isEmpty()) return new HashMap<>();
        Map<String, List<IamGroupRef>> result = new HashMap<>();
        String authToken = getAuthToken();
        for (List<String> batch : partition(userIds, batchSize)) {
            String url =
                    getBaseUrl() + USERS_ENDPOINT + "?" + encode("filter", buildInFilter(batch)) + "&columns=groups";
            List<IamUserResponse> responses = get(url, authToken, new TypeReference<>() {});
            for (IamUserResponse r : responses) {
                result.put(r.getId(), r.getGroups() != null ? r.getGroups() : Collections.emptyList());
            }
        }
        return result;
    }

    /**
     * Bulk-fetch group aliases (display names) for the given group IDs.
     *
     * @param groupIds list of group IDs to look up
     * @return map of groupId to alias (display name), empty string if no alias set
     * @throws AtlanException on any API communication issue
     */
    public Map<String, String> getGroupAliases(List<String> groupIds) throws AtlanException {
        return getGroupAliases(groupIds, DEFAULT_BATCH_SIZE);
    }

    /**
     * Bulk-fetch group aliases (display names) for the given group IDs.
     *
     * @param groupIds list of group IDs to look up
     * @param batchSize number of IDs per request
     * @return map of groupId to alias (display name), empty string if no alias set
     * @throws AtlanException on any API communication issue
     */
    public Map<String, String> getGroupAliases(List<String> groupIds, int batchSize) throws AtlanException {
        if (groupIds == null || groupIds.isEmpty()) return new HashMap<>();
        Map<String, String> result = new HashMap<>();
        String authToken = getAuthToken();
        for (List<String> batch : partition(groupIds, batchSize)) {
            String url = getBaseUrl() + GROUPS_ENDPOINT + "?" + encode("filter", buildInFilter(batch))
                    + "&columns=attributes";
            List<IamGroupResponse> responses = get(url, authToken, new TypeReference<>() {});
            for (IamGroupResponse r : responses) {
                String alias = r.getAttributes() != null ? r.getAttributes().getAlias() : null;
                result.put(r.getId(), alias != null ? alias : "");
            }
        }
        return result;
    }

    private String buildInFilter(List<String> ids) {
        String idList = ids.stream().map(id -> "\"" + id + "\"").collect(Collectors.joining(","));
        return "{\"id\":{\"$in\":[" + idList + "]}}";
    }

    private String encode(String name, String value) {
        return name + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private <T> T get(String url, String authToken, TypeReference<T> typeRef) throws AtlanException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", authToken)
                    .header("Accept", "application/json")
                    .header("x-endpoint-type", "utility")
                    .GET()
                    .build();
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ApiException(
                        ErrorCode.ERROR_PASSTHROUGH, null, String.valueOf(response.statusCode()), url, response.body());
            }
            return testMapper != null
                    ? testMapper.readValue(response.body(), typeRef)
                    : client.readValue(response.body(), typeRef);
        } catch (IOException e) {
            throw new ApiConnectionException(ErrorCode.CONNECTION_ERROR, e, e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiConnectionException(ErrorCode.CONNECTION_ERROR, e, e.getMessage());
        }
    }

    private static <T> List<List<T>> partition(List<T> list, int size) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            result.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return result;
    }
}
