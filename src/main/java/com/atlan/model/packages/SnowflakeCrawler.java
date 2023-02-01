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

public class SnowflakeCrawler extends AbstractCrawler {

    public static final String PREFIX = "atlan-snowflake";

    /**
     * Builds the minimal object necessary to create a new crawler for Snowflake,
     * using basic authentication, with the default settings.
     *
     * @param connectionName name of the connection to create
     * @param hostname of the Snowflake instance
     * @param username through which to access Snowflake
     * @param password through which to access Snowflake
     * @param role name of the role within Snowflake to crawl through
     * @param warehouse name of the warehouse within Snowflake to crawl through
     * @return the minimal workflow necessary to crawl Snowflake
     * @throws AtlanException if there is any issue obtaining the admin role GUID
     */
    public static Workflow infoSchemaBasicAuth(
            String connectionName, String hostname, String username, String password, String role, String warehouse)
            throws AtlanException {
        return infoSchemaBasicAuth(
                connectionName,
                hostname,
                443,
                username,
                password,
                role,
                warehouse,
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
     * Builds the minimal object necessary to create a new crawler for Snowflake.
     *
     * @param connectionName name of the connection to create
     * @param hostname of the Snowflake instance
     * @param port on which the Snowflake instance is running
     * @param username through which to access Snowflake
     * @param password through which to access Snowflake
     * @param role name of the role within Snowflake to crawl through
     * @param warehouse name of the warehouse within Snowflake to crawl through
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
     * @return the minimal workflow necessary to crawl Snowflake
     * @throws InvalidRequestException if there is no administrator specified for the connection, or the provided filters cannot be serialized to JSON
     * @throws com.atlan.exception.NotFoundException if the specified administrator does not exist
     * @throws AtlanException on any other error, such as an inability to retrieve the users, groups or roles in Atlan
     */
    public static Workflow infoSchemaBasicAuth(
            String connectionName,
            String hostname,
            int port,
            String username,
            String password,
            String role,
            String warehouse,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers,
            boolean allowQuery,
            boolean allowQueryPreview,
            long rowLimit,
            Map<String, List<String>> includeAssets,
            Map<String, List<String>> excludeAssets)
            throws AtlanException {

        Connection connection = Connection.creator(
                        connectionName, AtlanConnectorType.SNOWFLAKE, adminRoles, adminGroups, adminUsers)
                .allowQuery(allowQuery)
                .allowQueryPreview(allowQueryPreview)
                .rowLimit(rowLimit)
                .defaultCredentialGuid("{{credentialGuid}}")
                .sourceLogo("https://docs.snowflake.com/en/_images/logo-snowflake-sans-text.png")
                .isDiscoverable(true)
                .isEditable(false)
                .build();

        String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());

        Map<String, Object> credentialBody = new HashMap<>();
        credentialBody.put("name", "default-snowflake-" + epoch + "-0");
        credentialBody.put("host", hostname);
        credentialBody.put("port", port);
        credentialBody.put("authType", "basic");
        credentialBody.put("username", username);
        credentialBody.put("password", password);
        credentialBody.put("extra", Map.of("role", role, "warehouse", warehouse));
        credentialBody.put("connectorConfigName", "atlan-connectors-snowflake");

        Map<String, List<String>> toInclude = buildHierarchicalFilter(includeAssets);
        Map<String, List<String>> toExclude = buildHierarchicalFilter(excludeAssets);

        WorkflowParameters.WorkflowParametersBuilder<?, ?> argsBuilder = WorkflowParameters.builder()
                .parameter(NameValuePair.builder()
                        .name("credential-guid")
                        .value("{{credentialGuid}}")
                        .build())
                .parameter(NameValuePair.builder()
                        .name("extract-strategy")
                        .value("information-schema")
                        .build())
                .parameter(NameValuePair.builder()
                        .name("account-usage-database-name")
                        .value("SNOWFLAKE")
                        .build())
                .parameter(NameValuePair.builder()
                        .name("account-usage-schema-name")
                        .value("ACCOUNT_USAGE")
                        .build())
                .parameter(NameValuePair.builder()
                        .name("control-config-strategy")
                        .value("default")
                        .build())
                .parameter(NameValuePair.builder()
                        .name("enable-lineage")
                        .value(true)
                        .build())
                .parameter(NameValuePair.builder()
                        .name("connection")
                        .value(connection.toJson())
                        .build());
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

        String atlanName = PREFIX + "-default-snowflake-" + epoch;
        String runName = PREFIX + "-" + epoch;
        return Workflow.builder()
                .metadata(WorkflowMetadata.builder()
                        .label("orchestration.atlan.com/certified", "true")
                        .label("orchestration.atlan.com/source", "snowflake")
                        .label("orchestration.atlan.com/sourceCategory", "warehouse")
                        .label("orchestration.atlan.com/type", "connector")
                        .label("orchestration.atlan.com/verified", "true")
                        .label("package.argoproj.io/installer", "argopm")
                        .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hsnowflake")
                        .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                        .label("orchestration.atlan.com/default-snowflake-" + epoch, "true")
                        .label("orchestration.atlan.com/atlan-ui", "true")
                        .annotation("orchestration.atlan.com/allowSchedule", "true")
                        .annotation("orchestration.atlan.com/categories", "warehouse,crawler")
                        .annotation("orchestration.atlan.com/dependentPackage", "")
                        .annotation(
                                "orchestration.atlan.com/docsUrl",
                                "https://ask.atlan.com/hc/en-us/articles/6037440864145")
                        .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                        .annotation(
                                "orchestration.atlan.com/icon",
                                "https://docs.snowflake.com/en/_images/logo-snowflake-sans-text.png")
                        .annotation(
                                "orchestration.atlan.com/logo",
                                "https://1amiydhcmj36tz3733v94f15-wpengine.netdna-ssl.com/wp-content/themes/snowflake/assets/img/logo-blue.svg")
                        .annotation(
                                "orchestration.atlan.com/marketplaceLink",
                                "https://packages.atlan.com/-/web/detail/@atlan/snowflake")
                        .annotation("orchestration.atlan.com/name", "Snowflake Assets")
                        .annotation("package.argoproj.io/author", "Atlan")
                        .annotation(
                                "package.argoproj.io/description",
                                "Package to crawl snowflake assets and publish to Atlan for discovery")
                        .annotation(
                                "package.argoproj.io/homepage",
                                "https://packages.atlan.com/-/web/detail/@atlan/snowflake")
                        .annotation(
                                "package.argoproj.io/keywords",
                                "[\"snowflake\",\"warehouse\",\"connector\",\"crawler\"]")
                        .annotation("package.argoproj.io/name", "@atlan/snowflake")
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
