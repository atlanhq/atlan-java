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
        return _internal()
                .setup(
                        PREFIX,
                        "@atlan/glue",
                        client,
                        getConnection(
                                client,
                                connectionName,
                                AtlanConnectorType.GLUE,
                                adminRoles,
                                adminGroups,
                                adminUsers,
                                allowQuery,
                                allowSamples,
                                rowLimit,
                                "https://atlan-public.s3.eu-west-1.amazonaws.com/atlan/logos/aws-glue.png"))
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
        public B direct(String region) {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            localCreds
                    .name("default-glue-" + epoch + "-0")
                    .extra("region", region)
                    .connectorConfigName("atlan-connectors-glue");
            return this._credential(localCreds);
        }

        /**
         * Set up the crawler to use IAM user-based authentication.
         *
         * @param accessKey through which to access Glue
         * @param secretKey through which to access Glue
         * @return the builder, set up to use IAM user-based authentication
         */
        public B iamUserAuth(String accessKey, String secretKey) {
            localCreds.authType("iam").username(accessKey).password(secretKey);
            return this._credential(localCreds);
        }

        /**
         * Defines the filter for assets to include when crawling.
         *
         * @param assets list of schema names to include when crawling
         * @return the builder, set to include only those assets specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public B include(List<String> assets) throws InvalidRequestException {
            if (assets == null) {
                return this._parameter("include-filter", "{}");
            } else {
                Map<String, Map<String, Map<String, String>>> map = new HashMap<>();
                map.put("AwsDataCatalog", new HashMap<>());
                for (String one : assets) {
                    map.get("AwsDataCatalog").put(one, Collections.emptyMap());
                }
                try {
                    return this._parameter("include-filter", Serde.allInclusiveMapper.writeValueAsString(map));
                } catch (JsonProcessingException e) {
                    throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
                }
            }
        }

        /**
         * Defines the filter for assets to exclude when crawling.
         *
         * @param assets list of schema names to exclude when crawling
         * @return the builder, set to exclude only those assets specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public B exclude(List<String> assets) throws InvalidRequestException {
            if (assets == null) {
                return this._parameter("exclude-filter", "{}");
            } else {
                Map<String, Map<String, Map<String, String>>> map = new HashMap<>();
                map.put("AwsDataCatalog", new HashMap<>());
                for (String one : assets) {
                    map.get("AwsDataCatalog").put(one, Collections.emptyMap());
                }
                try {
                    return this._parameter("exclude-filter", Serde.allInclusiveMapper.writeValueAsString(map));
                } catch (JsonProcessingException e) {
                    throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
                }
            }
        }

        /**
         * Defines a regular expression to use for excluding assets when crawling.
         *
         * @param regex any asset names that match this regular expression will be excluded from crawling
         * @return the builder, set to exclude any assets that match the provided regular expression
         */
        public B exclude(String regex) {
            return this._parameter("exclude-table-regex", regex);
        }

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        @Override
        protected B metadata() {
            return this._label("orchestration.atlan.com/certified", "true")
                    ._label("orchestration.atlan.com/source", "glue")
                    ._label("orchestration.atlan.com/sourceCategory", "lake")
                    ._label("orchestration.atlan.com/type", "connector")
                    ._label("orchestration.atlan.com/verified", "true")
                    ._label("package.argoproj.io/installer", "argopm")
                    ._label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hglue")
                    ._label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    ._label("orchestration.atlan.com/default-glue-" + epoch, "true")
                    ._label("orchestration.atlan.com/atlan-ui", "true")
                    ._annotation("orchestration.atlan.com/allowSchedule", "true")
                    ._annotation("orchestration.atlan.com/dependentPackage", "")
                    ._annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6335637665681")
                    ._annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    ._annotation(
                            "orchestration.atlan.com/icon",
                            "https://atlan-public.s3.eu-west-1.amazonaws.com/atlan/logos/aws-glue.png")
                    ._annotation(
                            "orchestration.atlan.com/logo",
                            "https://atlan-public.s3.eu-west-1.amazonaws.com/atlan/logos/aws-glue.png")
                    ._annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/glue")
                    ._annotation("orchestration.atlan.com/name", "Glue Assets")
                    ._annotation("orchestration.atlan.com/usecase", "crawling,auto-classifications")
                    ._annotation("package.argoproj.io/author", "Atlan")
                    ._annotation(
                            "package.argoproj.io/description",
                            "Package to crawl AWS Glue assets and publish to Atlan for discovery.")
                    ._annotation("package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/glue")
                    ._annotation(
                            "package.argoproj.io/keywords",
                            "[\"lake\",\"connector\",\"crawler\",\"glue\",\"aws\",\"s3\"]")
                    ._annotation("package.argoproj.io/name", "@atlan/glue")
                    ._annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    ._annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    ._annotation("package.argoproj.io/support", "support@atlan.com")
                    ._annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-glue-" + epoch)
                    ._parameters(Map.ofEntries(
                            Map.entry("credentials-fetch-strategy", "credential_guid"),
                            Map.entry("credential-guid", "{{credentialGuid}}"),
                            Map.entry("connection", connection.toJson(client)),
                            Map.entry("publish-mode", "production"),
                            Map.entry("atlas-auth-type", "internal")));
        }
    }
}
