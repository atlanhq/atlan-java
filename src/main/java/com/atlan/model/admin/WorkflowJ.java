package com.atlan.model.admin;

import com.atlan.api.WorkflowsEndpointJ;
import com.atlan.exception.AtlanException;
import com.atlan.model.responses.WorkflowResponseJ;
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
public class WorkflowJ extends AtlanObjectJ {
    private static final long serialVersionUID = 2L;

    WorkflowMetadataJ metadata;
    WorkflowSpecJ spec;
    List<Object> payload;

    /**
     * Run the workflow immediately.
     * @return the details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public WorkflowResponseJ run() throws AtlanException {
        return WorkflowsEndpointJ.run(this);
    }
}
