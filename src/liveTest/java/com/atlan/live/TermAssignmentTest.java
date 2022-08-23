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
                        GlossaryTest.termQame, GlossaryTest.TERM_NAME, GlossaryTest.glossaryGuid)
                .assignedEntity(Reference.to(S3Object.TYPE_NAME, S3AssetTest.s3Object1Guid))
                .assignedEntity(Reference.by(S3Object.TYPE_NAME, S3AssetTest.s3Object2Qame))
                .build();
        try {
            EntityMutationResponse response = term.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 3);
            Entity full = Entity.retrieveFull(GlossaryTest.termGuid);
            assertTrue(full instanceof GlossaryTerm);
            term = (GlossaryTerm) full;
            assertEquals(term.getQualifiedName(), GlossaryTest.termQame);
            assertEquals(term.getName(), GlossaryTest.TERM_NAME);
            List<Reference> entities = term.getAssignedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 2);
            assertEquals(entities.get(0).getTypeName(), S3Object.TYPE_NAME);
            assertEquals(entities.get(1).getTypeName(), S3Object.TYPE_NAME);
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
                        GlossaryTest.termQame, GlossaryTest.TERM_NAME, GlossaryTest.glossaryGuid)
                .build();
        term.removeAssignedEntities();
        try {
            EntityMutationResponse response = term.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 3);
            Entity full = Entity.retrieveFull(GlossaryTest.termGuid);
            assertTrue(full instanceof GlossaryTerm);
            term = (GlossaryTerm) full;
            assertEquals(term.getQualifiedName(), GlossaryTest.termQame);
            assertEquals(term.getName(), GlossaryTest.TERM_NAME);
            List<Reference> entities = term.getAssignedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 2);
            assertEquals(entities.get(0).getRelationshipStatus(), AtlanStatus.DELETED);
            assertEquals(entities.get(1).getRelationshipStatus(), AtlanStatus.DELETED);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove assigned assets from a term.");
        }
    }

    @Test(
            groups = {"link.asset.term"},
            dependsOnGroups = {"unlink.term.asset"})
    void linkAssetToTerms() {
        S3Object s3Object1 = S3Object.updater(S3AssetTest.s3Object1Qame, S3AssetTest.S3_OBJECT1_NAME)
                .meaning(Reference.to(GlossaryTerm.TYPE_NAME, GlossaryTest.termGuid))
                .build();
        try {
            EntityMutationResponse response = s3Object1.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 2);
            Entity full = Entity.retrieveFull(S3AssetTest.s3Object1Guid);
            assertTrue(full instanceof S3Object);
            s3Object1 = (S3Object) full;
            assertEquals(s3Object1.getQualifiedName(), S3AssetTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetTest.S3_OBJECT1_NAME);
            List<Reference> terms = s3Object1.getMeanings();
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
            assertTrue(guids.contains(GlossaryTest.termGuid));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to link an asset to a term.");
        }
    }

    @Test(
            groups = {"unlink.asset.term"},
            dependsOnGroups = {"link.asset.term"})
    void removeAssetToTermLinks() {
        S3Object s3Object1 = S3Object.updater(S3AssetTest.s3Object1Qame, S3AssetTest.S3_OBJECT1_NAME)
                .build();
        s3Object1.removeMeanings();
        try {
            EntityMutationResponse response = s3Object1.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 2);
            Entity full = Entity.retrieveFull(S3AssetTest.s3Object1Guid);
            assertTrue(full instanceof S3Object);
            s3Object1 = (S3Object) full;
            assertEquals(s3Object1.getQualifiedName(), S3AssetTest.s3Object1Qame);
            assertEquals(s3Object1.getName(), S3AssetTest.S3_OBJECT1_NAME);
            List<Reference> terms = s3Object1.getMeanings();
            assertNotNull(terms);
            assertEquals(terms.size(), 2);
            assertEquals(terms.get(0).getRelationshipStatus(), AtlanStatus.DELETED);
            assertEquals(terms.get(1).getRelationshipStatus(), AtlanStatus.DELETED);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove assigned assets from a term.");
        }
    }
}
