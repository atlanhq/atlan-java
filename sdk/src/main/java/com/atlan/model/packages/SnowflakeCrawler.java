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
public class SnowflakeCrawler extends AbstractCrawler {

    public static final String PREFIX = AtlanPackageType.SNOWFLAKE.getValue();

    /**
     * Create the base configuration for a new Snowflake crawler.
     * Sets all admins as connection admins, allows querying and sample data previews, and a maximum
     * limit of 10,000 rows from queries.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @return the builder for the base configuration of a Snowflake crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static SnowflakeCrawlerBuilder<?, ?> creator(AtlanClient client, String connectionName)
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
     * Create the base configuration for a new Snowflake crawler.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @param adminRoles unique identifiers (GUIDs) of roles who will be connection admins on the connection
     * @param adminGroups internal names of groups who will be connection admins on the connection
     * @param adminUsers usernames of users who will be connection admins on the connection
     * @param allowQuery if true, allow SQL queries against assets in the connection
     * @param allowSamples if true, allow sample data previews for assets in the connection
     * @param rowLimit maximum number of rows that can be returned by a SQL query for all assets in the connection
     * @return the builder for the base configuration of a Snowflake crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static SnowflakeCrawlerBuilder<?, ?> creator(
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
                        "@atlan/snowflake",
                        client,
                        getConnection(
                                client,
                                connectionName,
                                AtlanConnectorType.SNOWFLAKE,
                                adminRoles,
                                adminGroups,
                                adminUsers,
                                allowQuery,
                                allowSamples,
                                rowLimit,
                                "https://docs.snowflake.com/en/_images/logo-snowflake-sans-text.png"))
                .tags(false)
                .include(null)
                .exclude((Map<String, List<String>>) null);
    }

    public abstract static class SnowflakeCrawlerBuilder<
                    C extends SnowflakeCrawler, B extends SnowflakeCrawlerBuilder<C, B>>
            extends AbstractCrawlerBuilder<C, B> {

        /**
         * Set up the crawler to use basic authentication.
         *
         * @param username through which to access Snowflake
         * @param password through which to access Snowflake
         * @param role name of the role within Snowflake to crawl through
         * @param warehouse name of the warehouse within Snowflake to crawl through
         * @return the builder, set up to use basic authentication
         */
        public B basicAuth(String username, String password, String role, String warehouse) {
            localCreds
                    .port(443)
                    .authType("basic")
                    .username(username)
                    .password(password)
                    .extra("role", role)
                    .extra("warehouse", warehouse);
            return this._credential(localCreds);
        }

        /**
         * Set up the crawler to use keypair-based authentication.
         *
         * @param username through which to access Snowflake
         * @param privateKey encrypted private key to for authenticating with Snowflake
         * @param privateKeyPassword password for the encrypted private key
         * @param role name of the role within Snowflake to crawl through
         * @param warehouse name of the warehouse within Snowflake to crawl through
         * @return the builder, set up to use keypair-based authentication
         */
        public B keypairAuth(
                String username, String privateKey, String privateKeyPassword, String role, String warehouse) {
            localCreds
                    .port(443)
                    .authType("keypair")
                    .username(username)
                    .password(privateKey)
                    .extra("role", role)
                    .extra("warehouse", warehouse)
                    .extra("private_key_password", privateKeyPassword);
            return this._credential(localCreds);
        }

        /**
         * Set the crawler to extract using Snowflake's information schema.
         *
         * @param hostname of the Snowflake instance
         * @return the builder, set to extract using information schema
         */
        public B informationSchema(String hostname) {
            localCreds
                    .host(hostname)
                    .name("default-snowflake-" + epoch + "-0")
                    .connectorConfigName("atlan-connectors-snowflake");
            return this._parameter("extract-strategy", "information-schema")._credential(localCreds);
        }

        /**
         * Set the crawler to extract using Snowflake's account usage database and schema.
         *
         * @param hostname of the Snowflake instance
         * @param databaseName name of the database to use
         * @param schemaName name of the schema to use
         * @return the builder, set to extract using account usage
         */
        public B accountUsage(String hostname, String databaseName, String schemaName) {
            localCreds
                    .host(hostname)
                    .name("default-snowflake-" + epoch + "-0")
                    .connectorConfigName("atlan-connectors-snowflake");
            return this._parameter("extract-strategy", "account-usage")
                    ._parameter("account-usage-database-name", databaseName)
                    ._parameter("account-usage-schema-name", schemaName)
                    ._credential(localCreds);
        }

        /**
         * Whether to enable lineage as part of crawling Snowflake.
         *
         * @param include if true, lineage will be included while crawling Snowflake
         * @return the builder, set to include or exclude lineage
         */
        public B lineage(boolean include) {
            return this._parameter("enable-lineage", "" + include);
        }

        /**
         * Whether to enable Snowflake tag syncing as part of crawling Snowflake.
         *
         * @param include if true, tags in Snowflake will be included while crawling Snowflake
         * @return the builder, set to include or exclude Snowflake tags
         */
        public B tags(boolean include) {
            return this._parameter("enable-snowflake-tags", "" + include);
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
                    ._label("orchestration.atlan.com/source", "snowflake")
                    ._label("orchestration.atlan.com/sourceCategory", "warehouse")
                    ._label("orchestration.atlan.com/type", "connector")
                    ._label("orchestration.atlan.com/verified", "true")
                    ._label("package.argoproj.io/installer", "argopm")
                    ._label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hsnowflake")
                    ._label("package.argoproj.io/parent", "")
                    ._label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    ._label("orchestration.atlan.com/default-snowflake-" + epoch, "true")
                    ._label("orchestration.atlan.com/atlan-ui", "true")
                    ._annotation("orchestration.atlan.com/allowSchedule", "true")
                    ._annotation("orchestration.atlan.com/categories", "warehouse,crawler")
                    ._annotation("orchestration.atlan.com/dependentPackage", "")
                    ._annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6037440864145")
                    ._annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    ._annotation(
                            "orchestration.atlan.com/icon",
                            "https://docs.snowflake.com/en/_images/logo-snowflake-sans-text.png")
                    ._annotation(
                            "orchestration.atlan.com/logo",
                            "https://1amiydhcmj36tz3733v94f15-wpengine.netdna-ssl.com/wp-content/themes/snowflake/assets/img/logo-blue.svg")
                    ._annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/snowflake")
                    ._annotation("orchestration.atlan.com/name", "Snowflake Assets")
                    ._annotation("package.argoproj.io/author", "Atlan")
                    ._annotation(
                            "package.argoproj.io/description",
                            "Package to crawl snowflake assets and publish to Atlan for discovery")
                    ._annotation(
                            "package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/snowflake")
                    ._annotation(
                            "package.argoproj.io/keywords", "[\"snowflake\",\"warehouse\",\"connector\",\"crawler\"]")
                    ._annotation("package.argoproj.io/name", "@atlan/snowflake")
                    ._annotation("package.argoproj.io/parent", ".")
                    ._annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    ._annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    ._annotation("package.argoproj.io/support", "support@atlan.com")
                    ._annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-snowflake-" + epoch)
                    ._parameters(Map.ofEntries(
                            Map.entry("credential-guid", "{{credentialGuid}}"),
                            Map.entry("control-config-strategy", "default"),
                            Map.entry("connection", connection.toJson(client))));
        }
    }
}
