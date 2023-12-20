/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.Credential;
import com.atlan.model.assets.Connection;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanPackageType;
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
public class DbtCrawler extends AbstractCrawler {

    public static final String PREFIX = AtlanPackageType.DBT.getValue();

    /** Connectivity to the Atlan tenant where the package will run. */
    AtlanClient client;

    /** Connection through which the package will manage its assets. */
    Connection connection;

    /** Credentials for this connection. */
    Credential.CredentialBuilder<?, ?> localCreds;

    /**
     * Create the base configuration for a new dbt crawler.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @param adminRoles unique identifiers (GUIDs) of roles who will be connection admins on the connection
     * @param adminGroups internal names of groups who will be connection admins on the connection
     * @param adminUsers usernames of users who will be connection admins on the connection
     * @return the builder for the base configuration of a dbt crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static DbtCrawlerBuilder<?, ?> creator(
            AtlanClient client,
            String connectionName,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers)
            throws AtlanException {
        Connection connection = getConnection(
                client,
                connectionName,
                AtlanConnectorType.DBT,
                adminRoles,
                adminGroups,
                adminUsers,
                false,
                false,
                0L,
                "https://assets.atlan.com/assets/dbt-new.svg");
        return _internal()
                .client(client)
                .connection(connection)
                .metadata()
                .enrichMaterializedAssets(false)
                .tags(false)
                .include(null)
                .exclude(null);
    }

    public abstract static class DbtCrawlerBuilder<C extends DbtCrawler, B extends DbtCrawlerBuilder<C, B>>
            extends AbstractCrawlerBuilder<C, B> {

        /**
         * Set up the crawler to extract using dbt Cloud.
         *
         * @param hostname of dbt (usually https://cloud.getdbt.com)
         * @param serviceToken token to use to authenticate against dbt
         * @param multiTenant if true, use a multi-tenant cloud config, otherwise a single-tenant cloud config
         * @return the builder, set up to extract using dbt Cloud
         */
        public DbtCrawlerBuilder<C, B> cloud(String hostname, String serviceToken, boolean multiTenant) {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            localCreds
                    .name("default-dbt-" + epoch + "-1")
                    .host(hostname)
                    .port(443)
                    .authType("token")
                    .username("")
                    .password(serviceToken)
                    .connectorConfigName("atlan-connectors-dbt");
            this.parameters(params()).parameter("extraction-method", "api").credential(localCreds);
            return multiTenant
                    ? this.parameter("deployment-type", "multi")
                    : this.parameter("deployment-type", "single");
        }

        /**
         * Set up the crawler to extract using dbt Core files in S3.
         *
         * @param s3Bucket S3 bucket containing the dbt Core files
         * @param s3Prefix prefix within the S3 bucket where the dbt Core files are located
         * @param s3Region S3 region where the bucket is located
         * @return the builder, set up to extract using dbt Core files in S3
         */
        public DbtCrawlerBuilder<C, B> core(String s3Bucket, String s3Prefix, String s3Region) {
            return this.parameters(params())
                    .parameter("extraction-method", "core")
                    .parameter("deployment-type", "single")
                    .parameter("core-extraction-s3-bucket", s3Bucket)
                    .parameter("core-extraction-s3-prefix", s3Prefix)
                    .parameter("core-extraction-s3-region", s3Region);
        }

        /**
         * Whether to enable the enrichment of materialized SQL assets as part of crawling dbt.
         *
         * @param enabled if true, any assets that dbt materializes will also be enriched with details from dbt
         * @return the builder, set up to include or exclude enrichment of materialized assets
         */
        public DbtCrawlerBuilder<C, B> enrichMaterializedAssets(boolean enabled) {
            return this.parameter("enrich-materialised-sql-assets", "" + enabled);
        }

        /**
         * Whether to enable dbt tag syncing as part of crawling dbt.
         *
         * @param include if true, tags in dbt will be included while crawling dbt
         * @return the builder, set to include or exclude dbt tags
         */
        public DbtCrawlerBuilder<C, B> tags(boolean include) {
            return this.parameter("enable-dbt-tagsync", "" + include);
        }

        /**
         * Limit the crawling to a single connection's assets. (If not specified, crawling will be attempted across
         * all connection's assets.)
         *
         * @param connectionQualifiedName unique name of the connection for whose assets to limit crawling
         * @return the builder, set to limit crawling to only those assets in the specified connection
         */
        public DbtCrawlerBuilder<C, B> limitToConnection(String connectionQualifiedName) {
            return this.parameter("connection-qualified-name", connectionQualifiedName);
        }

        /**
         * Defines the filter for assets to include when crawling.
         *
         * @param filter for dbt Core provide a wildcard expression and for dbt Cloud provide a string-encoded map
         * @return the builder, set to include only those assets specified
         */
        public DbtCrawlerBuilder<C, B> include(String filter) {
            if (filter == null || filter.isEmpty()) {
                this.parameter("include-filter", "{}");
                this.parameter("include-filter-core", "*");
            } else {
                this.parameter("include-filter", filter);
                this.parameter("include-filter-core", filter);
            }
            return this;
        }

        /**
         * Defines the filter for assets to exclude when crawling.
         *
         * @param filter for dbt Core provide a wildcard expression and for dbt Cloud provide a string-encoded map
         * @return the builder, set to exclude only those assets specified
         */
        public DbtCrawlerBuilder<C, B> exclude(String filter) {
            if (filter == null || filter.isEmpty()) {
                this.parameter("exclude-filter", "{}");
                this.parameter("exclude-filter-core", "*");
            } else {
                this.parameter("exclude-filter", filter);
                this.parameter("exclude-filter-core", filter);
            }
            return this;
        }

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        protected DbtCrawlerBuilder<C, B> metadata() {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this.prefix(PREFIX)
                    .name("@atlan/dbt")
                    .runName(PREFIX + "-" + epoch)
                    .label("orchestration.atlan.com/certified", "true")
                    .label("orchestration.atlan.com/source", "dbt")
                    .label("orchestration.atlan.com/sourceCategory", "elt")
                    .label("orchestration.atlan.com/type", "connector")
                    .label("orchestration.atlan.com/verified", "true")
                    .label("package.argoproj.io/installer", "argopm")
                    .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hdbt")
                    .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    .label("orchestration.atlan.com/default-dbt-" + epoch, "true")
                    .label("orchestration.atlan.com/atlan-ui", "true")
                    .annotation("orchestration.atlan.com/allowSchedule", "true")
                    .annotation("orchestration.atlan.com/dependentPackage", "")
                    .annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6335824578705")
                    .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    .annotation("orchestration.atlan.com/icon", "https://assets.atlan.com/assets/dbt-new.svg")
                    .annotation("orchestration.atlan.com/logo", "https://assets.atlan.com/assets/dbt-new.svg")
                    .annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/dbt")
                    .annotation("orchestration.atlan.com/name", "dbt Assets")
                    .annotation("orchestration.atlan.com/usecase", "crawling")
                    .annotation("package.argoproj.io/author", "Atlan")
                    .annotation(
                            "package.argoproj.io/description",
                            "Package to crawl dbt Assets and publish to Atlan for discovery.")
                    .annotation("package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/dbt")
                    .annotation("package.argoproj.io/keywords", "[\"connector\",\"crawler\",\"dbt\"]")
                    .annotation("package.argoproj.io/name", "@atlan/dbt")
                    .annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    .annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    .annotation("package.argoproj.io/support", "support@atlan.com")
                    .annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-dbt-" + epoch);
        }

        private Map<String, String> params() {
            return Map.ofEntries(
                    Map.entry("api-credential-guid", "{{credentialGuid}}"),
                    Map.entry("control-config-strategy", "default"),
                    Map.entry("connection", connection.toJson(client)));
        }
    }
}
