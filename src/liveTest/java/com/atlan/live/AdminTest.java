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
public class AdminTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("Admin");
    private static final String GROUP_NAME1 = PREFIX + "1";
    private static final String GROUP_NAME2 = PREFIX + "2";

    private static final String EMAIL_DOMAIN = "@example.com";
    private static final String USER_EMAIL1 = GROUP_NAME1 + EMAIL_DOMAIN;
    private static final String USER_EMAIL2 = GROUP_NAME2 + EMAIL_DOMAIN;
    private static final String USER_EMAIL3 = PREFIX + "3" + EMAIL_DOMAIN;

    private static AtlanGroup group1 = null;
    private static AtlanGroup group2 = null;
    private static AtlanUser user1 = null;
    private static AtlanUser user2 = null;
    private static AtlanUser user3 = null;

    private static long defaultGroupCount = 0L;

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

    @Test(groups = {"admin.read.roles"})
    void retrieveRoles() throws AtlanException {
        String adminRoleGuid = RoleCache.getIdForName("$admin");
        assertNotNull(adminRoleGuid);
    }

    @Test(groups = {"admin.read.sessions"})
    void retrieveSessions() throws AtlanException {
        UserMinimalResponse response = UsersEndpoint.getCurrentUser();
        assertNotNull(response);
        AtlanUser user = response.toAtlanUser();
        assertNotNull(user);
        assertNotNull(user.getId());
        SessionResponse sessions = user.fetchSessions();
        assertNotNull(sessions);
    }

    @Test(groups = {"admin.create.group.1"})
    void createGroup1() throws AtlanException {
        group1 = createGroup(GROUP_NAME1);
        assertNotNull(group1);
        assertNotNull(group1.getId());
    }

    @Test(
            groups = {"admin.read.group.1"},
            dependsOnGroups = {"admin.create.group.1"})
    void retrieveGroups1() throws AtlanException {
        List<AtlanGroup> groups = AtlanGroup.retrieveAll();
        assertNotNull(groups);
        assertTrue(groups.size() >= 1);
        for (AtlanGroup group : groups) {
            if (group.isDefault()) {
                defaultGroupCount++;
            }
        }
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
            groups = {"admin.update.group.1"},
            dependsOnGroups = {"admin.create.group.1"})
    void updateGroups() throws AtlanException {
        AtlanGroup group = AtlanGroup.updater(group1.getId(), group1.getPath())
                .attributes(AtlanGroup.GroupAttributes.builder()
                        .description(List.of("Now with a description!"))
                        .build())
                .build();
        group.update();
    }

    @Test(groups = {"admin.create.users"})
    void createUsers() throws AtlanException {
        AtlanUser user = AtlanUser.creator(USER_EMAIL1, "$guest").build();
        user.create();
        AtlanUser user2 = AtlanUser.creator(USER_EMAIL2, "$guest").build();
        AtlanUser user3 = AtlanUser.creator(USER_EMAIL3, "$guest").build();
        UsersEndpoint.createUsers(List.of(user2, user3));
    }

    @Test(
            groups = {"admin.read.users.1"},
            dependsOnGroups = {"admin.create.users", "admin.read.group.1"})
    void retrieveUsers1() throws AtlanException {
        List<AtlanUser> users = AtlanUser.retrieveAll();
        assertNotNull(users);
        assertTrue(users.size() >= 2);
        users = AtlanUser.retrieveByEmail(USER_EMAIL1);
        assertNotNull(users);
        assertEquals(users.size(), 1);
        user1 = users.get(0);
        assertNotNull(user1);
        assertNotNull(user1.getId());
        assertNull(user1.getAttributes().getDesignation());
        assertEquals(user1.getGroupCount().longValue(), defaultGroupCount);
        users = AtlanUser.retrieveByEmail(EMAIL_DOMAIN);
        assertNotNull(users);
        assertEquals(users.size(), 3);
        Set<String> ids = users.stream().map(AtlanUser::getId).collect(Collectors.toSet());
        assertEquals(ids.size(), 3);
        for (AtlanUser user : users) {
            if (user.getEmail().equals(USER_EMAIL2.toLowerCase())) {
                user2 = user;
                assertNotNull(user2.getId());
            } else if (user.getEmail().equals(USER_EMAIL3.toLowerCase())) {
                user3 = user;
                assertNotNull(user3.getId());
            }
        }
    }

    @Test(
            groups = {"admin.create.group.2"},
            dependsOnGroups = {"admin.read.users.1"})
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
            groups = {"admin.update.users"},
            dependsOnGroups = {"admin.create.users", "admin.create.group.*"})
    void updateUsers() throws AtlanException {
        AtlanUser user = AtlanUser.updater(user1.getId()).build();
        user.addToGroups(List.of(group1.getId()));
        user.changeRole(RoleCache.getIdForName("$member"));
        GroupResponse response = user.fetchGroups();
        assertNotNull(response);
        assertNotNull(response.getRecords());
        assertEquals(response.getRecords().size(), 1 + defaultGroupCount);
        Set<String> groupIds =
                response.getRecords().stream().map(AtlanGroup::getId).collect(Collectors.toSet());
        assertTrue(groupIds.contains(group1.getId()));
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
            groups = {"admin.read.users.2"},
            dependsOnGroups = {"admin.update.users"})
    void retrieveUsers2() throws AtlanException {
        List<AtlanUser> users = AtlanUser.retrieveByEmail(USER_EMAIL1);
        assertNotNull(users);
        assertEquals(users.size(), 1);
        AtlanUser one = users.get(0);
        assertNotNull(one);
        assertEquals(one.getId(), user1.getId());
        assertEquals(one.getGroupCount().longValue(), 1 + defaultGroupCount);
        AtlanUser guest = AtlanUser.retrieveByUsername(GROUP_NAME1.toLowerCase());
        assertEquals(guest, one);
    }

    @Test(
            groups = {"admin.read.groups.2"},
            dependsOnGroups = {"admin.update.group.*", "admin.update.users"})
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
            groups = {"admin.update.users.2"},
            dependsOnGroups = {"admin.read.groups.2", "admin.read.users.2"})
    void removeUserFromGroup() throws AtlanException {
        List<AtlanGroup> groups = AtlanGroup.retrieveByName(GROUP_NAME1);
        assertNotNull(groups);
        AtlanGroup group = groups.get(0);
        group.removeUsers(List.of(user1.getId()));
        UserResponse response = group.fetchUsers();
        assertNotNull(response);
        assertTrue(response.getRecords() == null || response.getRecords().isEmpty());
    }

    @Test(
            groups = {"admin.read.users.3"},
            dependsOnGroups = {"admin.update.users.2"})
    void retrieveUsers3() throws AtlanException {
        AtlanUser user = AtlanUser.retrieveByUsername(user1.getUsername());
        GroupResponse response = user.fetchGroups();
        assertNotNull(response);
        assertTrue(response.getRecords() == null
                || response.getRecords().isEmpty()
                || response.getRecords().size() == defaultGroupCount);
    }

    @Test(
            groups = {"admin.purge.users"},
            dependsOnGroups = {"admin.create.*", "admin.read.*", "admin.update.*"},
            alwaysRun = true)
    void purgeUsers() throws AtlanException {
        AtlanUser.delete(user1.getId());
        AtlanUser.delete(user2.getId());
        AtlanUser.delete(user3.getId());
    }

    @Test(
            groups = {"admin.purge.groups"},
            dependsOnGroups = {"admin.create.*", "admin.read.*", "admin.update.*"},
            alwaysRun = true)
    void purgeGroups() throws AtlanException {
        deleteGroup(group1.getId());
        deleteGroup(group2.getId());
    }
}
