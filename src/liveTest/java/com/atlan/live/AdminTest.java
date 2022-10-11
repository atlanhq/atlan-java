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
import org.testng.annotations.Test;

@Test(groups = {"admin"})
public class AdminTest extends AtlanLiveTest {

    public static final String GROUP_NAME = "JavaClient Group";

    public static String groupGuid = null;
    public static String groupPath = null;
    private static String groupGuid2 = null;

    public static String userGuid = null;
    private static String userGuid2 = null;
    private static String userGuid3 = null;

    @Test(groups = {"read.roles"})
    void retrieveRoles() {
        try {
            String adminRoleGuid = RoleCache.getIdForName("$admin");
            assertNotNull(adminRoleGuid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when retrieving roles.");
        }
    }

    @Test(groups = {"read.sessions"})
    void retrieveSessions() {
        try {
            UserMinimalResponse response = UsersEndpoint.getCurrentUser();
            assertNotNull(response);
            AtlanUser user = response.toAtlanUser();
            assertNotNull(user);
            assertNotNull(user.getId());
            SessionResponse sessions = user.fetchSessions();
            assertNotNull(sessions);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when retrieving sessions.");
        }
    }

    @Test(groups = {"create.groups"})
    void createGroups() {
        try {
            AtlanGroup group = AtlanGroup.creator(GROUP_NAME).build();
            groupGuid = group.create();
            assertNotNull(groupGuid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when creating groups.");
        }
    }

    @Test(
            groups = {"read.groups.1"},
            dependsOnGroups = {"create.groups"})
    void retrieveGroups1() {
        try {
            List<AtlanGroup> groups = AtlanGroup.retrieveAll();
            assertNotNull(groups);
            assertTrue(groups.size() >= 1);
            groups = AtlanGroup.retrieveByName(GROUP_NAME);
            assertNotNull(groups);
            assertEquals(groups.size(), 1);
            AtlanGroup one = groups.get(0);
            assertNotNull(one);
            groupPath = one.getPath();
            assertNotNull(groupPath);
            assertEquals(one.getId(), groupGuid);
            assertNull(one.getAttributes().getDescription());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when retrieving groups.");
        }
    }

    @Test(
            groups = {"update.groups"},
            dependsOnGroups = {"create.groups"})
    void updateGroups() {
        try {
            AtlanGroup group = AtlanGroup.updater(groupGuid, groupPath)
                    .attributes(AtlanGroup.GroupAttributes.builder()
                            .description(List.of("Now with a description!"))
                            .build())
                    .build();
            group.update();
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when updating groups.");
        }
    }

    @Test(groups = {"create.users"})
    void createUsers() {
        try {
            AtlanUser user = AtlanUser.creator("guest@example.com", "$guest").build();
            user.create();
            AtlanUser user2 = AtlanUser.creator("user2@example.com", "$guest").build();
            AtlanUser user3 = AtlanUser.creator("user3@example.com", "$guest").build();
            UsersEndpoint.createUsers(List.of(user2, user3));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when creating users.");
        }
    }

    @Test(
            groups = {"read.users.1"},
            dependsOnGroups = {"create.users"})
    void retrieveUsers1() {
        try {
            List<AtlanUser> users = AtlanUser.retrieveAll();
            assertNotNull(users);
            assertTrue(users.size() >= 2);
            users = AtlanUser.retrieveByEmail("guest@example.com");
            assertNotNull(users);
            assertEquals(users.size(), 1);
            AtlanUser one = users.get(0);
            assertNotNull(one);
            userGuid = one.getId();
            assertNotNull(userGuid);
            assertNull(one.getAttributes().getDesignation());
            assertEquals(one.getGroupCount().longValue(), 0L);
            users = AtlanUser.retrieveByEmail("@example.com");
            assertNotNull(users);
            assertEquals(users.size(), 3);
            for (AtlanUser user : users) {
                if (user.getEmail().equals("user2@example.com")) {
                    userGuid2 = user.getId();
                    assertNotNull(userGuid2);
                } else if (user.getEmail().equals("user3@example.com")) {
                    userGuid3 = user.getId();
                    assertNotNull(userGuid3);
                }
            }
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when retrieving users.");
        }
    }

    @Test(
            groups = {"create.groups.2"},
            dependsOnGroups = {"read.users.1"})
    void createGroups2() {
        try {
            AtlanGroup group = AtlanGroup.creator("JC Group2").build();
            CreateGroupResponse response = GroupsEndpoint.createGroup(group, List.of(userGuid2, userGuid3));
            groupGuid2 = response.getGroup();
            assertNotNull(groupGuid2);
            Map<String, CreateGroupResponse.UserStatus> statusMap = response.getUsers();
            assertNotNull(statusMap);
            assertTrue(statusMap.containsKey(userGuid2));
            assertTrue(statusMap.get(userGuid2).wasSuccessful());
            assertTrue(statusMap.containsKey(userGuid3));
            assertTrue(statusMap.get(userGuid3).wasSuccessful());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when creating groups.");
        }
    }

    @Test(
            groups = {"update.users"},
            dependsOnGroups = {"create.users", "create.groups"})
    void updateUsers() {
        try {
            AtlanUser user = AtlanUser.updater(userGuid).build();
            user.addToGroups(List.of(groupGuid));
            user.changeRole(RoleCache.getIdForName("$member"));
            // TODO: these won't work before we have a verified user
            /*UpdateUserResponse response = user.activate();
            assertNotNull(response);
            assertTrue(response.getEnabled());
            response = user.deactivate();
            assertNotNull(response);
            assertFalse(response.getEnabled());*/
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when updating users.");
        }
    }

    @Test(
            groups = {"read.users.2"},
            dependsOnGroups = {"update.users"})
    void retrieveUsers2() {
        try {
            List<AtlanUser> users = AtlanUser.retrieveByEmail("guest@example.com");
            assertNotNull(users);
            assertEquals(users.size(), 1);
            AtlanUser one = users.get(0);
            assertNotNull(one);
            assertEquals(one.getId(), userGuid);
            assertEquals(one.getGroupCount().longValue(), 1L);
            AtlanUser guest = AtlanUser.retrieveByUsername("guest");
            assertEquals(guest, one);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when retrieving users.");
        }
    }

    @Test(
            groups = {"read.groups.2"},
            dependsOnGroups = {"update.groups", "update.users"})
    void retrieveGroups2() {
        try {
            List<AtlanGroup> groups = AtlanGroup.retrieveByName(GROUP_NAME);
            assertNotNull(groups);
            assertEquals(groups.size(), 1);
            AtlanGroup one = groups.get(0);
            assertNotNull(one);
            assertEquals(one.getId(), groupGuid);
            assertEquals(one.getAttributes().getDescription(), List.of("Now with a description!"));
            assertEquals(one.getUserCount().longValue(), 1L);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when retrieving groups.");
        }
    }

    @Test(
            groups = {"purge.users"},
            // TODO        dependsOnGroups = {"create.*", "update.*", "read.*", "search.*", "link.*", "unlink.*"},
            dependsOnGroups = {"create.*", "update.*", "read.*"},
            alwaysRun = true)
    void purgeUsers() {
        try {
            AtlanUser.delete(userGuid);
            AtlanUser.delete(userGuid2);
            AtlanUser.delete(userGuid3);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when deleting users.");
        }
    }

    @Test(
            groups = {"purge.groups"},
            // TODO        dependsOnGroups = {"create.*", "update.*", "read.*", "search.*", "link.*", "unlink.*"},
            dependsOnGroups = {"create.*", "update.*", "read.*"},
            alwaysRun = true)
    void purgeGroups() {
        try {
            AtlanGroup.delete(groupGuid);
            AtlanGroup.delete(groupGuid2);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when deleting groups.");
        }
    }
}
