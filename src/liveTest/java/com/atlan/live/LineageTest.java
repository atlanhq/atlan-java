/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.S3Object;
import com.atlan.model.core.Entity;
import com.atlan.model.core.EntityMutationResponse;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.lineage.LineageProcess;
import com.atlan.model.lineage.LineageRequest;
import com.atlan.model.lineage.LineageResponse;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.testng.annotations.Test;

@Test(groups = {"lineage"})
public class LineageTest extends AtlanLiveTest {

    private static String lineageGuid1 = null;
    private static String lineageQame1 = null;
    private static String lineageGuid2 = null;
    private static String lineageQame2 = null;

    @Test(
            groups = {"create.lineage.1"},
            dependsOnGroups = {"read.s3bucket"})
    void createLineage1() {
        final String processName = S3AssetTest.S3_OBJECT1_NAME + " >> " + S3AssetTest.S3_OBJECT2_NAME;
        LineageProcess process = LineageProcess.creator(
                        processName,
                        AtlanConnectorType.S3,
                        S3AssetTest.CONNECTION_NAME,
                        S3AssetTest.connectionQame,
                        Collections.singletonList(S3Object.refByGuid(S3AssetTest.s3Object1Guid)),
                        Collections.singletonList(S3Object.refByGuid(S3AssetTest.s3Object2Guid)))
                .build();
        try {
            EntityMutationResponse response = process.upsert();
            assertNotNull(response);
            assertEquals(response.getCreatedEntities().size(), 1);
            Entity one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertTrue(one instanceof LineageProcess);
            process = (LineageProcess) one;
            lineageGuid1 = one.getGuid();
            assertNotNull(lineageGuid1);
            lineageQame1 = process.getQualifiedName();
            assertNotNull(lineageQame1);
            assertEquals(process.getName(), processName);
            assertNotNull(process.getInputs());
            assertEquals(process.getInputs().size(), 1);
            Asset input = process.getInputs().get(0);
            assertNotNull(input);
            assertEquals(input.getTypeName(), S3Object.TYPE_NAME);
            assertEquals(input.getGuid(), S3AssetTest.s3Object1Guid);
            assertNotNull(process.getOutputs());
            assertEquals(process.getOutputs().size(), 1);
            Asset output = process.getOutputs().get(0);
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
            groups = {"create.lineage.2"},
            dependsOnGroups = {"create.lineage.1"})
    void createLineage2() {
        final String processName = S3AssetTest.S3_OBJECT2_NAME + " >> " + S3AssetTest.S3_OBJECT3_NAME;
        LineageProcess process = LineageProcess.creator(
                        processName,
                        AtlanConnectorType.S3,
                        S3AssetTest.CONNECTION_NAME,
                        S3AssetTest.connectionQame,
                        Collections.singletonList(S3Object.refByGuid(S3AssetTest.s3Object2Guid)),
                        Collections.singletonList(S3Object.refByGuid(S3AssetTest.s3Object3Guid)))
                .build();
        try {
            EntityMutationResponse response = process.upsert();
            assertNotNull(response);
            assertEquals(response.getCreatedEntities().size(), 1);
            Entity one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertTrue(one instanceof LineageProcess);
            process = (LineageProcess) one;
            lineageGuid2 = one.getGuid();
            assertNotNull(lineageGuid2);
            lineageQame2 = process.getQualifiedName();
            assertNotNull(lineageQame2);
            assertEquals(process.getName(), processName);
            assertNotNull(process.getInputs());
            assertEquals(process.getInputs().size(), 1);
            Asset input = process.getInputs().get(0);
            assertNotNull(input);
            assertEquals(input.getTypeName(), S3Object.TYPE_NAME);
            assertEquals(input.getGuid(), S3AssetTest.s3Object2Guid);
            assertNotNull(process.getOutputs());
            assertEquals(process.getOutputs().size(), 1);
            Asset output = process.getOutputs().get(0);
            assertNotNull(output);
            assertEquals(output.getTypeName(), S3Object.TYPE_NAME);
            assertEquals(output.getGuid(), S3AssetTest.s3Object3Guid);
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
            groups = {"read.lineage.1"},
            dependsOnGroups = {"create.lineage.*"})
    void fetchLineage1() {
        try {
            LineageRequest lineage = LineageRequest.builder()
                    .guid(S3AssetTest.s3Object1Guid)
                    .hideProcess(true)
                    .build();
            LineageResponse response = lineage.fetch();
            assertNotNull(response);
            assertEquals(response.getBaseEntityGuid(), S3AssetTest.s3Object1Guid);
            assertTrue(response.getUpstreamEntityGuids().isEmpty());
            Set<String> downstreamGuids = response.getDownstreamEntityGuids();
            assertNotNull(downstreamGuids);
            assertEquals(downstreamGuids.size(), 1);
            assertTrue(downstreamGuids.contains(S3AssetTest.s3Object2Guid));
            assertEquals(response.getDownstreamEntities().size(), 1);
            downstreamGuids = response.getDownstreamProcessGuids();
            assertNotNull(downstreamGuids);
            assertEquals(downstreamGuids.size(), 1);
            assertTrue(downstreamGuids.contains(lineageGuid1));
            List<String> dfsDownstreamGuids = response.getAllDownstreamEntityGuidsDFS();
            System.out.println("Downstream GUIDs: " + dfsDownstreamGuids);
            assertEquals(dfsDownstreamGuids.size(), 3);
            assertEquals(dfsDownstreamGuids.get(0), S3AssetTest.s3Object1Guid);
            assertEquals(dfsDownstreamGuids.get(1), S3AssetTest.s3Object2Guid);
            assertEquals(dfsDownstreamGuids.get(2), S3AssetTest.s3Object3Guid);
            List<Entity> dfsDownstream = response.getAllDownstreamEntitiesDFS();
            assertEquals(dfsDownstream.size(), 3);
            Entity one = dfsDownstream.get(2);
            assertTrue(one instanceof S3Object);
            S3Object object = (S3Object) one;
            assertEquals(object.getQualifiedName(), S3AssetTest.s3Object3Qame);
            List<String> dfsUpstreamGuids = response.getAllUpstreamEntityGuidsDFS();
            assertEquals(dfsUpstreamGuids.size(), 1);
            assertEquals(dfsUpstreamGuids.get(0), S3AssetTest.s3Object1Guid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected error during lineage retrieval.");
        }
    }

    @Test(
            groups = {"read.lineage.2"},
            dependsOnGroups = {"create.lineage.*"})
    void fetchLineage2() {
        try {
            LineageRequest lineage = LineageRequest.builder()
                    .guid(S3AssetTest.s3Object2Guid)
                    .hideProcess(true)
                    .build();
            LineageResponse response = lineage.fetch();
            assertNotNull(response);
            assertEquals(response.getBaseEntityGuid(), S3AssetTest.s3Object2Guid);
            Set<String> upstreamGuids = response.getUpstreamEntityGuids();
            assertNotNull(upstreamGuids);
            assertEquals(upstreamGuids.size(), 1);
            assertTrue(upstreamGuids.contains(S3AssetTest.s3Object1Guid));
            assertEquals(response.getUpstreamEntities().size(), 1);
            Entity one = response.getUpstreamEntities().get(0);
            assertNotNull(one);
            assertTrue(one instanceof S3Object);
            S3Object object = (S3Object) one;
            assertEquals(object.getQualifiedName(), S3AssetTest.s3Object1Qame);
            upstreamGuids = response.getUpstreamProcessGuids();
            assertNotNull(upstreamGuids);
            assertEquals(upstreamGuids.size(), 1);
            assertTrue(upstreamGuids.contains(lineageGuid1));
            Set<String> downstreamGuids = response.getDownstreamEntityGuids();
            assertNotNull(downstreamGuids);
            assertEquals(downstreamGuids.size(), 1);
            assertTrue(downstreamGuids.contains(S3AssetTest.s3Object3Guid));
            assertEquals(response.getDownstreamEntities().size(), 1);
            one = response.getDownstreamEntities().get(0);
            assertNotNull(one);
            assertTrue(one instanceof S3Object);
            object = (S3Object) one;
            assertEquals(object.getQualifiedName(), S3AssetTest.s3Object3Qame);
            downstreamGuids = response.getDownstreamProcessGuids();
            assertNotNull(downstreamGuids);
            assertEquals(downstreamGuids.size(), 1);
            assertTrue(downstreamGuids.contains(lineageGuid2));
            List<String> dfsDownstreamGuids = response.getAllDownstreamEntityGuidsDFS();
            assertEquals(dfsDownstreamGuids.size(), 2);
            assertEquals(dfsDownstreamGuids.get(0), S3AssetTest.s3Object2Guid);
            assertEquals(dfsDownstreamGuids.get(1), S3AssetTest.s3Object3Guid);
            List<Entity> dfsDownstream = response.getAllDownstreamEntitiesDFS();
            assertEquals(dfsDownstream.size(), 2);
            one = dfsDownstream.get(1);
            assertTrue(one instanceof S3Object);
            object = (S3Object) one;
            assertEquals(object.getQualifiedName(), S3AssetTest.s3Object3Qame);
            List<String> dfsUpstreamGuids = response.getAllUpstreamEntityGuidsDFS();
            assertEquals(dfsUpstreamGuids.size(), 2);
            assertEquals(dfsUpstreamGuids.get(0), S3AssetTest.s3Object2Guid);
            assertEquals(dfsUpstreamGuids.get(1), S3AssetTest.s3Object1Guid);
            List<Entity> dfsUpstream = response.getAllUpstreamEntitiesDFS();
            assertEquals(dfsUpstream.size(), 2);
            one = dfsUpstream.get(1);
            assertTrue(one instanceof S3Object);
            object = (S3Object) one;
            assertEquals(object.getQualifiedName(), S3AssetTest.s3Object1Qame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected error during lineage retrieval.");
        }
    }

    @Test(
            groups = {"read.lineage.3"},
            dependsOnGroups = {"create.lineage.*"})
    void fetchLineage3() {
        try {
            LineageRequest lineage = LineageRequest.builder()
                    .guid(S3AssetTest.s3Object3Guid)
                    .hideProcess(true)
                    .build();
            LineageResponse response = lineage.fetch();
            assertNotNull(response);
            assertEquals(response.getBaseEntityGuid(), S3AssetTest.s3Object3Guid);
            Set<String> upstreamGuids = response.getUpstreamEntityGuids();
            assertNotNull(upstreamGuids);
            assertEquals(upstreamGuids.size(), 1);
            assertEquals(response.getUpstreamEntities().size(), 1);
            assertTrue(upstreamGuids.contains(S3AssetTest.s3Object2Guid));
            upstreamGuids = response.getUpstreamProcessGuids();
            assertNotNull(upstreamGuids);
            assertEquals(upstreamGuids.size(), 1);
            assertTrue(upstreamGuids.contains(lineageGuid2));
            assertTrue(response.getDownstreamEntityGuids().isEmpty());
            List<String> dfsDownstreamGuids = response.getAllDownstreamEntityGuidsDFS();
            assertEquals(dfsDownstreamGuids.size(), 1);
            assertEquals(dfsDownstreamGuids.get(0), S3AssetTest.s3Object3Guid);
            List<Entity> dfsDownstream = response.getAllDownstreamEntitiesDFS();
            assertEquals(dfsDownstream.size(), 1);
            Entity one = dfsDownstream.get(0);
            assertTrue(one instanceof S3Object);
            S3Object object = (S3Object) one;
            assertEquals(object.getQualifiedName(), S3AssetTest.s3Object3Qame);
            List<String> dfsUpstreamGuids = response.getAllUpstreamEntityGuidsDFS();
            System.out.println("Upstream GUIDs: " + dfsUpstreamGuids);
            assertEquals(dfsUpstreamGuids.size(), 3);
            assertEquals(dfsUpstreamGuids.get(0), S3AssetTest.s3Object3Guid);
            assertEquals(dfsUpstreamGuids.get(1), S3AssetTest.s3Object2Guid);
            assertEquals(dfsUpstreamGuids.get(2), S3AssetTest.s3Object1Guid);
            List<Entity> dfsUpstream = response.getAllUpstreamEntitiesDFS();
            assertEquals(dfsUpstream.size(), 3);
            one = dfsUpstream.get(2);
            assertTrue(one instanceof S3Object);
            object = (S3Object) one;
            assertEquals(object.getQualifiedName(), S3AssetTest.s3Object1Qame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected error during lineage retrieval.");
        }
    }

    @Test(
            groups = {"read.lineage.*"},
            dependsOnGroups = {"create.lineage.*"})
    void fetchLineageInvalid() {
        try {
            LineageRequest lineage = LineageRequest.builder()
                    .guid(S3AssetTest.s3Object1Guid)
                    .hideProcess(false)
                    .build();
            LineageResponse response = lineage.fetch();
            assertNotNull(response);
            assertEquals(response.getBaseEntityGuid(), S3AssetTest.s3Object1Guid);
            assertThrows(InvalidRequestException.class, response::getDownstreamEntityGuids);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected error during lineage retrieval.");
        }
    }

    @Test(
            groups = {"purge.lineage"},
            dependsOnGroups = {"create.*", "update.*", "read.*", "search.*", "link.*", "unlink.*"},
            alwaysRun = true)
    void deleteLineage() {
        try {
            EntityMutationResponse response = LineageProcess.purge(lineageGuid1);
            assertNotNull(response);
            assertEquals(response.getDeletedEntities().size(), 1);
            Entity one = response.getDeletedEntities().get(0);
            assertNotNull(one);
            assertTrue(one instanceof LineageProcess);
            LineageProcess process = (LineageProcess) one;
            assertEquals(process.getGuid(), lineageGuid1);
            assertEquals(process.getQualifiedName(), lineageQame1);
            assertEquals(process.getStatus(), AtlanStatus.DELETED);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected error during lineage deletion.");
        }
    }
}
