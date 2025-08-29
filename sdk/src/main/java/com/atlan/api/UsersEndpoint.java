/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.*;
import com.atlan.model.core.AtlanObject;
import com.atlan.net.ApiResource;
import com.atlan.net.RequestOptions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

/**
 * API endpoints for interacting with Atlan's users.
 */
public class UsersEndpoint extends HeraclesEndpoint {

    public static final List<String> DEFAULT_PROJECTIONS = List.of(
            "firstName",
            "lastName",
            "username",
            "id",
            "email",
            "emailVerified",
            "enabled",
            "roles",
            "defaultRoles",
            "groupCount",
            "attributes",
            "personas",
            "createdTimestamp",
            "lastLoginTime",
            "loginEvents",
            "isLocked",
            "workspaceRole",
            "assignedRole");

    private static final String endpoint = "/users";

    public UsersEndpoint(AtlanClient client) {
        super(client);
    }

    // TODO: eventually provide a rich RQL object for the filter

    /**
     * Retrieve users defined in Atlan.
     *
     * @param request containing details about which users to retrieve
     * @return a list of the users in Atlan
     * @throws AtlanException on any API communication issue
     */
    public UserResponse list(UserRequest request) throws AtlanException {
        return list(request, null);
    }

    /**
     * Retrieve users defined in Atlan.
     *
     * @param request containing details about which users to retrieve
     * @param options to override default client settings
     * @return a list of the users in Atlan
     * @throws AtlanException on any API communication issue
     */
    public UserResponse list(UserRequest request, RequestOptions options) throws AtlanException {
        List<String> queryParams = new ArrayList<>();
        queryParams.add("maxLoginEvents=" + request.getMaxLoginEvents());
        if (request.getFilter() != null) {
            queryParams.add("filter=" + ApiResource.urlEncode(request.getFilter()));
        }
        if (request.getSort() != null) {
            queryParams.add("sort=" + ApiResource.urlEncode(request.getSort()));
        }
        queryParams.add("count=" + request.getCount());
        queryParams.add("offset=" + request.getOffset());
        queryParams.add("limit=" + request.getLimit());
        if (!request.getColumns().isEmpty()) {
            queryParams.add("columns=" + String.join("&columns=", request.getColumns()));
        }
        String url = String.format("%s%s?%s", getBaseUrl(), endpoint, String.join("&", queryParams));
        UserResponse response =
                ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", UserResponse.class, options);
        response.setClient(client);
        response.setRequest(request);
        return response;
    }

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
        return list(
                UserRequest.builder()
                        .filter(filter)
                        .sort(sort)
                        .count(count)
                        .offset(offset)
                        .limit(limit)
                        .build(),
                null);
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
        return list(
                UserRequest.builder()
                        .filter(filter)
                        .sort(sort)
                        .count(count)
                        .offset(offset)
                        .limit(limit)
                        .build(),
                options);
    }

    /**
     * Retrieves a list of the users defined in Atlan.
     *
     * @param filter which users to retrieve
     * @return a list of users that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public UserResponse list(String filter) throws AtlanException {
        return list(UserRequest.builder().filter(filter).build(), null);
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
        return list(
                UserRequest.builder()
                        .filter(filter)
                        .columns(DEFAULT_PROJECTIONS)
                        .build(),
                options);
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
        return list(20, options);
    }

    /**
     * Retrieve all users defined in Atlan.
     *
     * @param pageSize maximum number of users to retrieve per request
     * @param options to override default client settings
     * @return a list of all the users in Atlan
     * @throws AtlanException on any API communication issue
     */
    public List<AtlanUser> list(int pageSize, RequestOptions options) throws AtlanException {
        List<AtlanUser> users = new ArrayList<>();
        UserResponse response = list(
                UserRequest.builder()
                        .limit(pageSize)
                        .columns(DEFAULT_PROJECTIONS)
                        .build(),
                options);
        if (response != null) {
            for (AtlanUser user : response) {
                users.add(user);
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
     * Retrieves all users with the email addresses provided.
     *
     * @param emails list of email addresses for which to retrieve users
     * @return all users who have one of the provided email addresses
     * @throws AtlanException on any error during API invocation
     */
    public List<AtlanUser> getByEmails(List<String> emails) throws AtlanException {
        return getByEmails(emails, null);
    }

    /**
     * Retrieves all users with the email addresses provided.
     *
     * @param emails list of email addresses for which to retrieve users
     * @param options to override default client settings
     * @return all users who have one of the provided email addresses
     * @throws AtlanException on any error during API invocation
     */
    public List<AtlanUser> getByEmails(List<String> emails, RequestOptions options) throws AtlanException {
        String emailList;
        if (emails == null || emails.isEmpty()) {
            emailList = "[\"\"]";
        } else {
            emailList = "[" + emails.stream().map(u -> "\"" + u + "\"").collect(Collectors.joining(",")) + "]";
        }
        UserResponse response = list("{\"email\":{\"$in\":" + emailList + "}}", options);
        if (response != null && response.getRecords() != null) {
            return response.getRecords();
        } else {
            return Collections.emptyList();
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
        if (response != null
                && response.getRecords() != null
                && !response.getRecords().isEmpty()) {
            return response.getRecords().get(0);
        } else {
            return null;
        }
    }

    /**
     * Retrieves users based on a list of usernames. (This attempts an exact match on username rather than a
     * contains search.)
     *
     * @param users the usernames by which to find the users
     * @return the users with one of those usernames
     * @throws AtlanException on any error during API invocation
     */
    public List<AtlanUser> getByUsernames(List<String> users) throws AtlanException {
        return getByUsernames(users, null);
    }

    /**
     * Retrieves users based on a list of usernames. (This attempts an exact match on username rather than a
     * contains search.)
     *
     * @param users the usernames by which to find the users
     * @param options to override default client settings
     * @return the users with one of those usernames
     * @throws AtlanException on any error during API invocation
     */
    public List<AtlanUser> getByUsernames(List<String> users, RequestOptions options) throws AtlanException {
        String userList;
        if (users == null || users.isEmpty()) {
            userList = "[\"\"]";
        } else {
            userList = "[" + users.stream().map(u -> "\"" + u + "\"").collect(Collectors.joining(",")) + "]";
        }
        UserResponse response = list("{\"username\":{\"$in\":" + userList + "}}", options);
        if (response != null && response.getRecords() != null) {
            return response.getRecords();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves the user with a unique ID (GUID) that exactly matches the provided string.
     *
     * @param guid unique identifier by which to retrieve the user
     * @return the user whose GUID matches the provided string, or null if there is none
     * @throws AtlanException on any error during API invocation
     */
    public AtlanUser getByGuid(String guid) throws AtlanException {
        return getByGuid(guid, null);
    }

    /**
     * Retrieves the user with a unique ID (GUID) that exactly matches the provided string.
     *
     * @param guid unique identifier by which to retrieve the user
     * @param options to override default client settings
     * @return the user whose GUID matches the provided string, or null if there is none
     * @throws AtlanException on any error during API invocation
     */
    public AtlanUser getByGuid(String guid, RequestOptions options) throws AtlanException {
        UserResponse response = list("{\"id\":\"" + guid + "\"}", options);
        if (response != null
                && response.getRecords() != null
                && !response.getRecords().isEmpty()) {
            return response.getRecords().get(0);
        } else {
            return null;
        }
    }

    /**
     * Create a new user.
     *
     * @param user the details of the new user
     * @throws AtlanException on any API communication issue
     */
    public void create(AtlanUser user) throws AtlanException {
        create(user, (RequestOptions) null);
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
     * Create a new user.
     *
     * @param user the details of the new user
     * @param returnUser if true, lookup and return the created user as well
     * @return the created user
     * @throws AtlanException on any API communication issue
     */
    public AtlanUser create(AtlanUser user, boolean returnUser) throws AtlanException {
        return create(user, returnUser, null);
    }

    /**
     * Create a new user.
     *
     * @param user the details of the new user
     * @param returnUser if true, lookup and return the created user as well
     * @param options to override default client settings
     * @return the created user
     * @throws AtlanException on any API communication issue
     */
    public AtlanUser create(AtlanUser user, boolean returnUser, RequestOptions options) throws AtlanException {
        create(List.of(user), options);
        if (returnUser) {
            return getByUsername(user.getUsername());
        } else {
            return null;
        }
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
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, cur.build(), options);
    }

    /**
     * Create multiple new users.
     *
     * @param users the details of the new users
     * @param returnUsers if true, lookup and return the created users as well
     * @return the created users
     * @throws AtlanException on any API communication issue
     */
    public List<AtlanUser> create(List<AtlanUser> users, boolean returnUsers) throws AtlanException {
        return create(users, returnUsers, null);
    }

    /**
     * Create multiple new users.
     *
     * @param users the details of the new users
     * @param returnUsers if true, lookup and return the created users as well
     * @param options to override default client settings
     * @return the created users
     * @throws AtlanException on any API communication issue
     */
    public List<AtlanUser> create(List<AtlanUser> users, boolean returnUsers, RequestOptions options)
            throws AtlanException {
        create(users, options);
        if (returnUsers) {
            return getByUsernames(users.stream().map(AtlanUser::getUsername).collect(Collectors.toList()), options);
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Update a user.
     * Note: you can only update users that have already signed up to Atlan. Users that are
     * only invited (but have not yet logged in) cannot be updated.
     *
     * @param id unique identifier (GUID) of the user to update
     * @param user the details to update on the user
     * @return basic details about the updated user
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
     * @return basic details about the updated user
     * @throws AtlanException on any API communication issue
     */
    public UserMinimalResponse update(String id, AtlanUser user, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/%s", getBaseUrl(), endpoint, id);
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, user, UserMinimalResponse.class, options);
    }

    /**
     * Retrieve the groups this user belongs to.
     *
     * @param id unique identifier (GUID) of the user
     * @param request containing details about which groups to retrieve
     * @return groups this user belongs to
     * @throws AtlanException on any API communication issue
     */
    public GroupResponse listGroups(String id, GroupRequest request) throws AtlanException {
        return listGroups(id, request, null);
    }

    /**
     * Retrieve the groups this user belongs to.
     *
     * @param id unique identifier (GUID) of the user
     * @param request containing details about which groups to retrieve
     * @param options to override default client settings
     * @return groups this user belongs to
     * @throws AtlanException on any API communication issue
     */
    public GroupResponse listGroups(String id, GroupRequest request, RequestOptions options) throws AtlanException {
        List<String> queryParams = new ArrayList<>();
        queryParams.add("offset=" + request.getOffset());
        queryParams.add("limit=" + request.getLimit());
        queryParams.add("sort=" + ApiResource.urlEncode(request.getSort()));
        if (request.getFilter() != null) {
            queryParams.add("filter=" + ApiResource.urlEncode(request.getFilter()));
        }
        String url = String.format("%s%s/%s/groups?%s", getBaseUrl(), endpoint, id, String.join("&", queryParams));
        GroupResponse response =
                ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", GroupResponse.class, options);
        response.setClient(client);
        response.setRequest(request);
        return response;
    }

    /**
     * Retrieve the groups this user belongs to.
     *
     * @param id unique identifier (GUID) of the user
     * @return groups this user belongs to
     * @throws AtlanException on any API communication issue
     */
    public GroupResponse listGroups(String id) throws AtlanException {
        return listGroups(id, (RequestOptions) null);
    }

    /**
     * Retrieve the groups this user belongs to.
     *
     * @param id unique identifier (GUID) of the user
     * @param options to override default client settings
     * @return groups this user belongs to
     * @throws AtlanException on any API communication issue
     */
    public GroupResponse listGroups(String id, RequestOptions options) throws AtlanException {
        return listGroups(id, GroupRequest.builder().build(), options);
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
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, atgr, options);
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
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, crr, options);
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
    @SuppressWarnings("serial")
    static final class CreateUserRequest extends AtlanObject {
        private static final long serialVersionUID = 2L;

        @Singular
        List<CreateUser> users;
    }

    /** Request class for changing a user's role. */
    @Getter
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    static final class ChangeRoleRequest extends AtlanObject {
        private static final long serialVersionUID = 2L;
        String roleId;
    }

    @Getter
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    private static final class CreateUser extends AtlanObject {
        private static final long serialVersionUID = 2L;
        String email;
        String roleName;
        String roleId;
    }

    /** Request class for adding a user to one or more groups. */
    @Getter
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    @SuppressWarnings("serial")
    static final class AddToGroupsRequest extends AtlanObject {
        private static final long serialVersionUID = 2L;
        List<String> groups;
    }
}
