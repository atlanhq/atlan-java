package com.atlan.functional;

import static org.testng.Assert.*;

import com.atlan.api.WorkflowsEndpoint;
import com.atlan.cache.RoleCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.Connection;
import com.atlan.model.admin.Packages;
import com.atlan.model.admin.Workflow;
import com.atlan.model.admin.WorkflowSearchRequest;
import com.atlan.model.admin.WorkflowSearchResult;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanConnectionCategory;
import com.atlan.model.enums.AtlanWorkflowPhase;
import com.atlan.model.responses.EntityMutationResponse;
import com.atlan.model.responses.WorkflowResponse;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class DataStudioTest extends AtlanLiveTest {

    public static final String CONNECTION_NAME = "jc-test-connection";

    private static String connectionGuid = null;
    private static String connectionQame = null;

    @Test(groups = {"connection.invalid"})
    void invalidConnection() {
        assertThrows(
                InvalidRequestException.class,
                () -> Connection.toCreate(CONNECTION_NAME, AtlanConnectionCategory.BI, "datastudio", null, null, null));
    }

    @Test(groups = {"connection.create"})
    void createConnection() {
        try {
            String adminRoleGuid = RoleCache.getIdForName("$admin");
            if (adminRoleGuid != null) {
                Connection connection = Connection.toCreate(
                        CONNECTION_NAME,
                        AtlanConnectionCategory.BI,
                        "datastudio",
                        Collections.singletonList(adminRoleGuid),
                        null,
                        null);
                EntityMutationResponse response = connection.upsert();
                assertNotNull(response);
                assertTrue(response.getUpdatedEntities().isEmpty());
                assertTrue(response.getDeletedEntities().isEmpty());
                assertEquals(response.getCreatedEntities().size(), 1);
                Entity one = response.getCreatedEntities().get(0);
                assertNotNull(one);
                assertEquals(one.getTypeName(), Connection.TYPE_NAME);
                assertTrue(one instanceof Connection);
                connection = (Connection) one;
                connectionGuid = connection.getGuid();
                assertNotNull(connectionGuid);
                connectionQame = connection.getQualifiedName();
                assertNotNull(connectionQame);
                assertEquals(connection.getName(), CONNECTION_NAME);
            }
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a connection.");
        }
    }

    @Test(
            groups = {"connection.purge"},
            dependsOnGroups = {"connection.create"})
    void purgeConnection() {
        try {
            // 1. Run the connection delete workflow
            Workflow deleteWorkflow = Packages.getConnectionDelete(connectionQame, true);
            WorkflowResponse response = deleteWorkflow.run();
            assertNotNull(response);
            String workflowName = response.getMetadata().getName();
            // 2. Busy-wait until the deletion is finished
            AtlanWorkflowPhase status = null;
            do {
                final WorkflowSearchResult runDetails = WorkflowSearchRequest.findLatestRun(workflowName);
                if (runDetails != null) {
                    status = runDetails.getStatus();
                }
                log.info("Workflow status: {}", status);
                Thread.sleep(5000);
            } while (status != null && status != AtlanWorkflowPhase.SUCCESS);
            // 3. Archive the (one-off) connection delete workflow itself
            WorkflowsEndpoint.archive(workflowName);
        } catch (AtlanException | InterruptedException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to delete a connection.");
        }
    }
}
