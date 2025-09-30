/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
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

    /**
     * Create the base configuration for a new Power BI crawler. Sets all admins as connection admins.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @return the builder for the base configuration of a Power BI crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static PowerBICrawlerBuilder<?, ?> creator(AtlanClient client, String connectionName) throws AtlanException {
        return creator(client, connectionName, List.of(client.getRoleCache().getIdForSid("$admin")), null, null);
    }

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
        return _internal()
                .setup(
                        PREFIX,
                        "@atlan/tableau",
                        client,
                        getConnection(
                                client,
                                connectionName,
                                AtlanConnectorType.POWERBI,
                                adminRoles,
                                adminGroups,
                                adminUsers,
                                false,
                                false,
                                0L,
                                "https://powerbi.microsoft.com/pictures/application-logos/svg/powerbi.svg"))
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
        public B direct() {
            localCreds
                    .name("default-powerbi-" + epoch + "-0")
                    .host("api.powerbi.com")
                    .port(443)
                    .connectorConfigName("atlan-connectors-powerbi");
            return this._credential(localCreds);
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
        public B delegatedUser(
                String username, String password, String tenantId, String clientId, String clientSecret) {
            localCreds
                    .authType("basic")
                    .username(username)
                    .password(password)
                    .extra("tenantId", tenantId)
                    .extra("clientId", clientId)
                    .extra("clientSecret", clientSecret);
            return this._credential(localCreds);
        }

        /**
         * Set up the crawler to use service principal authentication.
         *
         * @param tenantId unique ID (GUID) of the tenant for Power BI
         * @param clientId unique ID (GUID) of the client for Power BI
         * @param clientSecret through which to access Power BI
         * @return the builder, set up to use basic authentication
         */
        public B servicePrincipal(String tenantId, String clientId, String clientSecret) {
            localCreds
                    .authType("service_principal")
                    .connectorType("rest")
                    .extra("tenantId", tenantId)
                    .extra("clientId", clientId)
                    .extra("clientSecret", clientSecret);
            return this._credential(localCreds);
        }

        /**
         * Defines the filter for workspaces to include when crawling.
         *
         * @param workspaces the GUIDs of workspaces to include when crawling
         * @return the builder, set to include only those workspaces specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public B include(List<String> workspaces) throws InvalidRequestException {
            Map<String, Map<String, String>> toInclude = buildFlatFilter(workspaces);
            try {
                return this._parameter("include-filter", Serde.allInclusiveMapper.writeValueAsString(toInclude));
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
        public B exclude(List<String> workspaces) throws InvalidRequestException {
            Map<String, Map<String, String>> toExclude = buildFlatFilter(workspaces);
            try {
                return this._parameter("exclude-filter", Serde.allInclusiveMapper.writeValueAsString(toExclude));
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
        public B include(String regex) {
            return this._parameter("dashboard_report_include_regex", regex);
        }

        /**
         * Defines a regular expression to use for excluding dashboards and reports when crawling.
         *
         * @param regex any dashboard and report names that match this regular expression will be excluded from crawling
         * @return the builder, set to exclude any assets that match the provided regular expression
         */
        public B exclude(String regex) {
            return this._parameter("dashboard_report_exclude_regex", regex);
        }

        /**
         * Whether to directly attach endorsements as certificates (true), or instead raise these as requests.
         *
         * @param enabled if true, endorsements will be directly set as certificates on assets, otherwise requests will be raised
         * @return the builder, set to directly (or not) set certificates on assets for endorsements
         */
        public B directEndorsements(boolean enabled) {
            return this._parameter("endorsement-attach-mode", enabled ? "metastore" : "requests");
        }

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        @Override
        protected B metadata() {
            return this._label("orchestration.atlan.com/certified", "true")
                    ._label("orchestration.atlan.com/source", "powerbi")
                    ._label("orchestration.atlan.com/sourceCategory", "bi")
                    ._label("orchestration.atlan.com/type", "connector")
                    ._label("orchestration.atlan.com/verified", "true")
                    ._label("package.argoproj.io/installer", "argopm")
                    ._label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hpowerbi")
                    ._label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    ._label("orchestration.atlan.com/default-powerbi-" + epoch, "true")
                    ._label("orchestration.atlan.com/atlan-ui", "true")
                    ._annotation("orchestration.atlan.com/allowSchedule", "true")
                    ._annotation("orchestration.atlan.com/categories", "powerbi,crawler")
                    ._annotation("orchestration.atlan.com/dependentPackage", "")
                    ._annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6332245668881")
                    ._annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    ._annotation(
                            "orchestration.atlan.com/icon",
                            "https://powerbi.microsoft.com/pictures/application-logos/svg/powerbi.svg")
                    ._annotation(
                            "orchestration.atlan.com/logo",
                            "https://powerbi.microsoft.com/pictures/application-logos/svg/powerbi.svg")
                    ._annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/powerbi")
                    ._annotation("orchestration.atlan.com/name", "Power BI Assets")
                    ._annotation("package.argoproj.io/author", "Atlan")
                    ._annotation(
                            "package.argoproj.io/description",
                            "Package to crawl Power BI assets and publish to Atlan for discovery")
                    ._annotation(
                            "package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/powerbi")
                    ._annotation("package.argoproj.io/keywords", "[\"powerbi\",\"bi\",\"connector\",\"crawler\"]")
                    ._annotation("package.argoproj.io/name", "@atlan/powerbi")
                    ._annotation("package.argoproj.io/parent", ".")
                    ._annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    ._annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    ._annotation("package.argoproj.io/support", "support@atlan.com")
                    ._annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-powerbi-" + epoch)
                    ._parameters(Map.ofEntries(
                            Map.entry("credential-guid", "{{credentialGuid}}"),
                            Map.entry("connection", connection.toJson(client)),
                            Map.entry("atlas-auth-type", "internal"),
                            Map.entry("publish-mode", "production")));
        }
    }
}
