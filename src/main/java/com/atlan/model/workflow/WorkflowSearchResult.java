/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanWorkflowPhase;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class WorkflowSearchResult extends AtlanObject {
    private static final long serialVersionUID = 2L;

    String _index;
    String _type;
    String _id;
    Object _seq_no;
    Object _primary_term;
    List<Object> sort;
    WorkflowSearchResultDetail _source;

    /** Retrieve the status of the workflow run. */
    public AtlanWorkflowPhase getStatus() {
        if (_source != null) {
            WorkflowSearchResultStatus status = _source.getStatus();
            if (status != null) {
                return status.getPhase();
            }
        }
        return null;
    }

    /**
     * Re-run this workflow.
     *
     * @return details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public WorkflowRunResponse rerun() throws AtlanException {
        return rerun(Atlan.getDefaultClient());
    }

    /**
     * Re-run this workflow.
     *
     * @param client connectivity to the Atlan tenant on which to rerun the workflow
     * @return details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public WorkflowRunResponse rerun(AtlanClient client) throws AtlanException {
        if (_source != null) {
            return client.workflows().run(_source);
        }
        return null;
    }
}
