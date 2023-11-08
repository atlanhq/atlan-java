/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.admin.PackageParameter;
import com.atlan.model.assets.Connection;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanPackageType;
import com.atlan.model.workflow.*;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AthenaCrawler extends AbstractCrawler {

    public static final String PREFIX = AtlanPackageType.ATHENA.getValue();

    /**
     * Builds the minimal object necessary to create a new crawler for Athena,
     * using basic authentication, with the default settings.
     *
     * @param client connectivity to Atlan
     * @param connectionName name of the connection to create
     * @param hostname of the Athena instance
     * @param accessKey through which to access Athena
     * @param secretKey through which to access Athena
     * @param s3OutputLocation S3 location where Athena can store query results
     * @return the minimal workflow necessary to crawl Athena
     * @throws AtlanException if there is any issue obtaining the admin role GUID
     */
    public static Workflow iamUserAuth(
            AtlanClient client,
            String connectionName,
            String hostname,
            String accessKey,
            String secretKey,
            String s3OutputLocation)
            throws AtlanException {
        return iamUserAuth(
                client,
                connectionName,
                hostname,
                443,
                accessKey,
                secretKey,
                s3OutputLocation,
                "primary",
                List.of(client.getRoleCache().getIdForName("$admin")),
                null,
                null,
                true,
                true,
                10000L,
                null,
                null);
    }

    /**
     * Builds the minimal object necessary to create a new crawler for Athena.
     *
     * @param client connectivity to Atlan
     * @param connectionName name of the connection to create
     * @param hostname of the Athena instance
     * @param port on which the Athena instance is running
     * @param accessKey through which to access Athena
     * @param secretKey through which to access Athena
     * @param s3OutputLocation S3 location where Athena can store query results
     * @param workgroup Athena workgroup (defaults to primary)
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
     * @return the minimal workflow necessary to crawl Athena
     * @throws InvalidRequestException if there is no administrator specified for the connection, or the provided filters cannot be serialized to JSON
     * @throws com.atlan.exception.NotFoundException if the specified administrator does not exist
     * @throws AtlanException on any other error, such as an inability to retrieve the users, groups or roles in Atlan
     */
    public static Workflow iamUserAuth(
            AtlanClient client,
            String connectionName,
            String hostname,
            int port,
            String accessKey,
            String secretKey,
            String s3OutputLocation,
            String workgroup,
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
                        connectionName, AtlanConnectorType.ATHENA, adminRoles, adminGroups, adminUsers)
                .allowQuery(allowQuery)
                .allowQueryPreview(allowQueryPreview)
                .rowLimit(rowLimit)
                .defaultCredentialGuid("{{credentialGuid}}")
                .sourceLogo("https://atlan-public.s3.eu-west-1.amazonaws.com/atlan/logos/aws-athena.png")
                .isDiscoverable(true)
                .isEditable(false)
                .build();

        String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());

        Map<String, Object> credentialBody = new HashMap<>();
        credentialBody.put("name", "default-athena-" + epoch + "-0");
        credentialBody.put("host", hostname);
        credentialBody.put("port", port);
        credentialBody.put("authType", "basic");
        credentialBody.put("username", accessKey);
        credentialBody.put("password", secretKey);
        credentialBody.put("extra", Map.of("s3_output_location", s3OutputLocation, "workgroup", workgroup));
        credentialBody.put("connectorConfigName", "atlan-connectors-athena");

        Map<String, List<String>> toInclude = buildHierarchicalFilter(includeAssets);
        Map<String, List<String>> toExclude = buildHierarchicalFilter(excludeAssets);

        WorkflowParameters.WorkflowParametersBuilder<?, ?> argsBuilder = WorkflowParameters.builder()
                .parameter(NameValuePair.of("credentials-fetch-strategy", "credential_guid"))
                .parameter(NameValuePair.of("credential-guid", "{{credentialGuid}}"))
                .parameter(NameValuePair.of("connection", connection.toJson(client)))
                .parameter(NameValuePair.of("publish-mode", "production"))
                .parameter(NameValuePair.of("atlas-auth-type", "internal"));
        try {
            if (!toInclude.isEmpty()) {
                argsBuilder = argsBuilder.parameter(
                        NameValuePair.of("include-filter", Serde.allInclusiveMapper.writeValueAsString(toInclude)));
            }
            if (!toExclude.isEmpty()) {
                argsBuilder = argsBuilder.parameter(
                        NameValuePair.of("exclude-filter", Serde.allInclusiveMapper.writeValueAsString(toExclude)));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
        }

        String atlanName = PREFIX + "-default-athena-" + epoch;
        String runName = PREFIX + "-" + epoch;
        return Workflow.builder()
                .metadata(WorkflowMetadata.builder()
                        .label("orchestration.atlan.com/certified", "true")
                        .label("orchestration.atlan.com/source", "athena")
                        .label("orchestration.atlan.com/sourceCategory", "queryengine")
                        .label("orchestration.atlan.com/type", "connector")
                        .label("orchestration.atlan.com/verified", "true")
                        .label("package.argoproj.io/installer", "argopm")
                        .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hathena")
                        .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                        .label("orchestration.atlan.com/default-athena-" + epoch, "true")
                        .label("orchestration.atlan.com/atlan-ui", "true")
                        .annotation("orchestration.atlan.com/allowSchedule", "true")
                        // .annotation("orchestration.atlan.com/categories", "warehouse,crawler")
                        .annotation("orchestration.atlan.com/dependentPackage", "")
                        .annotation(
                                "orchestration.atlan.com/docsUrl",
                                "https://ask.atlan.com/hc/en-us/articles/6325285989009")
                        .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                        .annotation(
                                "orchestration.atlan.com/icon",
                                "https://atlan-public.s3.eu-west-1.amazonaws.com/atlan/logos/aws-athena.png")
                        .annotation(
                                "orchestration.atlan.com/logo",
                                "https://atlan-public.s3.eu-west-1.amazonaws.com/atlan/logos/aws-athena.png")
                        .annotation(
                                "orchestration.atlan.com/marketplaceLink",
                                "https://packages.atlan.com/-/web/detail/@atlan/athena")
                        .annotation("orchestration.atlan.com/name", "Athena Assets")
                        .annotation("orchestration.atlan.com/usecase", "crawling,auto-classifications")
                        .annotation("package.argoproj.io/author", "Atlan")
                        .annotation(
                                "package.argoproj.io/description", "Scan all your Athena assets and publish to Atlan.")
                        .annotation(
                                "package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/athena")
                        .annotation(
                                "package.argoproj.io/keywords",
                                "[\"athena\",\"lake\",\"connector\",\"crawler\",\"glue\",\"aws\",\"s3\"]")
                        .annotation("package.argoproj.io/name", "@atlan/athena")
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
                                .annotation("package.argoproj.io/name", "@atlan/athena")
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
