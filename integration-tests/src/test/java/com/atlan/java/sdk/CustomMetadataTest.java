/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import com.atlan.*;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.admin.AtlanGroup;
import com.atlan.model.assets.*;
import com.atlan.model.core.*;
import com.atlan.model.enums.*;
import com.atlan.model.fields.*;
import com.atlan.model.search.*;
import com.atlan.model.structs.*;
import com.atlan.model.typedefs.*;
import com.atlan.net.RequestOptions;
import java.util.*;
import java.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import org.testng.util.*;

/**
 * Test management of custom metadata.
 */
@Slf4j
public class CustomMetadataTest extends AtlanLiveTest {

    private static final int MAX_CM_RETRIES = 30;
    private static final String PREFIX = makeUnique("CM");

    private static final String CM_RACI = makeUnique("RACI");
    private static final String CM_IPR = makeUnique("IPR");
    private static final String CM_QUALITY = makeUnique("DQ");
    private static final String CM_ATTR_RACI_RESPONSIBLE = "Responsible"; // user
    private static final String CM_ATTR_RACI_ACCOUNTABLE = "Accountable"; // user
    private static final String CM_ATTR_RACI_CONSULTED = "Consulted"; // group
    private static final String CM_ATTR_RACI_INFORMED = "Informed"; // group
    private static final String CM_ATTR_RACI_EXTRA = "Extra"; // string

    private static final String CM_ATTR_IPR_LICENSE = "License"; // string
    private static final String CM_ATTR_IPR_VERSION = "Version"; // decimal
    private static final String CM_ATTR_IPR_MANDATORY = "Mandatory"; // boolean
    private static final String CM_ATTR_IPR_DATE = "Date"; // date
    private static final String CM_ATTR_IPR_URL = "URL"; // url

    private static final String CM_ATTR_QUALITY_COUNT = "Count"; // integer
    private static final String CM_ATTR_QUALITY_SQL = "SQL"; // sql
    private static final String CM_ATTR_QUALITY_TYPE = "Type"; // options
    private static final String CM_ENUM_DQ_TYPE = makeUnique("DataQualityType");
    private static final List<String> DQ_TYPE_LIST =
            List.of("Accuracy", "Completeness", "Consistency", "Timeliness", "Validity", "Uniqueness");

    private static CustomMetadataField CM_RACI_RESPONSIBLE = null;
    private static CustomMetadataField CM_RACI_ACCOUNTABLE = null;
    private static CustomMetadataField CM_RACI_INFORMED = null;
    private static final String GROUP_NAME1 = PREFIX + "1";
    private static final String GROUP_NAME2 = PREFIX + "2";

    private static Table table = null;
    private Connection connection = null;
    private Database database = null;
    private Schema schema = null;
    private static final String TABLE_PREFIX = makeUnique("TABLE_ATTR");
    private static final String TABLE_NAME = TABLE_PREFIX + "_name";
    private static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.CLICKHOUSE;
    private static final String CONNECTION_PREFIX = makeUnique("SQL");

    public static final String CONNECTION_NAME = CONNECTION_PREFIX;
    public static final String DATABASE_NAME = CONNECTION_PREFIX + "_db";
    public static final String SCHEMA_NAME = CONNECTION_PREFIX + "_schema";

    private static Glossary glossary = null;
    private static GlossaryTerm term = null;
    private static AtlanGroup group1 = null;
    private static AtlanGroup group2 = null;
    private static Badge badge1 = null;
    private static Badge badge2 = null;
    private static Badge badge3 = null;
    private static Badge badge4 = null;
    private static long removalEpoch;

    @Test(groups = {"cm.create.cm.ipr"})
    void createCustomMetadataIPR() throws AtlanException {
        CustomMetadataDef customMetadataDef = CustomMetadataDef.creator(CM_IPR)
                .attributeDef(AttributeDef.of(
                        client, CM_ATTR_IPR_LICENSE, AtlanCustomAttributePrimitiveType.STRING, null, false))
                .attributeDef(AttributeDef.of(
                        client, CM_ATTR_IPR_VERSION, AtlanCustomAttributePrimitiveType.DECIMAL, null, false))
                .attributeDef(AttributeDef.of(
                        client, CM_ATTR_IPR_MANDATORY, AtlanCustomAttributePrimitiveType.BOOLEAN, null, false))
                .attributeDef(
                        AttributeDef.of(client, CM_ATTR_IPR_DATE, AtlanCustomAttributePrimitiveType.DATE, null, false))
                .attributeDef(
                        AttributeDef.of(client, CM_ATTR_IPR_URL, AtlanCustomAttributePrimitiveType.URL, null, false))
                .options(CustomMetadataOptions.withEmoji("⚖️", true))
                .build();
        TypeDefResponse typeDefResponse = client.typeDefs.create(
                customMetadataDef,
                RequestOptions.from(client).maxNetworkRetries(MAX_CM_RETRIES).build());
        assertNotNull(typeDefResponse);
        assertNotNull(typeDefResponse.getCustomMetadataDefs());
        assertEquals(typeDefResponse.getCustomMetadataDefs().size(), 1);
        CustomMetadataDef response = typeDefResponse.getCustomMetadataDefs().get(0);
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
    }

    @Test(groups = {"cm.create.cm.raci"})
    void createCustomMetadataRACI() throws AtlanException {
        AttributeDef informed =
                AttributeDef.of(client, CM_ATTR_RACI_INFORMED, AtlanCustomAttributePrimitiveType.GROUPS, null, false)
                        .toBuilder()
                        .multiValued(true)
                        .build();
        CustomMetadataDef customMetadataDef = CustomMetadataDef.creator(CM_RACI)
                .attributeDef(AttributeDef.of(
                        client, CM_ATTR_RACI_RESPONSIBLE, AtlanCustomAttributePrimitiveType.USERS, null, true))
                .attributeDef(AttributeDef.of(
                        client, CM_ATTR_RACI_ACCOUNTABLE, AtlanCustomAttributePrimitiveType.USERS, null, false))
                .attributeDef(AttributeDef.of(
                        client, CM_ATTR_RACI_CONSULTED, AtlanCustomAttributePrimitiveType.GROUPS, null, true))
                .attributeDef(informed)
                .attributeDef(AttributeDef.of(
                        client, CM_ATTR_RACI_EXTRA, AtlanCustomAttributePrimitiveType.STRING, null, false))
                .options(CustomMetadataOptions.withIcon(AtlanIcon.USERS_THREE, AtlanTagColor.GRAY))
                .build();
        TypeDefResponse typeDefResponse = client.typeDefs.create(
                customMetadataDef,
                RequestOptions.from(client).maxNetworkRetries(MAX_CM_RETRIES).build());
        assertNotNull(typeDefResponse);
        assertNotNull(typeDefResponse.getCustomMetadataDefs());
        assertEquals(typeDefResponse.getCustomMetadataDefs().size(), 1);
        CustomMetadataDef response = typeDefResponse.getCustomMetadataDefs().get(0);
        assertNotNull(response);
        assertEquals(response.getCategory(), AtlanTypeCategory.CUSTOM_METADATA);
        assertNotNull(response.getName());
        assertNotEquals(response.getName(), CM_RACI);
        assertNotNull(response.getGuid());
        assertEquals(response.getDisplayName(), CM_RACI);
        List<AttributeDef> attributes = response.getAttributeDefs();
        assertNotNull(attributes);
        assertEquals(attributes.size(), 5);
        AttributeDef one = attributes.get(0);
        assertNotNull(one);
        assertEquals(one.getDisplayName(), CM_ATTR_RACI_RESPONSIBLE);
        assertNotNull(one.getName());
        assertNotEquals(one.getName(), CM_ATTR_RACI_RESPONSIBLE);
        assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
        assertEquals(one.getCardinality(), AtlanCustomAttributeCardinality.SET);
        assertNotNull(one.getOptions());
        assertTrue(one.getOptions().getMultiValueSelect());
        assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.USERS.getValue());
        one = attributes.get(1);
        assertEquals(one.getDisplayName(), CM_ATTR_RACI_ACCOUNTABLE);
        assertNotNull(one.getName());
        assertNotEquals(one.getName(), CM_ATTR_RACI_ACCOUNTABLE);
        assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
        assertEquals(one.getCardinality(), AtlanCustomAttributeCardinality.SINGLE);
        assertNotNull(one.getOptions());
        assertFalse(one.getOptions().getMultiValueSelect());
        assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.USERS.getValue());
        one = attributes.get(2);
        assertEquals(one.getDisplayName(), CM_ATTR_RACI_CONSULTED);
        assertNotNull(one.getName());
        assertNotEquals(one.getName(), CM_ATTR_RACI_CONSULTED);
        assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
        assertEquals(one.getCardinality(), AtlanCustomAttributeCardinality.SET);
        assertNotNull(one.getOptions());
        assertTrue(one.getOptions().getMultiValueSelect());
        assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.GROUPS.getValue());
        one = attributes.get(3);
        assertEquals(one.getDisplayName(), CM_ATTR_RACI_INFORMED);
        assertNotNull(one.getName());
        assertNotEquals(one.getName(), CM_ATTR_RACI_INFORMED);
        assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
        assertEquals(one.getCardinality(), AtlanCustomAttributeCardinality.SET);
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
    }

    @Test(groups = {"cm.create.cm.dq"})
    void createCustomMetadataDQ() throws AtlanException {
        EnumDef enumDef = EnumDef.creator(CM_ENUM_DQ_TYPE, DQ_TYPE_LIST).build();
        TypeDefResponse typeDefResponse = client.typeDefs.create(
                enumDef,
                RequestOptions.from(client).maxNetworkRetries(MAX_CM_RETRIES).build());
        assertNotNull(typeDefResponse);
        assertNotNull(typeDefResponse.getEnumDefs());
        assertEquals(typeDefResponse.getEnumDefs().size(), 1);
        EnumDef resp = typeDefResponse.getEnumDefs().get(0);
        assertNotNull(resp);
        assertEquals(resp.getCategory(), AtlanTypeCategory.ENUM);
        assertNotNull(resp.getName());
        assertEquals(resp.getName(), CM_ENUM_DQ_TYPE);
        assertNotNull(resp.getGuid());
        assertNotNull(resp.getElementDefs());
        assertEquals(resp.getElementDefs().size(), DQ_TYPE_LIST.size());

        CustomMetadataDef customMetadataDef = CustomMetadataDef.creator(CM_QUALITY)
                .attributeDef(AttributeDef.of(
                        client, CM_ATTR_QUALITY_COUNT, AtlanCustomAttributePrimitiveType.INTEGER, null, false))
                .attributeDef(AttributeDef.of(
                        client, CM_ATTR_QUALITY_SQL, AtlanCustomAttributePrimitiveType.SQL, null, false))
                .attributeDef(AttributeDef.of(
                        client,
                        CM_ATTR_QUALITY_TYPE,
                        AtlanCustomAttributePrimitiveType.OPTIONS,
                        CM_ENUM_DQ_TYPE,
                        false))
                .options(CustomMetadataOptions.withImage(
                        "https://github.com/great-expectations/great_expectations/raw/develop/docs/docusaurus/static/img/gx-mark-160.png",
                        true))
                .build();
        typeDefResponse = client.typeDefs.create(
                customMetadataDef,
                RequestOptions.from(client).maxNetworkRetries(MAX_CM_RETRIES).build());
        assertNotNull(typeDefResponse);
        assertNotNull(typeDefResponse.getCustomMetadataDefs());
        assertEquals(typeDefResponse.getCustomMetadataDefs().size(), 1);
        CustomMetadataDef response = typeDefResponse.getCustomMetadataDefs().get(0);
        assertNotNull(response);
        assertEquals(response.getCategory(), AtlanTypeCategory.CUSTOM_METADATA);
        assertNotNull(response.getName());
        assertNotEquals(response.getName(), CM_QUALITY);
        assertNotNull(response.getGuid());
        assertEquals(response.getDisplayName(), CM_QUALITY);
        List<AttributeDef> attributes = response.getAttributeDefs();
        assertNotNull(attributes);
        assertEquals(attributes.size(), 3);
        AttributeDef one = attributes.get(0);
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
        one = attributes.get(2);
        assertEquals(one.getDisplayName(), CM_ATTR_QUALITY_TYPE);
        assertNotNull(one.getName());
        assertNotEquals(one.getName(), CM_ATTR_QUALITY_TYPE);
        // Note: for enumerations, the typeName will be the name of the enumeration
        assertEquals(one.getTypeName(), CM_ENUM_DQ_TYPE);
        assertNotNull(one.getOptions());
        assertFalse(one.getOptions().getMultiValueSelect());
        assertEquals(one.getOptions().getPrimitiveType(), AtlanCustomAttributePrimitiveType.OPTIONS);
        assertEquals(one.getOptions().getEnumType(), CM_ENUM_DQ_TYPE);
    }

    @Test(
            groups = {"cm.update.cm.dq"},
            dependsOnGroups = {"cm.create.cm.dq", "cm.update.term.add.dq"})
    void updateCustomMetadataDQ() throws AtlanException {
        EnumDef enumDef = EnumDef.updater(client, CM_ENUM_DQ_TYPE, List.of("Accuracy", "Awesomeness"), false)
                .build();
        assertNotNull(enumDef);
        EnumDef updated = enumDef.update(client);
        assertNotNull(updated);
        assertEquals(updated.getCategory(), AtlanTypeCategory.ENUM);
        assertNotNull(updated.getName());
        assertEquals(updated.getName(), CM_ENUM_DQ_TYPE);
        assertNotNull(updated.getElementDefs());
        assertEquals(updated.getElementDefs().size(), 7);
        List<String> values = updated.getElementDefs().stream()
                .map(EnumDef.ElementDef::getValue)
                .toList();
        assertTrue(values.contains("Awesomeness"));
        assertTrue(values.contains("Accuracy"));
    }

    @Test(
            groups = {"cm.create.badges"},
            dependsOnGroups = {"cm.create.cm.dq", "cm.create.cm.ipr"})
    void createBadges() throws AtlanException {
        Badge toCreate = Badge.creator(client, CM_ATTR_QUALITY_COUNT, CM_QUALITY, CM_ATTR_QUALITY_COUNT)
                .userDescription("How many data quality checks ran against this asset.")
                .badgeCondition(BadgeCondition.of(BadgeComparisonOperator.GTE, 5, BadgeConditionColor.GREEN))
                .badgeCondition(BadgeCondition.of(BadgeComparisonOperator.LT, 5, BadgeConditionColor.YELLOW))
                .badgeCondition(BadgeCondition.of(BadgeComparisonOperator.LTE, 2, BadgeConditionColor.RED))
                .build();
        AssetMutationResponse response = toCreate.save(client);
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 1);
        assertTrue(response.getCreatedAssets().get(0) instanceof Badge);
        badge1 = (Badge) response.getCreatedAssets().get(0);
        assertNotNull(badge1.getGuid());
        List<BadgeCondition> badgeConditions = badge1.getBadgeConditions();
        assertEquals(badgeConditions.size(), 3);
        // Badges may come back in a different order in the response...
        badgeConditions.forEach(b -> {
            switch (b.getBadgeConditionOperator()) {
                case GTE:
                    assertEquals(b.getBadgeConditionValue(), "5");
                    assertEquals(b.getBadgeConditionColorhex(), BadgeConditionColor.GREEN.getValue());
                    break;
                case LT:
                    assertEquals(b.getBadgeConditionValue(), "5");
                    assertEquals(b.getBadgeConditionColorhex(), BadgeConditionColor.YELLOW.getValue());
                    break;
                case LTE:
                    assertEquals(b.getBadgeConditionValue(), "2");
                    assertEquals(b.getBadgeConditionColorhex(), BadgeConditionColor.RED.getValue());
                    break;
                default:
                    fail("Unexpected badge condition operator: " + b.getBadgeConditionOperator());
            }
        });

        toCreate = Badge.creator(client, CM_ATTR_QUALITY_TYPE, CM_QUALITY, CM_ATTR_QUALITY_TYPE)
                .userDescription("The type of quality checks used on this asset.")
                .badgeCondition(BadgeCondition.of(BadgeComparisonOperator.EQ, "Completeness", "#001122"))
                .badgeCondition(BadgeCondition.of(BadgeComparisonOperator.EQ, "Timeliness", "#ffeedd"))
                .badgeCondition(BadgeCondition.of(BadgeComparisonOperator.EQ, "Accuracy", "#aabbcc"))
                .badgeCondition(BadgeCondition.of(BadgeComparisonOperator.EQ, "Consistency", "#ccbbaa"))
                .build();
        response = toCreate.save(client);
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 1);
        assertTrue(response.getCreatedAssets().get(0) instanceof Badge);
        badge2 = (Badge) response.getCreatedAssets().get(0);
        assertNotNull(badge2.getGuid());
        badgeConditions = badge2.getBadgeConditions();
        assertEquals(badgeConditions.size(), 4);
        for (BadgeCondition condition : badgeConditions) {
            assertEquals(condition.getBadgeConditionOperator(), BadgeComparisonOperator.EQ);
            assertTrue(Set.of("\"Completeness\"", "\"Timeliness\"", "\"Accuracy\"", "\"Consistency\"")
                    .contains(condition.getBadgeConditionValue()));
            assertTrue(
                    Set.of("#001122", "#ffeedd", "#aabbcc", "#ccbbaa").contains(condition.getBadgeConditionColorhex()));
        }

        toCreate = Badge.creator(client, CM_ATTR_IPR_LICENSE, CM_IPR, CM_ATTR_IPR_LICENSE)
                .userDescription("License associated with this asset.")
                .badgeCondition(BadgeCondition.of(BadgeComparisonOperator.EQ, "CC BY", BadgeConditionColor.GREEN))
                .build();
        response = toCreate.save(client);
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 1);
        assertTrue(response.getCreatedAssets().get(0) instanceof Badge);
        badge3 = (Badge) response.getCreatedAssets().get(0);
        assertNotNull(badge3.getGuid());
        badgeConditions = badge3.getBadgeConditions();
        assertEquals(badgeConditions.size(), 1);
        assertEquals(badgeConditions.get(0).getBadgeConditionOperator(), BadgeComparisonOperator.EQ);
        assertEquals(badgeConditions.get(0).getBadgeConditionValue(), "\"CC BY\"");
        assertEquals(badgeConditions.get(0).getBadgeConditionColorhex(), BadgeConditionColor.GREEN.getValue());

        toCreate = Badge.creator(client, CM_ATTR_IPR_MANDATORY, CM_IPR, CM_ATTR_IPR_MANDATORY)
                .userDescription("Whether adherence to the associated license is mandatory or not.")
                .badgeCondition(BadgeCondition.of(BadgeComparisonOperator.EQ, true, BadgeConditionColor.RED))
                .badgeCondition(BadgeCondition.of(BadgeComparisonOperator.EQ, false, BadgeConditionColor.GREEN))
                .build();
        response = toCreate.save(client);
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 1);
        assertTrue(response.getCreatedAssets().get(0) instanceof Badge);
        badge4 = (Badge) response.getCreatedAssets().get(0);
        assertNotNull(badge4.getGuid());
        badgeConditions = badge4.getBadgeConditions();
        assertEquals(badgeConditions.size(), 2);
        for (BadgeCondition condition : badgeConditions) {
            assertEquals(condition.getBadgeConditionOperator(), BadgeComparisonOperator.EQ);
            assertTrue(Set.of("true", "false").contains(condition.getBadgeConditionValue()));
            assertTrue(Set.of(BadgeConditionColor.RED.getValue(), BadgeConditionColor.GREEN.getValue())
                    .contains(condition.getBadgeConditionColorhex()));
        }
    }

    @Test(groups = {"cm.create.term"})
    void createTerm() throws AtlanException {
        glossary = GlossaryTest.createGlossary(client, PREFIX);
        term = GlossaryTest.createTerm(client, PREFIX, glossary);
    }

    @Test(groups = {"cm.create.groups"})
    void createGroups() throws AtlanException {
        group1 = AdminTest.createGroup(client, GROUP_NAME1);
        group2 = AdminTest.createGroup(client, GROUP_NAME2);
    }

    @Test(
            groups = {"cm.search.invalid"},
            dependsOnGroups = {"cm.create.cm.*"})
    void testInvalidSearchParameters() {
        assertThrows(InvalidRequestException.class, () -> GlossaryTerm.select(client)
                .where(CustomMetadataField.of(client, CM_RACI, CM_ATTR_RACI_ACCOUNTABLE)
                        .gt(5)));
        assertThrows(InvalidRequestException.class, () -> GlossaryTerm.select(client)
                .where(CustomMetadataField.of(client, CM_IPR, CM_ATTR_IPR_DATE).startsWith("abc", false)));
        assertThrows(InvalidRequestException.class, () -> GlossaryTerm.select(client)
                .where(CustomMetadataField.of(client, CM_IPR, CM_ATTR_IPR_MANDATORY)
                        .lt(3)));
        assertThrows(InvalidRequestException.class, () -> GlossaryTerm.select(client)
                .where(CustomMetadataField.of(client, CM_QUALITY, CM_ATTR_QUALITY_TYPE)
                        .lte(10)));
    }

    @Test(
            groups = {"cm.update.term.add.raci"},
            dependsOnGroups = {"cm.create.term", "cm.create.cm.raci", "cm.create.groups"})
    void addTermCMRACI() throws AtlanException {
        CustomMetadataAttributes cm = CustomMetadataAttributes.builder()
                .attribute(CM_ATTR_RACI_RESPONSIBLE, List.of(FIXED_USER))
                .attribute(CM_ATTR_RACI_ACCOUNTABLE, FIXED_USER)
                .attribute(CM_ATTR_RACI_CONSULTED, List.of(group1.getName()))
                .attribute(CM_ATTR_RACI_INFORMED, List.of(group1.getName(), group2.getName()))
                .build();
        GlossaryTerm.updateCustomMetadataAttributes(client, term.getGuid(), CM_RACI, cm);
        GlossaryTerm t = GlossaryTerm.get(client, term.getGuid(), true);
        assertNotNull(t);
        assertTrue(t.isComplete());
        Map<String, CustomMetadataAttributes> sets = t.getCustomMetadataSets();
        assertNotNull(sets);
        assertEquals(sets.size(), 1);
        CustomMetadataAttributes attrs = sets.get(CM_RACI);
        validateRACIAttributes(attrs);
    }

    @Test(
            groups = {"cm.update.term.add.ipr"},
            dependsOnGroups = {"cm.update.term.add.raci", "cm.create.cm.ipr"})
    void addTermCMIPR() throws AtlanException {
        CustomMetadataAttributes cm = CustomMetadataAttributes.builder()
                .attribute(CM_ATTR_IPR_LICENSE, "CC BY")
                .attribute(CM_ATTR_IPR_VERSION, 2.0)
                .attribute(CM_ATTR_IPR_MANDATORY, true)
                .attribute(CM_ATTR_IPR_DATE, 1659308400000L)
                .attribute(CM_ATTR_IPR_URL, "https://creativecommons.org/licenses/by/2.0/")
                .build();
        GlossaryTerm.updateCustomMetadataAttributes(client, term.getGuid(), CM_IPR, cm);
        GlossaryTerm t = GlossaryTerm.get(client, term.getGuid(), true);
        assertNotNull(t);
        assertTrue(t.isComplete());
        Map<String, CustomMetadataAttributes> sets = t.getCustomMetadataSets();
        assertNotNull(sets);
        assertEquals(sets.size(), 2);
        CustomMetadataAttributes attrs = sets.get(CM_RACI);
        validateRACIAttributes(attrs);
        attrs = sets.get(CM_IPR);
        validateIPRAttributes(attrs);
    }

    @Test(
            groups = {"cm.update.term.add.dq"},
            dependsOnGroups = {"cm.update.term.add.ipr", "cm.create.cm.dq"})
    void addTermCMDQ() throws AtlanException {
        CustomMetadataAttributes cm = CustomMetadataAttributes.builder()
                .attribute(CM_ATTR_QUALITY_COUNT, 42)
                .attribute(CM_ATTR_QUALITY_SQL, "SELECT * from SOMEWHERE;")
                .attribute(CM_ATTR_QUALITY_TYPE, "Completeness")
                .build();
        GlossaryTerm.updateCustomMetadataAttributes(client, term.getGuid(), CM_QUALITY, cm);
        GlossaryTerm t = GlossaryTerm.get(client, term.getGuid(), true);
        assertNotNull(t);
        assertTrue(t.isComplete());
        Map<String, CustomMetadataAttributes> sets = t.getCustomMetadataSets();
        assertNotNull(sets);
        assertEquals(sets.size(), 3);
        CustomMetadataAttributes attrs = sets.get(CM_RACI);
        validateRACIAttributes(attrs);
        attrs = sets.get(CM_IPR);
        validateIPRAttributes(attrs);
        attrs = sets.get(CM_QUALITY);
        validateDQAttributes(attrs);
    }

    @Test(
            groups = {"cm.update.term.update.ipr"},
            dependsOnGroups = {"cm.update.term.add.dq"})
    void updateTermCMIPR() throws AtlanException {
        CustomMetadataAttributes cm = CustomMetadataAttributes.builder()
                .attribute(CM_ATTR_IPR_MANDATORY, false)
                .build();
        GlossaryTerm.updateCustomMetadataAttributes(client, term.getGuid(), CM_IPR, cm);
        GlossaryTerm t = GlossaryTerm.get(client, term.getQualifiedName(), true);
        assertNotNull(t);
        assertTrue(t.isComplete());
        Map<String, CustomMetadataAttributes> sets = t.getCustomMetadataSets();
        assertNotNull(sets);
        assertEquals(sets.size(), 3);
        CustomMetadataAttributes attrs = sets.get(CM_IPR);
        validateIPRAttributes(attrs, false);
        attrs = sets.get(CM_RACI);
        validateRACIAttributes(attrs);
        attrs = sets.get(CM_QUALITY);
        validateDQAttributes(attrs);
    }

    @Test(
            groups = {"cm.update.term.replace.raci"},
            dependsOnGroups = {"cm.update.term.add.*", "cm.update.term.update.*"})
    void replaceTermCMRACI() throws AtlanException {
        CustomMetadataAttributes cm = CustomMetadataAttributes.builder()
                .attribute(CM_ATTR_RACI_RESPONSIBLE, List.of(FIXED_USER))
                .attribute(CM_ATTR_RACI_ACCOUNTABLE, FIXED_USER)
                .attribute(CM_ATTR_RACI_INFORMED, List.of(group1.getName(), group2.getName()))
                .build();
        GlossaryTerm.replaceCustomMetadata(client, term.getGuid(), CM_RACI, cm);
        GlossaryTerm t = GlossaryTerm.get(client, term.getGuid(), true);
        assertNotNull(t);
        assertTrue(t.isComplete());
        Map<String, CustomMetadataAttributes> sets = t.getCustomMetadataSets();
        assertNotNull(sets);
        assertEquals(sets.size(), 3);
        validateRACIAttributesReplacement(sets.get(CM_RACI));
        validateIPRAttributes(sets.get(CM_IPR), false);
        validateDQAttributes(sets.get(CM_QUALITY));
    }

    @Test(
            groups = {"cm.update.term.replace.ipr"},
            dependsOnGroups = {"cm.update.term.replace.raci"})
    void replaceTermCMIPR() throws AtlanException {
        GlossaryTerm.replaceCustomMetadata(client, term.getGuid(), CM_IPR, null);
        GlossaryTerm t = GlossaryTerm.get(client, term.getGuid(), true);
        assertNotNull(t);
        assertTrue(t.isComplete());
        Map<String, CustomMetadataAttributes> sets = t.getCustomMetadataSets();
        assertNotNull(sets);
        assertEquals(sets.size(), 2);
        validateRACIAttributesReplacement(sets.get(CM_RACI));
        validateDQAttributes(sets.get(CM_QUALITY));
        assertNull(sets.get(CM_IPR));
    }

    @Test(
            groups = {"cm.search.term.cm"},
            dependsOnGroups = {"cm.update.term.replace.ipr"})
    void searchByAnyAccountable() throws AtlanException, InterruptedException {

        IndexSearchRequest index = GlossaryTerm.select(client)
                .where(CustomMetadataField.of(client, CM_RACI, CM_ATTR_RACI_ACCOUNTABLE)
                        .hasAnyValue())
                .includeOnResults(GlossaryTerm.NAME)
                .includeOnResults(GlossaryTerm.ANCHOR)
                .includeOnRelations(Asset.NAME)
                .toRequest();

        IndexSearchResponse response = retrySearchUntil(index, 1L);

        assertEquals(response.getApproximateCount().longValue(), 1L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 1);
        Asset one = entities.get(0);
        assertTrue(one instanceof GlossaryTerm);
        assertFalse(one.isComplete());
        GlossaryTerm t = (GlossaryTerm) one;
        assertEquals(t.getGuid(), term.getGuid());
        assertEquals(t.getQualifiedName(), term.getQualifiedName());
        IGlossary anchor = t.getAnchor();
        assertNotNull(anchor);
        assertEquals(anchor.getName(), glossary.getName());
    }

    @Test(
            groups = {"cm.search.term.cm"},
            dependsOnGroups = {"cm.update.term.replace.ipr"})
    void searchBySpecificAccountable() throws AtlanException, InterruptedException {

        IndexSearchRequest index = GlossaryTerm.select(client)
                .where(CustomMetadataField.of(client, CM_RACI, CM_ATTR_RACI_ACCOUNTABLE)
                        .eq(FIXED_USER, false))
                .includeOnResults(GlossaryTerm.NAME)
                .includeOnResults(GlossaryTerm.ANCHOR)
                .includeOnRelations(Asset.NAME)
                .toRequest();

        IndexSearchResponse response = retrySearchUntil(index, 1L);

        assertEquals(response.getApproximateCount().longValue(), 1L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 1);
        Asset one = entities.get(0);
        assertTrue(one instanceof GlossaryTerm);
        assertFalse(one.isComplete());
        GlossaryTerm t = (GlossaryTerm) one;
        assertEquals(t.getGuid(), term.getGuid());
        assertEquals(t.getQualifiedName(), term.getQualifiedName());
        IGlossary anchor = t.getAnchor();
        assertNotNull(anchor);
        assertEquals(anchor.getName(), glossary.getName());
    }

    @Test(
            groups = {"cm.update.term.remove.raci"},
            dependsOnGroups = {"cm.search.term.cm"})
    void removeTermCMRACI() throws AtlanException {
        GlossaryTerm.removeCustomMetadata(client, term.getGuid(), CM_RACI);
        GlossaryTerm t = GlossaryTerm.get(client, term.getGuid(), true);
        assertNotNull(t);
        assertTrue(t.isComplete());
        Map<String, CustomMetadataAttributes> sets = t.getCustomMetadataSets();
        assertNotNull(sets);
        assertEquals(sets.size(), 1);
        validateDQAttributes(sets.get(CM_QUALITY));
        assertNull(sets.get(CM_IPR));
    }

    @Test(
            groups = {"cm.update.term.remove.ipr"},
            dependsOnGroups = {"cm.update.term.remove.raci"})
    void removeTermCMIPR() throws AtlanException {
        GlossaryTerm.removeCustomMetadata(client, term.getGuid(), CM_IPR);
        GlossaryTerm t = GlossaryTerm.get(client, term.getQualifiedName(), true);
        assertNotNull(t);
        assertTrue(t.isComplete());
        Map<String, CustomMetadataAttributes> sets = t.getCustomMetadataSets();
        assertNotNull(sets);
        assertEquals(sets.size(), 1);
        validateDQAttributes(sets.get(CM_QUALITY));
    }

    @Test(
            groups = {"cm.update.term.remove.dq"},
            dependsOnGroups = {"cm.update.term.remove.ipr"})
    void removeObjectCMDQ() throws AtlanException {
        GlossaryTerm.removeCustomMetadata(client, term.getGuid(), CM_QUALITY);
        GlossaryTerm t = GlossaryTerm.get(client, term.getGuid(), true);
        assertNotNull(t);
        assertTrue(t.isComplete());
        Map<String, CustomMetadataAttributes> sets = t.getCustomMetadataSets();
        assertNotNull(sets);
        assertTrue(sets.isEmpty());
    }

    @Test(
            groups = {"cm.update.cm.attribute.1"},
            dependsOnGroups = {"cm.create.cm.raci"})
    void removeAttribute() throws AtlanException {
        CustomMetadataDef existing = client.getCustomMetadataCache().getByName(CM_RACI);
        List<AttributeDef> existingAttrs = existing.getAttributeDefs();
        List<AttributeDef> updatedAttrs = new ArrayList<>();
        for (AttributeDef existingAttr : existingAttrs) {
            AttributeDef toKeep = existingAttr;
            if (existingAttr.getDisplayName().equals(CM_ATTR_RACI_EXTRA)) {
                toKeep = existingAttr.toBuilder().archive("test-automation").build();
                removalEpoch = toKeep.getOptions().getArchivedAt();
            }
            updatedAttrs.add(toKeep);
        }
        existing = existing.toBuilder()
                .clearAttributeDefs()
                .attributeDefs(updatedAttrs)
                .build();
        TypeDefResponse typeDefResponse = client.typeDefs.update(
                existing,
                RequestOptions.from(client).maxNetworkRetries(MAX_CM_RETRIES).build());
        assertNotNull(typeDefResponse);
        assertNotNull(typeDefResponse.getCustomMetadataDefs());
        assertEquals(typeDefResponse.getCustomMetadataDefs().size(), 1);
        CustomMetadataDef updated = typeDefResponse.getCustomMetadataDefs().get(0);
        assertNotNull(updated);
        assertEquals(updated.getCategory(), AtlanTypeCategory.CUSTOM_METADATA);
        assertNotNull(updated.getName());
        assertNotEquals(updated.getName(), CM_RACI);
        assertNotNull(updated.getGuid());
        assertEquals(updated.getDisplayName(), CM_RACI);
        List<AttributeDef> attributes = updated.getAttributeDefs();
        AttributeDef archived = validateRACIStructure(attributes, 5);
        assertNotNull(archived);
        assertEquals(archived.getDisplayName(), CM_ATTR_RACI_EXTRA + "-archived-" + removalEpoch);
        assertNotNull(archived.getName());
        assertNotEquals(archived.getName(), CM_ATTR_RACI_EXTRA);
        assertEquals(archived.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
        assertNotNull(archived.getOptions());
        assertFalse(archived.getOptions().getMultiValueSelect());
        assertTrue(archived.isArchived());
    }

    @Test(
            groups = {"cm.read.cm.structure.1"},
            dependsOnGroups = {"cm.update.cm.attribute.1"})
    void retrieveStructures() throws AtlanException {
        Map<String, List<AttributeDef>> map = client.getCustomMetadataCache().getAllCustomAttributes();
        assertNotNull(map);
        assertTrue(map.size() >= 3);
        assertTrue(map.containsKey(CM_RACI));
        assertTrue(map.containsKey(CM_IPR));
        assertTrue(map.containsKey(CM_QUALITY));
        AttributeDef extra = validateRACIStructure(map.get(CM_RACI), 4);
        assertNull(extra);
        map = client.getCustomMetadataCache().getAllCustomAttributes(true);
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
        assertTrue(extra.getOptions().getApplicableAssetTypes().contains(Database.TYPE_NAME));
        assertFalse(extra.getOptions().getMultiValueSelect());
        assertTrue(extra.isArchived());
    }

    @Test(
            groups = {"cm.update.cm.attribute.2"},
            dependsOnGroups = {"cm.read.cm.structure.1"})
    void recreateAttribute() throws AtlanException {
        CustomMetadataDef existing = client.getCustomMetadataCache().getByName(CM_RACI);
        List<AttributeDef> existingAttrs = existing.getAttributeDefs();
        List<AttributeDef> updatedAttrs = new ArrayList<>();
        for (AttributeDef attributeDef : existingAttrs) {
            updatedAttrs.add(attributeDef.toBuilder().isNew(null).build());
        }
        updatedAttrs.add(
                AttributeDef.of(client, CM_ATTR_RACI_EXTRA, AtlanCustomAttributePrimitiveType.STRING, null, false));
        existing = existing.toBuilder()
                .clearAttributeDefs()
                .attributeDefs(updatedAttrs)
                .build();
        TypeDefResponse typeDefResponse = client.typeDefs.update(
                existing,
                RequestOptions.from(client).maxNetworkRetries(MAX_CM_RETRIES).build());
        assertNotNull(typeDefResponse);
        assertNotNull(typeDefResponse.getCustomMetadataDefs());
        assertEquals(typeDefResponse.getCustomMetadataDefs().size(), 1);
        CustomMetadataDef updated = typeDefResponse.getCustomMetadataDefs().get(0);
        assertNotNull(updated);
        assertEquals(updated.getCategory(), AtlanTypeCategory.CUSTOM_METADATA);
        assertNotNull(updated.getName());
        assertNotEquals(updated.getName(), CM_RACI);
        assertNotNull(updated.getGuid());
        assertEquals(updated.getDisplayName(), CM_RACI);
        List<AttributeDef> attributes = updated.getAttributeDefs();
        assertNotNull(attributes);
        AttributeDef extra = validateRACIStructure(attributes, 6);
        assertNotNull(extra);
        assertEquals(extra.getDisplayName(), CM_ATTR_RACI_EXTRA);
        assertNotNull(extra.getName());
        assertNotEquals(extra.getName(), CM_ATTR_RACI_EXTRA);
        assertEquals(extra.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
        assertNotNull(extra.getOptions());
        assertFalse(extra.getOptions().getMultiValueSelect());
        assertFalse(extra.isArchived());
    }

    @Test(
            groups = {"cm.read.cm.structure.2"},
            dependsOnGroups = {"cm.update.cm.attribute.2"})
    void retrieveStructureWithoutArchived2() {
        try {
            Map<String, List<AttributeDef>> map =
                    client.getCustomMetadataCache().getAllCustomAttributes();
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
            assertTrue(extra.getOptions().getApplicableAssetTypes().contains(Database.TYPE_NAME));
            assertFalse(extra.isArchived());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to retrieve a custom metadata structure.");
        }
    }

    @Test(
            groups = {"cm.read.cm.structure.3"},
            dependsOnGroups = {"cm.update.cm.attribute.2"})
    void retrieveStructureWithArchived() {
        try {
            Map<String, List<AttributeDef>> map =
                    client.getCustomMetadataCache().getAllCustomAttributes(true);
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
            assertTrue(extra.getOptions().getApplicableAssetTypes().contains(Database.TYPE_NAME));
            assertFalse(extra.isArchived());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove custom metadata from an asset.");
        }
    }

    @Test(
            groups = {"cm.update.term.replaceAll.raci"},
            dependsOnGroups = {"cm.update.cm.attribute.2", "cm.update.term.remove.dq"})
    void updateTermCMRACI() throws AtlanException {
        CustomMetadataAttributes cm1 = CustomMetadataAttributes.builder()
                .attribute(CM_ATTR_RACI_RESPONSIBLE, List.of(FIXED_USER))
                .attribute(CM_ATTR_RACI_ACCOUNTABLE, FIXED_USER)
                .attribute(CM_ATTR_RACI_CONSULTED, List.of(group1.getName()))
                .attribute(CM_ATTR_RACI_INFORMED, List.of(group1.getName(), group2.getName()))
                .attribute(CM_ATTR_RACI_EXTRA, "something extra...")
                .build();
        GlossaryTerm toUpdate = GlossaryTerm.updater(term.getQualifiedName(), term.getName(), glossary.getGuid())
                .customMetadata(CM_RACI, cm1)
                .build();
        AssetMutationResponse response = toUpdate.saveReplacingCM(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertTrue(response.getCreatedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof GlossaryTerm);
        GlossaryTerm t = (GlossaryTerm) one;
        assertEquals(t.getQualifiedName(), term.getQualifiedName());
        t = GlossaryTerm.get(client, term.getQualifiedName(), true);
        assertNotNull(t);
        assertTrue(t.isComplete());
        assertEquals(t.getQualifiedName(), term.getQualifiedName());
        assertNotNull(t.getCustomMetadataSets());
        cm1 = t.getCustomMetadataSets().get(CM_RACI);
        validateRACIAttributes(cm1);
        assertEquals(cm1.getAttributes().get(CM_ATTR_RACI_EXTRA), "something extra...");
    }

    @Test(
            groups = {"cm.update.term.remove.cm"},
            dependsOnGroups = {"cm.update.term.replaceAll.raci", "cm.search.term.cm"})
    void removeTermCM() throws AtlanException {
        GlossaryTerm toUpdate = GlossaryTerm.updater(term.getQualifiedName(), term.getName(), glossary.getGuid())
                .removeCustomMetadata()
                .build();
        AssetMutationResponse response = toUpdate.saveReplacingCM(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertTrue(response.getCreatedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof GlossaryTerm);
        GlossaryTerm t = (GlossaryTerm) one;
        assertEquals(t.getQualifiedName(), term.getQualifiedName());
        t = GlossaryTerm.get(client, term.getQualifiedName(), true);
        assertNotNull(t);
        assertTrue(t.isComplete());
        assertEquals(t.getQualifiedName(), term.getQualifiedName());
        assertTrue(t.getCustomMetadataSets().isEmpty());
    }

    @Test(
            groups = {"cm.read.term.audit"},
            dependsOnGroups = {"cm.update.term.remove.cm"})
    void readTermAuditByGuid() throws AtlanException, InterruptedException {
        AuditSearchRequest request =
                AuditSearchRequest.byGuid(client, term.getGuid(), 100).build();
        AuditSearchResponse response = retrySearchUntil(request, 14L);
        validateAudits(response.getEntityAudits());
    }

    @Test(
            groups = {"cm.read.term.audit"},
            dependsOnGroups = {"cm.update.term.remove.cm"})
    void readTermAuditByQN() throws AtlanException, InterruptedException {
        AuditSearchRequest request = AuditSearchRequest.byQualifiedName(
                        client, GlossaryTerm.TYPE_NAME, term.getQualifiedName(), 100)
                .build();
        AuditSearchResponse response = retrySearchUntil(request, 14L);
        validateAudits(response.getEntityAudits());
    }

    @Test(
            groups = {"cm.purge.term"},
            dependsOnGroups = {"cm.create.*", "cm.read.*", "cm.update.*", "cm.search.*"},
            alwaysRun = true)
    void purgeTerm() throws AtlanException {
        GlossaryTest.deleteTerm(client, term.getGuid());
        GlossaryTest.deleteGlossary(client, glossary.getGuid());
    }

    @Test(
            groups = {"cm.purge.groups"},
            dependsOnGroups = {"cm.purge.term", "table.cleanup"},
            alwaysRun = true)
    void purgeGroups() throws AtlanException {
        AdminTest.deleteGroup(client, group1.getId());
        AdminTest.deleteGroup(client, group2.getId());
    }

    @Test(
            groups = {"cm.purge.badges"},
            dependsOnGroups = {"cm.purge.term"},
            alwaysRun = true)
    void purgeBadges() throws AtlanException {
        client.assets
                .delete(
                        List.of(badge1.getGuid(), badge2.getGuid(), badge3.getGuid(), badge4.getGuid()),
                        AtlanDeleteType.PURGE)
                .block();
    }

    @Test(
            groups = {"cm.purge.cm"},
            dependsOnGroups = {"table.cleanup", "cm.purge.badges"},
            alwaysRun = true)
    void purgeCustomMetadata() throws AtlanException {
        CustomMetadataDef.purge(client, CM_RACI);
        CustomMetadataDef.purge(client, CM_IPR);
        CustomMetadataDef.purge(client, CM_QUALITY);
    }

    @Test(
            groups = {"cm.purge.cm.enum"},
            dependsOnGroups = {"cm.purge.cm"},
            alwaysRun = true)
    void purgeCustomMetadataEnums() throws AtlanException {
        EnumDef.purge(client, CM_ENUM_DQ_TYPE);
    }

    private void validateRACIAttributes(CustomMetadataAttributes cma) {
        assertNotNull(cma);
        validateRACIAttributes(cma.getAttributes());
    }

    private void validateRACIAttributes(Map<String, Object> attributes) {
        assertNotNull(attributes);
        assertEquals(attributes.get(CM_ATTR_RACI_RESPONSIBLE), Set.of(FIXED_USER));
        assertEquals(attributes.get(CM_ATTR_RACI_ACCOUNTABLE), FIXED_USER);
        assertEquals(attributes.get(CM_ATTR_RACI_CONSULTED), Set.of(group1.getName()));
        assertEquals(attributes.get(CM_ATTR_RACI_INFORMED), Set.of(group1.getName(), group2.getName()));
    }

    private void validateRACIAttributesReplacement(CustomMetadataAttributes cma) {
        assertNotNull(cma);
        validateRACIAttributesReplacement(cma.getAttributes());
    }

    private void validateRACIAttributesReplacement(Map<String, Object> attributes) {
        assertNotNull(attributes);
        assertEquals(attributes.get(CM_ATTR_RACI_RESPONSIBLE), Set.of(FIXED_USER));
        assertEquals(attributes.get(CM_ATTR_RACI_ACCOUNTABLE), FIXED_USER);
        assertNull(attributes.get(CM_ATTR_RACI_CONSULTED));
        assertEquals(attributes.get(CM_ATTR_RACI_INFORMED), Set.of(group1.getName(), group2.getName()));
    }

    private void validateIPRAttributes(CustomMetadataAttributes cma) {
        validateIPRAttributes(cma, true);
    }

    private void validateIPRAttributes(CustomMetadataAttributes cma, boolean value) {
        assertNotNull(cma);
        validateIPRAttributes(cma.getAttributes(), value);
    }

    private void validateIPRAttributes(Map<String, Object> attributes, boolean value) {
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

    private void validateDQAttributes(Map<String, Object> attributes) {
        assertNotNull(attributes);
        assertEquals(attributes.size(), 3);
        assertEquals(attributes.get(CM_ATTR_QUALITY_COUNT), 42L);
        assertEquals(attributes.get(CM_ATTR_QUALITY_SQL), "SELECT * from SOMEWHERE;");
        assertEquals(attributes.get(CM_ATTR_QUALITY_TYPE), "Completeness");
    }

    private AttributeDef validateRACIStructure(List<AttributeDef> list, int totalExpected) {
        assertNotNull(list);
        assertEquals(list.size(), totalExpected);
        AttributeDef one = list.get(0);
        assertEquals(one.getDisplayName(), CM_ATTR_RACI_RESPONSIBLE);
        assertNotEquals(one.getName(), CM_ATTR_RACI_RESPONSIBLE);
        assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
        assertEquals(one.getCardinality(), AtlanCustomAttributeCardinality.SET);
        assertTrue(one.getOptions().getApplicableAssetTypes().contains(Database.TYPE_NAME));
        assertFalse(one.isArchived());
        assertTrue(one.getOptions().getMultiValueSelect());
        assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.USERS.getValue());
        one = list.get(1);
        assertEquals(one.getDisplayName(), CM_ATTR_RACI_ACCOUNTABLE);
        assertNotEquals(one.getName(), CM_ATTR_RACI_ACCOUNTABLE);
        assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
        assertEquals(one.getCardinality(), AtlanCustomAttributeCardinality.SINGLE);
        assertTrue(one.getOptions().getApplicableAssetTypes().contains(Table.TYPE_NAME));
        assertFalse(one.isArchived());
        assertFalse(one.getOptions().getMultiValueSelect());
        assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.USERS.getValue());
        one = list.get(2);
        assertEquals(one.getDisplayName(), CM_ATTR_RACI_CONSULTED);
        assertNotEquals(one.getName(), CM_ATTR_RACI_CONSULTED);
        assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
        assertEquals(one.getCardinality(), AtlanCustomAttributeCardinality.SET);
        assertTrue(one.getOptions().getApplicableAssetTypes().contains(Column.TYPE_NAME));
        assertFalse(one.isArchived());
        assertTrue(one.getOptions().getMultiValueSelect());
        assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.GROUPS.getValue());
        one = list.get(3);
        assertEquals(one.getDisplayName(), CM_ATTR_RACI_INFORMED);
        assertNotEquals(one.getName(), CM_ATTR_RACI_INFORMED);
        assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
        assertEquals(one.getCardinality(), AtlanCustomAttributeCardinality.SET);
        assertTrue(one.getOptions().getApplicableAssetTypes().contains(MaterializedView.TYPE_NAME));
        assertFalse(one.isArchived());
        assertTrue(one.getOptions().getMultiValueSelect());
        assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.GROUPS.getValue());
        if (totalExpected > 5) {
            // If we're expecting more than 5, then the penultimate must be an archived CM_ATTR_EXTRA
            one = list.get(4);
            assertEquals(one.getDisplayName(), CM_ATTR_RACI_EXTRA + "-archived-" + removalEpoch);
            assertNotEquals(one.getName(), CM_ATTR_RACI_EXTRA);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertTrue(one.getOptions().getApplicableGlossaryTypes().contains(GlossaryTerm.TYPE_NAME));
            assertFalse(one.getOptions().getMultiValueSelect());
            assertTrue(one.isArchived());
        }
        if (totalExpected > 4) {
            return list.get(totalExpected - 1);
        }
        return null;
    }

    private void validateAudits(List<EntityAudit> audits) {
        assertNotNull(audits);
        assertTrue(audits.size() >= 14);

        ListIterator<EntityAudit> iterator = audits.listIterator(audits.size());

        // First one in the (reversed) list should always be the creation of the entity
        EntityAudit next = iterator.previous();
        assertNotNull(next);
        assertEquals(next.getAction(), AuditActionType.ENTITY_CREATE);
        AuditDetail detail = next.getDetail();
        assertTrue(detail instanceof GlossaryTerm);
        GlossaryTerm t = (GlossaryTerm) detail;
        assertEquals(t.getGuid(), term.getGuid());
        assertEquals(t.getQualifiedName(), term.getQualifiedName());
        assertEquals(t.getName(), term.getName());
        assertNotNull(t.getAnchor());
        assertEquals(t.getAnchor().getGuid(), glossary.getGuid());

        // Then adding RACI
        next = consumeEmptyUpdates(iterator);
        assertNotNull(next);
        assertEquals(next.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = next.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        CustomMetadataAttributesAuditDetail cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CM_RACI);
        validateRACIAttributes(cmad.getAttributes());

        // Then adding IPR
        next = consumeEmptyUpdates(iterator);
        assertNotNull(next);
        assertEquals(next.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = next.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CM_IPR);
        validateIPRAttributes(cmad.getAttributes(), true);

        // Then adding DQ
        next = consumeEmptyUpdates(iterator);
        assertNotNull(next);
        assertEquals(next.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = next.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CM_QUALITY);
        validateDQAttributes(cmad.getAttributes());

        // Then updating IPR
        next = consumeEmptyUpdates(iterator);
        assertNotNull(next);
        assertEquals(next.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = next.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CM_IPR);
        Map<String, Object> attributes = cmad.getAttributes();
        assertNotNull(attributes);
        assertEquals(attributes.size(), 1);
        assertTrue(attributes.containsKey(CM_ATTR_IPR_MANDATORY));
        assertEquals(attributes.get(CM_ATTR_IPR_MANDATORY), false);

        // Then replacing RACI
        detail = consumeOtherCMUpdates(iterator, CM_RACI);
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CM_RACI);
        attributes = cmad.getAttributes();
        assertEquals(attributes.size(), 1);
        assertNull(attributes.get(CM_ATTR_RACI_RESPONSIBLE));
        assertNull(attributes.get(CM_ATTR_RACI_ACCOUNTABLE));
        assertNull(attributes.get(CM_ATTR_RACI_CONSULTED));
        assertNull(attributes.get(CM_ATTR_RACI_INFORMED));

        // Then replacing IPR (with nothing, so removing it)
        detail = consumeOtherCMUpdates(iterator, CM_IPR);
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CM_IPR);
        attributes = cmad.getAttributes();
        assertEquals(attributes.size(), 5);
        assertNull(attributes.get(CM_ATTR_IPR_MANDATORY));
        assertNull(attributes.get(CM_ATTR_IPR_URL));
        assertNull(attributes.get(CM_ATTR_IPR_DATE));
        assertNull(attributes.get(CM_ATTR_IPR_LICENSE));
        assertNull(attributes.get(CM_ATTR_IPR_VERSION));

        // Then removing RACI
        detail = consumeOtherCMUpdates(iterator, CM_RACI);
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CM_RACI);
        attributes = cmad.getAttributes();
        assertEquals(attributes.size(), 4);
        assertNull(attributes.get(CM_ATTR_RACI_RESPONSIBLE));
        assertNull(attributes.get(CM_ATTR_RACI_ACCOUNTABLE));
        assertNull(attributes.get(CM_ATTR_RACI_CONSULTED));
        assertNull(attributes.get(CM_ATTR_RACI_INFORMED));

        // Note: no entry for removing IPR again, because it was already removed (above)

        // Then removing DQ
        detail = consumeOtherCMUpdates(iterator, CM_QUALITY);
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CM_QUALITY);
        attributes = cmad.getAttributes();
        assertEquals(attributes.size(), 3);
        assertNull(attributes.get(CM_ATTR_QUALITY_SQL));
        assertNull(attributes.get(CM_ATTR_QUALITY_COUNT));
        assertNull(attributes.get(CM_ATTR_QUALITY_TYPE));

        // Then update RACI again (with new attribute)
        detail = consumeOtherCMUpdates(iterator, CM_RACI);
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CM_RACI);
        attributes = cmad.getAttributes();
        assertEquals(attributes.size(), 5);
        validateRACIAttributes(attributes);
        assertTrue(attributes.containsKey(CM_ATTR_RACI_EXTRA));
        assertEquals(attributes.get(CM_ATTR_RACI_EXTRA), "something extra...");

        // Then remove all custom metadata (replace)
        detail = consumeOtherCMUpdates(iterator, CM_RACI);
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertNotNull(cmad);
        assertEquals(cmad.getTypeName(), CM_RACI);
        attributes = cmad.getAttributes();
        assertEquals(attributes.size(), 5);
        assertNull(attributes.get(CM_ATTR_RACI_RESPONSIBLE));
        assertNull(attributes.get(CM_ATTR_RACI_ACCOUNTABLE));
        assertNull(attributes.get(CM_ATTR_RACI_CONSULTED));
        assertNull(attributes.get(CM_ATTR_RACI_INFORMED));
        assertNull(attributes.get(CM_ATTR_RACI_EXTRA));
    }

    /**
     * Drop two (known) cases where there seem to be (spurious) empty updates:
     * - ENTITY_UPDATE actions
     * - CUSTOM_METADATA_UPDATE actions, where the details are just a GlossaryTerm as a whole (not the custom metadata)
     *
     * @param audits iterator over the audit log
     * @return the next non-empty update in the log
     */
    private EntityAudit consumeEmptyUpdates(ListIterator<EntityAudit> audits) {
        EntityAudit next = audits.previous();
        while (next != null) {
            AuditActionType action = next.getAction();
            if (action.equals(AuditActionType.ENTITY_UPDATE)
                    || (action.equals(AuditActionType.CUSTOM_METADATA_UPDATE)
                            && next.getDetail() instanceof GlossaryTerm)) {
                assertTrue(next.getDetail() instanceof GlossaryTerm);
                next = audits.previous();
            } else {
                return next;
            }
        }
        return null;
    }

    /**
     * Drop any custom metadata updates to CM that is NOT the CM we're looking for.
     * (It seems unrelated CM entries can also be updated in certain operations (like replaces),
     * and can therefore be ignored.)
     *
     * @param audits iterator over the audit log
     * @param cmToFind custom metadata on which to stop skipping
     * @return the next update in the log for the specific CM we're looking for
     */
    private AuditDetail consumeOtherCMUpdates(ListIterator<EntityAudit> audits, String cmToFind) {
        EntityAudit next = consumeEmptyUpdates(audits);
        while (next != null) {
            assertEquals(next.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
            AuditDetail detail = next.getDetail();
            if (detail instanceof GlossaryTerm
                    || (detail instanceof CustomMetadataAttributesAuditDetail
                            && detail.getTypeName().equals(cmToFind))) {
                return detail;
            } else {
                next = consumeEmptyUpdates(audits);
            }
        }
        return null;
    }

    @Test(
            groups = {"table.create"},
            dependsOnGroups = {"cm.create.cm.raci", "cm.create.groups"})
    public void createTable() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(client, CONNECTION_NAME, CONNECTOR_TYPE);
        database = SQLAssetTest.createDatabase(client, DATABASE_NAME, connection.getQualifiedName());
        schema = SQLAssetTest.createSchema(client, SCHEMA_NAME, database);
        CM_RACI_RESPONSIBLE = new CustomMetadataField(client, CM_RACI, CM_ATTR_RACI_RESPONSIBLE);
        CM_RACI_ACCOUNTABLE = new CustomMetadataField(client, CM_RACI, CM_ATTR_RACI_ACCOUNTABLE);
        CM_RACI_INFORMED = new CustomMetadataField(client, CM_RACI, CM_ATTR_RACI_INFORMED);
        table = Table._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(TABLE_NAME)
                .qualifiedName(Table.generateQualifiedName(TABLE_NAME, schema.getQualifiedName()))
                .connectorType(CONNECTOR_TYPE)
                .schemaName(SCHEMA_NAME)
                .schemaQualifiedName(schema.getQualifiedName())
                .schema(Schema.refByQualifiedName(schema.getQualifiedName()))
                .databaseName(DATABASE_NAME)
                .databaseQualifiedName(database.getQualifiedName())
                .connectionQualifiedName(connection.getQualifiedName())
                .customMetadata(
                        CM_RACI,
                        CustomMetadataAttributes.builder()
                                .attribute(CM_ATTR_RACI_RESPONSIBLE, List.of(FIXED_USER, "user2"))
                                .attribute(CM_ATTR_RACI_ACCOUNTABLE, FIXED_USER)
                                .attribute(CM_ATTR_RACI_INFORMED, List.of(group1.getName(), group2.getName()))
                                .build())
                .columnCount(2L)
                .build();
        AssetMutationResponse response = table.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof Schema);
        Schema updated = (Schema) one;
        assertEquals(updated.getQualifiedName(), schema.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof Table);
        table = (Table) one;
        assertNotNull(table.getGuid());
        assertNotNull(table.getQualifiedName());
        assertEquals(table.getName(), TABLE_NAME);
        assertEquals(table.getSchemaQualifiedName(), schema.getQualifiedName());
    }

    @Test(
            groups = {"table.search.business.attributes"},
            dependsOnGroups = {"table.search.consistent"})
    void searchBusinessAttributes() throws AtlanException {
        Set<AtlanField> fields = Set.of(CM_RACI_RESPONSIBLE, CM_RACI_ACCOUNTABLE, CM_RACI_INFORMED);
        IndexSearchRequest request = Table.select(client)
                .where(Table.QUALIFIED_NAME.eq(table.getQualifiedName()))
                .includesOnResults(fields)
                .toRequest();
        IndexSearchResponse response = request.search(client);
        assertNotNull(response);
        assertEquals(response.getApproximateCount(), 1L);
        List<Asset> results = response.getAssets();
        assertEquals(results.size(), 1);
        Table found = (Table) results.get(0);
        Map<String, CustomMetadataAttributes> sets = found.getCustomMetadataSets();
        assertNotNull(sets);
        assertEquals(sets.size(), 1);
        CustomMetadataAttributes attrs = sets.get(CM_RACI);
        assertNotNull(attrs);
        Map<String, Object> attributes = attrs.getAttributes();
        assertNotNull(attributes);
        assertEquals(attributes.get(CM_ATTR_RACI_RESPONSIBLE), Set.of(FIXED_USER, "user2"));
        assertEquals(attributes.get(CM_ATTR_RACI_ACCOUNTABLE), FIXED_USER);
        assertEquals(attributes.get(CM_ATTR_RACI_INFORMED), Set.of(group1.getName(), group2.getName()));
    }

    @Test(
            groups = {"table.search.consistent"},
            dependsOnGroups = {"table.create"})
    void waitForConsistency() throws AtlanException, InterruptedException {
        IndexSearchRequest index = Table.select(client)
                .where(Table.QUALIFIED_NAME.eq(table.getQualifiedName()))
                .toRequest();
        IndexSearchResponse response = retrySearchUntil(index, 1L);
        assertEquals(response.getApproximateCount(), 1L);
    }

    @Test(
            groups = {"table.cleanup"},
            dependsOnGroups = {
                "table.create",
                "table.search.consistent",
                "table.search.business.attributes",
            },
            alwaysRun = true)
    void purgeTableData() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(client, connection.getQualifiedName(), log);
        deleteAndValidateAssets(
                client, List.of(schema.getGuid(), database.getGuid(), connection.getGuid(), table.getGuid()));
    }

    private void deleteAndValidateAssets(AtlanClient client, List<String> guids) throws AtlanException {
        for (String guid : guids) {
            if (Strings.isNullOrEmpty(guid)) {
                continue;
            }
            AssetMutationResponse response = Asset.delete(client, guid);
            assertNotNull(response);
            assertEquals(response.getCreatedAssets().size(), 0);
            assertEquals(response.getUpdatedAssets().size(), 0);
            List<Asset> entities = response.getDeletedAssets();
            assertNotNull(entities);
        }
    }
}
