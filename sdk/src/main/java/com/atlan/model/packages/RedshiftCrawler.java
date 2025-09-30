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
public class RedshiftCrawler extends AbstractCrawler {

    public static final String PREFIX = AtlanPackageType.REDSHIFT.getValue();

    /**
     * Create the base configuration for a new Redshift crawler.
     * Sets all admins as connection admins, allows querying and sample data previews, and a maximum
     * limit of 10,000 rows from queries.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @return the builder for the base configuration of a Redshift crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static RedshiftCrawlerBuilder<?, ?> creator(AtlanClient client, String connectionName)
            throws AtlanException {
        return creator(
                client,
                connectionName,
                List.of(client.getRoleCache().getIdForSid("$admin")),
                null,
                null,
                true,
                true,
                10000L);
    }

    /**
     * Create the base configuration for a new Redshift crawler.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @param adminRoles unique identifiers (GUIDs) of roles who will be connection admins on the connection
     * @param adminGroups internal names of groups who will be connection admins on the connection
     * @param adminUsers usernames of users who will be connection admins on the connection
     * @param allowQuery if true, allow SQL queries against assets in the connection
     * @param allowSamples if true, allow sample data previews for assets in the connection
     * @param rowLimit maximum number of rows that can be returned by a SQL query for all assets in the connection
     * @return the builder for the base configuration of a Redshift crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static RedshiftCrawlerBuilder<?, ?> creator(
            AtlanClient client,
            String connectionName,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers,
            boolean allowQuery,
            boolean allowSamples,
            long rowLimit)
            throws AtlanException {
        return _internal()
                .setup(
                        PREFIX,
                        "@atlan/redshift",
                        client,
                        getConnection(
                                client,
                                connectionName,
                                AtlanConnectorType.REDSHIFT,
                                adminRoles,
                                adminGroups,
                                adminUsers,
                                allowQuery,
                                allowSamples,
                                rowLimit,
                                "https://cdn.worldvectorlogo.com/logos/aws-redshift-logo.svg"))
                .include(null)
                .exclude((Map<String, List<String>>) null);
    }

    public abstract static class RedshiftCrawlerBuilder<
                    C extends RedshiftCrawler, B extends RedshiftCrawlerBuilder<C, B>>
            extends AbstractCrawlerBuilder<C, B> {

        /**
         * Set up the crawler to extract directly from Redshift.
         *
         * @param hostname of Redshift
         * @param database to extract
         * @param serverless if true, use a serverless configuration otherwise use a provisioned configuration
         * @return the builder, set up to extract directly from Redshift
         */
        public B direct(String hostname, String database, boolean serverless) {
            localCreds
                    .name("default-redshift-" + epoch + "-0")
                    .host(hostname)
                    .port(5439)
                    .extra("database", database)
                    .extra("deployment_type", serverless ? "serverless" : "provisioned")
                    .connectorConfigName("atlan-connectors-redshift");
            return this._credential(localCreds);
        }

        /**
         * Set up the crawler to use basic authentication.
         *
         * @param username through which to access Redshift
         * @param password through which to access Redshift
         * @return the builder, set up to use basic authentication
         */
        public B basicAuth(String username, String password) {
            localCreds.authType("basic").username(username).password(password);
            return this._credential(localCreds);
        }

        /**
         * Set up the crawler to use IAM user-based authentication.
         *
         * @param username for the IAM user
         * @param accessKey through which to access Redshift
         * @param secretKey through which to access Redshift
         * @return the builder, set up to use IAM user-based authentication
         */
        public B iamUserAuth(String username, String accessKey, String secretKey) {
            localCreds.authType("iam").username(accessKey).password(secretKey).extra("dbuser", username);
            return this._credential(localCreds);
        }

        /**
         * Whether to search for lineage across all available connections on Atlan (true) or only the selected connection.
         *
         * @param enabled if true, searches for lineage across all available connections in Atlan
         * @return the builder, set up to consider cross-connection lineage (or not)
         */
        public B crossConnection(boolean enabled) {
            return this._parameter("advanced-config", "custom")._parameter("cross-connection", "" + enabled);
        }

        /**
         * Defines the filter for assets to include when crawling.
         *
         * @param assets map keyed by database name with each value being a list of schemas
         * @return the builder, set to include only those assets specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public B include(Map<String, List<String>> assets) throws InvalidRequestException {
            Map<String, List<String>> toInclude = buildHierarchicalFilter(assets);
            try {
                return this._parameter("include-filter", Serde.allInclusiveMapper.writeValueAsString(toInclude));
            } catch (JsonProcessingException e) {
                throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
            }
        }

        /**
         * Defines the filter for assets to exclude when crawling.
         *
         * @param assets map keyed by database name with each value being a list of schemas
         * @return the builder, set to exclude only those assets specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public B exclude(Map<String, List<String>> assets) throws InvalidRequestException {
            Map<String, List<String>> toExclude = buildHierarchicalFilter(assets);
            try {
                return this._parameter("exclude-filter", Serde.allInclusiveMapper.writeValueAsString(toExclude));
            } catch (JsonProcessingException e) {
                throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
            }
        }

        /**
         * Defines a regular expression to use for excluding assets when crawling.
         *
         * @param regex any asset names that match this regular expression will be excluded from crawling
         * @return the builder, set to exclude any assets that match the provided regular expression
         */
        public B exclude(String regex) {
            return this._parameter("temp-table-regex", regex);
        }

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        @Override
        protected B metadata() {
            return this._label("orchestration.atlan.com/certified", "true")
                    ._label("orchestration.atlan.com/source", "redshift")
                    ._label("orchestration.atlan.com/sourceCategory", "warehouse")
                    ._label("orchestration.atlan.com/type", "connector")
                    ._label("orchestration.atlan.com/verified", "true")
                    ._label("package.argoproj.io/installer", "argopm")
                    ._label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hredshift")
                    ._label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    ._label("orchestration.atlan.com/default-redshift-" + epoch, "true")
                    ._label("orchestration.atlan.com/atlan-ui", "true")
                    ._annotation("orchestration.atlan.com/allowSchedule", "true")
                    ._annotation("orchestration.atlan.com/categories", "warehouse,crawler")
                    ._annotation("orchestration.atlan.com/dependentPackage", "")
                    ._annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6326396122641")
                    ._annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    ._annotation(
                            "orchestration.atlan.com/icon",
                            "https://cdn.worldvectorlogo.com/logos/aws-redshift-logo.svg")
                    ._annotation(
                            "orchestration.atlan.com/logo",
                            "https://cdn.worldvectorlogo.com/logos/aws-redshift-logo.svg")
                    ._annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/redshift")
                    ._annotation("orchestration.atlan.com/name", "Redshift Assets")
                    ._annotation("package.argoproj.io/author", "Atlan")
                    ._annotation(
                            "package.argoproj.io/description",
                            "Package to crawl AWS Redshift assets and publish to Atlan for discovery")
                    ._annotation(
                            "package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/redshift")
                    ._annotation(
                            "package.argoproj.io/keywords", "[\"redshift\",\"warehouse\",\"connector\",\"crawler\"]")
                    ._annotation("package.argoproj.io/name", "@atlan/redshift")
                    ._annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    ._annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    ._annotation("package.argoproj.io/support", "support@atlan.com")
                    ._annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-redshift-" + epoch)
                    ._parameters(Map.ofEntries(
                            Map.entry("credentials-fetch-strategy", "credential_guid"),
                            Map.entry("credential-guid", "{{credentialGuid}}"),
                            Map.entry("control-config-strategy", "default"),
                            Map.entry("connection", connection.toJson(client)),
                            Map.entry("publish-mode", "production"),
                            Map.entry("atlas-auth-type", "internal")));
        }
    }
}
