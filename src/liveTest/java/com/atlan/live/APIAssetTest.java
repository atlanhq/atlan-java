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
import java.util.List;
import org.testng.annotations.Test;

@Test(groups = {"api-asset"})
public class APIAssetTest extends AtlanLiveTest {

    public static final String CONNECTION_NAME = "api-connection";
    public static final String SPEC_NAME = "api-spec";
    public static final String PATH_NAME = "/api/path";

    public static String connectionGuid = null;
    public static String connectionQame = null;

    public static String specGuid = null;
    public static String specQame = null;

    public static String pathGuid = null;
    public static String pathQame = null;

    @Test(groups = {"invalid.connection"})
    void invalidConnection() {
        assertThrows(
                InvalidRequestException.class,
                () -> Connection.creator(CONNECTION_NAME, AtlanConnectorType.API, null, null, null));
    }

    @Test(groups = {"create.connection.api"})
    void createConnection() {
        try {
            String adminRoleGuid = RoleCache.getIdForName("$admin");
            if (adminRoleGuid != null) {
                Connection connection = Connection.creator(
                                CONNECTION_NAME, AtlanConnectorType.API, List.of(adminRoleGuid), null, null)
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
            groups = {"read.connection.api"},
            dependsOnGroups = {"create.connection.api"})
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
            groups = {"create.api.spec"},
            dependsOnGroups = {"read.connection.api"})
    void createSpec() {
        try {
            APISpec spec = APISpec.creator(SPEC_NAME, connectionQame).build();
            EntityMutationResponse response = spec.upsert();
            assertNotNull(response);
            assertTrue(response.getUpdatedEntities().isEmpty());
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getCreatedEntities().size(), 1);
            Entity one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), APISpec.TYPE_NAME);
            assertTrue(one instanceof APISpec);
            spec = (APISpec) one;
            specGuid = spec.getGuid();
            assertNotNull(specGuid);
            specQame = spec.getQualifiedName();
            assertNotNull(specQame);
            assertEquals(spec.getName(), SPEC_NAME);
            assertEquals(spec.getConnectorType(), AtlanConnectorType.API);
            assertEquals(spec.getConnectionQualifiedName(), connectionQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create an API spec.");
        }
    }

    @Test(
            groups = {"create.api.path"},
            dependsOnGroups = {"create.api.spec"})
    void createPath() {
        try {
            APIPath path = APIPath.creator(PATH_NAME, specQame).build();
            EntityMutationResponse response = path.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), APISpec.TYPE_NAME);
            assertTrue(one instanceof APISpec);
            APISpec spec = (APISpec) one;
            assertEquals(spec.getGuid(), specGuid);
            assertEquals(spec.getQualifiedName(), specQame);
            assertEquals(response.getCreatedEntities().size(), 1);
            one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), APIPath.TYPE_NAME);
            assertTrue(one instanceof APIPath);
            path = (APIPath) one;
            pathGuid = path.getGuid();
            assertNotNull(pathGuid);
            pathQame = path.getQualifiedName();
            assertNotNull(pathQame);
            assertEquals(path.getName(), PATH_NAME);
            assertEquals(path.getConnectorType(), AtlanConnectorType.API);
            assertEquals(path.getConnectionQualifiedName(), connectionQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create an API path.");
        }
    }

    @Test(
            groups = {"update.api.spec"},
            dependsOnGroups = {"create.api.spec"})
    void updateSpec() {
        try {
            APISpec updated = APISpec.updateCertificate(specQame, AtlanCertificateStatus.VERIFIED, null);
            assertNotNull(updated);
            assertEquals(updated.getCertificateStatus(), AtlanCertificateStatus.VERIFIED);
            updated = APISpec.updateAnnouncement(
                    specQame, AtlanAnnouncementType.INFORMATION, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
            assertNotNull(updated);
            assertEquals(updated.getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
            assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to update an API spec.");
        }
    }

    @Test(
            groups = {"read.api.spec"},
            dependsOnGroups = {"create.api.*", "update.api.spec"})
    void retrieveSpec() {
        try {
            APISpec spec = APISpec.retrieveByGuid(specGuid);
            assertNotNull(spec);
            assertTrue(spec.isComplete());
            assertEquals(spec.getGuid(), specGuid);
            assertEquals(spec.getQualifiedName(), specQame);
            assertEquals(spec.getName(), SPEC_NAME);
            assertEquals(spec.getCertificateStatus(), AtlanCertificateStatus.VERIFIED);
            assertEquals(spec.getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
            assertEquals(spec.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(spec.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to retrieve an API spec.");
        }
    }

    @Test(
            groups = {"update.api.spec.again"},
            dependsOnGroups = {"read.api.spec"})
    void updateSpecAgain() {
        try {
            APISpec updated = APISpec.removeCertificate(specQame, SPEC_NAME);
            assertNotNull(updated);
            assertNull(updated.getCertificateStatus());
            assertNull(updated.getCertificateStatusMessage());
            assertEquals(updated.getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
            assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
            updated = APISpec.removeAnnouncement(specQame, SPEC_NAME);
            assertNotNull(updated);
            assertNull(updated.getAnnouncementType());
            assertNull(updated.getAnnouncementTitle());
            assertNull(updated.getAnnouncementMessage());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(
                    e, "Unexpected exception while trying to remove certificates and announcements from an API spec.");
        }
    }
}
