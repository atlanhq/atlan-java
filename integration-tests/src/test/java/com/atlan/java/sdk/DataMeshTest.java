/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Column;
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.DataContract;
import com.atlan.model.assets.DataDomain;
import com.atlan.model.assets.DataProduct;
import com.atlan.model.assets.Database;
import com.atlan.model.assets.Schema;
import com.atlan.model.assets.Table;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.search.FluentSearch;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class DataMeshTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("Mesh");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.PRESTO;
    public static final String CONNECTION_NAME = PREFIX;

    private static final String DB_NAME = PREFIX + "-db";
    private static final String SCH_NAME = PREFIX + "-sch";
    private static final String TBL_NAME = PREFIX + "-table";
    private static final String COL_NAME = PREFIX + "-col1";
    private static final String DOMAIN_NAME = PREFIX + "-domain";
    private static final String SUB_DOMAIN_NAME = PREFIX + "-subdom";
    private static final String PROD_NAME = PREFIX + "-prod";

    private static Connection connection = null;
    private static Database database = null;
    private static Schema schema = null;
    private static Table table = null;
    private static Column col1 = null;

    private static DataDomain domain = null;
    private static DataDomain subDomain = null;
    private static DataProduct product = null;
    private static DataContract contract = null;

    @Test(groups = {"mesh.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"mesh.create.db"},
            dependsOnGroups = {"mesh.create.connection"})
    void createDatabase() throws AtlanException {
        Database toCreate =
                Database.creator(DB_NAME, connection.getQualifiedName()).build();
        AssetMutationResponse response = toCreate.save();
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof Database);
        database = (Database) one;
        assertNotNull(database.getGuid());
        assertNotNull(database.getQualifiedName());
        assertEquals(database.getName(), DB_NAME);
        assertEquals(database.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(database.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"mesh.create.schema"},
            dependsOnGroups = {"mesh.create.db"})
    void createSchema() throws AtlanException {
        Schema toCreate = Schema.creator(SCH_NAME, database).build();
        AssetMutationResponse response = toCreate.save();
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset parent = response.getUpdatedAssets().get(0);
        assertTrue(parent instanceof Database);
        assertEquals(parent.getGuid(), database.getGuid());
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof Schema);
        schema = (Schema) one;
        assertNotNull(schema.getGuid());
        assertNotNull(schema.getQualifiedName());
        assertEquals(schema.getName(), SCH_NAME);
        assertEquals(schema.getDatabaseName(), DB_NAME);
        assertEquals(schema.getDatabaseQualifiedName(), database.getQualifiedName());
    }

    @Test(
            groups = {"mesh.create.table"},
            dependsOnGroups = {"mesh.create.schema"})
    void createTable() throws AtlanException {
        Table toCreate = Table.creator(TBL_NAME, schema).build();
        AssetMutationResponse response = toCreate.save();
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset parent = response.getUpdatedAssets().get(0);
        assertTrue(parent instanceof Schema);
        assertEquals(parent.getGuid(), schema.getGuid());
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof Table);
        table = (Table) one;
        assertNotNull(table.getGuid());
        assertNotNull(table.getQualifiedName());
        assertEquals(table.getName(), TBL_NAME);
        assertEquals(table.getDatabaseName(), DB_NAME);
        assertEquals(table.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(table.getSchemaName(), SCH_NAME);
        assertEquals(table.getSchemaQualifiedName(), schema.getQualifiedName());
    }

    @Test(
            groups = {"mesh.create.col1"},
            dependsOnGroups = {"mesh.create.table"})
    void createCol1() throws AtlanException {
        Column toCreate = Column.creator(COL_NAME, table, 1).build();
        AssetMutationResponse response = toCreate.save();
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset parent = response.getUpdatedAssets().get(0);
        assertTrue(parent instanceof Table);
        assertEquals(parent.getGuid(), table.getGuid());
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof Column);
        col1 = (Column) one;
        assertNotNull(col1.getGuid());
        assertNotNull(col1.getQualifiedName());
        assertEquals(col1.getName(), COL_NAME);
        assertEquals(col1.getDatabaseName(), DB_NAME);
        assertEquals(col1.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(col1.getSchemaName(), SCH_NAME);
        assertEquals(col1.getSchemaQualifiedName(), schema.getQualifiedName());
        assertEquals(col1.getTableName(), TBL_NAME);
        assertEquals(col1.getTableQualifiedName(), table.getQualifiedName());
    }

    @Test(
            groups = {"mesh.create.domain"},
            dependsOnGroups = {"mesh.create.connection"})
    void createDomain() throws AtlanException {
        DataDomain toCreate = DataDomain.creator(DOMAIN_NAME).build();
        AssetMutationResponse response = toCreate.save();
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof DataDomain);
        domain = (DataDomain) one;
        assertNotNull(domain.getGuid());
        assertNotNull(domain.getQualifiedName());
        assertEquals(domain.getName(), DOMAIN_NAME);
    }

    @Test(
            groups = {"mesh.create.subdomain"},
            dependsOnGroups = {"mesh.create.domain"})
    void createSubDomain() throws AtlanException {
        DataDomain toCreate =
                DataDomain.creator(SUB_DOMAIN_NAME, domain.getQualifiedName()).build();
        AssetMutationResponse response = toCreate.save();
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset parent = response.getUpdatedAssets().get(0);
        assertTrue(parent instanceof DataDomain);
        assertEquals(parent.getGuid(), domain.getGuid());
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof DataDomain);
        subDomain = (DataDomain) one;
        assertNotNull(subDomain.getGuid());
        assertNotNull(subDomain.getQualifiedName());
        assertEquals(subDomain.getName(), SUB_DOMAIN_NAME);
    }

    @Test(
            groups = {"mesh.create.product"},
            dependsOnGroups = {"mesh.create.subdomain", "mesh.create.table"})
    void createProduct() throws AtlanException {
        FluentSearch query = Table.select()
                .where(Table.QUALIFIED_NAME.eq(table.getQualifiedName()))
                .build();
        DataProduct toCreate = DataProduct.creator(PROD_NAME, subDomain.getQualifiedName(), query)
                .build();
        AssetMutationResponse response = toCreate.save();
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset parent = response.getUpdatedAssets().get(0);
        assertTrue(parent instanceof DataDomain);
        assertEquals(parent.getGuid(), subDomain.getGuid());
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof DataProduct);
        product = (DataProduct) one;
        assertNotNull(product.getGuid());
        assertNotNull(product.getQualifiedName());
        assertEquals(product.getName(), PROD_NAME);
    }

    @Test(
            groups = {"mesh.create.contract"},
            dependsOnGroups = {"mesh.create.col1"})
    void createContract() throws AtlanException {
        String contractJson = getContractJson("Automated testing of the Java SDK.");
        DataContract toCreate = DataContract.creator(contractJson, table).build();
        AssetMutationResponse response = toCreate.save();
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset related = response.getUpdatedAssets().get(0);
        assertTrue(related instanceof Table);
        assertEquals(related.getGuid(), table.getGuid());
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof DataContract);
        contract = (DataContract) one;
        assertNotNull(contract.getGuid());
        assertNotNull(contract.getQualifiedName());
    }

    @Test(
            groups = {"mesh.read.contract"},
            dependsOnGroups = {"mesh.create.contract"})
    void readContractFromAsset() throws AtlanException {
        Table tbl = Table.get(table.getGuid());
        assertNotNull(tbl.getDataContractLatest());
        assertEquals(tbl.getDataContractLatest().getGuid(), contract.getGuid());
    }

    @Test(
            groups = {"mesh.update.contract"},
            dependsOnGroups = {"mesh.read.contract"})
    void updateContract() throws AtlanException {
        String contractJson = getContractJson("Automated testing of the Java SDK (UPDATED).");
        DataContract toCreate = DataContract.creator(contractJson, table).build();
        AssetMutationResponse response = toCreate.save();
        assertNotNull(response);
        // TODO: leaving out any further validation until next stage of Data Contract changes
    }

    private String getContractJson(String description) {
        return "{"
                + "\"type\": \"Table\","
                + "\"status\": \"DRAFT\","
                + "\"kind\": \"DataContract\","
                + "\"data_source\": \"" + connection.getName() + "\","
                + "\"dataset\": \"" + TBL_NAME + "\","
                + "\"description\": \"" + description + "\""
                + "}";
    }

    @Test(
            groups = {"mesh.purge.product"},
            dependsOnGroups = {"mesh.create.*", "mesh.read.*", "mesh.update.*"},
            alwaysRun = true)
    void purgeProduct() throws AtlanException, InterruptedException {
        AssetMutationResponse response = DataProduct.purge(product.getGuid());
        assertNotNull(response);
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof DataProduct);
        DataProduct deletedProduct =
                response.getDeletedAssets(DataProduct.class).get(0);
        assertEquals(deletedProduct.getGuid(), product.getGuid());
        assertEquals(deletedProduct.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"mesh.purge.domains"},
            dependsOnGroups = {"mesh.create.*", "mesh.read.*", "mesh.update.*", "mesh.purge.product"},
            alwaysRun = true)
    void purgeDomains() throws AtlanException, InterruptedException {
        AssetMutationResponse response = DataDomain.purge(subDomain.getGuid());
        assertNotNull(response);
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof DataDomain);
        DataDomain deletedDomain = response.getDeletedAssets(DataDomain.class).get(0);
        assertEquals(deletedDomain.getGuid(), subDomain.getGuid());
        assertEquals(deletedDomain.getStatus(), AtlanStatus.DELETED);
        response = DataDomain.purge(domain.getGuid());
        assertNotNull(response);
        assertEquals(response.getDeletedAssets().size(), 1);
        one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof DataDomain);
        deletedDomain = response.getDeletedAssets(DataDomain.class).get(0);
        assertEquals(deletedDomain.getGuid(), domain.getGuid());
        assertEquals(deletedDomain.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"mesh.purge.connection"},
            dependsOnGroups = {"mesh.create.*", "mesh.read.*", "mesh.update.*", "mesh.purge.domains"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
