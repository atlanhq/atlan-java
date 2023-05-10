/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static com.atlan.util.QueryFactory.*;
import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.Atlan;
import com.atlan.cache.CustomMetadataCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.AtlanGroup;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.model.enums.*;
import com.atlan.model.search.*;
import com.atlan.model.structs.BadgeCondition;
import com.atlan.model.typedefs.*;
import com.atlan.net.HttpClient;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Test management of custom metadata.
 */
@Slf4j
public class CustomMetadataTest extends AtlanLiveTest {

    private static final String PREFIX = AtlanLiveTest.PREFIX + "CM";

    // TODO: This cannot be dynamic because a user must first be verified
    //  before they can be linked — so we must use a hard-coded value for
    //  a username that we know is verified and active in the environment
    public static final String FIXED_USER = "chris";

    private static final String CM_RACI = AtlanLiveTest.PREFIX + "RACI";
    private static final String CM_IPR = AtlanLiveTest.PREFIX + "IPR";
    private static final String CM_QUALITY = AtlanLiveTest.PREFIX + "DQ";

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
    private static final String CM_ENUM_DQ_TYPE = AtlanLiveTest.PREFIX.replace("-", "_") + "DataQualityType";
    private static final List<String> DQ_TYPE_LIST =
            List.of("Accuracy", "Completeness", "Consistency", "Timeliness", "Validity", "Uniqueness");

    private static final String GROUP_NAME1 = PREFIX + "1";
    private static final String GROUP_NAME2 = PREFIX + "2";

    private static Glossary glossary = null;
    private static GlossaryTerm term = null;
    private static AtlanGroup group1 = null;
    private static AtlanGroup group2 = null;
    private static Badge badge = null;
    private static long removalEpoch;

    @Test(groups = {"cm.create.cm.ipr"})
    void createCustomMetadataIPR() throws AtlanException {
        CustomMetadataDef customMetadataDef = CustomMetadataDef.creator(CM_IPR)
                .attributeDef(
                        AttributeDef.of(CM_ATTR_IPR_LICENSE, AtlanCustomAttributePrimitiveType.STRING, null, false))
                .attributeDef(
                        AttributeDef.of(CM_ATTR_IPR_VERSION, AtlanCustomAttributePrimitiveType.DECIMAL, null, false))
                .attributeDef(
                        AttributeDef.of(CM_ATTR_IPR_MANDATORY, AtlanCustomAttributePrimitiveType.BOOLEAN, null, false))
                .attributeDef(AttributeDef.of(CM_ATTR_IPR_DATE, AtlanCustomAttributePrimitiveType.DATE, null, false))
                .attributeDef(AttributeDef.of(CM_ATTR_IPR_URL, AtlanCustomAttributePrimitiveType.URL, null, false))
                .options(CustomMetadataOptions.builder()
                        .logoType("emoji")
                        .emoji("⚖️")
                        .build())
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
    }

    @Test(groups = {"cm.create.cm.raci"})
    void createCustomMetadataRACI() throws AtlanException {
        CustomMetadataDef customMetadataDef = CustomMetadataDef.creator(CM_RACI)
                .attributeDef(
                        AttributeDef.of(CM_ATTR_RACI_RESPONSIBLE, AtlanCustomAttributePrimitiveType.USERS, null, true))
                .attributeDef(
                        AttributeDef.of(CM_ATTR_RACI_ACCOUNTABLE, AtlanCustomAttributePrimitiveType.USERS, null, false))
                .attributeDef(
                        AttributeDef.of(CM_ATTR_RACI_CONSULTED, AtlanCustomAttributePrimitiveType.GROUPS, null, true))
                .attributeDef(
                        AttributeDef.of(CM_ATTR_RACI_INFORMED, AtlanCustomAttributePrimitiveType.GROUPS, null, true))
                .attributeDef(
                        AttributeDef.of(CM_ATTR_RACI_EXTRA, AtlanCustomAttributePrimitiveType.STRING, null, false))
                .options(CustomMetadataOptions.builder()
                        .logoType("emoji")
                        .emoji("\uD83D\uDC6A")
                        .build())
                .build();
        CustomMetadataDef response = customMetadataDef.create();
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
    }

    @Test(groups = {"cm.create.cm.dq"})
    void createCustomMetadataDQ() throws AtlanException {
        EnumDef enumDef = EnumDef.creator(CM_ENUM_DQ_TYPE, DQ_TYPE_LIST).build();
        EnumDef resp = enumDef.create();
        assertNotNull(resp);
        assertEquals(resp.getCategory(), AtlanTypeCategory.ENUM);
        assertNotNull(resp.getName());
        assertEquals(resp.getName(), CM_ENUM_DQ_TYPE);
        assertNotNull(resp.getGuid());
        assertNotNull(resp.getElementDefs());
        assertEquals(resp.getElementDefs().size(), DQ_TYPE_LIST.size());

        CustomMetadataDef customMetadataDef = CustomMetadataDef.creator(CM_QUALITY)
                .attributeDef(
                        AttributeDef.of(CM_ATTR_QUALITY_COUNT, AtlanCustomAttributePrimitiveType.INTEGER, null, false))
                .attributeDef(AttributeDef.of(CM_ATTR_QUALITY_SQL, AtlanCustomAttributePrimitiveType.SQL, null, false))
                .attributeDef(AttributeDef.of(
                        CM_ATTR_QUALITY_TYPE, AtlanCustomAttributePrimitiveType.OPTIONS, CM_ENUM_DQ_TYPE, false))
                .options(CustomMetadataOptions.builder()
                        .logoType("emoji")
                        .emoji("\uD83D\uDD16")
                        .build())
                .build();
        CustomMetadataDef response = customMetadataDef.create();
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
            groups = {"cm.create.badges"},
            dependsOnGroups = {"cm.create.cm.dq"})
    void createBadges() throws AtlanException {
        Badge toCreate = Badge.creator(CM_ATTR_QUALITY_COUNT, CM_QUALITY, CM_ATTR_QUALITY_COUNT)
                .userDescription("How many data quality checks ran against this asset.")
                .badgeCondition(BadgeCondition.of(BadgeComparisonOperator.GTE, "5", BadgeConditionColor.GREEN))
                .badgeCondition(BadgeCondition.of(BadgeComparisonOperator.LT, "5", BadgeConditionColor.YELLOW))
                .badgeCondition(BadgeCondition.of(BadgeComparisonOperator.LTE, "2", BadgeConditionColor.RED))
                .build();
        AssetMutationResponse response = toCreate.upsert();
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 1);
        assertTrue(response.getCreatedAssets().get(0) instanceof Badge);
        badge = (Badge) response.getCreatedAssets().get(0);
        assertNotNull(badge.getGuid());
    }

    @Test(groups = {"cm.create.term"})
    void createTerm() throws AtlanException {
        glossary = GlossaryTest.createGlossary(PREFIX);
        term = GlossaryTest.createTerm(PREFIX, glossary.getGuid());
    }

    @Test(groups = {"cm.create.groups"})
    void createGroups() throws AtlanException {
        group1 = AdminTest.createGroup(GROUP_NAME1);
        group2 = AdminTest.createGroup(GROUP_NAME2);
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
        GlossaryTerm.updateCustomMetadataAttributes(term.getGuid(), CM_RACI, cm);
        GlossaryTerm t = GlossaryTerm.retrieveByGuid(term.getGuid());
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
        GlossaryTerm.updateCustomMetadataAttributes(term.getGuid(), CM_IPR, cm);
        GlossaryTerm t = GlossaryTerm.retrieveByGuid(term.getGuid());
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
        GlossaryTerm.updateCustomMetadataAttributes(term.getGuid(), CM_QUALITY, cm);
        GlossaryTerm t = GlossaryTerm.retrieveByGuid(term.getGuid());
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
        GlossaryTerm.updateCustomMetadataAttributes(term.getGuid(), CM_IPR, cm);
        GlossaryTerm t = GlossaryTerm.retrieveByQualifiedName(term.getQualifiedName());
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
        GlossaryTerm.replaceCustomMetadata(term.getGuid(), CM_RACI, cm);
        GlossaryTerm t = GlossaryTerm.retrieveByGuid(term.getGuid());
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
        GlossaryTerm.replaceCustomMetadata(term.getGuid(), CM_IPR, null);
        GlossaryTerm t = GlossaryTerm.retrieveByGuid(term.getGuid());
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

        Query combined = CompoundQuery.builder()
                .must(beActive())
                .must(beOfType(GlossaryTerm.TYPE_NAME))
                .must(haveCM(CM_RACI, CM_ATTR_RACI_ACCOUNTABLE).present())
                .build()
                ._toQuery();

        IndexSearchRequest index = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder().query(combined).build())
                .attribute("name")
                .attribute("anchor")
                .relationAttribute("name")
                .build();

        IndexSearchResponse response = index.search();

        assertNotNull(response);
        int count = 0;
        while (response.getApproximateCount() == 0L && count < Atlan.getMaxNetworkRetries()) {
            Thread.sleep(HttpClient.waitTime(count).toMillis());
            response = index.search();
            count++;
        }
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
        Glossary anchor = t.getAnchor();
        assertNotNull(anchor);
        assertEquals(anchor.getName(), glossary.getName());
    }

    @Test(
            groups = {"cm.search.term.cm"},
            dependsOnGroups = {"cm.update.term.replace.ipr"})
    void searchBySpecificAccountable() throws AtlanException, InterruptedException {

        Query combined = CompoundQuery.builder()
                .must(beActive())
                .must(beOfType(GlossaryTerm.TYPE_NAME))
                .must(haveCM(CM_RACI, CM_ATTR_RACI_ACCOUNTABLE).eq(FIXED_USER))
                .build()
                ._toQuery();

        IndexSearchRequest index = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder().query(combined).build())
                .attribute("name")
                .attribute("anchor")
                .relationAttribute("name")
                .build();

        IndexSearchResponse response = index.search();

        assertNotNull(response);
        int count = 0;
        while (response.getApproximateCount() == 0L && count < Atlan.getMaxNetworkRetries()) {
            Thread.sleep(HttpClient.waitTime(count).toMillis());
            response = index.search();
            count++;
        }
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
        Glossary anchor = t.getAnchor();
        assertNotNull(anchor);
        assertEquals(anchor.getName(), glossary.getName());
    }

    @Test(
            groups = {"cm.update.term.remove.raci"},
            dependsOnGroups = {"cm.search.term.cm"})
    void removeTermCMRACI() throws AtlanException {
        GlossaryTerm.removeCustomMetadata(term.getGuid(), CM_RACI);
        GlossaryTerm t = GlossaryTerm.retrieveByGuid(term.getGuid());
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
        GlossaryTerm.removeCustomMetadata(term.getGuid(), CM_IPR);
        GlossaryTerm t = GlossaryTerm.retrieveByQualifiedName(term.getQualifiedName());
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
        GlossaryTerm.removeCustomMetadata(term.getGuid(), CM_QUALITY);
        GlossaryTerm t = GlossaryTerm.retrieveByGuid(term.getGuid());
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
        CustomMetadataDef existing = CustomMetadataCache.getCustomMetadataDef(CM_RACI);
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
        CustomMetadataDef updated = existing.update();
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
        assertFalse(extra.getOptions().getMultiValueSelect());
        assertTrue(extra.isArchived());
    }

    @Test(
            groups = {"cm.update.cm.attribute.2"},
            dependsOnGroups = {"cm.read.cm.structure.1"})
    void recreateAttribute() throws AtlanException {
        CustomMetadataDef existing = CustomMetadataCache.getCustomMetadataDef(CM_RACI);
        List<AttributeDef> existingAttrs = existing.getAttributeDefs();
        List<AttributeDef> updatedAttrs = new ArrayList<>();
        for (AttributeDef attributeDef : existingAttrs) {
            updatedAttrs.add(attributeDef.toBuilder().isNew(null).build());
        }
        updatedAttrs.add(AttributeDef.of(CM_ATTR_RACI_EXTRA, AtlanCustomAttributePrimitiveType.STRING, null, false));
        existing = existing.toBuilder()
                .clearAttributeDefs()
                .attributeDefs(updatedAttrs)
                .build();
        CustomMetadataDef updated = existing.update();
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
        AssetMutationResponse response = toUpdate.upsertReplacingCM(false);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertTrue(response.getCreatedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof GlossaryTerm);
        GlossaryTerm t = (GlossaryTerm) one;
        assertEquals(t.getQualifiedName(), term.getQualifiedName());
        t = GlossaryTerm.retrieveByQualifiedName(term.getQualifiedName());
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
        AssetMutationResponse response = toUpdate.upsertReplacingCM(false);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertTrue(response.getCreatedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof GlossaryTerm);
        GlossaryTerm t = (GlossaryTerm) one;
        assertEquals(t.getQualifiedName(), term.getQualifiedName());
        t = GlossaryTerm.retrieveByQualifiedName(term.getQualifiedName());
        assertNotNull(t);
        assertTrue(t.isComplete());
        assertEquals(t.getQualifiedName(), term.getQualifiedName());
        assertTrue(t.getCustomMetadataSets().isEmpty());
    }

    @Test(
            groups = {"cm.read.term.audit"},
            dependsOnGroups = {"cm.update.term.remove.cm"})
    void readTermAuditByGuid() throws AtlanException {
        AuditSearchRequest request =
                AuditSearchRequest.byGuid(term.getGuid(), 40).build();
        AuditSearchResponse response = request.search();
        assertNotNull(response);
        validateAudits(response.getEntityAudits());
    }

    @Test(
            groups = {"cm.read.term.audit"},
            dependsOnGroups = {"cm.update.term.remove.cm"})
    void readTermAuditByQN() throws AtlanException {
        AuditSearchRequest request = AuditSearchRequest.byQualifiedName(
                        GlossaryTerm.TYPE_NAME, term.getQualifiedName(), 40)
                .build();
        AuditSearchResponse response = request.search();
        assertNotNull(response);
        validateAudits(response.getEntityAudits());
    }

    @Test(
            groups = {"cm.purge.term"},
            dependsOnGroups = {"cm.create.*", "cm.read.*", "cm.update.*", "cm.search.*"},
            alwaysRun = true)
    void purgeTerm() throws AtlanException {
        GlossaryTest.deleteTerm(term.getGuid());
        GlossaryTest.deleteGlossary(glossary.getGuid());
    }

    @Test(
            groups = {"cm.purge.groups"},
            dependsOnGroups = {"cm.purge.term"},
            alwaysRun = true)
    void purgeGroups() throws AtlanException {
        AdminTest.deleteGroup(group1.getId());
        AdminTest.deleteGroup(group2.getId());
    }

    @Test(
            groups = {"cm.purge.badges"},
            dependsOnGroups = {"cm.purge.term"},
            alwaysRun = true)
    void purgeBadges() throws AtlanException {
        Badge.purge(badge.getGuid());
    }

    @Test(
            groups = {"cm.purge.cm"},
            dependsOnGroups = {"cm.purge.badges"},
            alwaysRun = true)
    void purgeCustomMetadata() throws AtlanException {
        CustomMetadataDef.purge(CM_RACI);
        CustomMetadataDef.purge(CM_IPR);
        CustomMetadataDef.purge(CM_QUALITY);
    }

    @Test(
            groups = {"cm.purge.cm.enum"},
            dependsOnGroups = {"cm.purge.cm"},
            alwaysRun = true)
    void purgeCustomMetadataEnums() throws AtlanException {
        EnumDef.purge(CM_ENUM_DQ_TYPE);
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

    private void validateIPRAttributesReplacement(CustomMetadataAttributes cma) {
        assertNotNull(cma);
        validateIPRAttributesReplacement(cma.getAttributes());
    }

    private void validateIPRAttributesReplacement(Map<String, Object> attributes) {
        assertNotNull(attributes);
        assertTrue(attributes.isEmpty());
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
        assertTrue(one.getOptions().getCustomApplicableEntityTypes().contains(Database.TYPE_NAME));
        assertFalse(one.isArchived());
        assertTrue(one.getOptions().getMultiValueSelect());
        assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.USERS.getValue());
        one = list.get(1);
        assertEquals(one.getDisplayName(), CM_ATTR_RACI_ACCOUNTABLE);
        assertNotEquals(one.getName(), CM_ATTR_RACI_ACCOUNTABLE);
        assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
        assertTrue(one.getOptions().getCustomApplicableEntityTypes().contains(Table.TYPE_NAME));
        assertFalse(one.isArchived());
        assertFalse(one.getOptions().getMultiValueSelect());
        assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.USERS.getValue());
        one = list.get(2);
        assertEquals(one.getDisplayName(), CM_ATTR_RACI_CONSULTED);
        assertNotEquals(one.getName(), CM_ATTR_RACI_CONSULTED);
        assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
        assertTrue(one.getOptions().getCustomApplicableEntityTypes().contains(Column.TYPE_NAME));
        assertFalse(one.isArchived());
        assertTrue(one.getOptions().getMultiValueSelect());
        assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.GROUPS.getValue());
        one = list.get(3);
        assertEquals(one.getDisplayName(), CM_ATTR_RACI_INFORMED);
        assertNotEquals(one.getName(), CM_ATTR_RACI_INFORMED);
        assertEquals(one.getTypeName(), "array<" + AtlanCustomAttributePrimitiveType.STRING.getValue() + ">");
        assertTrue(one.getOptions().getCustomApplicableEntityTypes().contains(MaterializedView.TYPE_NAME));
        assertFalse(one.isArchived());
        assertTrue(one.getOptions().getMultiValueSelect());
        assertEquals(one.getOptions().getCustomType(), AtlanCustomAttributePrimitiveType.GROUPS.getValue());
        if (totalExpected > 5) {
            // If we're expecting more than 5, then the penultimate must be an archived CM_ATTR_EXTRA
            one = list.get(4);
            assertEquals(one.getDisplayName(), CM_ATTR_RACI_EXTRA + "-archived-" + removalEpoch);
            assertNotEquals(one.getName(), CM_ATTR_RACI_EXTRA);
            assertEquals(one.getTypeName(), AtlanCustomAttributePrimitiveType.STRING.getValue());
            assertTrue(one.getOptions().getCustomApplicableEntityTypes().contains(GlossaryTerm.TYPE_NAME));
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
        assertEquals(audits.size(), 13);

        EntityAudit one = audits.get(12);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_CREATE);
        AuditDetail detail = one.getDetail();
        assertTrue(detail instanceof GlossaryTerm);
        GlossaryTerm t = (GlossaryTerm) detail;
        assertEquals(t.getGuid(), term.getGuid());
        assertEquals(t.getQualifiedName(), term.getQualifiedName());
        assertEquals(t.getName(), term.getName());
        assertNotNull(t.getAnchor());
        assertEquals(t.getAnchor().getGuid(), glossary.getGuid());

        one = audits.get(11);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        CustomMetadataAttributesAuditDetail cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CM_RACI);
        validateRACIAttributes(cmad.getAttributes());

        one = audits.get(10);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CM_IPR);
        validateIPRAttributes(cmad.getAttributes(), true);

        one = audits.get(9);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CM_QUALITY);
        validateDQAttributes(cmad.getAttributes());

        one = audits.get(8);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CM_IPR);
        Map<String, Object> attributes = cmad.getAttributes();
        assertNotNull(attributes);
        assertEquals(attributes.size(), 1);
        assertTrue(attributes.containsKey(CM_ATTR_IPR_MANDATORY));
        assertEquals(attributes.get(CM_ATTR_IPR_MANDATORY), false);

        one = audits.get(7);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CM_RACI);
        attributes = cmad.getAttributes();
        assertEquals(attributes.size(), 1);
        assertNull(attributes.get(CM_ATTR_RACI_RESPONSIBLE));
        assertNull(attributes.get(CM_ATTR_RACI_ACCOUNTABLE));
        assertNull(attributes.get(CM_ATTR_RACI_CONSULTED));
        assertNull(attributes.get(CM_ATTR_RACI_INFORMED));

        one = audits.get(6);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = one.getDetail();
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

        one = audits.get(5);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CM_RACI);
        attributes = cmad.getAttributes();
        assertEquals(attributes.size(), 4);
        assertNull(attributes.get(CM_ATTR_RACI_RESPONSIBLE));
        assertNull(attributes.get(CM_ATTR_RACI_ACCOUNTABLE));
        assertNull(attributes.get(CM_ATTR_RACI_CONSULTED));
        assertNull(attributes.get(CM_ATTR_RACI_INFORMED));

        one = audits.get(4);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CM_QUALITY);
        attributes = cmad.getAttributes();
        assertEquals(attributes.size(), 3);
        assertNull(attributes.get(CM_ATTR_QUALITY_SQL));
        assertNull(attributes.get(CM_ATTR_QUALITY_COUNT));
        assertNull(attributes.get(CM_ATTR_QUALITY_TYPE));

        one = audits.get(3);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CM_RACI);
        attributes = cmad.getAttributes();
        assertEquals(attributes.size(), 5);
        validateRACIAttributes(attributes);
        assertTrue(attributes.containsKey(CM_ATTR_RACI_EXTRA));
        assertEquals(attributes.get(CM_ATTR_RACI_EXTRA), "something extra...");

        one = audits.get(2);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof GlossaryTerm);
        t = (GlossaryTerm) detail;
        assertEquals(t.getGuid(), term.getGuid());
        assertNotNull(t.getCustomMetadataSets());
        CustomMetadataAttributes cma = t.getCustomMetadataSets().get(CM_RACI);
        assertNotNull(cma);
        attributes = cma.getAttributes();
        assertEquals(attributes.size(), 5);
        validateRACIAttributes(attributes);
        assertTrue(attributes.containsKey(CM_ATTR_RACI_EXTRA));
        assertEquals(attributes.get(CM_ATTR_RACI_EXTRA), "something extra...");

        one = audits.get(1);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof CustomMetadataAttributesAuditDetail);
        cmad = (CustomMetadataAttributesAuditDetail) detail;
        assertEquals(cmad.getTypeName(), CM_RACI);
        attributes = cmad.getAttributes();
        assertEquals(attributes.size(), 5);
        assertNull(attributes.get(CM_ATTR_RACI_RESPONSIBLE));
        assertNull(attributes.get(CM_ATTR_RACI_ACCOUNTABLE));
        assertNull(attributes.get(CM_ATTR_RACI_CONSULTED));
        assertNull(attributes.get(CM_ATTR_RACI_INFORMED));
        assertNull(attributes.get(CM_ATTR_RACI_EXTRA));

        one = audits.get(0);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CUSTOM_METADATA_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof GlossaryTerm);
        t = (GlossaryTerm) detail;
        assertEquals(t.getGuid(), term.getGuid());
        assertTrue(
                t.getCustomMetadataSets() == null || t.getCustomMetadataSets().isEmpty());
    }
}
