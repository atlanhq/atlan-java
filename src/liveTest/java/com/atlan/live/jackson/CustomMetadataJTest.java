package com.atlan.live.jackson;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.live.AtlanLiveTest;
import com.atlan.model.CustomMetadataAttributesJ;
import com.atlan.model.GlossaryTermJ;
import com.atlan.model.S3ObjectJ;
import com.atlan.model.core.EntityJ;
import com.atlan.model.enums.AtlanCustomAttributePrimitiveType;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.responses.EntityMutationResponseJ;
import com.atlan.model.typedefs.AttributeDefJ;
import com.atlan.model.typedefs.CustomMetadataDefJ;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.testng.annotations.Test;

@Test(groups = {"custom_metadata"})
public class CustomMetadataJTest extends AtlanLiveTest {

    public static final String CM_NAME1 = "JC CM1";
    public static final String CM_NAME2 = "JC CM2";

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

    @Test(groups = {"create.cm"})
    void createCustomMetadata() {
        try {
            CustomMetadataDefJ customMetadataDef = CustomMetadataDefJ.toCreate(CM_NAME1).toBuilder()
                    .attributeDef(
                            AttributeDefJ.of(CM_ATTR_STRING, AtlanCustomAttributePrimitiveType.STRING, null, true))
                    .attributeDef(
                            AttributeDefJ.of(CM_ATTR_INTEGER, AtlanCustomAttributePrimitiveType.INTEGER, null, false))
                    .attributeDef(
                            AttributeDefJ.of(CM_ATTR_DECIMAL, AtlanCustomAttributePrimitiveType.DECIMAL, null, false))
                    .attributeDef(
                            AttributeDefJ.of(CM_ATTR_BOOLEAN, AtlanCustomAttributePrimitiveType.BOOLEAN, null, false))
                    .attributeDef(AttributeDefJ.of(CM_ATTR_DATE, AtlanCustomAttributePrimitiveType.DATE, null, false))
                    .attributeDef(AttributeDefJ.of(CM_ATTR_URL, AtlanCustomAttributePrimitiveType.URL, null, false))
                    .attributeDef(AttributeDefJ.of(CM_ATTR_SQL, AtlanCustomAttributePrimitiveType.SQL, null, false))
                    .build();
            CustomMetadataDefJ response = customMetadataDef.create();
            assertNotNull(response);
            assertEquals(response.getCategory(), AtlanTypeCategory.BUSINESS_METADATA);
            assertNotNull(response.getName());
            assertNotEquals(response.getName(), CM_NAME1);
            assertNotNull(response.getGuid());
            assertEquals(response.getDisplayName(), CM_NAME1);
            List<AttributeDefJ> attributes = response.getAttributeDefs();
            assertNotNull(attributes);
            assertEquals(attributes.size(), 7);
            AttributeDefJ one = attributes.get(0);
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

            customMetadataDef = CustomMetadataDefJ.toCreate(CM_NAME2).toBuilder()
                    .attributeDef(
                            AttributeDefJ.of(CM_ATTR_STRING, AtlanCustomAttributePrimitiveType.STRING, null, true))
                    .attributeDef(
                            AttributeDefJ.of(CM_ATTR_INTEGER, AtlanCustomAttributePrimitiveType.INTEGER, null, false))
                    .attributeDef(
                            AttributeDefJ.of(CM_ATTR_DECIMAL, AtlanCustomAttributePrimitiveType.DECIMAL, null, false))
                    .attributeDef(
                            AttributeDefJ.of(CM_ATTR_BOOLEAN, AtlanCustomAttributePrimitiveType.BOOLEAN, null, false))
                    .attributeDef(AttributeDefJ.of(CM_ATTR_DATE, AtlanCustomAttributePrimitiveType.DATE, null, false))
                    .attributeDef(AttributeDefJ.of(CM_ATTR_URL, AtlanCustomAttributePrimitiveType.URL, null, false))
                    .attributeDef(AttributeDefJ.of(CM_ATTR_SQL, AtlanCustomAttributePrimitiveType.SQL, null, false))
                    .build();
            response = customMetadataDef.create();
            assertNotNull(response);
            assertEquals(response.getCategory(), AtlanTypeCategory.BUSINESS_METADATA);
            assertNotNull(response.getName());
            assertNotEquals(response.getName(), CM_NAME2);
            assertNotNull(response.getGuid());
            assertEquals(response.getDisplayName(), CM_NAME2);
            attributes = response.getAttributeDefs();
            assertNotNull(attributes);
            assertEquals(attributes.size(), 7);
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
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a new custom metadata structures.");
        }
    }

    @Test(
            groups = {"link.cm.term"},
            dependsOnGroups = {"create.cm", "unlink.asset.term.*", "unlink.term.asset"})
    void updateTermCM() {
        try {
            CustomMetadataAttributesJ cm1 = CustomMetadataAttributesJ.builder()
                    .attribute(CM_ATTR_STRING, List.of("one", "two", "three"))
                    .attribute(CM_ATTR_INTEGER, 42)
                    // TODO: currently broken .attribute(CM_ATTR_DECIMAL, 4.2)
                    .attribute(CM_ATTR_BOOLEAN, true)
                    .attribute(CM_ATTR_DATE, 1659308400000L)
                    .attribute(CM_ATTR_URL, "http://www.example.com")
                    .attribute(CM_ATTR_SQL, "SELECT * from SOMEWHERE;")
                    .build();
            GlossaryTermJ toUpdate = GlossaryTermJ.updater(
                            GlossaryJTest.termQame1, GlossaryJTest.TERM_NAME1, GlossaryJTest.glossaryGuid)
                    .customMetadata(CM_NAME1, cm1)
                    .build();
            EntityMutationResponseJ response = toUpdate.upsert(false, true);
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            EntityJ one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof GlossaryTermJ);
            GlossaryTermJ term = (GlossaryTermJ) one;
            assertEquals(term.getQualifiedName(), GlossaryJTest.termQame1);
            one = EntityJ.retrieveFull(GlossaryTermJ.TYPE_NAME, GlossaryJTest.termQame1);
            assertNotNull(one);
            assertTrue(one instanceof GlossaryTermJ);
            term = (GlossaryTermJ) one;
            assertEquals(term.getQualifiedName(), GlossaryJTest.termQame1);
            assertNotNull(term.getCustomMetadataSets());
            cm1 = term.getCustomMetadataSets().get(CM_NAME1);
            validateAttributes(cm1);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected error trying to update term with custom metadata.");
        }
    }

    @Test(
            groups = {"unlink.cm.term"},
            dependsOnGroups = {"link.cm.term"})
    void removeTermCM() {
        try {
            GlossaryTermJ toUpdate = GlossaryTermJ.updater(
                            GlossaryJTest.termQame1, GlossaryJTest.TERM_NAME1, GlossaryJTest.glossaryGuid)
                    .build();
            toUpdate.removeCustomMetadata();
            EntityMutationResponseJ response = toUpdate.upsert(false, true);
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            EntityJ one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof GlossaryTermJ);
            GlossaryTermJ term = (GlossaryTermJ) one;
            assertEquals(term.getQualifiedName(), GlossaryJTest.termQame1);
            one = EntityJ.retrieveFull(GlossaryTermJ.TYPE_NAME, GlossaryJTest.termQame1);
            assertNotNull(one);
            assertTrue(one instanceof GlossaryTermJ);
            term = (GlossaryTermJ) one;
            assertEquals(term.getQualifiedName(), GlossaryJTest.termQame1);
            assertTrue(term.getCustomMetadataSets().isEmpty());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected error trying to remove custom metadata from term.");
        }
    }

    @Test(
            groups = {"link.cm.s3object.1"},
            dependsOnGroups = {"create.s3object", "create.cm"})
    void addObjectCM1() {
        try {
            CustomMetadataAttributesJ cm = CustomMetadataAttributesJ.builder()
                    .attribute(CM_ATTR_STRING, List.of("one", "two", "three"))
                    .attribute(CM_ATTR_INTEGER, 42)
                    // TODO: currently broken .attribute(CM_ATTR_DECIMAL, 4.2)
                    .attribute(CM_ATTR_BOOLEAN, true)
                    .attribute(CM_ATTR_DATE, 1659308400000L)
                    .attribute(CM_ATTR_URL, "http://www.example.com")
                    .attribute(CM_ATTR_SQL, "SELECT * from SOMEWHERE;")
                    .build();
            S3ObjectJ.replaceCustomMetadata(S3AssetJTest.s3Object1Guid, CM_NAME1, cm);
            EntityJ result = S3ObjectJ.retrieveFull(S3AssetJTest.s3Object1Guid);
            assertTrue(result instanceof S3ObjectJ);
            S3ObjectJ object = (S3ObjectJ) result;
            assertNotNull(object);
            Map<String, CustomMetadataAttributesJ> sets = object.getCustomMetadataSets();
            assertNotNull(sets);
            assertEquals(sets.size(), 1);
            CustomMetadataAttributesJ attrs = sets.get(CM_NAME1);
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
            CustomMetadataAttributesJ cm = CustomMetadataAttributesJ.builder()
                    .attribute(CM_ATTR_STRING, List.of("one", "two", "three"))
                    .attribute(CM_ATTR_INTEGER, 42)
                    // TODO: currently broken .attribute(CM_ATTR_DECIMAL, 4.2)
                    .attribute(CM_ATTR_BOOLEAN, true)
                    .attribute(CM_ATTR_DATE, 1659308400000L)
                    .attribute(CM_ATTR_URL, "http://www.example.com")
                    .attribute(CM_ATTR_SQL, "SELECT * from SOMEWHERE;")
                    .build();
            S3ObjectJ.replaceCustomMetadata(S3AssetJTest.s3Object1Guid, CM_NAME2, cm);
            EntityJ result = S3ObjectJ.retrieveFull(S3AssetJTest.s3Object1Guid);
            assertTrue(result instanceof S3ObjectJ);
            S3ObjectJ object = (S3ObjectJ) result;
            assertNotNull(object);
            Map<String, CustomMetadataAttributesJ> sets = object.getCustomMetadataSets();
            assertNotNull(sets);
            assertEquals(sets.size(), 2);
            CustomMetadataAttributesJ attrs = sets.get(CM_NAME1);
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
            CustomMetadataAttributesJ cm = CustomMetadataAttributesJ.builder()
                    .attribute(CM_ATTR_BOOLEAN, false)
                    .build();
            S3ObjectJ.updateCustomMetadataAttributes(S3AssetJTest.s3Object1Guid, CM_NAME1, cm);
            EntityJ result = S3ObjectJ.retrieveFull(S3AssetJTest.s3Object1Guid);
            assertTrue(result instanceof S3ObjectJ);
            S3ObjectJ object = (S3ObjectJ) result;
            assertNotNull(object);
            Map<String, CustomMetadataAttributesJ> sets = object.getCustomMetadataSets();
            assertNotNull(sets);
            assertEquals(sets.size(), 2);
            CustomMetadataAttributesJ attrs = sets.get(CM_NAME1);
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
            S3ObjectJ.removeCustomMetadata(S3AssetJTest.s3Object1Guid, CM_NAME1);
            EntityJ result = S3ObjectJ.retrieveFull(S3AssetJTest.s3Object1Guid);
            assertTrue(result instanceof S3ObjectJ);
            S3ObjectJ object = (S3ObjectJ) result;
            assertNotNull(object);
            Map<String, CustomMetadataAttributesJ> sets = object.getCustomMetadataSets();
            assertNotNull(sets);
            assertEquals(sets.size(), 1);
            CustomMetadataAttributesJ attrs = sets.get(CM_NAME2);
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
            S3ObjectJ.removeCustomMetadata(S3AssetJTest.s3Object1Guid, CM_NAME2);
            EntityJ result = S3ObjectJ.retrieveFull(S3AssetJTest.s3Object1Guid);
            assertTrue(result instanceof S3ObjectJ);
            S3ObjectJ object = (S3ObjectJ) result;
            assertNotNull(object);
            Map<String, CustomMetadataAttributesJ> sets = object.getCustomMetadataSets();
            assertNotNull(sets);
            assertTrue(sets.isEmpty());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove custom metadata from an asset.");
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
                "purge.connection"
            },
            alwaysRun = true)
    void purgeCustomMetadata() {
        try {
            CustomMetadataDefJ.purge(CM_NAME1);
            CustomMetadataDefJ.purge(CM_NAME2);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove custom metadata (structures).");
        }
    }

    private void validateAttributes(CustomMetadataAttributesJ cma) {
        validateAttributes(cma, true);
    }

    private void validateAttributes(CustomMetadataAttributesJ cma, boolean value) {
        assertNotNull(cma);
        assertNotNull(cma.getAttributes());
        assertEquals(cma.getAttributes().get(CM_ATTR_STRING), Set.of("one", "two", "three"));
        assertEquals(cma.getAttributes().get(CM_ATTR_INTEGER), 42L);
        // TODO: currently broken assertEquals(cma.getAttributes().get(CM_ATTR_DECIMAL), 4.2);
        assertEquals(cma.getAttributes().get(CM_ATTR_DATE), 1659308400000L);
        assertEquals(cma.getAttributes().get(CM_ATTR_BOOLEAN), value);
        assertEquals(cma.getAttributes().get(CM_ATTR_URL), "http://www.example.com");
        assertEquals(cma.getAttributes().get(CM_ATTR_SQL), "SELECT * from SOMEWHERE;");
    }
}
