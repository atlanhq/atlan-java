/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.model.admin.AtlanGroup;
import com.atlan.model.admin.AtlanUser;
import com.atlan.net.ApiResource;
import com.atlan.net.RequestOptions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * API endpoints for the Redis-backed IAM identity service (/identity/...).
 * Provides bulk lookups for user group memberships and group aliases,
 * replacing per-user Keycloak calls that fail at high user volumes.
 */
@Slf4j
public class IdentityEndpoint extends HeraclesEndpoint {

    private static final String USERS_ENDPOINT = "/identity/users";
    private static final String GROUPS_ENDPOINT = "/identity/groups";
    private static final int DEFAULT_BATCH_SIZE = 500;
    private static final List<String> IDENTITY_USER_PROJECTIONS =
            List.of("firstName", "lastName", "createdTimestamp", "groups");
    private static final RequestOptions UTILITY_OPTIONS = RequestOptions.builder()
            .extraHeaders(Map.of("x-endpoint-type", List.of("utility")))
            .build();

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GroupAliasResponse {
        String id;
        GroupAliasAttributes attributes;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GroupAliasAttributes {
        String alias;
    }

    public IdentityEndpoint(AtlanClient client) {
        super(client);
    }

    /**
     * Bulk-fetch group memberships for the given user IDs.
     *
     * @param userIds list of user IDs to look up
     * @return map of userId to list of AtlanGroup (id + Keycloak group name)
     * @throws AtlanException on any API communication issue
     */
    public Map<String, List<AtlanGroup>> getUserGroups(List<String> userIds) throws AtlanException {
        return getUserGroups(userIds, DEFAULT_BATCH_SIZE);
    }

    /**
     * Bulk-fetch group memberships for the given user IDs.
     *
     * @param userIds list of user IDs to look up
     * @param batchSize number of IDs per request
     * @return map of userId to list of AtlanGroup (id + Keycloak group name)
     * @throws AtlanException on any API communication issue
     */
    public Map<String, List<AtlanGroup>> getUserGroups(List<String> userIds, int batchSize) throws AtlanException {
        if (userIds == null || userIds.isEmpty()) return new HashMap<>();
        Map<String, List<AtlanGroup>> result = new HashMap<>();
        try (AtlanClient sudo = new AtlanClient("INTERNAL", client.impersonate.escalate())) {
            for (List<String> batch : partition(userIds, batchSize)) {
                String url = sudo.identity.getBaseUrl() + USERS_ENDPOINT + "?" + encode("filter", buildInFilter(batch))
                        + "&columns=groups";
                String body =
                        ApiResource.requestPlainText(sudo, ApiResource.RequestMethod.GET, url, "", UTILITY_OPTIONS);
                List<AtlanUser> responses = parseArray(sudo, body, new TypeReference<>() {});
                for (AtlanUser r : responses) {
                    result.put(r.getId(), r.getGroups() != null ? r.getGroups() : List.of());
                }
            }
        } catch (java.io.IOException e) {
            throw new com.atlan.exception.ApiConnectionException(ErrorCode.CONNECTION_ERROR, e, e.getMessage());
        }
        return result;
    }

    /**
     * Bulk-fetch full user details (including group memberships) for the given user IDs.
     * Returns each user's id, username, email, enabled, firstName, lastName,
     * createdTimestamp, groups, and attributes in a single API call per batch,
     * eliminating the need for a separate {@link #getUserGroups} call.
     *
     * @param userIds list of user IDs to look up
     * @return map of userId to AtlanUser
     * @throws AtlanException on any API communication issue
     */
    public Map<String, AtlanUser> getUsers(List<String> userIds) throws AtlanException {
        return getUsers(userIds, DEFAULT_BATCH_SIZE);
    }

    /**
     * Bulk-fetch full user details (including group memberships) for the given user IDs.
     *
     * @param userIds list of user IDs to look up
     * @param batchSize number of IDs per request
     * @return map of userId to AtlanUser
     * @throws AtlanException on any API communication issue
     */
    public Map<String, AtlanUser> getUsers(List<String> userIds, int batchSize) throws AtlanException {
        if (userIds == null || userIds.isEmpty()) return new HashMap<>();
        Map<String, AtlanUser> result = new HashMap<>();
        String columns =
                IDENTITY_USER_PROJECTIONS.stream().map(c -> "columns=" + c).collect(Collectors.joining("&"));
        try (AtlanClient sudo = new AtlanClient("INTERNAL", client.impersonate.escalate())) {
            for (List<String> batch : partition(userIds, batchSize)) {
                String url = sudo.identity.getBaseUrl() + USERS_ENDPOINT + "?" + encode("filter", buildInFilter(batch))
                        + "&" + columns;
                String body =
                        ApiResource.requestPlainText(sudo, ApiResource.RequestMethod.GET, url, "", UTILITY_OPTIONS);
                List<AtlanUser> responses = parseArray(sudo, body, new TypeReference<>() {});
                for (AtlanUser r : responses) {
                    result.put(r.getId(), r);
                }
            }
        } catch (java.io.IOException e) {
            throw new com.atlan.exception.ApiConnectionException(ErrorCode.CONNECTION_ERROR, e, e.getMessage());
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
        try (AtlanClient sudo = new AtlanClient("INTERNAL", client.impersonate.escalate())) {
            for (List<String> batch : partition(groupIds, batchSize)) {
                String url = sudo.identity.getBaseUrl() + GROUPS_ENDPOINT + "?" + encode("filter", buildInFilter(batch))
                        + "&columns=attributes";
                String body =
                        ApiResource.requestPlainText(sudo, ApiResource.RequestMethod.GET, url, "", UTILITY_OPTIONS);
                List<GroupAliasResponse> responses = parseArray(sudo, body, new TypeReference<>() {});
                for (GroupAliasResponse r : responses) {
                    String alias = r.getAttributes() != null ? r.getAttributes().getAlias() : null;
                    result.put(r.getId(), alias != null ? alias : "");
                }
            }
        } catch (java.io.IOException e) {
            throw new com.atlan.exception.ApiConnectionException(ErrorCode.CONNECTION_ERROR, e, e.getMessage());
        }
        return result;
    }

    private <T> List<T> parseArray(AtlanClient c, String body, TypeReference<List<T>> typeRef)
            throws java.io.IOException {
        JsonNode node = c.readValue(body, JsonNode.class);
        if (node.isArray()) {
            return c.readValue(body, typeRef);
        }
        for (String field : List.of("list", "records", "data")) {
            JsonNode arr = node.path(field);
            if (!arr.isMissingNode() && arr.isArray()) {
                log.info("Identity API returned wrapped object; extracting '{}' field", field);
                return c.readValue(arr.toString(), typeRef);
            }
        }
        log.warn(
                "Identity API returned unexpected object, no array field found: {}",
                body.substring(0, Math.min(300, body.length())));
        return List.of();
    }

    private String buildInFilter(List<String> ids) {
        String idList = ids.stream().map(id -> "\"" + id + "\"").collect(Collectors.joining(","));
        return "{\"id\":{\"$in\":[" + idList + "]}}";
    }

    private String encode(String name, String value) {
        return name + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static <T> List<List<T>> partition(List<T> list, int size) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            result.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return result;
    }
}
