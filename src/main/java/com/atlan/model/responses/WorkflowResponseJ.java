package com.atlan.model.responses;

import com.atlan.model.admin.WorkflowMetadataJ;
import com.atlan.model.admin.WorkflowSpecJ;
import com.atlan.net.ApiResourceJ;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkflowResponseJ extends ApiResourceJ {
    private static final long serialVersionUID = 2L;

    WorkflowMetadataJ metadata;
    WorkflowSpecJ spec;
    List<Object> payload;
}
