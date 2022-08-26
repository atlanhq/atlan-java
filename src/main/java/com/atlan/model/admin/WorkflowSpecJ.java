package com.atlan.model.admin;

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
public class WorkflowSpecJ extends AtlanObjectJ {
    private static final long serialVersionUID = 2L;

    List<WorkflowTemplateJ> templates;
    String entrypoint;
    final Object arguments;
}
