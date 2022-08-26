package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.model.GlossaryTerm;
import com.atlan.model.S3Object;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.relations.Reference;
import com.atlan.model.responses.EntityMutationResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.testng.annotations.Test;

public class TermAssignmentTest extends AtlanLiveTest {

    @Test(
            groups = {"link.term.asset"},
            dependsOnGroups = {"create.*"})
    void linkTermToAssets() {
        GlossaryTerm term = GlossaryTerm.updater(
                        GlossaryTest.termQame1, GlossaryTest.TERM_NAME1, GlossaryTest.glossaryGuid)
                .assignedEntity(Reference.to(S3Object.TYPE_NAME, S3AssetTest.s3Object1Guid))
                .assignedEntity(Reference.by(S3Object.TYPE_NAME, S3AssetTest.s3Object2Qame))
                .build();
        try {
            EntityMutationResponse response = term.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 3);
            Entity full = Entity.retrieveFull(GlossaryTest.termGuid1);
            assertTrue(full instanceof GlossaryTerm);
            term = (GlossaryTerm) full;
            assertEquals(term.getQualifiedName(), GlossaryTest.termQame1);
            assertEquals(term.getName(), GlossaryTest.TERM_NAME1);
            Set<Reference> entities = term.getAssignedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 2);
            Set<String> types = entities.stream().map(Reference::getTypeName).collect(Collectors.toSet());
            assertEquals(types.size(), 1);
            assertTrue(types.contains(S3Object.TYPE_NAME));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to link a term to multiple assets.");
        }
    }

    @Test(
            groups = {"unlink.term.asset"},
            dependsOnGroups = {"link.term.asset"})
    void removeTermToAssetLinks() {
        GlossaryTerm term = GlossaryTerm.updater(
                        GlossaryTest.termQame1, GlossaryTest.TERM_NAME1, GlossaryTest.glossaryGuid)
                .build();
        term.removeAssignedEntities();
        try {
            EntityMutationResponse response = term.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 3);
            Entity full = Entity.retrieveFull(GlossaryTest.termGuid1);
            assertTrue(full instanceof GlossaryTerm);
            term = (GlossaryTerm) full;
            assertEquals(term.getQualifiedName(), GlossaryTest.termQame1);
            assertEquals(term.getName(), GlossaryTest.TERM_NAME1);
            Set<Reference> entities = term.getAssignedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 2);
            Set<AtlanStatus> statuses =
                    entities.stream().map(Reference::getRelationshipStatus).collect(Collectors.toSet());
            assertEquals(statuses.size(), 1);
            assertTrue(statuses.contains(AtlanStatus.DELETED));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove assigned assets from a term.");
        }
    }

    @Test(
            groups = {"link.asset.term"},
            dependsOnGroups = {"unlink.term.asset"})
    void linkAssetToTerms() {
        try {
            S3Object result = S3Object.replaceTerms(
                    S3AssetTest.s3Object1Qame,
                    S3AssetTest.S3_OBJECT1_NAME,
                    List.of(Reference.to(GlossaryTerm.TYPE_NAME, GlossaryTest.termGuid1)));
            assertNotNull(result);
            Entity full = Entity.retrieveFull(S3AssetTest.s3Object1Guid);
            assertTrue(full instanceof S3Object);
            S3Object s3Object1 = (S3Object) full;
            assertEquals(s3Object1.getQualifiedName(), S3AssetTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetTest.S3_OBJECT1_NAME);
            Set<Reference> terms = s3Object1.getMeanings();
            assertNotNull(terms);
            assertEquals(terms.size(), 2);
            Set<AtlanStatus> statuses =
                    terms.stream().map(Reference::getRelationshipStatus).collect(Collectors.toSet());
            assertEquals(statuses.size(), 2);
            assertTrue(statuses.contains(AtlanStatus.DELETED));
            assertTrue(statuses.contains(AtlanStatus.ACTIVE));
            Set<String> types = terms.stream().map(Reference::getTypeName).collect(Collectors.toSet());
            assertEquals(types.size(), 1);
            assertTrue(types.contains(GlossaryTerm.TYPE_NAME));
            Set<String> guids = terms.stream().map(Reference::getGuid).collect(Collectors.toSet());
            assertEquals(guids.size(), 1);
            assertTrue(guids.contains(GlossaryTest.termGuid1));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to link an asset to a term.");
        }
    }

    @Test(
            groups = {"unlink.asset.term"},
            dependsOnGroups = {"link.asset.term"})
    void removeAssetToTermLinks() {
        try {
            S3Object result = S3Object.replaceTerms(S3AssetTest.s3Object1Qame, S3AssetTest.S3_OBJECT1_NAME, null);
            assertNotNull(result);
            // TODO: verify whether we actually see relationships in the entity response?
            Entity full = Entity.retrieveFull(S3AssetTest.s3Object1Guid);
            assertTrue(full instanceof S3Object);
            S3Object s3Object1 = (S3Object) full;
            assertEquals(s3Object1.getQualifiedName(), S3AssetTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetTest.S3_OBJECT1_NAME);
            Set<Reference> terms = s3Object1.getMeanings();
            assertNotNull(terms);
            assertEquals(terms.size(), 2);
            Set<AtlanStatus> status =
                    terms.stream().map(Reference::getRelationshipStatus).collect(Collectors.toSet());
            assertEquals(status.size(), 1);
            assertTrue(status.contains(AtlanStatus.DELETED));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove assigned assets from a term.");
        }
    }

    @Test(
            groups = {"link.asset.term.1"},
            dependsOnGroups = {"unlink.asset.term"})
    void linkAssetToTerm1() {
        try {
            S3Object result = S3Object.appendTerms(
                    S3AssetTest.s3Object1Qame, List.of(Reference.to(GlossaryTerm.TYPE_NAME, GlossaryTest.termGuid1)));
            assertNotNull(result);
            // TODO: verify whether we actually see relationships in the entity response?
            Entity full = Entity.retrieveFull(S3AssetTest.s3Object1Guid);
            assertTrue(full instanceof S3Object);
            S3Object s3Object1 = (S3Object) full;
            assertEquals(s3Object1.getQualifiedName(), S3AssetTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetTest.S3_OBJECT1_NAME);
            Set<Reference> terms = s3Object1.getMeanings();
            assertNotNull(terms);
            assertEquals(terms.size(), 3);
            Set<AtlanStatus> status =
                    terms.stream().map(Reference::getRelationshipStatus).collect(Collectors.toSet());
            assertEquals(status.size(), 2);
            assertTrue(status.contains(AtlanStatus.ACTIVE));
            assertTrue(status.contains(AtlanStatus.DELETED));
            for (Reference term : terms) {
                if (term.getRelationshipStatus() == AtlanStatus.ACTIVE) {
                    assertEquals(term.getGuid(), GlossaryTest.termGuid1);
                }
            }
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove assigned assets from a term.");
        }
    }

    @Test(
            groups = {"link.asset.term.2"},
            dependsOnGroups = {"link.asset.term.1"})
    void linkAssetToTerm2() {
        try {
            S3Object result = S3Object.appendTerms(
                    S3AssetTest.s3Object1Qame, List.of(Reference.to(GlossaryTerm.TYPE_NAME, GlossaryTest.termGuid2)));
            assertNotNull(result);
            // TODO: verify whether we actually see relationships in the entity response?
            Entity full = Entity.retrieveFull(S3AssetTest.s3Object1Guid);
            assertTrue(full instanceof S3Object);
            S3Object s3Object1 = (S3Object) full;
            assertEquals(s3Object1.getQualifiedName(), S3AssetTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetTest.S3_OBJECT1_NAME);
            Set<Reference> terms = s3Object1.getMeanings();
            assertNotNull(terms);
            assertEquals(terms.size(), 4);
            Set<AtlanStatus> status =
                    terms.stream().map(Reference::getRelationshipStatus).collect(Collectors.toSet());
            assertEquals(status.size(), 2);
            assertTrue(status.contains(AtlanStatus.ACTIVE));
            assertTrue(status.contains(AtlanStatus.DELETED));
            for (Reference term : terms) {
                if (term.getRelationshipStatus() == AtlanStatus.ACTIVE) {
                    assertTrue(term.getGuid().equals(GlossaryTest.termGuid1)
                            || term.getGuid().equals(GlossaryTest.termGuid2));
                }
            }
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove assigned assets from a term.");
        }
    }

    @Test(
            groups = {"unlink.asset.term.1"},
            dependsOnGroups = {"link.asset.term.2"})
    void unlinkAssetToTerm1() {
        try {
            S3Object result = S3Object.removeTerms(
                    S3AssetTest.s3Object1Qame, List.of(Reference.to(GlossaryTerm.TYPE_NAME, GlossaryTest.termGuid1)));
            assertNotNull(result);
            // TODO: verify whether we actually see relationships in the entity response?
            Entity full = Entity.retrieveFull(S3AssetTest.s3Object1Guid);
            assertTrue(full instanceof S3Object);
            S3Object s3Object1 = (S3Object) full;
            assertEquals(s3Object1.getQualifiedName(), S3AssetTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetTest.S3_OBJECT1_NAME);
            Set<Reference> terms = s3Object1.getMeanings();
            assertNotNull(terms);
            assertEquals(terms.size(), 4);
            Set<AtlanStatus> status =
                    terms.stream().map(Reference::getRelationshipStatus).collect(Collectors.toSet());
            assertEquals(status.size(), 2);
            assertTrue(status.contains(AtlanStatus.ACTIVE));
            assertTrue(status.contains(AtlanStatus.DELETED));
            for (Reference term : terms) {
                if (term.getRelationshipStatus() == AtlanStatus.ACTIVE) {
                    assertEquals(term.getGuid(), GlossaryTest.termGuid2);
                }
            }
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove assigned assets from a term.");
        }
    }

    @Test(
            groups = {"unlink.asset.term.2"},
            dependsOnGroups = {"unlink.asset.term.1"})
    void unlinkAssetToTerm2() {
        try {
            S3Object result = S3Object.removeTerms(
                    S3AssetTest.s3Object1Qame, List.of(Reference.to(GlossaryTerm.TYPE_NAME, GlossaryTest.termGuid2)));
            assertNotNull(result);
            // TODO: verify whether we actually see relationships in the entity response?
            Entity full = Entity.retrieveFull(S3AssetTest.s3Object1Guid);
            assertTrue(full instanceof S3Object);
            S3Object s3Object1 = (S3Object) full;
            assertEquals(s3Object1.getQualifiedName(), S3AssetTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetTest.S3_OBJECT1_NAME);
            Set<Reference> terms = s3Object1.getMeanings();
            assertNotNull(terms);
            assertEquals(terms.size(), 4);
            Set<AtlanStatus> status =
                    terms.stream().map(Reference::getRelationshipStatus).collect(Collectors.toSet());
            assertEquals(status.size(), 1);
            assertTrue(status.contains(AtlanStatus.DELETED));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove assigned assets from a term.");
        }
    }
}
