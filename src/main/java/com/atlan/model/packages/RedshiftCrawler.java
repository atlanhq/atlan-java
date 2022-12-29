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

public class RedshiftCrawler extends AbstractCrawler {

    public static final String PREFIX = "atlan-redshift";

    /**
     * Builds the minimal object necessary to create a new crawler for Redshift,
     * using basic authentication, with the default settings.
     *
     * @param connectionName name of the connection to create
     * @param hostname of the Redshift instance
     * @param username through which to access Redshift
     * @param password through which to access Redshift
     * @param database name of the database in Redshift to crawl
     * @return the minimal workflow necessary to crawl Redshift
     * @throws AtlanException if there is any issue obtaining the admin role GUID
     */
    public static Workflow basicAuth(
            String connectionName, String hostname, String username, String password, String database)
            throws AtlanException {
        return basicAuth(
                connectionName,
                hostname,
                5439,
                username,
                password,
                database,
                List.of(RoleCache.getIdForName("$admin")),
                null,
                null,
                true,
                true,
                10000L,
                null,
                null);
    }

    /**
     * Builds the minimal object necessary to create a new crawler for Redshift.
     *
     * @param connectionName name of the connection to create
     * @param hostname of the Redshift instance
     * @param port on which the Redshift instance is running
     * @param username through which to access Redshift
     * @param password through which to access Redshift
     * @param database name of the database in Redshift to crawl
     * @param adminRoles the GUIDs of the roles that can administer this connection
     * @param adminGroups the names of the groups that can administer this connection
     * @param adminUsers the names of the users that can administer this connection
     * @param allowQuery whether to allow SQL queries against the source (true) or not (false)
     * @param allowQueryPreview whether to allow data previews for the source (true) or not (false)
     * @param rowLimit the maximum number of rows that can be returned by a query
     * @param includeAssets which assets to include when crawling (when null: all). The map should be keyed
     *                      by database name, with the list of values giving the list of schemas within that
     *                      database to include.
     * @param excludeAssets which assets to exclude when crawling (when null: none). The map should be keyed
     *                      by database name, with the list of values giving the list of schemas within that
     *                      database to exclude.
     * @return the minimal workflow necessary to crawl Redshift
     * @throws InvalidRequestException if there is no administrator specified for the connection, or the provided filters cannot be serialized to JSON
     */
    public static Workflow basicAuth(
            String connectionName,
            String hostname,
            int port,
            String username,
            String password,
            String database,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers,
            boolean allowQuery,
            boolean allowQueryPreview,
            long rowLimit,
            Map<String, List<String>> includeAssets,
            Map<String, List<String>> excludeAssets)
            throws InvalidRequestException {

        Connection connection = Connection.creator(
                        connectionName, AtlanConnectorType.REDSHIFT, adminRoles, adminGroups, adminUsers)
                .allowQuery(allowQuery)
                .allowQueryPreview(allowQueryPreview)
                .rowLimit(rowLimit)
                .defaultCredentialGuid("{{credentialGuid}}")
                .sourceLogo("https://cdn.worldvectorlogo.com/logos/aws-redshift-logo.svg")
                .isDiscoverable(true)
                .isEditable(false)
                .build();

        String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());

        Map<String, Object> credentialBody = new HashMap<>();
        credentialBody.put("name", "default-redshift-" + epoch + "-0");
        credentialBody.put("host", hostname);
        credentialBody.put("port", port);
        credentialBody.put("authType", "basic");
        credentialBody.put("username", username);
        credentialBody.put("password", password);
        credentialBody.put("extra", Map.of("database", database));
        credentialBody.put("connectorConfigName", "atlan-connectors-redshift");

        Map<String, List<String>> toInclude = buildHierarchicalFilter(includeAssets);
        Map<String, List<String>> toExclude = buildHierarchicalFilter(excludeAssets);

        WorkflowTaskArguments.WorkflowTaskArgumentsBuilder<?, ?> argsBuilder = WorkflowTaskArguments.builder()
                .parameter(NameValuePair.of("credentials-fetch-strategy", "credential_guid"))
                .parameter(NameValuePair.of("credential-guid", "{{credentialGuid}}"))
                .parameter(NameValuePair.of("control-config-strategy", "default"))
                .parameter(NameValuePair.of("connection", connection.toJson()));
        try {
            if (!toInclude.isEmpty()) {
                argsBuilder = argsBuilder.parameter(NameValuePair.builder()
                        .name("include-filter")
                        .value(Serde.allInclusiveMapper.writeValueAsString(toInclude))
                        .build());
            }
            if (!toExclude.isEmpty()) {
                argsBuilder = argsBuilder.parameter(NameValuePair.builder()
                        .name("exclude-filter")
                        .value(Serde.allInclusiveMapper.writeValueAsString(toExclude))
                        .build());
            }
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
        }
        argsBuilder = argsBuilder
                .parameter(NameValuePair.of("publish-mode", "production"))
                .parameter(NameValuePair.of("atlas-auth-type", "internal"));

        String atlanName = PREFIX + "-default-redshift-" + epoch;
        String runName = PREFIX + "-" + epoch;
        return Workflow.builder()
                .metadata(WorkflowMetadata.builder()
                        .label("orchestration.atlan.com/certified", "true")
                        .label("orchestration.atlan.com/source", "redshift")
                        .label("orchestration.atlan.com/sourceCategory", "warehouse")
                        .label("orchestration.atlan.com/type", "connector")
                        .label("orchestration.atlan.com/verified", "true")
                        .label("package.argoproj.io/installer", "argopm")
                        .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hredshift")
                        .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                        .label("orchestration.atlan.com/default-redshift-" + epoch, "true")
                        .label("orchestration.atlan.com/atlan-ui", "true")
                        .annotation("orchestration.atlan.com/allowSchedule", "true")
                        .annotation("orchestration.atlan.com/categories", "warehouse,crawler")
                        .annotation("orchestration.atlan.com/dependentPackage", "")
                        .annotation(
                                "orchestration.atlan.com/docsUrl",
                                "https://ask.atlan.com/hc/en-us/articles/6326396122641")
                        .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                        .annotation(
                                "orchestration.atlan.com/icon",
                                "https://cdn.worldvectorlogo.com/logos/aws-redshift-logo.svg")
                        .annotation(
                                "orchestration.atlan.com/logo",
                                "https://cdn.worldvectorlogo.com/logos/aws-redshift-logo.svg")
                        .annotation(
                                "orchestration.atlan.com/marketplaceLink",
                                "https://packages.atlan.com/-/web/detail/@atlan/redshift")
                        .annotation("orchestration.atlan.com/name", "Redshift Assets")
                        .annotation("package.argoproj.io/author", "Atlan")
                        .annotation(
                                "package.argoproj.io/description",
                                "Package to crawl AWS Redshift assets and publish to Atlan for discovery")
                        .annotation(
                                "package.argoproj.io/homepage",
                                "https://packages.atlan.com/-/web/detail/@atlan/redshift")
                        .annotation(
                                "package.argoproj.io/keywords",
                                "[\"redshift\",\"warehouse\",\"connector\",\"crawler\"]")
                        .annotation("package.argoproj.io/name", "@atlan/redshift")
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
                        .build())
                .payload(List.of(PackageParameter.builder()
                        .parameter("credentialGuid")
                        .type("credential")
                        .body(credentialBody)
                        .build()))
                .build();
    }
}
