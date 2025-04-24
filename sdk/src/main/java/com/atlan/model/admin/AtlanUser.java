/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
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
    final List<KeycloakEvent> loginEvents;

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
     * @param client connectivity to the Atlan tenant on which to create the user
     * @throws AtlanException on any error during API invocation
     */
    public void create(AtlanClient client) throws AtlanException {
        client.users.create(this);
    }

    /**
     * Send this user to Atlan to create the user in Atlan, and return the created user.
     * Note: this will make 2 API calls, one to create the user and a second to retrieve the created user.
     *
     * @param client connectivity to the Atlan tenant on which to create the user
     * @param returnUser whether to return the created user (true) or not (false)
     * @return the created user
     * @throws AtlanException on any error during API invocation
     */
    public AtlanUser create(AtlanClient client, boolean returnUser) throws AtlanException {
        return client.users.create(this, returnUser);
    }

    /**
     * Send this user to Atlan to update the user in Atlan.
     * Note: you can only update users that have already signed up to Atlan. Users that are
     * only invited (but have not yet logged in) cannot be updated.
     *
     * @param client connectivity to the Atlan tenant where the user should be updated
     * @throws AtlanException on any error during API invocation
     */
    public UserMinimalResponse update(AtlanClient client) throws AtlanException {
        if (this.id == null || this.id.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_USER_ID);
        }
        return client.users.update(this.id, this);
    }

    /**
     * Add this user to one or more groups.
     *
     * @param client connectivity to the Atlan tenant where the user should be added to one or more groups
     * @param groupIds unique identifiers (GUIDs) of the groups to add the user into
     * @throws AtlanException on any API communication issue
     */
    public void addToGroups(AtlanClient client, List<String> groupIds) throws AtlanException {
        if (this.id == null || this.id.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_USER_ID);
        }
        client.users.addToGroups(this.id, groupIds);
    }

    /**
     * Fetch the groups this user belongs to.
     *
     * @param client connectivity to the Atlan tenant from which the groups for the user should be fetched
     * @return details of the groups the user belongs to
     * @throws AtlanException on any API communication issue
     */
    public GroupResponse fetchGroups(AtlanClient client) throws AtlanException {
        if (this.id == null || this.id.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_USER_ID);
        }
        return client.users.listGroups(this.id);
    }

    /**
     * Retrieves all users currently defined in Atlan.
     *
     * @param client connectivity to the Atlan tenant from which users should be listed
     * @return the list of users currently defined in Atlan
     * @throws AtlanException on any error during API invocation
     */
    public static List<AtlanUser> list(AtlanClient client) throws AtlanException {
        return client.users.list();
    }

    /**
     * Retrieves all users with email addresses that contain the provided email.
     * (This could include a complete email address, in which case there should be at
     * most a single item in the returned list, or could be a partial email address
     * such as "@example.com" to retrieve all users with that domain in their email
     * address.)
     *
     * @param client connectivity to the Atlan tenant from which to list users
     * @param email on which to filter the users
     * @return all users whose email addresses contain the provided string
     * @throws AtlanException on any error during API invocation
     */
    @JsonIgnore
    public static List<AtlanUser> getByEmail(AtlanClient client, String email) throws AtlanException {
        return client.users.getByEmail(email);
    }

    /**
     * Retrieves a user based on the username. (This attempts an exact match on username rather than a
     * contains search.)
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the user
     * @param user the username by which to find the user
     * @return the user with that username
     * @throws AtlanException on any error during API invocation
     */
    @JsonIgnore
    public static AtlanUser getByUsername(AtlanClient client, String user) throws AtlanException {
        return client.users.getByUsername(user);
    }

    /**
     * Change the role of this user.
     *
     * @param client connectivity to the Atlan tenant in which to change the user's role
     * @param roleId unique identifier (GUID) of the role to move the user into
     * @throws AtlanException on any API communication issue
     */
    public void changeRole(AtlanClient client, String roleId) throws AtlanException {
        client.users.changeRole(this.id, roleId);
    }

    /**
     * Retrieve the sessions for this user.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the user's sessions
     * @return the list of sessions for this user
     * @throws AtlanException on any API communication issue
     */
    public SessionResponse fetchSessions(AtlanClient client) throws AtlanException {
        return client.users.listSessions(this.id);
    }

    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class UserAttributes extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** (Unused) */
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

        /** User-provided role during initial registration. */
        List<String> profileRole;

        /** User-provided role during initial registration when profileRole is "other". */
        List<String> profileRoleOther;
    }

    /**
     * Personas associated with a user.
     */
    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
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

        /** {@inheritDoc} */
        @Override
        public int compareTo(Persona o) {
            return personaComparator.compare(this, o);
        }
    }
}
