/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.admin.Credential;
import com.atlan.model.assets.Connection;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanPackageType;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PowerBICrawler extends AbstractCrawler {

    public static final String PREFIX = AtlanPackageType.POWERBI.getValue();

    /** Connectivity to the Atlan tenant where the package will run. */
    AtlanClient client;

    /** Connection through which the package will manage its assets. */
    Connection connection;

    /** Credentials for this connection. */
    Credential.CredentialBuilder<?, ?> localCreds;

    /**
     * Create the base configuration for a new Power BI crawler.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @param adminRoles unique identifiers (GUIDs) of roles who will be connection admins on the connection
     * @param adminGroups internal names of groups who will be connection admins on the connection
     * @param adminUsers usernames of users who will be connection admins on the connection
     * @return the builder for the base configuration of a Power BI crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static PowerBICrawlerBuilder<?, ?> creator(
            AtlanClient client,
            String connectionName,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers)
            throws AtlanException {
        Connection connection = getConnection(
                client,
                connectionName,
                AtlanConnectorType.POWERBI,
                adminRoles,
                adminGroups,
                adminUsers,
                false,
                false,
                0L,
                "https://powerbi.microsoft.com/pictures/application-logos/svg/powerbi.svg");
        return _internal()
                .client(client)
                .connection(connection)
                .metadata()
                .include((List<String>) null)
                .exclude((List<String>) null)
                .directEndorsements(true);
    }

    public abstract static class PowerBICrawlerBuilder<C extends PowerBICrawler, B extends PowerBICrawlerBuilder<C, B>>
            extends AbstractCrawlerBuilder<C, B> {

        /**
         * Set up the crawler to extract directly from Power BI.
         *
         * @return the builder, set up to extract directly from Power BI
         */
        public PowerBICrawlerBuilder<C, B> direct() {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            localCreds
                    .name("default-powerbi-" + epoch + "-0")
                    .host("api.powerbi.com")
                    .port(443)
                    .connectorConfigName("atlan-connectors-powerbi");
            return this.parameters(params()).credential(localCreds);
        }

        /**
         * Set up the crawler to use delegated user authentication.
         *
         * @param username through which to access Power BI
         * @param password through which to access Power BI
         * @param tenantId unique ID (GUID) of the tenant for Power BI
         * @param clientId unique ID (GUID) of the client for Power BI
         * @param clientSecret through which to access Power BI
         * @return the builder, set up to use basic authentication
         */
        public PowerBICrawlerBuilder<C, B> delegatedUser(
                String username, String password, String tenantId, String clientId, String clientSecret) {
            localCreds
                    .authType("basic")
                    .username(username)
                    .password(password)
                    .extra("tenantId", tenantId)
                    .extra("clientId", clientId)
                    .extra("clientSecret", clientSecret);
            return this.credential(localCreds);
        }

        /**
         * Set up the crawler to use service principal authentication.
         *
         * @param tenantId unique ID (GUID) of the tenant for Power BI
         * @param clientId unique ID (GUID) of the client for Power BI
         * @param clientSecret through which to access Power BI
         * @return the builder, set up to use basic authentication
         */
        public PowerBICrawlerBuilder<C, B> servicePrincipal(String tenantId, String clientId, String clientSecret) {
            localCreds
                    .authType("service_principal")
                    .connectorType("rest")
                    .extra("tenantId", tenantId)
                    .extra("clientId", clientId)
                    .extra("clientSecret", clientSecret);
            return this.credential(localCreds);
        }

        /**
         * Defines the filter for workspaces to include when crawling.
         *
         * @param workspaces the GUIDs of workspaces to include when crawling
         * @return the builder, set to include only those workspaces specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public PowerBICrawlerBuilder<C, B> include(List<String> workspaces) throws InvalidRequestException {
            Map<String, Map<String, String>> toInclude = buildFlatFilter(workspaces);
            try {
                return this.parameter("include-filter", Serde.allInclusiveMapper.writeValueAsString(toInclude));
            } catch (JsonProcessingException e) {
                throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
            }
        }

        /**
         * Defines the filter for workspaces to exclude when crawling.
         *
         * @param workspaces the GUIDs of workspaces to exclude when crawling
         * @return the builder, set to exclude only those workspaces specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public PowerBICrawlerBuilder<C, B> exclude(List<String> workspaces) throws InvalidRequestException {
            Map<String, Map<String, String>> toExclude = buildFlatFilter(workspaces);
            try {
                return this.parameter("exclude-filter", Serde.allInclusiveMapper.writeValueAsString(toExclude));
            } catch (JsonProcessingException e) {
                throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
            }
        }

        /**
         * Defines a regular expression to use for including dashboards and reports when crawling.
         *
         * @param regex any dashboard and report names that match this regular expression will be included in crawling
         * @return the builder, set to include any assets that match the provided regular expression
         */
        public PowerBICrawlerBuilder<C, B> include(String regex) {
            return this.parameter("dashboard_report_include_regex", regex);
        }

        /**
         * Defines a regular expression to use for excluding dashboards and reports when crawling.
         *
         * @param regex any dashboard and report names that match this regular expression will be excluded from crawling
         * @return the builder, set to exclude any assets that match the provided regular expression
         */
        public PowerBICrawlerBuilder<C, B> exclude(String regex) {
            return this.parameter("dashboard_report_exclude_regex", regex);
        }

        /**
         * Whether to directly attach endorsements as certificates (true), or instead raise these as requests.
         *
         * @param enabled if true, endorsements will be directly set as certificates on assets, otherwise requests will be raised
         * @return the builder, set to directly (or not) set certificates on assets for endorsements
         */
        public PowerBICrawlerBuilder<C, B> directEndorsements(boolean enabled) {
            return this.parameter("endorsement-attach-mode", enabled ? "metastore" : "requests");
        }

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        protected PowerBICrawlerBuilder<C, B> metadata() {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this.prefix(PREFIX)
                    .name("@atlan/tableau")
                    .runName(PREFIX + "-" + epoch)
                    .label("orchestration.atlan.com/certified", "true")
                    .label("orchestration.atlan.com/source", "powerbi")
                    .label("orchestration.atlan.com/sourceCategory", "bi")
                    .label("orchestration.atlan.com/type", "connector")
                    .label("orchestration.atlan.com/verified", "true")
                    .label("package.argoproj.io/installer", "argopm")
                    .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hpowerbi")
                    .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    .label("orchestration.atlan.com/default-powerbi-" + epoch, "true")
                    .label("orchestration.atlan.com/atlan-ui", "true")
                    .annotation("orchestration.atlan.com/allowSchedule", "true")
                    .annotation("orchestration.atlan.com/categories", "powerbi,crawler")
                    .annotation("orchestration.atlan.com/dependentPackage", "")
                    .annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6332245668881")
                    .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    .annotation(
                            "orchestration.atlan.com/icon",
                            "https://powerbi.microsoft.com/pictures/application-logos/svg/powerbi.svg")
                    .annotation(
                            "orchestration.atlan.com/logo",
                            "https://powerbi.microsoft.com/pictures/application-logos/svg/powerbi.svg")
                    .annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/powerbi")
                    .annotation("orchestration.atlan.com/name", "Power BI Assets")
                    .annotation("package.argoproj.io/author", "Atlan")
                    .annotation(
                            "package.argoproj.io/description",
                            "Package to crawl Power BI assets and publish to Atlan for discovery")
                    .annotation(
                            "package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/powerbi")
                    .annotation("package.argoproj.io/keywords", "[\"powerbi\",\"bi\",\"connector\",\"crawler\"]")
                    .annotation("package.argoproj.io/name", "@atlan/powerbi")
                    .annotation("package.argoproj.io/parent", ".")
                    .annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    .annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    .annotation("package.argoproj.io/support", "support@atlan.com")
                    .annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-powerbi-" + epoch);
        }

        private Map<String, String> params() {
            return Map.ofEntries(
                    Map.entry("credential-guid", "{{credentialGuid}}"),
                    Map.entry("connection", connection.toJson(client)),
                    Map.entry("atlas-auth-type", "internal"),
                    Map.entry("publish-mode", "production"));
        }
    }
}
