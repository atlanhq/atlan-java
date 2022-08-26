package com.atlan.live.jackson;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.live.AtlanLiveTest;
import com.atlan.model.LineageProcessJ;
import com.atlan.model.S3ObjectJ;
import com.atlan.model.core.EntityJ;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.relations.ReferenceJ;
import com.atlan.model.responses.EntityMutationResponseJ;
import java.util.Collections;
import org.testng.annotations.Test;

@Test(groups = {"lineage"})
public class LineageJTest extends AtlanLiveTest {

    private static String lineageGuid = null;
    private static String lineageQame = null;

    @Test(
            groups = {"create.lineage"},
            dependsOnGroups = {"read.s3bucket"})
    void createLineage() {
        final String processName = S3AssetJTest.S3_OBJECT1_NAME + " >> " + S3AssetJTest.S3_OBJECT2_NAME;
        LineageProcessJ process = LineageProcessJ.creator(
                        processName,
                        "s3",
                        S3AssetJTest.CONNECTION_NAME,
                        S3AssetJTest.connectionQame,
                        Collections.singletonList(ReferenceJ.to(S3ObjectJ.TYPE_NAME, S3AssetJTest.s3Object1Guid)),
                        Collections.singletonList(ReferenceJ.to(S3ObjectJ.TYPE_NAME, S3AssetJTest.s3Object2Guid)))
                .build();
        try {
            EntityMutationResponseJ response = process.upsert();
            assertNotNull(response);
            assertEquals(response.getCreatedEntities().size(), 1);
            EntityJ one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertTrue(one instanceof LineageProcessJ);
            process = (LineageProcessJ) one;
            lineageGuid = one.getGuid();
            assertNotNull(lineageGuid);
            lineageQame = process.getQualifiedName();
            assertNotNull(lineageQame);
            assertEquals(process.getName(), processName);
            assertNotNull(process.getInputs());
            assertEquals(process.getInputs().size(), 1);
            ReferenceJ input = process.getInputs().get(0);
            assertNotNull(input);
            assertEquals(input.getTypeName(), S3ObjectJ.TYPE_NAME);
            assertEquals(input.getGuid(), S3AssetJTest.s3Object1Guid);
            assertNotNull(process.getOutputs());
            assertEquals(process.getOutputs().size(), 1);
            ReferenceJ output = process.getOutputs().get(0);
            assertNotNull(output);
            assertEquals(output.getTypeName(), S3ObjectJ.TYPE_NAME);
            assertEquals(output.getGuid(), S3AssetJTest.s3Object2Guid);
            assertEquals(response.getUpdatedEntities().size(), 2);
            EntityJ updated = response.getUpdatedEntities().get(0);
            assertNotNull(updated);
            assertEquals(updated.getTypeName(), S3ObjectJ.TYPE_NAME);
            updated = response.getUpdatedEntities().get(1);
            assertEquals(updated.getTypeName(), S3ObjectJ.TYPE_NAME);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected error during lineage creation.");
        }
    }

    @Test(
            groups = {"purge.lineage"},
            dependsOnGroups = {"create.*", "update.*", "read.*", "search.*", "link.*", "unlink.*"},
            alwaysRun = true)
    void deleteLineage() {
        try {
            EntityMutationResponseJ response = LineageProcessJ.purge(lineageGuid);
            assertNotNull(response);
            assertEquals(response.getDeletedEntities().size(), 1);
            EntityJ one = response.getDeletedEntities().get(0);
            assertNotNull(one);
            assertTrue(one instanceof LineageProcessJ);
            LineageProcessJ process = (LineageProcessJ) one;
            assertEquals(process.getGuid(), lineageGuid);
            assertEquals(process.getQualifiedName(), lineageQame);
            assertEquals(process.getStatus(), AtlanStatus.DELETED);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected error during lineage deletion.");
        }
    }
}
