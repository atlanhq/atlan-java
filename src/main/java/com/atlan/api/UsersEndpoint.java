/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.*;
import com.atlan.model.core.AtlanObject;
import com.atlan.net.ApiResource;
import com.atlan.net.RequestOptions;
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

    public UsersEndpoint(AtlanClient client) {
        super(client);
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
        return list(filter, sort, count, offset, limit, null);
    }

    /**
     * Retrieves a list of the users defined in Atlan.
     *
     * @param filter which users to retrieve
     * @param sort property by which to sort the results
     * @param count whether to return the total number of records (true) or not (false)
     * @param offset starting point for results to return, for paging
     * @param limit maximum number of results to be returned
     * @param options to override default client settings
     * @return a list of users that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public UserResponse list(String filter, String sort, boolean count, int offset, int limit, RequestOptions options)
            throws AtlanException {
        if (filter == null) {
            filter = "";
        }
        if (sort == null) {
            sort = "";
        }
        String url = String.format(
                "%s%s?filter=%s&sort=%s&count=%s&offset=%s&limit=%s",
                getBaseUrl(),
                endpoint,
                ApiResource.urlEncode(filter),
                ApiResource.urlEncode(sort),
                count,
                offset,
                limit);
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", UserResponse.class, options);
    }

    /**
     * Retrieves a list of the users defined in Atlan.
     *
     * @param filter which users to retrieve
     * @return a list of users that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public UserResponse list(String filter) throws AtlanException {
        return list(filter, null);
    }

    /**
     * Retrieves a list of the users defined in Atlan.
     *
     * @param filter which users to retrieve
     * @param options to override default client settings
     * @return a list of users that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public UserResponse list(String filter, RequestOptions options) throws AtlanException {
        if (filter == null) {
            filter = "";
        }
        String url = String.format("%s%s?filter=%s", getBaseUrl(), endpoint, ApiResource.urlEncode(filter));
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", UserResponse.class, options);
    }

    /**
     * Retrieve all users defined in Atlan.
     *
     * @return a list of all the users in Atlan
     * @throws AtlanException on any API communication issue
     */
    public List<AtlanUser> list() throws AtlanException {
        return list((RequestOptions) null);
    }

    /**
     * Retrieve all users defined in Atlan.
     *
     * @param options to override default client settings
     * @return a list of all the users in Atlan
     * @throws AtlanException on any API communication issue
     */
    public List<AtlanUser> list(RequestOptions options) throws AtlanException {
        List<AtlanUser> users = new ArrayList<>();
        String unlimitedUrl = String.format("%s%s?sort=username", getBaseUrl(), endpoint);
        int limit = 100;
        int offset = 0;
        String url = String.format("%s&limit=%s&offset=%s", unlimitedUrl, limit, offset);
        UserResponse response =
                ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", UserResponse.class, options);
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
     * Retrieves all users with email addresses that contain the provided email.
     * (This could include a complete email address, in which case there should be at
     * most a single item in the returned list, or could be a partial email address
     * such as "@example.com" to retrieve all users with that domain in their email
     * address.)
     *
     * @param email on which to filter the users
     * @return all users whose email addresses contain the provided string
     * @throws AtlanException on any error during API invocation
     */
    public List<AtlanUser> getByEmail(String email) throws AtlanException {
        return getByEmail(email, null);
    }

    /**
     * Retrieves all users with email addresses that contain the provided email.
     * (This could include a complete email address, in which case there should be at
     * most a single item in the returned list, or could be a partial email address
     * such as "@example.com" to retrieve all users with that domain in their email
     * address.)
     *
     * @param email on which to filter the users
     * @param options to override default client settings
     * @return all users whose email addresses contain the provided string
     * @throws AtlanException on any error during API invocation
     */
    public List<AtlanUser> getByEmail(String email, RequestOptions options) throws AtlanException {
        UserResponse response = list("{\"email\":{\"$ilike\":\"%" + email + "%\"}}", options);
        if (response != null && response.getRecords() != null) {
            return response.getRecords();
        } else {
            return null;
        }
    }

    /**
     * Retrieves a user based on the username. (This attempts an exact match on username rather than a
     * contains search.)
     *
     * @param user the username by which to find the user
     * @return the user with that username
     * @throws AtlanException on any error during API invocation
     */
    public AtlanUser getByUsername(String user) throws AtlanException {
        return getByUsername(user, null);
    }

    /**
     * Retrieves a user based on the username. (This attempts an exact match on username rather than a
     * contains search.)
     *
     * @param user the username by which to find the user
     * @param options to override default client settings
     * @return the user with that username
     * @throws AtlanException on any error during API invocation
     */
    public AtlanUser getByUsername(String user, RequestOptions options) throws AtlanException {
        UserResponse response = list("{\"username\":\"" + user + "\"}", options);
        if (response != null && response.getRecords() != null) {
            return response.getRecords().get(0);
        } else {
            return null;
        }
    }

    /**
     * Enable this user to log into Atlan. This will only affect users who are deactivated, and will
     * allow them to login again once completed.
     *
     * @param id the unique identifier (GUID) of the user to activate
     * @return the result of the update to the user
     * @throws AtlanException on any error during API invocation
     */
    public UserMinimalResponse activate(String id) throws AtlanException {
        return activate(id, null);
    }

    /**
     * Enable this user to log into Atlan. This will only affect users who are deactivated, and will
     * allow them to login again once completed.
     *
     * @param id the unique identifier (GUID) of the user to activate
     * @param options to override default client settings
     * @return the result of the update to the user
     * @throws AtlanException on any error during API invocation
     */
    public UserMinimalResponse activate(String id, RequestOptions options) throws AtlanException {
        return update(id, AtlanUser.builder().enabled(true).build(), options);
    }

    /**
     * Prevent this user from logging into Atlan. This will only affect users who are activated, and will
     * prevent them logging in once completed.
     *
     * @param id the unique identifier (GUID) of the user to deactivate
     * @return the result of the update to the user
     * @throws AtlanException on any error during API invocation
     */
    public UserMinimalResponse deactivate(String id) throws AtlanException {
        return deactivate(id, null);
    }

    /**
     * Prevent this user from logging into Atlan. This will only affect users who are activated, and will
     * prevent them logging in once completed.
     *
     * @param id the unique identifier (GUID) of the user to deactivate
     * @param options to override default client settings
     * @return the result of the update to the user
     * @throws AtlanException on any error during API invocation
     */
    public UserMinimalResponse deactivate(String id, RequestOptions options) throws AtlanException {
        return update(id, AtlanUser.builder().enabled(false).build(), options);
    }

    /**
     * Create a new user.
     *
     * @param user the details of the new user
     * @throws AtlanException on any API communication issue
     */
    public void create(AtlanUser user) throws AtlanException {
        create(user, null);
    }

    /**
     * Create a new user.
     *
     * @param user the details of the new user
     * @param options to override default client settings
     * @throws AtlanException on any API communication issue
     */
    public void create(AtlanUser user, RequestOptions options) throws AtlanException {
        create(List.of(user), options);
    }

    /**
     * Create multiple new users.
     *
     * @param users the details of the new users
     * @throws AtlanException on any API communication issue
     */
    public void create(List<AtlanUser> users) throws AtlanException {
        create(users, null);
    }

    /**
     * Create multiple new users.
     *
     * @param users the details of the new users
     * @param options to override default client settings
     * @throws AtlanException on any API communication issue
     */
    public void create(List<AtlanUser> users, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), endpoint);
        CreateUserRequest.CreateUserRequestBuilder<?, ?> cur = CreateUserRequest.builder();
        for (AtlanUser user : users) {
            String roleName = user.getWorkspaceRole();
            cur.user(CreateUser.builder()
                    .email(user.getEmail())
                    .roleName(roleName)
                    .roleId(client.getRoleCache().getIdForName(roleName))
                    .build());
        }
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, cur.build(), null, options);
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
        return update(id, user, null);
    }

    /**
     * Update a user.
     * Note: you can only update users that have already signed up to Atlan. Users that are
     * only invited (but have not yet logged in) cannot be updated.
     *
     * @param id unique identifier (GUID) of the user to update
     * @param user the details to update on the user
     * @param options to override default client settings
     * @throws AtlanException on any API communication issue
     */
    public UserMinimalResponse update(String id, AtlanUser user, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/%s", getBaseUrl(), endpoint, id);
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, user, UserMinimalResponse.class, options);
    }

    /**
     * Delete a user.
     *
     * @param id unique identifier (GUID) of the user to delete
     * @throws AtlanException on any API communication issue
     */
    public void delete(String id) throws AtlanException {
        delete(id, null);
    }

    /**
     * Delete a user.
     *
     * @param id unique identifier (GUID) of the user to delete
     * @param options to override default client settings
     * @throws AtlanException on any API communication issue
     */
    public void delete(String id, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/%s/delete", getBaseUrl(), endpoint, id);
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, "", null, options);
    }

    /**
     * Retrieve the groups this user belongs to.
     *
     * @param id unique identifier (GUID) of the user
     * @throws AtlanException on any API communication issue
     */
    public GroupResponse listGroups(String id) throws AtlanException {
        return listGroups(id, null);
    }

    /**
     * Retrieve the groups this user belongs to.
     *
     * @param id unique identifier (GUID) of the user
     * @param options to override default client settings
     * @throws AtlanException on any API communication issue
     */
    public GroupResponse listGroups(String id, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/%s/groups", getBaseUrl(), endpoint, id);
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", GroupResponse.class, options);
    }

    /**
     * Add a user to one or more groups.
     *
     * @param id unique identifier (GUID) of the user to add into groups
     * @param groupIds unique identifiers (GUIDs) of the groups to add the user into
     * @throws AtlanException on any API communication issue
     */
    public void addToGroups(String id, List<String> groupIds) throws AtlanException {
        addToGroups(id, groupIds, null);
    }

    /**
     * Add a user to one or more groups.
     *
     * @param id unique identifier (GUID) of the user to add into groups
     * @param groupIds unique identifiers (GUIDs) of the groups to add the user into
     * @param options to override default client settings
     * @throws AtlanException on any API communication issue
     */
    public void addToGroups(String id, List<String> groupIds, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/%s/groups", getBaseUrl(), endpoint, id);
        AddToGroupsRequest atgr = AddToGroupsRequest.builder().groups(groupIds).build();
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, atgr, null, options);
    }

    /**
     * Change the role of a user.
     *
     * @param id unique identifier (GUID) of the user whose role should be changed
     * @param roleId unique identifier (GUID) of the role to move the user into
     * @throws AtlanException on any API communication issue
     */
    public void changeRole(String id, String roleId) throws AtlanException {
        changeRole(id, roleId, null);
    }

    /**
     * Change the role of a user.
     *
     * @param id unique identifier (GUID) of the user whose role should be changed
     * @param roleId unique identifier (GUID) of the role to move the user into
     * @param options to override default client settings
     * @throws AtlanException on any API communication issue
     */
    public void changeRole(String id, String roleId, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/%s/roles/update", getBaseUrl(), endpoint, id);
        ChangeRoleRequest crr = ChangeRoleRequest.builder().roleId(roleId).build();
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, crr, null, options);
    }

    /**
     * Retrieve the current user (representing the API token).
     *
     * @return minimalist details about the current user (API token)
     * @throws AtlanException on any API communication issue
     */
    public UserMinimalResponse getCurrentUser() throws AtlanException {
        return getCurrentUser(null);
    }

    /**
     * Retrieve the current user (representing the API token).
     *
     * @param options to override default client settings
     * @return minimalist details about the current user (API token)
     * @throws AtlanException on any API communication issue
     */
    public UserMinimalResponse getCurrentUser(RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/current", getBaseUrl(), endpoint);
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", UserMinimalResponse.class, options);
    }

    /**
     * Retrieve the sessions for a given user.
     *
     * @param id unique identifier (GUID) of the user whose sessions should be retrieved
     * @return the list of sessions for that user
     * @throws AtlanException on any API communication issue
     */
    public SessionResponse listSessions(String id) throws AtlanException {
        return listSessions(id, null);
    }

    /**
     * Retrieve the sessions for a given user.
     *
     * @param id unique identifier (GUID) of the user whose sessions should be retrieved
     * @param options to override default client options
     * @return the list of sessions for that user
     * @throws AtlanException on any API communication issue
     */
    public SessionResponse listSessions(String id, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/%s/sessions", getBaseUrl(), endpoint, id);
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", SessionResponse.class, options);
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
