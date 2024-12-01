/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import static org.testng.Assert.*;

import com.atlan.mock.MockTenant;
import java.io.IOException;
import org.testng.annotations.Test;

public class AtlanRoleTest {

    private static final AtlanRole full = AtlanRole.builder()
            .id("id")
            .description("description")
            .name("name")
            .clientRole(false)
            .level("level")
            .memberCount("memberCount")
            .build();

    private static AtlanRole frodo;
    private static String serialized;

    @Test(groups = {"AtlanRole.serialize"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson(MockTenant.client);
        assertNotNull(serialized);
    }

    @Test(
            groups = {"AtlanRole.deserialize"},
            dependsOnGroups = {"AtlanRole.serialize"})
    void deserialization() throws IOException {
        assertNotNull(serialized);
        frodo = MockTenant.client.readValue(serialized, AtlanRole.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"AtlanRole.equivalency"},
            dependsOnGroups = {"AtlanRole.serialize", "AtlanRole.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson(MockTenant.client);
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"AtlanRole.equivalency"},
            dependsOnGroups = {"AtlanRole.serialize", "AtlanRole.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
