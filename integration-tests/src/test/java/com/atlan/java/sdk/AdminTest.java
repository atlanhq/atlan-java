/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.testng.annotations.Test;

/**
 * Test management of users and groups.
 */
public class AdminTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("Admin");
    private static final String GROUP_NAME = PREFIX;

    private static final String EMAIL_DOMAIN = "@atlan.com";

    private static final DateTimeFormatter SIMPLE_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final LocalDate NOW = LocalDate.now(ZoneId.of("UTC"));
    private static final LocalDate BEFORE = NOW.minusDays(1);
    private static final String TODAY = SIMPLE_DATE.format(NOW);
    private static final String YESTERDAY = SIMPLE_DATE.format(BEFORE);

    private static AtlanGroup group1 = null;

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
        List<AtlanGroup> response = AtlanGroup.get(name);
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
        String adminRoleGuid = Atlan.getDefaultClient().getRoleCache().getIdForName("$admin");
        assertNotNull(adminRoleGuid);
    }

    @Test(groups = {"admin.read.sessions"})
    void retrieveSessions() throws AtlanException {
        UserMinimalResponse response = Atlan.getDefaultClient().users.getCurrentUser();
        assertNotNull(response);
        AtlanUser user = response.toAtlanUser();
        assertNotNull(user);
        assertNotNull(user.getId());
        SessionResponse sessions = user.fetchSessions();
        assertNotNull(sessions);
    }

    @Test(groups = {"admin.read.groups"})
    void retrieveGroups() throws AtlanException {
        List<AtlanGroup> groups = AtlanGroup.list();
        assertNotNull(groups);
        assertFalse(groups.isEmpty());
        for (AtlanGroup group : groups) {
            if (group.isDefault()) {
                defaultGroupCount++;
            }
        }
    }

    @Test(
            groups = {"admin.read.users.1"},
            dependsOnGroups = {"admin.read.groups"})
    void retrieveUsers1() throws AtlanException {
        List<AtlanUser> users = AtlanUser.list();
        assertNotNull(users);
        assertFalse(users.isEmpty());
        AtlanUser user1 = AtlanUser.getByUsername(FIXED_USER);
        assertNotNull(user1);
        assertNotNull(user1.getId());
        assertEquals(user1.getGroupCount().longValue(), defaultGroupCount);
        users = AtlanUser.getByEmail(EMAIL_DOMAIN);
        assertNotNull(users);
        assertFalse(users.isEmpty());
        String email = user1.getEmail();
        users = AtlanUser.getByEmail(email);
        assertNotNull(users);
        assertEquals(users.size(), 1);
        assertEquals(user1, users.get(0));
    }

    @Test(
            groups = {"admin.create.group.1"},
            dependsOnGroups = {"admin.read.users.1"})
    void createGroup1() throws AtlanException {
        AtlanGroup group = AtlanGroup.creator(GROUP_NAME).build();
        String fixedUserId = AtlanUser.getByUsername(FIXED_USER).getId();
        CreateGroupResponse response = Atlan.getDefaultClient().groups.create(group, List.of(fixedUserId));
        String groupGuid2 = response.getGroup();
        assertNotNull(groupGuid2);
        Map<String, CreateGroupResponse.UserStatus> statusMap = response.getUsers();
        assertNotNull(statusMap);
        assertTrue(statusMap.containsKey(fixedUserId));
        assertTrue(statusMap.get(fixedUserId).wasSuccessful());
        List<AtlanGroup> list = AtlanGroup.get(GROUP_NAME);
        assertNotNull(list);
        assertEquals(list.size(), 1);
        group1 = list.get(0);
        assertNotNull(group1);
        assertNotNull(group1.getId());
    }

    @Test(
            groups = {"admin.read.group.1"},
            dependsOnGroups = {"admin.create.group.1"})
    void retrieveGroups1() throws AtlanException {
        List<AtlanGroup> groups = AtlanGroup.get(GROUP_NAME);
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

    @Test(
            groups = {"admin.read.users.2"},
            dependsOnGroups = {"admin.create.group.*"})
    void retrieveUsers2() throws AtlanException {
        AtlanUser one = AtlanUser.getByUsername(FIXED_USER);
        assertNotNull(one);
        assertEquals(one.getGroupCount().longValue(), 1 + defaultGroupCount);
    }

    @Test(
            groups = {"admin.read.groups.2"},
            dependsOnGroups = {"admin.create.group.*", "admin.update.group.*"})
    void retrieveGroups2() throws AtlanException {
        List<AtlanGroup> groups = AtlanGroup.get(GROUP_NAME);
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
        List<AtlanGroup> groups = AtlanGroup.get(GROUP_NAME);
        String fixedUserId = AtlanUser.getByUsername(FIXED_USER).getId();
        assertNotNull(groups);
        AtlanGroup group = groups.get(0);
        group.removeUsers(List.of(fixedUserId));
        UserResponse response = group.fetchUsers();
        assertNotNull(response);
        assertTrue(response.getRecords() == null || response.getRecords().isEmpty());
    }

    @Test(
            groups = {"admin.read.users.3"},
            dependsOnGroups = {"admin.update.users.2"})
    void retrieveUsers3() throws AtlanException {
        AtlanUser user = AtlanUser.getByUsername(FIXED_USER);
        GroupResponse response = user.fetchGroups();
        assertNotNull(response);
        assertTrue(response.getRecords() == null
                || response.getRecords().isEmpty()
                || response.getRecords().size() == defaultGroupCount);
    }

    @Test(
            groups = {"admin.read.logs"},
            dependsOnGroups = {"admin.read.users.*"})
    void retrieveLogs() throws AtlanException {
        KeycloakEventResponse events = Atlan.getDefaultClient()
                .logs
                .getEvents(KeycloakEventRequest.builder()
                        .dateFrom(YESTERDAY)
                        .dateTo(TODAY)
                        .build());
        List<KeycloakEvent> results = events.stream().limit(1000).collect(Collectors.toList());
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test(
            groups = {"admin.read.logs"},
            dependsOnGroups = {"admin.read.users.*"})
    void retrieveAdminLogs() throws AtlanException {
        AdminEventResponse events = Atlan.getDefaultClient()
                .logs
                .getAdminEvents(AdminEventRequest.builder()
                        .realmId("default")
                        .dateFrom(YESTERDAY)
                        .dateTo(TODAY)
                        .build());
        List<AdminEvent> results = events.stream().limit(1000).collect(Collectors.toList());
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test(
            groups = {"admin.purge.groups"},
            dependsOnGroups = {"admin.create.*", "admin.read.*", "admin.update.*"},
            alwaysRun = true)
    void purgeGroups() throws AtlanException {
        deleteGroup(group1.getId());
    }
}
