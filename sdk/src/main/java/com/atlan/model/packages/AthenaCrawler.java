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
public class AthenaCrawler extends AbstractCrawler {

    public static final String PREFIX = AtlanPackageType.ATHENA.getValue();

    /**
     * Create the base configuration for a new Athena crawler.
     * Sets all admins as connection admins, allows querying and sample data previews, and a maximum
     * limit of 10,000 rows from queries.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @return the builder for the base configuration of an Athena crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static AthenaCrawlerBuilder<?, ?> creator(AtlanClient client, String connectionName) throws AtlanException {
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
     * Create the base configuration for a new Athena crawler.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @param adminRoles unique identifiers (GUIDs) of roles who will be connection admins on the connection
     * @param adminGroups internal names of groups who will be connection admins on the connection
     * @param adminUsers usernames of users who will be connection admins on the connection
     * @param allowQuery if true, allow SQL queries against assets in the connection
     * @param allowSamples if true, allow sample data previews for assets in the connection
     * @param rowLimit maximum number of rows that can be returned by a SQL query for all assets in the connection
     * @return the builder for the base configuration of an Athena crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static AthenaCrawlerBuilder<?, ?> creator(
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
                        "@atlan/athena",
                        client,
                        getConnection(
                                client,
                                connectionName,
                                AtlanConnectorType.ATHENA,
                                adminRoles,
                                adminGroups,
                                adminUsers,
                                allowQuery,
                                allowSamples,
                                rowLimit,
                                "https://atlan-public.s3.eu-west-1.amazonaws.com/atlan/logos/aws-athena.png"))
                .include(null)
                .exclude((Map<String, List<String>>) null);
    }

    public abstract static class AthenaCrawlerBuilder<C extends AthenaCrawler, B extends AthenaCrawlerBuilder<C, B>>
            extends AbstractCrawlerBuilder<C, B> {

        /**
         * Set up the crawler to extract directly from Athena.
         *
         * @param hostname of Athena
         * @param workgroup in Athena
         * @param s3Output location in S3 where Athena can store query results (s3://bucket/prefix)
         * @return the builder, set up to extract directly from Athena
         */
        public B direct(String hostname, String workgroup, String s3Output) {
            localCreds
                    .name("default-athena-" + epoch + "-0")
                    .host(hostname)
                    .port(443)
                    .extra("workgroup", workgroup)
                    .extra("s3_output_location", s3Output)
                    .connectorConfigName("atlan-connectors-athena");
            return this._credential(localCreds);
        }

        /**
         * Set up the crawler to use IAM user-based authentication.
         *
         * @param accessKey through which to access Athena
         * @param secretKey through which to access Athena
         * @return the builder, set up to use IAM user-based authentication
         */
        public B iamUserAuth(String accessKey, String secretKey) {
            localCreds.authType("basic").username(accessKey).password(secretKey);
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
                    ._label("orchestration.atlan.com/source", "athena")
                    ._label("orchestration.atlan.com/sourceCategory", "queryengine")
                    ._label("orchestration.atlan.com/type", "connector")
                    ._label("orchestration.atlan.com/verified", "true")
                    ._label("package.argoproj.io/installer", "argopm")
                    ._label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hathena")
                    ._label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    ._label("orchestration.atlan.com/default-athena-" + epoch, "true")
                    ._label("orchestration.atlan.com/atlan-ui", "true")
                    ._annotation("orchestration.atlan.com/allowSchedule", "true")
                    // .annotation("orchestration.atlan.com/categories", "warehouse,crawler")
                    ._annotation("orchestration.atlan.com/dependentPackage", "")
                    ._annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6325285989009")
                    ._annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    ._annotation(
                            "orchestration.atlan.com/icon",
                            "https://atlan-public.s3.eu-west-1.amazonaws.com/atlan/logos/aws-athena.png")
                    ._annotation(
                            "orchestration.atlan.com/logo",
                            "https://atlan-public.s3.eu-west-1.amazonaws.com/atlan/logos/aws-athena.png")
                    ._annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/athena")
                    ._annotation("orchestration.atlan.com/name", "Athena Assets")
                    ._annotation("orchestration.atlan.com/usecase", "crawling,auto-classifications")
                    ._annotation("package.argoproj.io/author", "Atlan")
                    ._annotation("package.argoproj.io/description", "Scan all your Athena assets and publish to Atlan.")
                    ._annotation(
                            "package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/athena")
                    ._annotation(
                            "package.argoproj.io/keywords",
                            "[\"athena\",\"lake\",\"connector\",\"crawler\",\"glue\",\"aws\",\"s3\"]")
                    ._annotation("package.argoproj.io/name", "@atlan/athena")
                    ._annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    ._annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    ._annotation("package.argoproj.io/support", "support@atlan.com")
                    ._annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-athena-" + epoch)
                    ._parameters(Map.ofEntries(
                            Map.entry("credentials-fetch-strategy", "credential_guid"),
                            Map.entry("credential-guid", "{{credentialGuid}}"),
                            Map.entry("connection", connection.toJson(client)),
                            Map.entry("publish-mode", "production"),
                            Map.entry("atlas-auth-type", "internal")));
        }
    }
}
