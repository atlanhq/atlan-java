/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.probable.guacamole.instances;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.Database;
import com.atlan.model.assets.Schema;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.AssetBatch;
import com.probable.guacamole.ExtendedModelGenerator;
import com.probable.guacamole.model.assets.GuacamoleColumn;
import com.probable.guacamole.model.assets.GuacamoleTable;
import com.probable.guacamole.model.enums.GuacamoleTemperature;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InstanceManager extends ExtendedModelGenerator {

    private static Connection connection = null;

    InstanceManager(AtlanClient client) {
        super(client);
    }

    public static void main(String[] args) {
        try (AtlanClient client = new AtlanClient()) {
            InstanceManager im = new InstanceManager(client);
            im.createConnection();
            im.createOOTBEntities();
            im.createCustomEntities();
            im.readEntities();
            im.updateEntities();
            im.searchEntities();
            im.purgeEntities();
        } catch (Exception e) {
            log.error("Failed to cleanup client.", e);
        }
    }

    void createConnection() {
        try {
            List<Connection> results = Connection.findByName(client, SERVICE_TYPE, AtlanConnectorType.MONGODB);
            if (!results.isEmpty()) {
                connection = results.get(0);
                log.info("Connection already exists, reusing it: {}", connection.getQualifiedName());
            }
        } catch (NotFoundException err) {
            try {
                Connection toCreate = Connection.creator(
                                client,
                                SERVICE_TYPE,
                                AtlanConnectorType.MONGODB,
                                List.of(client.getRoleCache().getIdForSid("$admin")),
                                null,
                                null)
                        .build();
                AssetMutationResponse response = toCreate.save(client).block();
                connection = (Connection) response.getCreatedAssets().get(0);
            } catch (AtlanException e) {
                log.error("Unable to create a new connection.", e);
            }
        } catch (AtlanException e) {
            log.error("Unable to search for existing connection.", e);
        }
    }

    void createOOTBEntities() {
        Database db = Database.creator("db", connection.getQualifiedName()).build();
        try {
            AssetMutationResponse response = db.save(client);
            log.info("Created database entity: {}", response);
        } catch (AtlanException e) {
            log.error("Failed to create new database.", e);
        }
        Schema schema =
                Schema.creator("schema", connection.getQualifiedName() + "/db").build();
        try {
            AssetMutationResponse response = schema.save(client);
            log.info("Created schema entity: {}", response);
        } catch (AtlanException e) {
            log.error("Failed to create new schema.", e);
        }
    }

    void createCustomEntities() {
        GuacamoleTable table = GuacamoleTable.creator("table", connection.getQualifiedName() + "/db/schema")
                .guacamoleTemperature(GuacamoleTemperature.HOT)
                .guacamoleArchived(false)
                .guacamoleSize(123L)
                .build();
        try {
            AssetMutationResponse response = table.save(client);
            log.info("Created table entity: {}", response);
            table = (GuacamoleTable) response.getCreatedAssets().get(0);
        } catch (AtlanException e) {
            log.error("Failed to create new guacamole table.", e);
        }
        try (AssetBatch batch = new AssetBatch(client, 20)) {
            GuacamoleColumn child1 = GuacamoleColumn.creator("column1", table.getQualifiedName(), 1)
                    .guacamoleConceptualized(123456789L)
                    .guacamoleWidth(100L)
                    .build();
            batch.add(child1);
            GuacamoleColumn child2 = GuacamoleColumn.creator("column2", table.getQualifiedName(), 2)
                    .guacamoleConceptualized(1234567890L)
                    .guacamoleWidth(200L)
                    .build();
            batch.add(child2);
            AssetMutationResponse response = batch.flush();
            log.info("Created child entities: {}", response);
        } catch (Exception e) {
            log.error("Unable to bulk-upsert Guacamole columns.", e);
        }
    }

    void readEntities() {
        final String parentQN = connection.getQualifiedName() + "/db/schema/table";
        final String child1QN = parentQN + "/column1";
        final String child2QN = parentQN + "/column2";
        try {
            GuacamoleTable table = GuacamoleTable.get(client, parentQN, true);
            assert table.getQualifiedName().equals(parentQN);
            assert table.getGuacamoleColumns().size() == 2;
            String tableGuid = table.getGuid();
            GuacamoleColumn one = GuacamoleColumn.get(client, child1QN, true);
            assert one.getQualifiedName().equals(child1QN);
            assert one.getGuacamoleTable().getGuid().equals(tableGuid);
            GuacamoleColumn two = GuacamoleColumn.get(client, child2QN, true);
            assert two.getQualifiedName().equals(child2QN);
            assert two.getGuacamoleTable().getGuid().equals(tableGuid);
        } catch (AtlanException e) {
            log.error("Unable to read entities.", e);
        }
    }

    void updateEntities() {
        GuacamoleTable toUpdate = GuacamoleTable.updater(connection.getQualifiedName() + "/db/schema/table", "table")
                .description("Now with a description!")
                .certificateStatus(CertificateStatus.DRAFT)
                .build();
        try {
            AssetMutationResponse response = toUpdate.save(client);
            log.info("Updated parent: {}", response);
        } catch (AtlanException e) {
            log.error("Unable to update entity.", e);
        }
    }

    void searchEntities() {
        IndexSearchRequest request = client.assets
                .select()
                .where(Asset.TYPE_NAME.in(List.of(GuacamoleTable.TYPE_NAME, GuacamoleColumn.TYPE_NAME)))
                .toRequest();

        try {
            IndexSearchResponse response = request.search(client);
            log.info("Found results: {}", response);
            assert response.getApproximateCount() == 3;
            assert response.getAssets().size() == 3;
        } catch (AtlanException e) {
            log.error("Unable to search.", e);
        }

        request = GuacamoleColumn.select(client).where(Asset.NAME.eq("column1")).toRequest();

        try {
            IndexSearchResponse response = request.search(client);
            log.info("Found results: {}", response);
            assert response.getApproximateCount() == 1;
            assert response.getAssets().size() == 1;
        } catch (AtlanException e) {
            log.error("Unable to search.", e);
        }

        request = GuacamoleColumn.select(client)
                .where(GuacamoleColumn.GUACAMOLE_WIDTH.gt(150L))
                .toRequest();

        try {
            IndexSearchResponse response = request.search(client);
            log.info("Found results: {}", response);
            assert response.getApproximateCount() == 1;
            assert response.getAssets().size() == 1;
        } catch (AtlanException e) {
            log.error("Unable to search.", e);
        }

        request = GuacamoleTable.select(client)
                .where(Asset.DESCRIPTION.startsWith("Now"))
                .toRequest();

        try {
            IndexSearchResponse response = request.search(client);
            log.info("Found results: {}", response);
            assert response.getApproximateCount() == 1;
            assert response.getAssets().size() == 1;
        } catch (AtlanException e) {
            log.error("Unable to search.", e);
        }
    }

    void purgeEntities() {
        final String parentQN = connection.getQualifiedName() + "/db/schema/table";
        final String child1QN = parentQN + "/column1";
        final String child2QN = parentQN + "/column2";
        try {
            GuacamoleTable parent = GuacamoleTable.get(client, parentQN);
            GuacamoleColumn one = GuacamoleColumn.get(client, child1QN);
            GuacamoleColumn two = GuacamoleColumn.get(client, child2QN);
            client.assets.delete(List.of(parent.getGuid(), one.getGuid(), two.getGuid()), AtlanDeleteType.PURGE);
            log.info("Entities purged.");
        } catch (AtlanException e) {
            log.error("Unable to purge entities.", e);
        }
    }
}
