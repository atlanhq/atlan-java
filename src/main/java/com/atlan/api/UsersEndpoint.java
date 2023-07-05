/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.*;
import com.atlan.model.core.AtlanObject;
import com.atlan.net.ApiResource;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

/**
 * API endpoints for interacting with Atlan's users.
 */
public class UsersEndpoint extends HeraclesEndpoint {

    private static final String endpoint = "/users";

    private final AtlanClient client;

    public UsersEndpoint(AtlanClient client) {
        this.client = client;
    }

    // TODO: eventually provide a rich RQL object for the filter

    /**
     * Retrieves a list of the users defined in Atlan.
     *
     * @param filter which users to retrieve
     * @param sort property by which to sort the results
     * @param count whether to return the total number of records (true) or not (false)
     * @param offset starting point for results to return, for paging
     * @param limit maximum number of results to be returned
     * @return a list of users that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public UserResponse list(String filter, String sort, boolean count, int offset, int limit) throws AtlanException {
        if (filter == null) {
            filter = "";
        }
        if (sort == null) {
            sort = "";
        }
        String url = String.format(
                "%s%s?filter=%s&sort=%s&count=%s&offset=%s&limit=%s",
                getBaseUrl(client),
                endpoint,
                ApiResource.urlEncode(filter),
                ApiResource.urlEncode(sort),
                count,
                offset,
                limit);
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", UserResponse.class, null);
    }

    /**
     * Retrieves a list of the users defined in Atlan.
     *
     * @param filter which users to retrieve
     * @return a list of users that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public UserResponse list(String filter) throws AtlanException {
        if (filter == null) {
            filter = "";
        }
        String url = String.format("%s%s?filter=%s", getBaseUrl(client), endpoint, ApiResource.urlEncode(filter));
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", UserResponse.class, null);
    }

    /**
     * Retrieve all users defined in Atlan.
     *
     * @return a list of all the users in Atlan
     * @throws AtlanException on any API communication issue
     */
    public List<AtlanUser> list() throws AtlanException {
        List<AtlanUser> users = new ArrayList<>();
        String unlimitedUrl = String.format("%s%s?sort=username", getBaseUrl(client), endpoint);
        int limit = 100;
        int offset = 0;
        String url = String.format("%s&limit=%s&offset=%s", unlimitedUrl, limit, offset);
        UserResponse response =
                ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", UserResponse.class, null);
        while (response != null) {
            List<AtlanUser> page = response.getRecords();
            if (page != null) {
                users.addAll(page);
                offset += limit;
                url = String.format("%s&limit=%s&offset=%s", unlimitedUrl, limit, offset);
                response =
                        ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", UserResponse.class, null);
            } else {
                response = null;
            }
        }
        return users;
    }

    /**
     * Create a new user.
     *
     * @param user the details of the new user
     * @throws AtlanException on any API communication issue
     */
    public void create(AtlanUser user) throws AtlanException {
        create(List.of(user));
    }

    /**
     * Create multiple new users.
     *
     * @param users the details of the new users
     * @throws AtlanException on any API communication issue
     */
    public void create(List<AtlanUser> users) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(client), endpoint);
        CreateUserRequest.CreateUserRequestBuilder<?, ?> cur = CreateUserRequest.builder();
        for (AtlanUser user : users) {
            String roleName = user.getWorkspaceRole();
            cur.user(CreateUser.builder()
                    .email(user.getEmail())
                    .roleName(roleName)
                    .roleId(client.getRoleCache().getIdForName(roleName))
                    .build());
        }
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, cur.build(), null, null);
    }

    /**
     * Update a user.
     * Note: you can only update users that have already signed up to Atlan. Users that are
     * only invited (but have not yet logged in) cannot be updated.
     *
     * @param id unique identifier (GUID) of the user to update
     * @param user the details to update on the user
     * @throws AtlanException on any API communication issue
     */
    public UserMinimalResponse update(String id, AtlanUser user) throws AtlanException {
        String url = String.format("%s%s/%s", getBaseUrl(client), endpoint, id);
        return ApiResource.request(client, ApiResource.RequestMethod.POST, url, user, UserMinimalResponse.class, null);
    }

    /**
     * Delete a user.
     *
     * @param id unique identifier (GUID) of the user to delete
     * @throws AtlanException on any API communication issue
     */
    public void delete(String id) throws AtlanException {
        String url = String.format("%s%s/%s/delete", getBaseUrl(client), endpoint, id);
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, "", null, null);
    }

    /**
     * Retrieve the groups this user belongs to.
     *
     * @param id unique identifier (GUID) of the user
     * @throws AtlanException on any API communication issue
     */
    public GroupResponse listGroups(String id) throws AtlanException {
        String url = String.format("%s%s/%s/groups", getBaseUrl(client), endpoint, id);
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", GroupResponse.class, null);
    }

    /**
     * Add a user to one or more groups.
     *
     * @param id unique identifier (GUID) of the user to add into groups
     * @param groupIds unique identifiers (GUIDs) of the groups to add the user into
     * @throws AtlanException on any API communication issue
     */
    public void addToGroups(String id, List<String> groupIds) throws AtlanException {
        String url = String.format("%s%s/%s/groups", getBaseUrl(client), endpoint, id);
        AddToGroupsRequest atgr = AddToGroupsRequest.builder().groups(groupIds).build();
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, atgr, null, null);
    }

    /**
     * Change the role of a user.
     *
     * @param id unique identifier (GUID) of the user whose role should be changed
     * @param roleId unique identifier (GUID) of the role to move the user into
     * @throws AtlanException on any API communication issue
     */
    public void changeRole(String id, String roleId) throws AtlanException {
        String url = String.format("%s%s/%s/roles/update", getBaseUrl(client), endpoint, id);
        ChangeRoleRequest crr = ChangeRoleRequest.builder().roleId(roleId).build();
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, crr, null, null);
    }

    /**
     * Retrieve the current user (representing the API token).
     *
     * @return minimalist details about the current user (API token)
     * @throws AtlanException on any API communication issue
     */
    public UserMinimalResponse getCurrentUser() throws AtlanException {
        String url = String.format("%s%s/current", getBaseUrl(client), endpoint);
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", UserMinimalResponse.class, null);
    }

    /**
     * Retrieve the sessions for a given user.
     *
     * @param id unique identifier (GUID) of the user whose sessions should be retrieved
     * @return the list of sessions for that user
     * @throws AtlanException on any API communication issue
     */
    public SessionResponse listSessions(String id) throws AtlanException {
        String url = String.format("%s%s/%s/sessions", getBaseUrl(client), endpoint, id);
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", SessionResponse.class, null);
    }

    /** Request class for creating a user. */
    @Getter
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    static final class CreateUserRequest extends AtlanObject {
        @Singular
        List<CreateUser> users;
    }

    /** Request class for changing a user's role. */
    @Getter
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    static final class ChangeRoleRequest extends AtlanObject {
        String roleId;
    }

    @Getter
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    private static final class CreateUser extends AtlanObject {
        String email;
        String roleName;
        String roleId;
    }

    /** Request class for adding a user to one or more groups. */
    @Getter
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    static final class AddToGroupsRequest extends AtlanObject {
        List<String> groups;
    }
}
