/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.Cube;
import com.atlan.model.assets.CubeDimension;
import com.atlan.model.assets.CubeField;
import com.atlan.model.assets.CubeHierarchy;
import com.atlan.model.assets.DataStudioAsset;
import com.atlan.model.assets.IReferenceable;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.search.AggregationBucketResult;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.net.HttpClient;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class CubeTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("Cube");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.ESSBASE;
    public static final String CONNECTION_NAME = PREFIX;

    private static final String CUBE_NAME = PREFIX + "-cube";
    private static final String DIMENSION_NAME = PREFIX + "-dim";
    private static final String HIERARCHY_NAME = PREFIX + "-hierarchy";
    private static final String LEVEL1_NAME = PREFIX + "-level1";
    private static final String LEVEL2_NAME = PREFIX + "-level2";
    private static final String LEVEL3_NAME = PREFIX + "-level3";

    private static Connection connection = null;
    private static Cube cube = null;
    private static CubeDimension dimension = null;
    private static CubeHierarchy hierarchy = null;
    private static CubeField level1 = null;
    private static CubeField level2 = null;
    private static CubeField level3 = null;

    @Test(groups = {"mdd.create.connection"})
    void createConnection() throws AtlanException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"mdd.create.cube"},
            dependsOnGroups = {"mdd.create.connection"})
    void createCube() throws AtlanException {
        Cube toCreate = Cube.creator(CUBE_NAME, connection.getQualifiedName()).build();
        AssetMutationResponse response = toCreate.save();
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof Cube);
        cube = (Cube) one;
        assertNotNull(cube.getGuid());
        assertNotNull(cube.getQualifiedName());
        assertEquals(cube.getName(), CUBE_NAME);
        assertEquals(cube.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(cube.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"mdd.create.dimension"},
            dependsOnGroups = {"mdd.create.cube"})
    void createDimension() throws AtlanException {
        dimension = CubeDimension.creator(DIMENSION_NAME, cube).build();
        AssetMutationResponse response = dimension.save();
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset parent = response.getUpdatedAssets().get(0);
        assertTrue(parent instanceof Cube);
        assertEquals(parent.getGuid(), cube.getGuid());
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof CubeDimension);
        dimension = (CubeDimension) one;
        assertNotNull(dimension.getGuid());
        assertNotNull(dimension.getQualifiedName());
        assertEquals(dimension.getName(), DIMENSION_NAME);
        assertEquals(dimension.getCubeName(), CUBE_NAME);
        assertEquals(dimension.getCubeQualifiedName(), cube.getQualifiedName());
    }

    @Test(
            groups = {"mdd.create.hierarchy"},
            dependsOnGroups = {"mdd.create.dimension"})
    void createHierarchy() throws AtlanException {
        hierarchy = CubeHierarchy.creator(HIERARCHY_NAME, dimension).build();
        AssetMutationResponse response = hierarchy.save();
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset parent = response.getUpdatedAssets().get(0);
        assertTrue(parent instanceof CubeDimension);
        assertEquals(parent.getGuid(), dimension.getGuid());
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof CubeHierarchy);
        hierarchy = (CubeHierarchy) one;
        assertNotNull(hierarchy.getGuid());
        assertNotNull(hierarchy.getQualifiedName());
        assertEquals(hierarchy.getName(), HIERARCHY_NAME);
        assertEquals(hierarchy.getCubeName(), CUBE_NAME);
        assertEquals(hierarchy.getCubeQualifiedName(), cube.getQualifiedName());
        assertEquals(hierarchy.getCubeDimensionName(), DIMENSION_NAME);
        assertEquals(hierarchy.getCubeDimensionQualifiedName(), dimension.getQualifiedName());
    }

    @Test(
            groups = {"mdd.create.level1"},
            dependsOnGroups = {"mdd.create.hierarchy"})
    void createLevel1() throws AtlanException {
        level1 = CubeField.creator(LEVEL1_NAME, hierarchy).build();
        AssetMutationResponse response = level1.save();
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset parent = response.getUpdatedAssets().get(0);
        assertTrue(parent instanceof CubeHierarchy);
        assertEquals(parent.getGuid(), hierarchy.getGuid());
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof CubeField);
        level1 = (CubeField) one;
        assertNotNull(level1.getGuid());
        assertNotNull(level1.getQualifiedName());
        assertEquals(level1.getName(), LEVEL1_NAME);
        assertEquals(level1.getCubeName(), CUBE_NAME);
        assertEquals(level1.getCubeQualifiedName(), cube.getQualifiedName());
        assertEquals(level1.getCubeDimensionName(), DIMENSION_NAME);
        assertEquals(level1.getCubeDimensionQualifiedName(), dimension.getQualifiedName());
        assertEquals(level1.getCubeHierarchyName(), HIERARCHY_NAME);
        assertEquals(level1.getCubeHierarchyQualifiedName(), hierarchy.getQualifiedName());
    }

    @Test(
            groups = {"mdd.create.level2"},
            dependsOnGroups = {"mdd.create.level1"})
    void createLevel2() throws AtlanException {
        level2 = CubeField.creator(LEVEL2_NAME, level1).build();
        AssetMutationResponse response = level2.save();
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 2);
        Set<String> parentTypes =
                response.getUpdatedAssets().stream().map(Asset::getTypeName).collect(Collectors.toSet());
        assertEquals(parentTypes.size(), 2);
        assertTrue(parentTypes.contains(CubeHierarchy.TYPE_NAME));
        assertTrue(parentTypes.contains(CubeField.TYPE_NAME));
        Set<String> parentGuids =
                response.getUpdatedAssets().stream().map(Asset::getGuid).collect(Collectors.toSet());
        assertEquals(parentGuids.size(), 2);
        assertTrue(parentGuids.contains(hierarchy.getGuid()));
        assertTrue(parentGuids.contains(level1.getGuid()));
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof CubeField);
        level2 = (CubeField) one;
        assertNotNull(level2.getGuid());
        assertNotNull(level2.getQualifiedName());
        assertEquals(level2.getName(), LEVEL2_NAME);
        assertEquals(level2.getCubeName(), CUBE_NAME);
        assertEquals(level2.getCubeQualifiedName(), cube.getQualifiedName());
        assertEquals(level2.getCubeDimensionName(), DIMENSION_NAME);
        assertEquals(level2.getCubeDimensionQualifiedName(), dimension.getQualifiedName());
        assertEquals(level2.getCubeHierarchyName(), HIERARCHY_NAME);
        assertEquals(level2.getCubeHierarchyQualifiedName(), hierarchy.getQualifiedName());
    }

    @Test(
            groups = {"mdd.create.level3"},
            dependsOnGroups = {"mdd.create.level2"})
    void createLevel3() throws AtlanException {
        level3 = CubeField.creator(LEVEL3_NAME, level2).build();
        AssetMutationResponse response = level3.save();
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 2);
        Set<String> parentTypes =
                response.getUpdatedAssets().stream().map(Asset::getTypeName).collect(Collectors.toSet());
        assertEquals(parentTypes.size(), 2);
        assertTrue(parentTypes.contains(CubeHierarchy.TYPE_NAME));
        assertTrue(parentTypes.contains(CubeField.TYPE_NAME));
        Set<String> parentGuids =
                response.getUpdatedAssets().stream().map(Asset::getGuid).collect(Collectors.toSet());
        assertEquals(parentGuids.size(), 2);
        assertTrue(parentGuids.contains(hierarchy.getGuid()));
        assertTrue(parentGuids.contains(level2.getGuid()));
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof CubeField);
        level3 = (CubeField) one;
        assertNotNull(level3.getGuid());
        assertNotNull(level3.getQualifiedName());
        assertEquals(level3.getName(), LEVEL3_NAME);
        assertEquals(level3.getCubeName(), CUBE_NAME);
        assertEquals(level3.getCubeQualifiedName(), cube.getQualifiedName());
        assertEquals(level3.getCubeDimensionName(), DIMENSION_NAME);
        assertEquals(level3.getCubeDimensionQualifiedName(), dimension.getQualifiedName());
        assertEquals(level3.getCubeHierarchyName(), HIERARCHY_NAME);
        assertEquals(level3.getCubeHierarchyQualifiedName(), hierarchy.getQualifiedName());
    }

    @Test(
            groups = {"mdd.read.dimension"},
            dependsOnGroups = {"mdd.create.*"})
    void readDimension() throws AtlanException {
        CubeDimension read = CubeDimension.get(dimension.getQualifiedName());
        assertNotNull(read);
        assertEquals(read.getGuid(), dimension.getGuid());
        assertEquals(read.getQualifiedName(), dimension.getQualifiedName());
        assertEquals(read.getName(), dimension.getName());
        assertEquals(read.getCubeQualifiedName(), dimension.getCubeQualifiedName());
        assertNotNull(read.getCube());
        assertEquals(read.getCube().getGuid(), cube.getGuid());
        assertNotNull(read.getCubeHierarchies());
        assertEquals(read.getCubeHierarchies().size(), 1);
    }

    @Test(
            groups = {"mdd.read.hierarchy"},
            dependsOnGroups = {"mdd.create.*"})
    void readHierarchy() throws AtlanException {
        CubeHierarchy read = CubeHierarchy.get(hierarchy.getQualifiedName());
        assertNotNull(read);
        assertEquals(read.getGuid(), hierarchy.getGuid());
        assertEquals(read.getQualifiedName(), hierarchy.getQualifiedName());
        assertEquals(read.getName(), hierarchy.getName());
        assertEquals(read.getCubeDimensionQualifiedName(), hierarchy.getCubeDimensionQualifiedName());
        assertNotNull(read.getCubeDimension());
        assertEquals(read.getCubeDimension().getGuid(), dimension.getGuid());
        assertNotNull(read.getCubeFields());
        assertEquals(read.getCubeFields().size(), 3);
    }

    @Test(
            groups = {"mdd.read.level1"},
            dependsOnGroups = {"mdd.create.*"})
    void readLevel1() throws AtlanException {
        CubeField read = CubeField.get(level1.getQualifiedName());
        assertNotNull(read);
        assertEquals(read.getGuid(), level1.getGuid());
        assertEquals(read.getQualifiedName(), level1.getQualifiedName());
        assertEquals(read.getName(), level1.getName());
        assertEquals(read.getCubeHierarchyQualifiedName(), level1.getCubeHierarchyQualifiedName());
        assertNotNull(read.getCubeHierarchy());
        assertEquals(read.getCubeHierarchy().getGuid(), hierarchy.getGuid());
        assertNull(read.getCubeParentField());
        assertNotNull(read.getCubeNestedFields());
        assertEquals(read.getCubeNestedFields().size(), 1);
    }

    @Test(
            groups = {"mdd.read.level2"},
            dependsOnGroups = {"mdd.create.*"})
    void readLevel2() throws AtlanException {
        CubeField read = CubeField.get(level2.getQualifiedName());
        assertNotNull(read);
        assertEquals(read.getGuid(), level2.getGuid());
        assertEquals(read.getQualifiedName(), level2.getQualifiedName());
        assertEquals(read.getName(), level2.getName());
        assertEquals(read.getCubeHierarchyQualifiedName(), level2.getCubeHierarchyQualifiedName());
        assertNotNull(read.getCubeHierarchy());
        assertEquals(read.getCubeHierarchy().getGuid(), hierarchy.getGuid());
        assertNotNull(read.getCubeParentField());
        assertEquals(read.getCubeParentField().getGuid(), level1.getGuid());
        assertNotNull(read.getCubeNestedFields());
        assertEquals(read.getCubeNestedFields().size(), 1);
    }

    @Test(
            groups = {"mdd.read.level3"},
            dependsOnGroups = {"mdd.create.*"})
    void readLevel3() throws AtlanException {
        CubeField read = CubeField.get(level3.getQualifiedName());
        assertNotNull(read);
        assertEquals(read.getGuid(), level3.getGuid());
        assertEquals(read.getQualifiedName(), level3.getQualifiedName());
        assertEquals(read.getName(), level3.getName());
        assertEquals(read.getCubeHierarchyQualifiedName(), level3.getCubeHierarchyQualifiedName());
        assertNotNull(read.getCubeHierarchy());
        assertEquals(read.getCubeHierarchy().getGuid(), hierarchy.getGuid());
        assertNotNull(read.getCubeParentField());
        assertEquals(read.getCubeParentField().getGuid(), level2.getGuid());
        assertTrue(
                read.getCubeNestedFields() == null || read.getCubeNestedFields().isEmpty());
    }

    @Test(
            groups = {"mdd.update.cube"},
            dependsOnGroups = {"mdd.create.*"})
    void updateCube() throws AtlanException {
        Cube updated = Cube.updateCertificate(cube.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = Cube.updateAnnouncement(
                cube.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"mdd.read.cube"},
            dependsOnGroups = {"mdd.update.cube"})
    void readCube() throws AtlanException {
        Cube read = Cube.get(cube.getQualifiedName());
        assertNotNull(read);
        assertEquals(read.getGuid(), cube.getGuid());
        assertEquals(read.getQualifiedName(), cube.getQualifiedName());
        assertEquals(read.getName(), cube.getName());
        assertEquals(read.getConnectorType(), cube.getConnectorType());
        assertEquals(read.getConnectionQualifiedName(), cube.getConnectionQualifiedName());
        assertEquals(read.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(read.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertEquals(read.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(read.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(read.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"mdd.update.cube.again"},
            dependsOnGroups = {"mdd.read.cube"})
    void updateCubeAgain() throws AtlanException {
        Cube updated = Cube.removeCertificate(cube.getQualifiedName(), CUBE_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = Cube.removeAnnouncement(cube.getQualifiedName(), CUBE_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"mdd.search.assets"},
            dependsOnGroups = {"mdd.update.cube.again"})
    void searchAssets() throws AtlanException, InterruptedException {
        IndexSearchRequest index = Atlan.getDefaultClient()
                .assets
                .select()
                .where(Asset.SUPER_TYPE_NAMES.eq("MultiDimensionalDataset"))
                .where(Asset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .aggregate("type", IReferenceable.TYPE_NAME.bucketBy())
                .sort(DataStudioAsset.CREATE_TIME.order(SortOrder.Asc))
                .includeOnResults(Asset.NAME)
                .includeOnResults(Asset.CONNECTION_QUALIFIED_NAME)
                .toRequest();

        IndexSearchResponse response = index.search();
        assertNotNull(response);

        int count = 0;
        while (response.getApproximateCount() < 6L && count < Atlan.getMaxNetworkRetries()) {
            Thread.sleep(HttpClient.waitTime(count).toMillis());
            response = index.search();
            count++;
        }

        assertNotNull(response.getAggregations());
        assertEquals(response.getAggregations().size(), 1);
        assertTrue(response.getAggregations().get("type") instanceof AggregationBucketResult);
        assertEquals(
                ((AggregationBucketResult) response.getAggregations().get("type"))
                        .getBuckets()
                        .size(),
                4);

        assertEquals(response.getApproximateCount().longValue(), 6L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 6);

        Asset one = entities.get(0);
        assertTrue(one instanceof Cube);
        assertFalse(one.isComplete());
        Cube cube1 = (Cube) one;
        assertEquals(cube1.getQualifiedName(), cube.getQualifiedName());
        assertEquals(cube1.getName(), cube.getName());
        assertEquals(cube1.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof CubeDimension);
        assertFalse(one.isComplete());
        CubeDimension dim1 = (CubeDimension) one;
        assertEquals(dim1.getQualifiedName(), dimension.getQualifiedName());
        assertEquals(dim1.getName(), dimension.getName());
        assertEquals(dim1.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(2);
        assertTrue(one instanceof CubeHierarchy);
        assertFalse(one.isComplete());
        CubeHierarchy hier1 = (CubeHierarchy) one;
        assertEquals(hier1.getQualifiedName(), hierarchy.getQualifiedName());
        assertEquals(hier1.getName(), hierarchy.getName());
        assertEquals(hier1.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(3);
        assertTrue(one instanceof CubeField);
        assertFalse(one.isComplete());
        CubeField field1 = (CubeField) one;
        assertEquals(field1.getQualifiedName(), level1.getQualifiedName());
        assertEquals(field1.getName(), level1.getName());
        assertEquals(field1.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(4);
        assertTrue(one instanceof CubeField);
        assertFalse(one.isComplete());
        CubeField field2 = (CubeField) one;
        assertEquals(field2.getQualifiedName(), level2.getQualifiedName());
        assertEquals(field2.getName(), level2.getName());
        assertEquals(field2.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(5);
        assertTrue(one instanceof CubeField);
        assertFalse(one.isComplete());
        CubeField field3 = (CubeField) one;
        assertEquals(field3.getQualifiedName(), level3.getQualifiedName());
        assertEquals(field3.getName(), level3.getName());
        assertEquals(field3.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"mdd.purge.connection"},
            dependsOnGroups = {"mdd.create.*", "mdd.read.*", "mdd.search.*", "mdd.update.*"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
