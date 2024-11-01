/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.*;
import com.atlan.model.search.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests all aspects of custom assets.
 */
@Slf4j
public class DetectiDataAssetTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("dd");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.GENERIC;
    public static final String CONNECTION_NAME = PREFIX;

    private static final String DOSSIER_NAME = PREFIX + "-dossier";
    private static final String ELEMENT_NAME1 = PREFIX + "-e1";
    private static final String ELEMENT_NAME2 = PREFIX + "-e2";

    private static Connection connection = null;
    private static DetectiDataDossier dossier = null;
    private static DetectiDataDossierElement element1 = null;
    private static DetectiDataDossierElement element2 = null;

    @Test(groups = {"dd.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"dd.create.dossier"},
            dependsOnGroups = {"dd.create.connection"})
    void createDossier() throws AtlanException {
        DetectiDataDossier toCreate = DetectiDataDossier.creator(DOSSIER_NAME, connection.getQualifiedName())
                .build();
        AssetMutationResponse response = toCreate.save();
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof DetectiDataDossier);
        dossier = (DetectiDataDossier) one;
        assertNotNull(dossier.getGuid());
        assertNotNull(dossier.getQualifiedName());
        assertEquals(dossier.getName(), DOSSIER_NAME);
        assertEquals(dossier.getConnectorType(), CONNECTOR_TYPE);
    }

    @Test(
            groups = {"dd.create.elements"},
            dependsOnGroups = {"dd.create.dossier"})
    void createElements() throws AtlanException {
        DetectiDataDossierElement toCreate1 =
                DetectiDataDossierElement.creator(ELEMENT_NAME1, dossier).build();
        DetectiDataDossierElement toCreate2 =
                DetectiDataDossierElement.creator(ELEMENT_NAME2, dossier).build();
        AssetMutationResponse response = Atlan.getDefaultClient().assets.save(List.of(toCreate1, toCreate2), false);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof DetectiDataDossier);
        DetectiDataDossier t = (DetectiDataDossier) one;
        assertEquals(t.getGuid(), dossier.getGuid());
        assertEquals(t.getQualifiedName(), dossier.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 2);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof DetectiDataDossierElement);
        element1 = (DetectiDataDossierElement) one;
        assertNotNull(element1.getGuid());
        assertNotNull(element1.getQualifiedName());
        assertEquals(element1.getName(), ELEMENT_NAME1);
        assertEquals(element1.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(element1.getDetectiDataDossierName(), DOSSIER_NAME);
        assertEquals(element1.getDetectiDataDossierQualifiedName(), dossier.getQualifiedName());
        one = response.getCreatedAssets().get(1);
        assertTrue(one instanceof DetectiDataDossierElement);
        element2 = (DetectiDataDossierElement) one;
        assertNotNull(element2.getGuid());
        assertNotNull(element2.getQualifiedName());
        assertEquals(element2.getName(), ELEMENT_NAME2);
        assertEquals(element2.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(element2.getDetectiDataDossierName(), DOSSIER_NAME);
        assertEquals(element2.getDetectiDataDossierQualifiedName(), dossier.getQualifiedName());
    }

    @Test(
            groups = {"dd.update.dossier"},
            dependsOnGroups = {"dd.create.elements"})
    void updateDossier() throws AtlanException {
        DetectiDataDossier updated = DetectiDataDossier.updateCertificate(
                dossier.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = DetectiDataDossier.updateAnnouncement(
                dossier.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"dd.read.dossier"},
            dependsOnGroups = {"dd.create.dossier", "dd.update.dossier"})
    void retrieveDossier() throws AtlanException {
        DetectiDataDossier b = DetectiDataDossier.get(dossier.getGuid());
        assertNotNull(b);
        assertTrue(b.isComplete());
        assertEquals(b.getGuid(), dossier.getGuid());
        assertEquals(b.getQualifiedName(), dossier.getQualifiedName());
        assertEquals(b.getName(), DOSSIER_NAME);
        assertEquals(b.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(b.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertNotNull(b.getDetectiDataDossierElements());
        assertEquals(b.getDetectiDataDossierElements().size(), 2);
        Set<String> types = b.getDetectiDataDossierElements().stream()
                .map(IDetectiDataDossierElement::getTypeName)
                .collect(Collectors.toSet());
        assertEquals(types.size(), 1);
        assertTrue(types.contains(DetectiDataDossierElement.TYPE_NAME));
        Set<String> guids = b.getDetectiDataDossierElements().stream()
                .map(IDetectiDataDossierElement::getGuid)
                .collect(Collectors.toSet());
        assertEquals(guids.size(), 2);
        assertTrue(guids.contains(element1.getGuid()));
        assertTrue(guids.contains(element1.getGuid()));
    }

    @Test(
            groups = {"dd.update.dossier.again"},
            dependsOnGroups = {"dd.read.dossier"})
    void updateDossierAgain() throws AtlanException {
        DetectiDataDossier updated = DetectiDataDossier.removeCertificate(dossier.getQualifiedName(), DOSSIER_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = DetectiDataDossier.removeAnnouncement(dossier.getQualifiedName(), DOSSIER_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"dd.search.assets"},
            dependsOnGroups = {"dd.update.dossier.again"})
    void searchAssets() throws AtlanException, InterruptedException {
        IndexSearchRequest index = Atlan.getDefaultClient()
                .assets
                .select()
                .where(Asset.SUPER_TYPE_NAMES.eq(IDetectiData.TYPE_NAME))
                .where(Asset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .pageSize(10)
                .aggregate("type", IReferenceable.TYPE_NAME.bucketBy())
                .sort(Asset.CREATE_TIME.order(SortOrder.Asc))
                .sort(Asset.NAME.order(SortOrder.Asc))
                .includeOnResults(Asset.NAME)
                .includeOnResults(Asset.CONNECTION_QUALIFIED_NAME)
                .toRequest();

        IndexSearchResponse response = retrySearchUntil(index, 3L);

        assertNotNull(response.getAggregations());
        assertEquals(response.getAggregations().size(), 1);
        assertTrue(response.getAggregations().get("type") instanceof AggregationBucketResult);
        assertEquals(
                ((AggregationBucketResult) response.getAggregations().get("type"))
                        .getBuckets()
                        .size(),
                2);

        assertEquals(response.getApproximateCount().longValue(), 3L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 3);

        Asset one = entities.get(0);
        assertTrue(one instanceof DetectiDataDossier);
        assertFalse(one.isComplete());
        DetectiDataDossier b = (DetectiDataDossier) one;
        assertEquals(b.getQualifiedName(), dossier.getQualifiedName());
        assertEquals(b.getName(), dossier.getName());
        assertEquals(b.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof DetectiDataDossierElement);
        assertFalse(one.isComplete());
        DetectiDataDossierElement o = (DetectiDataDossierElement) one;
        assertEquals(o.getQualifiedName(), element1.getQualifiedName());
        assertEquals(o.getName(), element1.getName());
        assertEquals(o.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(2);
        assertTrue(one instanceof DetectiDataDossierElement);
        assertFalse(one.isComplete());
        DetectiDataDossierElement f = (DetectiDataDossierElement) one;
        assertEquals(f.getQualifiedName(), element2.getQualifiedName());
        assertEquals(f.getName(), element2.getName());
        assertEquals(f.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"dd.delete.element"},
            dependsOnGroups = {"dd.update.*", "dd.search.*"})
    void deleteElement() throws AtlanException {
        AssetMutationResponse response = Asset.delete(element2.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof DetectiDataDossierElement);
        DetectiDataDossierElement s = (DetectiDataDossierElement) one;
        assertEquals(s.getGuid(), element2.getGuid());
        assertEquals(s.getQualifiedName(), element2.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"dd.delete.element.read"},
            dependsOnGroups = {"dd.delete.element"})
    void readDeletedElement() throws AtlanException {
        validateDeletedAsset(element2, log);
    }

    @Test(
            groups = {"dd.delete.element.restore"},
            dependsOnGroups = {"dd.delete.element.read"})
    void restoreElement() throws AtlanException {
        assertTrue(DetectiDataDossierElement.restore(element2.getQualifiedName()));
        DetectiDataDossierElement restored = DetectiDataDossierElement.get(element2.getQualifiedName());
        assertEquals(restored.getGuid(), element2.getGuid());
        assertEquals(restored.getQualifiedName(), element2.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"dd.purge.element"},
            dependsOnGroups = {"dd.delete.element.restore"})
    void purgeElement() throws AtlanException {
        AssetMutationResponse response = Asset.purge(element2.getGuid());
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof DetectiDataDossierElement);
        DetectiDataDossierElement s = (DetectiDataDossierElement) one;
        assertEquals(s.getGuid(), element2.getGuid());
        assertEquals(s.getQualifiedName(), element2.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "PURGE");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"dd.purge.connection"},
            dependsOnGroups = {"dd.create.*", "dd.read.*", "dd.search.*", "dd.update.*", "dd.purge.field"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
