package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.cache.RoleCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.PermissionException;
import com.atlan.model.*;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.*;
import com.atlan.model.relations.Reference;
import com.atlan.model.responses.EntityMutationResponse;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

@Test(groups = {"s3asset"})
public class S3AssetTest extends AtlanLiveTest {

    private static final Logger log = LoggerFactory.getLogger(S3AssetTest.class);

    public static final String CONNECTION_NAME = "jc-test-connection";
    public static final String S3_BUCKET_NAME = "jc-test-bucket";
    public static final String S3_BUCKET_ARN = "aws::production:jc-test-bucket";
    public static final String S3_OBJECT1_NAME = "jc-test-object-source";
    public static final String S3_OBJECT1_ARN = "aws::production:jc-test-bucket:a/prefix/jc-test-source.csv";
    public static final String S3_OBJECT2_NAME = "jc-test-object-target";
    public static final String S3_OBJECT2_ARN = "aws::production:jc-test-bucket:a/prefix/jc-test-target.csv";

    public static String connectionGuid = null;
    public static String connectionQame = null;

    public static String s3BucketGuid = null;
    public static String s3BucketQame = null;

    public static String s3Object1Guid = null;
    public static String s3Object1Qame = null;
    public static String s3Object2Guid = null;
    public static String s3Object2Qame = null;

    @Test(groups = {"invalid.connection"})
    void invalidConnection() {
        assertThrows(
                InvalidRequestException.class,
                () -> Connection.creator(
                        CONNECTION_NAME, AtlanConnectionCategory.OBJECT_STORE, "s3", null, null, null));
    }

    @Test(groups = {"create.connection"})
    void createConnection() {
        try {
            String adminRoleGuid = RoleCache.getIdForName("$admin");
            if (adminRoleGuid != null) {
                Connection connection = Connection.creator(
                                CONNECTION_NAME,
                                AtlanConnectionCategory.OBJECT_STORE,
                                "s3",
                                Collections.singletonList(adminRoleGuid),
                                null,
                                null)
                        .build();
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
            groups = {"read.connection"},
            dependsOnGroups = {"create.connection"})
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
            groups = {"create.s3bucket"},
            dependsOnGroups = {"read.connection"})
    void createS3Bucket() {
        try {
            S3Bucket s3Bucket = S3Bucket.creator(S3_BUCKET_NAME, connectionQame, S3_BUCKET_ARN)
                    .build();
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
            groups = {"create.s3object"},
            dependsOnGroups = {"create.s3bucket"})
    void createS3Object1() {
        try {
            S3Object s3Object = S3Object.creator(S3_OBJECT1_NAME, connectionQame, S3_OBJECT1_ARN)
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
            groups = {"create.s3object"},
            dependsOnGroups = {"create.s3bucket"})
    void createS3Object2() {
        try {
            S3Object s3Object = S3Object.creator(S3_OBJECT2_NAME, connectionQame, S3_OBJECT2_ARN)
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
            groups = {"update.s3bucket"},
            dependsOnGroups = {"create.s3bucket"})
    void updateS3Bucket() {
        try {
            S3Bucket updated = S3Bucket.updateCertificate(s3BucketQame, AtlanCertificateStatus.VERIFIED, null);
            assertNotNull(updated);
            assertEquals(updated.getCertificateStatus(), AtlanCertificateStatus.VERIFIED);
            updated = S3Bucket.updateAnnouncement(
                    s3BucketQame, AtlanAnnouncementType.INFORMATION, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
            assertNotNull(updated);
            assertEquals(updated.getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
            assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to update an S3 bucket.");
        }
    }

    @Test(
            groups = {"read.s3bucket"},
            dependsOnGroups = {"create.s3object", "create.readme", "update.s3bucket"})
    void retrieveS3Bucket() {
        try {
            Entity full = Entity.retrieveFull(s3BucketGuid);
            assertNotNull(full);
            assertTrue(full instanceof S3Bucket);
            S3Bucket bucket = (S3Bucket) full;
            assertEquals(bucket.getGuid(), s3BucketGuid);
            assertEquals(bucket.getQualifiedName(), s3BucketQame);
            assertEquals(bucket.getName(), S3_BUCKET_NAME);
            assertEquals(bucket.getCertificateStatus(), AtlanCertificateStatus.VERIFIED);
            assertNotNull(bucket.getObjects());
            assertEquals(bucket.getObjects().size(), 2);
            Reference one = bucket.getObjects().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), S3Object.TYPE_NAME);
            assertNotNull(bucket.getReadme());
            one = bucket.getReadme();
            assertNotNull(one);
            assertEquals(one.getTypeName(), Readme.TYPE_NAME);
            assertEquals(one.getGuid(), ReadmeTest.readmeGuid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to retrieve an S3 bucket.");
        }
    }

    @Test(
            groups = {"update.s3bucket.again"},
            dependsOnGroups = {"read.s3bucket"})
    void updateS3BucketAgain() {
        try {
            S3Bucket updated = S3Bucket.removeCertificate(s3BucketQame, S3_BUCKET_NAME);
            assertNotNull(updated);
            assertNull(updated.getCertificateStatus());
            assertNull(updated.getCertificateStatusMessage());
            assertEquals(updated.getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
            assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
            updated = S3Bucket.removeAnnouncement(s3BucketQame, S3_BUCKET_NAME);
            assertNotNull(updated);
            assertNull(updated.getAnnouncementType());
            assertNull(updated.getAnnouncementTitle());
            assertNull(updated.getAnnouncementMessage());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(
                    e, "Unexpected exception while trying to remove certificates and announcements from an S3 bucket.");
        }
    }
}
