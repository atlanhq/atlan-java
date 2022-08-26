package com.atlan.model.responses;

import com.atlan.model.workflow.WorkflowSearchHits;
import com.atlan.net.ApiResource;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkflowSearchResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    Long took;
    WorkflowSearchHits hits;
    Map<String, Object> _shards;
}
