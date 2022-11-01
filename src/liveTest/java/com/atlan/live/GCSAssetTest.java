/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.cache.RoleCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.*;
import com.atlan.model.core.Entity;
import com.atlan.model.core.EntityMutationResponse;
import com.atlan.model.enums.*;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.testng.annotations.Test;

@Test(groups = {"gcs-asset"})
public class GCSAssetTest extends AtlanLiveTest {

    public static final String CONNECTION_NAME = "gcs-connection";
    public static final String GCS_BUCKET_NAME = "mybucket";
    public static final String GCS_OBJECT_NAME = "myobject.csv";

    public static String connectionGuid = null;
    public static String connectionQame = null;

    public static String gcsBucketGuid = null;
    public static String gcsBucketQame = null;

    public static String gcsObjectGuid = null;
    public static String gcsObjectQame = null;

    @Test(groups = {"invalid.connection"})
    void invalidConnection() {
        assertThrows(
                InvalidRequestException.class,
                () -> Connection.creator(CONNECTION_NAME, AtlanConnectorType.GCS, null, null, null));
    }

    @Test(groups = {"create.connection.gcs"})
    void createConnection() {
        try {
            String adminRoleGuid = RoleCache.getIdForName("$admin");
            if (adminRoleGuid != null) {
                Connection connection = Connection.creator(
                                CONNECTION_NAME,
                                AtlanConnectorType.GCS,
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
            groups = {"read.connection.gcs"},
            dependsOnGroups = {"create.connection.gcs"})
    void retrieveConnection() {
        Entity minimal = null;
        do {
            try {
                minimal = Entity.retrieveMinimal(connectionGuid);
            } catch (AtlanException e) {
                e.printStackTrace();
                assertNull(e, "Unexpected exception while trying to read-back the created connection.");
            }
        } while (minimal == null);
    }

    @Test(
            groups = {"create.gcs-bucket"},
            dependsOnGroups = {"read.connection.gcs"})
    void createGCSBucket() {
        try {
            GCSBucket gcsBucket =
                    GCSBucket.creator(GCS_BUCKET_NAME, connectionQame).build();
            EntityMutationResponse response = gcsBucket.upsert();
            assertNotNull(response);
            assertTrue(response.getUpdatedEntities().isEmpty());
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getCreatedEntities().size(), 1);
            Entity one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GCSBucket.TYPE_NAME);
            assertTrue(one instanceof GCSBucket);
            gcsBucket = (GCSBucket) one;
            gcsBucketGuid = gcsBucket.getGuid();
            assertNotNull(gcsBucketGuid);
            gcsBucketQame = gcsBucket.getQualifiedName();
            assertNotNull(gcsBucketQame);
            assertEquals(gcsBucket.getName(), GCS_BUCKET_NAME);
            assertEquals(gcsBucket.getConnectorType(), AtlanConnectorType.GCS);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a GCS bucket.");
        }
    }

    @Test(
            groups = {"create.gcs-object"},
            dependsOnGroups = {"create.gcs-bucket"})
    void createGCSObject() {
        try {
            GCSObject gcsObject =
                    GCSObject.creator(GCS_OBJECT_NAME, gcsBucketQame).build();
            EntityMutationResponse response = gcsObject.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GCSBucket.TYPE_NAME);
            assertTrue(one instanceof GCSBucket);
            GCSBucket bucket = (GCSBucket) one;
            assertEquals(bucket.getGuid(), gcsBucketGuid);
            assertEquals(bucket.getQualifiedName(), gcsBucketQame);
            assertEquals(response.getCreatedEntities().size(), 1);
            one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GCSObject.TYPE_NAME);
            assertTrue(one instanceof GCSObject);
            gcsObject = (GCSObject) one;
            gcsObjectGuid = gcsObject.getGuid();
            assertNotNull(gcsObjectGuid);
            gcsObjectQame = gcsObject.getQualifiedName();
            assertNotNull(gcsObjectQame);
            assertEquals(gcsObject.getName(), GCS_OBJECT_NAME);
            assertEquals(gcsObject.getConnectorType(), AtlanConnectorType.GCS);
            assertEquals(gcsObject.getGcsBucketName(), GCS_BUCKET_NAME);
            assertEquals(gcsObject.getGcsBucketQualifiedName(), gcsBucketQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a GCS object.");
        }
    }

    @Test(
            groups = {"update.gcs-bucket"},
            dependsOnGroups = {"create.gcs-bucket"})
    void updateGCSBucket() {
        try {
            GCSBucket updated = GCSBucket.updateCertificate(gcsBucketQame, AtlanCertificateStatus.VERIFIED, null);
            assertNotNull(updated);
            assertEquals(updated.getCertificateStatus(), AtlanCertificateStatus.VERIFIED);
            updated = GCSBucket.updateAnnouncement(
                    gcsBucketQame, AtlanAnnouncementType.INFORMATION, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
            assertNotNull(updated);
            assertEquals(updated.getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
            assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to update a GCS bucket.");
        }
    }

    @Test(
            groups = {"read.gcs-bucket"},
            dependsOnGroups = {"create.gcs-object", "update.gcs-bucket"})
    void retrieveGCSBucket() {
        try {
            GCSBucket bucket = GCSBucket.retrieveByGuid(gcsBucketGuid);
            assertNotNull(bucket);
            assertTrue(bucket.isComplete());
            assertEquals(bucket.getGuid(), gcsBucketGuid);
            assertEquals(bucket.getQualifiedName(), gcsBucketQame);
            assertEquals(bucket.getName(), GCS_BUCKET_NAME);
            assertEquals(bucket.getCertificateStatus(), AtlanCertificateStatus.VERIFIED);
            assertNotNull(bucket.getGcsObjects());
            assertEquals(bucket.getGcsObjects().size(), 1);
            Set<String> types =
                    bucket.getGcsObjects().stream().map(GCSObject::getTypeName).collect(Collectors.toSet());
            assertEquals(types.size(), 1);
            assertTrue(types.contains(GCSObject.TYPE_NAME));
            Set<String> guids =
                    bucket.getGcsObjects().stream().map(GCSObject::getGuid).collect(Collectors.toSet());
            assertEquals(guids.size(), 1);
            assertTrue(guids.contains(gcsObjectGuid));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to retrieve a GCS bucket.");
        }
    }

    @Test(
            groups = {"update.gcs-bucket.again"},
            dependsOnGroups = {"read.gcs-bucket"})
    void updateGCSBucketAgain() {
        try {
            GCSBucket updated = GCSBucket.removeCertificate(gcsBucketQame, GCS_BUCKET_NAME);
            assertNotNull(updated);
            assertNull(updated.getCertificateStatus());
            assertNull(updated.getCertificateStatusMessage());
            assertEquals(updated.getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
            assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
            updated = GCSBucket.removeAnnouncement(gcsBucketQame, GCS_BUCKET_NAME);
            assertNotNull(updated);
            assertNull(updated.getAnnouncementType());
            assertNull(updated.getAnnouncementTitle());
            assertNull(updated.getAnnouncementMessage());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(
                    e, "Unexpected exception while trying to remove certificates and announcements from a GCS bucket.");
        }
    }
}
