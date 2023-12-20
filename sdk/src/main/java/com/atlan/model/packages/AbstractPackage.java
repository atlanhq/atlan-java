/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.model.admin.Credential;
import com.atlan.model.admin.PackageParameter;
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
    protected String _prefix;

    /** Unique name of the package, usually @atlan/something. */
    protected String _name;

    /** Unique name for the run of a package. */
    protected String _runName;

    /** Labels associated with the package. */
    @Singular
    Map<String, String> _labels;

    /** Annotations associated with the package. */
    @Singular
    Map<String, String> _annotations;

    /** Parameters associated with the package. */
    @Singular
    Map<String, String> _parameters;

    /** Credentials for the package to access its source. */
    Credential.CredentialBuilder<?, ?> _credential;

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
        for (Map.Entry<String, String> entry : _parameters.entrySet()) {
            argsBuilder.parameter(NameValuePair.of(entry.getKey(), entry.getValue()));
        }
        List<PackageParameter> payload;
        if (_credential != null) {
            payload = List.of(PackageParameter.builder()
                    .parameter("credentialGuid")
                    .type("credential")
                    .body(_credential.build().toMap())
                    .build());
        } else {
            payload = List.of();
        }
        return Workflow.builder()
                .metadata(WorkflowMetadata.builder()
                        .labels(_labels)
                        .annotations(_annotations)
                        .name(_runName)
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
                                                        .name(_prefix)
                                                        .template("main")
                                                        .clusterScope(true)
                                                        .build())
                                                .build())
                                        .build())
                                .build()))
                        .entrypoint("main")
                        .workflowMetadata(WorkflowMetadata.builder()
                                .annotation("package.argoproj.io/name", _name)
                                .build())
                        .build())
                .payload(payload)
                .build();
    }

    public abstract static class AbstractPackageBuilder<
            C extends AbstractPackage, B extends AbstractPackageBuilder<C, B>> {

        /** Unique timestamp for the crawler. */
        protected String epoch;

        /**
         * Set up the crawler with its foundational information.
         *
         * @param prefix the unique prefix that identifies the type of the crawler
         * @param name the unique name of the crawler package
         * @return the builder, with all the common elements of the crawler configured
         */
        B setup(String prefix, String name) {
            this.epoch = getEpoch();
            return this._prefix(prefix)
                    ._name(name)
                    ._runName(prefix + "-" + epoch)
                    .metadata();
        }

        /**
         * Set all the annotations, labels and common parameters specific to the crawler.
         * (This will be called automatically by the 'setup' method.)
         *
         * @return the builder, with all the common metadata configured
         */
        protected abstract B metadata();
    }
}
