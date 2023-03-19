/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import static org.testng.Assert.*;

import com.atlan.model.enums.*;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.annotations.Test;

public class ApiTokenTest {

    private static final ApiToken full = ApiToken.builder()
            .id("id")
            .clientId("clientId")
            .displayName("displayName")
            .attributes(ApiToken.ApiTokenAttributes.builder()
                    .accessTokenLifespan(123456789L)
                    .accessToken("accessToken")
                    .clientId("clientId")
                    .createdAt(123456789L)
                    .createdBy("createdBy")
                    .description("description")
                    .displayName("displayName")
                    .persona(ApiToken.ApiTokenPersona.of("id1", "persona1"))
                    .persona(ApiToken.ApiTokenPersona.of("id2", "persona2"))
                    .purposes("purposes")
                    .workspacePermission("abc")
                    .workspacePermission("def")
                    .build())
            .build();

    private static ApiToken frodo;
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
        frodo = Serde.mapper.readValue(serialized, ApiToken.class);
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
