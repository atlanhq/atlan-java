/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.model.admin.AtlanGroup;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.enums.*;
import com.atlan.model.search.*;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests all aspects of finding and applying suggestions.
 */
@Slf4j
@SuppressWarnings("deprecation")
public class SuggestionsTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("trident");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.TRINO;
    public static final String CONNECTION_NAME = PREFIX;

    public static final String SYSTEM_DESCRIPTION = DESCRIPTION + " (system)";

    public static final String DATABASE_NAME = PREFIX + "_db";
    public static final String SCHEMA_NAME1 = PREFIX + "_schema";
    public static final String SCHEMA_NAME2 = SCHEMA_NAME1 + "2";
    public static final String SCHEMA_NAME3 = SCHEMA_NAME1 + "3";
    public static final String TABLE_NAME = PREFIX + "_table";
    public static final String VIEW_NAME = PREFIX + "_view";
    public static final String COLUMN_NAME1 = PREFIX + "_col1";
    // public static final String COLUMN_NAME2 = PREFIX + "_col2";

    private static final String ATLAN_TAG_NAME1 = PREFIX + "1";
    private static final String ATLAN_TAG_NAME2 = PREFIX + "2";
    private static final String TERM_NAME1 = PREFIX + "1";
    private static final String TERM_NAME2 = PREFIX + "2";

    private static Connection connection = null;
    private static Database database = null;
    private static Schema schema1 = null;
    private static Schema schema2 = null;
    private static Schema schema3 = null;
    private static Table table1 = null;
    private static Table table2 = null;
    private static Table table3 = null;
    private static View view1 = null;
    private static View view2 = null;
    private static Column t1c1 = null;
    // private static Column t1c2 = null;
    private static Column t2c1 = null;
    // private static Column t2c2 = null;
    private static Column v1c1 = null;
    // private static Column v1c2 = null;
    private static Column v2c1 = null;
    // private static Column v2c2 = null;
    private static AtlanGroup ownerGroup = null;
    private static Glossary glossary = null;
    private static GlossaryTerm term1 = null;
    private static GlossaryTerm term2 = null;

    private static List<String> taggedAssetGuids = new ArrayList<>();

    @Test(groups = {"suggestions.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(client, CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"suggestions.search.connection"},
            dependsOnGroups = {"suggestions.create.connection"})
    void findConnection() throws AtlanException {
        List<Connection> results = Connection.findByName(client, CONNECTION_NAME, CONNECTOR_TYPE);
        assertNotNull(results);
        assertEquals(results.size(), 1);
        Connection one = results.get(0);
        assertEquals(one.getGuid(), connection.getGuid());
        assertEquals(one.getQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"suggestions.create.database"},
            dependsOnGroups = {"suggestions.create.connection"})
    void createDatabase() throws AtlanException {
        database = SQLAssetTest.createDatabase(client, DATABASE_NAME, connection.getQualifiedName());
        assertEquals(database.getConnectorType(), CONNECTOR_TYPE);
    }

    @Test(
            groups = {"suggestions.create.schema"},
            dependsOnGroups = {"suggestions.create.database"})
    void createSchemas() throws AtlanException {
        schema1 = SQLAssetTest.createSchema(client, SCHEMA_NAME1, database);
        assertEquals(schema1.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(schema1.getDatabaseName(), DATABASE_NAME);
        schema2 = SQLAssetTest.createSchema(client, SCHEMA_NAME2, database);
        assertEquals(schema2.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(schema2.getDatabaseName(), DATABASE_NAME);
        schema3 = SQLAssetTest.createSchema(client, SCHEMA_NAME3, database);
        assertEquals(schema3.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(schema3.getDatabaseName(), DATABASE_NAME);
    }

    @Test(
            groups = {"suggestions.create.table"},
            dependsOnGroups = {"suggestions.create.schema"})
    void createTables() throws AtlanException {
        table1 = SQLAssetTest.createTable(client, TABLE_NAME, schema1);
        assertEquals(table1.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(table1.getSchemaName(), SCHEMA_NAME1);
        assertEquals(table1.getDatabaseName(), DATABASE_NAME);
        assertEquals(table1.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(table1.getConnectionQualifiedName(), connection.getQualifiedName());
        table2 = SQLAssetTest.createTable(client, TABLE_NAME, schema2);
        assertEquals(table2.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(table2.getSchemaName(), SCHEMA_NAME2);
        assertEquals(table2.getDatabaseName(), DATABASE_NAME);
        assertEquals(table2.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(table2.getConnectionQualifiedName(), connection.getQualifiedName());
        table3 = SQLAssetTest.createTable(client, VIEW_NAME, schema3);
        assertEquals(table3.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(table3.getSchemaName(), SCHEMA_NAME3);
        assertEquals(table3.getDatabaseName(), DATABASE_NAME);
        assertEquals(table3.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(table3.getConnectionQualifiedName(), connection.getQualifiedName());
        taggedAssetGuids.add(table1.getGuid());
        taggedAssetGuids.add(table3.getGuid());
    }

    @Test(
            groups = {"suggestions.create.view"},
            dependsOnGroups = {"suggestions.create.table"})
    void createViews() throws AtlanException {
        view1 = SQLAssetTest.createView(client, VIEW_NAME, schema1);
        assertEquals(view1.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(view1.getSchemaName(), SCHEMA_NAME1);
        assertEquals(view1.getDatabaseName(), DATABASE_NAME);
        assertEquals(view1.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(view1.getConnectionQualifiedName(), connection.getQualifiedName());
        view2 = SQLAssetTest.createView(client, VIEW_NAME, schema2);
        assertEquals(view2.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(view2.getSchemaName(), SCHEMA_NAME2);
        assertEquals(view2.getDatabaseName(), DATABASE_NAME);
        assertEquals(view2.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(view2.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"suggestions.create.column.1"},
            dependsOnGroups = {"suggestions.create.table", "suggestions.create.view"})
    void createColumn1() throws AtlanException {
        t1c1 = SQLAssetTest.createColumn(client, COLUMN_NAME1, table1, 1);
        assertNotNull(t1c1);
        assertEquals(t1c1.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(t1c1.getTableName(), TABLE_NAME);
        assertEquals(t1c1.getSchemaName(), SCHEMA_NAME1);
        assertEquals(t1c1.getSchemaQualifiedName(), schema1.getQualifiedName());
        assertEquals(t1c1.getDatabaseName(), DATABASE_NAME);
        assertEquals(t1c1.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(t1c1.getConnectionQualifiedName(), connection.getQualifiedName());
        t2c1 = SQLAssetTest.createColumn(client, COLUMN_NAME1, table2, 1);
        assertNotNull(t2c1);
        assertEquals(t2c1.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(t2c1.getTableName(), TABLE_NAME);
        assertEquals(t2c1.getSchemaName(), SCHEMA_NAME2);
        assertEquals(t2c1.getSchemaQualifiedName(), schema2.getQualifiedName());
        assertEquals(t2c1.getDatabaseName(), DATABASE_NAME);
        assertEquals(t2c1.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(t2c1.getConnectionQualifiedName(), connection.getQualifiedName());
        v1c1 = SQLAssetTest.createColumn(client, COLUMN_NAME1, view1, 1);
        assertNotNull(v1c1);
        assertEquals(v1c1.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(v1c1.getViewName(), VIEW_NAME);
        assertEquals(v1c1.getSchemaName(), SCHEMA_NAME1);
        assertEquals(v1c1.getSchemaQualifiedName(), schema1.getQualifiedName());
        assertEquals(v1c1.getDatabaseName(), DATABASE_NAME);
        assertEquals(v1c1.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(v1c1.getConnectionQualifiedName(), connection.getQualifiedName());
        v2c1 = SQLAssetTest.createColumn(client, COLUMN_NAME1, view2, 1);
        assertNotNull(v2c1);
        assertEquals(v2c1.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(v2c1.getViewName(), VIEW_NAME);
        assertEquals(v2c1.getSchemaName(), SCHEMA_NAME2);
        assertEquals(v2c1.getSchemaQualifiedName(), schema2.getQualifiedName());
        assertEquals(v2c1.getDatabaseName(), DATABASE_NAME);
        assertEquals(v2c1.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(v2c1.getConnectionQualifiedName(), connection.getQualifiedName());
        taggedAssetGuids.add(t1c1.getGuid());
        taggedAssetGuids.add(v1c1.getGuid());
    }

    /*@Test(
        groups = {"suggestions.create.column.2"},
        dependsOnGroups = {"suggestions.create.column.1"})
    void createColumn2() throws AtlanException {
        t1c2 = SQLAssetTest.createColumn(COLUMN_NAME2, table1, 2);
        assertNotNull(t1c2);
        assertEquals(t1c2.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(t1c2.getTableName(), TABLE_NAME);
        assertEquals(t1c2.getSchemaName(), SCHEMA_NAME1);
        assertEquals(t1c2.getSchemaQualifiedName(), schema1.getQualifiedName());
        assertEquals(t1c2.getDatabaseName(), DATABASE_NAME);
        assertEquals(t1c2.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(t1c2.getConnectionQualifiedName(), connection.getQualifiedName());
        t2c2 = SQLAssetTest.createColumn(COLUMN_NAME2, table2, 2);
        assertNotNull(t2c2);
        assertEquals(t2c2.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(t2c2.getTableName(), TABLE_NAME);
        assertEquals(t2c2.getSchemaName(), SCHEMA_NAME2);
        assertEquals(t2c2.getSchemaQualifiedName(), schema2.getQualifiedName());
        assertEquals(t2c2.getDatabaseName(), DATABASE_NAME);
        assertEquals(t2c2.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(t2c2.getConnectionQualifiedName(), connection.getQualifiedName());
        v1c2 = SQLAssetTest.createColumn(COLUMN_NAME2, view1, 2);
        assertNotNull(v1c2);
        assertEquals(v1c2.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(v1c2.getViewName(), TABLE_NAME);
        assertEquals(v1c2.getSchemaName(), SCHEMA_NAME1);
        assertEquals(v1c2.getSchemaQualifiedName(), schema1.getQualifiedName());
        assertEquals(v1c2.getDatabaseName(), DATABASE_NAME);
        assertEquals(v1c2.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(v1c2.getConnectionQualifiedName(), connection.getQualifiedName());
        v2c2 = SQLAssetTest.createColumn(COLUMN_NAME2, view2, 2);
        assertNotNull(v2c2);
        assertEquals(v2c2.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(v2c2.getViewName(), TABLE_NAME);
        assertEquals(v2c2.getSchemaName(), SCHEMA_NAME2);
        assertEquals(v2c2.getSchemaQualifiedName(), schema2.getQualifiedName());
        assertEquals(v2c2.getDatabaseName(), DATABASE_NAME);
        assertEquals(v2c2.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(v2c2.getConnectionQualifiedName(), connection.getQualifiedName());
    }*/

    @Test(groups = {"suggestions.create.group.owners"})
    void createGroupOwners() throws AtlanException {
        ownerGroup = AdminTest.createGroup(client, PREFIX);
    }

    @Test(groups = {"suggestions.create.atlantags"})
    void createAtlanTags() throws AtlanException {
        AtlanTagTest.createAtlanTag(client, ATLAN_TAG_NAME1);
        AtlanTagTest.createAtlanTag(client, ATLAN_TAG_NAME2);
    }

    @Test(groups = {"suggestions.create.glossary"})
    void createGlossary() throws AtlanException {
        glossary = GlossaryTest.createGlossary(client, PREFIX);
        term1 = GlossaryTest.createTerm(client, TERM_NAME1, glossary);
        term2 = GlossaryTest.createTerm(client, TERM_NAME2, glossary);
    }

    @Test(
            groups = {"suggestions.update.column.t1"},
            dependsOnGroups = {
                "suggestions.create.table",
                "suggestions.create.group.owners",
                "suggestions.create.atlantags"
            })
    void updateT1() throws AtlanException {
        Table toUpdate = Table.updater(table1.getQualifiedName(), TABLE_NAME)
                .ownerGroup(ownerGroup.getName())
                .description(SYSTEM_DESCRIPTION)
                .userDescription(DESCRIPTION)
                .atlanTag(AtlanTag.of(ATLAN_TAG_NAME1).toBuilder()
                        .propagate(false)
                        .build())
                .atlanTag(AtlanTag.of(ATLAN_TAG_NAME2).toBuilder()
                        .propagate(false)
                        .build())
                .assignedTerm(GlossaryTerm.refByGuid(term1.getGuid()))
                .assignedTerm(GlossaryTerm.refByGuid(term2.getGuid()))
                .build();
        AssetMutationResponse response = toUpdate.save(client, true);
        assertEquals(response.getUpdatedAssets().size(), 3); // table + 2x terms
        assertEquals(
                response.getUpdatedAssets().stream().map(Asset::getTypeName).collect(Collectors.toSet()),
                Set.of(Table.TYPE_NAME, GlossaryTerm.TYPE_NAME));
        Table result = response.getResult(toUpdate);
        assertNotNull(result);
        assertNotNull(result.getOwnerGroups());
        assertEquals(result.getOwnerGroups().size(), 1);
        assertEquals(result.getOwnerGroups(), Set.of(ownerGroup.getName()));
    }

    @Test(
            groups = {"suggestions.update.column.t3"},
            dependsOnGroups = {
                "suggestions.create.table",
                "suggestions.create.group.owners",
                "suggestions.create.atlantags"
            })
    void updateT3() throws AtlanException {
        Table toUpdate = Table.updater(table3.getQualifiedName(), VIEW_NAME)
                .ownerGroup(ownerGroup.getName())
                .description(SYSTEM_DESCRIPTION)
                .userDescription(DESCRIPTION)
                .atlanTag(AtlanTag.of(ATLAN_TAG_NAME1).toBuilder()
                        .propagate(false)
                        .build())
                .atlanTag(AtlanTag.of(ATLAN_TAG_NAME2).toBuilder()
                        .propagate(false)
                        .build())
                .assignedTerm(GlossaryTerm.refByGuid(term1.getGuid()))
                .assignedTerm(GlossaryTerm.refByGuid(term2.getGuid()))
                .build();
        AssetMutationResponse response = toUpdate.save(client, true);
        assertEquals(response.getUpdatedAssets().size(), 3); // table + 2x terms
        assertEquals(
                response.getUpdatedAssets().stream().map(Asset::getTypeName).collect(Collectors.toSet()),
                Set.of(Table.TYPE_NAME, GlossaryTerm.TYPE_NAME));
        Table result = response.getResult(toUpdate);
        assertNotNull(result);
        assertNotNull(result.getOwnerGroups());
        assertEquals(result.getOwnerGroups().size(), 1);
        assertEquals(result.getOwnerGroups(), Set.of(ownerGroup.getName()));
    }

    @Test(
            groups = {"suggestions.update.column.t1c1"},
            dependsOnGroups = {
                "suggestions.create.column.*",
                "suggestions.create.group.owners",
                "suggestions.create.atlantags"
            })
    void updateT1C1() throws AtlanException {
        Column toUpdate = Column.updater(t1c1.getQualifiedName(), COLUMN_NAME1)
                .ownerGroup(ownerGroup.getName())
                .description(SYSTEM_DESCRIPTION)
                .userDescription(DESCRIPTION)
                .atlanTag(AtlanTag.of(ATLAN_TAG_NAME1).toBuilder()
                        .propagate(false)
                        .build())
                .atlanTag(AtlanTag.of(ATLAN_TAG_NAME2).toBuilder()
                        .propagate(false)
                        .build())
                .assignedTerm(GlossaryTerm.refByGuid(term1.getGuid()))
                .assignedTerm(GlossaryTerm.refByGuid(term2.getGuid()))
                .build();
        AssetMutationResponse response = toUpdate.save(client, true);
        assertEquals(response.getUpdatedAssets().size(), 3); // column + 2x terms
        assertEquals(
                response.getUpdatedAssets().stream().map(Asset::getTypeName).collect(Collectors.toSet()),
                Set.of(Column.TYPE_NAME, GlossaryTerm.TYPE_NAME));
        Column result = response.getResult(toUpdate);
        assertNotNull(result);
        assertNotNull(result.getOwnerGroups());
        assertEquals(result.getOwnerGroups().size(), 1);
        assertEquals(result.getOwnerGroups(), Set.of(ownerGroup.getName()));
    }

    @Test(
            groups = {"suggestions.update.column.v1c1"},
            dependsOnGroups = {
                "suggestions.create.column.*",
                "suggestions.create.group.owners",
                "suggestions.create.atlantags"
            })
    void updateV1C1() throws AtlanException {
        Column toUpdate = Column.updater(v1c1.getQualifiedName(), COLUMN_NAME1)
                .ownerGroup(ownerGroup.getName())
                .description(SYSTEM_DESCRIPTION)
                .userDescription(DESCRIPTION)
                .atlanTag(AtlanTag.of(ATLAN_TAG_NAME2).toBuilder()
                        .propagate(false)
                        .build())
                .assignedTerm(GlossaryTerm.refByGuid(term2.getGuid()))
                .build();
        AssetMutationResponse response = toUpdate.save(client, true);
        assertEquals(response.getUpdatedAssets().size(), 2); // column + term
        assertEquals(
                response.getUpdatedAssets().stream().map(Asset::getTypeName).collect(Collectors.toSet()),
                Set.of(Column.TYPE_NAME, GlossaryTerm.TYPE_NAME));
        Column result = response.getResult(toUpdate);
        assertNotNull(result);
        assertNotNull(result.getOwnerGroups());
        assertEquals(result.getOwnerGroups().size(), 1);
        assertEquals(result.getOwnerGroups(), Set.of(ownerGroup.getName()));
    }

    @Test(
            groups = {"suggestions.find.wait"},
            dependsOnGroups = {"suggestions.update.column.*"})
    void awaitConsistency() throws AtlanException, InterruptedException {
        waitForTagsToSync(taggedAssetGuids, log);
    }

    @Test(
            groups = {"suggestions.find.t2c1"},
            dependsOnGroups = {"suggestions.find.wait"})
    void findSuggestionsDefault() throws AtlanException {
        SuggestionResponse response = Suggestions.finder(client, t2c1)
                .includes(Arrays.asList(Suggestions.TYPE.values()))
                .get();
        assertNotNull(response);
        assertNotNull(response.getOwnerGroups());
        assertEquals(response.getOwnerGroups().size(), 1);
        assertEquals(response.getOwnerGroups().get(0).getCount(), 2);
        assertEquals(response.getOwnerGroups().get(0).getValue(), ownerGroup.getName());
        assertNotNull(response.getSystemDescriptions());
        assertEquals(response.getSystemDescriptions().size(), 1);
        assertEquals(response.getSystemDescriptions().get(0).getCount(), 2);
        assertEquals(response.getSystemDescriptions().get(0).getValue(), SYSTEM_DESCRIPTION);
        assertNotNull(response.getUserDescriptions());
        assertEquals(response.getUserDescriptions().size(), 1);
        assertEquals(response.getUserDescriptions().get(0).getCount(), 2);
        assertEquals(response.getUserDescriptions().get(0).getValue(), DESCRIPTION);
        assertNotNull(response.getAtlanTags());
        assertEquals(response.getAtlanTags().size(), 2);
        assertEquals(response.getAtlanTags().get(0).getCount(), 2);
        assertEquals(response.getAtlanTags().get(0).getValue(), ATLAN_TAG_NAME2);
        assertEquals(response.getAtlanTags().get(1).getCount(), 1);
        assertEquals(response.getAtlanTags().get(1).getValue(), ATLAN_TAG_NAME1);
        assertNotNull(response.getAssignedTerms());
        assertEquals(response.getAssignedTerms().size(), 2);
        assertEquals(response.getAssignedTerms().get(0).getCount(), 2);
        assertEquals(
                response.getAssignedTerms().get(0).getValue(),
                GlossaryTerm.refByQualifiedName(term2.getQualifiedName()));
        assertEquals(response.getAssignedTerms().get(1).getCount(), 1);
        assertEquals(
                response.getAssignedTerms().get(1).getValue(),
                GlossaryTerm.refByQualifiedName(term1.getQualifiedName()));
    }

    @Test(
            groups = {"suggestions.find.v1"},
            dependsOnGroups = {"suggestions.find.wait"})
    void findSuggestionsAcrossTypes() throws AtlanException {
        SuggestionResponse response = Suggestions.finder(client, view1)
                .includes(Arrays.asList(Suggestions.TYPE.values()))
                .withOtherType(Table.TYPE_NAME)
                .get();
        assertNotNull(response);
        assertNotNull(response.getOwnerGroups());
        assertEquals(response.getOwnerGroups().size(), 1);
        assertEquals(response.getOwnerGroups().get(0).getCount(), 1);
        assertEquals(response.getOwnerGroups().get(0).getValue(), ownerGroup.getName());
        assertNotNull(response.getSystemDescriptions());
        assertEquals(response.getSystemDescriptions().size(), 1);
        assertEquals(response.getSystemDescriptions().get(0).getCount(), 1);
        assertEquals(response.getSystemDescriptions().get(0).getValue(), SYSTEM_DESCRIPTION);
        assertNotNull(response.getUserDescriptions());
        assertEquals(response.getUserDescriptions().size(), 1);
        assertEquals(response.getUserDescriptions().get(0).getCount(), 1);
        assertEquals(response.getUserDescriptions().get(0).getValue(), DESCRIPTION);
        assertNotNull(response.getAtlanTags());
        assertEquals(response.getAtlanTags().size(), 2);
        assertEquals(response.getAtlanTags().get(0).getCount(), 1);
        assertEquals(response.getAtlanTags().get(1).getCount(), 1);
        assertEquals(
                response.getAtlanTags().stream()
                        .map(SuggestionResponse.SuggestedItem::getValue)
                        .collect(Collectors.toSet()),
                Set.of(ATLAN_TAG_NAME1, ATLAN_TAG_NAME2));
        assertNotNull(response.getAssignedTerms());
        assertEquals(response.getAssignedTerms().size(), 2);
        assertEquals(response.getAssignedTerms().get(0).getCount(), 1);
        assertEquals(response.getAssignedTerms().get(1).getCount(), 1);
        assertEquals(
                response.getAssignedTerms().stream()
                        .map(SuggestionResponse.SuggestedTerm::getValue)
                        .collect(Collectors.toSet()),
                Set.of(
                        GlossaryTerm.refByQualifiedName(term1.getQualifiedName()),
                        GlossaryTerm.refByQualifiedName(term2.getQualifiedName())));
    }

    @Test(
            groups = {"suggestions.find.t2"},
            dependsOnGroups = {"suggestions.find.wait"})
    void findLimitedSuggestions() throws AtlanException {
        SuggestionResponse response = Suggestions.finder(client, table2)
                .include(Suggestions.TYPE.SystemDescription)
                .include(Suggestions.TYPE.GroupOwners)
                .get();
        assertNotNull(response);
        assertNotNull(response.getOwnerGroups());
        assertEquals(response.getOwnerGroups().size(), 1);
        assertEquals(response.getOwnerGroups().get(0).getCount(), 1);
        assertEquals(response.getOwnerGroups().get(0).getValue(), ownerGroup.getName());
        assertNotNull(response.getSystemDescriptions());
        assertEquals(response.getSystemDescriptions().size(), 1);
        assertEquals(response.getSystemDescriptions().get(0).getCount(), 1);
        assertEquals(response.getSystemDescriptions().get(0).getValue(), SYSTEM_DESCRIPTION);
        assertTrue(response.getUserDescriptions() == null
                || response.getUserDescriptions().isEmpty());
        assertTrue(response.getAtlanTags() == null || response.getAtlanTags().isEmpty());
        assertTrue(response.getAssignedTerms() == null
                || response.getAssignedTerms().isEmpty());
    }

    // TODO: Test application of suggestions
    @Test(
            groups = "suggestions.apply.t2c1",
            dependsOnGroups = {"suggestions.find.*"})
    void applyT2C1() throws AtlanException {
        AssetMutationResponse response = Suggestions.finder(client, t2c1)
                .include(Suggestions.TYPE.UserDescription)
                .include(Suggestions.TYPE.IndividualOwners)
                .include(Suggestions.TYPE.GroupOwners)
                .include(Suggestions.TYPE.Tags)
                .include(Suggestions.TYPE.Terms)
                .apply();
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 2); // column + term
        Column one = response.getResult(t2c1);
        assertNotNull(one);
        assertEquals(one.getOwnerGroups(), Set.of(ownerGroup.getName()));
        assertNull(one.getDescription()); // System description should be untouched (still empty)
        assertEquals(one.getUserDescription(), DESCRIPTION);
        assertNotNull(one.getAtlanTags());
        assertEquals(one.getAtlanTags().size(), 1);
        assertEquals(
                one.getAtlanTags().stream().map(AtlanTag::getTypeName).collect(Collectors.toSet()),
                Set.of(ATLAN_TAG_NAME2));
        assertNotNull(one.getMeanings());
        assertEquals(
                one.getMeanings().stream().map(Meaning::getTermGuid).collect(Collectors.toSet()),
                Set.of(term2.getGuid()));
    }

    @Test(
            groups = "suggestions.apply.v2c1",
            dependsOnGroups = {"suggestions.find.*"})
    void applyV2C1() throws AtlanException {
        AssetMutationResponse response = Suggestions.finder(client, v2c1)
                .include(Suggestions.TYPE.SystemDescription)
                .include(Suggestions.TYPE.Tags)
                .apply(true);
        assertNotNull(response);
        Asset one = validateSingleUpdate(response);
        assertTrue(one instanceof Column);
        assertTrue(one.getOwnerGroups() == null || one.getOwnerGroups().isEmpty());
        assertNull(one.getDescription()); // System description should be untouched (still empty)
        assertEquals(
                one.getUserDescription(),
                SYSTEM_DESCRIPTION); // System description should be applied to user description
        assertNotNull(one.getAtlanTags());
        assertEquals(one.getAtlanTags().size(), 2);
        assertEquals(
                one.getAtlanTags().stream().map(AtlanTag::getTypeName).collect(Collectors.toSet()),
                Set.of(ATLAN_TAG_NAME1, ATLAN_TAG_NAME2));
        assertTrue(one.getMeanings() == null || one.getMeanings().isEmpty());
    }

    @Test(
            groups = {"suggestions.purge.connection"},
            dependsOnGroups = {
                "suggestions.create.*",
                "suggestions.update.*",
                "suggestions.find.*",
                "suggestions.apply.*",
            },
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(client, connection.getQualifiedName(), log);
    }

    @Test(
            groups = {"suggestions.purge.groups"},
            dependsOnGroups = {"suggestions.purge.connection"},
            alwaysRun = true)
    void purgeGroups() throws AtlanException {
        AtlanGroup.delete(client, ownerGroup.getId());
    }

    @Test(
            groups = {"suggestions.purge.atlantags"},
            dependsOnGroups = {"suggestions.purge.connection"},
            alwaysRun = true)
    void purgeAtlanTags() throws AtlanException {
        AtlanTagTest.deleteAtlanTag(client, ATLAN_TAG_NAME1);
        AtlanTagTest.deleteAtlanTag(client, ATLAN_TAG_NAME2);
    }

    @Test(
            groups = {"suggestions.purge.glossary"},
            dependsOnGroups = {"suggestions.purge.connection"},
            alwaysRun = true)
    void purgeGlossary() throws AtlanException {
        GlossaryTest.deleteTerm(client, term1.getGuid());
        GlossaryTest.deleteTerm(client, term2.getGuid());
        GlossaryTest.deleteGlossary(client, glossary.getGuid());
    }
}
