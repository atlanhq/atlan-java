/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import static org.testng.Assert.*;

import com.atlan.mock.MockTenant;
import java.io.IOException;
import java.util.List;
import org.testng.annotations.Test;

public class AtlanGroupTest {

    private static final AtlanGroup full = AtlanGroup.builder()
            .alias("alias")
            .attributes(AtlanGroup.GroupAttributes.builder()
                    .alias(List.of("alias"))
                    .createdAt(List.of("123456789"))
                    .createdBy(List.of("createdBy"))
                    .updatedAt(List.of("123456789"))
                    .updatedBy(List.of("createdBy"))
                    .description(List.of("description"))
                    .isDefault(List.of("false"))
                    .build())
            // .decentralizedRoles()
            .id("id")
            .name("name")
            .path("path")
            // .persona()
            // .purpose()
            .userCount(123L)
            .build();

    private static AtlanGroup frodo;
    private static String serialized;

    @Test(groups = {"AtlanGroup.serialize"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson(MockTenant.client);
        assertNotNull(serialized);
    }

    @Test(
            groups = {"AtlanGroup.deserialize"},
            dependsOnGroups = {"AtlanGroup.serialize"})
    void deserialization() throws IOException {
        assertNotNull(serialized);
        frodo = MockTenant.client.readValue(serialized, AtlanGroup.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"AtlanGroup.equivalency"},
            dependsOnGroups = {"AtlanGroup.serialize", "AtlanGroup.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson(MockTenant.client);
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"AtlanGroup.equivalency"},
            dependsOnGroups = {"AtlanGroup.serialize", "AtlanGroup.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
