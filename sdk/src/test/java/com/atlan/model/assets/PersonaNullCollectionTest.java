/* SPDX-License-Identifier: Apache-2.0
   Copyright 2026 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.mock.MockAtlanTenant;
import java.io.IOException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Regression tests for deserialization NPEs against real tenant data, where a payload contains a
 * value that resolves to {@code null} inside a {@code @Singular SortedSet} attribute. Such a null
 * would otherwise crash at {@link java.util.TreeSet} construction via {@code compareTo(null)},
 * surfacing only as a bare {@link NullPointerException} with an unhelpful reference chain.
 */
public class PersonaNullCollectionTest {

    @BeforeClass
    void init() throws InterruptedException {
        MockAtlanTenant.initializeClient();
    }

    /** The exact shape reported from dsm/fs3: an unrecognized enum value in a deny* SortedSet. */
    @Test
    void deserializesPersonaWithUnrecognizedEnumInSortedSet() throws IOException {
        String json = "{"
                + "\"typeName\":\"Persona\","
                + "\"guid\":\"guid-1\","
                + "\"attributes\":{"
                + "\"name\":\"test-persona\","
                // SOME_UNKNOWN_FILTER -> AssetFilterGroup.fromValue() returns null
                + "\"denyAssetFilters\":[\"terms\",\"SOME_UNKNOWN_FILTER\"],"
                + "\"denyAssetTypes\":[\"Table\",null,\"Column\"],"
                + "\"personaUsers\":[null,\"alice\"]"
                + "}"
                + "}";

        final Persona persona = MockAtlanTenant.client.readValue(json, Persona.class);
        assertNotNull(persona, "Persona with null/unrecognized SortedSet elements should deserialize.");

        // Nulls (incl. the unrecognized enum) are stripped; recognized values survive.
        assertNotNull(persona.getDenyAssetFilters());
        assertEquals(persona.getDenyAssetFilters().size(), 1);
        assertEquals(persona.getDenyAssetTypes().size(), 2);
        assertEquals(persona.getPersonaUsers().size(), 1);
        assertTrue(persona.getPersonaUsers().contains("alice"));
        assertTrue(persona.getDenyAssetTypes().contains("Table"));
        assertTrue(persona.getDenyAssetTypes().contains("Column"));
    }

    /**
     * Root-level Asset collections ({@code meaningNames}, {@code meanings}, {@code pendingTasks})
     * are deserialized straight into a {@link java.util.TreeSet} by Jackson, bypassing the
     * null-stripping in {@code Serde.deserializeList}. A null element here must not crash.
     */
    @Test
    void deserializesAssetWithNullRootLevelSortedSetElements() throws IOException {
        String json = "{"
                + "\"typeName\":\"Persona\","
                + "\"guid\":\"guid-2\","
                + "\"meaningNames\":[\"term-a\",null],"
                + "\"pendingTasks\":[null,\"task-1\"],"
                + "\"attributes\":{\"name\":\"test-persona-2\"}"
                + "}";

        final Persona persona = MockAtlanTenant.client.readValue(json, Persona.class);
        assertNotNull(persona, "Asset with null root-level SortedSet elements should deserialize.");
        assertFalse(persona.getMeaningNames().isEmpty());
        assertFalse(persona.getPendingTasks().isEmpty());
    }
}
