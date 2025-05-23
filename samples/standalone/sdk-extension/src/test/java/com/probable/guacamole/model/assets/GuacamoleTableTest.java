/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.probable.guacamole.model.assets;

import static org.testng.Assert.*;

import com.atlan.model.assets.Meaning;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.model.enums.*;
import com.atlan.model.structs.*;
import com.probable.guacamole.MockTenant;
import com.probable.guacamole.model.enums.*;
import com.probable.guacamole.model.structs.*;
import java.io.IOException;
import java.util.*;
import javax.annotation.processing.Generated;
import org.testng.annotations.Test;

@Generated(value = "com.probable.guacamole.generators.POJOGenerator")
@SuppressWarnings("deprecation")
public class GuacamoleTableTest {

    private static final GuacamoleTable full = GuacamoleTable._internal()
            .guid("guid")
            .displayText("displayText")
            .status(AtlanStatus.ACTIVE)
            .createdBy("createdBy")
            .updatedBy("updatedBy")
            .createTime(123456789L)
            .updateTime(123456789L)
            .isIncomplete(false)
            .deleteHandler("SOFT")
            .meaningNames(Set.of("meaningName1", "meaningName2"))
            .meanings(Set.of(
                    Meaning.builder()
                            .termGuid("termGuid1")
                            .relationGuid("relationGuid1")
                            .displayText("displayText1")
                            .confidence(100)
                            .build(),
                    Meaning.builder()
                            .termGuid("termGuid2")
                            .relationGuid("relationGuid2")
                            .displayText("displayText2")
                            .confidence(100)
                            .build()))
            .qualifiedName("qualifiedName")
            .atlanTag(AtlanTag.of("String0"))
            .atlanTag(AtlanTag.builder().typeName("String1").propagate(false).build())
            .customMetadata(
                    "String0",
                    CustomMetadataAttributes.builder()
                            .attribute("String0", 123.456)
                            .attribute("String1", true)
                            .build())
            .customMetadata(
                    "String1",
                    CustomMetadataAttributes.builder()
                            // Note: for equivalency this MUST be a Long (not an Integer), as deserialization
                            // will always produce a Long
                            .attribute("String0", 789L)
                            .attribute("String1", "AnotherString")
                            .build())
            .guacamoleArchived(true)
            .guacamoleColumn(GuacamoleColumn.refByGuid("705d96f4-bdb6-4792-8dfe-8dc4ca3d2c23"))
            .guacamoleColumn(GuacamoleColumn.refByQualifiedName("default/snowflake/1234567890/test/qualifiedName"))
            .guacamoleSize(123456789L)
            .guacamoleTemperature(GuacamoleTemperature.HOT)
            .build();

    private static final int hash = full.hashCode();
    private static GuacamoleTable frodo;
    private static String serialized;

    @Test(groups = {"GuacamoleTable.builderEquivalency"})
    void builderEquivalency() {
        assertEquals(full.toBuilder().build(), full);
    }

    @Test(
            groups = {"GuacamoleTable.serialize"},
            dependsOnGroups = {"GuacamoleTable.builderEquivalency"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson(MockTenant.client);
        assertNotNull(serialized);
        assertEquals(full.hashCode(), hash, "Serialization mutated the original value,");
    }

    @Test(
            groups = {"GuacamoleTable.deserialize"},
            dependsOnGroups = {"GuacamoleTable.serialize"})
    void deserialization() throws IOException {
        assertNotNull(serialized);
        frodo = MockTenant.client.readValue(serialized, GuacamoleTable.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"GuacamoleTable.equivalency"},
            dependsOnGroups = {"GuacamoleTable.serialize", "GuacamoleTable.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson(MockTenant.client);
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"GuacamoleTable.equivalency"},
            dependsOnGroups = {"GuacamoleTable.serialize", "GuacamoleTable.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
