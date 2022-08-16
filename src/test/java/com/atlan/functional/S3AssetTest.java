package com.atlan.functional;

import static org.testng.Assert.*;

import com.atlan.api.WorkflowsEndpoint;
import com.atlan.cache.RoleCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.PermissionException;
import com.atlan.model.*;
import com.atlan.model.admin.Packages;
import com.atlan.model.admin.Workflow;
import com.atlan.model.admin.WorkflowSearchRequest;
import com.atlan.model.admin.WorkflowSearchResult;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanConnectionCategory;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.AtlanWorkflowPhase;
import com.atlan.model.relations.Reference;
import com.atlan.model.responses.EntityMutationResponse;
import com.atlan.model.responses.WorkflowResponse;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class S3AssetTest extends AtlanLiveTest {

    public static final String CONNECTION_NAME = "jc-test-connection";
    public static final String S3_BUCKET_NAME = "jc-test-bucket";
    public static final String S3_BUCKET_ARN = "aws::production:jc-test-bucket";
    public static final String S3_OBJECT1_NAME = "jc-test-object-source";
    public static final String S3_OBJECT1_ARN = "aws::production:jc-test-bucket:a/prefix/jc-test-source.csv";
    public static final String S3_OBJECT2_NAME = "jc-test-object-target";
    public static final String S3_OBJECT2_ARN = "aws::production:jc-test-bucket:a/prefix/jc-test-target.csv";

    private static String connectionGuid = null;
    private static String connectionQame = null;

    public static String s3BucketGuid = null;
    public static String s3BucketQame = null;

    public static String s3Object1Guid = null;
    public static String s3Object1Qame = null;
    public static String s3Object2Guid = null;
    public static String s3Object2Qame = null;

    private static String lineageGuid = null;
    private static String lineageQame = null;

    private static String workflowName = null;

    @Test(groups = {"connection.invalid"})
    void invalidConnection() {
        assertThrows(
                InvalidRequestException.class,
                () -> Connection.toCreate(
                        CONNECTION_NAME, AtlanConnectionCategory.OBJECT_STORE, "s3", null, null, null));
    }

    @Test(groups = {"connection.create", "create"})
    void createConnection() {
        try {
            String adminRoleGuid = RoleCache.getIdForName("$admin");
            if (adminRoleGuid != null) {
                Connection connection = Connection.toCreate(
                        CONNECTION_NAME,
                        AtlanConnectionCategory.OBJECT_STORE,
                        "s3",
                        Collections.singletonList(adminRoleGuid),
                        null,
                        null);
                EntityMutationResponse response = connection.upsert();
                assertNotNull(response);
                assertTrue(response.getUpdatedEntities().isEmpty());
                assertTrue(response.getDeletedEntities().isEmpty());
                assertEquals(response.getCreatedEntities().size(), 1);
                Entity one = response.getCreatedEntities().get(0);
                assertNotNull(one);
                assertEquals(one.getTypeName(), Connection.TYPE_NAME);
                assertTrue(one instanceof Connection);
                connection = (Connection) one;
                connectionGuid = connection.getGuid();
                assertNotNull(connectionGuid);
                connectionQame = connection.getQualifiedName();
                assertNotNull(connectionQame);
                assertEquals(connection.getName(), CONNECTION_NAME);
            }
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a connection.");
        }
    }

    @Test(
            groups = {"connection.retrieve", "read"},
            dependsOnGroups = {"connection.create"})
    void retrieveConnection() throws InterruptedException {
        Entity full = null;
        do {
            try {
                full = Entity.retrieveFull(connectionGuid);
            } catch (PermissionException pe) {
                log.warn("Connection not yet accessible, will wait and retry...");
                Thread.sleep(5000);
            } catch (AtlanException e) {
                e.printStackTrace();
                assertNull(e, "Unexpected exception while trying to create a connection.");
            }
        } while (full == null);
    }

    @Test(
            groups = {"s3bucket.create", "create"},
            dependsOnGroups = {"connection.retrieve"})
    void createS3Bucket() {
        try {
            S3Bucket s3Bucket = S3Bucket.toCreate(S3_BUCKET_NAME, connectionQame, S3_BUCKET_ARN);
            EntityMutationResponse response = s3Bucket.upsert();
            assertNotNull(response);
            assertTrue(response.getUpdatedEntities().isEmpty());
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getCreatedEntities().size(), 1);
            Entity one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), S3Bucket.TYPE_NAME);
            assertTrue(one instanceof S3Bucket);
            s3Bucket = (S3Bucket) one;
            s3BucketGuid = s3Bucket.getGuid();
            assertNotNull(s3BucketGuid);
            s3BucketQame = s3Bucket.getQualifiedName();
            assertNotNull(s3BucketQame);
            assertEquals(s3Bucket.getName(), S3_BUCKET_NAME);
            assertEquals(s3Bucket.getAwsArn(), S3_BUCKET_ARN);
            assertEquals(s3Bucket.getConnectorName(), "s3");
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create an S3 bucket.");
        }
    }

    @Test(
            groups = {"s3object.create", "create"},
            dependsOnGroups = {"s3bucket.create"})
    void createS3Object1() {
        try {
            S3Object s3Object = S3Object.toCreate(S3_OBJECT1_NAME, connectionQame, S3_OBJECT1_ARN);
            s3Object = s3Object.toBuilder()
                    .s3BucketName(S3_BUCKET_NAME)
                    .s3BucketQualifiedName(s3BucketQame)
                    .bucket(Reference.to(S3Bucket.TYPE_NAME, s3BucketGuid))
                    .build();
            EntityMutationResponse response = s3Object.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), S3Bucket.TYPE_NAME);
            assertTrue(one instanceof S3Bucket);
            S3Bucket bucket = (S3Bucket) one;
            assertEquals(bucket.getGuid(), s3BucketGuid);
            assertEquals(bucket.getQualifiedName(), s3BucketQame);
            assertEquals(response.getCreatedEntities().size(), 1);
            one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), S3Object.TYPE_NAME);
            assertTrue(one instanceof S3Object);
            s3Object = (S3Object) one;
            s3Object1Guid = s3Object.getGuid();
            assertNotNull(s3Object1Guid);
            s3Object1Qame = s3Object.getQualifiedName();
            assertNotNull(s3Object1Qame);
            assertEquals(s3Object.getName(), S3_OBJECT1_NAME);
            assertEquals(s3Object.getAwsArn(), S3_OBJECT1_ARN);
            assertEquals(s3Object.getConnectorName(), "s3");
            assertEquals(s3Object.getS3BucketName(), S3_BUCKET_NAME);
            assertEquals(s3Object.getS3BucketQualifiedName(), s3BucketQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create an S3 object.");
        }
    }

    @Test(
            groups = {"s3object.create", "create"},
            dependsOnGroups = {"s3bucket.create"})
    void createS3Object2() {
        try {
            S3Object s3Object = S3Object.toCreate(S3_OBJECT2_NAME, connectionQame, S3_OBJECT2_ARN);
            s3Object = s3Object.toBuilder()
                    .s3BucketName(S3_BUCKET_NAME)
                    .s3BucketQualifiedName(s3BucketQame)
                    .bucket(Reference.to(S3Bucket.TYPE_NAME, s3BucketGuid))
                    .build();
            EntityMutationResponse response = s3Object.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), S3Bucket.TYPE_NAME);
            assertTrue(one instanceof S3Bucket);
            S3Bucket bucket = (S3Bucket) one;
            assertEquals(bucket.getGuid(), s3BucketGuid);
            assertEquals(bucket.getQualifiedName(), s3BucketQame);
            assertEquals(response.getCreatedEntities().size(), 1);
            one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), S3Object.TYPE_NAME);
            assertTrue(one instanceof S3Object);
            s3Object = (S3Object) one;
            s3Object2Guid = s3Object.getGuid();
            assertNotNull(s3Object2Guid);
            s3Object2Qame = s3Object.getQualifiedName();
            assertNotNull(s3Object2Qame);
            assertEquals(s3Object.getName(), S3_OBJECT2_NAME);
            assertEquals(s3Object.getAwsArn(), S3_OBJECT2_ARN);
            assertEquals(s3Object.getConnectorName(), "s3");
            assertEquals(s3Object.getS3BucketName(), S3_BUCKET_NAME);
            assertEquals(s3Object.getS3BucketQualifiedName(), s3BucketQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create an S3 object.");
        }
    }

    @Test(
            groups = {"s3bucket.retrieve", "read"},
            dependsOnGroups = {"s3object.create", "readme.create"})
    void retrieveS3Bucket() {
        try {
            Entity full = Entity.retrieveFull(s3BucketGuid);
            assertNotNull(full);
            assertTrue(full instanceof S3Bucket);
            S3Bucket bucket = (S3Bucket) full;
            assertEquals(bucket.getGuid(), s3BucketGuid);
            assertEquals(bucket.getQualifiedName(), s3BucketQame);
            assertEquals(bucket.getName(), S3_BUCKET_NAME);
            assertNotNull(bucket.getObjects());
            assertEquals(bucket.getObjects().size(), 2);
            Reference one = bucket.getObjects().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), S3Object.TYPE_NAME);
            assertNotNull(bucket.getReadme());
            one = bucket.getReadme();
            assertNotNull(one);
            assertEquals(one.getTypeName(), Readme.TYPE_NAME);
            assertEquals(one.getGuid(), LinkingTest.readmeGuid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to retrieve an S3 bucket.");
        }
    }

    @Test(
            groups = {"lineage.create", "create"},
            dependsOnGroups = {"s3bucket.retrieve"})
    void createLineage() {
        final String processName = S3_OBJECT1_NAME + " >> " + S3_OBJECT2_NAME;
        LineageProcess process = LineageProcess.toCreate(
                processName,
                "s3",
                CONNECTION_NAME,
                connectionQame,
                Collections.singletonList(Reference.to(S3Object.TYPE_NAME, s3Object1Guid)),
                Collections.singletonList(Reference.to(S3Object.TYPE_NAME, s3Object2Guid)));
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
            assertEquals(input.getGuid(), s3Object1Guid);
            assertNotNull(process.getOutputs());
            assertEquals(process.getOutputs().size(), 1);
            Reference output = process.getOutputs().get(0);
            assertNotNull(output);
            assertEquals(output.getTypeName(), S3Object.TYPE_NAME);
            assertEquals(output.getGuid(), s3Object2Guid);
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
            groups = {"lineage.purge", "purge"},
            dependsOnGroups = {"create", "update", "read"},
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

    @Test(
            groups = {"connection.purge", "purge"},
            dependsOnGroups = {"lineage.purge"},
            alwaysRun = true)
    void purgeConnection() {
        try {
            Workflow deleteWorkflow = Packages.getConnectionDelete(connectionQame, true);
            WorkflowResponse response = deleteWorkflow.run();
            assertNotNull(response);
            workflowName = response.getMetadata().getName();
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to delete a connection.");
        }
    }

    @Test(
            groups = {"workflow.status", "purge"},
            dependsOnGroups = {"connection.purge"})
    void monitorStatus() {
        try {
            AtlanWorkflowPhase status = null;
            do {
                final WorkflowSearchResult runDetails = WorkflowSearchRequest.findLatestRun(workflowName);
                if (runDetails != null) {
                    status = runDetails.getStatus();
                }
                log.info("Workflow status: {}", status);
                Thread.sleep(5000);
            } while (status != null && status != AtlanWorkflowPhase.SUCCESS);
        } catch (AtlanException | InterruptedException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to monitor deletion workflow.");
        }
    }

    @Test(
            groups = {"workflow.run.archive", "purge"},
            dependsOnGroups = {"workflow.status"})
    void archiveWorkflowRun() {
        try {
            WorkflowsEndpoint.archive(workflowName);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to archive the workflow run.");
        }
    }
}
