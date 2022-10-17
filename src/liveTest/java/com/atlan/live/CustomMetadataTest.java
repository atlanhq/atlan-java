/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.cache.CustomMetadataCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.assets.LineageProcess;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.model.core.Entity;
import com.atlan.model.core.EntityMutationResponse;
import com.atlan.model.enums.AtlanCustomAttributePrimitiveType;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.AttributeDefOptions;
import com.atlan.model.typedefs.CustomMetadataDef;
import com.atlan.model.typedefs.CustomMetadataOptions;
import java.time.Instant;
import java.util.*;
import org.testng.annotations.Test;

@Test(groups = {"custom_metadata"})
public class CustomMetadataTest extends AtlanLiveTest {

    public static final String CM_NAME1 = "RACI";
    public static final String CM_NAME2 = "IPR";

    public static final String CM_ATTR_STRING = "string";
    public static final String CM_ATTR_INTEGER = "integer";
    public static final String CM_ATTR_DECIMAL = "decimal";
    public static final String CM_ATTR_BOOLEAN = "boolean";
    public static final String CM_ATTR_DATE = "date";
    // TODO: public static final String CM_ATTR_OPTIONS = "options";
    // TODO: public static final String CM_ATTR_USER = "user";
    // TODO: public static final String CM_ATTR_GROUP = "group";
    public static final String CM_ATTR_URL = "url";
    public static final String CM_ATTR_SQL = "sql";
    public static final String CM_ATTR_EXTRA = "extra";

    public static long removalEpoch;

    @Test(groups = {"create.cm"})
    void createCustomMetadata() {
        try {
            CustomMetadataDef customMetadataDef = CustomMetadataDef.creator(CM_NAME1)
                    .attributeDef(AttributeDef.of(CM_ATTR_STRING, AtlanCustomAttributePrimitiveType.STRING, null, true))
                    .attributeDef(
                            AttributeDef.of(CM_ATTR_INTEGER, AtlanCustomAttributePrimitiveType.INTEGER, null, false))
                    .attributeDef(
                            AttributeDef.of(CM_ATTR_DECIMAL, AtlanCustomAttributePrimitiveType.DECIMAL, null, false))
                    .attributeDef(
                            AttributeDef.of(CM_ATTR_BOOLEAN, AtlanCustomAttributePrimitiveType.BOOLEAN, null, false))
                    .attributeDef(AttributeDef.of(CM_ATTR_DATE, AtlanCustomAttributePrimitiveType.DATE, null, false))
                    .attributeDef(AttributeDef.of(CM_ATTR_URL, AtlanCustomAttributePrimitiveType.URL, null, false))
                    .attributeDef(AttributeDef.of(CM_ATTR_SQL, AtlanCustomAttributePrimitiveType.SQL, null, false))
                    .attributeDef(AttributeDef.of(CM_ATTR_EXTRA, AtlanCustomAttributePrimitiveType.STRING, null, false))
                    .options(CustomMetadataOptions.builder()
                            .emoji("\uD83D\uDC6A")
                            .build())
                    .build();
            CustomMetadataDef response = customMetadataDef.create();
            assertNotNull(response);
            assertEquals(response.getCategory(), AtlanTypeCategory.CUSTOM_METADATA);
            assertNotNull(response.getName());
            assertNotEquals(response.getName(), CM_NAME1);
            assertNotNull(response.getGuid());
            assertEquals(response.getDisplayName(), CM_NAME1);
            List<AttributeDef> attributes = response.getAttributeDefs();
            assertNotNull(attributes);
            assertEquals(attributes.size(), 8);
            AttributeDef one = attributes.get(0);
            assertNotNull(one);
            assertEquals(one.getDisplayName(), CM_ATTR_STRING);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_STRING);
            assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
            assertNotNull(one.getOptions());
            assertTrue(one.getOptions().getMultiValueSelect());
            one = attributes.get(1);
            assertEquals(one.getDisplayName(), CM_ATTR_INTEGER);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_INTEGER);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.INTEGER.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(2);
            assertEquals(one.getDisplayName(), CM_ATTR_DECIMAL);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_DECIMAL);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.DECIMAL.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(3);
            assertEquals(one.getDisplayName(), CM_ATTR_BOOLEAN);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_BOOLEAN);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.BOOLEAN.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(4);
            assertEquals(one.getDisplayName(), CM_ATTR_DATE);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_DATE);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.DATE.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(5);
            assertEquals(one.getDisplayName(), CM_ATTR_URL);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_URL);
            // Note: for custom attribute types, the typeName will remain "string"
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.URL.getValue());
            one = attributes.get(6);
            assertEquals(one.getDisplayName(), CM_ATTR_SQL);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_SQL);
            // Note: for custom attribute types, the typeName will remain "string"
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.SQL.getValue());
            one = attributes.get(7);
            assertNotNull(one);
            assertEquals(one.getDisplayName(), CM_ATTR_EXTRA);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_EXTRA);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());

            customMetadataDef = CustomMetadataDef.creator(CM_NAME2)
                    .attributeDef(AttributeDef.of(CM_ATTR_STRING, AtlanCustomAttributePrimitiveType.STRING, null, true))
                    .attributeDef(
                            AttributeDef.of(CM_ATTR_INTEGER, AtlanCustomAttributePrimitiveType.INTEGER, null, false))
                    .attributeDef(
                            AttributeDef.of(CM_ATTR_DECIMAL, AtlanCustomAttributePrimitiveType.DECIMAL, null, false))
                    .attributeDef(
                            AttributeDef.of(CM_ATTR_BOOLEAN, AtlanCustomAttributePrimitiveType.BOOLEAN, null, false))
                    .attributeDef(AttributeDef.of(CM_ATTR_DATE, AtlanCustomAttributePrimitiveType.DATE, null, false))
                    .attributeDef(AttributeDef.of(CM_ATTR_URL, AtlanCustomAttributePrimitiveType.URL, null, false))
                    .attributeDef(AttributeDef.of(CM_ATTR_SQL, AtlanCustomAttributePrimitiveType.SQL, null, false))
                    .attributeDef(AttributeDef.of(CM_ATTR_EXTRA, AtlanCustomAttributePrimitiveType.STRING, null, false))
                    .options(CustomMetadataOptions.builder().emoji("⚖️").build())
                    .build();
            response = customMetadataDef.create();
            assertNotNull(response);
            assertEquals(response.getCategory(), AtlanTypeCategory.CUSTOM_METADATA);
            assertNotNull(response.getName());
            assertNotEquals(response.getName(), CM_NAME2);
            assertNotNull(response.getGuid());
            assertEquals(response.getDisplayName(), CM_NAME2);
            attributes = response.getAttributeDefs();
            assertNotNull(attributes);
            assertEquals(attributes.size(), 8);
            one = attributes.get(0);
            assertNotNull(one);
            assertEquals(one.getDisplayName(), CM_ATTR_STRING);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_STRING);
            assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
            assertNotNull(one.getOptions());
            assertTrue(one.getOptions().getMultiValueSelect());
            one = attributes.get(1);
            assertEquals(one.getDisplayName(), CM_ATTR_INTEGER);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_INTEGER);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.INTEGER.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(2);
            assertEquals(one.getDisplayName(), CM_ATTR_DECIMAL);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_DECIMAL);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.DECIMAL.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(3);
            assertEquals(one.getDisplayName(), CM_ATTR_BOOLEAN);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_BOOLEAN);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.BOOLEAN.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(4);
            assertEquals(one.getDisplayName(), CM_ATTR_DATE);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_DATE);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.DATE.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(5);
            assertEquals(one.getDisplayName(), CM_ATTR_URL);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_URL);
            // Note: for custom attribute types, the typeName will remain "string"
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.URL.getValue());
            one = attributes.get(6);
            assertEquals(one.getDisplayName(), CM_ATTR_SQL);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_SQL);
            // Note: for custom attribute types, the typeName will remain "string"
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.SQL.getValue());
            one = attributes.get(7);
            assertNotNull(one);
            assertEquals(one.getDisplayName(), CM_ATTR_EXTRA);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_EXTRA);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a new custom metadata structures.");
        }
    }

    @Test(
            groups = {"link.cm.s3object.1"},
            dependsOnGroups = {"create.s3object", "create.cm"})
    void addObjectCM1() {
        try {
            CustomMetadataAttributes cm = CustomMetadataAttributes.builder()
                    .attribute(CM_ATTR_STRING, List.of("one", "two", "three"))
                    .attribute(CM_ATTR_INTEGER, 42)
                    .attribute(CM_ATTR_DECIMAL, 4.2)
                    .attribute(CM_ATTR_BOOLEAN, true)
                    .attribute(CM_ATTR_DATE, 1659308400000L)
                    .attribute(CM_ATTR_URL, "http://www.example.com")
                    .attribute(CM_ATTR_SQL, "SELECT * from SOMEWHERE;")
                    .build();
            S3Object.replaceCustomMetadata(S3AssetTest.s3Object1Guid, CM_NAME1, cm);
            S3Object object = S3Object.retrieveByGuid(S3AssetTest.s3Object1Guid);
            assertNotNull(object);
            assertTrue(object.isComplete());
            Map<String, CustomMetadataAttributes> sets = object.getCustomMetadataSets();
            assertNotNull(sets);
            assertEquals(sets.size(), 1);
            CustomMetadataAttributes attrs = sets.get(CM_NAME1);
            validateAttributes(attrs);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to add custom metadata to S3 object.");
        }
    }

    @Test(
            groups = {"link.cm.s3object.2"},
            dependsOnGroups = {"link.cm.s3object.1"})
    void addObjectCM2() {
        try {
            CustomMetadataAttributes cm = CustomMetadataAttributes.builder()
                    .attribute(CM_ATTR_STRING, List.of("one", "two", "three"))
                    .attribute(CM_ATTR_INTEGER, 42)
                    .attribute(CM_ATTR_DECIMAL, 4.2)
                    .attribute(CM_ATTR_BOOLEAN, true)
                    .attribute(CM_ATTR_DATE, 1659308400000L)
                    .attribute(CM_ATTR_URL, "http://www.example.com")
                    .attribute(CM_ATTR_SQL, "SELECT * from SOMEWHERE;")
                    .build();
            S3Object.replaceCustomMetadata(S3AssetTest.s3Object1Guid, CM_NAME2, cm);
            S3Object object = S3Object.retrieveByGuid(S3AssetTest.s3Object1Guid);
            assertNotNull(object);
            assertTrue(object.isComplete());
            Map<String, CustomMetadataAttributes> sets = object.getCustomMetadataSets();
            assertNotNull(sets);
            assertEquals(sets.size(), 2);
            CustomMetadataAttributes attrs = sets.get(CM_NAME1);
            validateAttributes(attrs);
            attrs = sets.get(CM_NAME2);
            validateAttributes(attrs);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to add custom metadata to S3 object.");
        }
    }

    @Test(
            groups = {"link.cm.s3object.3"},
            dependsOnGroups = {"link.cm.s3object.2"})
    void updateObjectCM1() {
        try {
            CustomMetadataAttributes cm = CustomMetadataAttributes.builder()
                    .attribute(CM_ATTR_BOOLEAN, false)
                    .build();
            S3Object.updateCustomMetadataAttributes(S3AssetTest.s3Object1Guid, CM_NAME1, cm);
            S3Object object = S3Object.retrieveByGuid(S3AssetTest.s3Object1Guid);
            assertNotNull(object);
            assertTrue(object.isComplete());
            Map<String, CustomMetadataAttributes> sets = object.getCustomMetadataSets();
            assertNotNull(sets);
            assertEquals(sets.size(), 2);
            CustomMetadataAttributes attrs = sets.get(CM_NAME1);
            validateAttributes(attrs, false);
            attrs = sets.get(CM_NAME2);
            validateAttributes(attrs);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to add custom metadata to S3 object.");
        }
    }

    @Test(
            groups = {"unlink.cm.s3object.1"},
            dependsOnGroups = {"link.cm.s3object.3"})
    void removeObjectCM1() {
        try {
            S3Object.removeCustomMetadata(S3AssetTest.s3Object1Guid, CM_NAME1);
            S3Object object = S3Object.retrieveByGuid(S3AssetTest.s3Object1Guid);
            assertNotNull(object);
            assertTrue(object.isComplete());
            Map<String, CustomMetadataAttributes> sets = object.getCustomMetadataSets();
            assertNotNull(sets);
            assertEquals(sets.size(), 1);
            CustomMetadataAttributes attrs = sets.get(CM_NAME2);
            validateAttributes(attrs);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove custom metadata from an asset.");
        }
    }

    @Test(
            groups = {"unlink.cm.s3object.2"},
            dependsOnGroups = {"unlink.cm.s3object.1"})
    void removeObjectCM2() {
        try {
            S3Object.removeCustomMetadata(S3AssetTest.s3Object1Guid, CM_NAME2);
            S3Object object = S3Object.retrieveByGuid(S3AssetTest.s3Object1Guid);
            assertNotNull(object);
            assertTrue(object.isComplete());
            Map<String, CustomMetadataAttributes> sets = object.getCustomMetadataSets();
            assertNotNull(sets);
            assertTrue(sets.isEmpty());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove custom metadata from an asset.");
        }
    }

    @Test(
            groups = {"update.cm.attribute.1"},
            dependsOnGroups = {"create.cm"})
    void removeAttribute() {
        try {
            CustomMetadataDef existing = CustomMetadataCache.getCustomMetadataDef(CM_NAME1);
            List<AttributeDef> existingAttrs = existing.getAttributeDefs();
            List<AttributeDef> updatedAttrs = new ArrayList<>();
            for (AttributeDef existingAttr : existingAttrs) {
                if (existingAttr.getDisplayName().equals(CM_ATTR_EXTRA)) {
                    AttributeDefOptions options = existingAttr.getOptions();
                    removalEpoch = Instant.now().toEpochMilli();
                    options.setIsArchived(true);
                    options.setArchivedBy("test-automation");
                    options.setArchivedAt(removalEpoch);
                    existingAttr.setOptions(options);
                    existingAttr.setDisplayName(existingAttr.getDisplayName() + "-archived-" + removalEpoch);
                }
                updatedAttrs.add(existingAttr);
            }
            existing.setAttributeDefs(updatedAttrs);
            CustomMetadataDef updated = existing.update();
            assertNotNull(updated);
            assertEquals(updated.getCategory(), AtlanTypeCategory.CUSTOM_METADATA);
            assertNotNull(updated.getName());
            assertNotEquals(updated.getName(), CM_NAME1);
            assertNotNull(updated.getGuid());
            assertEquals(updated.getDisplayName(), CM_NAME1);
            List<AttributeDef> attributes = updated.getAttributeDefs();
            assertNotNull(attributes);
            assertEquals(attributes.size(), 8);
            AttributeDef one = attributes.get(0);
            assertNotNull(one);
            assertEquals(one.getDisplayName(), CM_ATTR_STRING);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_STRING);
            assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
            assertNotNull(one.getOptions());
            assertTrue(one.getOptions().getMultiValueSelect());
            one = attributes.get(1);
            assertEquals(one.getDisplayName(), CM_ATTR_INTEGER);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_INTEGER);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.INTEGER.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(2);
            assertEquals(one.getDisplayName(), CM_ATTR_DECIMAL);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_DECIMAL);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.DECIMAL.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(3);
            assertEquals(one.getDisplayName(), CM_ATTR_BOOLEAN);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_BOOLEAN);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.BOOLEAN.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(4);
            assertEquals(one.getDisplayName(), CM_ATTR_DATE);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_DATE);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.DATE.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(5);
            assertEquals(one.getDisplayName(), CM_ATTR_URL);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_URL);
            // Note: for custom attribute types, the typeName will remain "string"
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.URL.getValue());
            one = attributes.get(6);
            assertEquals(one.getDisplayName(), CM_ATTR_SQL);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_SQL);
            // Note: for custom attribute types, the typeName will remain "string"
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.SQL.getValue());
            one = attributes.get(7);
            assertNotNull(one);
            assertEquals(one.getDisplayName(), CM_ATTR_EXTRA + "-archived-" + removalEpoch);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_EXTRA);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            assertTrue(one.getOptions().getIsArchived());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove a custom metadata attribute.");
        }
    }

    @Test(
            groups = {"read.cm.structure.1"},
            dependsOnGroups = {"update.cm.attribute.1"})
    void retrieveStructures() {
        try {
            Map<String, List<AttributeDef>> map = CustomMetadataCache.getAllCustomAttributes();
            assertNotNull(map);
            assertEquals(map.size(), 2);
            assertTrue(map.containsKey(CM_NAME1));
            assertTrue(map.containsKey(CM_NAME2));
            AttributeDef extra = validateStructure(map.get(CM_NAME1), 7);
            assertNull(extra);
            extra = validateStructure(map.get(CM_NAME2), 8);
            assertNotNull(extra);
            assertEquals(extra.getDisplayName(), CM_ATTR_EXTRA);
            assertNotEquals(extra.getName(), CM_ATTR_EXTRA);
            assertEquals(extra.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertTrue(extra.getOptions().getCustomApplicableEntityTypes().contains(Database.TYPE_NAME));
            assertFalse(extra.getOptions().getIsArchived());
            map = CustomMetadataCache.getAllCustomAttributes(true);
            assertNotNull(map);
            assertEquals(map.size(), 2);
            assertTrue(map.containsKey(CM_NAME1));
            assertTrue(map.containsKey(CM_NAME2));
            extra = validateStructure(map.get(CM_NAME1), 8);
            assertNotNull(extra);
            assertEquals(extra.getDisplayName(), CM_ATTR_EXTRA + "-archived-" + removalEpoch);
            assertNotEquals(extra.getName(), CM_ATTR_EXTRA);
            assertEquals(extra.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertTrue(extra.getOptions().getCustomApplicableEntityTypes().contains(Database.TYPE_NAME));
            assertTrue(extra.getOptions().getIsArchived());
            extra = validateStructure(map.get(CM_NAME2), 8);
            assertNotNull(extra);
            assertEquals(extra.getDisplayName(), CM_ATTR_EXTRA);
            assertNotEquals(extra.getName(), CM_ATTR_EXTRA);
            assertEquals(extra.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertTrue(extra.getOptions().getCustomApplicableEntityTypes().contains(Database.TYPE_NAME));
            assertFalse(extra.getOptions().getIsArchived());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to retrieve a custom metadata structure.");
        }
    }

    @Test(
            groups = {"update.cm.attribute.2"},
            dependsOnGroups = {"read.cm.structure.1"})
    void recreateAttribute() {
        try {
            CustomMetadataDef existing = CustomMetadataCache.getCustomMetadataDef(CM_NAME1);
            List<AttributeDef> existingAttrs = existing.getAttributeDefs();
            List<AttributeDef> updatedAttrs = new ArrayList<>();
            for (AttributeDef attributeDef : existingAttrs) {
                attributeDef.setIsNew(null);
                updatedAttrs.add(attributeDef);
            }
            updatedAttrs.add(AttributeDef.of(CM_ATTR_EXTRA, AtlanCustomAttributePrimitiveType.STRING, null, false));
            existing.setAttributeDefs(updatedAttrs);
            CustomMetadataDef updated = existing.update();
            assertNotNull(updated);
            assertEquals(updated.getCategory(), AtlanTypeCategory.CUSTOM_METADATA);
            assertNotNull(updated.getName());
            assertNotEquals(updated.getName(), CM_NAME1);
            assertNotNull(updated.getGuid());
            assertEquals(updated.getDisplayName(), CM_NAME1);
            List<AttributeDef> attributes = updated.getAttributeDefs();
            assertNotNull(attributes);
            assertEquals(attributes.size(), 9);
            AttributeDef one = attributes.get(0);
            assertNotNull(one);
            assertEquals(one.getDisplayName(), CM_ATTR_STRING);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_STRING);
            assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
            assertNotNull(one.getOptions());
            assertTrue(one.getOptions().getMultiValueSelect());
            one = attributes.get(1);
            assertEquals(one.getDisplayName(), CM_ATTR_INTEGER);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_INTEGER);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.INTEGER.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(2);
            assertEquals(one.getDisplayName(), CM_ATTR_DECIMAL);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_DECIMAL);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.DECIMAL.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(3);
            assertEquals(one.getDisplayName(), CM_ATTR_BOOLEAN);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_BOOLEAN);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.BOOLEAN.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(4);
            assertEquals(one.getDisplayName(), CM_ATTR_DATE);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_DATE);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.DATE.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(5);
            assertEquals(one.getDisplayName(), CM_ATTR_URL);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_URL);
            // Note: for custom attribute types, the typeName will remain "string"
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.URL.getValue());
            one = attributes.get(6);
            assertEquals(one.getDisplayName(), CM_ATTR_SQL);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_SQL);
            // Note: for custom attribute types, the typeName will remain "string"
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.SQL.getValue());
            // The archived CM_ATTR_EXTRA attribute should still be there
            one = attributes.get(7);
            assertNotNull(one);
            assertEquals(one.getDisplayName(), CM_ATTR_EXTRA + "-archived-" + removalEpoch);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_EXTRA);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            assertTrue(one.getOptions().getIsArchived());
            // We should now have another CM_ATTR_EXTRA attribute, this one not archived
            one = attributes.get(8);
            assertNotNull(one);
            assertEquals(one.getDisplayName(), CM_ATTR_EXTRA);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_EXTRA);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            assertFalse(one.getOptions().getIsArchived());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to recreate a custom metadata attribute.");
        }
    }

    @Test(
            groups = {"read.cm.structure.2"},
            dependsOnGroups = {"update.cm.attribute.2"})
    void retrieveStructureWithoutArchived2() {
        try {
            Map<String, List<AttributeDef>> map = CustomMetadataCache.getAllCustomAttributes();
            assertNotNull(map);
            assertEquals(map.size(), 2);
            assertTrue(map.containsKey(CM_NAME1));
            assertTrue(map.containsKey(CM_NAME2));
            AttributeDef extra = validateStructure(map.get(CM_NAME1), 8);
            assertNotNull(extra);
            assertEquals(extra.getDisplayName(), CM_ATTR_EXTRA);
            assertNotEquals(extra.getName(), CM_ATTR_EXTRA);
            assertEquals(extra.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertTrue(extra.getOptions().getCustomApplicableEntityTypes().contains(Database.TYPE_NAME));
            assertFalse(extra.getOptions().getIsArchived());
            extra = validateStructure(map.get(CM_NAME2), 8);
            assertNotNull(extra);
            assertEquals(extra.getDisplayName(), CM_ATTR_EXTRA);
            assertNotEquals(extra.getName(), CM_ATTR_EXTRA);
            assertEquals(extra.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertTrue(extra.getOptions().getCustomApplicableEntityTypes().contains(Database.TYPE_NAME));
            assertFalse(extra.getOptions().getIsArchived());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to retrieve a custom metadata structure.");
        }
    }

    @Test(
            groups = {"read.cm.structure.3"},
            dependsOnGroups = {"update.cm.attribute.2"})
    void retrieveStructureWithArchived() {
        try {
            Map<String, List<AttributeDef>> map = CustomMetadataCache.getAllCustomAttributes(true);
            assertNotNull(map);
            assertEquals(map.size(), 2);
            assertTrue(map.containsKey(CM_NAME1));
            assertTrue(map.containsKey(CM_NAME2));
            AttributeDef extra = validateStructure(map.get(CM_NAME1), 9);
            assertNotNull(extra);
            assertEquals(extra.getDisplayName(), CM_ATTR_EXTRA);
            assertNotEquals(extra.getName(), CM_ATTR_EXTRA);
            assertEquals(extra.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertTrue(extra.getOptions().getCustomApplicableEntityTypes().contains(Database.TYPE_NAME));
            assertFalse(extra.getOptions().getIsArchived());
            extra = validateStructure(map.get(CM_NAME2), 8);
            assertNotNull(extra);
            assertEquals(extra.getDisplayName(), CM_ATTR_EXTRA);
            assertNotEquals(extra.getName(), CM_ATTR_EXTRA);
            assertEquals(extra.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertTrue(extra.getOptions().getCustomApplicableEntityTypes().contains(Database.TYPE_NAME));
            assertFalse(extra.getOptions().getIsArchived());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove custom metadata from an asset.");
        }
    }

    @Test(
            groups = {"link.cm.term"},
            dependsOnGroups = {"update.cm.attribute.2", "unlink.asset.term.*", "unlink.term.asset"})
    void updateTermCM() {
        try {
            CustomMetadataAttributes cm1 = CustomMetadataAttributes.builder()
                    .attribute(CM_ATTR_STRING, List.of("one", "two", "three"))
                    .attribute(CM_ATTR_INTEGER, 42)
                    .attribute(CM_ATTR_DECIMAL, 4.2)
                    .attribute(CM_ATTR_BOOLEAN, true)
                    .attribute(CM_ATTR_DATE, 1659308400000L)
                    .attribute(CM_ATTR_URL, "http://www.example.com")
                    .attribute(CM_ATTR_SQL, "SELECT * from SOMEWHERE;")
                    .attribute(CM_ATTR_EXTRA, "something extra...")
                    .build();
            GlossaryTerm toUpdate = GlossaryTerm.updater(
                            GlossaryTest.termQame1, GlossaryTest.TERM_NAME1, GlossaryTest.glossaryGuid)
                    .customMetadata(CM_NAME1, cm1)
                    .build();
            EntityMutationResponse response = toUpdate.upsert(false, true);
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof GlossaryTerm);
            GlossaryTerm term = (GlossaryTerm) one;
            assertEquals(term.getQualifiedName(), GlossaryTest.termQame1);
            term = GlossaryTerm.retrieveByQualifiedName(GlossaryTest.termQame1);
            assertNotNull(term);
            assertTrue(term.isComplete());
            assertEquals(term.getQualifiedName(), GlossaryTest.termQame1);
            assertNotNull(term.getCustomMetadataSets());
            cm1 = term.getCustomMetadataSets().get(CM_NAME1);
            validateAttributes(cm1);
            assertEquals(cm1.getAttributes().get(CM_ATTR_EXTRA), "something extra...");
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected error trying to update term with custom metadata.");
        }
    }

    @Test(
            groups = {"unlink.cm.term"},
            dependsOnGroups = {"link.cm.term", "search.term.cm"})
    void removeTermCM() {
        try {
            GlossaryTerm toUpdate = GlossaryTerm.updater(
                            GlossaryTest.termQame1, GlossaryTest.TERM_NAME1, GlossaryTest.glossaryGuid)
                    .build();
            toUpdate.removeCustomMetadata();
            EntityMutationResponse response = toUpdate.upsert(false, true);
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof GlossaryTerm);
            GlossaryTerm term = (GlossaryTerm) one;
            assertEquals(term.getQualifiedName(), GlossaryTest.termQame1);
            term = GlossaryTerm.retrieveByQualifiedName(GlossaryTest.termQame1);
            assertNotNull(term);
            assertTrue(term.isComplete());
            assertEquals(term.getQualifiedName(), GlossaryTest.termQame1);
            assertTrue(term.getCustomMetadataSets().isEmpty());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected error trying to remove custom metadata from term.");
        }
    }

    @Test(
            groups = {"purge.cm"},
            dependsOnGroups = {
                "create.*",
                "read.*",
                "update.*",
                "link.*",
                "unlink.*",
                "search.*",
                "purge.term.*",
                "purge.connection.*",
                "workflow.status.*"
            },
            alwaysRun = true)
    void purgeCustomMetadata() {
        try {
            CustomMetadataDef.purge(CM_NAME1);
            CustomMetadataDef.purge(CM_NAME2);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove custom metadata (structures).");
        }
    }

    private void validateAttributes(CustomMetadataAttributes cma) {
        validateAttributes(cma, true);
    }

    private void validateAttributes(CustomMetadataAttributes cma, boolean value) {
        assertNotNull(cma);
        assertNotNull(cma.getAttributes());
        assertEquals(cma.getAttributes().get(CM_ATTR_STRING), Set.of("one", "two", "three"));
        assertEquals(cma.getAttributes().get(CM_ATTR_INTEGER), 42L);
        assertEquals(cma.getAttributes().get(CM_ATTR_DECIMAL), 4.2);
        assertEquals(cma.getAttributes().get(CM_ATTR_DATE), 1659308400000L);
        assertEquals(cma.getAttributes().get(CM_ATTR_BOOLEAN), value);
        assertEquals(cma.getAttributes().get(CM_ATTR_URL), "http://www.example.com");
        assertEquals(cma.getAttributes().get(CM_ATTR_SQL), "SELECT * from SOMEWHERE;");
    }

    private AttributeDef validateStructure(List<AttributeDef> list, int totalExpected) {
        assertNotNull(list);
        assertEquals(list.size(), totalExpected);
        AttributeDef one = list.get(0);
        assertEquals(one.getDisplayName(), CM_ATTR_STRING);
        assertNotEquals(one.getName(), CM_ATTR_STRING);
        assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
        assertTrue(one.getOptions().getCustomApplicableEntityTypes().contains(Database.TYPE_NAME));
        assertFalse(one.getOptions().getIsArchived());
        one = list.get(1);
        assertEquals(one.getDisplayName(), CM_ATTR_INTEGER);
        assertNotEquals(one.getName(), CM_ATTR_INTEGER);
        assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.INTEGER.getValue());
        assertTrue(one.getOptions().getCustomApplicableEntityTypes().contains(Table.TYPE_NAME));
        assertFalse(one.getOptions().getIsArchived());
        one = list.get(2);
        assertEquals(one.getDisplayName(), CM_ATTR_DECIMAL);
        assertNotEquals(one.getName(), CM_ATTR_DECIMAL);
        assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.DECIMAL.getValue());
        assertTrue(one.getOptions().getCustomApplicableEntityTypes().contains(Column.TYPE_NAME));
        assertFalse(one.getOptions().getIsArchived());
        one = list.get(3);
        assertEquals(one.getDisplayName(), CM_ATTR_BOOLEAN);
        assertNotEquals(one.getName(), CM_ATTR_BOOLEAN);
        assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.BOOLEAN.getValue());
        assertTrue(one.getOptions().getCustomApplicableEntityTypes().contains(MaterializedView.TYPE_NAME));
        assertFalse(one.getOptions().getIsArchived());
        one = list.get(4);
        assertEquals(one.getDisplayName(), CM_ATTR_DATE);
        assertNotEquals(one.getName(), CM_ATTR_DATE);
        assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.DATE.getValue());
        assertTrue(one.getOptions().getCustomApplicableEntityTypes().contains(LineageProcess.TYPE_NAME));
        assertFalse(one.getOptions().getIsArchived());
        one = list.get(5);
        assertEquals(one.getDisplayName(), CM_ATTR_URL);
        assertNotEquals(one.getName(), CM_ATTR_URL);
        assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
        assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.URL.getValue());
        assertTrue(one.getOptions().getCustomApplicableEntityTypes().contains(Glossary.TYPE_NAME));
        assertFalse(one.getOptions().getIsArchived());
        one = list.get(6);
        assertEquals(one.getDisplayName(), CM_ATTR_SQL);
        assertNotEquals(one.getName(), CM_ATTR_SQL);
        assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
        assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.SQL.getValue());
        assertTrue(one.getOptions().getCustomApplicableEntityTypes().contains(GlossaryTerm.TYPE_NAME));
        assertFalse(one.getOptions().getIsArchived());
        if (totalExpected > 8) {
            // If we're expecting more than 8, then the penultimate must be an archived CM_ATTR_EXTRA
            one = list.get(7);
            assertEquals(one.getDisplayName(), CM_ATTR_EXTRA + "-archived-" + removalEpoch);
            assertNotEquals(one.getName(), CM_ATTR_EXTRA);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertTrue(one.getOptions().getCustomApplicableEntityTypes().contains(GlossaryTerm.TYPE_NAME));
            assertTrue(one.getOptions().getIsArchived());
        }
        if (totalExpected > 7) {
            return list.get(totalExpected - 1);
        }
        return null;
    }
}
