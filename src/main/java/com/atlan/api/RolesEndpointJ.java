package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.responses.RoleResponseJ;
import com.atlan.net.ApiResourceJ;

public class RolesEndpointJ {

    private static final String endpoint = "/api/service/roles";

    // TODO: eventually provide a rich RQL object for the filter

    /**
     * Retrieves a list of the roles defined in Atlan.
     * @param filter which roles to retrieve
     * @param sort property by which to sort the results
     * @param count whether to return the total number of records (true) or not (false)
     * @param offset starting point for results to return, for paging
     * @param limit maximum number of results to be returned
     * @return a list of roles that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public static RoleResponseJ getRoles(String filter, String sort, boolean count, int offset, int limit)
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
                ApiResourceJ.urlEncode(filter),
                ApiResourceJ.urlEncode(sort),
                count,
                offset,
                limit);
        return ApiResourceJ.request(ApiResourceJ.RequestMethod.GET, url, "", RoleResponseJ.class, null);
    }

    /**
     * Retrieves a list of the roles defined in Atlan.
     * @param filter which roles to retrieve
     * @return a list of roles that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public static RoleResponseJ getRoles(String filter) throws AtlanException {
        if (filter == null) {
            filter = "";
        }
        String url = String.format("%s%s?filter=%s", Atlan.getBaseUrl(), endpoint, ApiResourceJ.urlEncode(filter));
        return ApiResourceJ.request(ApiResourceJ.RequestMethod.GET, url, "", RoleResponseJ.class, null);
    }

    /**
     * Retrieve all roles defined in Atlan.
     * @return a list of all the roles in Atlan
     * @throws AtlanException on any API communication issue
     */
    public static RoleResponseJ getAllRoles() throws AtlanException {
        String url = String.format("%s%s", Atlan.getBaseUrl(), endpoint);
        return ApiResourceJ.request(ApiResourceJ.RequestMethod.GET, url, "", RoleResponseJ.class, null);
    }
}
