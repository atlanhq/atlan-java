/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.api.WorkflowsEndpoint;
import com.atlan.cache.RoleCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.LogicException;
import com.atlan.model.assets.Connection;
import com.atlan.model.core.Entity;
import com.atlan.model.core.EntityMutationResponse;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanWorkflowPhase;
import com.atlan.model.packages.ConnectionDelete;
import com.atlan.model.workflow.Workflow;
import com.atlan.model.workflow.WorkflowResponse;
import java.util.List;
import org.slf4j.Logger;
import org.testng.annotations.Test;

/**
 * Utility methods for managing connections.
 */
public class ConnectionTest extends AtlanLiveTest {

    private static final String PREFIX = "ConnectionTest";

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
        if (adminRoleGuid != null) {
            Connection connection = Connection.creator(prefix, type, List.of(adminRoleGuid), null, null)
                    .build();
            EntityMutationResponse response = connection.upsert();
            assertNotNull(response);
            assertTrue(response.getUpdatedEntities().isEmpty());
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getCreatedEntities().size(), 1);
            Entity one = response.getCreatedEntities().get(0);
            assertTrue(one instanceof Connection);
            connection = (Connection) one;
            assertNotNull(connection.getGuid());
            assertNotNull(connection.getQualifiedName());
            assertEquals(connection.getName(), prefix);
            Entity minimal;
            do {
                minimal = Entity.retrieveMinimal(connection.getGuid());
            } while (minimal == null);
            return connection;
        } else {
            throw new LogicException(
                    "Unable to find the $admin role in the environment.", "ATLAN_JAVA_SDK-500-002", 500);
        }
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
        Workflow deleteWorkflow = ConnectionDelete.creator(qualifiedName, true);
        WorkflowResponse response = deleteWorkflow.run();
        assertNotNull(response);
        String workflowName = response.getMetadata().getName();
        AtlanWorkflowPhase state = response.monitorStatus(log);
        assertNotNull(state);
        assertEquals(state, AtlanWorkflowPhase.SUCCESS);
        WorkflowsEndpoint.archive(workflowName);
    }

    @Test(groups = {"invalid.connection"})
    void invalidConnection() {
        assertThrows(
                InvalidRequestException.class,
                () -> Connection.creator(PREFIX, AtlanConnectorType.POSTGRES, null, null, null));
    }
}
