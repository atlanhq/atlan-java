/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanPackageType;
import com.atlan.model.workflow.WorkflowSearchRequest;
import com.atlan.model.workflow.WorkflowSearchResult;
import com.atlan.model.workflow.WorkflowSearchResultDetail;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests most aspects of workflows (packages).
 */
@Slf4j
public class WorkflowTest extends AtlanLiveTest {

    private static String workflowName;
    private static String workflowRun;
    private static WorkflowSearchResultDetail wfl;
    private static WorkflowSearchResult run;

    @Test(groups = {"pkg.search.type"})
    void findByType() throws AtlanException {
        List<WorkflowSearchResult> results = WorkflowSearchRequest.findByType(AtlanPackageType.SNOWFLAKE, 5);
        assertNotNull(results);
        assertFalse(results.isEmpty());
        wfl = results.get(0).get_source();
        workflowName = results.get(0).get_id();
    }

    @Test(
            groups = {"pkg.search.latest"},
            dependsOnGroups = {"pkg.search.type"})
    void findLatestRun() throws AtlanException {
        run = WorkflowSearchRequest.findLatestRun(workflowName);
        assertNotNull(run);
        workflowRun = run.get_id();
    }

    @Test(
            groups = {"pkg.search.run"},
            dependsOnGroups = {"pkg.search.latest"})
    void findRunByName() throws AtlanException {
        WorkflowSearchResult result = WorkflowSearchRequest.findRunByName(workflowRun);
        assertNotNull(result);
        assertEquals(result, run);
    }

    @Test(
            groups = {"pkg.search.id"},
            dependsOnGroups = {"pkg.search.run"})
    void findById() throws AtlanException {
        WorkflowSearchResult result = WorkflowSearchRequest.findById(workflowName);
        assertNotNull(result);
        assertEquals(result.get_source(), wfl);
    }
}
