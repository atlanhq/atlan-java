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
import java.util.Collections;
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
public class GlueCrawler extends AbstractCrawler {

    public static final String PREFIX = AtlanPackageType.GLUE.getValue();

    /** Connectivity to the Atlan tenant where the package will run. */
    AtlanClient client;

    /** Connection through which the package will manage its assets. */
    Connection connection;

    /**
     * Create the base configuration for a new Glue crawler.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @param adminRoles unique identifiers (GUIDs) of roles who will be connection admins on the connection
     * @param adminGroups internal names of groups who will be connection admins on the connection
     * @param adminUsers usernames of users who will be connection admins on the connection
     * @param allowQuery if true, allow SQL queries against assets in the connection
     * @param allowSamples if true, allow sample data previews for assets in the connection
     * @param rowLimit maximum number of rows that can be returned by a SQL query for all assets in the connection
     * @return the builder for the base configuration of a Glue crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static GlueCrawlerBuilder<?, ?> creator(
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
                AtlanConnectorType.GLUE,
                adminRoles,
                adminGroups,
                adminUsers,
                allowQuery,
                allowSamples,
                rowLimit,
                "https://atlan-public.s3.eu-west-1.amazonaws.com/atlan/logos/aws-glue.png");
        return _internal()
                .client(client)
                .connection(connection)
                .metadata()
                .include(null)
                .exclude((List<String>) null);
    }

    public abstract static class GlueCrawlerBuilder<C extends GlueCrawler, B extends GlueCrawlerBuilder<C, B>>
            extends AbstractCrawlerBuilder<C, B> {

        /**
         * Set up the crawler to extract directly from Glue.
         *
         * @param region AWS region where Glue is setup
         * @return the builder, set up to extract directly from Glue
         */
        public GlueCrawlerBuilder<C, B> direct(String region) {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this.parameters(params())
                    .credential("name", "default-glue-" + epoch + "-0")
                    .credential("extra", Map.of("region", region))
                    .credential("connectorConfigName", "atlan-connectors-glue");
        }

        /**
         * Set up the crawler to use IAM user-based authentication.
         *
         * @param accessKey through which to access Glue
         * @param secretKey through which to access Glue
         * @return the builder, set up to use IAM user-based authentication
         */
        public GlueCrawlerBuilder<C, B> iamUserAuth(String accessKey, String secretKey) {
            return this.credential("authType", "iam")
                    .credential("username", accessKey)
                    .credential("password", secretKey);
        }

        /**
         * Defines the filter for assets to include when crawling.
         *
         * @param assets list of schema names to include when crawling
         * @return the builder, set to include only those assets specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public GlueCrawlerBuilder<C, B> include(List<String> assets) throws InvalidRequestException {
            if (assets == null) {
                this.parameter("include-filter", "{}");
            } else {
                Map<String, Map<String, Map<String, String>>> map = new HashMap<>();
                map.put("AwsDataCatalog", new HashMap<>());
                for (String one : assets) {
                    map.get("AwsDataCatalog").put(one, Collections.emptyMap());
                }
                try {
                    if (!map.get("AwsDataCatalog").isEmpty()) {
                        this.parameter("include-filter", Serde.allInclusiveMapper.writeValueAsString(map));
                    }
                } catch (JsonProcessingException e) {
                    throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
                }
            }
            return this;
        }

        /**
         * Defines the filter for assets to exclude when crawling.
         *
         * @param assets list of schema names to exclude when crawling
         * @return the builder, set to exclude only those assets specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public GlueCrawlerBuilder<C, B> exclude(List<String> assets) throws InvalidRequestException {
            if (assets == null) {
                this.parameter("exclude-filter", "{}");
            } else {
                Map<String, Map<String, Map<String, String>>> map = new HashMap<>();
                map.put("AwsDataCatalog", new HashMap<>());
                for (String one : assets) {
                    map.get("AwsDataCatalog").put(one, Collections.emptyMap());
                }
                try {
                    if (!map.get("AwsDataCatalog").isEmpty()) {
                        this.parameter("exclude-filter", Serde.allInclusiveMapper.writeValueAsString(map));
                    }
                } catch (JsonProcessingException e) {
                    throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
                }
            }
            return this;
        }

        /**
         * Defines a regular expression to use for excluding assets when crawling.
         *
         * @param regex any asset names that match this regular expression will be excluded from crawling
         * @return the builder, set to exclude any assets that match the provided regular expression
         */
        public GlueCrawlerBuilder<C, B> exclude(String regex) {
            return this.parameter("exclude-table-regex", regex);
        }

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        protected GlueCrawlerBuilder<C, B> metadata() {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this.prefix(PREFIX)
                    .name("@atlan/glue")
                    .runName(PREFIX + "-" + epoch)
                    .label("orchestration.atlan.com/certified", "true")
                    .label("orchestration.atlan.com/source", "glue")
                    .label("orchestration.atlan.com/sourceCategory", "lake")
                    .label("orchestration.atlan.com/type", "connector")
                    .label("orchestration.atlan.com/verified", "true")
                    .label("package.argoproj.io/installer", "argopm")
                    .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hglue")
                    .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    .label("orchestration.atlan.com/default-glue-" + epoch, "true")
                    .label("orchestration.atlan.com/atlan-ui", "true")
                    .annotation("orchestration.atlan.com/allowSchedule", "true")
                    .annotation("orchestration.atlan.com/dependentPackage", "")
                    .annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6335637665681")
                    .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    .annotation(
                            "orchestration.atlan.com/icon",
                            "https://atlan-public.s3.eu-west-1.amazonaws.com/atlan/logos/aws-glue.png")
                    .annotation(
                            "orchestration.atlan.com/logo",
                            "https://atlan-public.s3.eu-west-1.amazonaws.com/atlan/logos/aws-glue.png")
                    .annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/glue")
                    .annotation("orchestration.atlan.com/name", "Glue Assets")
                    .annotation("orchestration.atlan.com/usecase", "crawling,auto-classifications")
                    .annotation("package.argoproj.io/author", "Atlan")
                    .annotation(
                            "package.argoproj.io/description",
                            "Package to crawl AWS Glue assets and publish to Atlan for discovery.")
                    .annotation("package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/glue")
                    .annotation(
                            "package.argoproj.io/keywords",
                            "[\"lake\",\"connector\",\"crawler\",\"glue\",\"aws\",\"s3\"]")
                    .annotation("package.argoproj.io/name", "@atlan/glue")
                    .annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    .annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    .annotation("package.argoproj.io/support", "support@atlan.com")
                    .annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-glue-" + epoch);
        }

        private Map<String, String> params() {
            return Map.ofEntries(
                    Map.entry("credentials-fetch-strategy", "credential_guid"),
                    Map.entry("credential-guid", "{{credentialGuid}}"),
                    Map.entry("connection", connection.toJson(client)),
                    Map.entry("publish-mode", "production"),
                    Map.entry("atlas-auth-type", "internal"));
        }
    }
}
