/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.api.GroupsEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
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

    /**
     * Builds the minimal object necessary to create a group.
     *
     * @param alias name of the group, as it should appear in the UI
     * @return the minimal request necessary to update the group, as a builder
     */
    public static AtlanGroupBuilder<?, ?> creator(String alias) {
        return AtlanGroup.builder().name(generateName(alias)).attributes(GroupAttributes.builder().alias(List.of(alias)).build());
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
        String internal = alias.toLowerCase();
        return internal.replace(" ", "_");
    }

    /** Send this group to Atlan to create the group in Atlan.
     *
     * @return the unique identifier (GUID) of the group that was created, or null if no group was created
     * @throws AtlanException on any error during API invocation
     */
    public String create() throws AtlanException {
        return GroupsEndpoint.createGroup(this);
    }

    /**
     * Send this group to Atlan to update the group in Atlan.
     *
     * @throws AtlanException on any error during API invocation
     */
    public void update() throws AtlanException {
        if (this.id == null || this.id.length() == 0) {
            throw new InvalidRequestException("An id must be provided to update the group.", "id", "ATLAN_JAVA_CLIENT-400-401", 400, null);
        }
        GroupsEndpoint.updateGroup(this.id, this);
    }

    /**
     * Delete a group from Atlan.
     *
     * @param id unique identifier (GUID) of the group to delete
     * @throws AtlanException on any error during API invocation
     */
    public static void delete(String id) throws AtlanException {
        GroupsEndpoint.deleteGroup(id);
    }

    /**
     * Retrieves all groups currently defined in Atlan.
     * @return the list of groups currently defined in Atlan
     * @throws AtlanException on any error during API invocation
     */
    public static List<AtlanGroup> retrieveAll() throws AtlanException {
        GroupResponse response = GroupsEndpoint.getAllGroups();
        if (response != null && response.getRecords() != null) {
            return response.getRecords();
        } else {
            return Collections.emptyList();
        }
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
    public static List<AtlanGroup> retrieveByName(String alias) throws AtlanException {
        GroupResponse response = GroupsEndpoint.getGroups("{\"$and\":[{\"alias\":{\"$ilike\":\"%" + alias + "%\"}}]}");
        if (response != null && response.getRecords() != null) {
            return response.getRecords();
        } else {
            return null;
        }
    }

    @Getter
    @Setter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
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

        /** TBC */
        @Builder.Default
        List<String> isDefault = List.of("false");

        /** Slack channels for this group. */
        List<String> channels;
    }
}
