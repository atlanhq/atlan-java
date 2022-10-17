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

@Test(groups = {"gds-asset"})
public class DataStudioAssetTest extends AtlanLiveTest {

    public static final String CONNECTION_NAME = "gds-connection";
    public static final String REPORT_NAME = "gds-report";
    public static final String SOURCE_NAME = "gds-source";

    public static String connectionGuid = null;
    public static String connectionQame = null;

    public static String reportGuid = null;
    public static String reportQame = null;

    public static String sourceGuid = null;
    public static String sourceQame = null;

    @Test(groups = {"invalid.connection"})
    void invalidConnection() {
        assertThrows(
                InvalidRequestException.class,
                () -> Connection.creator(CONNECTION_NAME, AtlanConnectorType.DATASTUDIO, null, null, null));
    }

    @Test(groups = {"create.connection.gds"})
    void createConnection() {
        try {
            String adminRoleGuid = RoleCache.getIdForName("$admin");
            if (adminRoleGuid != null) {
                Connection connection = Connection.creator(
                                CONNECTION_NAME, AtlanConnectorType.DATASTUDIO, List.of(adminRoleGuid), null, null)
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
            groups = {"read.connection.gds"},
            dependsOnGroups = {"create.connection.gds"})
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
            groups = {"create.gds.report"},
            dependsOnGroups = {"read.connection.gds"})
    void createReport() {
        try {
            DataStudioAsset report = DataStudioAsset.creator(
                            REPORT_NAME, connectionQame, GoogleDataStudioAssetType.REPORT)
                    .build();
            EntityMutationResponse response = report.upsert();
            assertNotNull(response);
            assertTrue(response.getUpdatedEntities().isEmpty());
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getCreatedEntities().size(), 1);
            Entity one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), DataStudioAsset.TYPE_NAME);
            assertTrue(one instanceof DataStudioAsset);
            report = (DataStudioAsset) one;
            reportGuid = report.getGuid();
            assertNotNull(reportGuid);
            reportQame = report.getQualifiedName();
            assertNotNull(reportQame);
            assertEquals(report.getName(), REPORT_NAME);
            assertEquals(report.getConnectorType(), AtlanConnectorType.DATASTUDIO);
            assertEquals(report.getConnectionQualifiedName(), connectionQame);
            assertEquals(report.getDataStudioAssetType(), GoogleDataStudioAssetType.REPORT);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a Google Data Studio report.");
        }
    }

    @Test(
            groups = {"create.gds.source"},
            dependsOnGroups = {"read.connection.gds"})
    void createSource() {
        try {
            DataStudioAsset source = DataStudioAsset.creator(
                            SOURCE_NAME, connectionQame, GoogleDataStudioAssetType.DATA_SOURCE)
                    .build();
            EntityMutationResponse response = source.upsert();
            assertNotNull(response);
            assertTrue(response.getUpdatedEntities().isEmpty());
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getCreatedEntities().size(), 1);
            Entity one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), DataStudioAsset.TYPE_NAME);
            assertTrue(one instanceof DataStudioAsset);
            source = (DataStudioAsset) one;
            sourceGuid = source.getGuid();
            assertNotNull(sourceGuid);
            sourceQame = source.getQualifiedName();
            assertNotNull(sourceQame);
            assertEquals(source.getName(), SOURCE_NAME);
            assertEquals(source.getConnectorType(), AtlanConnectorType.DATASTUDIO);
            assertEquals(source.getConnectionQualifiedName(), connectionQame);
            assertEquals(source.getDataStudioAssetType(), GoogleDataStudioAssetType.DATA_SOURCE);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a Google Data Studio data source.");
        }
    }

    @Test(
            groups = {"update.gds.report"},
            dependsOnGroups = {"create.gds.report"})
    void updateReport() {
        try {
            DataStudioAsset updated =
                    DataStudioAsset.updateCertificate(reportQame, AtlanCertificateStatus.VERIFIED, null);
            assertNotNull(updated);
            assertEquals(updated.getCertificateStatus(), AtlanCertificateStatus.VERIFIED);
            updated = DataStudioAsset.updateAnnouncement(
                    reportQame, AtlanAnnouncementType.INFORMATION, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
            assertNotNull(updated);
            assertEquals(updated.getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
            assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to update a Google Data Studio report.");
        }
    }

    @Test(
            groups = {"read.gds.report"},
            dependsOnGroups = {"create.gds.*", "update.gds.report"})
    void retrieveReport() {
        try {
            DataStudioAsset report = DataStudioAsset.retrieveByGuid(reportGuid);
            assertNotNull(report);
            assertTrue(report.isComplete());
            assertEquals(report.getGuid(), reportGuid);
            assertEquals(report.getQualifiedName(), reportQame);
            assertEquals(report.getName(), REPORT_NAME);
            assertEquals(report.getCertificateStatus(), AtlanCertificateStatus.VERIFIED);
            assertEquals(report.getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
            assertEquals(report.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(report.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to retrieve a Google Data Studio report.");
        }
    }

    @Test(
            groups = {"update.gds.report.again"},
            dependsOnGroups = {"read.gds.report"})
    void updateReportAgain() {
        try {
            DataStudioAsset updated = DataStudioAsset.removeCertificate(reportQame, REPORT_NAME);
            assertNotNull(updated);
            assertNull(updated.getCertificateStatus());
            assertNull(updated.getCertificateStatusMessage());
            assertEquals(updated.getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
            assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
            updated = DataStudioAsset.removeAnnouncement(reportQame, REPORT_NAME);
            assertNotNull(updated);
            assertNull(updated.getAnnouncementType());
            assertNull(updated.getAnnouncementTitle());
            assertNull(updated.getAnnouncementMessage());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(
                    e,
                    "Unexpected exception while trying to remove certificates and announcements from a Google Data Studio report.");
        }
    }
}
