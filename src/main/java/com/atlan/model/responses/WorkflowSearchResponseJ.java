package com.atlan.model.responses;

import com.atlan.model.admin.WorkflowSearchHitsJ;
import com.atlan.net.ApiResourceJ;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkflowSearchResponseJ extends ApiResourceJ {
    private static final long serialVersionUID = 2L;

    Long took;
    WorkflowSearchHitsJ hits;
    Map<String, Object> _shards;
}
