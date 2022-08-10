package com.atlan.model.admin;

import com.atlan.net.AtlanObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class WorkflowTask extends AtlanObject {
    private static final long serialVersionUID = 2L;

    String name;
    WorkflowTaskArguments arguments;
    WorkflowTemplateRef templateRef;
}
