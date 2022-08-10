package com.atlan.model.admin;

import com.atlan.model.enums.AtlanWorkflowPhase;
import com.atlan.net.AtlanObject;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkflowSearchResultStatus extends AtlanObject {
    private static final long serialVersionUID = 2L;

    AtlanWorkflowPhase phase;
    String startedAt;
    String finishedAt;
    String progress;
    List<Object> conditions;
    Map<String, Long> resourcesDuration;
}
