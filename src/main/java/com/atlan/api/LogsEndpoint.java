/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.AdminEventRequest;
import com.atlan.model.admin.AdminEventResponse;
import com.atlan.model.admin.KeycloakEventRequest;
import com.atlan.model.admin.KeycloakEventResponse;
import com.atlan.net.ApiResource;
import java.util.HashMap;
import java.util.Map;

/**
 * API endpoints for access Atlan's event logs.
 */
public class LogsEndpoint extends HeraclesEndpoint {

    private static final String endpoint = "/events";

    /** Keycloak: /{realm}/admin-events */
    private static final String endpoint_main = endpoint + "/main";

    /** Keycloak: /{realm}/events */
    private static final String endpoint_login = endpoint + "/login";

    public LogsEndpoint(AtlanClient client) {
        super(client);
    }

    /**
     * Retrieve the 100 most recent admin events.
     *
     * @return a list of the 100 most recent admin events
     * @throws AtlanException on any issue interacting with the API
     */
    public AdminEventResponse getAdminEvents() throws AtlanException {
        return getAdminEvents(AdminEventRequest.builder().build());
    }

    /**
     * Retrieve admin events based on the supplied filters.
     *
     * @param request details of the filters to apply when retrieving admin events
     * @return the admin events that match the supplied filters
     * @throws AtlanException on any issue interacting with the API
     */
    public AdminEventResponse getAdminEvents(AdminEventRequest request) throws AtlanException {
        Map<String, Object> queryParams = new HashMap<>();
        if (request.getClientId() != null) {
            queryParams.put("authClient", request.getClientId());
        }
        if (request.getIpAddress() != null) {
            queryParams.put("authIpAddress", request.getIpAddress());
        }
        if (request.getRealmId() != null) {
            queryParams.put("authRealm", request.getRealmId());
        }
        if (request.getUserId() != null) {
            queryParams.put("authUser", request.getUserId());
        }
        if (request.getDateFrom() != null) {
            queryParams.put("dateFrom", request.getDateFrom());
        }
        if (request.getDateTo() != null) {
            queryParams.put("dateTo", request.getDateTo());
        }
        queryParams.put("first", request.getOffset());
        queryParams.put("max", request.getSize());
        if (request.getOperationTypes() != null && !request.getOperationTypes().isEmpty()) {
            queryParams.put("operationTypes", request.getOperationTypes());
        }
        if (request.getResourcePath() != null) {
            queryParams.put("resourcePath", request.getResourcePath());
        }
        if (request.getResourceTypes() != null) {
            queryParams.put("resourceTypes", request.getResourceTypes());
        }
        String url = String.format("%s%s?%s", getBaseUrl(), endpoint_main, ApiResource.createQueryString(queryParams));
        AdminEventResponse response =
                ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", AdminEventResponse.class, null);
        response.setRequest(request);
        response.setClient(client);
        return response;
    }

    /**
     * Retrieve the 100 most recent events.
     *
     * @return a list of the 100 most recent events
     * @throws AtlanException on any issue interacting with the API
     */
    public KeycloakEventResponse getEvents() throws AtlanException {
        return getEvents(KeycloakEventRequest.builder().build());
    }

    /**
     * Retrieve all events, based on the supplied filters.
     *
     * @param request details of the filters to apply when retrieving events
     * @return the events that match the supplied filters
     * @throws AtlanException on any issue interacting with the API
     */
    public KeycloakEventResponse getEvents(KeycloakEventRequest request) throws AtlanException {
        Map<String, Object> queryParams = new HashMap<>();
        if (request.getClient() != null) {
            queryParams.put("client", request.getClient());
        }
        if (request.getIpAddress() != null) {
            queryParams.put("ipAddress", request.getIpAddress());
        }
        if (request.getDateFrom() != null) {
            queryParams.put("dateFrom", request.getDateFrom());
        }
        if (request.getDateTo() != null) {
            queryParams.put("dateTo", request.getDateTo());
        }
        queryParams.put("first", request.getOffset());
        queryParams.put("max", request.getSize());
        if (request.getTypes() != null) {
            queryParams.put("type", request.getTypes());
        }
        if (request.getUserId() != null) {
            queryParams.put("user", request.getUserId());
        }
        String url = String.format("%s%s?%s", getBaseUrl(), endpoint_login, ApiResource.createQueryString(queryParams));
        KeycloakEventResponse response =
                ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", KeycloakEventResponse.class, null);
        response.setRequest(request);
        response.setClient(client);
        return response;
    }
}
