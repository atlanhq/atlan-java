package com.atlan.model.admin;

import com.atlan.net.AtlanObjectJ;
import java.util.List;
import java.util.Map;
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
public class WorkflowMetadataJ extends AtlanObjectJ {
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
