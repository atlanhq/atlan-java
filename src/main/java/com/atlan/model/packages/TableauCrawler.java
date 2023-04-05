/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.cache.RoleCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.admin.PackageParameter;
import com.atlan.model.assets.Connection;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.workflow.*;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableauCrawler extends AbstractCrawler {

    public static final String PREFIX = "atlan-tableau";

    /**
     * Builds the minimal object necessary to create a new crawler for Tableau,
     * using basic authentication, with the default settings.
     *
     * @param connectionName name of the connection to create
     * @param hostname of the Tableau instance
     * @param username through which to access Tableau
     * @param password through which to access Tableau
     * @return the minimal workflow necessary to crawl Tableau
     * @throws AtlanException if there is any issue obtaining the admin role GUID
     */
    public static Workflow basicAuth(String connectionName, String hostname, String username, String password)
            throws AtlanException {
        return basicAuth(
                connectionName,
                hostname,
                443,
                username,
                password,
                null,
                List.of(RoleCache.getIdForName("$admin")),
                null,
                null,
                null,
                null);
    }

    /**
     * Builds the minimal object necessary to create a new crawler for Tableau.
     *
     * @param connectionName name of the connection to create
     * @param hostname of the Tableau instance
     * @param port on which the Tableau instance is running
     * @param username through which to access Tableau
     * @param password through which to access Tableau
     * @param defaultSite the default Tableau site to crawl
     * @param adminRoles the GUIDs of the roles that can administer this connection
     * @param adminGroups the names of the groups that can administer this connection
     * @param adminUsers the names of the users that can administer this connection
     * @param includeProjects the GUIDs of projects to include when crawling (when null: all)
     * @param excludeProjects the GUIDs of projects to exclude when crawling (when null: none)
     * @return the minimal workflow necessary to crawl Tableau
     * @throws InvalidRequestException if there is no administrator specified for the connection, or the provided filters cannot be serialized to JSON
     * @throws com.atlan.exception.NotFoundException if the specified administrator does not exist
     * @throws AtlanException on any other error, such as an inability to retrieve the users, groups or roles in Atlan
     */
    public static Workflow basicAuth(
            String connectionName,
            String hostname,
            int port,
            String username,
            String password,
            String defaultSite,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers,
            List<String> includeProjects,
            List<String> excludeProjects)
            throws AtlanException {

        Connection connection = Connection.creator(
                        connectionName, AtlanConnectorType.TABLEAU, adminRoles, adminGroups, adminUsers)
                .allowQuery(true)
                .allowQueryPreview(true)
                .rowLimit(10000L)
                .defaultCredentialGuid("{{credentialGuid}}")
                .sourceLogo("https://img.icons8.com/color/480/000000/tableau-software.png")
                .isDiscoverable(true)
                .isEditable(false)
                .build();

        String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());

        Map<String, String> extra = new HashMap<>();
        extra.put("protocol", "https");
        if (defaultSite != null) {
            extra.put("defaultSite", defaultSite);
        }

        Map<String, Object> credentialBody = new HashMap<>();
        credentialBody.put("name", "default-tableau-" + epoch + "-0");
        credentialBody.put("host", hostname);
        credentialBody.put("port", port);
        credentialBody.put("authType", "basic");
        credentialBody.put("username", username);
        credentialBody.put("password", password);
        credentialBody.put("extra", extra);
        credentialBody.put("connectorConfigName", "atlan-connectors-tableau");

        Map<String, Map<String, String>> toIncludeProjects = buildFlatFilter(includeProjects);
        Map<String, Map<String, String>> toExcludeProjects = buildFlatFilter(excludeProjects);

        WorkflowParameters.WorkflowParametersBuilder<?, ?> argsBuilder = WorkflowParameters.builder()
                .parameter(NameValuePair.of("credential-guid", "{{credentialGuid}}"))
                .parameter(NameValuePair.of("connection", connection.toJson()))
                .parameter(NameValuePair.of("atlas-auth-type", "internal"))
                .parameter(NameValuePair.of("crawl-unpublished-worksheets-dashboards", "" + true))
                .parameter(NameValuePair.of("publish-mode", "production"));
        try {
            argsBuilder = argsBuilder
                    .parameter(NameValuePair.of(
                            "include-filter", Serde.allInclusiveMapper.writeValueAsString(toIncludeProjects)))
                    .parameter(NameValuePair.of(
                            "exclude-filter", Serde.allInclusiveMapper.writeValueAsString(toExcludeProjects)));
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
        }

        String atlanName = PREFIX + "-default-tableau-" + epoch;
        String runName = PREFIX + "-" + epoch;
        return Workflow.builder()
                .metadata(WorkflowMetadata.builder()
                        .label("orchestration.atlan.com/certified", "true")
                        .label("orchestration.atlan.com/source", "tableau")
                        .label("orchestration.atlan.com/sourceCategory", "bi")
                        .label("orchestration.atlan.com/type", "connector")
                        .label("orchestration.atlan.com/verified", "true")
                        .label("package.argoproj.io/installer", "argopm")
                        .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-htableau")
                        .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                        .label("orchestration.atlan.com/default-tableau-" + epoch, "true")
                        .label("orchestration.atlan.com/atlan-ui", "true")
                        .annotation("orchestration.atlan.com/allowSchedule", "true")
                        .annotation("orchestration.atlan.com/categories", "tableau,crawler")
                        .annotation("orchestration.atlan.com/dependentPackage", "")
                        .annotation(
                                "orchestration.atlan.com/docsUrl",
                                "https://ask.atlan.com/hc/en-us/articles/6332449996689")
                        .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                        .annotation(
                                "orchestration.atlan.com/icon",
                                "https://img.icons8.com/color/480/000000/tableau-software.png")
                        .annotation(
                                "orchestration.atlan.com/logo",
                                "https://img.icons8.com/color/480/000000/tableau-software.png")
                        .annotation(
                                "orchestration.atlan.com/marketplaceLink",
                                "https://packages.atlan.com/-/web/detail/@atlan/tableau")
                        .annotation("orchestration.atlan.com/name", "Tableau Assets")
                        // .annotation("orchestration.atlan.com/usecase", "crawling,auto-classifications")
                        .annotation("package.argoproj.io/author", "Atlan")
                        .annotation(
                                "package.argoproj.io/description",
                                "Package to crawl Tableau assets and publish to Atlan for discovery")
                        .annotation(
                                "package.argoproj.io/homepage",
                                "https://github.com/atlanhq/marketplace-packages#readme")
                        .annotation("package.argoproj.io/keywords", "[\"tableau\",\"bi\",\"connector\",\"crawler\"]")
                        .annotation("package.argoproj.io/name", "@atlan/tableau")
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
                                .annotation("package.argoproj.io/name", "@atlan/tableau")
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
