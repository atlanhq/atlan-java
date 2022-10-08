/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import static org.testng.Assert.*;

import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.annotations.Test;

import java.util.List;

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
            AtlanUser.AdminEvent.builder()
                .operationType("operationType")
                .realmId("realmId")
                .representation("representation")
                .resourcePath("resourcePath")
                .resourceType("resourceType")
                .time(12345789L)
                .authDetails(AtlanUser.AuthDetails.builder()
                    .clientId("clientId")
                    .ipAddress("ipAddress")
                    .realmId("realmId")
                    .userId("userId")
                    .build())
                .build(),
            AtlanUser.AdminEvent.builder()
                .operationType("operationType")
                .realmId("realmId")
                .representation("representation")
                .resourcePath("resourcePath")
                .resourceType("resourceType")
                .time(23457890L)
                .authDetails(AtlanUser.AuthDetails.builder()
                    .clientId("clientId")
                    .ipAddress("ipAddress")
                    .realmId("realmId")
                    .userId("userId")
                    .build())
                .build()
        ))
        .loginEvents(List.of(
            AtlanUser.LoginEvent.builder()
                .clientId("clientId")
                .ipAddress("ipAddress")
                .realmId("realmId")
                .sessionId("sessionId")
                .time(12345789L)
                .type("type")
                .userId("userId")
                .build(),
            AtlanUser.LoginEvent.builder()
                .clientId("clientId")
                .ipAddress("ipAddress")
                .realmId("realmId")
                .sessionId("sessionId")
                .time(23457890L)
                .type("type")
                .userId("userId")
                .build()
        ))
        .build();

    private static AtlanUser frodo;
    private static String serialized;

    @Test(groups = {"serialize"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
    }

    @Test(
        groups = {"deserialize"},
        dependsOnGroups = {"serialize"})
    void deserialization() throws JsonProcessingException {
        assertNotNull(serialized);
        frodo = Serde.mapper.readValue(serialized, AtlanUser.class);
        assertNotNull(frodo);
    }

    @Test(
        groups = {"equivalency"},
        dependsOnGroups = {"serialize", "deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
        groups = {"equivalency"},
        dependsOnGroups = {"serialize", "deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
