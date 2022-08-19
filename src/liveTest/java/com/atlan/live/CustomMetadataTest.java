package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.model.CustomMetadata;
import com.atlan.model.GlossaryTerm;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanCustomAttributePrimitiveType;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.responses.EntityMutationResponse;
import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.CustomMetadataDef;
import java.util.List;
import java.util.Set;
import org.testng.annotations.Test;

public class CustomMetadataTest extends AtlanLiveTest {

    public static final String CUSTOM_METADATA_SET_NAME = "JC CM";
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

    public static String customMetadataGuid;
    public static String customMetadataQame;

    @Test(groups = {"create.cm", "create"})
    void createCustomMetadata() {
        try {
            CustomMetadataDef customMetadataDef = CustomMetadataDef.toCreate(CUSTOM_METADATA_SET_NAME).toBuilder()
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
                    .build();
            CustomMetadataDef response = customMetadataDef.create();
            assertNotNull(response);
            assertEquals(response.getCategory(), AtlanTypeCategory.BUSINESS_METADATA);
            customMetadataQame = response.getName();
            assertNotNull(customMetadataQame);
            assertNotEquals(customMetadataQame, CUSTOM_METADATA_SET_NAME);
            customMetadataGuid = response.getGuid();
            assertNotNull(customMetadataGuid);
            assertEquals(response.getDisplayName(), CUSTOM_METADATA_SET_NAME);
            List<AttributeDef> attributes = response.getAttributeDefs();
            assertNotNull(attributes);
            assertEquals(attributes.size(), 7);
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
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a new custom metadata structure.");
        }
    }

    @Test(
            groups = {"update.term.cm", "update"},
            dependsOnGroups = {"create", "create.cm", "link.remove2"})
    void updateTermCM() {
        try {
            CustomMetadata cm = CustomMetadata.builder()
                    .withAttribute(CUSTOM_METADATA_SET_NAME, CM_ATTR_STRING, List.of("one", "two", "three"))
                    .withAttribute(CUSTOM_METADATA_SET_NAME, CM_ATTR_INTEGER, 42)
                    .withAttribute(CUSTOM_METADATA_SET_NAME, CM_ATTR_DECIMAL, 4.2)
                    .withAttribute(CUSTOM_METADATA_SET_NAME, CM_ATTR_BOOLEAN, true)
                    .withAttribute(CUSTOM_METADATA_SET_NAME, CM_ATTR_DATE, 123456789L)
                    .withAttribute(CUSTOM_METADATA_SET_NAME, CM_ATTR_URL, "http://www.example.com")
                    .withAttribute(CUSTOM_METADATA_SET_NAME, CM_ATTR_SQL, "SELECT * from SOMEWHERE;")
                    .build();
            GlossaryTerm toUpdate =
                    GlossaryTerm.toUpdate(GlossaryTest.termQame, GlossaryTest.TERM_NAME, GlossaryTest.glossaryGuid);
            toUpdate = toUpdate.toBuilder().customMetadata(cm).build();
            EntityMutationResponse response = toUpdate.upsert(false, true);
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof GlossaryTerm);
            GlossaryTerm term = (GlossaryTerm) one;
            assertEquals(term.getQualifiedName(), GlossaryTest.termQame);
            one = Entity.retrieveFull(GlossaryTerm.TYPE_NAME, GlossaryTest.termQame);
            assertNotNull(one);
            assertTrue(one instanceof GlossaryTerm);
            term = (GlossaryTerm) one;
            assertEquals(term.getQualifiedName(), GlossaryTest.termQame);
            assertNotNull(term.getCustomMetadata());
            assertEquals(
                    term.getCustomMetadata().getValueForAttribute(CUSTOM_METADATA_SET_NAME, CM_ATTR_STRING),
                    Set.of("one", "two", "three"));
            /*assertEquals(
                    ((LazilyParsedNumber) term.getCustomMetadata()
                                    .getValueForAttribute(CUSTOM_METADATA_SET_NAME, CM_ATTR_INTEGER))
                            .intValue(),
                    42);
            // TODO: float / double values lose precision with GSON serde...
            assertEquals(
            ((LazilyParsedNumber) term.getCustomMetadata().getValueForAttribute(CUSTOM_METADATA_SET_NAME, CM_ATTR_DECIMAL)).floatValue(),
            4.2);
            assertEquals(
                    ((LazilyParsedNumber) term.getCustomMetadata()
                                    .getValueForAttribute(CUSTOM_METADATA_SET_NAME, CM_ATTR_DATE))
                            .longValue(),
                    123456789L);*/
            assertEquals(
                    term.getCustomMetadata().getValueForAttribute(CUSTOM_METADATA_SET_NAME, CM_ATTR_BOOLEAN), true);
            assertEquals(
                    term.getCustomMetadata().getValueForAttribute(CUSTOM_METADATA_SET_NAME, CM_ATTR_URL),
                    "http://www.example.com");
            assertEquals(
                    term.getCustomMetadata().getValueForAttribute(CUSTOM_METADATA_SET_NAME, CM_ATTR_SQL),
                    "SELECT * from SOMEWHERE;");
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected error trying to update term with custom metadata.");
        }
    }

    @Test(
            groups = {"remove.term.cm", "update"},
            dependsOnGroups = {"update.term.cm"})
    void removeTermCM() {
        try {
            GlossaryTerm toUpdate =
                    GlossaryTerm.toUpdate(GlossaryTest.termQame, GlossaryTest.TERM_NAME, GlossaryTest.glossaryGuid);
            toUpdate.removeCustomMetadata();
            EntityMutationResponse response = toUpdate.upsert(false, true);
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof GlossaryTerm);
            GlossaryTerm term = (GlossaryTerm) one;
            assertEquals(term.getQualifiedName(), GlossaryTest.termQame);
            one = Entity.retrieveFull(GlossaryTerm.TYPE_NAME, GlossaryTest.termQame);
            assertNotNull(one);
            assertTrue(one instanceof GlossaryTerm);
            term = (GlossaryTerm) one;
            assertEquals(term.getQualifiedName(), GlossaryTest.termQame);
            assertTrue(term.getCustomMetadata().isEmpty());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected error trying to remove custom metadata from term.");
        }
    }

    @Test(
            groups = {"purge.cm", "purge"},
            dependsOnGroups = {"create", "read", "update", "term.purge"},
            alwaysRun = true)
    void purgeCustomMetadata() {
        try {
            CustomMetadataDef.purge(CUSTOM_METADATA_SET_NAME);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a new classification.");
        }
    }
}
