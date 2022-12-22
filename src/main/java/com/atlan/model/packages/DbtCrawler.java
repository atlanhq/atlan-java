/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.cache.RoleCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.admin.PackageParameter;
import com.atlan.model.assets.Connection;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.workflow.*;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbtCrawler extends AbstractCrawler {

    public static final String PREFIX = "atlan-dbt";

    /**
     * Builds the minimal object necessary to create a new crawler for dbt,
     * using API-based authentication for multi-tenant cloud, with the default settings.
     *
     * @param connectionName name of the connection to create
     * @param apiToken through which to access dbt APIs
     * @return the minimal workflow necessary to crawl dbt
     * @throws AtlanException if there is any issue obtaining the admin role GUID
     */
    public static Workflow mtCloudAuth(String connectionName, String apiToken) throws AtlanException {
        return mtCloudAuth(connectionName, apiToken, List.of(RoleCache.getIdForName("$admin")), null, null, null, null);
    }

    /**
     * Builds the minimal object necessary to create a new crawler for dbt.
     *
     * @param connectionName name of the connection to create
     * @param apiToken through which to access dbt APIs
     * @param adminRoles the GUIDs of the roles that can administer this connection
     * @param adminGroups the names of the groups that can administer this connection
     * @param adminUsers the names of the users that can administer this connection
     * @param includeAssets which assets to include when crawling (when null: all). The map should be keyed
     *                      by dbt Cloud account ID, with the list of values being project IDs.
     * @param excludeAssets which assets to exclude when crawling (when null: none). The map should be keyed
     *                      by dbt Cloud account ID, with the list of values being project IDs.
     * @return the minimal workflow necessary to crawl dbt
     * @throws InvalidRequestException if there is no administrator specified for the connection, or the provided filters cannot be serialized to JSON
     */
    public static Workflow mtCloudAuth(
            String connectionName,
            String apiToken,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers,
            Map<String, List<String>> includeAssets,
            Map<String, List<String>> excludeAssets)
            throws InvalidRequestException {

        Connection connection = Connection.creator(
                        connectionName, AtlanConnectorType.DBT, adminRoles, adminGroups, adminUsers)
                .allowQuery(true)
                .allowQueryPreview(true)
                .rowLimit(10000L)
                .defaultCredentialGuid("{{credentialGuid}}")
                .sourceLogo("https://assets.atlan.com/assets/dbt-new.svg")
                .isDiscoverable(true)
                .isEditable(false)
                .build();

        String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());

        Map<String, Object> credentialBody = new HashMap<>();
        credentialBody.put("name", "default-dbt-" + epoch + "-0");
        credentialBody.put("host", "https://cloud.getdbt.com");
        credentialBody.put("port", 443);
        credentialBody.put("authType", "token");
        credentialBody.put("username", "");
        credentialBody.put("password", apiToken);
        credentialBody.put("extra", Collections.emptyMap());
        credentialBody.put("connectorConfigName", "atlan-connectors-dbt");

        Map<String, Map<String, Map<String, String>>> toInclude = buildDbtCloudFilter(includeAssets);
        Map<String, Map<String, Map<String, String>>> toExclude = buildDbtCloudFilter(excludeAssets);

        WorkflowTaskArguments.WorkflowTaskArgumentsBuilder<?, ?> argsBuilder = WorkflowTaskArguments.builder()
                .parameter(NameValuePair.of("connection", connection.toJson()))
                .parameter(NameValuePair.of("extraction-method", "api"))
                .parameter(NameValuePair.of("deployment-type", "multi"))
                .parameter(NameValuePair.of("core-extraction-method", "s3"))
                .parameter(NameValuePair.of("api-credential-guid", "{{credentialGuid}}"));
        try {
            argsBuilder = argsBuilder.parameter(
                    NameValuePair.of("include-filter", Serde.allInclusiveMapper.writeValueAsString(toInclude)));
            argsBuilder = argsBuilder.parameter(
                    NameValuePair.of("exclude-filter", Serde.allInclusiveMapper.writeValueAsString(toExclude)));
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException(
                    "Unable to translate the provided include/exclude asset filters into JSON.",
                    "includeAssets/excludeAssets",
                    "ATLAN_JAVA_CLIENT-400-601",
                    400,
                    e);
        }
        argsBuilder = argsBuilder
                .parameter(NameValuePair.of("include-filter-core", "*"))
                .parameter(NameValuePair.of("exclude-filter-core", "*"));

        String name = PREFIX + "-" + epoch;
        String runName = PREFIX + "-default-dbt-" + epoch;
        return Workflow.builder()
                .metadata(WorkflowMetadata.builder()
                        .label("orchestration.atlan.com/certified", "true")
                        .label("orchestration.atlan.com/source", "dbt")
                        .label("orchestration.atlan.com/sourceCategory", "elt")
                        .label("orchestration.atlan.com/type", "connector")
                        .label("orchestration.atlan.com/verified", "true")
                        .label("package.argoproj.io/installer", "argopm")
                        .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hdbt")
                        .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                        .label("orchestration.atlan.com/default-dbt-" + epoch, "true")
                        .label("orchestration.atlan.com/atlan-ui", "true")
                        .annotation("orchestration.atlan.com/allowSchedule", "true")
                        .annotation("orchestration.atlan.com/dependentPackage", "")
                        .annotation(
                                "orchestration.atlan.com/docsUrl",
                                "https://ask.atlan.com/hc/en-us/articles/6335824578705")
                        .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                        .annotation("orchestration.atlan.com/icon", "https://assets.atlan.com/assets/dbt-new.svg")
                        .annotation("orchestration.atlan.com/logo", "https://assets.atlan.com/assets/dbt-new.svg")
                        .annotation(
                                "orchestration.atlan.com/marketplaceLink",
                                "https://packages.atlan.com/-/web/detail/@atlan/dbt")
                        .annotation("orchestration.atlan.com/name", "dbt Assets")
                        .annotation("orchestration.atlan.com/usecase", "crawling")
                        .annotation("package.argoproj.io/author", "Atlan")
                        .annotation(
                                "package.argoproj.io/description",
                                "Package to crawl dbt Assets and publish to Atlan for discovery.")
                        .annotation(
                                "package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/dbt")
                        .annotation("package.argoproj.io/keywords", "[\"connector\",\"crawler\",\"dbt\"]")
                        .annotation("package.argoproj.io/name", "@atlan/dbt")
                        .annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                        .annotation(
                                "package.argoproj.io/repository",
                                "git+https://github.com/atlanhq/marketplace-packages.git")
                        .annotation("package.argoproj.io/support", "support@atlan.com")
                        .annotation("orchestration.atlan.com/atlanName", runName)
                        .name(name)
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
                                                        .name("atlan-dbt")
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
