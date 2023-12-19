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
import java.util.HashMap;
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
public class PostgreSQLCrawler extends AbstractCrawler {

    public static final String PREFIX = AtlanPackageType.POSTGRES.getValue();

    /** Connectivity to the Atlan tenant where the package will run. */
    AtlanClient client;

    /** Connection through which the package will manage its assets. */
    Connection connection;

    /**
     * Create the base configuration for a new PostgreSQL crawler.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @param adminRoles unique identifiers (GUIDs) of roles who will be connection admins on the connection
     * @param adminGroups internal names of groups who will be connection admins on the connection
     * @param adminUsers usernames of users who will be connection admins on the connection
     * @param allowQuery if true, allow SQL queries against assets in the connection
     * @param allowSamples if true, allow sample data previews for assets in the connection
     * @param rowLimit maximum number of rows that can be returned by a SQL query for all assets in the connection
     * @return the builder for the base configuration of a PostgreSQL crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static PostgreSQLCrawlerBuilder<?, ?> creator(
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
                AtlanConnectorType.POSTGRES,
                adminRoles,
                adminGroups,
                adminUsers,
                allowQuery,
                allowSamples,
                rowLimit,
                "https://www.postgresql.org/media/img/about/press/elephant.png");
        return _internal()
                .client(client)
                .connection(connection)
                .metadata()
                .include(null)
                .exclude((Map<String, List<String>>) null);
    }

    public abstract static class PostgreSQLCrawlerBuilder<
                    C extends PostgreSQLCrawler, B extends PostgreSQLCrawlerBuilder<C, B>>
            extends AbstractCrawlerBuilder<C, B> {

        private final Map<String, String> extras = new HashMap<>();

        /**
         * Set up the crawler to extract directly from PostgreSQL.
         *
         * @param hostname of PostgreSQL
         * @param database to extract
         * @return the builder, set up to extract directly from Redshift
         */
        public PostgreSQLCrawlerBuilder<C, B> direct(String hostname, String database, boolean serverless) {
            extras.put("database", database);
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this.parameters(params())
                    .parameter("extraction-method", "direct")
                    .credential("name", "default-postgres-" + epoch + "-0")
                    .credential("host", hostname)
                    .credential("port", 5432)
                    .credential("extra", extras)
                    .credential("connectorConfigName", "atlan-connectors-postgres");
        }

        /**
         * Set up the crawler to use basic authentication.
         *
         * @param username through which to access PostgreSQL
         * @param password through which to access PostgreSQL
         * @return the builder, set up to use basic authentication
         */
        public PostgreSQLCrawlerBuilder<C, B> basicAuth(String username, String password) {
            return this.credential("authType", "basic")
                    .credential("username", username)
                    .credential("password", password);
        }

        /**
         * Set up the crawler to use IAM user-based authentication.
         *
         * @param username for the IAM user
         * @param accessKey through which to access PostgreSQL
         * @param secretKey through which to access PostgreSQL
         * @return the builder, set up to use IAM user-based authentication
         */
        public PostgreSQLCrawlerBuilder<C, B> iamUserAuth(String username, String accessKey, String secretKey) {
            extras.put("dbuser", username);
            return this.credential("authType", "iam")
                    .credential("username", accessKey)
                    .credential("password", secretKey)
                    .credential("extra", extras);
        }

        /**
         * Defines the filter for assets to include when crawling.
         *
         * @param assets map keyed by database name with each value being a list of schemas
         * @return the builder, set to include only those assets specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public PostgreSQLCrawlerBuilder<C, B> include(Map<String, List<String>> assets) throws InvalidRequestException {
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
        public PostgreSQLCrawlerBuilder<C, B> exclude(Map<String, List<String>> assets) throws InvalidRequestException {
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
        public PostgreSQLCrawlerBuilder<C, B> exclude(String regex) {
            return this.parameter("temp-table-regex", regex);
        }

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        protected PostgreSQLCrawlerBuilder<C, B> metadata() {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this.prefix(PREFIX)
                    .name("@atlan/postgres")
                    .runName(PREFIX + "-" + epoch)
                    .label("orchestration.atlan.com/certified", "true")
                    .label("orchestration.atlan.com/source", "postgres")
                    .label("orchestration.atlan.com/sourceCategory", "database")
                    .label("orchestration.atlan.com/type", "connector")
                    .label("orchestration.atlan.com/verified", "true")
                    .label("package.argoproj.io/installer", "argopm")
                    .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hpostgres")
                    .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    .label("orchestration.atlan.com/default-postgres-" + epoch, "true")
                    .label("orchestration.atlan.com/atlan-ui", "true")
                    .annotation("orchestration.atlan.com/allowSchedule", "true")
                    .annotation("orchestration.atlan.com/categories", "postgres,crawler")
                    .annotation("orchestration.atlan.com/dependentPackage", "")
                    .annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6329557275793")
                    .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    .annotation(
                            "orchestration.atlan.com/icon",
                            "https://www.postgresql.org/media/img/about/press/elephant.png")
                    .annotation(
                            "orchestration.atlan.com/logo",
                            "https://www.postgresql.org/media/img/about/press/elephant.png")
                    .annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/postgres")
                    .annotation("orchestration.atlan.com/name", "Postgres Assets")
                    .annotation("package.argoproj.io/author", "Atlan")
                    .annotation(
                            "package.argoproj.io/description",
                            "Package to crawl PostgreSQL assets and publish to Atlan for discovery")
                    .annotation(
                            "package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/postgres")
                    .annotation(
                            "package.argoproj.io/keywords",
                            "[\"postgres\",\"database\",\"sql\",\"connector\",\"crawler\"]")
                    .annotation("package.argoproj.io/name", "@atlan/postgres")
                    .annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    .annotation("package.argoproj.io/support", "support@atlan.com")
                    .annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-postgres-" + epoch);
        }

        private Map<String, String> params() {
            return Map.ofEntries(
                    Map.entry("credential-guid", "{{credentialGuid}}"),
                    Map.entry("connection", connection.toJson(client)),
                    Map.entry("publish-mode", "production"));
        }
    }
}
