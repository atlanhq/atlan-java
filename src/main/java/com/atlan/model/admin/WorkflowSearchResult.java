package com.atlan.model.admin;

import com.atlan.model.enums.AtlanWorkflowPhase;
import com.atlan.net.AtlanObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
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
}
