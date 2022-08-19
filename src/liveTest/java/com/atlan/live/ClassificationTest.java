package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.model.GlossaryTerm;
import com.atlan.model.core.Classification;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanClassificationColor;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.responses.EntityMutationResponse;
import com.atlan.model.typedefs.ClassificationDef;
import java.util.Set;
import java.util.stream.Collectors;
import org.testng.annotations.Test;

public class ClassificationTest extends AtlanLiveTest {

    public static final String CLASSIFICATION_NAME = "JC Test Classification";

    public static String classificationGuid;
    public static String classificationQame;

    @Test(groups = {"create.classification", "create"})
    void createClassification() {
        try {
            ClassificationDef classificationDef =
                    ClassificationDef.toCreate(CLASSIFICATION_NAME, AtlanClassificationColor.YELLOW);
            ClassificationDef response = classificationDef.create();
            assertNotNull(response);
            assertEquals(response.getCategory(), AtlanTypeCategory.CLASSIFICATION);
            classificationQame = response.getName();
            assertNotNull(classificationQame);
            assertNotEquals(classificationQame, CLASSIFICATION_NAME);
            classificationGuid = response.getGuid();
            assertNotNull(classificationGuid);
            assertEquals(response.getDisplayName(), CLASSIFICATION_NAME);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a new classification.");
        }
    }

    @Test(
            groups = {"update.term.classification", "update"},
            dependsOnGroups = {"link.remove2"})
    void updateTermClassification() {
        try {
            GlossaryTerm toUpdate =
                    GlossaryTerm.toUpdate(GlossaryTest.termQame, GlossaryTest.TERM_NAME, GlossaryTest.glossaryGuid);
            toUpdate = toUpdate.toBuilder()
                    .classification(Classification.of(CLASSIFICATION_NAME, GlossaryTest.termGuid))
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
            assertTrue(typeNames.contains(CLASSIFICATION_NAME));
            Set<String> clsNames = term.getClassificationNames();
            assertNotNull(clsNames);
            assertEquals(clsNames.size(), 1);
            assertTrue(clsNames.contains(CLASSIFICATION_NAME));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to add classification to a term.");
        }
    }

    @Test(
            groups = {"remove.term.classification", "update"},
            dependsOnGroups = {"update.term.classification"})
    void removeTermClassification() {
        try {
            GlossaryTerm toUpdate =
                    GlossaryTerm.toUpdate(GlossaryTest.termQame, GlossaryTest.TERM_NAME, GlossaryTest.glossaryGuid);
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
            groups = {"purge.classification", "purge"},
            dependsOnGroups = {"create", "read", "update", "term.purge"},
            alwaysRun = true)
    void purgeClassification() {
        try {
            ClassificationDef.purge(CLASSIFICATION_NAME);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a new classification.");
        }
    }
}
