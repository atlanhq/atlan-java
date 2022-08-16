package com.atlan.functional;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.model.GlossaryTerm;
import com.atlan.model.Readme;
import com.atlan.model.S3Bucket;
import com.atlan.model.S3Object;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.relations.Reference;
import com.atlan.model.responses.EntityMutationResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.testng.annotations.Test;

public class LinkingTest extends AtlanLiveTest {

    private static final String readmeContent =
            "<h1>This is a test</h1><h2>With some headings</h2><p>And some normal content.</p>";

    public static String readmeGuid = null;
    public static String readmeQame = null;

    @Test(
            groups = {"readme.create", "create"},
            dependsOnGroups = {"s3object.create"})
    void addReadme() {
        try {
            Readme readme = Readme.toCreate(
                    S3Bucket.TYPE_NAME, S3AssetTest.s3BucketGuid, S3AssetTest.S3_BUCKET_NAME, readmeContent);
            EntityMutationResponse response = readme.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getCreatedEntities().size(), 1);
            Entity one = response.getCreatedEntities().get(0);
            assertTrue(one instanceof Readme);
            readme = (Readme) one;
            assertNotNull(readme);
            readmeGuid = readme.getGuid();
            assertNotNull(readmeGuid);
            readmeQame = readme.getQualifiedName();
            assertNotNull(readmeQame);
            assertEquals(readme.getDescription(), readmeContent);
            assertEquals(response.getUpdatedEntities().size(), 1);
            one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof S3Bucket);
            S3Bucket s3Bucket = (S3Bucket) one;
            assertNotNull(s3Bucket);
            assertEquals(s3Bucket.getGuid(), S3AssetTest.s3BucketGuid);
            assertEquals(s3Bucket.getQualifiedName(), S3AssetTest.s3BucketQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a README.");
        }
    }

    @Test(
            groups = {"link.term2asset", "update"},
            dependsOnGroups = {"create"})
    void linkTermToAssets() {
        GlossaryTerm term =
                GlossaryTerm.toUpdate(GlossaryTest.termQame, GlossaryTest.TERM_NAME, GlossaryTest.glossaryGuid);
        term = term.toBuilder()
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
            groups = {"link.remove1", "update"},
            dependsOnGroups = {"create", "link.term2asset"})
    void removeTermToAssetLinks() {
        GlossaryTerm term =
                GlossaryTerm.toUpdate(GlossaryTest.termQame, GlossaryTest.TERM_NAME, GlossaryTest.glossaryGuid);
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
            groups = {"link.asset2term", "update"},
            dependsOnGroups = {"create", "link.remove1"})
    void linkAssetToTerms() {
        S3Object s3Object1 = S3Object.toUpdate(S3AssetTest.s3Object1Qame, S3AssetTest.S3_OBJECT1_NAME);
        s3Object1 = s3Object1.toBuilder()
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
            groups = {"link.remove2", "update"},
            dependsOnGroups = {"create", "link.asset2term"})
    void removeAssetToTermLinks() {
        S3Object s3Object1 = S3Object.toUpdate(S3AssetTest.s3Object1Qame, S3AssetTest.S3_OBJECT1_NAME);
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

    @Test(
            groups = {"readme.purge", "purge"},
            dependsOnGroups = {"create", "update", "read"},
            alwaysRun = true)
    void purgeReadme() {
        try {
            EntityMutationResponse response = Readme.purge(readmeGuid);
            assertNotNull(response);
            assertEquals(response.getDeletedEntities().size(), 1);
            Entity one = response.getDeletedEntities().get(0);
            assertNotNull(one);
            assertTrue(one instanceof Readme);
            Readme readme = (Readme) one;
            assertEquals(readme.getGuid(), readmeGuid);
            assertEquals(readme.getQualifiedName(), readmeQame);
            assertEquals(readme.getStatus(), AtlanStatus.DELETED);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected error during README deletion.");
        }
    }
}
