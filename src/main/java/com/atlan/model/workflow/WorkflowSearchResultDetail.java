package com.atlan.model.workflow;

import com.atlan.net.AtlanObjectJ;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkflowSearchResultDetail extends AtlanObjectJ {
    private static final long serialVersionUID = 2L;

    String kind;
    String apiVersion;
    Object metadata;
    Object spec;
    WorkflowSearchResultStatus status;
}
