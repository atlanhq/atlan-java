package com.atlan.model.admin;

import com.atlan.net.AtlanObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkflowSearchHits extends AtlanObject {
    private static final long serialVersionUID = 2L;

    Map<String, Object> total;
    List<WorkflowSearchResult> hits;
}
