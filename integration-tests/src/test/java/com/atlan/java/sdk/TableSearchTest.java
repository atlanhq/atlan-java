package com.atlan.java.sdk;

import static org.testng.Assert.*;

import com.atlan.*;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.*;
import com.atlan.model.enums.*;
import com.atlan.model.fields.*;
import com.atlan.model.search.*;
import com.atlan.model.structs.*;
import java.util.*;
import java.util.concurrent.*;

import org.testng.annotations.Test;

/**
 * Tests validation of table attributes through IndexSearch.
 */
public class TableSearchTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("TABLE_ATTR");
    private static final String name = PREFIX + "_name";
    private static final Set<String> ownerUsers = Set.of("user1", "user2");
    private static final Set<String> ownerGroups = Set.of("group1", "group2");

    private static Table table = null;

    @Test(groups = {"table.create"})
    public  void createTable() throws AtlanException, InterruptedException {
        Connection connection = ConnectionTest.createConnection(client, SQLAssetTest.CONNECTION_NAME, SQLAssetTest.CONNECTOR_TYPE);
        Database database = SQLAssetTest.createDatabase(client, SQLAssetTest.DATABASE_NAME, connection.getQualifiedName());
        Schema schema = SQLAssetTest.createSchema(client, SQLAssetTest.SCHEMA_NAME, database);
        table = Table._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .qualifiedName(Table.generateQualifiedName(name, schema.getQualifiedName()))
            .connectorType(SQLAssetTest.CONNECTOR_TYPE)
            .schemaName(SQLAssetTest.SCHEMA_NAME)
            .schemaQualifiedName(schema.getQualifiedName())
            .schema(Schema.refByQualifiedName(schema.getQualifiedName()))
            .databaseName(SQLAssetTest.DATABASE_NAME)
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
        groups = {"table.search.basic"},
        dependsOnGroups = {"table.search.consistent"})
    void searchBasicTypes() throws AtlanException {
        Set<AtlanField> fields = Set.of(
            Table.SOURCE_LAST_READ_AT,
            Table.SOURCE_READ_QUERY_COST,
            Table.CERTIFICATE_STATUS_MESSAGE,
            Table.CERTIFICATE_UPDATED_BY,
            Table.SOURCE_COST_UNIT
        );
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
        dependsOnGroups = {"table.search.consistent"})
    void searchCollectionTypes() throws AtlanException {
        Set<AtlanField> fields = Set.of(
            Table.OWNER_USERS,
            Table.OWNER_GROUPS,
            Connection.QUERY_PREVIEW_CONFIG
        );
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
        groups = {"table.search.complex"},
        dependsOnGroups = {"table.search.consistent"})
    void searchComplexTypes() throws AtlanException {
        Set<AtlanField> fields = Set.of(
            Table.SOURCE_READ_RECENT_USER_RECORDS,
            Table.SOURCE_READ_POPULAR_QUERY_RECORDS,
            Table.SCHEMA,
            Table.CUSTOM_ATTRIBUTES
        );
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
        assertEquals(found.getSourceReadRecentUserRecords().get(0).getRecordComputeCostUnit(), SourceCostUnitType.CREDITS);
        assertEquals(found.getSourceReadRecentUserRecords().get(1).getRecordUser(), "user2");
        assertEquals(found.getSourceReadRecentUserRecords().get(1).getRecordQuery(), "query2");
        assertEquals(found.getSourceReadRecentUserRecords().get(1).getRecordComputeCostUnit(), SourceCostUnitType.BYTES);
        assertEquals(found.getSourceReadPopularQueryRecords().size(), 1);
        System.out.println(found.getCustomAttributes().get("String0"));

    }

    @Test(
        groups = {"table.cleanup"},
        dependsOnGroups = {
            "table.create",
            "table.search.consistent",
            "table.search.basic",
            "table.search.collections",
            "table.search.complex"
        },
        alwaysRun = true)
    void deleteTestTable() throws AtlanException {
        if (table != null) {
            AssetDeletionResponse deleted = Asset.delete(client, table.getGuid());
            assertNotNull(deleted);
        }
    }
}
