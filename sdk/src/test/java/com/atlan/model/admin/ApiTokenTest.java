/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import static org.testng.Assert.*;

import com.atlan.mock.MockAtlanTenant;
import com.atlan.mock.MockTenant;
import java.io.IOException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ApiTokenTest {

    private final ApiToken full = ApiToken.builder()
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

    @BeforeClass
    void init() throws InterruptedException {
        MockAtlanTenant.initializeClient();
    }

    @Test
    void serdeCycleApiToken() throws IOException {
        assertNotNull(full, "Unable to build sample instance of ApiToken,");
        final int hash = full.hashCode();
        // Builder equivalency
        assertEquals(
                full.toBuilder().build(),
                full,
                "Unable to converting ApiToken via builder back to its original state,");
        // Serialization
        final String serialized = full.toJson(MockTenant.client);
        assertNotNull(serialized, "Unable to serialize sample instance of ApiToken,");
        assertEquals(full.hashCode(), hash, "Serialization mutated the original value,");
        // Deserialization
        final ApiToken frodo = MockTenant.client.readValue(serialized, ApiToken.class);
        assertNotNull(frodo, "Unable to reverse-read serialized value back into an instance of ApiToken,");
        // Serialization equivalency
        String backAgain = frodo.toJson(MockTenant.client);
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
        // Deserialization equivalency
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
