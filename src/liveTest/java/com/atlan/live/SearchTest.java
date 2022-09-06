/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Glossary;
import com.atlan.model.assets.GlossaryTerm;
import com.atlan.model.assets.S3Object;
import com.atlan.model.core.Entity;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

public class SearchTest extends AtlanLiveTest {

    private static Long s3ObjectCreationTime = null;

    @Test(
            groups = {"search.term"},
            dependsOnGroups = {"update.term"})
    void searchTerms() {
        try {
            Query byState =
                    TermQuery.of(t -> t.field("__state").value("ACTIVE"))._toQuery();

            Query byType = TermQuery.of(t -> t.field("__typeName.keyword").value(GlossaryTerm.TYPE_NAME))
                    ._toQuery();

            Query byName = TermQuery.of(t -> t.field("name.keyword").value(GlossaryTest.TERM_NAME1))
                    ._toQuery();

            Query combined =
                    BoolQuery.of(b -> b.must(byState).must(byType).must(byName))._toQuery();

            IndexSearchRequest index = IndexSearchRequest.builder()
                    .dsl(IndexSearchDSL.builder()
                            .from(0)
                            .size(100)
                            .query(combined)
                            .build())
                    .attributes(Collections.singletonList("anchor"))
                    .relationAttributes(Collections.singletonList("certificateStatus"))
                    .build();

            IndexSearchResponse response = index.search();

            assertNotNull(response);
            assertEquals(response.getApproximateCount().longValue(), 1L);
            List<Entity> entities = response.getEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            Entity one = entities.get(0);
            assertTrue(one instanceof GlossaryTerm);
            GlossaryTerm term = (GlossaryTerm) one;
            assertEquals(term.getGuid(), GlossaryTest.termGuid1);
            assertEquals(term.getQualifiedName(), GlossaryTest.termQame1);
            assertNotNull(term.getAnchor());
            assertEquals(term.getAnchor().getTypeName(), Glossary.TYPE_NAME);
            assertEquals(term.getAnchor().getGuid(), GlossaryTest.glossaryGuid);
            // TODO: test embedded relationship attributes that were requested
            //  ... this probably needs a more complex entity structure than
            //  just the basic references defined in Entity (?)
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while searching terms.");
        }
    }

    @Test(
            groups = {"search.s3object"},
            dependsOnGroups = {"create.s3object"})
    void searchS3Objects() {
        try {
            Query byState =
                    TermQuery.of(t -> t.field("__state").value("ACTIVE"))._toQuery();
            Query byType = TermQuery.of(t -> t.field("__typeName.keyword").value(S3Object.TYPE_NAME))
                    ._toQuery();
            Query combined = BoolQuery.of(b -> b.must(byState).must(byType))._toQuery();

            SortOptions sort = SortOptions.of(
                    s -> s.field(FieldSort.of(f -> f.field("__timestamp").order(SortOrder.Asc))));

            IndexSearchRequest index = IndexSearchRequest.builder()
                    .dsl(IndexSearchDSL.builder()
                            .from(0)
                            .size(1)
                            .query(combined)
                            .sortOption(sort)
                            .build())
                    .attribute("name")
                    .attribute("connectionQualifiedName")
                    .build();

            IndexSearchResponse response = index.search();

            // First result
            assertNotNull(response);
            assertEquals(response.getApproximateCount().longValue(), 3L);
            List<Entity> entities = response.getEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            Entity one = entities.get(0);
            assertTrue(one instanceof S3Object);
            S3Object object = (S3Object) one;
            assertNotNull(object);
            assertEquals(object.getQualifiedName(), S3AssetTest.s3Object1Qame);
            assertEquals(object.getConnectionQualifiedName(), S3AssetTest.connectionQame);
            // Second result
            response = response.getNextPage();
            assertNotNull(response);
            assertEquals(response.getApproximateCount().longValue(), 3L);
            entities = response.getEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            one = entities.get(0);
            assertTrue(one instanceof S3Object);
            object = (S3Object) one;
            assertNotNull(object);
            assertEquals(object.getQualifiedName(), S3AssetTest.s3Object2Qame);
            assertEquals(object.getConnectionQualifiedName(), S3AssetTest.connectionQame);
            // Third result
            response = response.getNextPage();
            assertNotNull(response);
            assertEquals(response.getApproximateCount().longValue(), 3L);
            entities = response.getEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            one = entities.get(0);
            assertTrue(one instanceof S3Object);
            object = (S3Object) one;
            s3ObjectCreationTime = object.getCreateTime();
            assertNotNull(object);
            assertEquals(object.getQualifiedName(), S3AssetTest.s3Object3Qame);
            assertEquals(object.getConnectionQualifiedName(), S3AssetTest.connectionQame);
            // No more results
            response = response.getNextPage();
            assertNotNull(response);
            assertEquals(response.getApproximateCount().longValue(), 3L);
            entities = response.getEntities();
            assertNull(entities);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while searching and paging through S3 objects.");
        }
    }

    @Test(
            groups = {"search.s3object.creation"},
            dependsOnGroups = {"search.s3object"})
    void searchByRange() {

        try {
            Query byCreation = RangeQuery.of(r -> r.field("__timestamp").gte(JsonData.of(s3ObjectCreationTime)))
                    ._toQuery();
            Query byState =
                    TermQuery.of(t -> t.field("__state").value("ACTIVE"))._toQuery();
            Query byType = TermQuery.of(t -> t.field("__typeName.keyword").value(S3Object.TYPE_NAME))
                    ._toQuery();
            Query combined = BoolQuery.of(b -> b.must(byState).must(byType).must(byCreation))
                    ._toQuery();

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
            S3Object object = (S3Object) one;
            assertNotNull(object);
            assertEquals(object.getQualifiedName(), S3AssetTest.s3Object3Qame);
            assertEquals(object.getConnectionQualifiedName(), S3AssetTest.connectionQame);

        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while searching by range.");
        }
    }

    @Test(
            groups = {"search.s3object.classification"},
            dependsOnGroups = {"link.classification.s3object"})
    void searchByClassification() {

        try {
            Query byClassification =
                    ExistsQuery.of(q -> q.field("__traitNames"))._toQuery();
            Query byState =
                    TermQuery.of(t -> t.field("__state").value("ACTIVE"))._toQuery();
            Query byType = TermQuery.of(t -> t.field("__typeName.keyword").value(S3Object.TYPE_NAME))
                    ._toQuery();
            Query combined = BoolQuery.of(b -> b.must(byState).must(byType).must(byClassification))
                    ._toQuery();

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
            S3Object object = (S3Object) one;
            assertNotNull(object);
            assertEquals(object.getQualifiedName(), S3AssetTest.s3Object2Qame);
            assertEquals(object.getConnectionQualifiedName(), S3AssetTest.connectionQame);

        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while searching by classification existence.");
        }
    }

    @Test(
            groups = {"search.s3object.lineage"},
            dependsOnGroups = {"create.lineage.*"})
    void searchByLineage() {

        try {
            Query byLineage =
                    TermQuery.of(t -> t.field("__hasLineage").value(true))._toQuery();
            Query byState =
                TermQuery.of(t -> t.field("__state").value("ACTIVE"))._toQuery();
            Query byType = TermQuery.of(t -> t.field("__typeName.keyword").value(S3Object.TYPE_NAME))
                ._toQuery();
            Query combined = BoolQuery.of(b -> b.must(byState).must(byType).must(byLineage))
                ._toQuery();

            IndexSearchRequest index = IndexSearchRequest.builder()
                    .dsl(IndexSearchDSL.builder().query(combined).build())
                    .attribute("name")
                    .attribute("__hasLineage")
                    .build();

            IndexSearchResponse response = index.search();

            assertNotNull(response);
            assertEquals(response.getApproximateCount().longValue(), 3L);
            List<Entity> entities = response.getEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 3);
            Set<String> types = entities.stream().map(Entity::getTypeName).collect(Collectors.toSet());
            assertEquals(types.size(), 1);
            assertTrue(types.contains(S3Object.TYPE_NAME));
            Set<String> guids = entities.stream().map(Entity::getGuid).collect(Collectors.toSet());
            assertEquals(guids.size(), 3);
            assertTrue(guids.contains(S3AssetTest.s3Object1Guid));
            assertTrue(guids.contains(S3AssetTest.s3Object2Guid));
            assertTrue(guids.contains(S3AssetTest.s3Object3Guid));
            for (Entity one : entities) {
                assertTrue(((S3Object) one).getHasLineage());
            }

        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while searching by lineage existence.");
        }
    }
}
