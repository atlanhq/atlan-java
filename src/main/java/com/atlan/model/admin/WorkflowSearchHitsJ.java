package com.atlan.model.admin;

import com.atlan.net.AtlanObjectJ;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkflowSearchHitsJ extends AtlanObjectJ {
    private static final long serialVersionUID = 2L;

    Map<String, Object> total;
    List<WorkflowSearchResultJ> hits;
}
