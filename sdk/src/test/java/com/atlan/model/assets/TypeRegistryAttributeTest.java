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
public class TypeRegistryAttributeTest {

    private static final TypeRegistryAttribute full = TypeRegistryAttribute._internal()
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
            .typeRegistryDefinedBy(TypeRegistryStruct.refByGuid("705d96f4-bdb6-4792-8dfe-8dc4ca3d2c23"))
            .typeRegistryEntity(TypeRegistryEntity.refByGuid("705d96f4-bdb6-4792-8dfe-8dc4ca3d2c23"))
            .typeRegistryMultiValued(true)
            .typeRegistryRelationship(
                    TypeRegistryAssociationRelationship.refByGuid("705d96f4-bdb6-4792-8dfe-8dc4ca3d2c23"))
            .typeRegistryStruct(TypeRegistryStruct.refByGuid("705d96f4-bdb6-4792-8dfe-8dc4ca3d2c23"))
            .typeRegistryType(TypeRegistryDataType.STRING)
            .build();

    private static final int hash = full.hashCode();
    private static TypeRegistryAttribute frodo;
    private static String serialized;

    @Test(groups = {"TypeRegistryAttribute.builderEquivalency"})
    void builderEquivalency() {
        assertEquals(full.toBuilder().build(), full);
    }

    @Test(
            groups = {"TypeRegistryAttribute.serialize"},
            dependsOnGroups = {"TypeRegistryAttribute.builderEquivalency"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson(MockAtlanTenant.client);
        assertNotNull(serialized);
        assertEquals(full.hashCode(), hash, "Serialization mutated the original value,");
    }

    @Test(
            groups = {"TypeRegistryAttribute.deserialize"},
            dependsOnGroups = {"TypeRegistryAttribute.serialize"})
    void deserialization() throws IOException {
        assertNotNull(serialized);
        frodo = MockAtlanTenant.client.readValue(serialized, TypeRegistryAttribute.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"TypeRegistryAttribute.equivalency"},
            dependsOnGroups = {"TypeRegistryAttribute.serialize", "TypeRegistryAttribute.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson(MockAtlanTenant.client);
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"TypeRegistryAttribute.equivalency"},
            dependsOnGroups = {"TypeRegistryAttribute.serialize", "TypeRegistryAttribute.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
