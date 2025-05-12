/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.*;
import com.atlan.model.search.*;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests all aspects of File assets.
 */
@Slf4j
public class FileTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("File");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.FILE;
    public static final String CONNECTION_NAME = PREFIX;
    private static final String FILE_NAME = PREFIX + "-file.pdf";

    private static Connection connection = null;
    private static File file = null;

    @Test(groups = {"file.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(client, CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"file.create.file"},
            dependsOnGroups = {"file.create.connection"})
    void createFile() throws AtlanException {
        File toCreate = File.creator(FILE_NAME, connection.getQualifiedName(), FileType.PDF)
                .filePath("https://www.example.com")
                .build();
        AssetMutationResponse response = toCreate.save(client);
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof File);
        file = (File) one;
        assertNotNull(file.getGuid());
        assertNotNull(file.getQualifiedName());
        assertEquals(file.getQualifiedName(), File.generateQualifiedName(connection.getQualifiedName(), FILE_NAME));
        assertEquals(file.getName(), FILE_NAME);
        assertEquals(file.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(file.getConnectionQualifiedName(), connection.getQualifiedName());
        assertEquals(file.getFileType(), FileType.PDF);
        assertEquals(file.getFilePath(), "https://www.example.com");
    }

    @Test(
            groups = {"file.update.file"},
            dependsOnGroups = {"file.create.file"})
    void updateFile() throws AtlanException {
        File updated = File.updateCertificate(client, file.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = File.updateAnnouncement(
                client, file.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"file.read.file"},
            dependsOnGroups = {"file.update.file"})
    void retrieveFile() throws AtlanException {
        File r = File.get(client, file.getGuid(), true);
        assertNotNull(r);
        assertTrue(r.isComplete());
        assertEquals(r.getGuid(), file.getGuid());
        assertEquals(r.getQualifiedName(), file.getQualifiedName());
        assertEquals(r.getName(), FILE_NAME);
        assertEquals(r.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(r.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertEquals(r.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(r.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(r.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"file.update.file.again"},
            dependsOnGroups = {"file.read.file"})
    void updateFileAgain() throws AtlanException {
        File updated = File.removeCertificate(client, file.getQualifiedName(), FILE_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = File.removeAnnouncement(client, file.getQualifiedName(), FILE_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"file.search.files"},
            dependsOnGroups = {"file.update.file.again"})
    void searchFiles() throws AtlanException, InterruptedException {
        IndexSearchRequest index = File.select(client)
                .where(Asset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .pageSize(10)
                .aggregate("type", IReferenceable.TYPE_NAME.bucketBy())
                .sort(Asset.CREATE_TIME.order(SortOrder.Asc))
                .includeOnResults(Asset.NAME)
                .includeOnResults(Asset.CONNECTION_QUALIFIED_NAME)
                .toRequest();

        IndexSearchResponse response = retrySearchUntil(index, 1L);

        assertNotNull(response.getAggregations());
        assertEquals(response.getAggregations().size(), 1);
        assertTrue(response.getAggregations().get("type") instanceof AggregationBucketResult);
        assertEquals(
                ((AggregationBucketResult) response.getAggregations().get("type"))
                        .getBuckets()
                        .size(),
                1);

        assertEquals(response.getApproximateCount().longValue(), 1L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 1);

        Asset one = entities.get(0);
        assertTrue(one instanceof File);
        assertFalse(one.isComplete());
        File asset = (File) one;
        assertEquals(asset.getQualifiedName(), file.getQualifiedName());
        assertEquals(asset.getName(), file.getName());
        assertEquals(asset.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"file.delete.file"},
            dependsOnGroups = {"file.update.*", "file.search.*"})
    void deleteFile() throws AtlanException {
        AssetMutationResponse response = Asset.delete(client, file.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof File);
        File s = (File) one;
        assertEquals(s.getGuid(), file.getGuid());
        assertEquals(s.getQualifiedName(), file.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"file.delete.file.read"},
            dependsOnGroups = {"file.delete.file"})
    void readDeletedFile() throws AtlanException {
        validateDeletedAsset(file, log);
    }

    @Test(
            groups = {"file.delete.file.restore"},
            dependsOnGroups = {"file.delete.file.read"})
    void restoreFile() throws AtlanException {
        assertTrue(File.restore(client, file.getQualifiedName()));
        File restored = File.get(client, file.getQualifiedName());
        assertFalse(restored.isComplete());
        assertEquals(restored.getGuid(), file.getGuid());
        assertEquals(restored.getQualifiedName(), file.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"file.purge.file"},
            dependsOnGroups = {"file.delete.file.restore"})
    void purgeFile() throws AtlanException {
        AssetMutationResponse response = Asset.purge(client, file.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof File);
        File s = (File) one;
        assertEquals(s.getGuid(), file.getGuid());
        assertEquals(s.getQualifiedName(), file.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "PURGE");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"file.purge.connection"},
            dependsOnGroups = {"file.create.*", "file.read.*", "file.search.*", "file.update.*", "file.purge.*"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(client, connection.getQualifiedName(), log);
    }
}
