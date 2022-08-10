package com.atlan.model.admin;

import com.atlan.net.AtlanObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class WorkflowSpec extends AtlanObject {
    private static final long serialVersionUID = 2L;

    List<WorkflowTemplate> templates;
    String entrypoint;
    final Object arguments;
}
