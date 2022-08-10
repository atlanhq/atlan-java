package com.atlan.model.admin;

import com.atlan.net.AtlanObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkflowSearchResultDetail extends AtlanObject {
    private static final long serialVersionUID = 2L;

    String kind;
    String apiVersion;
    Object metadata;
    Object spec;
    WorkflowSearchResultStatus status;
}
