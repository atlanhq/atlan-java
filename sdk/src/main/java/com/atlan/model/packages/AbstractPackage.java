/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.model.workflow.NameValuePair;
import com.atlan.model.workflow.Workflow;
import com.atlan.model.workflow.WorkflowDAG;
import com.atlan.model.workflow.WorkflowMetadata;
import com.atlan.model.workflow.WorkflowParameters;
import com.atlan.model.workflow.WorkflowSpec;
import com.atlan.model.workflow.WorkflowTask;
import com.atlan.model.workflow.WorkflowTemplate;
import com.atlan.model.workflow.WorkflowTemplateRef;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode
@ToString(callSuper = true)
@SuppressWarnings("cast")
public abstract class AbstractPackage {

    /** Uniquely identifies the type of package. */
    protected String prefix;

    /** Unique name of the package, usually @atlan/something. */
    protected String name;

    /** Unique name for the run of a package. */
    protected String runName;

    /** Labels associated with the package. */
    @Singular
    Map<String, String> labels;

    /** Annotations associated with the package. */
    @Singular
    Map<String, String> annotations;

    /** Parameters associated with the package. */
    @Singular
    Map<String, String> parameters;

    /**
     * Generate an epoch timestamp for now.
     *
     * @return the string value of the epoch timestamp
     */
    public static String getEpoch() {
        return "" + System.currentTimeMillis() / 1000;
    }

    /**
     * Convert the package into a workflow that can be submitted (run).
     *
     * @return the workflow representing the package
     */
    public Workflow toWorkflow() {
        WorkflowParameters.WorkflowParametersBuilder<?, ?> argsBuilder = WorkflowParameters.builder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            argsBuilder.parameter(NameValuePair.of(entry.getKey(), entry.getValue()));
        }
        return Workflow.builder()
                .metadata(WorkflowMetadata.builder()
                        .labels(labels)
                        .annotations(annotations)
                        .name(runName)
                        .namespace("default")
                        .build())
                .spec(WorkflowSpec.builder()
                        .templates(List.of(WorkflowTemplate.builder()
                                .name("main")
                                .dag(WorkflowDAG.builder()
                                        .task(WorkflowTask.builder()
                                                .name("run")
                                                .arguments(argsBuilder.build())
                                                .templateRef(WorkflowTemplateRef.builder()
                                                        .name(prefix)
                                                        .template("main")
                                                        .clusterScope(true)
                                                        .build())
                                                .build())
                                        .build())
                                .build()))
                        .entrypoint("main")
                        .workflowMetadata(WorkflowMetadata.builder()
                                .annotation("package.argoproj.io/name", name)
                                .build())
                        .build())
                .build();
    }
}
