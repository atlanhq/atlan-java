package com.atlan.model;

import static org.testng.Assert.*;

import com.atlan.model.admin.AtlanRole;
import com.atlan.net.ApiResource;
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

    @Test(groups = {"serialize"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
    }

    @Test(
            groups = {"deserialize"},
            dependsOnGroups = {"serialize"})
    void deserialization() {
        assertNotNull(serialized);
        frodo = ApiResource.GSON.fromJson(serialized, AtlanRole.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"equivalency"},
            dependsOnGroups = {"serialize", "deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(serialized, backAgain, "Serialization is not equivalent after serde loop,");
    }

    // TODO: Determine why the deserialized form would differ
    @Test(
            groups = {"equivalency"},
            dependsOnGroups = {"serialize", "deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(full, frodo, "Deserialization is not equivalent after serde loop,");
    }
}
