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
public class CustomAssetTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("custom");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.IICS;
    public static final String CONNECTION_NAME = PREFIX;

    private static final String DATASET_NAME = PREFIX + "-ds";
    private static final String TABLE_NAME = PREFIX + "-tbl";
    private static final String FIELD_NAME1 = PREFIX + "-f1";
    private static final String FIELD_NAME2 = PREFIX + "-f2";

    private static Connection connection = null;
    private static CustomDataset dataset = null;
    private static CustomTable table = null;
    private static CustomField field1 = null;
    private static CustomField field2 = null;

    @Test(groups = {"custom.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"custom.create.dataset"},
            dependsOnGroups = {"custom.create.connection"})
    void createDataset() throws AtlanException {
        CustomDataset toCreate = CustomDataset.creator(DATASET_NAME, connection.getQualifiedName())
                .build();
        AssetMutationResponse response = toCreate.save();
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof CustomDataset);
        dataset = (CustomDataset) one;
        assertNotNull(dataset.getGuid());
        assertNotNull(dataset.getQualifiedName());
        assertEquals(dataset.getName(), DATASET_NAME);
        assertEquals(dataset.getConnectorType(), CONNECTOR_TYPE);
    }

    @Test(
            groups = {"custom.create.table"},
            dependsOnGroups = {"custom.create.dataset"})
    void createTable() throws AtlanException {
        CustomTable toCreate = CustomTable.creator(TABLE_NAME, dataset).build();
        AssetMutationResponse response = toCreate.save();
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof CustomDataset);
        CustomDataset d = (CustomDataset) one;
        assertEquals(d.getGuid(), dataset.getGuid());
        assertEquals(d.getQualifiedName(), dataset.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof CustomTable);
        table = (CustomTable) one;
        assertNotNull(table.getGuid());
        assertNotNull(table.getQualifiedName());
        assertEquals(table.getName(), TABLE_NAME);
        assertEquals(table.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(table.getCustomDatasetName(), DATASET_NAME);
        assertEquals(table.getCustomDatasetQualifiedName(), dataset.getQualifiedName());
    }

    @Test(
            groups = {"custom.create.fields"},
            dependsOnGroups = {"custom.create.table"})
    void createFields() throws AtlanException {
        CustomField toCreate1 = CustomField.creator(FIELD_NAME1, table).build();
        CustomField toCreate2 = CustomField.creator(FIELD_NAME2, table).build();
        AssetMutationResponse response = Atlan.getDefaultClient().assets.save(List.of(toCreate1, toCreate2), false);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof CustomTable);
        CustomTable t = (CustomTable) one;
        assertEquals(t.getGuid(), table.getGuid());
        assertEquals(t.getQualifiedName(), table.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 2);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof CustomField);
        field1 = (CustomField) one;
        assertNotNull(field1.getGuid());
        assertNotNull(field1.getQualifiedName());
        assertEquals(field1.getName(), FIELD_NAME1);
        assertEquals(field1.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(field1.getCustomDatasetName(), DATASET_NAME);
        assertEquals(field1.getCustomDatasetQualifiedName(), dataset.getQualifiedName());
        assertEquals(field1.getTableName(), TABLE_NAME);
        assertEquals(field1.getTableQualifiedName(), table.getQualifiedName());
        one = response.getCreatedAssets().get(1);
        assertTrue(one instanceof CustomField);
        field2 = (CustomField) one;
        assertNotNull(field2.getGuid());
        assertNotNull(field2.getQualifiedName());
        assertEquals(field2.getName(), FIELD_NAME2);
        assertEquals(field2.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(field2.getCustomDatasetName(), DATASET_NAME);
        assertEquals(field2.getCustomDatasetQualifiedName(), dataset.getQualifiedName());
        assertEquals(field2.getTableName(), TABLE_NAME);
        assertEquals(field2.getTableQualifiedName(), table.getQualifiedName());
    }

    @Test(
            groups = {"custom.update.table"},
            dependsOnGroups = {"custom.create.fields"})
    void updateTable() throws AtlanException {
        CustomTable updated =
                CustomTable.updateCertificate(table.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = CustomTable.updateAnnouncement(
                table.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"custom.read.table"},
            dependsOnGroups = {"custom.create.table", "custom.update.table"})
    void retrieveTable() throws AtlanException {
        CustomTable b = CustomTable.get(table.getGuid());
        assertNotNull(b);
        assertTrue(b.isComplete());
        assertEquals(b.getGuid(), table.getGuid());
        assertEquals(b.getQualifiedName(), table.getQualifiedName());
        assertEquals(b.getName(), TABLE_NAME);
        assertEquals(b.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(b.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertNotNull(b.getCustomFields());
        assertEquals(b.getCustomFields().size(), 2);
        Set<String> types =
                b.getCustomFields().stream().map(ICustomField::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 1);
        assertTrue(types.contains(CustomField.TYPE_NAME));
        Set<String> guids =
                b.getCustomFields().stream().map(ICustomField::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 2);
        assertTrue(guids.contains(field1.getGuid()));
        assertTrue(guids.contains(field2.getGuid()));
    }

    @Test(
            groups = {"custom.update.table.again"},
            dependsOnGroups = {"custom.read.table"})
    void updateTableAgain() throws AtlanException {
        CustomTable updated = CustomTable.removeCertificate(table.getQualifiedName(), TABLE_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = CustomTable.removeAnnouncement(table.getQualifiedName(), TABLE_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"custom.search.assets"},
            dependsOnGroups = {"custom.update.table.again"})
    void searchAssets() throws AtlanException, InterruptedException {
        IndexSearchRequest index = Atlan.getDefaultClient()
                .assets
                .select()
                .where(Asset.SUPER_TYPE_NAMES.eq(ICustom.TYPE_NAME))
                .where(Asset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .pageSize(10)
                .aggregate("type", IReferenceable.TYPE_NAME.bucketBy())
                .sort(Asset.CREATE_TIME.order(SortOrder.Asc))
                .sort(Asset.NAME.order(SortOrder.Asc))
                .includeOnResults(Asset.NAME)
                .includeOnResults(Asset.CONNECTION_QUALIFIED_NAME)
                .toRequest();

        IndexSearchResponse response = retrySearchUntil(index, 4L);

        assertNotNull(response.getAggregations());
        assertEquals(response.getAggregations().size(), 1);
        assertTrue(response.getAggregations().get("type") instanceof AggregationBucketResult);
        assertEquals(
                ((AggregationBucketResult) response.getAggregations().get("type"))
                        .getBuckets()
                        .size(),
                3);

        assertEquals(response.getApproximateCount().longValue(), 4L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 4);

        Asset one = entities.get(0);
        assertTrue(one instanceof CustomDataset);
        assertFalse(one.isComplete());
        CustomDataset b = (CustomDataset) one;
        assertEquals(b.getQualifiedName(), dataset.getQualifiedName());
        assertEquals(b.getName(), dataset.getName());
        assertEquals(b.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof CustomTable);
        assertFalse(one.isComplete());
        CustomTable o = (CustomTable) one;
        assertEquals(o.getQualifiedName(), table.getQualifiedName());
        assertEquals(o.getName(), table.getName());
        assertEquals(o.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(2);
        assertTrue(one instanceof CustomField);
        assertFalse(one.isComplete());
        CustomField f = (CustomField) one;
        assertEquals(f.getQualifiedName(), field1.getQualifiedName());
        assertEquals(f.getName(), field1.getName());
        assertEquals(f.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(3);
        assertTrue(one instanceof CustomField);
        assertFalse(one.isComplete());
        f = (CustomField) one;
        assertEquals(f.getQualifiedName(), field2.getQualifiedName());
        assertEquals(f.getName(), field2.getName());
        assertEquals(f.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"custom.delete.field"},
            dependsOnGroups = {"custom.update.*", "custom.search.*"})
    void deleteField() throws AtlanException {
        AssetMutationResponse response = Asset.delete(field2.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof CustomField);
        CustomField s = (CustomField) one;
        assertEquals(s.getGuid(), field2.getGuid());
        assertEquals(s.getQualifiedName(), field2.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"custom.delete.field.read"},
            dependsOnGroups = {"custom.delete.field"})
    void readDeletedField() throws AtlanException {
        validateDeletedAsset(field2, log);
    }

    @Test(
            groups = {"custom.delete.field.restore"},
            dependsOnGroups = {"custom.delete.field.read"})
    void restoreField() throws AtlanException {
        assertTrue(CustomField.restore(field2.getQualifiedName()));
        CustomField restored = CustomField.get(field2.getQualifiedName());
        assertEquals(restored.getGuid(), field2.getGuid());
        assertEquals(restored.getQualifiedName(), field2.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"custom.purge.field"},
            dependsOnGroups = {"custom.delete.field.restore"})
    void purgeField() throws AtlanException {
        AssetMutationResponse response = Asset.purge(field2.getGuid());
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof CustomField);
        CustomField s = (CustomField) one;
        assertEquals(s.getGuid(), field2.getGuid());
        assertEquals(s.getQualifiedName(), field2.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "PURGE");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"custom.purge.connection"},
            dependsOnGroups = {
                "custom.create.*",
                "custom.read.*",
                "custom.search.*",
                "custom.update.*",
                "custom.purge.field"
            },
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
