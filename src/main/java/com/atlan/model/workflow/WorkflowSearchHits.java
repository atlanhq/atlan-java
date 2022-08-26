package com.atlan.model.workflow;

import com.atlan.net.AtlanObjectJ;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkflowSearchHits extends AtlanObjectJ {
    private static final long serialVersionUID = 2L;

    Map<String, Object> total;
    List<WorkflowSearchResult> hits;
}
