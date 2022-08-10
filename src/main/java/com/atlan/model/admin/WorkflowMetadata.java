package com.atlan.model.admin;

import com.atlan.net.AtlanObject;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class WorkflowMetadata extends AtlanObject {
    private static final long serialVersionUID = 2L;

    Map<String, String> labels;
    Map<String, String> annotations;

    String name;
    String namespace;
    final String uid;
    final String resourceVersion;
    final Long generation;
    final String creationTimestamp;
    final List<Object> managedFields;
}
