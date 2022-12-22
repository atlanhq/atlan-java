/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.api.UsersEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class AtlanUser extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Username of the user within Atlan. */
    String username;

    /** Unique identifier (GUID) of the user within Atlan. */
    String id;

    /** Name of the role of the user within Atlan. */
    String workspaceRole;

    /** Email address of the user. */
    String email;

    /** When true, the email address of the user has been verified. */
    Boolean emailVerified;

    /** When true, the user is enabled. When false, the user has been deactivated. */
    Boolean enabled;

    /** First name of the user. */
    String firstName;

    /** Last name (surname) of the user. */
    String lastName;

    /** Detailed attributes of the user. */
    UserAttributes attributes;

    /** Time (epoch) at which the user was created, in milliseconds. */
    Long createdTimestamp;

    /** Time (epoch) at which the user last logged into Atlan. */
    Long lastLoginTime;

    /** Number of groups to which the user belongs. */
    Long groupCount;

    /** TBC */
    List<String> defaultRoles;

    /** TBC */
    List<String> roles;

    /** TBC */
    @JsonIgnore // TODO
    String decentralizedRoles;

    /** Personas the user is associated with. */
    SortedSet<AtlanUser.Persona> personas;

    /** Purposes the user is associated with. */
    @JsonIgnore // TODO
    SortedSet<String> purposes;

    /** List of administration-related events for this user. */
    final List<AdminEvent> adminEvents;

    /** List of login-related events for this user. */
    final List<LoginEvent> loginEvents;

    /**
     * Builds the minimal object necessary to create (invite) a user.
     *
     * @param email email address of the user
     * @param roleName name of the role for the user ({@code $admin}, {@code $member}, or {@code $guest})
     * @return the minimal request necessary to update the user, as a builder
     */
    public static AtlanUserBuilder<?, ?> creator(String email, String roleName) {
        return AtlanUser.builder().email(email).workspaceRole(roleName);
    }

    /**
     * Builds the minimal object necessary to update a user.
     *
     * @param id unique identifier (GUID) of the user
     * @return the minimal request necessary to update the user, as a builder
     */
    public static AtlanUserBuilder<?, ?> updater(String id) {
        return AtlanUser.builder().id(id);
    }

    /** Send this user to Atlan to create the user in Atlan.
     *
     * @throws AtlanException on any error during API invocation
     */
    public void create() throws AtlanException {
        UsersEndpoint.createUser(this);
    }

    /**
     * Send this user to Atlan to update the user in Atlan.
     * Note: you can only update users that have already signed up to Atlan. Users that are
     * only invited (but have not yet logged in) cannot be updated.
     *
     * @throws AtlanException on any error during API invocation
     */
    public UserMinimalResponse update() throws AtlanException {
        if (this.id == null || this.id.length() == 0) {
            throw new InvalidRequestException(ErrorCode.MISSING_USER_ID);
        }
        return UsersEndpoint.updateUser(this.id, this);
    }

    /**
     * Delete a user from Atlan.
     *
     * @param id unique identifier (GUID) of the user to delete
     * @throws AtlanException on any error during API invocation
     */
    public static void delete(String id) throws AtlanException {
        UsersEndpoint.deleteUser(id);
    }

    /**
     * Add this user to one or more groups.
     *
     * @param groupIds unique identifiers (GUIDs) of the groups to add the user into
     * @throws AtlanException on any API communication issue
     */
    public void addToGroups(List<String> groupIds) throws AtlanException {
        if (this.id == null || this.id.length() == 0) {
            throw new InvalidRequestException(ErrorCode.MISSING_USER_ID);
        }
        UsersEndpoint.addToGroups(this.id, groupIds);
    }

    /**
     * Fetch the groups this user belongs to.
     *
     * @return details of the groups the user belongs to
     * @throws AtlanException on any API communication issue
     */
    public GroupResponse fetchGroups() throws AtlanException {
        if (this.id == null || this.id.length() == 0) {
            throw new InvalidRequestException(ErrorCode.MISSING_USER_ID);
        }
        return UsersEndpoint.getGroups(this.id);
    }

    /**
     * Retrieves all users currently defined in Atlan.
     * @return the list of users currently defined in Atlan
     * @throws AtlanException on any error during API invocation
     */
    public static List<AtlanUser> retrieveAll() throws AtlanException {
        UserResponse response = UsersEndpoint.getAllUsers();
        if (response != null && response.getRecords() != null) {
            return response.getRecords();
        } else {
            return Collections.emptyList();
        }
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
    public static List<AtlanUser> retrieveByEmail(String email) throws AtlanException {
        UserResponse response = UsersEndpoint.getUsers("{\"email\":{\"$ilike\":\"%" + email + "%\"}}");
        if (response != null && response.getRecords() != null) {
            return response.getRecords();
        } else {
            return null;
        }
    }

    /**
     * Retrieves a user based on the username. (This attemps an exact match on username rather than a
     * contains search.)
     *
     * @param user the username by which to find the user
     * @return the user with that username
     * @throws AtlanException on any error during API invocation
     */
    public static AtlanUser retrieveByUsername(String user) throws AtlanException {
        UserResponse response = UsersEndpoint.getUsers("{\"username\":\"" + user + "\"}");
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
     * @return the result of the update to the user
     * @throws AtlanException on any error during API invocation
     */
    public UserMinimalResponse activate() throws AtlanException {
        return UsersEndpoint.updateUser(
                this.id, AtlanUser.builder().enabled(true).build());
    }

    /**
     * Prevent this user from logging into Atlan. This will only affect users who are activated, and will
     * prevent them logging in once completed.
     *
     * @return the result of the update to the user
     * @throws AtlanException on any error during API invocation
     */
    public UserMinimalResponse deactivate() throws AtlanException {
        return UsersEndpoint.updateUser(
                this.id, AtlanUser.builder().enabled(false).build());
    }

    /**
     * Change the role of this user.
     *
     * @param roleId unique identifier (GUID) of the role to move the user into
     * @throws AtlanException on any API communication issue
     */
    public void changeRole(String roleId) throws AtlanException {
        UsersEndpoint.changeRole(this.id, roleId);
    }

    /**
     * Retrieve the sessions for this user.
     *
     * @return the list of sessions for this user
     * @throws AtlanException on any API communication issue
     */
    public SessionResponse fetchSessions() throws AtlanException {
        return UsersEndpoint.getSessions(this.id);
    }

    @Getter
    @Setter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    public static final class UserAttributes extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Designation for the user, such as an honorific or title. */
        List<String> designation;

        /** Skills the user possesses. */
        List<String> skills;

        /** Unique Slack member identifier. */
        List<String> slack;

        /** Unique JIRA user identifier. */
        List<String> jira;

        /** Time at which the user was invited (as a formatted string). */
        List<String> invitedAt;

        /** User who invited this user. */
        List<String> invitedBy;

        /** TBC */
        List<String> invitedByName;
    }

    @Getter
    @Setter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    public static final class LoginEvent extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Where the login occurred (usually {@code atlan-frontend}). */
        String clientId;

        /** TBC */
        @JsonIgnore
        String details;

        /** IP address from which the user logged in. */
        String ipAddress;

        /** TBC */
        String realmId;

        /** Unique identifier (GUID) of the session for the login. */
        String sessionId;

        /** Time (epoch) when the login occurred, in milliseconds. */
        Long time;

        /** Type of login event that occurred (usually {@code LOGIN}). */
        String type;

        /** Unique identifier (GUID) of the user that logged in. */
        String userId;
    }

    /**
     * Personas associated with a user.
     */
    @Getter
    @Setter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    public static final class Persona extends AtlanObject implements Comparable<Persona> {
        private static final long serialVersionUID = 2L;

        private static final Comparator<String> stringComparator = Comparator.nullsFirst(String::compareTo);
        private static final Comparator<Persona> personaComparator =
                Comparator.comparing(Persona::getId, stringComparator);

        /** Unique identifier (GUID) of the persona. */
        String id;

        /** Internal name of the persona. */
        String name;

        /** Human-readable name of the persona. */
        String displayName;

        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo(Persona o) {
            return personaComparator.compare(this, o);
        }
    }

    @Getter
    @Setter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    public static final class AdminEvent extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Type of admin operation that occurred. */
        String operationType;

        /** TBC */
        String realmId;

        /** TBC */
        String representation;

        /** TBC */
        String resourcePath;

        /** Type of resource for the admin operation that occurred. */
        String resourceType;

        /** Time (epoch) when the admin operation occurred, in milliseconds. */
        Long time;

        /** TBC */
        AuthDetails authDetails;
    }

    @Getter
    @Setter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    public static final class AuthDetails extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** TBC */
        String clientId;

        /** TBC */
        String ipAddress;

        /** TBC */
        String realmId;

        /** TBC */
        String userId;
    }
}
