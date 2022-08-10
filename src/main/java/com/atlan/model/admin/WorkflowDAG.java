package com.atlan.model.admin;

import com.atlan.net.AtlanObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class WorkflowDAG extends AtlanObject {
    private static final long serialVersionUID = 2L;

    @Singular
    List<WorkflowTask> tasks;
}
