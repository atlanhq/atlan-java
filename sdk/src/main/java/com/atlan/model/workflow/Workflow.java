/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.PackageParameter;
import com.atlan.model.core.AtlanObject;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Definition of a runnable workflow in Atlan.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class Workflow extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Metadata that describes the workflow. */
    WorkflowMetadata metadata;

    /** Specification of the workflow: per-task inputs, outputs, and logic. */
    WorkflowSpec spec;

    /** Parameters to send to the workflow as a whole (not an individual task). */
    List<PackageParameter> payload;

    /**
     * Run the workflow immediately.
     *
     * @param client connectivity to the Atlan tenant on which to run the workflow
     * @return the details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public WorkflowResponse run(AtlanClient client) throws AtlanException {
        return client.workflows.run(this);
    }
}
