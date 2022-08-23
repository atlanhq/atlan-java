package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.GlossaryTerm;
import com.atlan.model.S3Object;
import com.atlan.model.core.Classification;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanClassificationColor;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.responses.EntityMutationResponse;
import com.atlan.model.typedefs.ClassificationDef;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.testng.annotations.Test;

@Test(groups = {"classification"})
public class ClassificationTest extends AtlanLiveTest {

    public static final String CLASSIFICATION_NAME1 = "JC Test Classification 1";
    public static final String CLASSIFICATION_NAME2 = "JC Test Classification 2";

    @Test(groups = {"create.classification"})
    void createClassification() {
        try {
            ClassificationDef classificationDef =
                    ClassificationDef.toCreate(CLASSIFICATION_NAME1, AtlanClassificationColor.GREEN);
            ClassificationDef response = classificationDef.create();
            assertNotNull(response);
            assertEquals(response.getCategory(), AtlanTypeCategory.CLASSIFICATION);
            String classificationQame1 = response.getName();
            assertNotNull(classificationQame1);
            assertNotEquals(classificationQame1, CLASSIFICATION_NAME1);
            assertNotNull(response.getGuid());
            assertEquals(response.getDisplayName(), CLASSIFICATION_NAME1);
            classificationDef = ClassificationDef.toCreate(CLASSIFICATION_NAME2, AtlanClassificationColor.YELLOW);
            response = classificationDef.create();
            assertNotNull(response);
            assertEquals(response.getCategory(), AtlanTypeCategory.CLASSIFICATION);
            String classificationQame2 = response.getName();
            assertNotNull(classificationQame2);
            assertNotEquals(classificationQame2, CLASSIFICATION_NAME2);
            assertNotNull(response.getGuid());
            assertEquals(response.getDisplayName(), CLASSIFICATION_NAME2);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create new classifications.");
        }
    }

    @Test(
            groups = {"link.classification.term"},
            dependsOnGroups = {"unlink.asset.term", "unlink.term.asset"})
    void updateTermClassification() {
        try {
            GlossaryTerm toUpdate = GlossaryTerm.updater(
                            GlossaryTest.termQame, GlossaryTest.TERM_NAME, GlossaryTest.glossaryGuid)
                    .classification(Classification.of(CLASSIFICATION_NAME1, GlossaryTest.termGuid))
                    .build();
            EntityMutationResponse response = toUpdate.upsert(true, false);
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof GlossaryTerm);
            GlossaryTerm term = (GlossaryTerm) one;
            assertNotNull(term);
            assertEquals(term.getQualifiedName(), GlossaryTest.termQame);
            Set<Classification> classifications = term.getClassifications();
            assertNotNull(classifications);
            assertEquals(classifications.size(), 1);
            Set<String> typeNames =
                    classifications.stream().map(Classification::getTypeName).collect(Collectors.toSet());
            assertTrue(typeNames.contains(CLASSIFICATION_NAME1));
            Set<String> clsNames = term.getClassificationNames();
            assertNotNull(clsNames);
            assertEquals(clsNames.size(), 1);
            assertTrue(clsNames.contains(CLASSIFICATION_NAME1));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to add classification to a term.");
        }
    }

    @Test(
            groups = {"unlink.classification.term"},
            dependsOnGroups = {"link.classification.term"})
    void removeTermClassification() {
        try {
            GlossaryTerm toUpdate = GlossaryTerm.updater(
                            GlossaryTest.termQame, GlossaryTest.TERM_NAME, GlossaryTest.glossaryGuid)
                    .build();
            toUpdate.removeClassifications();
            EntityMutationResponse response = toUpdate.upsert(true, false);
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof GlossaryTerm);
            GlossaryTerm term = (GlossaryTerm) one;
            assertNotNull(term);
            assertEquals(term.getQualifiedName(), GlossaryTest.termQame);
            Set<Classification> classifications = term.getClassifications();
            assertNotNull(classifications);
            assertTrue(classifications.isEmpty());
            Set<String> clsNames = term.getClassificationNames();
            assertNotNull(clsNames);
            assertTrue(clsNames.isEmpty());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove classifications from a term.");
        }
    }

    @Test(
            groups = {"link.classification.s3object"},
            dependsOnGroups = {"create.s3object"})
    void addObjectClassification() {
        try {
            S3Object.addClassifications(S3AssetTest.s3Object2Qame, List.of(CLASSIFICATION_NAME1, CLASSIFICATION_NAME2));
            Entity result = S3Object.retrieveFull(S3AssetTest.s3Object2Guid);
            assertTrue(result instanceof S3Object);
            S3Object object = (S3Object) result;
            assertNotNull(object);
            Set<Classification> classifications = object.getClassifications();
            assertNotNull(classifications);
            assertEquals(classifications.size(), 2);
            Set<String> typeNames =
                    classifications.stream().map(Classification::getTypeName).collect(Collectors.toSet());
            assertNotNull(typeNames);
            assertTrue(typeNames.contains(CLASSIFICATION_NAME1));
            assertTrue(typeNames.contains(CLASSIFICATION_NAME2));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to add classification to S3 object.");
        }
    }

    @Test(
            groups = {"link.classification.s3object.duplicate"},
            dependsOnGroups = {"link.classification.s3object"})
    void addDuplicateObjectClassification() {
        assertThrows(
                InvalidRequestException.class,
                () -> S3Object.addClassifications(S3AssetTest.s3Object2Qame, List.of(CLASSIFICATION_NAME1)));
    }

    @Test(
            groups = {"unlink.classification.s3object"},
            dependsOnGroups = {"link.classification.s3object"})
    void removeObjectClassification() {
        try {
            S3Object.removeClassification(S3AssetTest.s3Object2Qame, CLASSIFICATION_NAME2);
            Entity result = S3Object.retrieveFull(S3AssetTest.s3Object2Guid);
            assertTrue(result instanceof S3Object);
            S3Object object = (S3Object) result;
            assertNotNull(object);
            Set<Classification> classifications = object.getClassifications();
            assertNotNull(classifications);
            assertEquals(classifications.size(), 1);
            Set<String> typeNames =
                    classifications.stream().map(Classification::getTypeName).collect(Collectors.toSet());
            assertNotNull(typeNames);
            assertTrue(typeNames.contains(CLASSIFICATION_NAME1));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove a classification from an asset.");
        }
    }

    @Test(
            groups = {"unlink.classification.s3object.nonexistent"},
            dependsOnGroups = {"unlink.classification.s3object"})
    void removeNonexistentObjectClassification() {
        assertThrows(
                InvalidRequestException.class,
                () -> S3Object.removeClassification(S3AssetTest.s3Object2Qame, CLASSIFICATION_NAME2));
    }

    @Test(
            groups = {"link.classification.s3object.again"},
            dependsOnGroups = {"unlink.classification.s3object.nonexistent"})
    void addObjectClassificationAgain() {
        try {
            S3Object.addClassifications(S3AssetTest.s3Object2Qame, List.of(CLASSIFICATION_NAME2));
            Entity result = S3Object.retrieveFull(S3AssetTest.s3Object2Guid);
            assertTrue(result instanceof S3Object);
            S3Object object = (S3Object) result;
            assertNotNull(object);
            Set<Classification> classifications = object.getClassifications();
            assertNotNull(classifications);
            assertEquals(classifications.size(), 2);
            Set<String> typeNames =
                    classifications.stream().map(Classification::getTypeName).collect(Collectors.toSet());
            assertNotNull(typeNames);
            assertTrue(typeNames.contains(CLASSIFICATION_NAME1));
            assertTrue(typeNames.contains(CLASSIFICATION_NAME2));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to add classification to S3 object.");
        }
    }

    @Test(
            groups = {"unlink.classification.s3object.all"},
            dependsOnGroups = {"link.classification.s3object.again"},
            alwaysRun = true)
    void removeObjectClassifications() {
        try {
            S3Object object = S3Object.updater(S3AssetTest.s3Object2Qame, S3AssetTest.S3_OBJECT2_NAME)
                    .build();
            object.removeClassifications();
            EntityMutationResponse response = object.upsert(true, false);
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof S3Object);
            object = (S3Object) one;
            assertNotNull(object);
            assertEquals(object.getQualifiedName(), S3AssetTest.s3Object2Qame);
            Set<Classification> classifications = object.getClassifications();
            assertNotNull(classifications);
            assertTrue(classifications.isEmpty());
            Set<String> clsNames = object.getClassificationNames();
            assertNotNull(clsNames);
            assertTrue(clsNames.isEmpty());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove classifications from an S3 object.");
        }
    }

    @Test(
            groups = {"purge.classification"},
            dependsOnGroups = {
                "create.*",
                "read.*",
                "update.*",
                "link.*",
                "unlink.*",
                "search.*",
                "purge.term",
                "purge.connection"
            },
            alwaysRun = true)
    void purgeClassification1() {
        try {
            ClassificationDef.purge(CLASSIFICATION_NAME1);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to delete a classification.");
        }
    }

    @Test(
            groups = {"purge.classification"},
            dependsOnGroups = {
                "create.*",
                "read.*",
                "update.*",
                "link.*",
                "unlink.*",
                "search.*",
                "purge.term",
                "purge.connection"
            },
            alwaysRun = true)
    void purgeClassification2() {
        try {
            ClassificationDef.purge(CLASSIFICATION_NAME2);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to delete a classification.");
        }
    }
}
