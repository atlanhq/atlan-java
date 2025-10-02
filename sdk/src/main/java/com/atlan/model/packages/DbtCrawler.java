/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
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

    /**
     * Create the base configuration for a new dbt crawler. Sets all admins as connection admins.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @return the builder for the base configuration of a dbt crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static DbtCrawlerBuilder<?, ?> creator(AtlanClient client, String connectionName) throws AtlanException {
        return creator(client, connectionName, List.of(client.getRoleCache().getIdForSid("$admin")), null, null);
    }

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
        return _internal()
                .setup(
                        PREFIX,
                        "@atlan/dbt",
                        client,
                        getConnection(
                                client,
                                connectionName,
                                AtlanConnectorType.DBT,
                                adminRoles,
                                adminGroups,
                                adminUsers,
                                false,
                                false,
                                0L,
                                "https://assets.atlan.com/assets/dbt-new.svg"))
                .enrichMaterializedAssets(false)
                .tags(false)
                .include(null)
                .exclude(null);
    }

    public abstract static class DbtCrawlerBuilder<C extends DbtCrawler, B extends DbtCrawlerBuilder<C, B>>
            extends AbstractCrawlerBuilder<C, B> {

        /**
         * Set up the crawler to extract using dbt Cloud (and its default hostname).
         *
         * @param serviceToken token to use to authenticate against dbt
         * @param multiTenant if true, use a multi-tenant cloud config, otherwise a single-tenant cloud config
         * @return the builder, set up to extract using dbt Cloud
         */
        public B cloud(String serviceToken, boolean multiTenant) {
            return cloud("https://cloud.getdbt.com", serviceToken, multiTenant);
        }

        /**
         * Set up the crawler to extract using dbt Cloud.
         *
         * @param hostname of dbt (usually https://cloud.getdbt.com)
         * @param serviceToken token to use to authenticate against dbt
         * @param multiTenant if true, use a multi-tenant cloud config, otherwise a single-tenant cloud config
         * @return the builder, set up to extract using dbt Cloud
         */
        public B cloud(String hostname, String serviceToken, boolean multiTenant) {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            localCreds
                    .name("default-dbt-" + epoch + "-1")
                    .host(hostname)
                    .port(443)
                    .authType("token")
                    .username("")
                    .password(serviceToken)
                    .connectorConfigName("atlan-connectors-dbt");
            this._parameter("extraction-method", "api")._credential(localCreds);
            return multiTenant
                    ? this._parameter("deployment-type", "multi")
                    : this._parameter("deployment-type", "single");
        }

        /**
         * Set up the crawler to extract using dbt Core files in S3.
         *
         * @param s3Bucket S3 bucket containing the dbt Core files
         * @param s3Prefix prefix within the S3 bucket where the dbt Core files are located
         * @param s3Region S3 region where the bucket is located
         * @return the builder, set up to extract using dbt Core files in S3
         */
        public B core(String s3Bucket, String s3Prefix, String s3Region) {
            return this._parameter("extraction-method", "core")
                    ._parameter("deployment-type", "single")
                    ._parameter("core-extraction-s3-bucket", s3Bucket)
                    ._parameter("core-extraction-s3-prefix", s3Prefix)
                    ._parameter("core-extraction-s3-region", s3Region);
        }

        /**
         * Whether to enable the enrichment of materialized SQL assets as part of crawling dbt.
         *
         * @param enabled if true, any assets that dbt materializes will also be enriched with details from dbt
         * @return the builder, set up to include or exclude enrichment of materialized assets
         */
        public B enrichMaterializedAssets(boolean enabled) {
            return this._parameter("enrich-materialised-sql-assets", "" + enabled);
        }

        /**
         * Whether to enable dbt tag syncing as part of crawling dbt.
         *
         * @param include if true, tags in dbt will be included while crawling dbt
         * @return the builder, set to include or exclude dbt tags
         */
        public B tags(boolean include) {
            return this._parameter("enable-dbt-tagsync", "" + include);
        }

        /**
         * Limit the crawling to a single connection's assets. (If not specified, crawling will be attempted across
         * all connection's assets.)
         *
         * @param connectionQualifiedName unique name of the connection for whose assets to limit crawling
         * @return the builder, set to limit crawling to only those assets in the specified connection
         */
        public B limitToConnection(String connectionQualifiedName) {
            return this._parameter("connection-qualified-name", connectionQualifiedName);
        }

        /**
         * Defines the filter for assets to include when crawling.
         *
         * @param filter for dbt Core provide a wildcard expression and for dbt Cloud provide a string-encoded map
         * @return the builder, set to include only those assets specified
         */
        public B include(String filter) {
            if (filter == null || filter.isEmpty()) {
                return this._parameter("include-filter", "{}")._parameter("include-filter-core", "*");
            } else {
                return this._parameter("include-filter", filter)._parameter("include-filter-core", filter);
            }
        }

        /**
         * Defines the filter for assets to exclude when crawling.
         *
         * @param filter for dbt Core provide a wildcard expression and for dbt Cloud provide a string-encoded map
         * @return the builder, set to exclude only those assets specified
         */
        public B exclude(String filter) {
            if (filter == null || filter.isEmpty()) {
                return this._parameter("exclude-filter", "{}")._parameter("exclude-filter-core", "*");
            } else {
                return this._parameter("exclude-filter", filter)._parameter("exclude-filter-core", filter);
            }
        }

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        @Override
        protected B metadata() {
            return this._label("orchestration.atlan.com/certified", "true")
                    ._label("orchestration.atlan.com/source", "dbt")
                    ._label("orchestration.atlan.com/sourceCategory", "elt")
                    ._label("orchestration.atlan.com/type", "connector")
                    ._label("orchestration.atlan.com/verified", "true")
                    ._label("package.argoproj.io/installer", "argopm")
                    ._label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hdbt")
                    ._label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    ._label("orchestration.atlan.com/default-dbt-" + epoch, "true")
                    ._label("orchestration.atlan.com/atlan-ui", "true")
                    ._annotation("orchestration.atlan.com/allowSchedule", "true")
                    ._annotation("orchestration.atlan.com/dependentPackage", "")
                    ._annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6335824578705")
                    ._annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    ._annotation("orchestration.atlan.com/icon", "https://assets.atlan.com/assets/dbt-new.svg")
                    ._annotation("orchestration.atlan.com/logo", "https://assets.atlan.com/assets/dbt-new.svg")
                    ._annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/dbt")
                    ._annotation("orchestration.atlan.com/name", "dbt Assets")
                    ._annotation("orchestration.atlan.com/usecase", "crawling")
                    ._annotation("package.argoproj.io/author", "Atlan")
                    ._annotation(
                            "package.argoproj.io/description",
                            "Package to crawl dbt Assets and publish to Atlan for discovery.")
                    ._annotation("package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/dbt")
                    ._annotation("package.argoproj.io/keywords", "[\"connector\",\"crawler\",\"dbt\"]")
                    ._annotation("package.argoproj.io/name", "@atlan/dbt")
                    ._annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    ._annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    ._annotation("package.argoproj.io/support", "support@atlan.com")
                    ._annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-dbt-" + epoch)
                    ._parameters(Map.ofEntries(
                            Map.entry("api-credential-guid", "{{credentialGuid}}"),
                            Map.entry("control-config-strategy", "default"),
                            Map.entry("connection", connection.toJson(client))));
        }
    }
}
