/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.events;

import static org.testng.Assert.*;

import com.atlan.model.assets.Glossary;
import com.atlan.model.assets.GlossaryTerm;
import com.atlan.model.enums.*;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.annotations.Test;

public class AssetDeleteTest {

    private static final AtlanEvent full = AtlanEvent.builder()
            .msgSourceIP("msgSourceIP")
            .msgCreatedBy("msgCreatedBy")
            .msgCreationTime(123456789L)
            .payload(AssetDeletePayload.builder()
                    .asset(GlossaryTerm.builder()
                            .guid("guid1")
                            .qualifiedName("qualifiedName")
                            .anchor(Glossary.refByGuid("guid2"))
                            .build())
                    .build())
            .build();
    private static AtlanEvent frodo;
    private static String serialized;
    private static final int HASH = full.hashCode();

    @Test(groups = {"AssetDelete.builderEquivalency"})
    void builderEquivalency() {
        assertEquals(full.toBuilder().build(), full);
    }

    @Test(
            groups = {"AssetDelete.serialize"},
            dependsOnGroups = {"AssetDelete.builderEquivalency"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
        assertEquals(full.hashCode(), HASH, "Object is mutated by serialization.");
    }

    @Test(
            groups = {"AssetDelete.deserialize"},
            dependsOnGroups = {"AssetDelete.serialize"})
    void deserialization() throws JsonProcessingException {
        assertNotNull(serialized);
        frodo = Serde.mapper.readValue(serialized, AtlanEvent.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"AssetDelete.equivalency"},
            dependsOnGroups = {"AssetDelete.serialize", "AssetDelete.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"AssetDelete.equivalency"},
            dependsOnGroups = {"AssetDelete.serialize", "AssetDelete.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
