/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.AdminEventRequest;
import com.atlan.model.admin.AdminEventResponse;
import com.atlan.model.admin.KeycloakEventRequest;
import com.atlan.model.admin.KeycloakEventResponse;
import com.atlan.net.ApiResource;
import com.atlan.net.RequestOptions;
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
        return getAdminEvents((RequestOptions) null);
    }

    /**
     * Retrieve the 100 most recent admin events.
     *
     * @param options to override default client options
     * @return a list of the 100 most recent admin events
     * @throws AtlanException on any issue interacting with the API
     */
    public AdminEventResponse getAdminEvents(RequestOptions options) throws AtlanException {
        return getAdminEvents(AdminEventRequest.builder().build(), options);
    }

    /**
     * Retrieve admin events based on the supplied filters.
     *
     * @param request details of the filters to apply when retrieving admin events
     * @return the admin events that match the supplied filters
     * @throws AtlanException on any issue interacting with the API
     */
    public AdminEventResponse getAdminEvents(AdminEventRequest request) throws AtlanException {
        return getAdminEvents(request, null);
    }

    /**
     * Retrieve admin events based on the supplied filters.
     *
     * @param request details of the filters to apply when retrieving admin events
     * @param options to override default client options
     * @return the admin events that match the supplied filters
     * @throws AtlanException on any issue interacting with the API
     */
    public AdminEventResponse getAdminEvents(AdminEventRequest request, RequestOptions options) throws AtlanException {
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
                ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", AdminEventResponse.class, options);
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
        return getEvents((RequestOptions) null);
    }

    /**
     * Retrieve the 100 most recent events.
     *
     * @param options to override default client settings
     * @return a list of the 100 most recent events
     * @throws AtlanException on any issue interacting with the API
     */
    public KeycloakEventResponse getEvents(RequestOptions options) throws AtlanException {
        return getEvents(KeycloakEventRequest.builder().build(), options);
    }

    /**
     * Retrieve all events, based on the supplied filters.
     *
     * @param request details of the filters to apply when retrieving events
     * @return the events that match the supplied filters
     * @throws AtlanException on any issue interacting with the API
     */
    public KeycloakEventResponse getEvents(KeycloakEventRequest request) throws AtlanException {
        return getEvents(request, null);
    }

    /**
     * Retrieve all events, based on the supplied filters.
     *
     * @param request details of the filters to apply when retrieving events
     * @param options to override default client settings
     * @return the events that match the supplied filters
     * @throws AtlanException on any issue interacting with the API
     */
    public KeycloakEventResponse getEvents(KeycloakEventRequest request, RequestOptions options) throws AtlanException {
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
        KeycloakEventResponse response = ApiResource.request(
                client, ApiResource.RequestMethod.GET, url, "", KeycloakEventResponse.class, options);
        response.setRequest(request);
        response.setClient(client);
        return response;
    }
}
