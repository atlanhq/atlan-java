package com.atlan.model.admin;

import com.atlan.net.AtlanObjectJ;
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
public class WorkflowTemplateJ extends AtlanObjectJ {
    private static final long serialVersionUID = 2L;

    String name;
    final Object inputs;
    final Object outputs;
    final Object metadata;
    WorkflowDAGJ dag;
}
