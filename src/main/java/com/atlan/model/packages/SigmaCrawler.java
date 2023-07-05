/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.admin.PackageParameter;
import com.atlan.model.assets.Connection;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.workflow.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SigmaCrawler extends AbstractCrawler {

    public static final String PREFIX = "atlan-sigma";

    /**
     * Builds the minimal object necessary to create a new crawler for Sigma,
     * using token-based authentication, with the default settings.
     *
     * @param connectionName name of the connection to create
     * @param hostname of the Sigma instance
     * @param clientId through which to access Sigma
     * @param apiToken through which to access Sigma
     * @return the minimal workflow necessary to crawl Sigma
     * @throws AtlanException if there is any issue obtaining the admin role GUID
     */
    public static Workflow tokenBased(String connectionName, String hostname, String clientId, String apiToken)
            throws AtlanException {
        return tokenBased(
                connectionName,
                hostname,
                443,
                clientId,
                apiToken,
                List.of(Atlan.getDefaultClient().getRoleCache().getIdForName("$admin")),
                null,
                null,
                "{}",
                "{}");
    }

    /**
     * Builds the minimal object necessary to create a new crawler for Sigma.
     *
     * @param connectionName name of the connection to create
     * @param hostname of the Sigma instance
     * @param port on which the Sigma instance is running
     * @param clientId through which to access Sigma
     * @param apiToken through which to access Sigma
     * @param adminRoles the GUIDs of the roles that can administer this connection
     * @param adminGroups the names of the groups that can administer this connection
     * @param adminUsers the names of the users that can administer this connection
     * @param includeFilter the assets to include when crawling
     * @param excludeFilter the assets to exclude when crawling
     * @return the minimal workflow necessary to crawl Sigma
     * @throws InvalidRequestException if there is no administrator specified for the connection, or the provided filters cannot be serialized to JSON
     * @throws com.atlan.exception.NotFoundException if the specified administrator does not exist
     * @throws AtlanException on any other error, such as an inability to retrieve the users, groups or roles in Atlan
     */
    public static Workflow tokenBased(
            String connectionName,
            String hostname,
            int port,
            String clientId,
            String apiToken,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers,
            String includeFilter,
            String excludeFilter)
            throws AtlanException {

        Connection connection = Connection.creator(
                        connectionName, AtlanConnectorType.SIGMA, adminRoles, adminGroups, adminUsers)
                .allowQuery(true)
                .allowQueryPreview(true)
                .rowLimit(10000L)
                .defaultCredentialGuid("{{credentialGuid}}")
                .sourceLogo("http://assets.atlan.com/assets/sigma.svg")
                .isDiscoverable(true)
                .isEditable(false)
                .build();

        String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());

        Map<String, Object> credentialBody = new HashMap<>();
        credentialBody.put("name", "default-sigma-" + epoch + "-0");
        credentialBody.put("host", hostname);
        credentialBody.put("port", port);
        credentialBody.put("authType", "api_token");
        credentialBody.put("username", clientId);
        credentialBody.put("password", apiToken);
        credentialBody.put("extra", Collections.emptyMap());
        credentialBody.put("connectorConfigName", "atlan-connectors-sigma");

        WorkflowParameters.WorkflowParametersBuilder<?, ?> argsBuilder = WorkflowParameters.builder()
                .parameter(NameValuePair.of("credential-guid", "{{credentialGuid}}"))
                .parameter(NameValuePair.of("atlas-auth-type", "internal"))
                .parameter(NameValuePair.of("include-filter", includeFilter))
                .parameter(NameValuePair.of("exclude-filter", excludeFilter))
                .parameter(NameValuePair.of("connection", connection.toJson()))
                .parameter(NameValuePair.of("publish-mode", "production"));

        String atlanName = PREFIX + "-default-sigma-" + epoch;
        String runName = PREFIX + "-" + epoch;
        return Workflow.builder()
                .metadata(WorkflowMetadata.builder()
                        .label("orchestration.atlan.com/certified", "true")
                        .label("orchestration.atlan.com/source", "sigma")
                        .label("orchestration.atlan.com/sourceCategory", "bi")
                        .label("orchestration.atlan.com/type", "connector")
                        .label("orchestration.atlan.com/verified", "true")
                        .label("package.argoproj.io/installer", "argopm")
                        .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hsigma")
                        .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                        .label("orchestration.atlan.com/default-sigma-" + epoch, "true")
                        .label("orchestration.atlan.com/atlan-ui", "true")
                        .annotation("orchestration.atlan.com/allowSchedule", "true")
                        .annotation("orchestration.atlan.com/categories", "sigma,crawler")
                        .annotation("orchestration.atlan.com/dependentPackage", "")
                        .annotation(
                                "orchestration.atlan.com/docsUrl",
                                "https://ask.atlan.com/hc/en-us/articles/8731744918813")
                        .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                        .annotation("orchestration.atlan.com/icon", "http://assets.atlan.com/assets/sigma.svg")
                        .annotation("orchestration.atlan.com/logo", "http://assets.atlan.com/assets/sigma.svg")
                        .annotation(
                                "orchestration.atlan.com/marketplaceLink",
                                "https://packages.atlan.com/-/web/detail/@atlan/sigma")
                        .annotation("orchestration.atlan.com/name", "Sigma Assets")
                        .annotation("package.argoproj.io/author", "Atlan")
                        .annotation(
                                "package.argoproj.io/description",
                                "Package to crawl Sigma assets and publish to Atlan for discovery")
                        .annotation(
                                "package.argoproj.io/homepage",
                                "https://github.com/atlanhq/marketplace-packages#readme")
                        .annotation("package.argoproj.io/keywords", "[\"sigma\",\"bi\",\"connector\",\"crawler\"]")
                        .annotation("package.argoproj.io/name", "@atlan/sigma")
                        .annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                        .annotation(
                                "package.argoproj.io/repository",
                                "git+https://github.com/atlanhq/marketplace-packages.git")
                        .annotation("package.argoproj.io/support", "support@atlan.com")
                        .annotation("orchestration.atlan.com/atlanName", atlanName)
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
                                                        .name(PREFIX)
                                                        .template("main")
                                                        .clusterScope(true)
                                                        .build())
                                                .build())
                                        .build())
                                .build()))
                        .entrypoint("main")
                        .workflowMetadata(WorkflowMetadata.builder()
                                .annotation("package.argoproj.io/name", "@atlan/sigma")
                                .build())
                        .build())
                .payload(List.of(PackageParameter.builder()
                        .parameter("credentialGuid")
                        .type("credential")
                        .body(credentialBody)
                        .build()))
                .build();
    }
}
