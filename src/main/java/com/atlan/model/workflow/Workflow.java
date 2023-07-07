/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.PackageParameter;
import com.atlan.model.core.AtlanObject;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Workflow extends AtlanObject {
    private static final long serialVersionUID = 2L;

    WorkflowMetadata metadata;
    WorkflowSpec spec;
    List<PackageParameter> payload;

    /**
     * Run the workflow immediately.
     *
     * @return the details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public WorkflowResponse run() throws AtlanException {
        return run(Atlan.getDefaultClient());
    }

    /**
     * Run the workflow immediately.
     *
     * @param client connectivity to the Atlan tenant on which to run the workflow
     * @return the details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public WorkflowResponse run(AtlanClient client) throws AtlanException {
        return client.workflows().run(this);
    }
}
