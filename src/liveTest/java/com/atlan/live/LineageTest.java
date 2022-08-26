package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.model.LineageProcess;
import com.atlan.model.S3Object;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.relations.Reference;
import com.atlan.model.responses.EntityMutationResponse;
import java.util.Collections;
import org.testng.annotations.Test;

@Test(groups = {"lineage"})
public class LineageTest extends AtlanLiveTest {

    private static String lineageGuid = null;
    private static String lineageQame = null;

    @Test(
            groups = {"create.lineage"},
            dependsOnGroups = {"read.s3bucket"})
    void createLineage() {
        final String processName = S3AssetTest.S3_OBJECT1_NAME + " >> " + S3AssetTest.S3_OBJECT2_NAME;
        LineageProcess process = LineageProcess.creator(
                        processName,
                        "s3",
                        S3AssetTest.CONNECTION_NAME,
                        S3AssetTest.connectionQame,
                        Collections.singletonList(Reference.to(S3Object.TYPE_NAME, S3AssetTest.s3Object1Guid)),
                        Collections.singletonList(Reference.to(S3Object.TYPE_NAME, S3AssetTest.s3Object2Guid)))
                .build();
        try {
            EntityMutationResponse response = process.upsert();
            assertNotNull(response);
            assertEquals(response.getCreatedEntities().size(), 1);
            Entity one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertTrue(one instanceof LineageProcess);
            process = (LineageProcess) one;
            lineageGuid = one.getGuid();
            assertNotNull(lineageGuid);
            lineageQame = process.getQualifiedName();
            assertNotNull(lineageQame);
            assertEquals(process.getName(), processName);
            assertNotNull(process.getInputs());
            assertEquals(process.getInputs().size(), 1);
            Reference input = process.getInputs().get(0);
            assertNotNull(input);
            assertEquals(input.getTypeName(), S3Object.TYPE_NAME);
            assertEquals(input.getGuid(), S3AssetTest.s3Object1Guid);
            assertNotNull(process.getOutputs());
            assertEquals(process.getOutputs().size(), 1);
            Reference output = process.getOutputs().get(0);
            assertNotNull(output);
            assertEquals(output.getTypeName(), S3Object.TYPE_NAME);
            assertEquals(output.getGuid(), S3AssetTest.s3Object2Guid);
            assertEquals(response.getUpdatedEntities().size(), 2);
            Entity updated = response.getUpdatedEntities().get(0);
            assertNotNull(updated);
            assertEquals(updated.getTypeName(), S3Object.TYPE_NAME);
            updated = response.getUpdatedEntities().get(1);
            assertEquals(updated.getTypeName(), S3Object.TYPE_NAME);
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
            EntityMutationResponse response = LineageProcess.purge(lineageGuid);
            assertNotNull(response);
            assertEquals(response.getDeletedEntities().size(), 1);
            Entity one = response.getDeletedEntities().get(0);
            assertNotNull(one);
            assertTrue(one instanceof LineageProcess);
            LineageProcess process = (LineageProcess) one;
            assertEquals(process.getGuid(), lineageGuid);
            assertEquals(process.getQualifiedName(), lineageQame);
            assertEquals(process.getStatus(), AtlanStatus.DELETED);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected error during lineage deletion.");
        }
    }
}
