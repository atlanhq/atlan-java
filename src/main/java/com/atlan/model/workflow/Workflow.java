package com.atlan.model.workflow;

import com.atlan.api.WorkflowsEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.model.responses.WorkflowResponse;
import com.atlan.net.AtlanObjectJ;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Workflow extends AtlanObjectJ {
    private static final long serialVersionUID = 2L;

    WorkflowMetadata metadata;
    WorkflowSpec spec;
    List<Object> payload;

    /**
     * Run the workflow immediately.
     * @return the details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public WorkflowResponse run() throws AtlanException {
        return WorkflowsEndpoint.run(this);
    }
}
