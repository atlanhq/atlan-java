/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.AtlanClient;
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

public class FivetranCrawler extends AbstractCrawler {

    public static final String PREFIX = "atlan-fivetran";

    /**
     * Builds the minimal object necessary to create a new crawler for Fivetran,
     * using basic authentication, with the default settings.
     *
     * @param client connectivity to Atlan
     * @param connectionName name of the connection to create
     * @param apiKey through which to access Fivetran APIs
     * @param apiSecret through which to access Fivetran APIs
     * @return the minimal workflow necessary to crawl Fivetran
     * @throws AtlanException if there is any issue obtaining the admin role GUID
     */
    public static Workflow directApiAuth(AtlanClient client, String connectionName, String apiKey, String apiSecret)
            throws AtlanException {
        return directApiAuth(
                client,
                connectionName,
                apiKey,
                apiSecret,
                List.of(client.getRoleCache().getIdForName("$admin")),
                null,
                null);
    }

    /**
     * Builds the minimal object necessary to create a new crawler for Fivetran.
     *
     * @param client connectivity to Atlan
     * @param connectionName name of the connection to create
     * @param apiKey through which to access Fivetran APIs
     * @param apiSecret through which to access Fivetran APIs
     * @param adminRoles the GUIDs of the roles that can administer this connection
     * @param adminGroups the names of the groups that can administer this connection
     * @param adminUsers the names of the users that can administer this connection
     * @return the minimal workflow necessary to crawl Fivetran
     * @throws InvalidRequestException if there is no administrator specified for the connection, or the provided filters cannot be serialized to JSON
     * @throws com.atlan.exception.NotFoundException if the specified administrator does not exist
     * @throws AtlanException on any other error, such as an inability to retrieve the users, groups or roles in Atlan
     */
    public static Workflow directApiAuth(
            AtlanClient client,
            String connectionName,
            String apiKey,
            String apiSecret,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers)
            throws AtlanException {

        Connection connection = Connection.creator(
                        connectionName, AtlanConnectorType.FIVETRAN, adminRoles, adminGroups, adminUsers)
                .allowQuery(true)
                .allowQueryPreview(true)
                .rowLimit(10000L)
                .defaultCredentialGuid("{{credentialGuid}}")
                .sourceLogo(
                        "https://res.cloudinary.com/crunchbase-production/image/upload/c_lpad,f_auto,q_auto:eco,dpr_1/mmhosuxvz2msbiieekl3")
                .isDiscoverable(true)
                .isEditable(false)
                .build();

        String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());

        Map<String, Object> credentialBody = new HashMap<>();
        credentialBody.put("name", "default-fivetran-" + epoch + "-0");
        credentialBody.put("host", "https://api.fivetran.com");
        credentialBody.put("port", 443);
        credentialBody.put("authType", "api");
        credentialBody.put("username", apiKey);
        credentialBody.put("password", apiSecret);
        credentialBody.put("extra", Collections.emptyMap());
        credentialBody.put("connectorConfigName", "atlan-connectors-fivetran");

        WorkflowParameters.WorkflowParametersBuilder<?, ?> argsBuilder = WorkflowParameters.builder()
                .parameter(NameValuePair.of("connection", connection.toJson(client)))
                .parameter(NameValuePair.of("credential-guid", "{{credentialGuid}}"));

        String runName = PREFIX + "-" + epoch;
        String atlanName = PREFIX + "-default-fivetran-" + epoch;
        return Workflow.builder()
                .metadata(WorkflowMetadata.builder()
                        .label("orchestration.atlan.com/certified", "true")
                        .label("orchestration.atlan.com/source", "fivetran")
                        .label("orchestration.atlan.com/sourceCategory", "elt")
                        .label("orchestration.atlan.com/type", "connector")
                        .label("orchestration.atlan.com/verified", "true")
                        .label("package.argoproj.io/installer", "argopm")
                        .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hfivetran")
                        .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                        .label("orchestration.atlan.com/default-fivetran-" + epoch, "true")
                        .label("orchestration.atlan.com/atlan-ui", "true")
                        .annotation("orchestration.atlan.com/allowSchedule", "true")
                        .annotation("orchestration.atlan.com/dependentPackage", "")
                        .annotation(
                                "orchestration.atlan.com/docsUrl",
                                "https://ask.atlan.com/hc/en-us/articles/8427123935121")
                        .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                        .annotation(
                                "orchestration.atlan.com/icon",
                                "https://res.cloudinary.com/crunchbase-production/image/upload/c_lpad,f_auto,q_auto:eco,dpr_1/mmhosuxvz2msbiieekl3")
                        .annotation(
                                "orchestration.atlan.com/logo",
                                "https://alternative.me/media/256/fivetran-icon-qfxkppdpdx2oh4r9-c.png")
                        .annotation(
                                "orchestration.atlan.com/marketplaceLink",
                                "https://packages.atlan.com/-/web/detail/@atlan/fivetran")
                        .annotation("orchestration.atlan.com/name", "Fivetran Enrichment")
                        .annotation("orchestration.atlan.com/usecase", "crawling,enrichment")
                        .annotation("package.argoproj.io/author", "Atlan")
                        .annotation(
                                "package.argoproj.io/description",
                                "Enrich known assets associated with Fivetran Connectors with column-level lineage.  Requires access to Fivetran's Metadata API.")
                        .annotation(
                                "package.argoproj.io/homepage",
                                "https://packages.atlan.com/-/web/detail/@atlan/fivetran")
                        .annotation("package.argoproj.io/keywords", "[\"connector\",\"elt\",\"fivetran\",\"lineage\"]")
                        .annotation("package.argoproj.io/name", "@atlan/fivetran")
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
                                .annotation("package.argoproj.io/name", "@atlan/fivetran")
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
