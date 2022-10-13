/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import static org.testng.Assert.*;

import com.atlan.model.enums.AssetSidebarTab;
import com.atlan.model.enums.DataPolicyAction;
import com.atlan.model.enums.GlossaryPolicyAction;
import com.atlan.model.enums.PersonaMetadataPolicyAction;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Set;
import java.util.TreeSet;
import org.testng.annotations.Test;

public class PersonaTest {

    private static final Persona full = Persona.builder()
            .id("id")
            .name("name")
            .displayName("displayName")
            .description("description")
            .group("group1")
            .group("group2")
            .user("user1")
            .user("user2")
            .metadataPolicy(PersonaMetadataPolicy.builder()
                    .name("name")
                    .description("description")
                    .allow(true)
                    .actions(Set.of(PersonaMetadataPolicyAction.values()))
                    .connectionId("connectionId")
                    .asset("asset1")
                    .asset("asset2")
                    .build())
            .dataPolicy(PersonaDataPolicy.builder()
                    .name("name")
                    .description("description")
                    .allow(true)
                    .actions(Set.of(DataPolicyAction.values()))
                    .connectionId("connectionId")
                    .asset("asset1")
                    .asset("asset2")
                    .build())
            .glossaryPolicy(GlossaryPolicy.builder()
                    .name("name")
                    .description("description")
                    .allow(true)
                    .actions(Set.of(GlossaryPolicyAction.values()))
                    .glossaryQualifiedName("glossary1")
                    .glossaryQualifiedName("glossary2")
                    .build())
            .enabled(true)
            .createdAt(123456789L)
            .createdBy("createdBy")
            .updatedAt("updatedAt")
            .updatedBy("updatedBy")
            .type("type")
            .level("level")
            .version("version")
            // .readme()
            // .resources()
            .attributes(Persona.PersonaAttributes.builder()
                    .preferences(Persona.PersonaPreferences.builder()
                            .assetTabsDenyList(new TreeSet<>(Set.of(AssetSidebarTab.LINEAGE, AssetSidebarTab.QUERIES)))
                            .customMetadataDenyList(new TreeSet<>(Set.of("cm1", "cm2")))
                            .build())
                    .build())
            .build();

    private static Persona frodo;
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
        frodo = Serde.mapper.readValue(serialized, Persona.class);
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
