/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import static org.testng.Assert.*;

import com.atlan.mock.MockTenant;
import java.io.IOException;
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
                    .persona(ApiToken.ApiTokenPersona.of("id1", "default_persona1", "default/persona1"))
                    .persona(ApiToken.ApiTokenPersona.of("id2", "default_persona2", "default/persona2"))
                    .workspacePermission("abc")
                    .workspacePermission("def")
                    .build())
            .build();

    private static ApiToken frodo;
    private static String serialized;

    @Test(groups = {"ApiToken.serialize"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson(MockTenant.client);
        assertNotNull(serialized);
    }

    @Test(
            groups = {"ApiToken.deserialize"},
            dependsOnGroups = {"ApiToken.serialize"})
    void deserialization() throws IOException {
        assertNotNull(serialized);
        frodo = MockTenant.client.readValue(serialized, ApiToken.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"ApiToken.equivalency"},
            dependsOnGroups = {"ApiToken.serialize", "ApiToken.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson(MockTenant.client);
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"ApiToken.equivalency"},
            dependsOnGroups = {"ApiToken.serialize", "ApiToken.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
