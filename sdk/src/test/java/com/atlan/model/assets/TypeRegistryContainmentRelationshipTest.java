/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.mock.MockAtlanTenant;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.model.enums.*;
import com.atlan.model.structs.*;
import java.io.IOException;
import java.util.*;
import javax.annotation.processing.Generated;
import org.testng.annotations.Test;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@SuppressWarnings("deprecation")
public class TypeRegistryContainmentRelationshipTest {

    private static final TypeRegistryContainmentRelationship full = TypeRegistryContainmentRelationship._internal()
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
            .typeRegistryAttribute(TypeRegistryAttribute.refByGuid("705d96f4-bdb6-4792-8dfe-8dc4ca3d2c23"))
            .typeRegistryAttribute(
                    TypeRegistryAttribute.refByQualifiedName("default/snowflake/1234567890/test/qualifiedName"))
            .typeRegistryNamespace(TypeRegistryNamespace.refByGuid("705d96f4-bdb6-4792-8dfe-8dc4ca3d2c23"))
            .typeRegistryTagPropagation(TypeRegistryPropagationType.NONE)
            .typeRegistryVersion("String0")
            .assignedTerm(GlossaryTerm.refByGuid("705d96f4-bdb6-4792-8dfe-8dc4ca3d2c23"))
            .assignedTerm(GlossaryTerm.refByQualifiedName("default/snowflake/1234567890/test/qualifiedName"))
            .typeRegistryDeprecatedAt(123456789L)
            .typeRegistryDeprecatedBy("String0")
            .typeRegistryDescription("String0")
            .typeRegistryIsDeprecated(true)
            .typeRegistryName("String0")
            .typeRegistryReplacedBy(TypeRegistryEntity.refByGuid("705d96f4-bdb6-4792-8dfe-8dc4ca3d2c23"))
            .typeRegistryReplace(TypeRegistryEntity.refByGuid("705d96f4-bdb6-4792-8dfe-8dc4ca3d2c23"))
            .typeRegistryReplace(
                    TypeRegistryEntity.refByQualifiedName("default/snowflake/1234567890/test/qualifiedName"))
            .typeRegistryStatus(TypeRegistryStatus.DRAFT)
            .userDefRelationshipFrom(Task.refByGuid("705d96f4-bdb6-4792-8dfe-8dc4ca3d2c23"))
            .userDefRelationshipFrom(Task.refByQualifiedName("default/snowflake/1234567890/test/qualifiedName"))
            .userDefRelationshipTo(Task.refByGuid("705d96f4-bdb6-4792-8dfe-8dc4ca3d2c23"))
            .userDefRelationshipTo(Task.refByQualifiedName("default/snowflake/1234567890/test/qualifiedName"))
            .typeRegistryChildren(TypeRegistryEndDef.builder()
                    .typeRegistryType("String0")
                    .typeRegistryReferredToAs("String0")
                    .typeRegistryDescription("String0")
                    .build())
            .typeRegistryParent(TypeRegistryEndDef.builder()
                    .typeRegistryType("String0")
                    .typeRegistryReferredToAs("String0")
                    .typeRegistryDescription("String0")
                    .build())
            .build();

    private static final int hash = full.hashCode();
    private static TypeRegistryContainmentRelationship frodo;
    private static String serialized;

    @Test(groups = {"TypeRegistryContainmentRelationship.builderEquivalency"})
    void builderEquivalency() {
        assertEquals(full.toBuilder().build(), full);
    }

    @Test(
            groups = {"TypeRegistryContainmentRelationship.serialize"},
            dependsOnGroups = {"TypeRegistryContainmentRelationship.builderEquivalency"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson(MockAtlanTenant.client);
        assertNotNull(serialized);
        assertEquals(full.hashCode(), hash, "Serialization mutated the original value,");
    }

    @Test(
            groups = {"TypeRegistryContainmentRelationship.deserialize"},
            dependsOnGroups = {"TypeRegistryContainmentRelationship.serialize"})
    void deserialization() throws IOException {
        assertNotNull(serialized);
        frodo = MockAtlanTenant.client.readValue(serialized, TypeRegistryContainmentRelationship.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"TypeRegistryContainmentRelationship.equivalency"},
            dependsOnGroups = {
                "TypeRegistryContainmentRelationship.serialize",
                "TypeRegistryContainmentRelationship.deserialize"
            })
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson(MockAtlanTenant.client);
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"TypeRegistryContainmentRelationship.equivalency"},
            dependsOnGroups = {
                "TypeRegistryContainmentRelationship.serialize",
                "TypeRegistryContainmentRelationship.deserialize"
            })
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
