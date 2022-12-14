/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.Atlan;
import com.atlan.api.WorkflowsEndpoint;
import com.atlan.cache.RoleCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Connection;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanWorkflowPhase;
import com.atlan.model.packages.ConnectionDelete;
import com.atlan.model.workflow.Workflow;
import com.atlan.model.workflow.WorkflowResponse;
import com.atlan.net.HttpClient;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.testng.annotations.Test;

/**
 * Utility methods for managing connections.
 */
@Slf4j
public class ConnectionTest extends AtlanLiveTest {

    private static final String PREFIX = "ConnectionTest";

    private static AtomicInteger retryCount = new AtomicInteger(0);

    /**
     * Create a new connection with a unique name.
     *
     * @param prefix to make the connection unique
     * @param type of the connection to create
     * @return the connection that was created
     * @throws AtlanException on any error creating or reading-back the connection
     */
    static Connection createConnection(String prefix, AtlanConnectorType type) throws AtlanException {
        String adminRoleGuid = RoleCache.getIdForName("$admin");
        Connection connection = Connection.creator(prefix, type, List.of(adminRoleGuid), null, null)
                .build();
        AssetMutationResponse response = connection.upsert();
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
        Asset minimal;
        do {
            minimal = Asset.retrieveMinimal(connection.getGuid());
        } while (minimal == null);
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
    static void deleteConnection(String qualifiedName, Logger log) throws AtlanException, InterruptedException {
        try {
            Workflow deleteWorkflow = ConnectionDelete.creator(qualifiedName, true);
            WorkflowResponse response = deleteWorkflow.run();
            assertNotNull(response);
            // If we get here we've succeeded in running, so we'll reset our retry counter
            retryCount.set(0);
            String workflowName = response.getMetadata().getName();
            AtlanWorkflowPhase state = response.monitorStatus(log);
            assertNotNull(state);
            assertEquals(state, AtlanWorkflowPhase.SUCCESS);
            WorkflowsEndpoint.archive(workflowName);
        } catch (InvalidRequestException e) {
            // Can happen if two deletion workflows are run at the same time,
            // in which case we should wait a few seconds and try again
            int attempt = retryCount.incrementAndGet();
            log.warn("Race condition on parallel deletion, waiting to retry ({})...", attempt);
            Thread.sleep(HttpClient.waitTime(attempt).toMillis());
            if (attempt < Atlan.getMaxNetworkRetries()) {
                deleteConnection(qualifiedName, log);
            }
        }
    }

    @Test(groups = {"invalid.connection"})
    void invalidConnection() {
        assertThrows(
                InvalidRequestException.class,
                () -> Connection.creator(PREFIX, AtlanConnectorType.POSTGRES, null, null, null));
    }
}
