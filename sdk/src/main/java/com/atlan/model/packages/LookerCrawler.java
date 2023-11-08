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

public class LookerCrawler extends AbstractCrawler {

    public static final String PREFIX = AtlanPackageType.LOOKER.getValue();

    /**
     * Builds the minimal object necessary to create a new crawler for Looker,
     * using basic authentication, with the default settings.
     *
     * @param client connectivity to Atlan
     * @param connectionName name of the connection to create
     * @param hostname of the Looker instance
     * @param clientId through which to access Looker
     * @param clientSecret through which to access Looker
     * @return the minimal workflow necessary to crawl Looker
     * @throws AtlanException if there is any issue obtaining the admin role GUID
     */
    public static Workflow directResourceOwner(
            AtlanClient client, String connectionName, String hostname, String clientId, String clientSecret)
            throws AtlanException {
        return directResourceOwner(
                client,
                connectionName,
                hostname,
                443,
                clientId,
                clientSecret,
                null,
                null,
                List.of(client.getRoleCache().getIdForName("$admin")),
                null,
                null,
                null,
                null,
                null,
                null);
    }

    /**
     * Builds the minimal object necessary to create a new crawler for Looker.
     *
     * @param client connectivity to Atlan
     * @param connectionName name of the connection to create
     * @param hostname of the Looker instance
     * @param port on which the Looker instance is running
     * @param clientId through which to access Looker
     * @param clientSecret through which to access Looker
     * @param privateKey the SSH private key to use to connect to Git for field-level lineage (or null, to skip field-level lineage)
     * @param privateKeyPassphrase the passphrase for the SSH private key
     * @param adminRoles the GUIDs of the roles that can administer this connection
     * @param adminGroups the names of the groups that can administer this connection
     * @param adminUsers the names of the users that can administer this connection
     * @param includeFolders the numeric IDs of folders to include when crawling (when null: all)
     * @param includeProjects the names of projects to include when crawling (when null: all)
     * @param excludeFolders the numeric IDs of folders to exclude when crawling (when null: none)
     * @param excludeProjects the names of projects to exclude when crawling (when null: none)
     * @return the minimal workflow necessary to crawl Looker
     * @throws InvalidRequestException if there is no administrator specified for the connection, or the provided filters cannot be serialized to JSON
     * @throws com.atlan.exception.NotFoundException if the specified administrator does not exist
     * @throws AtlanException on any other error, such as an inability to retrieve the users, groups or roles in Atlan
     */
    public static Workflow directResourceOwner(
            AtlanClient client,
            String connectionName,
            String hostname,
            int port,
            String clientId,
            String clientSecret,
            String privateKey,
            String privateKeyPassphrase,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers,
            List<String> includeFolders,
            List<String> includeProjects,
            List<String> excludeFolders,
            List<String> excludeProjects)
            throws AtlanException {

        boolean fieldLevelLineage = privateKey != null && !privateKey.equals("");

        Connection connection = Connection.creator(
                        connectionName, AtlanConnectorType.LOOKER, adminRoles, adminGroups, adminUsers)
                .allowQuery(true)
                .allowQueryPreview(true)
                .rowLimit(10000L)
                .defaultCredentialGuid("{{credentialGuid}}")
                .sourceLogo("https://www.pngrepo.com/png/354012/512/looker-icon.png")
                .isDiscoverable(true)
                .isEditable(false)
                .build();

        String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());

        Map<String, String> extraMap = new HashMap<>();
        if (fieldLevelLineage) {
            extraMap.put("ssh_private_key", privateKey);
            extraMap.put("passphrase", privateKeyPassphrase);
        }

        Map<String, Object> credentialBody = new HashMap<>();
        credentialBody.put("name", "default-looker-" + epoch + "-0");
        credentialBody.put("host", hostname);
        credentialBody.put("port", port);
        credentialBody.put("authType", "resource_owner");
        credentialBody.put("username", clientId);
        credentialBody.put("password", clientSecret);
        credentialBody.put("extra", extraMap);
        credentialBody.put("connectorConfigName", "atlan-connectors-looker");

        Map<String, Map<String, String>> toIncludeFolders = buildFlatFilter(includeFolders);
        Map<String, Map<String, String>> toExcludeFolders = buildFlatFilter(excludeFolders);
        Map<String, Map<String, String>> toIncludeProjects = buildFlatFilter(includeProjects);
        Map<String, Map<String, String>> toExcludeProjects = buildFlatFilter(excludeProjects);

        WorkflowParameters.WorkflowParametersBuilder<?, ?> argsBuilder;
        try {
            argsBuilder = WorkflowParameters.builder()
                    .parameter(NameValuePair.of("credential-guid", "{{credentialGuid}}"))
                    .parameter(NameValuePair.of(
                            "include-folders", Serde.allInclusiveMapper.writeValueAsString(toIncludeFolders)))
                    .parameter(NameValuePair.of(
                            "exclude-folders", Serde.allInclusiveMapper.writeValueAsString(toExcludeFolders)))
                    .parameter(NameValuePair.of(
                            "include-projects", Serde.allInclusiveMapper.writeValueAsString(toIncludeProjects)))
                    .parameter(NameValuePair.of(
                            "exclude-projects", Serde.allInclusiveMapper.writeValueAsString(toExcludeProjects)))
                    .parameter(NameValuePair.of("connection", connection.toJson(client)))
                    .parameter(NameValuePair.of("use-field-level-lineage", "" + fieldLevelLineage))
                    .parameter(NameValuePair.of("extraction-method", "direct"));
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
        }

        String atlanName = PREFIX + "-default-looker-" + epoch;
        String runName = PREFIX + "-" + epoch;
        return Workflow.builder()
                .metadata(WorkflowMetadata.builder()
                        .label("orchestration.atlan.com/certified", "true")
                        .label("orchestration.atlan.com/source", "looker")
                        .label("orchestration.atlan.com/sourceCategory", "bi")
                        .label("orchestration.atlan.com/type", "connector")
                        .label("orchestration.atlan.com/verified", "true")
                        .label("package.argoproj.io/installer", "argopm")
                        .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hlooker")
                        .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                        .label("orchestration.atlan.com/default-looker-" + epoch, "true")
                        .label("orchestration.atlan.com/atlan-ui", "true")
                        .annotation("orchestration.atlan.com/allowSchedule", "true")
                        .annotation("orchestration.atlan.com/dependentPackage", "")
                        .annotation(
                                "orchestration.atlan.com/docsUrl",
                                "https://ask.atlan.com/hc/en-us/articles/6330214610193")
                        .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                        .annotation(
                                "orchestration.atlan.com/icon",
                                "https://www.pngrepo.com/png/354012/512/looker-icon.png")
                        .annotation(
                                "orchestration.atlan.com/logo", "https://looker.com/assets/img/images/logos/looker.svg")
                        .annotation(
                                "orchestration.atlan.com/marketplaceLink",
                                "https://packages.atlan.com/-/web/detail/@atlan/looker")
                        .annotation("orchestration.atlan.com/name", "Looker Assets")
                        .annotation("orchestration.atlan.com/usecase", "crawling,auto-classifications")
                        .annotation("package.argoproj.io/author", "Atlan")
                        .annotation(
                                "package.argoproj.io/description",
                                "Package to crawl Looker assets and publish to Atlan for discovery")
                        .annotation(
                                "package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/looker")
                        .annotation(
                                "package.argoproj.io/keywords",
                                "[\"looker\",\"bi\",\"connector\",\"crawler\",\"lookml\"]")
                        .annotation("package.argoproj.io/name", "@atlan/looker")
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
                                .annotation("package.argoproj.io/name", "@atlan/looker")
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
