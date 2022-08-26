package com.atlan.live.jackson;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.live.AtlanLiveTest;
import com.atlan.model.GlossaryTermJ;
import com.atlan.model.S3ObjectJ;
import com.atlan.model.core.ClassificationJ;
import com.atlan.model.core.EntityJ;
import com.atlan.model.enums.AtlanClassificationColor;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.responses.EntityMutationResponseJ;
import com.atlan.model.typedefs.ClassificationDefJ;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.testng.annotations.Test;

@Test(groups = {"classification"})
public class ClassificationJTest extends AtlanLiveTest {

    public static final String CLASSIFICATION_NAME1 = "JC Test Classification 1";
    public static final String CLASSIFICATION_NAME2 = "JC Test Classification 2";

    @Test(groups = {"create.classification"})
    void createClassification() {
        try {
            ClassificationDefJ classificationDef =
                    ClassificationDefJ.toCreate(CLASSIFICATION_NAME1, AtlanClassificationColor.GREEN);
            ClassificationDefJ response = classificationDef.create();
            assertNotNull(response);
            assertEquals(response.getCategory(), AtlanTypeCategory.CLASSIFICATION);
            String classificationQame1 = response.getName();
            assertNotNull(classificationQame1);
            assertNotEquals(classificationQame1, CLASSIFICATION_NAME1);
            assertNotNull(response.getGuid());
            assertEquals(response.getDisplayName(), CLASSIFICATION_NAME1);
            classificationDef = ClassificationDefJ.toCreate(CLASSIFICATION_NAME2, AtlanClassificationColor.YELLOW);
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
            dependsOnGroups = {"unlink.asset.term.*", "unlink.term.asset"})
    void updateTermClassification() {
        try {
            GlossaryTermJ toUpdate = GlossaryTermJ.updater(
                            GlossaryJTest.termQame1, GlossaryJTest.TERM_NAME1, GlossaryJTest.glossaryGuid)
                    .classification(ClassificationJ.of(CLASSIFICATION_NAME1, GlossaryJTest.termGuid1))
                    .build();
            EntityMutationResponseJ response = toUpdate.upsert(true, false);
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            EntityJ one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof GlossaryTermJ);
            GlossaryTermJ term = (GlossaryTermJ) one;
            assertNotNull(term);
            assertEquals(term.getQualifiedName(), GlossaryJTest.termQame1);
            Set<ClassificationJ> classifications = term.getClassifications();
            assertNotNull(classifications);
            assertEquals(classifications.size(), 1);
            Set<String> typeNames =
                    classifications.stream().map(ClassificationJ::getTypeName).collect(Collectors.toSet());
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
            GlossaryTermJ toUpdate = GlossaryTermJ.updater(
                            GlossaryJTest.termQame1, GlossaryJTest.TERM_NAME1, GlossaryJTest.glossaryGuid)
                    .build();
            toUpdate.removeClassifications();
            EntityMutationResponseJ response = toUpdate.upsert(true, false);
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            EntityJ one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof GlossaryTermJ);
            GlossaryTermJ term = (GlossaryTermJ) one;
            assertNotNull(term);
            assertEquals(term.getQualifiedName(), GlossaryJTest.termQame1);
            Set<ClassificationJ> classifications = term.getClassifications();
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
            S3ObjectJ.addClassifications(
                    S3AssetJTest.s3Object2Qame, List.of(CLASSIFICATION_NAME1, CLASSIFICATION_NAME2));
            EntityJ result = S3ObjectJ.retrieveFull(S3AssetJTest.s3Object2Guid);
            assertTrue(result instanceof S3ObjectJ);
            S3ObjectJ object = (S3ObjectJ) result;
            assertNotNull(object);
            Set<ClassificationJ> classifications = object.getClassifications();
            assertNotNull(classifications);
            assertEquals(classifications.size(), 2);
            Set<String> typeNames =
                    classifications.stream().map(ClassificationJ::getTypeName).collect(Collectors.toSet());
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
                () -> S3ObjectJ.addClassifications(S3AssetJTest.s3Object2Qame, List.of(CLASSIFICATION_NAME1)));
    }

    @Test(
            groups = {"unlink.classification.s3object"},
            dependsOnGroups = {"link.classification.s3object"})
    void removeObjectClassification() {
        try {
            S3ObjectJ.removeClassification(S3AssetJTest.s3Object2Qame, CLASSIFICATION_NAME2);
            EntityJ result = S3ObjectJ.retrieveFull(S3AssetJTest.s3Object2Guid);
            assertTrue(result instanceof S3ObjectJ);
            S3ObjectJ object = (S3ObjectJ) result;
            assertNotNull(object);
            Set<ClassificationJ> classifications = object.getClassifications();
            assertNotNull(classifications);
            assertEquals(classifications.size(), 1);
            Set<String> typeNames =
                    classifications.stream().map(ClassificationJ::getTypeName).collect(Collectors.toSet());
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
                () -> S3ObjectJ.removeClassification(S3AssetJTest.s3Object2Qame, CLASSIFICATION_NAME2));
    }

    @Test(
            groups = {"link.classification.s3object.again"},
            dependsOnGroups = {"unlink.classification.s3object.nonexistent"})
    void addObjectClassificationAgain() {
        try {
            S3ObjectJ.addClassifications(S3AssetJTest.s3Object2Qame, List.of(CLASSIFICATION_NAME2));
            EntityJ result = S3ObjectJ.retrieveFull(S3AssetJTest.s3Object2Guid);
            assertTrue(result instanceof S3ObjectJ);
            S3ObjectJ object = (S3ObjectJ) result;
            assertNotNull(object);
            Set<ClassificationJ> classifications = object.getClassifications();
            assertNotNull(classifications);
            assertEquals(classifications.size(), 2);
            Set<String> typeNames =
                    classifications.stream().map(ClassificationJ::getTypeName).collect(Collectors.toSet());
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
            S3ObjectJ object = S3ObjectJ.updater(S3AssetJTest.s3Object2Qame, S3AssetJTest.S3_OBJECT2_NAME)
                    .build();
            object.removeClassifications();
            EntityMutationResponseJ response = object.upsert(true, false);
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getCreatedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            EntityJ one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof S3ObjectJ);
            object = (S3ObjectJ) one;
            assertNotNull(object);
            assertEquals(object.getQualifiedName(), S3AssetJTest.s3Object2Qame);
            Set<ClassificationJ> classifications = object.getClassifications();
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
                "purge.term.*",
                "purge.connection"
            },
            alwaysRun = true)
    void purgeClassification1() {
        try {
            ClassificationDefJ.purge(CLASSIFICATION_NAME1);
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
                "purge.term.*",
                "purge.connection"
            },
            alwaysRun = true)
    void purgeClassification2() {
        try {
            ClassificationDefJ.purge(CLASSIFICATION_NAME2);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to delete a classification.");
        }
    }
}
