/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import static org.testng.Assert.*;

import com.atlan.mock.MockAtlanTenant;
import com.atlan.mock.MockTenant;
import java.io.IOException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AtlanRoleTest {

    private final AtlanRole full = AtlanRole.builder()
            .id("id")
            .description("description")
            .name("name")
            .clientRole(false)
            .level("level")
            .memberCount("memberCount")
            .build();

    @BeforeClass
    void init() throws InterruptedException {
        MockAtlanTenant.initializeClient();
    }

    @Test
    void serdeCycleAtlanRole() throws IOException {
        assertNotNull(full, "Unable to build sample instance of AtlanRole,");
        final int hash = full.hashCode();
        // Builder equivalency
        assertEquals(
                full.toBuilder().build(),
                full,
                "Unable to converting AtlanRole via builder back to its original state,");
        // Serialization
        final String serialized = full.toJson(MockTenant.client);
        assertNotNull(serialized, "Unable to serialize sample instance of AtlanRole,");
        assertEquals(full.hashCode(), hash, "Serialization mutated the original value,");
        // Deserialization
        final AtlanRole frodo = MockTenant.client.readValue(serialized, AtlanRole.class);
        assertNotNull(frodo, "Unable to reverse-read serialized value back into an instance of AtlanRole,");
        // Serialization equivalency
        String backAgain = frodo.toJson(MockTenant.client);
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
        // Deserialization equivalency
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
