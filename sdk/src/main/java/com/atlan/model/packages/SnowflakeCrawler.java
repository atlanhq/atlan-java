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
public class SnowflakeCrawler extends AbstractCrawler {

    public static final String PREFIX = AtlanPackageType.SNOWFLAKE.getValue();

    /** Connectivity to the Atlan tenant where the package will run. */
    AtlanClient client;

    /** Connection through which the package will manage its assets. */
    Connection connection;

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
        Connection connection = getConnection(
                client,
                connectionName,
                AtlanConnectorType.SNOWFLAKE,
                adminRoles,
                adminGroups,
                adminUsers,
                allowQuery,
                allowSamples,
                rowLimit,
                "https://docs.snowflake.com/en/_images/logo-snowflake-sans-text.png");
        return _internal()
                .client(client)
                .connection(connection)
                .metadata()
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
         * @param hostname of the Snowflake instance
         * @param username through which to access Snowflake
         * @param password through which to access Snowflake
         * @param role name of the role within Snowflake to crawl through
         * @param warehouse name of the warehouse within Snowflake to crawl through
         * @return the builder, set up to use basic authentication
         */
        public SnowflakeCrawlerBuilder<C, B> basicAuth(
                String hostname, String username, String password, String role, String warehouse) {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this.credential("name", "default-snowflake-" + epoch + "-0")
                    .credential("host", hostname)
                    .credential("port", 443)
                    .credential("authType", "basic")
                    .credential("username", username)
                    .credential("password", password)
                    .credential("extra", Map.of("role", role, "warehouse", warehouse))
                    .credential("connectorConfigName", "atlan-connectors-snowflake");
        }

        /**
         * Set up the crawler to use keypair-based authentication.
         *
         * @param hostname of the Snowflake instance
         * @param username through which to access Snowflake
         * @param privateKey encrypted private key to for authenticating with Snowflake
         * @param privateKeyPassword password for the encrypted private key
         * @param role name of the role within Snowflake to crawl through
         * @param warehouse name of the warehouse within Snowflake to crawl through
         * @return the builder, set up to use keypair-based authentication
         */
        public SnowflakeCrawlerBuilder<C, B> keypairAuth(
                String hostname,
                String username,
                String privateKey,
                String privateKeyPassword,
                String role,
                String warehouse) {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this.credential("name", "default-snowflake-" + epoch + "-0")
                    .credential("host", hostname)
                    .credential("port", 443)
                    .credential("authType", "basic")
                    .credential("username", username)
                    .credential("password", privateKey)
                    .credential(
                            "extra",
                            Map.of("role", role, "warehouse", warehouse, "private_key_password", privateKeyPassword))
                    .credential("connectorConfigName", "atlan-connectors-snowflake");
        }

        /**
         * Set the crawler to extract using Snowflake's information schema.
         *
         * @return the builder, set to extract using information schema
         */
        public SnowflakeCrawlerBuilder<C, B> informationSchema() {
            return this.parameters(params()).parameter("extract-strategy", "information-schema");
        }

        /**
         * Set the crawler to extract using Snowflake's account usage database and schema.
         *
         * @param databaseName name of the database to use
         * @param schemaName name of the schema to use
         * @return the builder, set to extract using account usage
         */
        public SnowflakeCrawlerBuilder<C, B> accountUsage(String databaseName, String schemaName) {
            return this.parameters(params())
                    .parameter("extract-strategy", "account-usage")
                    .parameter("account-usage-database-name", databaseName)
                    .parameter("account-usage-schema-name", schemaName);
        }

        /**
         * Whether to enable lineage as part of crawling Snowflake.
         *
         * @param include if true, lineage will be included while crawling Snowflake
         * @return the builder, set to include or exclude lineage
         */
        public SnowflakeCrawlerBuilder<C, B> lineage(boolean include) {
            return this.parameter("enable-lineage", "" + include);
        }

        /**
         * Whether to enable Snowflake tag syncing as part of crawling Snowflake.
         *
         * @param include if true, tags in Snowflake will be included while crawling Snowflake
         * @return the builder, set to include or exclude Snowflake tags
         */
        public SnowflakeCrawlerBuilder<C, B> tags(boolean include) {
            return this.parameter("enable-snowflake-tags", "" + include);
        }

        /**
         * Defines the filter for assets to include when crawling.
         *
         * @param assets map keyed by database name with each value being a list of schemas
         * @return the builder, set to include only those assets specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public SnowflakeCrawlerBuilder<C, B> include(Map<String, List<String>> assets) throws InvalidRequestException {
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
        public SnowflakeCrawlerBuilder<C, B> exclude(Map<String, List<String>> assets) throws InvalidRequestException {
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
        public SnowflakeCrawlerBuilder<C, B> exclude(String regex) {
            return this.parameter("temp-table-regex", regex);
        }

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        protected SnowflakeCrawlerBuilder<C, B> metadata() {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this.prefix(PREFIX)
                    .name("@atlan/snowflake")
                    .runName(PREFIX + "-" + epoch)
                    .label("orchestration.atlan.com/certified", "true")
                    .label("orchestration.atlan.com/source", "snowflake")
                    .label("orchestration.atlan.com/sourceCategory", "warehouse")
                    .label("orchestration.atlan.com/type", "connector")
                    .label("orchestration.atlan.com/verified", "true")
                    .label("package.argoproj.io/installer", "argopm")
                    .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hsnowflake")
                    .label("package.argoproj.io/parent", "")
                    .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    .label("orchestration.atlan.com/default-snowflake-" + epoch, "true")
                    .label("orchestration.atlan.com/atlan-ui", "true")
                    .annotation("orchestration.atlan.com/allowSchedule", "true")
                    .annotation("orchestration.atlan.com/categories", "warehouse,crawler")
                    .annotation("orchestration.atlan.com/dependentPackage", "")
                    .annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6037440864145")
                    .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    .annotation(
                            "orchestration.atlan.com/icon",
                            "https://docs.snowflake.com/en/_images/logo-snowflake-sans-text.png")
                    .annotation(
                            "orchestration.atlan.com/logo",
                            "https://1amiydhcmj36tz3733v94f15-wpengine.netdna-ssl.com/wp-content/themes/snowflake/assets/img/logo-blue.svg")
                    .annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/snowflake")
                    .annotation("orchestration.atlan.com/name", "Snowflake Assets")
                    .annotation("package.argoproj.io/author", "Atlan")
                    .annotation(
                            "package.argoproj.io/description",
                            "Package to crawl snowflake assets and publish to Atlan for discovery")
                    .annotation(
                            "package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/snowflake")
                    .annotation(
                            "package.argoproj.io/keywords", "[\"snowflake\",\"warehouse\",\"connector\",\"crawler\"]")
                    .annotation("package.argoproj.io/name", "@atlan/snowflake")
                    .annotation("package.argoproj.io/parent", ".")
                    .annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    .annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    .annotation("package.argoproj.io/support", "support@atlan.com")
                    .annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-snowflake-" + epoch);
        }

        private Map<String, String> params() {
            return Map.ofEntries(
                    Map.entry("credential-guid", "{{credentialGuid}}"),
                    Map.entry("control-config-strategy", "default"),
                    Map.entry("connection", connection.toJson(client)));
        }
    }
}
