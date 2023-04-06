/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import static org.testng.Assert.*;

import com.atlan.model.enums.*;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Set;
import java.util.TreeSet;
import org.testng.annotations.Test;

public class PurposeTest {

    private static final Purpose full = Purpose.builder()
            .id("id")
            .name("name")
            .displayName("displayName")
            .description("description")
            // .tag("one")
            // .tag("two")
            .metadataPolicy(PurposeMetadataPolicy.builder()
                    .name("name")
                    .description("description")
                    .allow(true)
                    .actions(Set.of(PurposeMetadataPolicyAction.values()))
                    .allUsers(false)
                    .user("user1")
                    .user("user2")
                    .group("group1")
                    .group("group2")
                    .build())
            .dataPolicy(PurposeDataPolicy.builder()
                    .name("name")
                    .description("description")
                    .allow(true)
                    .actions(Set.of(DataPolicyAction.values()))
                    .allUsers(true)
                    .type(DataPolicyType.MASKING)
                    .mask(MaskingType.HASH)
                    .build())
            .enabled(true)
            .createdAt(123456789L)
            .createdBy("createdBy")
            .updatedAt(123456789L)
            .updatedBy("updatedBy")
            .level("level")
            .version("version")
            // .readme()
            // .resources()
            .attributes(Purpose.PurposeAttributes.builder()
                    .preferences(Purpose.PurposePreferences.builder()
                            .assetTabsDenyList(new TreeSet<>(Set.of(AssetSidebarTab.LINEAGE, AssetSidebarTab.QUERIES)))
                            .customMetadataDenyList(new TreeSet<>(Set.of("cm1", "cm2")))
                            .build())
                    .build())
            .build();

    private static Purpose frodo;
    private static String serialized;

    @Test(groups = {"Purpose.serialize"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
    }

    @Test(
            groups = {"Purpose.deserialize"},
            dependsOnGroups = {"Purpose.serialize"})
    void deserialization() throws JsonProcessingException {
        assertNotNull(serialized);
        frodo = Serde.mapper.readValue(serialized, Purpose.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"Purpose.equivalency"},
            dependsOnGroups = {"Purpose.serialize", "Purpose.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"Purpose.equivalency"},
            dependsOnGroups = {"Purpose.serialize", "Purpose.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
