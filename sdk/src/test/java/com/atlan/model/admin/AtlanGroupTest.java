/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import static org.testng.Assert.*;

import com.atlan.mock.MockAtlanTenant;
import com.atlan.mock.MockTenant;
import java.io.IOException;
import java.util.List;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AtlanGroupTest {

    private final AtlanGroup full = AtlanGroup.builder()
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

    @BeforeClass
    void init() throws InterruptedException {
        MockAtlanTenant.initializeClient();
    }

    @Test
    void serdeCycleAtlanGroup() throws IOException {
        assertNotNull(full, "Unable to build sample instance of AtlanGroup,");
        final int hash = full.hashCode();
        // Builder equivalency
        assertEquals(
                full.toBuilder().build(),
                full,
                "Unable to converting AtlanGroup via builder back to its original state,");
        // Serialization
        final String serialized = full.toJson(MockTenant.client);
        assertNotNull(serialized, "Unable to serialize sample instance of AtlanGroup,");
        assertEquals(full.hashCode(), hash, "Serialization mutated the original value,");
        // Deserialization
        final AtlanGroup frodo = MockTenant.client.readValue(serialized, AtlanGroup.class);
        assertNotNull(frodo, "Unable to reverse-read serialized value back into an instance of AtlanGroup,");
        // Serialization equivalency
        String backAgain = frodo.toJson(MockTenant.client);
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
        // Deserialization equivalency
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
