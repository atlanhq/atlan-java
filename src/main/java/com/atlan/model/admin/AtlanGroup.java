/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AtlanGroup extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Name of the group as it appears in the UI. */
    String alias;

    /** Detailed attributes of the group. */
    GroupAttributes attributes;

    /** TBC */
    @JsonIgnore // TODO
    String decentralizedRoles;

    /** Unique identifier for the group (GUID). */
    String id;

    /** Unique (internal) name for the group. */
    String name;

    /** TBC */
    String path;

    /** Personas the group is associated with. */
    @JsonIgnore // TODO
    SortedSet<String> personas;

    /** Purposes the group is associated with. */
    @JsonIgnore // TODO
    SortedSet<String> purposes;

    /** Number of users in the group. */
    Long userCount;

    /** Whether this group is a default (auto-assigned to new users) (true), or not (false). */
    @JsonIgnore
    public boolean isDefault() {
        return attributes != null
                && attributes.getIsDefault() != null
                && attributes.getIsDefault().contains("true");
    }

    /**
     * Builds the minimal object necessary to create a group.
     *
     * @param alias name of the group, as it should appear in the UI
     * @return the minimal request necessary to update the group, as a builder
     */
    public static AtlanGroupBuilder<?, ?> creator(String alias) {
        return AtlanGroup.builder()
                .name(generateName(alias))
                .attributes(GroupAttributes.builder().alias(List.of(alias)).build());
    }

    /**
     * Builds the minimal object necessary to update a group.
     *
     * @param id unique identifier (GUID) of the group
     * @param path unique path of the group
     * @return the minimal request necessary to update the group, as a builder
     */
    public static AtlanGroupBuilder<?, ?> updater(String id, String path) {
        return AtlanGroup.builder().id(id).path(path);
    }

    /**
     * Generate an internal name for this group from its UI-shown alias.
     *
     * @param alias name of the group as it appears in the UI
     * @return internal name for the group
     */
    public static String generateName(String alias) {
        String internal = alias.toLowerCase(Locale.ROOT);
        return internal.replace(" ", "_");
    }

    /** Send this group to Atlan to create the group in Atlan.
     *
     * @return the unique identifier (GUID) of the group that was created, or null if no group was created
     * @throws AtlanException on any error during API invocation
     */
    public String create() throws AtlanException {
        return create(Atlan.getDefaultClient());
    }

    /** Send this group to Atlan to create the group in Atlan.
     *
     * @param client connectivity to the Atlan tenant on which to create the group
     * @return the unique identifier (GUID) of the group that was created, or null if no group was created
     * @throws AtlanException on any error during API invocation
     */
    public String create(AtlanClient client) throws AtlanException {
        return client.groups.create(this);
    }

    /**
     * Send this group to Atlan to update the group in Atlan.
     *
     * @throws AtlanException on any error during API invocation
     */
    public void update() throws AtlanException {
        update(Atlan.getDefaultClient());
    }

    /**
     * Send this group to Atlan to update the group in Atlan.
     *
     * @param client connectivity to the Atlan tenant on which to update the group
     * @throws AtlanException on any error during API invocation
     */
    public void update(AtlanClient client) throws AtlanException {
        if (this.id == null || this.id.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_GROUP_ID);
        }
        client.groups.update(this.id, this);
    }

    /**
     * Delete a group from Atlan.
     *
     * @param id unique identifier (GUID) of the group to delete
     * @throws AtlanException on any error during API invocation
     */
    public static void delete(String id) throws AtlanException {
        delete(Atlan.getDefaultClient(), id);
    }

    /**
     * Delete a group from Atlan.
     *
     * @param client connectivity to the Atlan tenant from which to delete the group
     * @param id unique identifier (GUID) of the group to delete
     * @throws AtlanException on any error during API invocation
     */
    public static void delete(AtlanClient client, String id) throws AtlanException {
        client.groups.purge(id);
    }

    /**
     * Retrieves all groups currently defined in Atlan.
     *
     * @return the list of groups currently defined in Atlan
     * @throws AtlanException on any error during API invocation
     * @deprecated see {@link #list()} instead
     */
    @Deprecated
    public static List<AtlanGroup> retrieveAll() throws AtlanException {
        return list();
    }

    /**
     * Retrieves all groups currently defined in Atlan.
     *
     * @param client connectivity to the Atlan tenant from which to list the groups
     * @return the list of groups currently defined in Atlan
     * @throws AtlanException on any error during API invocation
     * @deprecated see {@link #list(AtlanClient)} instead
     */
    @Deprecated
    public static List<AtlanGroup> retrieveAll(AtlanClient client) throws AtlanException {
        return list(client);
    }

    /**
     * Retrieves all groups currently defined in Atlan.
     *
     * @return the list of groups currently defined in Atlan
     * @throws AtlanException on any error during API invocation
     */
    public static List<AtlanGroup> list() throws AtlanException {
        return list(Atlan.getDefaultClient());
    }

    /**
     * Retrieves all groups currently defined in Atlan.
     *
     * @param client connectivity to the Atlan tenant from which to list the groups
     * @return the list of groups currently defined in Atlan
     * @throws AtlanException on any error during API invocation
     */
    public static List<AtlanGroup> list(AtlanClient client) throws AtlanException {
        return client.groups.list();
    }

    /**
     * Retrieves all groups with a name that contains the provided string.
     * (This could include a complete group name, in which case there should be at
     * most a single item in the returned list, or could be a partial group name
     * to retrieve all groups with that naming convention.)
     *
     * @param alias name (as it appears in the UI) on which to filter the groups
     * @return all groups whose name (in the UI) contains the provided string
     * @throws AtlanException on any error during API invocation
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static List<AtlanGroup> retrieveByName(String alias) throws AtlanException {
        return get(alias);
    }

    /**
     * Retrieves all groups with a name that contains the provided string.
     * (This could include a complete group name, in which case there should be at
     * most a single item in the returned list, or could be a partial group name
     * to retrieve all groups with that naming convention.)
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the group
     * @param alias name (as it appears in the UI) on which to filter the groups
     * @return all groups whose name (in the UI) contains the provided string
     * @throws AtlanException on any error during API invocation
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static List<AtlanGroup> retrieveByName(AtlanClient client, String alias) throws AtlanException {
        return get(client, alias);
    }

    /**
     * Retrieves all groups with a name that contains the provided string.
     * (This could include a complete group name, in which case there should be at
     * most a single item in the returned list, or could be a partial group name
     * to retrieve all groups with that naming convention.)
     *
     * @param alias name (as it appears in the UI) on which to filter the groups
     * @return all groups whose name (in the UI) contains the provided string
     * @throws AtlanException on any error during API invocation
     */
    @JsonIgnore
    public static List<AtlanGroup> get(String alias) throws AtlanException {
        return get(Atlan.getDefaultClient(), alias);
    }

    /**
     * Retrieves all groups with a name that contains the provided string.
     * (This could include a complete group name, in which case there should be at
     * most a single item in the returned list, or could be a partial group name
     * to retrieve all groups with that naming convention.)
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the group
     * @param alias name (as it appears in the UI) on which to filter the groups
     * @return all groups whose name (in the UI) contains the provided string
     * @throws AtlanException on any error during API invocation
     */
    @JsonIgnore
    public static List<AtlanGroup> get(AtlanClient client, String alias) throws AtlanException {
        return client.groups.get(alias);
    }

    /**
     * Remove one or more users from this group.
     *
     * @param userIds unique identifiers (GUIDs) of the users to remove from the group
     * @throws AtlanException on any API communication issue
     */
    public void removeUsers(List<String> userIds) throws AtlanException {
        removeUsers(Atlan.getDefaultClient(), userIds);
    }

    /**
     * Remove one or more users from this group.
     *
     * @param client connectivity to the Atlan tenant from which to remove the users from the group
     * @param userIds unique identifiers (GUIDs) of the users to remove from the group
     * @throws AtlanException on any API communication issue
     */
    public void removeUsers(AtlanClient client, List<String> userIds) throws AtlanException {
        if (this.id == null || this.id.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_GROUP_ID);
        }
        client.groups.removeMembers(this.id, userIds);
    }

    /**
     * Fetch the users that belong to this group.
     *
     * @return details of the users that belong to this group
     * @throws AtlanException on any API communication issue
     */
    public UserResponse fetchUsers() throws AtlanException {
        return fetchUsers(Atlan.getDefaultClient());
    }

    /**
     * Fetch the users that belong to this group.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the users belonging to the group
     * @return details of the users that belong to this group
     * @throws AtlanException on any API communication issue
     */
    public UserResponse fetchUsers(AtlanClient client) throws AtlanException {
        if (this.id == null || this.id.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_GROUP_ID);
        }
        return client.groups.listMembers(this.id);
    }

    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class GroupAttributes extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Name of the group as it appears in the UI. */
        List<String> alias;

        /** Time (epoch) at which the group was created, in milliseconds. */
        List<String> createdAt;

        /** User who created the group. */
        List<String> createdBy;

        /** Time (epoch) at which the group was last updated, in milliseconds. */
        List<String> updatedAt;

        /** User who last updated the group. */
        List<String> updatedBy;

        /** Description of the group. */
        List<String> description;

        /** Whether this group should be auto-assigned to all new users (true) or not (false). */
        @Builder.Default
        List<String> isDefault = List.of("false");

        /** Slack channels for this group. */
        List<String> channels;
    }
}
