/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
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
public class SQLServerCrawler extends AbstractCrawler {

    public static final String PREFIX = AtlanPackageType.MSSQL.getValue();

    /** Connectivity to the Atlan tenant where the package will run. */
    AtlanClient client;

    /** Connection through which the package will manage its assets. */
    Connection connection;

    /**
     * Create the base configuration for a new SQL Server crawler.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @param adminRoles unique identifiers (GUIDs) of roles who will be connection admins on the connection
     * @param adminGroups internal names of groups who will be connection admins on the connection
     * @param adminUsers usernames of users who will be connection admins on the connection
     * @param allowQuery if true, allow SQL queries against assets in the connection
     * @param allowSamples if true, allow sample data previews for assets in the connection
     * @param rowLimit maximum number of rows that can be returned by a SQL query for all assets in the connection
     * @return the builder for the base configuration of a SQL Server crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static SQLServerCrawlerBuilder<?, ?> creator(
            AtlanClient client,
            String connectionName,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers,
            boolean allowQuery,
            boolean allowSamples,
            long rowLimit)
            throws AtlanException {
        Connection connection = getConnection(
                client,
                connectionName,
                AtlanConnectorType.MSSQL,
                adminRoles,
                adminGroups,
                adminUsers,
                allowQuery,
                allowSamples,
                rowLimit,
                "https://user-images.githubusercontent.com/4249331/52232852-e2c4f780-28bd-11e9-835d-1e3cf3e43888.png");
        return _internal()
                .client(client)
                .connection(connection)
                .metadata()
                .include(null)
                .exclude((Map<String, List<String>>) null);
    }

    public abstract static class SQLServerCrawlerBuilder<
                    C extends SQLServerCrawler, B extends SQLServerCrawlerBuilder<C, B>>
            extends AbstractCrawlerBuilder<C, B> {

        /**
         * Set up the crawler to extract from SQL Server directly.
         *
         * @param hostname hostname of the SQL Server host
         * @param database database to extract
         * @return the builder, set to extract directly from SQL Server
         */
        public SQLServerCrawlerBuilder<C, B> direct(String hostname, String database) {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this.parameters(params())
                    .parameter("extraction-method", "direct")
                    .credential("name", "default-mssql-" + epoch + "-0")
                    .credential("host", hostname)
                    .credential("port", 1433)
                    .credential("extra", Map.of("database", database))
                    .credential("connectorConfigName", "atlan-connectors-mssql");
        }

        /**
         * Set up the crawler to use basic authentication.
         *
         * @param username through which to access SQL Server
         * @param password through which to access SQL Server
         * @return the builder, set up to use basic authentication
         */
        public SQLServerCrawlerBuilder<C, B> basicAuth(String username, String password) {
            return this.credential("authType", "basic")
                    .credential("username", username)
                    .credential("password", password);
        }

        /**
         * Defines the filter for assets to include when crawling.
         *
         * @param assets map keyed by database name with each value being a list of schemas
         * @return the builder, set to include only those assets specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public SQLServerCrawlerBuilder<C, B> include(Map<String, List<String>> assets) throws InvalidRequestException {
            Map<String, List<String>> toInclude = buildHierarchicalFilter(assets);
            try {
                if (!toInclude.isEmpty()) {
                    this.parameter("include-filter", Serde.allInclusiveMapper.writeValueAsString(toInclude));
                }
            } catch (JsonProcessingException e) {
                throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
            }
            return this;
        }

        /**
         * Defines the filter for assets to exclude when crawling.
         *
         * @param assets map keyed by database name with each value being a list of schemas
         * @return the builder, set to exclude only those assets specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public SQLServerCrawlerBuilder<C, B> exclude(Map<String, List<String>> assets) throws InvalidRequestException {
            Map<String, List<String>> toExclude = buildHierarchicalFilter(assets);
            try {
                if (!toExclude.isEmpty()) {
                    this.parameter("exclude-filter", Serde.allInclusiveMapper.writeValueAsString(toExclude));
                }
            } catch (JsonProcessingException e) {
                throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
            }
            return this;
        }

        /**
         * Defines a regular expression to use for excluding assets when crawling.
         *
         * @param regex any asset names that match this regular expression will be excluded from crawling
         * @return the builder, set to exclude any assets that match the provided regular expression
         */
        public SQLServerCrawlerBuilder<C, B> exclude(String regex) {
            return this.parameter("temp-table-regex", regex);
        }

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        protected SQLServerCrawlerBuilder<C, B> metadata() {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this.prefix(PREFIX)
                    .name("@atlan/mssql")
                    .runName(PREFIX + "-" + epoch)
                    .label("orchestration.atlan.com/certified", "true")
                    .label("orchestration.atlan.com/source", "mssql")
                    .label("orchestration.atlan.com/sourceCategory", "warehouse")
                    .label("orchestration.atlan.com/type", "connector")
                    .label("orchestration.atlan.com/verified", "true")
                    .label("package.argoproj.io/installer", "argopm")
                    .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hmssql")
                    .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    .label("orchestration.atlan.com/default-mssql-" + epoch, "true")
                    .label("orchestration.atlan.com/atlan-ui", "true")
                    .annotation("orchestration.atlan.com/allowSchedule", "true")
                    .annotation("orchestration.atlan.com/categories", "mssql,crawler")
                    .annotation("orchestration.atlan.com/dependentPackage", "")
                    .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    .annotation(
                            "orchestration.atlan.com/icon",
                            "https://user-images.githubusercontent.com/4249331/52232852-e2c4f780-28bd-11e9-835d-1e3cf3e43888.png")
                    .annotation(
                            "orchestration.atlan.com/logo",
                            "https://user-images.githubusercontent.com/4249331/52232852-e2c4f780-28bd-11e9-835d-1e3cf3e43888.png")
                    .annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/mssql")
                    .annotation("orchestration.atlan.com/name", "SQL Server Assets")
                    .annotation("package.argoproj.io/author", "Atlan")
                    .annotation(
                            "package.argoproj.io/description",
                            "Package to crawl Microsoft SQL Server assets and publish to Atlan for discovery")
                    .annotation("package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/mssql")
                    .annotation(
                            "package.argoproj.io/keywords",
                            "[\"mssql\",\"database\",\"sql\",\"connector\",\"crawler\"]")
                    .annotation("package.argoproj.io/name", "@atlan/mssql")
                    .annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    .annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    .annotation("package.argoproj.io/support", "support@atlan.com")
                    .annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-mssql-" + epoch);
        }

        private Map<String, String> params() {
            return Map.ofEntries(
                    Map.entry("credential-guid", "{{credentialGuid}}"),
                    Map.entry("publish-mode", "production"),
                    Map.entry("atlas-auth-type", "internal"),
                    Map.entry("connection", connection.toJson(client)),
                    Map.entry("credentials-fetch-strategy", "credential_guid"));
        }
    }
}
