/* SPDX-License-Identifier: Apache-2.0
   Copyright 2025 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.model.admin.*;
import com.atlan.model.assets.*;
import com.atlan.model.core.*;
import com.atlan.model.enums.*;
import com.atlan.model.fields.*;
import com.atlan.model.search.*;
import com.atlan.model.structs.*;
import com.atlan.model.typedefs.*;
import java.util.*;
import java.util.concurrent.*;
import lombok.extern.slf4j.*;
import org.testng.annotations.Test;

/**
 * Tests validation of table attributes through IndexSearch.
 */
@Slf4j
public class TableSearchTest extends AtlanLiveTest {

    private Connection connection = null;
    private Database database = null;
    private Schema schema = null;
    private static final String PREFIX = makeUnique("TABLE_ATTR");
    private static final String name = PREFIX + "_name";
    private static final Set<String> ownerUsers = Set.of("user1", "user2");
    private static final Set<String> ownerGroups = Set.of("group1", "group2");
    private static final String GROUP_NAME1 = PREFIX + "1";
    private static final String GROUP_NAME2 = PREFIX + "2";
    private Column column1 = null;
    private Column column2 = null;
    public static final String COLUMN_NAME1 = PREFIX + "_col1";
    public static final String COLUMN_NAME2 = PREFIX + "_col2";
    private static final String CM_ATTR_RACI_RESPONSIBLE = "Responsible"; // user
    private static final String CM_ATTR_RACI_ACCOUNTABLE = "Accountable"; // user
    private static final String CM_ATTR_RACI_INFORMED = "Informed";
    private static CustomMetadataField CM_RACI_RESPONSIBLE = null;
    private static CustomMetadataField CM_RACI_ACCOUNTABLE = null;
    private static CustomMetadataField CM_RACI_INFORMED = null;
    private static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.CLICKHOUSE;
    private static final String CONNECTION_PREFIX = makeUnique("SQL");

    public static final String CONNECTION_NAME = CONNECTION_PREFIX;
    public static final String DATABASE_NAME = CONNECTION_PREFIX + "_db";
    public static final String SCHEMA_NAME = CONNECTION_PREFIX + "_schema";
    private static AtlanGroup group1 = null;
    private static AtlanGroup group2 = null;
    public static final String CM_RACI = makeUnique("RACII");
    private static Table table = null;

    @Test(groups = {"table.create"})
    public void createTable() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(client, CONNECTION_NAME, CONNECTOR_TYPE);
        database = SQLAssetTest.createDatabase(client, DATABASE_NAME, connection.getQualifiedName());
        schema = SQLAssetTest.createSchema(client, SCHEMA_NAME, database);
        CustomMetadataTest.createCustomMetadataRACI(CM_RACI, client);
        group1 = AdminTest.createGroup(client, GROUP_NAME1);
        group2 = AdminTest.createGroup(client, GROUP_NAME2);
        CM_RACI_RESPONSIBLE = new CustomMetadataField(client, CM_RACI, CM_ATTR_RACI_RESPONSIBLE);
        CM_RACI_ACCOUNTABLE = new CustomMetadataField(client, CM_RACI, CM_ATTR_RACI_ACCOUNTABLE);
        CM_RACI_INFORMED = new CustomMetadataField(client, CM_RACI, CM_ATTR_RACI_INFORMED);
        table = Table._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(Table.generateQualifiedName(name, schema.getQualifiedName()))
                .connectorType(CONNECTOR_TYPE)
                .schemaName(SCHEMA_NAME)
                .schemaQualifiedName(schema.getQualifiedName())
                .schema(Schema.refByQualifiedName(schema.getQualifiedName()))
                .databaseName(DATABASE_NAME)
                .databaseQualifiedName(database.getQualifiedName())
                .connectionQualifiedName(connection.getQualifiedName())
                .sourceLastReadAt(123456789L)
                .sourceReadUserCount(123456789L)
                .sourceReadQueryCost(123.456)
                .certificateStatusMessage("Test Certificate Message")
                .certificateUpdatedBy("test-user")
                .sourceCostUnit(SourceCostUnitType.CREDITS)
                // Collection Types
                .ownerUsers(ownerUsers)
                .ownerGroups(ownerGroups)
                .putQueryPreviewConfig("key1", "value1")
                .putQueryPreviewConfig("key2", "value2")
                // Complex Types
                .sourceReadRecentUserRecord(PopularityInsights.builder()
                        .recordUser("user1")
                        .recordQuery("query1")
                        .recordQueryDuration(123456789L)
                        .recordQueryCount(123456789L)
                        .recordTotalUserCount(123456789L)
                        .recordComputeCost(123.456)
                        .recordMaxComputeCost(123.456)
                        .recordComputeCostUnit(SourceCostUnitType.CREDITS)
                        .recordLastTimestamp(123456789L)
                        .recordWarehouse("warehouse1")
                        .build())
                .sourceReadRecentUserRecord(PopularityInsights.builder()
                        .recordUser("user2")
                        .recordQuery("query2")
                        .recordQueryDuration(123456789L)
                        .recordQueryCount(123456789L)
                        .recordTotalUserCount(123456789L)
                        .recordComputeCost(123.456)
                        .recordMaxComputeCost(123.456)
                        .recordComputeCostUnit(SourceCostUnitType.BYTES)
                        .recordLastTimestamp(123456789L)
                        .recordWarehouse("warehouse1")
                        .build())
                .sourceReadPopularQueryRecord(PopularityInsights.builder()
                        .recordUser("user1")
                        .recordQuery("query1")
                        .recordQueryDuration(987654321L)
                        .recordQueryCount(987654321L)
                        .recordTotalUserCount(987654321L)
                        .recordComputeCost(654.321)
                        .recordMaxComputeCost(654.321)
                        .recordComputeCostUnit(SourceCostUnitType.BYTES)
                        .recordLastTimestamp(987654321L)
                        .recordWarehouse("String1")
                        .build())
                .starredDetail(StarredDetails.builder()
                        .assetStarredBy("user1")
                        .assetStarredAt(123456789L)
                        .build())
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
        assertEquals(table.getName(), name);
        assertEquals(table.getSchemaQualifiedName(), schema.getQualifiedName());
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
            groups = {"table.column.create"},
            dependsOnGroups = {"table.search.consistent"})
    void createColumn() throws AtlanException {
        column1 = SQLAssetTest.createColumn(client, COLUMN_NAME1, table, 1);
        column2 = SQLAssetTest.createColumn(client, COLUMN_NAME2, table, 2);
    }

    @Test(
            groups = {"table.search.basic"},
            dependsOnGroups = {"table.search.consistent"})
    void searchBasicTypes() throws AtlanException {
        Set<AtlanField> fields = Set.of(
                Table.SOURCE_LAST_READ_AT,
                Table.SOURCE_READ_QUERY_COST,
                Table.CERTIFICATE_STATUS_MESSAGE,
                Table.CERTIFICATE_UPDATED_BY,
                Table.SOURCE_COST_UNIT);
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
        assertEquals(found.getSourceLastReadAt(), Long.valueOf(123456789L));
        assertEquals(found.getSourceReadQueryCost(), Double.valueOf(123.456));
        assertEquals(found.getCertificateStatusMessage(), "Test Certificate Message");
        assertNotNull(found.getCertificateUpdatedBy());
        assertEquals(found.getSourceCostUnit(), SourceCostUnitType.CREDITS);
    }

    @Test(
            groups = {"table.search.collections"},
            dependsOnGroups = {"t" + "able.search.consistent"})
    void searchCollectionTypes() throws AtlanException {
        Set<AtlanField> fields = Set.of(Table.OWNER_USERS, Table.OWNER_GROUPS, Connection.QUERY_PREVIEW_CONFIG);
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
        assertTrue(found.getOwnerUsers().containsAll(ownerUsers));
        assertTrue(found.getOwnerGroups().containsAll(ownerGroups));

        assertEquals(found.getQueryPreviewConfig().get("key1"), "value1");
        assertEquals(found.getQueryPreviewConfig().get("key2"), "value2");
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
            groups = {"table.search.complex"},
            dependsOnGroups = {"table.search.consistent"})
    void searchComplexTypes() throws AtlanException {
        Set<AtlanField> fields =
                Set.of(Table.SOURCE_READ_RECENT_USER_RECORDS, Table.SOURCE_READ_POPULAR_QUERY_RECORDS, Table.SCHEMA);
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
        assertNotNull(found.getSourceReadRecentUserRecords());
        assertEquals(found.getSourceReadRecentUserRecords().size(), 2);
        assertEquals(found.getSourceReadRecentUserRecords().get(0).getRecordUser(), "user1");
        assertEquals(found.getSourceReadRecentUserRecords().get(0).getRecordQuery(), "query1");
        assertEquals(
                found.getSourceReadRecentUserRecords().get(0).getRecordComputeCostUnit(), SourceCostUnitType.CREDITS);
        assertEquals(found.getSourceReadRecentUserRecords().get(1).getRecordUser(), "user2");
        assertEquals(found.getSourceReadRecentUserRecords().get(1).getRecordQuery(), "query2");
        assertEquals(
                found.getSourceReadRecentUserRecords().get(1).getRecordComputeCostUnit(), SourceCostUnitType.BYTES);
        assertEquals(found.getSourceReadPopularQueryRecords().size(), 1);
    }

    @Test(
            groups = {"table.search.relations"},
            dependsOnGroups = {"table.search.consistent"})
    void searchRelations() throws AtlanException {
        Set<AtlanField> fields = Set.of(Table.COLUMNS, Table.COLUMN_COUNT);

        Set<AtlanField> relationFields = Set.of(
                Column.NAME,
                Column.QUALIFIED_NAME,
                Column.SCHEMA_NAME,
                Column.SCHEMA_QUALIFIED_NAME,
                Column.DATABASE_NAME,
                Column.DATABASE_QUALIFIED_NAME,
                Column.CONNECTOR_TYPE,
                Column.TABLE_NAME,
                Column.CONNECTION_QUALIFIED_NAME);
        IndexSearchRequest request = Table.select(client)
                .where(Table.QUALIFIED_NAME.eq(table.getQualifiedName()))
                .includesOnResults(fields)
                .includesOnRelations(relationFields)
                .toRequest();
        IndexSearchResponse response = request.search(client);
        assertNotNull(response);
        assertEquals(response.getApproximateCount(), 1L);
        List<Asset> results = response.getAssets();
        assertEquals(results.size(), 1);
        Table found = (Table) results.get(0);
        assertEquals(found.getColumnCount(), 2L);
        assertNotNull(found.getColumns());
        assertEquals(found.getColumns().size(), 2);
        Column col1 = (Column) found.getColumns().first();
        assertNotNull(col1);
        assertEquals(col1.getConnectorType(), connection.getConnectorType());
        assertEquals(col1.getTableName(), table.getName());
        assertEquals(col1.getSchemaName(), schema.getName());
        assertEquals(col1.getSchemaQualifiedName(), schema.getQualifiedName());
        assertEquals(col1.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(col1.getConnectionQualifiedName(), connection.getQualifiedName());
        Column col2 = (Column) found.getColumns().last();
        assertNotNull(col2);
    }

    @Test(
            groups = {"table.cleanup"},
            dependsOnGroups = {
                "table.create",
                "table.column.create",
                "table.search.consistent",
                "table.search.basic",
                "table.search.collections",
                "table.search.complex",
                "table.search.relations",
                "table.search.business.attributes"
            },
            alwaysRun = true)
    void deleteTestData() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(client, connection.getQualifiedName(), log);
        CustomMetadataDef.purge(client, CM_RACI);
        AdminTest.deleteGroup(client, group1.getId());
        AdminTest.deleteGroup(client, group2.getId());
        if (table != null) {
            AssetDeletionResponse deleted = Asset.delete(client, table.getGuid());
            assertNotNull(deleted);
        }
        AssetMutationResponse response = Asset.delete(client, column1.getGuid()).block();
        assertNotNull(response);
        response = Asset.delete(client, column2.getGuid()).block();
        assertNotNull(response);
    }
}
