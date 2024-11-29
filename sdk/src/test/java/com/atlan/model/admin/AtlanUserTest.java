/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import static org.testng.Assert.*;

import com.atlan.mock.MockTenant;
import com.atlan.model.enums.AdminOperationType;
import com.atlan.model.enums.AdminResourceType;
import com.atlan.model.enums.KeycloakEventType;
import java.io.IOException;
import java.util.List;
import org.testng.annotations.Test;

public class AtlanUserTest {

    private static final AtlanUser full = AtlanUser.builder()
            .username("username")
            .id("id")
            .workspaceRole("workspaceRole")
            .email("email")
            .emailVerified(false)
            .enabled(false)
            .firstName("firstName")
            .lastName("lastName")
            .attributes(AtlanUser.UserAttributes.builder()
                    .designation(List.of("designation"))
                    .skills(List.of("skills"))
                    .slack(List.of("slack"))
                    .invitedAt(List.of("invitedAt"))
                    .invitedBy(List.of("invitedBy"))
                    .invitedByName(List.of("invitedByName"))
                    .build())
            .createdTimestamp(123456789L)
            .lastLoginTime(234567890L)
            .groupCount(1L)
            .defaultRoles(List.of("role1", "role2"))
            .roles(List.of("role1", "role2"))
            // .decentralizedRoles()
            // .persona()
            // .purpose()
            .adminEvents(List.of(
                    AdminEvent.builder()
                            .operationType(AdminOperationType.CREATE)
                            .realmId("realmId")
                            .representation("representation")
                            .resourcePath("resourcePath")
                            .resourceType(AdminResourceType.REALM_ROLE)
                            .time(12345789L)
                            .authDetails(AuthDetails.builder()
                                    .clientId("clientId")
                                    .ipAddress("ipAddress")
                                    .realmId("realmId")
                                    .userId("userId")
                                    .build())
                            .build(),
                    AdminEvent.builder()
                            .operationType(AdminOperationType.UPDATE)
                            .realmId("realmId")
                            .representation("representation")
                            .resourcePath("resourcePath")
                            .resourceType(AdminResourceType.REALM_ROLE_MAPPING)
                            .time(23457890L)
                            .authDetails(AuthDetails.builder()
                                    .clientId("clientId")
                                    .ipAddress("ipAddress")
                                    .realmId("realmId")
                                    .userId("userId")
                                    .build())
                            .build()))
            .loginEvents(List.of(
                    KeycloakEvent.builder()
                            .clientId("clientId")
                            .ipAddress("ipAddress")
                            .realmId("realmId")
                            .sessionId("sessionId")
                            .time(12345789L)
                            .type(KeycloakEventType.LOGIN)
                            .userId("userId")
                            .build(),
                    KeycloakEvent.builder()
                            .clientId("clientId")
                            .ipAddress("ipAddress")
                            .realmId("realmId")
                            .sessionId("sessionId")
                            .time(23457890L)
                            .type(KeycloakEventType.LOGIN)
                            .userId("userId")
                            .build()))
            .build();

    private static AtlanUser frodo;
    private static String serialized;

    @Test(groups = {"AtlanUser.serialize"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson(MockTenant.client);
        assertNotNull(serialized);
    }

    @Test(
            groups = {"AtlanUser.deserialize"},
            dependsOnGroups = {"AtlanUser.serialize"})
    void deserialization() throws IOException {
        assertNotNull(serialized);
        frodo = MockTenant.client.readValue(serialized, AtlanUser.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"AtlanUser.equivalency"},
            dependsOnGroups = {"AtlanUser.serialize", "AtlanUser.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson(MockTenant.client);
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"AtlanUser.equivalency"},
            dependsOnGroups = {"AtlanUser.serialize", "AtlanUser.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
