/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Connection;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.net.HttpClient;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.testng.annotations.Test;

/**
 * Utility methods for managing connections.
 */
@Slf4j
public class ConnectionTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("CONN");

    /**
     * Create a new connection with a unique name.
     *
     * @param prefix to make the connection unique
     * @param type of the connection to create
     * @return the connection that was created
     * @throws AtlanException on any error creating or reading-back the connection
     * @throws InterruptedException if the creation retry loop was interrupted
     */
    public static Connection createConnection(String prefix, AtlanConnectorType type)
            throws AtlanException, InterruptedException {
        return createConnection(client, prefix, type);
    }

    /**
     * Create a new connection with a unique name.
     *
     * @param client connectivity to the Atlan tenant in which to create the connection
     * @param prefix to make the connection unique
     * @param type of the connection to create
     * @return the connection that was created
     * @throws AtlanException on any error creating or reading-back the connection
     * @throws InterruptedException if the creation retry loop was interrupted
     */
    public static Connection createConnection(AtlanClient client, String prefix, AtlanConnectorType type)
            throws AtlanException, InterruptedException {
        String adminRoleGuid = client.getRoleCache().getIdForName("$admin");
        Connection connection = Connection.creator(client, prefix, type, List.of(adminRoleGuid), null, null)
                .build();
        AssetMutationResponse response = null;
        int retryCount = 0;
        while (response == null && retryCount < Atlan.getMaxNetworkRetries()) {
            retryCount++;
            try {
                response = connection.save(client).block();
            } catch (InvalidRequestException e) {
                if (retryCount < Atlan.getMaxNetworkRetries()) {
                    if (e.getCode() != null
                            && e.getCode().equals("ATLAN-JAVA-400-000")
                            && e.getMessage().equals("Server responded with ATLAS-400-00-029: Auth request failed")) {
                        Thread.sleep(HttpClient.waitTime(retryCount).toMillis());
                    }
                } else {
                    log.error("Overran retry limit ({}), rethrowing exception.", Atlan.getMaxNetworkRetries());
                    throw e;
                }
            }
        }
        assertNotNull(response);
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof Connection);
        connection = (Connection) one;
        assertNotNull(connection.getGuid());
        assertNotNull(connection.getQualifiedName());
        assertEquals(connection.getName(), prefix);
        return connection;
    }

    /**
     * Run the connection delete package for the specified connection, and block until it
     * completes successfully.
     *
     * @param qualifiedName of the connection to delete
     * @param log into which to write status information
     * @throws AtlanException on any errors deleting the connection
     * @throws InterruptedException if the busy-wait loop for monitoring is interuppted
     */
    public static void deleteConnection(String qualifiedName, Logger log) throws AtlanException, InterruptedException {
        deleteConnection(client, qualifiedName, log);
    }

    /**
     * Delete all assets in the specified connection, then the connection itself,
     * and block until it completes successfully.
     *
     * @param client connectivity to the Atlan tenant from which to purge the connection
     * @param qualifiedName of the connection to delete
     * @param log into which to write status information
     * @throws AtlanException on any errors deleting the connection and its assets
     * @throws InterruptedException if the busy-wait loop for monitoring is interuppted
     */
    public static void deleteConnection(AtlanClient client, String qualifiedName, Logger log)
            throws AtlanException, InterruptedException {
        List<String> guids =
                client.assets.select().where(Asset.QUALIFIED_NAME.startsWith(qualifiedName)).pageSize(50).stream()
                        .map(Asset::getGuid)
                        .collect(Collectors.toList());
        if (!guids.isEmpty()) {
            int totalToDelete = guids.size();
            log.info(" --- Purging {} assets from {}... ---", totalToDelete, qualifiedName);
            if (totalToDelete < 20) {
                client.assets.delete(guids, AtlanDeleteType.PURGE).block();
            } else {
                for (int i = 0; i < totalToDelete; i += 20) {
                    log.info(" ... next batch of 20 ({}%)", Math.round((i * 100.0) / totalToDelete));
                    List<String> sublist = guids.subList(i, Math.min(i + 20, totalToDelete));
                    client.assets.delete(sublist, AtlanDeleteType.PURGE).block();
                }
            }
        }
        // Purge the connection itself, now that all assets are purged
        Optional<Asset> found = Connection.select(client).where(Connection.QUALIFIED_NAME.eq(qualifiedName)).stream()
                .findFirst();
        if (found.isPresent()) {
            client.assets.delete(found.get().getGuid(), AtlanDeleteType.PURGE).block();
        }
    }

    @Test(groups = {"invalid.connection"})
    void invalidConnection() {
        assertThrows(
                InvalidRequestException.class,
                () -> Connection.creator(client, PREFIX, AtlanConnectorType.POSTGRES, null, null, null));
    }

    @Test(groups = {"invalid.connection.roles"})
    void invalidConnectionAdminRole() {
        assertThrows(
                NotFoundException.class,
                () -> Connection.creator(client, PREFIX, AtlanConnectorType.SAPHANA, List.of("abc123"), null, null));
    }

    @Test(groups = {"invalid.connection.groups"})
    void invalidConnectionAdminGroup() {
        assertThrows(
                NotFoundException.class,
                () -> Connection.creator(
                        client, PREFIX, AtlanConnectorType.SAPHANA, null, List.of("NONEXISTENT"), null));
    }

    @Test(groups = {"invalid.connection.users"})
    void invalidConnectionAdminUser() {
        assertThrows(
                NotFoundException.class,
                () -> Connection.creator(
                        client, PREFIX, AtlanConnectorType.SAPHANA, null, null, List.of("NONEXISTENT")));
    }
}
