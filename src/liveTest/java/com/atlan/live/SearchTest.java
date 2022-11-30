/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import com.atlan.Atlan;
import com.atlan.cache.CustomMetadataCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Glossary;
import com.atlan.model.assets.GlossaryTerm;
import com.atlan.model.assets.S3Object;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.QueryFactory;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.testng.annotations.Test;

/**
 * Tests various searches.
 * Note: since the search index is only eventually consistent with the metastore, there can be slight
 * delays between the data being persisted and it being searchable. As a result, there are retry loops
 * here in the tests to allow for that eventual consistency.
 */
public class SearchTest extends AtlanLiveTest {

    private static Long s3ObjectCreationTime = null;

    @Test(
            groups = {"search.term"},
            dependsOnGroups = {"update.term"})
    void searchTerms() {
        try {
            Query byState = QueryFactory.active();
            Query byType = QueryFactory.withType(GlossaryTerm.TYPE_NAME);
            Query byName = QueryFactory.withExactName(GlossaryTest.TERM_NAME1);

            Query combined =
                    BoolQuery.of(b -> b.filter(byState, byType, byName))._toQuery();

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
            assertFalse(one.isComplete());
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
            Query byState = QueryFactory.active();
            Query byType = QueryFactory.withType(S3Object.TYPE_NAME);
            Query combined = BoolQuery.of(b -> b.filter(byState, byType))._toQuery();

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
            assertFalse(one.isComplete());
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
            assertFalse(one.isComplete());
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
            assertFalse(one.isComplete());
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

    @Test(
            groups = {"search.s3object.classification.any"},
            dependsOnGroups = {"link.classification.s3object"})
    void searchByAnyClassification() {

        try {
            Query byClassification = QueryFactory.withAnyValueFor("__traitNames");
            Query byState = QueryFactory.active();
            Query byType = QueryFactory.withType(S3Object.TYPE_NAME);
            Query combined = BoolQuery.of(b -> b.filter(byState, byType, byClassification))
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
            assertFalse(one.isComplete());
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
            groups = {"search.s3object.classification.specific"},
            dependsOnGroups = {"link.classification.s3object"})
    void searchBySpecificClassification() {
        try {
            Query byClassification =
                    QueryFactory.withAtLeastOneClassification(List.of(ClassificationTest.CLASSIFICATION_NAME1));
            Query byState = QueryFactory.active();
            Query byType = QueryFactory.withType(S3Object.TYPE_NAME);
            Query combined = BoolQuery.of(b -> b.filter(byState, byType, byClassification))
                    ._toQuery();

            IndexSearchRequest index = IndexSearchRequest.builder()
                    .dsl(IndexSearchDSL.builder().query(combined).build())
                    .attribute("name")
                    .build();

            IndexSearchResponse response = index.search();

            assertNotNull(response);
            // Depending on how fast propagation happens, there should eventually be 2 assets
            // with the classification (since they're connected through lineage at this point)
            // ... but since that occurs through an async background process whose execution
            // time can vary widely, we'll just accept either result
            long count = response.getApproximateCount();
            if (count == 1) {
                assertEquals(response.getApproximateCount().longValue(), 1L);
                List<Entity> entities = response.getEntities();
                assertNotNull(entities);
                assertEquals(entities.size(), 1);
                Set<String> types = entities.stream().map(Entity::getTypeName).collect(Collectors.toSet());
                assertEquals(types.size(), 1);
                assertTrue(types.contains(S3Object.TYPE_NAME));
                Set<String> guids = entities.stream().map(Entity::getGuid).collect(Collectors.toSet());
                assertEquals(guids.size(), 1);
                assertTrue(guids.contains(S3AssetTest.s3Object2Guid));
            } else if (count == 2) {
                assertEquals(response.getApproximateCount().longValue(), 2L);
                List<Entity> entities = response.getEntities();
                assertNotNull(entities);
                assertEquals(entities.size(), 2);
                Set<String> types = entities.stream().map(Entity::getTypeName).collect(Collectors.toSet());
                assertEquals(types.size(), 1);
                assertTrue(types.contains(S3Object.TYPE_NAME));
                Set<String> guids = entities.stream().map(Entity::getGuid).collect(Collectors.toSet());
                assertEquals(guids.size(), 2);
                assertTrue(guids.contains(S3AssetTest.s3Object2Guid));
                assertTrue(guids.contains(S3AssetTest.s3Object3Guid));
            } else {
                assertTrue(1 <= count && count < 3, "Expected at least 1 result, but no more than 2.");
            }
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while searching by a specific classification.");
        }
    }

    @Test(
            groups = {"search.s3object.term.fromAsset"},
            dependsOnGroups = {"link.asset.term"})
    void searchByAssignedTerm() throws InterruptedException {

        try {
            Glossary glossary = Glossary.findByName(GlossaryTest.GLOSSARY_NAME, null);
            String glossaryQN = glossary.getQualifiedName();
            GlossaryTerm term = GlossaryTerm.findByName(GlossaryTest.TERM_NAME1, glossaryQN, null);
            String termQN = term.getQualifiedName();
            Query byTermAssignment = QueryFactory.withAtLeastOneTerm(List.of(termQN));
            Query byState = QueryFactory.active();
            Query byType = QueryFactory.withType(S3Object.TYPE_NAME);
            Query combined = BoolQuery.of(b -> b.filter(byState, byType, byTermAssignment))
                    ._toQuery();

            IndexSearchRequest index = IndexSearchRequest.builder()
                    .dsl(IndexSearchDSL.builder().query(combined).build())
                    .attribute("name")
                    .attribute("meanings")
                    .attribute("connectionQualifiedName")
                    .build();

            IndexSearchResponse response = index.search();

            assertNotNull(response);
            int count = 0;
            while (response.getApproximateCount() == 0L && count < Atlan.getMaxNetworkRetries()) {
                Thread.sleep(2000);
                response = index.search();
                count++;
            }
            assertEquals(response.getApproximateCount().longValue(), 1L);
            List<Entity> entities = response.getEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            Set<String> types = entities.stream().map(Entity::getTypeName).collect(Collectors.toSet());
            assertEquals(types.size(), 1);
            assertTrue(types.contains(S3Object.TYPE_NAME));
            Set<String> guids = entities.stream().map(Entity::getGuid).collect(Collectors.toSet());
            assertEquals(guids.size(), 1);
            assertTrue(guids.contains(S3AssetTest.s3Object1Guid));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while searching by a specific classification.");
        }
    }

    @Test(
            groups = {"search.term.cm"},
            dependsOnGroups = {"link.cm.term", "update.glossary"})
    void searchByCustomMetadata() throws InterruptedException {

        try {
            String attributeId = CustomMetadataCache.getAttrIdForName(
                    CustomMetadataTest.CM_RACI, CustomMetadataTest.CM_ATTR_RACI_ACCOUNTABLE);
            Query byCM = QueryFactory.withAnyValueFor(attributeId);
            Query byState = QueryFactory.active();
            Query byType = QueryFactory.withType(GlossaryTerm.TYPE_NAME);
            Query combined = BoolQuery.of(b -> b.filter(byState, byType, byCM))._toQuery();

            IndexSearchRequest index = IndexSearchRequest.builder()
                    .dsl(IndexSearchDSL.builder().query(combined).build())
                    .attribute("name")
                    .attribute("anchor")
                    .relationAttribute("name")
                    .relationAttribute("certificateStatus")
                    .build();

            IndexSearchResponse response = index.search();

            assertNotNull(response);
            int count = 0;
            while (response.getApproximateCount() == 0L && count < Atlan.getMaxNetworkRetries()) {
                Thread.sleep(2000);
                response = index.search();
                count++;
            }
            assertEquals(response.getApproximateCount().longValue(), 1L);
            List<Entity> entities = response.getEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            Entity one = entities.get(0);
            assertTrue(one instanceof GlossaryTerm);
            assertFalse(one.isComplete());
            GlossaryTerm term = (GlossaryTerm) one;
            assertEquals(term.getGuid(), GlossaryTest.termGuid1);
            assertEquals(term.getQualifiedName(), GlossaryTest.termQame1);
            Glossary anchor = term.getAnchor();
            assertNotNull(anchor);
            assertEquals(anchor.getName(), GlossaryTest.GLOSSARY_NAME);
            assertEquals(anchor.getCertificateStatus(), AtlanCertificateStatus.VERIFIED);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while searching by a specific classification.");
        }
    }

    @Test(
            groups = {"search.s3object.lineage"},
            dependsOnGroups = {"create.lineage.*"})
    void searchByLineage() {

        try {
            Query byLineage = QueryFactory.withLineage();
            Query byState = QueryFactory.active();
            Query byType = QueryFactory.withType(S3Object.TYPE_NAME);
            Query combined =
                    BoolQuery.of(b -> b.filter(byState, byType, byLineage))._toQuery();

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
