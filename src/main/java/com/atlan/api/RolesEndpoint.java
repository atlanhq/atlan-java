/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.RoleResponse;
import com.atlan.net.ApiResource;

/**
 * API endpoints for interacting with Atlan's roles.
 */
public class RolesEndpoint {

    private static final String endpoint = "/api/service/roles";

    // TODO: eventually provide a rich RQL object for the filter

    /**
     * Retrieves a list of the roles defined in Atlan.
     *
     * @param filter which roles to retrieve
     * @param sort property by which to sort the results
     * @param count whether to return the total number of records (true) or not (false)
     * @param offset starting point for results to return, for paging
     * @param limit maximum number of results to be returned
     * @return a list of roles that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public static RoleResponse getRoles(String filter, String sort, boolean count, int offset, int limit)
            throws AtlanException {
        if (filter == null) {
            filter = "";
        }
        if (sort == null) {
            sort = "";
        }
        String url = String.format(
                "%s%s?filter=%s&sort=%s&count=%s&offset=%s&limit=%s",
                Atlan.getBaseUrl(),
                endpoint,
                ApiResource.urlEncode(filter),
                ApiResource.urlEncode(sort),
                count,
                offset,
                limit);
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", RoleResponse.class, null);
    }

    /**
     * Retrieves a list of the roles defined in Atlan.
     *
     * @param filter which roles to retrieve
     * @return a list of roles that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public static RoleResponse getRoles(String filter) throws AtlanException {
        if (filter == null) {
            filter = "";
        }
        String url = String.format("%s%s?filter=%s", Atlan.getBaseUrl(), endpoint, ApiResource.urlEncode(filter));
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", RoleResponse.class, null);
    }

    /**
     * Retrieve all roles defined in Atlan.
     *
     * @return a list of all the roles in Atlan
     * @throws AtlanException on any API communication issue
     */
    public static RoleResponse getAllRoles() throws AtlanException {
        String url = String.format("%s%s", Atlan.getBaseUrl(), endpoint);
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", RoleResponse.class, null);
    }
}
