/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.search.AggregationBucketResult;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests all aspects of Anaplan assets.
 */
@Slf4j
public class AnaplanAssetTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("Anaplan");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.ANAPLAN;
    public static final String CONNECTION_NAME = PREFIX;

    private static final String WORKSPACE_NAME = PREFIX + "-workspace";
    private static final String APP_NAME = PREFIX + "-app";
    private static final String PAGE_NAME = PREFIX + "-page";
    private static final String MODEL_NAME = PREFIX + "-model";
    private static final String MODULE_NAME = PREFIX + "-module";
    private static final String LIST_NAME = PREFIX + "-list";
    private static final String SYSTEM_DIMENSION_NAME = PREFIX + "-system-dimension";
    private static final String DIMENSION_NAME = PREFIX + "-dimension";
    private static final String LINE_ITEM_NAME = PREFIX + "-lineItem";
    private static final String VIEW_NAME = PREFIX + "-view";

    private static Connection connection = null;
    private static AnaplanWorkspace workspace = null;
    private static AnaplanApp app = null;
    private static AnaplanPage page = null;
    private static AnaplanModel model = null;
    private static AnaplanModule module = null;
    private static AnaplanList list = null;
    private static AnaplanSystemDimension systemDimension = null;
    private static AnaplanDimension dimension = null;
    private static AnaplanLineItem lineItem = null;
    private static AnaplanView view = null;

    @Test(groups = {"anaplan.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(client, CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"anaplan.create.workspace"},
            dependsOnGroups = {"anaplan.create.connection"})
    void createWorkspace() throws AtlanException {
        AnaplanWorkspace toCreate = AnaplanWorkspace.creator(WORKSPACE_NAME, connection.getQualifiedName())
                .build();
        AssetMutationResponse response = toCreate.save(client);
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof AnaplanWorkspace);
        workspace = (AnaplanWorkspace) one;
        assertNotNull(workspace.getGuid());
        assertNotNull(workspace.getQualifiedName());
        assertEquals(workspace.getName(), WORKSPACE_NAME);
        assertEquals(workspace.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(workspace.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"anaplan.create.systemDimension"},
            dependsOnGroups = {"anaplan.create.workspace"})
    void createSystemDimension() throws AtlanException {
        AnaplanSystemDimension toCreate = AnaplanSystemDimension.creator(
                        SYSTEM_DIMENSION_NAME, connection.getQualifiedName())
                .build();
        AssetMutationResponse response = toCreate.save(client);
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof AnaplanSystemDimension);
        systemDimension = (AnaplanSystemDimension) one;
        assertNotNull(systemDimension.getGuid());
        assertNotNull(systemDimension.getQualifiedName());
        assertEquals(systemDimension.getName(), SYSTEM_DIMENSION_NAME);
        assertEquals(systemDimension.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(systemDimension.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"anaplan.create.app"},
            dependsOnGroups = {"anaplan.create.systemDimension"})
    void createApp() throws AtlanException {
        AnaplanApp toCreate =
                AnaplanApp.creator(APP_NAME, connection.getQualifiedName()).build();
        AssetMutationResponse response = toCreate.save(client);
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof AnaplanApp);
        app = (AnaplanApp) one;
        assertNotNull(app.getGuid());
        assertNotNull(app.getQualifiedName());
        assertEquals(app.getName(), APP_NAME);
        assertEquals(app.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(app.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"anaplan.create.page"},
            dependsOnGroups = {"anaplan.create.app"})
    void createPage() throws AtlanException {
        AnaplanPage toCreate = AnaplanPage.creator(PAGE_NAME, app).build();
        AssetMutationResponse response = toCreate.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof AnaplanApp);
        AnaplanApp c = (AnaplanApp) one;
        assertEquals(c.getGuid(), app.getGuid());
        assertEquals(c.getQualifiedName(), app.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof AnaplanPage);
        page = (AnaplanPage) one;
        assertNotNull(page.getGuid());
        assertNotNull(page.getQualifiedName());
        assertEquals(page.getName(), PAGE_NAME);
        assertEquals(page.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(page.getAnaplanAppQualifiedName(), app.getQualifiedName());
        assertEquals(page.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"anaplan.create.model"},
            dependsOnGroups = {"anaplan.create.page"})
    void createModel() throws AtlanException {
        AnaplanModel toCreate = AnaplanModel.creator(MODEL_NAME, workspace).build();
        AssetMutationResponse response = toCreate.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof AnaplanWorkspace);
        AnaplanWorkspace c = (AnaplanWorkspace) one;
        assertEquals(c.getGuid(), workspace.getGuid());
        assertEquals(c.getQualifiedName(), workspace.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof AnaplanModel);
        model = (AnaplanModel) one;
        assertNotNull(model.getGuid());
        assertNotNull(model.getQualifiedName());
        assertEquals(model.getName(), MODEL_NAME);
        assertEquals(model.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(model.getAnaplanWorkspaceQualifiedName(), workspace.getQualifiedName());
        assertEquals(model.getAnaplanWorkspaceName(), workspace.getName());
        assertEquals(model.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"anaplan.create.module"},
            dependsOnGroups = {"anaplan.create.model"})
    void createModule() throws AtlanException {
        AnaplanModule toCreate = AnaplanModule.creator(MODULE_NAME, model).build();
        AssetMutationResponse response = toCreate.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof AnaplanModel);
        AnaplanModel c = (AnaplanModel) one;
        assertEquals(c.getGuid(), model.getGuid());
        assertEquals(c.getQualifiedName(), model.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof AnaplanModule);
        module = (AnaplanModule) one;
        assertNotNull(module.getGuid());
        assertNotNull(module.getQualifiedName());
        assertEquals(module.getName(), MODULE_NAME);
        assertEquals(module.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(module.getAnaplanWorkspaceQualifiedName(), workspace.getQualifiedName());
        assertEquals(module.getAnaplanWorkspaceName(), workspace.getName());
        assertEquals(module.getAnaplanModelQualifiedName(), model.getQualifiedName());
        assertEquals(module.getAnaplanModelName(), model.getName());
        assertEquals(module.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"anaplan.create.list"},
            dependsOnGroups = {"anaplan.create.module"})
    void createList() throws AtlanException {
        AnaplanList toCreate = AnaplanList.creator(LIST_NAME, model).build();
        AssetMutationResponse response = toCreate.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof AnaplanModel);
        AnaplanModel c = (AnaplanModel) one;
        assertEquals(c.getGuid(), model.getGuid());
        assertEquals(c.getQualifiedName(), model.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof AnaplanList);
        list = (AnaplanList) one;
        assertNotNull(list.getGuid());
        assertNotNull(list.getQualifiedName());
        assertEquals(list.getName(), LIST_NAME);
        assertEquals(list.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(list.getAnaplanWorkspaceQualifiedName(), workspace.getQualifiedName());
        assertEquals(list.getAnaplanWorkspaceName(), workspace.getName());
        assertEquals(list.getAnaplanModelQualifiedName(), model.getQualifiedName());
        assertEquals(list.getAnaplanModelName(), model.getName());
        assertEquals(list.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"anaplan.create.dimension"},
            dependsOnGroups = {"anaplan.create.list"})
    void createDimension() throws AtlanException {
        AnaplanDimension toCreate =
                AnaplanDimension.creator(DIMENSION_NAME, model).build();
        AssetMutationResponse response = toCreate.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof AnaplanModel);
        AnaplanModel c = (AnaplanModel) one;
        assertEquals(c.getGuid(), model.getGuid());
        assertEquals(c.getQualifiedName(), model.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof AnaplanDimension);
        dimension = (AnaplanDimension) one;
        assertNotNull(dimension.getGuid());
        assertNotNull(dimension.getQualifiedName());
        assertEquals(dimension.getName(), DIMENSION_NAME);
        assertEquals(dimension.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(dimension.getAnaplanWorkspaceQualifiedName(), workspace.getQualifiedName());
        assertEquals(dimension.getAnaplanWorkspaceName(), workspace.getName());
        assertEquals(dimension.getAnaplanModelQualifiedName(), model.getQualifiedName());
        assertEquals(dimension.getAnaplanModelName(), model.getName());
        assertEquals(dimension.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"anaplan.create.lineItem"},
            dependsOnGroups = {"anaplan.create.dimension"})
    void createLineItem() throws AtlanException {
        AnaplanLineItem toCreate =
                AnaplanLineItem.creator(LINE_ITEM_NAME, module).build();
        AssetMutationResponse response = toCreate.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof AnaplanModule);
        AnaplanModule c = (AnaplanModule) one;
        assertEquals(c.getGuid(), module.getGuid());
        assertEquals(c.getQualifiedName(), module.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof AnaplanLineItem);
        lineItem = (AnaplanLineItem) one;
        assertNotNull(lineItem.getGuid());
        assertNotNull(lineItem.getQualifiedName());
        assertEquals(lineItem.getName(), LINE_ITEM_NAME);
        assertEquals(lineItem.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(lineItem.getAnaplanWorkspaceQualifiedName(), workspace.getQualifiedName());
        assertEquals(lineItem.getAnaplanWorkspaceName(), workspace.getName());
        assertEquals(lineItem.getAnaplanModelQualifiedName(), model.getQualifiedName());
        assertEquals(lineItem.getAnaplanModelName(), model.getName());
        assertEquals(lineItem.getAnaplanModuleQualifiedName(), module.getQualifiedName());
        assertEquals(lineItem.getAnaplanModuleName(), module.getName());
        assertEquals(lineItem.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"anaplan.create.view"},
            dependsOnGroups = {"anaplan.create.lineItem"})
    void createView() throws AtlanException {
        AnaplanView toCreate = AnaplanView.creator(VIEW_NAME, module).build();
        AssetMutationResponse response = toCreate.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof AnaplanModule);
        AnaplanModule c = (AnaplanModule) one;
        assertEquals(c.getGuid(), module.getGuid());
        assertEquals(c.getQualifiedName(), module.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof AnaplanView);
        view = (AnaplanView) one;
        assertNotNull(view.getGuid());
        assertNotNull(view.getQualifiedName());
        assertEquals(view.getName(), VIEW_NAME);
        assertEquals(view.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(view.getAnaplanWorkspaceQualifiedName(), workspace.getQualifiedName());
        assertEquals(view.getAnaplanWorkspaceName(), workspace.getName());
        assertEquals(view.getAnaplanModelQualifiedName(), model.getQualifiedName());
        assertEquals(view.getAnaplanModelName(), model.getName());
        assertEquals(view.getAnaplanModuleQualifiedName(), module.getQualifiedName());
        assertEquals(view.getAnaplanModuleName(), module.getName());
        assertEquals(view.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"anaplan.update.view"},
            dependsOnGroups = {"anaplan.create.view"})
    void updateView() throws AtlanException {
        AnaplanView updated =
                AnaplanView.updateCertificate(client, view.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = AnaplanView.updateAnnouncement(
                client, view.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"anaplan.read.view"},
            dependsOnGroups = {"anaplan.create.*", "anaplan.update.view"})
    void retrieveView() throws AtlanException {
        AnaplanView c = AnaplanView.get(client, view.getGuid(), true);
        assertNotNull(c);
        assertTrue(c.isComplete());
        assertEquals(c.getGuid(), view.getGuid());
        assertEquals(c.getQualifiedName(), view.getQualifiedName());
        assertEquals(c.getName(), VIEW_NAME);
        assertEquals(c.getCertificateStatus(), CERTIFICATE_STATUS);
        assertNotNull(c.getAnaplanModule());
        assertEquals(c.getAnaplanModule().getTypeName(), AnaplanModule.TYPE_NAME);
        assertEquals(c.getAnaplanModule().getGuid(), module.getGuid());
        assertNotNull(c.getAnaplanWorkspaceQualifiedName());
        assertEquals(c.getAnaplanWorkspaceQualifiedName(), workspace.getQualifiedName());
        assertNotNull(c.getAnaplanWorkspaceName());
        assertEquals(c.getAnaplanWorkspaceName(), workspace.getName());
        assertNotNull(c.getAnaplanModelQualifiedName());
        assertEquals(c.getAnaplanModelQualifiedName(), model.getQualifiedName());
        assertNotNull(c.getAnaplanModelName());
        assertEquals(c.getAnaplanModelName(), model.getName());
    }

    @Test(
            groups = {"anaplan.update.view.again"},
            dependsOnGroups = {"anaplan.read.view"})
    void updateViewAgain() throws AtlanException {
        AnaplanView updated = AnaplanView.removeCertificate(client, view.getQualifiedName(), VIEW_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = AnaplanView.removeAnnouncement(client, view.getQualifiedName(), VIEW_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"anaplan.search.assets"},
            dependsOnGroups = {"anaplan.update.view.again"})
    void searchAssets() throws AtlanException, InterruptedException {
        IndexSearchRequest index = client.assets
                .select()
                .where(Asset.SUPER_TYPE_NAMES.eq(IAnaplan.TYPE_NAME))
                .where(Asset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .pageSize(10)
                .aggregate("type", IReferenceable.TYPE_NAME.bucketBy())
                .sort(Asset.CREATE_TIME.order(SortOrder.Asc))
                .includeOnResults(Asset.NAME)
                .includeOnResults(Asset.CONNECTION_QUALIFIED_NAME)
                .toRequest();

        IndexSearchResponse response = retrySearchUntil(index, 10L);

        assertNotNull(response.getAggregations());
        assertEquals(response.getAggregations().size(), 1);
        assertTrue(response.getAggregations().get("type") instanceof AggregationBucketResult);
        assertEquals(
                ((AggregationBucketResult) response.getAggregations().get("type"))
                        .getBuckets()
                        .size(),
                10);

        assertEquals(response.getApproximateCount().longValue(), 10L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 10);

        Asset one = entities.get(0);
        assertTrue(one instanceof AnaplanWorkspace);
        assertFalse(one.isComplete());
        AnaplanWorkspace d = (AnaplanWorkspace) one;
        assertEquals(d.getQualifiedName(), workspace.getQualifiedName());
        assertEquals(d.getName(), workspace.getName());
        assertEquals(d.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof AnaplanSystemDimension);
        assertFalse(one.isComplete());
        AnaplanSystemDimension sd = (AnaplanSystemDimension) one;
        assertEquals(sd.getQualifiedName(), systemDimension.getQualifiedName());
        assertEquals(sd.getName(), systemDimension.getName());
        assertEquals(sd.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(2);
        assertTrue(one instanceof AnaplanApp);
        assertFalse(one.isComplete());
        AnaplanApp c = (AnaplanApp) one;
        assertEquals(c.getQualifiedName(), app.getQualifiedName());
        assertEquals(c.getName(), app.getName());
        assertEquals(c.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(3);
        assertTrue(one instanceof AnaplanPage);
        assertFalse(one.isComplete());
        AnaplanPage ds = (AnaplanPage) one;
        assertEquals(ds.getQualifiedName(), page.getQualifiedName());
        assertEquals(ds.getName(), page.getName());
        assertEquals(ds.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(4);
        assertTrue(one instanceof AnaplanModel);
        assertFalse(one.isComplete());
        AnaplanModel m = (AnaplanModel) one;
        assertEquals(m.getQualifiedName(), model.getQualifiedName());
        assertEquals(m.getName(), model.getName());
        assertEquals(m.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(5);
        assertTrue(one instanceof AnaplanModule);
        assertFalse(one.isComplete());
        AnaplanModule md = (AnaplanModule) one;
        assertEquals(md.getQualifiedName(), module.getQualifiedName());
        assertEquals(md.getName(), module.getName());
        assertEquals(md.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(6);
        assertTrue(one instanceof AnaplanList);
        assertFalse(one.isComplete());
        AnaplanList l = (AnaplanList) one;
        assertEquals(l.getQualifiedName(), list.getQualifiedName());
        assertEquals(l.getName(), list.getName());
        assertEquals(l.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(7);
        assertTrue(one instanceof AnaplanDimension);
        assertFalse(one.isComplete());
        AnaplanDimension di = (AnaplanDimension) one;
        assertEquals(di.getQualifiedName(), dimension.getQualifiedName());
        assertEquals(di.getName(), dimension.getName());
        assertEquals(di.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(8);
        assertTrue(one instanceof AnaplanLineItem);
        assertFalse(one.isComplete());
        AnaplanLineItem li = (AnaplanLineItem) one;
        assertEquals(li.getQualifiedName(), lineItem.getQualifiedName());
        assertEquals(li.getName(), lineItem.getName());
        assertEquals(li.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(9);
        assertTrue(one instanceof AnaplanView);
        assertFalse(one.isComplete());
        AnaplanView vi = (AnaplanView) one;
        assertEquals(vi.getQualifiedName(), view.getQualifiedName());
        assertEquals(vi.getName(), view.getName());
        assertEquals(vi.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"anaplan.delete.list"},
            dependsOnGroups = {"anaplan.update.*", "anaplan.search.*"})
    void deleteList() throws AtlanException {
        AssetMutationResponse response = Asset.delete(client, list.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof AnaplanList);
        AnaplanList s = (AnaplanList) one;
        assertEquals(s.getGuid(), list.getGuid());
        assertEquals(s.getQualifiedName(), list.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"anaplan.delete.list.read"},
            dependsOnGroups = {"anaplan.delete.list"})
    void readDeletedList() throws AtlanException {
        validateDeletedAsset(list, log);
    }

    @Test(
            groups = {"anaplan.delete.list.restore"},
            dependsOnGroups = {"anaplan.delete.list.read"})
    void restoreList() throws AtlanException {
        assertTrue(AnaplanList.restore(client, list.getQualifiedName()));
        AnaplanList restored = AnaplanList.get(client, list.getQualifiedName());
        assertFalse(restored.isComplete());
        assertEquals(restored.getGuid(), list.getGuid());
        assertEquals(restored.getQualifiedName(), list.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"anaplan.purge.list"},
            dependsOnGroups = {"anaplan.delete.list.restore"})
    void purgeList() throws AtlanException {
        AssetMutationResponse response = Asset.purge(client, list.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof AnaplanList);
        AnaplanList s = (AnaplanList) one;
        assertEquals(s.getGuid(), list.getGuid());
        assertEquals(s.getQualifiedName(), list.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "PURGE");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"anaplan.purge.connection"},
            dependsOnGroups = {
                "anaplan.create.*",
                "anaplan.read.*",
                "anaplan.search.*",
                "anaplan.update.*",
                "anaplan.purge.list"
            },
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(client, connection.getQualifiedName(), log);
    }
}
