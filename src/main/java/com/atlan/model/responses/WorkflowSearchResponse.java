package com.atlan.model.responses;

import com.atlan.model.admin.WorkflowSearchHits;
import com.atlan.net.ApiResource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkflowSearchResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    Long took;
    WorkflowSearchHits hits;
    Map<String, Object> _shards;
}
