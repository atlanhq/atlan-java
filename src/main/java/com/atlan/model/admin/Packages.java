package com.atlan.model.admin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Packages {

    public static Workflow getConnectionDelete(String qualifiedName, boolean purge) {
        String runName = "atlan-connection-delete-" + (System.currentTimeMillis() / 1000);
        Map<String, String> labels = new HashMap<>();
        labels.put("orchestration.atlan.com/certified", "true");
        labels.put("orchestration.atlan.com/type", "utility");
        labels.put("orchestration.atlan.com/verified", "true");
        labels.put("package.argoproj.io/installer", "argopm");
        labels.put("package.argoproj.io/name", "a-t-ratlans-l-a-s-hconnection-delete");
        labels.put("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com");
        labels.put("orchestration.atlan.com/atlan-ui", "true");
        Map<String, String> annotations = new HashMap<>();
        annotations.put("orchestration.atlan.com/allowSchedule", "false");
        annotations.put("orchestration.atlan.com/categories", "utility,admin,connection,delete");
        annotations.put("orchestration.atlan.com/dependentPackage", "");
        annotations.put("orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6755306791697");
        annotations.put("orchestration.atlan.com/emoji", "🗑️");
        annotations.put("orchestration.atlan.com/icon", "https://atlan.com/assets/img/atlan-blue.6ed81a56.svg");
        annotations.put("orchestration.atlan.com/logo", "https://atlan.com/assets/img/atlan-blue.6ed81a56.svg");
        annotations.put(
                "orchestration.atlan.com/marketplaceLink",
                "https://packages.atlan.com/-/web/detail/@atlan/connection-delete");
        annotations.put("orchestration.atlan.com/name", "Connection Delete");
        annotations.put("package.argoproj.io/author", "Atlan");
        annotations.put("package.argoproj.io/description", "Deletes a connection and all its related assets");
        annotations.put(
                "package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/connection-delete");
        annotations.put("package.argoproj.io/keywords", "[\"delete\",\"admin\",\"utility\"]");
        annotations.put("package.argoproj.io/name", "@atlan/connection-delete");
        annotations.put("package.argoproj.io/registry", "https://packages.atlan.com");
        annotations.put("package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git");
        annotations.put("package.argoproj.io/support", "support@atlan.com");
        annotations.put("orchestration.atlan.com/atlanName", runName);
        return Workflow.builder()
                .metadata(WorkflowMetadata.builder()
                        .labels(labels)
                        .annotations(annotations)
                        .name(runName)
                        .namespace("default")
                        .build())
                .spec(WorkflowSpec.builder()
                        .templates(Collections.singletonList(WorkflowTemplate.builder()
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
                .build();
    }
}
