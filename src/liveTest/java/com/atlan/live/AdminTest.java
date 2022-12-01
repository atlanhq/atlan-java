/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.api.GroupsEndpoint;
import com.atlan.api.UsersEndpoint;
import com.atlan.cache.RoleCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.testng.annotations.Test;

/**
 * Test management of users and groups.
 */
@Test(groups = {"admin"})
public class AdminTest extends AtlanLiveTest {

    private static final String PREFIX = "AdminTest";
    private static final String GROUP_NAME1 = PREFIX + "1";
    private static final String GROUP_NAME2 = PREFIX + "2";

    private static AtlanGroup group1 = null;
    private static AtlanGroup group2 = null;
    private static AtlanUser user1 = null;
    private static AtlanUser user2 = null;
    private static AtlanUser user3 = null;

    /**
     * Create a new group with a unique name.
     *
     * @param name to make the group unique
     * @return the group that was created
     * @throws AtlanException on any error creating or reading-back the group
     */
    static AtlanGroup createGroup(String name) throws AtlanException {
        AtlanGroup toCreate = AtlanGroup.creator(name).build();
        String guid = toCreate.create();
        List<AtlanGroup> response = AtlanGroup.retrieveByName(name);
        assertNotNull(response);
        assertEquals(response.size(), 1);
        AtlanGroup created = response.get(0);
        assertNotNull(created);
        assertEquals(created.getId(), guid);
        return created;
    }

    /**
     * Delete (purge) a group based on its GUID.
     *
     * @param guid of the group to purge
     * @throws AtlanException on any errors purging the group
     */
    static void deleteGroup(String guid) throws AtlanException {
        AtlanGroup.delete(guid);
    }

    @Test(groups = {"read.roles"})
    void retrieveRoles() throws AtlanException {
        String adminRoleGuid = RoleCache.getIdForName("$admin");
        assertNotNull(adminRoleGuid);
    }

    @Test(groups = {"read.sessions"})
    void retrieveSessions() throws AtlanException {
        UserMinimalResponse response = UsersEndpoint.getCurrentUser();
        assertNotNull(response);
        AtlanUser user = response.toAtlanUser();
        assertNotNull(user);
        assertNotNull(user.getId());
        SessionResponse sessions = user.fetchSessions();
        assertNotNull(sessions);
    }

    @Test(groups = {"create.group.1"})
    void createGroup1() throws AtlanException {
        group1 = createGroup(GROUP_NAME1);
        assertNotNull(group1);
        assertNotNull(group1.getId());
    }

    @Test(
            groups = {"read.group.1"},
            dependsOnGroups = {"create.group.1"})
    void retrieveGroups1() throws AtlanException {
        List<AtlanGroup> groups = AtlanGroup.retrieveAll();
        assertNotNull(groups);
        assertTrue(groups.size() >= 1);
        groups = AtlanGroup.retrieveByName(GROUP_NAME1);
        assertNotNull(groups);
        assertEquals(groups.size(), 1);
        AtlanGroup one = groups.get(0);
        assertNotNull(one);
        assertNotNull(one.getPath());
        assertNotNull(one.getName());
        assertEquals(one.getId(), group1.getId());
        assertNull(one.getAttributes().getDescription());
    }

    @Test(
            groups = {"update.group.1"},
            dependsOnGroups = {"create.group.1"})
    void updateGroups() throws AtlanException {
        AtlanGroup group = AtlanGroup.updater(group1.getId(), group1.getPath())
                .attributes(AtlanGroup.GroupAttributes.builder()
                        .description(List.of("Now with a description!"))
                        .build())
                .build();
        group.update();
    }

    @Test(groups = {"create.users"})
    void createUsers() throws AtlanException {
        AtlanUser user = AtlanUser.creator("guest@example.com", "$guest").build();
        user.create();
        AtlanUser user2 = AtlanUser.creator("user2@example.com", "$guest").build();
        AtlanUser user3 = AtlanUser.creator("user3@example.com", "$guest").build();
        UsersEndpoint.createUsers(List.of(user2, user3));
    }

    @Test(
            groups = {"read.users.1"},
            dependsOnGroups = {"create.users"})
    void retrieveUsers1() throws AtlanException {
        List<AtlanUser> users = AtlanUser.retrieveAll();
        assertNotNull(users);
        assertTrue(users.size() >= 2);
        users = AtlanUser.retrieveByEmail("guest@example.com");
        assertNotNull(users);
        assertEquals(users.size(), 1);
        user1 = users.get(0);
        assertNotNull(user1);
        assertNotNull(user1.getId());
        assertNull(user1.getAttributes().getDesignation());
        assertEquals(user1.getGroupCount().longValue(), 0L);
        users = AtlanUser.retrieveByEmail("@example.com");
        assertNotNull(users);
        assertEquals(users.size(), 3);
        Set<String> ids = users.stream().map(AtlanUser::getId).collect(Collectors.toSet());
        assertEquals(ids.size(), 3);
        for (AtlanUser user : users) {
            if (user.getEmail().equals("user2@example.com")) {
                user2 = user;
                assertNotNull(user2.getId());
            } else if (user.getEmail().equals("user3@example.com")) {
                user3 = user;
                assertNotNull(user3.getId());
            }
        }
    }

    @Test(
            groups = {"create.group.2"},
            dependsOnGroups = {"read.users.1"})
    void createGroups2() throws AtlanException {
        AtlanGroup group = AtlanGroup.creator(GROUP_NAME2).build();
        CreateGroupResponse response = GroupsEndpoint.createGroup(group, List.of(user2.getId(), user3.getId()));
        String groupGuid2 = response.getGroup();
        assertNotNull(groupGuid2);
        Map<String, CreateGroupResponse.UserStatus> statusMap = response.getUsers();
        assertNotNull(statusMap);
        assertTrue(statusMap.containsKey(user2.getId()));
        assertTrue(statusMap.get(user2.getId()).wasSuccessful());
        assertTrue(statusMap.containsKey(user3.getId()));
        assertTrue(statusMap.get(user3.getId()).wasSuccessful());
        List<AtlanGroup> list = AtlanGroup.retrieveByName(GROUP_NAME2);
        assertNotNull(list);
        assertEquals(list.size(), 1);
        group2 = list.get(0);
    }

    @Test(
            groups = {"update.users"},
            dependsOnGroups = {"create.users", "create.group.*"})
    void updateUsers() throws AtlanException {
        AtlanUser user = AtlanUser.updater(user1.getId()).build();
        user.addToGroups(List.of(group1.getId()));
        user.changeRole(RoleCache.getIdForName("$member"));
        // TODO: these won't work before we have a verified user
        /*
            UpdateUserResponse response = user.activate();
            assertNotNull(response);
            assertTrue(response.getEnabled());
            response = user.deactivate();
            assertNotNull(response);
            assertFalse(response.getEnabled());
        */
    }

    @Test(
            groups = {"read.users.2"},
            dependsOnGroups = {"update.users"})
    void retrieveUsers2() throws AtlanException {
        List<AtlanUser> users = AtlanUser.retrieveByEmail("guest@example.com");
        assertNotNull(users);
        assertEquals(users.size(), 1);
        AtlanUser one = users.get(0);
        assertNotNull(one);
        assertEquals(one.getId(), user1.getId());
        assertEquals(one.getGroupCount().longValue(), 1L);
        AtlanUser guest = AtlanUser.retrieveByUsername("guest");
        assertEquals(guest, one);
    }

    @Test(
            groups = {"read.groups.2"},
            dependsOnGroups = {"update.group.*", "update.users"})
    void retrieveGroups2() throws AtlanException {
        List<AtlanGroup> groups = AtlanGroup.retrieveByName(GROUP_NAME1);
        assertNotNull(groups);
        assertEquals(groups.size(), 1);
        AtlanGroup one = groups.get(0);
        assertNotNull(one);
        assertEquals(one.getId(), group1.getId());
        assertEquals(one.getAttributes().getDescription(), List.of("Now with a description!"));
        assertEquals(one.getUserCount().longValue(), 1L);
    }

    @Test(
            groups = {"purge.users"},
            dependsOnGroups = {"create.*", "read.*", "update.*"},
            alwaysRun = true)
    void purgeUsers() throws AtlanException {
        AtlanUser.delete(user1.getId());
        AtlanUser.delete(user2.getId());
        AtlanUser.delete(user3.getId());
    }

    @Test(
            groups = {"purge.groups"},
            dependsOnGroups = {"create.*", "read.*", "update.*"},
            alwaysRun = true)
    void purgeGroups() throws AtlanException {
        deleteGroup(group1.getId());
        deleteGroup(group2.getId());
    }
}
