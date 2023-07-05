/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanWorkflowPhase;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
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
        if (_source != null) {
            return Atlan.getDefaultClient().workflows().run(_source);
        }
        return null;
    }
}
