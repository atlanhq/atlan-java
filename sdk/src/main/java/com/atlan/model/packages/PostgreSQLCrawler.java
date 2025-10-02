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
public class PostgreSQLCrawler extends AbstractCrawler {

    public static final String PREFIX = AtlanPackageType.POSTGRES.getValue();

    /**
     * Create the base configuration for a new PostgreSQL crawler.
     * Sets all admins as connection admins, allows querying and sample data previews, and a maximum
     * limit of 10,000 rows from queries.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @return the builder for the base configuration of a PostgreSQL crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static PostgreSQLCrawlerBuilder<?, ?> creator(AtlanClient client, String connectionName)
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
        return _internal()
                .setup(
                        PREFIX,
                        "@atlan/postgres",
                        client,
                        getConnection(
                                client,
                                connectionName,
                                AtlanConnectorType.POSTGRES,
                                adminRoles,
                                adminGroups,
                                adminUsers,
                                allowQuery,
                                allowSamples,
                                rowLimit,
                                "https://www.postgresql.org/media/img/about/press/elephant.png"))
                .metadata()
                .include(null)
                .exclude((Map<String, List<String>>) null);
    }

    public abstract static class PostgreSQLCrawlerBuilder<
                    C extends PostgreSQLCrawler, B extends PostgreSQLCrawlerBuilder<C, B>>
            extends AbstractCrawlerBuilder<C, B> {

        /**
         * Set up the crawler to extract directly from PostgreSQL.
         *
         * @param hostname of PostgreSQL
         * @param database to extract
         * @return the builder, set up to extract directly from Redshift
         */
        public B direct(String hostname, String database) {
            localCreds
                    .name("default-postgres-" + epoch + "-0")
                    .host(hostname)
                    .port(5432)
                    .extra("database", database)
                    .connectorConfigName("atlan-connectors-postgres");
            return this._parameter("extraction-method", "direct")._credential(localCreds);
        }

        /**
         * Set up the crawler to use basic authentication.
         *
         * @param username through which to access PostgreSQL
         * @param password through which to access PostgreSQL
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
         * @param accessKey through which to access PostgreSQL
         * @param secretKey through which to access PostgreSQL
         * @return the builder, set up to use IAM user-based authentication
         */
        public B iamUserAuth(String username, String accessKey, String secretKey) {
            localCreds.authType("iam").username(accessKey).password(secretKey).extra("dbuser", username);
            return this._credential(localCreds);
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
                    ._label("orchestration.atlan.com/source", "postgres")
                    ._label("orchestration.atlan.com/sourceCategory", "database")
                    ._label("orchestration.atlan.com/type", "connector")
                    ._label("orchestration.atlan.com/verified", "true")
                    ._label("package.argoproj.io/installer", "argopm")
                    ._label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hpostgres")
                    ._label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    ._label("orchestration.atlan.com/default-postgres-" + epoch, "true")
                    ._label("orchestration.atlan.com/atlan-ui", "true")
                    ._annotation("orchestration.atlan.com/allowSchedule", "true")
                    ._annotation("orchestration.atlan.com/categories", "postgres,crawler")
                    ._annotation("orchestration.atlan.com/dependentPackage", "")
                    ._annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6329557275793")
                    ._annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    ._annotation(
                            "orchestration.atlan.com/icon",
                            "https://www.postgresql.org/media/img/about/press/elephant.png")
                    ._annotation(
                            "orchestration.atlan.com/logo",
                            "https://www.postgresql.org/media/img/about/press/elephant.png")
                    ._annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/postgres")
                    ._annotation("orchestration.atlan.com/name", "Postgres Assets")
                    ._annotation("package.argoproj.io/author", "Atlan")
                    ._annotation(
                            "package.argoproj.io/description",
                            "Package to crawl PostgreSQL assets and publish to Atlan for discovery")
                    ._annotation(
                            "package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/postgres")
                    ._annotation(
                            "package.argoproj.io/keywords",
                            "[\"postgres\",\"database\",\"sql\",\"connector\",\"crawler\"]")
                    ._annotation("package.argoproj.io/name", "@atlan/postgres")
                    ._annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    ._annotation("package.argoproj.io/support", "support@atlan.com")
                    ._annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-postgres-" + epoch)
                    ._parameters(Map.ofEntries(
                            Map.entry("credential-guid", "{{credentialGuid}}"),
                            Map.entry("connection", connection.toJson(client)),
                            Map.entry("publish-mode", "production")));
        }
    }
}
