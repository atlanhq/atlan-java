/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.query_dsl.*;

/**
 * Tests various searches.
 * Note: since the search index is only eventually consistent with the metastore, there can be slight
 * delays between the data being persisted and it being searchable. As a result, there are retry loops
 * here in the tests to allow for that eventual consistency.
 */
public class SearchTest extends AtlanLiveTest {

    // TODO: test paging, sorting, range queries

    /*
    @Test(
            groups = {"search.s3object.creation"},
            dependsOnGroups = {"search.s3object"})
    void searchByRange() {

        try {
            Query byCreation = RangeQuery.of(r -> r.field("__timestamp").gte(JsonData.of(s3ObjectCreationTime)))
                    ._toQuery();
            Query byState = QueryFactory.active();
            Query byType = QueryFactory.withType(S3Object.TYPE_NAME);
            Query combined =
                    BoolQuery.of(b -> b.filter(byState, byType, byCreation))._toQuery();

            IndexSearchRequest index = IndexSearchRequest.builder()
                    .dsl(IndexSearchDSL.builder().query(combined).build())
                    .attribute("name")
                    .attribute("connectionQualifiedName")
                    .build();

            IndexSearchResponse response = index.search();

            assertNotNull(response);
            assertEquals(response.getApproximateCount().longValue(), 1L);
            List<Entity> entities = response.getEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            Entity one = entities.get(0);
            assertTrue(one instanceof S3Object);
            assertFalse(one.isComplete());
            S3Object object = (S3Object) one;
            assertNotNull(object);
            assertEquals(object.getQualifiedName(), S3AssetTest.s3Object3Qame);
            assertEquals(object.getConnectionQualifiedName(), S3AssetTest.connectionQame);

        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while searching by range.");
        }
    }

     */
}
