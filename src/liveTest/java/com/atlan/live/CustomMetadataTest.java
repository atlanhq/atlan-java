/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.cache.CustomMetadataCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
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

    // TODO: This cannot be dynamic because a user must first be verified
    //  before they can be linked — so we must use a hard-coded value for
    //  a username that we know is verified and active in the environment
    public static final String FIXED_USER = "chris";

    public static final String CM_RACI = "RACI";
    public static final String CM_IPR = "IPR";
    public static final String CM_QUALITY = "DQ";

    public static final String CM_ATTR_RACI_RESPONSIBLE = "Responsible"; // user
    public static final String CM_ATTR_RACI_ACCOUNTABLE = "Accountable"; // user
    public static final String CM_ATTR_RACI_CONSULTED = "Consulted"; // group
    public static final String CM_ATTR_RACI_INFORMED = "Informed"; // group
    public static final String CM_ATTR_RACI_EXTRA = "Extra"; // string

    public static final String CM_ATTR_IPR_LICENSE = "License"; // string
    public static final String CM_ATTR_IPR_VERSION = "Version"; // decimal
    public static final String CM_ATTR_IPR_MANDATORY = "Mandatory"; // boolean
    public static final String CM_ATTR_IPR_DATE = "Date"; // date
    public static final String CM_ATTR_IPR_URL = "URL"; // url

    public static final String CM_ATTR_QUALITY_COUNT = "Count"; // integer
    public static final String CM_ATTR_QUALITY_SQL = "SQL"; // sql
    // TODO: public static final String CM_ATTR_QUALITY_TYPE = "Type"; // options

    public static long removalEpoch;

    @Test(groups = {"create.cm"})
    void createCustomMetadata() {
        try {
            CustomMetadataDef customMetadataDef = CustomMetadataDef.creator(CM_IPR)
                    .attributeDef(
                            AttributeDef.of(CM_ATTR_IPR_LICENSE, AtlanCustomAttributePrimitiveType.STRING, null, false))
                    .attributeDef(AttributeDef.of(
                            CM_ATTR_IPR_VERSION, AtlanCustomAttributePrimitiveType.DECIMAL, null, false))
                    .attributeDef(AttributeDef.of(
                            CM_ATTR_IPR_MANDATORY, AtlanCustomAttributePrimitiveType.BOOLEAN, null, false))
                    .attributeDef(
                            AttributeDef.of(CM_ATTR_IPR_DATE, AtlanCustomAttributePrimitiveType.DATE, null, false))
                    .attributeDef(AttributeDef.of(CM_ATTR_IPR_URL, AtlanCustomAttributePrimitiveType.URL, null, false))
                    .options(CustomMetadataOptions.builder().emoji("⚖️").build())
                    .build();
            CustomMetadataDef response = customMetadataDef.create();
            assertNotNull(response);
            assertEquals(response.getCategory(), AtlanTypeCategory.CUSTOM_METADATA);
            assertNotNull(response.getName());
            assertNotEquals(response.getName(), CM_IPR);
            assertNotNull(response.getGuid());
            assertEquals(response.getDisplayName(), CM_IPR);
            List<AttributeDef> attributes = response.getAttributeDefs();
            assertNotNull(attributes);
            assertEquals(attributes.size(), 5);
            AttributeDef one = attributes.get(0);
            assertNotNull(one);
            assertEquals(one.getDisplayName(), CM_ATTR_IPR_LICENSE);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_IPR_LICENSE);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(1);
            assertEquals(one.getDisplayName(), CM_ATTR_IPR_VERSION);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_IPR_VERSION);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.DECIMAL.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(2);
            assertEquals(one.getDisplayName(), CM_ATTR_IPR_MANDATORY);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_IPR_MANDATORY);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.BOOLEAN.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(3);
            assertEquals(one.getDisplayName(), CM_ATTR_IPR_DATE);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_IPR_DATE);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.DATE.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(4);
            assertEquals(one.getDisplayName(), CM_ATTR_IPR_URL);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_IPR_URL);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());

            customMetadataDef = CustomMetadataDef.creator(CM_RACI)
                    .attributeDef(AttributeDef.of(
                            CM_ATTR_RACI_RESPONSIBLE, AtlanCustomAttributePrimitiveType.USERS, null, true))
                    .attributeDef(AttributeDef.of(
                            CM_ATTR_RACI_ACCOUNTABLE, AtlanCustomAttributePrimitiveType.USERS, null, false))
                    .attributeDef(AttributeDef.of(
                            CM_ATTR_RACI_CONSULTED, AtlanCustomAttributePrimitiveType.GROUPS, null, true))
                    .attributeDef(AttributeDef.of(
                            CM_ATTR_RACI_INFORMED, AtlanCustomAttributePrimitiveType.GROUPS, null, true))
                    .attributeDef(
                            AttributeDef.of(CM_ATTR_RACI_EXTRA, AtlanCustomAttributePrimitiveType.STRING, null, false))
                    .options(CustomMetadataOptions.builder()
                            .emoji("\uD83D\uDC6A")
                            .build())
                    .build();
            response = customMetadataDef.create();
            assertNotNull(response);
            assertEquals(response.getCategory(), AtlanTypeCategory.CUSTOM_METADATA);
            assertNotNull(response.getName());
            assertNotEquals(response.getName(), CM_RACI);
            assertNotNull(response.getGuid());
            assertEquals(response.getDisplayName(), CM_RACI);
            attributes = response.getAttributeDefs();
            assertNotNull(attributes);
            assertEquals(attributes.size(), 5);
            one = attributes.get(0);
            assertNotNull(one);
            assertEquals(one.getDisplayName(), CM_ATTR_RACI_RESPONSIBLE);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_RACI_RESPONSIBLE);
            assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
            assertNotNull(one.getOptions());
            assertTrue(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.USERS.getValue());
            one = attributes.get(1);
            assertEquals(one.getDisplayName(), CM_ATTR_RACI_ACCOUNTABLE);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_RACI_ACCOUNTABLE);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.USERS.getValue());
            one = attributes.get(2);
            assertEquals(one.getDisplayName(), CM_ATTR_RACI_CONSULTED);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_RACI_CONSULTED);
            assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
            assertNotNull(one.getOptions());
            assertTrue(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.GROUPS.getValue());
            one = attributes.get(3);
            assertEquals(one.getDisplayName(), CM_ATTR_RACI_INFORMED);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_RACI_INFORMED);
            assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
            assertNotNull(one.getOptions());
            assertTrue(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.GROUPS.getValue());
            one = attributes.get(4);
            assertEquals(one.getDisplayName(), CM_ATTR_RACI_EXTRA);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_RACI_EXTRA);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());

            customMetadataDef = CustomMetadataDef.creator(CM_QUALITY)
                    .attributeDef(AttributeDef.of(
                            CM_ATTR_QUALITY_COUNT, AtlanCustomAttributePrimitiveType.INTEGER, null, false))
                    .attributeDef(
                            AttributeDef.of(CM_ATTR_QUALITY_SQL, AtlanCustomAttributePrimitiveType.SQL, null, false))
                    .options(CustomMetadataOptions.builder()
                            .emoji("\uD83D\uDD16")
                            .build())
                    .build();
            response = customMetadataDef.create();
            assertNotNull(response);
            assertEquals(response.getCategory(), AtlanTypeCategory.CUSTOM_METADATA);
            assertNotNull(response.getName());
            assertNotEquals(response.getName(), CM_QUALITY);
            assertNotNull(response.getGuid());
            assertEquals(response.getDisplayName(), CM_QUALITY);
            attributes = response.getAttributeDefs();
            assertNotNull(attributes);
            assertEquals(attributes.size(), 2);
            one = attributes.get(0);
            assertNotNull(one);
            assertEquals(one.getDisplayName(), CM_ATTR_QUALITY_COUNT);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_QUALITY_COUNT);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.INTEGER.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            one = attributes.get(1);
            assertEquals(one.getDisplayName(), CM_ATTR_QUALITY_SQL);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_QUALITY_SQL);
            // Note: for custom attribute types, the typeName will remain "string"
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.SQL.getValue());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a new custom metadata structures.");
        }
    }

    @Test(
            groups = {"link.cm.s3object.raci"},
            dependsOnGroups = {"create.s3object", "create.cm", "create.groups"})
    void addObjectCMRACI() {
        try {
            CustomMetadataAttributes cm = CustomMetadataAttributes.builder()
                    .attribute(CM_ATTR_RACI_RESPONSIBLE, List.of(FIXED_USER))
                    .attribute(CM_ATTR_RACI_ACCOUNTABLE, FIXED_USER)
                    .attribute(CM_ATTR_RACI_CONSULTED, List.of(AdminTest.groupName))
                    .attribute(CM_ATTR_RACI_INFORMED, List.of(AdminTest.groupName))
                    .build();
            S3Object.replaceCustomMetadata(S3AssetTest.s3Object1Guid, CM_RACI, cm);
            S3Object object = S3Object.retrieveByGuid(S3AssetTest.s3Object1Guid);
            assertNotNull(object);
            assertTrue(object.isComplete());
            Map<String, CustomMetadataAttributes> sets = object.getCustomMetadataSets();
            assertNotNull(sets);
            assertEquals(sets.size(), 1);
            CustomMetadataAttributes attrs = sets.get(CM_RACI);
            validateRACIAttributes(attrs);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to add RACI custom metadata to S3 object.");
        }
    }

    @Test(
            groups = {"link.cm.s3object.ipr"},
            dependsOnGroups = {"link.cm.s3object.raci"})
    void addObjectCMIPR() {
        try {
            CustomMetadataAttributes cm = CustomMetadataAttributes.builder()
                    .attribute(CM_ATTR_IPR_LICENSE, "CC BY")
                    .attribute(CM_ATTR_IPR_VERSION, 2.0)
                    .attribute(CM_ATTR_IPR_MANDATORY, true)
                    .attribute(CM_ATTR_IPR_DATE, 1659308400000L)
                    .attribute(CM_ATTR_IPR_URL, "https://creativecommons.org/licenses/by/2.0/")
                    .build();
            S3Object.replaceCustomMetadata(S3AssetTest.s3Object1Guid, CM_IPR, cm);
            S3Object object = S3Object.retrieveByGuid(S3AssetTest.s3Object1Guid);
            assertNotNull(object);
            assertTrue(object.isComplete());
            Map<String, CustomMetadataAttributes> sets = object.getCustomMetadataSets();
            assertNotNull(sets);
            assertEquals(sets.size(), 2);
            CustomMetadataAttributes attrs = sets.get(CM_RACI);
            validateRACIAttributes(attrs);
            attrs = sets.get(CM_IPR);
            validateIPRAttributes(attrs);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to add IPR custom metadata to S3 object.");
        }
    }

    @Test(
            groups = {"link.cm.s3object.dq"},
            dependsOnGroups = {"link.cm.s3object.ipr"})
    void addObjectCMDQ() {
        try {
            CustomMetadataAttributes cm = CustomMetadataAttributes.builder()
                    .attribute(CM_ATTR_QUALITY_COUNT, 42)
                    .attribute(CM_ATTR_QUALITY_SQL, "SELECT * from SOMEWHERE;")
                    .build();
            S3Object.updateCustomMetadataAttributes(S3AssetTest.s3Object1Guid, CM_QUALITY, cm);
            S3Object object = S3Object.retrieveByGuid(S3AssetTest.s3Object1Guid);
            assertNotNull(object);
            assertTrue(object.isComplete());
            Map<String, CustomMetadataAttributes> sets = object.getCustomMetadataSets();
            assertNotNull(sets);
            assertEquals(sets.size(), 3);
            CustomMetadataAttributes attrs = sets.get(CM_RACI);
            validateRACIAttributes(attrs);
            attrs = sets.get(CM_IPR);
            validateIPRAttributes(attrs);
            attrs = sets.get(CM_QUALITY);
            validateDQAttributes(attrs);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to add DQ custom metadata to S3 object.");
        }
    }

    @Test(
            groups = {"link.cm.s3object.ipr.update"},
            dependsOnGroups = {"link.cm.s3object.dq"})
    void updateObjectCMIPR() {
        try {
            CustomMetadataAttributes cm = CustomMetadataAttributes.builder()
                    .attribute(CM_ATTR_IPR_MANDATORY, false)
                    .build();
            S3Object.updateCustomMetadataAttributes(S3AssetTest.s3Object1Guid, CM_IPR, cm);
            S3Object object = S3Object.retrieveByGuid(S3AssetTest.s3Object1Guid);
            assertNotNull(object);
            assertTrue(object.isComplete());
            Map<String, CustomMetadataAttributes> sets = object.getCustomMetadataSets();
            assertNotNull(sets);
            assertEquals(sets.size(), 3);
            CustomMetadataAttributes attrs = sets.get(CM_IPR);
            validateIPRAttributes(attrs, false);
            attrs = sets.get(CM_RACI);
            validateRACIAttributes(attrs);
            attrs = sets.get(CM_QUALITY);
            validateDQAttributes(attrs);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to update IPR custom metadata on S3 object.");
        }
    }

    @Test(
            groups = {"unlink.cm.s3object.raci"},
            dependsOnGroups = {"link.cm.s3object.ipr.update"})
    void removeObjectCMRACI() {
        try {
            S3Object.removeCustomMetadata(S3AssetTest.s3Object1Guid, CM_RACI);
            S3Object object = S3Object.retrieveByGuid(S3AssetTest.s3Object1Guid);
            assertNotNull(object);
            assertTrue(object.isComplete());
            Map<String, CustomMetadataAttributes> sets = object.getCustomMetadataSets();
            assertNotNull(sets);
            assertEquals(sets.size(), 2);
            CustomMetadataAttributes attrs = sets.get(CM_IPR);
            validateIPRAttributes(attrs, false);
            attrs = sets.get(CM_QUALITY);
            validateDQAttributes(attrs);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove RACI custom metadata from an asset.");
        }
    }

    @Test(
            groups = {"unlink.cm.s3object.ipr"},
            dependsOnGroups = {"unlink.cm.s3object.raci"})
    void removeObjectCMIPR() {
        try {
            S3Object.removeCustomMetadata(S3AssetTest.s3Object1Guid, CM_IPR);
            S3Object object = S3Object.retrieveByGuid(S3AssetTest.s3Object1Guid);
            assertNotNull(object);
            assertTrue(object.isComplete());
            Map<String, CustomMetadataAttributes> sets = object.getCustomMetadataSets();
            assertNotNull(sets);
            assertEquals(sets.size(), 1);
            CustomMetadataAttributes attrs = sets.get(CM_QUALITY);
            validateDQAttributes(attrs);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove IPR custom metadata from an asset.");
        }
    }

    @Test(
            groups = {"unlink.cm.s3object.dq"},
            dependsOnGroups = {"unlink.cm.s3object.ipr"})
    void removeObjectCMDQ() {
        try {
            S3Object.removeCustomMetadata(S3AssetTest.s3Object1Guid, CM_QUALITY);
            S3Object object = S3Object.retrieveByGuid(S3AssetTest.s3Object1Guid);
            assertNotNull(object);
            assertTrue(object.isComplete());
            Map<String, CustomMetadataAttributes> sets = object.getCustomMetadataSets();
            assertNotNull(sets);
            assertTrue(sets.isEmpty());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove DQ custom metadata from an asset.");
        }
    }

    @Test(
            groups = {"update.cm.attribute.1"},
            dependsOnGroups = {"create.cm"})
    void removeAttribute() {
        try {
            CustomMetadataDef existing = CustomMetadataCache.getCustomMetadataDef(CM_RACI);
            List<AttributeDef> existingAttrs = existing.getAttributeDefs();
            List<AttributeDef> updatedAttrs = new ArrayList<>();
            for (AttributeDef existingAttr : existingAttrs) {
                if (existingAttr.getDisplayName().equals(CM_ATTR_RACI_EXTRA)) {
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
            assertNotEquals(updated.getName(), CM_RACI);
            assertNotNull(updated.getGuid());
            assertEquals(updated.getDisplayName(), CM_RACI);
            List<AttributeDef> attributes = updated.getAttributeDefs();
            assertNotNull(attributes);
            assertEquals(attributes.size(), 5);
            AttributeDef one = attributes.get(0);
            assertNotNull(one);
            assertEquals(one.getDisplayName(), CM_ATTR_RACI_RESPONSIBLE);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_RACI_RESPONSIBLE);
            assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
            assertNotNull(one.getOptions());
            assertTrue(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.USERS.getValue());
            one = attributes.get(1);
            assertEquals(one.getDisplayName(), CM_ATTR_RACI_ACCOUNTABLE);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_RACI_ACCOUNTABLE);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.USERS.getValue());
            one = attributes.get(2);
            assertEquals(one.getDisplayName(), CM_ATTR_RACI_CONSULTED);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_RACI_CONSULTED);
            assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
            assertNotNull(one.getOptions());
            assertTrue(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.GROUPS.getValue());
            one = attributes.get(3);
            assertEquals(one.getDisplayName(), CM_ATTR_RACI_INFORMED);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_RACI_INFORMED);
            assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
            assertNotNull(one.getOptions());
            assertTrue(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.GROUPS.getValue());
            one = attributes.get(4);
            assertNotNull(one);
            assertEquals(one.getDisplayName(), CM_ATTR_RACI_EXTRA + "-archived-" + removalEpoch);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_RACI_EXTRA);
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
            assertTrue(map.size() >= 3);
            assertTrue(map.containsKey(CM_RACI));
            assertTrue(map.containsKey(CM_IPR));
            assertTrue(map.containsKey(CM_QUALITY));
            AttributeDef extra = validateRACIStructure(map.get(CM_RACI), 4);
            assertNull(extra);
            map = CustomMetadataCache.getAllCustomAttributes(true);
            assertNotNull(map);
            assertTrue(map.size() >= 3);
            assertTrue(map.containsKey(CM_RACI));
            assertTrue(map.containsKey(CM_IPR));
            assertTrue(map.containsKey(CM_QUALITY));
            extra = validateRACIStructure(map.get(CM_RACI), 5);
            assertNotNull(extra);
            assertEquals(extra.getDisplayName(), CM_ATTR_RACI_EXTRA + "-archived-" + removalEpoch);
            assertNotEquals(extra.getName(), CM_ATTR_RACI_EXTRA);
            assertEquals(extra.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertTrue(extra.getOptions().getCustomApplicableEntityTypes().contains(Database.TYPE_NAME));
            assertTrue(extra.getOptions().getIsArchived());
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
            CustomMetadataDef existing = CustomMetadataCache.getCustomMetadataDef(CM_RACI);
            List<AttributeDef> existingAttrs = existing.getAttributeDefs();
            List<AttributeDef> updatedAttrs = new ArrayList<>();
            for (AttributeDef attributeDef : existingAttrs) {
                attributeDef.setIsNew(null);
                updatedAttrs.add(attributeDef);
            }
            updatedAttrs.add(
                    AttributeDef.of(CM_ATTR_RACI_EXTRA, AtlanCustomAttributePrimitiveType.STRING, null, false));
            existing.setAttributeDefs(updatedAttrs);
            CustomMetadataDef updated = existing.update();
            assertNotNull(updated);
            assertEquals(updated.getCategory(), AtlanTypeCategory.CUSTOM_METADATA);
            assertNotNull(updated.getName());
            assertNotEquals(updated.getName(), CM_RACI);
            assertNotNull(updated.getGuid());
            assertEquals(updated.getDisplayName(), CM_RACI);
            List<AttributeDef> attributes = updated.getAttributeDefs();
            assertNotNull(attributes);
            assertEquals(attributes.size(), 6);
            AttributeDef one = attributes.get(0);
            assertNotNull(one);
            assertEquals(one.getDisplayName(), CM_ATTR_RACI_RESPONSIBLE);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_RACI_RESPONSIBLE);
            assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
            assertNotNull(one.getOptions());
            assertTrue(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.USERS.getValue());
            one = attributes.get(1);
            assertEquals(one.getDisplayName(), CM_ATTR_RACI_ACCOUNTABLE);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_RACI_ACCOUNTABLE);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.USERS.getValue());
            one = attributes.get(2);
            assertEquals(one.getDisplayName(), CM_ATTR_RACI_CONSULTED);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_RACI_CONSULTED);
            assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
            assertNotNull(one.getOptions());
            assertTrue(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.GROUPS.getValue());
            one = attributes.get(3);
            assertEquals(one.getDisplayName(), CM_ATTR_RACI_INFORMED);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_RACI_INFORMED);
            assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
            assertNotNull(one.getOptions());
            assertTrue(one.getOptions().getMultiValueSelect());
            assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.GROUPS.getValue());
            one = attributes.get(4);
            assertNotNull(one);
            assertEquals(one.getDisplayName(), CM_ATTR_RACI_EXTRA + "-archived-" + removalEpoch);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_RACI_EXTRA);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertNotNull(one.getOptions());
            assertFalse(one.getOptions().getMultiValueSelect());
            assertTrue(one.getOptions().getIsArchived());
            // We should now have another CM_ATTR_EXTRA attribute, this one not archived
            one = attributes.get(5);
            assertNotNull(one);
            assertEquals(one.getDisplayName(), CM_ATTR_RACI_EXTRA);
            assertNotNull(one.getName());
            assertNotEquals(one.getName(), CM_ATTR_RACI_EXTRA);
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
            assertTrue(map.size() >= 3);
            assertTrue(map.containsKey(CM_RACI));
            assertTrue(map.containsKey(CM_IPR));
            assertTrue(map.containsKey(CM_QUALITY));
            AttributeDef extra = validateRACIStructure(map.get(CM_RACI), 5);
            assertNotNull(extra);
            assertEquals(extra.getDisplayName(), CM_ATTR_RACI_EXTRA);
            assertNotEquals(extra.getName(), CM_ATTR_RACI_EXTRA);
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
            assertTrue(map.size() >= 3);
            assertTrue(map.containsKey(CM_RACI));
            assertTrue(map.containsKey(CM_IPR));
            assertTrue(map.containsKey(CM_QUALITY));
            AttributeDef extra = validateRACIStructure(map.get(CM_RACI), 6);
            assertNotNull(extra);
            assertEquals(extra.getDisplayName(), CM_ATTR_RACI_EXTRA);
            assertNotEquals(extra.getName(), CM_ATTR_RACI_EXTRA);
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
            dependsOnGroups = {"update.cm.attribute.2", "unlink.asset.term.*"})
    void updateTermCM() {
        try {
            CustomMetadataAttributes cm1 = CustomMetadataAttributes.builder()
                    .attribute(CM_ATTR_RACI_RESPONSIBLE, List.of(FIXED_USER))
                    .attribute(CM_ATTR_RACI_ACCOUNTABLE, FIXED_USER)
                    .attribute(CM_ATTR_RACI_CONSULTED, List.of(AdminTest.groupName))
                    .attribute(CM_ATTR_RACI_INFORMED, List.of(AdminTest.groupName))
                    .attribute(CM_ATTR_RACI_EXTRA, "something extra...")
                    .build();
            GlossaryTerm toUpdate = GlossaryTerm.updater(
                            GlossaryTest.termQame1, GlossaryTest.TERM_NAME1, GlossaryTest.glossaryGuid)
                    .customMetadata(CM_RACI, cm1)
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
            cm1 = term.getCustomMetadataSets().get(CM_RACI);
            validateRACIAttributes(cm1);
            assertEquals(cm1.getAttributes().get(CM_ATTR_RACI_EXTRA), "something extra...");
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
            CustomMetadataDef.purge(CM_RACI);
            CustomMetadataDef.purge(CM_IPR);
            CustomMetadataDef.purge(CM_QUALITY);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove custom metadata (structures).");
        }
    }

    private void validateRACIAttributes(CustomMetadataAttributes cma) {
        assertNotNull(cma);
        validateRACIAttributes(cma.getAttributes());
    }

    static void validateRACIAttributes(Map<String, Object> attributes) {
        assertNotNull(attributes);
        assertEquals(attributes.get(CM_ATTR_RACI_RESPONSIBLE), Set.of(FIXED_USER));
        assertEquals(attributes.get(CM_ATTR_RACI_ACCOUNTABLE), FIXED_USER);
        assertEquals(attributes.get(CM_ATTR_RACI_CONSULTED), Set.of(AdminTest.groupName));
        assertEquals(attributes.get(CM_ATTR_RACI_INFORMED), Set.of(AdminTest.groupName));
    }

    private void validateIPRAttributes(CustomMetadataAttributes cma) {
        validateIPRAttributes(cma, true);
    }

    private void validateIPRAttributes(CustomMetadataAttributes cma, boolean value) {
        assertNotNull(cma);
        validateIPRAttributes(cma.getAttributes(), value);
    }

    static void validateIPRAttributes(Map<String, Object> attributes, boolean value) {
        assertNotNull(attributes);
        assertEquals(attributes.size(), 5);
        assertEquals(attributes.get(CM_ATTR_IPR_LICENSE), "CC BY");
        assertEquals(attributes.get(CM_ATTR_IPR_VERSION), 2.0);
        assertEquals(attributes.get(CM_ATTR_IPR_MANDATORY), value);
        assertEquals(attributes.get(CM_ATTR_IPR_DATE), 1659308400000L);
        assertEquals(attributes.get(CM_ATTR_IPR_URL), "https://creativecommons.org/licenses/by/2.0/");
    }

    private void validateDQAttributes(CustomMetadataAttributes cma) {
        assertNotNull(cma);
        validateDQAttributes(cma.getAttributes());
    }

    static void validateDQAttributes(Map<String, Object> attributes) {
        assertNotNull(attributes);
        assertEquals(attributes.size(), 2);
        assertEquals(attributes.get(CM_ATTR_QUALITY_COUNT), 42L);
        assertEquals(attributes.get(CM_ATTR_QUALITY_SQL), "SELECT * from SOMEWHERE;");
    }

    private AttributeDef validateRACIStructure(List<AttributeDef> list, int totalExpected) {
        assertNotNull(list);
        assertEquals(list.size(), totalExpected);
        AttributeDef one = list.get(0);
        assertEquals(one.getDisplayName(), CM_ATTR_RACI_RESPONSIBLE);
        assertNotEquals(one.getName(), CM_ATTR_RACI_RESPONSIBLE);
        assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
        assertTrue(one.getOptions().getCustomApplicableEntityTypes().contains(Database.TYPE_NAME));
        assertFalse(one.getOptions().getIsArchived());
        assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.USERS.getValue());
        one = list.get(1);
        assertEquals(one.getDisplayName(), CM_ATTR_RACI_ACCOUNTABLE);
        assertNotEquals(one.getName(), CM_ATTR_RACI_ACCOUNTABLE);
        assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
        assertTrue(one.getOptions().getCustomApplicableEntityTypes().contains(Table.TYPE_NAME));
        assertFalse(one.getOptions().getIsArchived());
        assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.USERS.getValue());
        one = list.get(2);
        assertEquals(one.getDisplayName(), CM_ATTR_RACI_CONSULTED);
        assertNotEquals(one.getName(), CM_ATTR_RACI_CONSULTED);
        assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
        assertTrue(one.getOptions().getCustomApplicableEntityTypes().contains(Column.TYPE_NAME));
        assertFalse(one.getOptions().getIsArchived());
        assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.GROUPS.getValue());
        one = list.get(3);
        assertEquals(one.getDisplayName(), CM_ATTR_RACI_INFORMED);
        assertNotEquals(one.getName(), CM_ATTR_RACI_INFORMED);
        assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
        assertTrue(one.getOptions().getCustomApplicableEntityTypes().contains(MaterializedView.TYPE_NAME));
        assertFalse(one.getOptions().getIsArchived());
        assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.GROUPS.getValue());
        if (totalExpected > 5) {
            // If we're expecting more than 4, then the penultimate must be an archived CM_ATTR_EXTRA
            one = list.get(4);
            assertEquals(one.getDisplayName(), CM_ATTR_RACI_EXTRA + "-archived-" + removalEpoch);
            assertNotEquals(one.getName(), CM_ATTR_RACI_EXTRA);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertTrue(one.getOptions().getCustomApplicableEntityTypes().contains(GlossaryTerm.TYPE_NAME));
            assertTrue(one.getOptions().getIsArchived());
        }
        if (totalExpected > 4) {
            return list.get(totalExpected - 1);
        }
        return null;
    }
}
