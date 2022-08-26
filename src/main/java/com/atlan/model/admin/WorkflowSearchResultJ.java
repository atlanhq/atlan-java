package com.atlan.model.admin;

import com.atlan.model.enums.AtlanWorkflowPhase;
import com.atlan.net.AtlanObjectJ;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkflowSearchResultJ extends AtlanObjectJ {
    private static final long serialVersionUID = 2L;

    String _index;
    String _type;
    String _id;
    Object _seq_no;
    Object _primary_term;
    List<Object> sort;
    WorkflowSearchResultDetailJ _source;

    /** Retrieve the status of the workflow run. */
    public AtlanWorkflowPhase getStatus() {
        if (_source != null) {
            WorkflowSearchResultStatusJ status = _source.getStatus();
            if (status != null) {
                return status.getPhase();
            }
        }
        return null;
    }
}
