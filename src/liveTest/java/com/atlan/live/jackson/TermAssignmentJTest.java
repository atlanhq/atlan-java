package com.atlan.live.jackson;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.live.AtlanLiveTest;
import com.atlan.model.GlossaryTermJ;
import com.atlan.model.S3ObjectJ;
import com.atlan.model.core.EntityJ;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.relations.ReferenceJ;
import com.atlan.model.responses.EntityMutationResponseJ;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.testng.annotations.Test;

public class TermAssignmentJTest extends AtlanLiveTest {

    @Test(
            groups = {"link.term.asset"},
            dependsOnGroups = {"create.*"})
    void linkTermToAssets() {
        GlossaryTermJ term = GlossaryTermJ.updater(
                        GlossaryJTest.termQame1, GlossaryJTest.TERM_NAME1, GlossaryJTest.glossaryGuid)
                .assignedEntity(ReferenceJ.to(S3ObjectJ.TYPE_NAME, S3AssetJTest.s3Object1Guid))
                .assignedEntity(ReferenceJ.by(S3ObjectJ.TYPE_NAME, S3AssetJTest.s3Object2Qame))
                .build();
        try {
            EntityMutationResponseJ response = term.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 3);
            EntityJ full = EntityJ.retrieveFull(GlossaryJTest.termGuid1);
            assertTrue(full instanceof GlossaryTermJ);
            term = (GlossaryTermJ) full;
            assertEquals(term.getQualifiedName(), GlossaryJTest.termQame1);
            assertEquals(term.getName(), GlossaryJTest.TERM_NAME1);
            Set<ReferenceJ> entities = term.getAssignedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 2);
            Set<String> types = entities.stream().map(ReferenceJ::getTypeName).collect(Collectors.toSet());
            assertEquals(types.size(), 1);
            assertTrue(types.contains(S3ObjectJ.TYPE_NAME));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to link a term to multiple assets.");
        }
    }

    @Test(
            groups = {"unlink.term.asset"},
            dependsOnGroups = {"link.term.asset"})
    void removeTermToAssetLinks() {
        GlossaryTermJ term = GlossaryTermJ.updater(
                        GlossaryJTest.termQame1, GlossaryJTest.TERM_NAME1, GlossaryJTest.glossaryGuid)
                .build();
        term.removeAssignedEntities();
        try {
            EntityMutationResponseJ response = term.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 3);
            EntityJ full = EntityJ.retrieveFull(GlossaryJTest.termGuid1);
            assertTrue(full instanceof GlossaryTermJ);
            term = (GlossaryTermJ) full;
            assertEquals(term.getQualifiedName(), GlossaryJTest.termQame1);
            assertEquals(term.getName(), GlossaryJTest.TERM_NAME1);
            Set<ReferenceJ> entities = term.getAssignedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 2);
            Set<AtlanStatus> statuses =
                    entities.stream().map(ReferenceJ::getRelationshipStatus).collect(Collectors.toSet());
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
            S3ObjectJ result = S3ObjectJ.replaceTerms(
                    S3AssetJTest.s3Object1Qame,
                    S3AssetJTest.S3_OBJECT1_NAME,
                    List.of(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, GlossaryJTest.termGuid1)));
            assertNotNull(result);
            EntityJ full = EntityJ.retrieveFull(S3AssetJTest.s3Object1Guid);
            assertTrue(full instanceof S3ObjectJ);
            S3ObjectJ s3Object1 = (S3ObjectJ) full;
            assertEquals(s3Object1.getQualifiedName(), S3AssetJTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetJTest.S3_OBJECT1_NAME);
            Set<ReferenceJ> terms = s3Object1.getMeanings();
            assertNotNull(terms);
            assertEquals(terms.size(), 2);
            Set<AtlanStatus> statuses =
                    terms.stream().map(ReferenceJ::getRelationshipStatus).collect(Collectors.toSet());
            assertEquals(statuses.size(), 2);
            assertTrue(statuses.contains(AtlanStatus.DELETED));
            assertTrue(statuses.contains(AtlanStatus.ACTIVE));
            Set<String> types = terms.stream().map(ReferenceJ::getTypeName).collect(Collectors.toSet());
            assertEquals(types.size(), 1);
            assertTrue(types.contains(GlossaryTermJ.TYPE_NAME));
            Set<String> guids = terms.stream().map(ReferenceJ::getGuid).collect(Collectors.toSet());
            assertEquals(guids.size(), 1);
            assertTrue(guids.contains(GlossaryJTest.termGuid1));
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
            S3ObjectJ result = S3ObjectJ.replaceTerms(S3AssetJTest.s3Object1Qame, S3AssetJTest.S3_OBJECT1_NAME, null);
            assertNotNull(result);
            // TODO: verify whether we actually see relationships in the entity response?
            EntityJ full = EntityJ.retrieveFull(S3AssetJTest.s3Object1Guid);
            assertTrue(full instanceof S3ObjectJ);
            S3ObjectJ s3Object1 = (S3ObjectJ) full;
            assertEquals(s3Object1.getQualifiedName(), S3AssetJTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetJTest.S3_OBJECT1_NAME);
            Set<ReferenceJ> terms = s3Object1.getMeanings();
            assertNotNull(terms);
            assertEquals(terms.size(), 2);
            Set<AtlanStatus> status =
                    terms.stream().map(ReferenceJ::getRelationshipStatus).collect(Collectors.toSet());
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
            S3ObjectJ result = S3ObjectJ.appendTerms(
                    S3AssetJTest.s3Object1Qame,
                    List.of(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, GlossaryJTest.termGuid1)));
            assertNotNull(result);
            // TODO: verify whether we actually see relationships in the entity response?
            EntityJ full = EntityJ.retrieveFull(S3AssetJTest.s3Object1Guid);
            assertTrue(full instanceof S3ObjectJ);
            S3ObjectJ s3Object1 = (S3ObjectJ) full;
            assertEquals(s3Object1.getQualifiedName(), S3AssetJTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetJTest.S3_OBJECT1_NAME);
            Set<ReferenceJ> terms = s3Object1.getMeanings();
            assertNotNull(terms);
            assertEquals(terms.size(), 3);
            Set<AtlanStatus> status =
                    terms.stream().map(ReferenceJ::getRelationshipStatus).collect(Collectors.toSet());
            assertEquals(status.size(), 2);
            assertTrue(status.contains(AtlanStatus.ACTIVE));
            assertTrue(status.contains(AtlanStatus.DELETED));
            for (ReferenceJ term : terms) {
                if (term.getRelationshipStatus() == AtlanStatus.ACTIVE) {
                    assertEquals(term.getGuid(), GlossaryJTest.termGuid1);
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
            S3ObjectJ result = S3ObjectJ.appendTerms(
                    S3AssetJTest.s3Object1Qame,
                    List.of(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, GlossaryJTest.termGuid2)));
            assertNotNull(result);
            // TODO: verify whether we actually see relationships in the entity response?
            EntityJ full = EntityJ.retrieveFull(S3AssetJTest.s3Object1Guid);
            assertTrue(full instanceof S3ObjectJ);
            S3ObjectJ s3Object1 = (S3ObjectJ) full;
            assertEquals(s3Object1.getQualifiedName(), S3AssetJTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetJTest.S3_OBJECT1_NAME);
            Set<ReferenceJ> terms = s3Object1.getMeanings();
            assertNotNull(terms);
            assertEquals(terms.size(), 4);
            Set<AtlanStatus> status =
                    terms.stream().map(ReferenceJ::getRelationshipStatus).collect(Collectors.toSet());
            assertEquals(status.size(), 2);
            assertTrue(status.contains(AtlanStatus.ACTIVE));
            assertTrue(status.contains(AtlanStatus.DELETED));
            for (ReferenceJ term : terms) {
                if (term.getRelationshipStatus() == AtlanStatus.ACTIVE) {
                    assertTrue(term.getGuid().equals(GlossaryJTest.termGuid1)
                            || term.getGuid().equals(GlossaryJTest.termGuid2));
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
            S3ObjectJ result = S3ObjectJ.removeTerms(
                    S3AssetJTest.s3Object1Qame,
                    List.of(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, GlossaryJTest.termGuid1)));
            assertNotNull(result);
            // TODO: verify whether we actually see relationships in the entity response?
            EntityJ full = EntityJ.retrieveFull(S3AssetJTest.s3Object1Guid);
            assertTrue(full instanceof S3ObjectJ);
            S3ObjectJ s3Object1 = (S3ObjectJ) full;
            assertEquals(s3Object1.getQualifiedName(), S3AssetJTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetJTest.S3_OBJECT1_NAME);
            Set<ReferenceJ> terms = s3Object1.getMeanings();
            assertNotNull(terms);
            assertEquals(terms.size(), 4);
            Set<AtlanStatus> status =
                    terms.stream().map(ReferenceJ::getRelationshipStatus).collect(Collectors.toSet());
            assertEquals(status.size(), 2);
            assertTrue(status.contains(AtlanStatus.ACTIVE));
            assertTrue(status.contains(AtlanStatus.DELETED));
            for (ReferenceJ term : terms) {
                if (term.getRelationshipStatus() == AtlanStatus.ACTIVE) {
                    assertEquals(term.getGuid(), GlossaryJTest.termGuid2);
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
            S3ObjectJ result = S3ObjectJ.removeTerms(
                    S3AssetJTest.s3Object1Qame,
                    List.of(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, GlossaryJTest.termGuid2)));
            assertNotNull(result);
            // TODO: verify whether we actually see relationships in the entity response?
            EntityJ full = EntityJ.retrieveFull(S3AssetJTest.s3Object1Guid);
            assertTrue(full instanceof S3ObjectJ);
            S3ObjectJ s3Object1 = (S3ObjectJ) full;
            assertEquals(s3Object1.getQualifiedName(), S3AssetJTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetJTest.S3_OBJECT1_NAME);
            Set<ReferenceJ> terms = s3Object1.getMeanings();
            assertNotNull(terms);
            assertEquals(terms.size(), 4);
            Set<AtlanStatus> status =
                    terms.stream().map(ReferenceJ::getRelationshipStatus).collect(Collectors.toSet());
            assertEquals(status.size(), 1);
            assertTrue(status.contains(AtlanStatus.DELETED));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove assigned assets from a term.");
        }
    }
}
