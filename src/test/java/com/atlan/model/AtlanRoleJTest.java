package com.atlan.model;

import static org.testng.Assert.*;

import com.atlan.model.admin.AtlanRoleJ;
import com.atlan.net.ApiResourceJ;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.annotations.Test;

public class AtlanRoleJTest {

    private static final AtlanRoleJ full = AtlanRoleJ.builder()
            .id("id")
            .description("description")
            .name("name")
            .clientRole(false)
            .level("level")
            .memberCount("memberCount")
            .build();

    private static AtlanRoleJ frodo;
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
        frodo = ApiResourceJ.mapper.readValue(serialized, AtlanRoleJ.class);
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
