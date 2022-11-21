/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.model.workflow.*;

import java.util.Collections;
import java.util.List;

public class ConnectionDelete {

    public static final String PREFIX = "atlan-connection-delete";

    /**
     * Builds the minimal object necessary to create a new column delete workflow.
     *
     * @param qualifiedName of the connection to delete
     * @param purge if true, hard-delete (purge) the connection and its assets, otherwise soft-delete (archive) them
     * @return the minimal workflow necessary to do the deletion
     */
    public static Workflow creator(String qualifiedName, boolean purge) {
        String runName = PREFIX + "-" + (System.currentTimeMillis() / 1000);
        return Workflow.builder()
                .metadata(WorkflowMetadata.builder()
                        .label("orchestration.atlan.com/certified", "true")
                        .label("orchestration.atlan.com/type", "utility")
                        .label("orchestration.atlan.com/verified", "true")
                        .label("package.argoproj.io/installer", "argopm")
                        .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hconnection-delete")
                        .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                        .label("orchestration.atlan.com/atlan-ui", "true")
                        .annotation("orchestration.atlan.com/allowSchedule", "false")
                        .annotation("orchestration.atlan.com/categories", "utility,admin,connection,delete")
                        .annotation("orchestration.atlan.com/dependentPackage", "")
                        .annotation(
                                "orchestration.atlan.com/docsUrl",
                                "https://ask.atlan.com/hc/en-us/articles/6755306791697")
                        .annotation("orchestration.atlan.com/emoji", "🗑️")
                        .annotation(
                                "orchestration.atlan.com/icon", "https://atlan.com/assets/img/atlan-blue.6ed81a56.svg")
                        .annotation(
                                "orchestration.atlan.com/logo", "https://atlan.com/assets/img/atlan-blue.6ed81a56.svg")
                        .annotation(
                                "orchestration.atlan.com/marketplaceLink",
                                "https://packages.atlan.com/-/web/detail/@atlan/connection-delete")
                        .annotation("orchestration.atlan.com/name", "Connection Delete")
                        .annotation("package.argoproj.io/author", "Atlan")
                        .annotation(
                                "package.argoproj.io/description", "Deletes a connection and all its related assets")
                        .annotation(
                                "package.argoproj.io/homepage",
                                "https://packages.atlan.com/-/web/detail/@atlan/connection-delete")
                        .annotation("package.argoproj.io/keywords", "[\"delete\",\"admin\",\"utility\"]")
                        .annotation("package.argoproj.io/name", "@atlan/connection-delete")
                        .annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                        .annotation(
                                "package.argoproj.io/repository",
                                "git+https://github.com/atlanhq/marketplace-packages.git")
                        .annotation("package.argoproj.io/support", "support@atlan.com")
                        .annotation("orchestration.atlan.com/atlanName", runName)
                        .name(runName)
                        .namespace("default")
                        .build())
                .spec(WorkflowSpec.builder()
                        .templates(List.of(WorkflowTemplate.builder()
                                .name("main")
                                .dag(WorkflowDAG.builder()
                                        .task(WorkflowTask.builder()
                                                .name("run")
                                                .arguments(WorkflowTaskArguments.builder()
                                                        .parameter(NameValuePair.builder()
                                                                .name("connection-qualified-name")
                                                                .value(qualifiedName)
                                                                .build())
                                                        .parameter(NameValuePair.builder()
                                                                .name("delete-assets")
                                                                .value(true)
                                                                .build())
                                                        .parameter(NameValuePair.builder()
                                                                .name("delete-type")
                                                                .value(purge ? "HARD" : "SOFT")
                                                                .build())
                                                        .build())
                                                .templateRef(WorkflowTemplateRef.builder()
                                                        .name("atlan-connection-delete")
                                                        .template("main")
                                                        .clusterScope(true)
                                                        .build())
                                                .build())
                                        .build())
                                .build()))
                        .entrypoint("main")
                        .build())
                .payload(Collections.emptyList())
                .build();
    }
}
