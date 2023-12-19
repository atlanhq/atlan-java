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
public class SigmaCrawler extends AbstractCrawler {

    public static final String PREFIX = AtlanPackageType.SIGMA.getValue();

    /** Connectivity to the Atlan tenant where the package will run. */
    AtlanClient client;

    /** Connection through which the package will manage its assets. */
    Connection connection;

    /**
     * Create the base configuration for a new Sigma crawler.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @param adminRoles unique identifiers (GUIDs) of roles who will be connection admins on the connection
     * @param adminGroups internal names of groups who will be connection admins on the connection
     * @param adminUsers usernames of users who will be connection admins on the connection
     * @return the builder for the base configuration of a Sigma crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static SigmaCrawlerBuilder<?, ?> creator(
            AtlanClient client,
            String connectionName,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers)
            throws AtlanException {
        Connection connection = getConnection(
                client,
                connectionName,
                AtlanConnectorType.SIGMA,
                adminRoles,
                adminGroups,
                adminUsers,
                false,
                false,
                0L,
                "http://assets.atlan.com/assets/sigma.svg");
        return _internal()
                .client(client)
                .connection(connection)
                .metadata()
                .include(null)
                .exclude(null);
    }

    public abstract static class SigmaCrawlerBuilder<C extends SigmaCrawler, B extends SigmaCrawlerBuilder<C, B>>
            extends AbstractCrawlerBuilder<C, B> {

        /**
         * Set up the crawler to directly extract from Sigma.
         *
         * @param hostname of the Sigma host, for example aws-api.sigmacomputing.com
         * @return the builder, set up to extract directly from Sigma
         */
        public SigmaCrawlerBuilder<C, B> direct(String hostname) {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this.parameters(params())
                    .credential("name", "default-sigma-" + epoch + "-0")
                    .credential("host", hostname)
                    .credential("port", 443)
                    .credential("extra", Collections.emptyMap())
                    .credential("connectorConfigName", "atlan-connectors-sigma");
        }

        /**
         * Set up the crawler to use API token-based authentication.
         *
         * @param clientId through which to access Sigma
         * @param apiToken through which to access Sigma
         * @return the builder, set up to use API token-based authentication
         */
        public SigmaCrawlerBuilder<C, B> apiToken(String clientId, String apiToken) {
            return this.credential("authType", "api_token")
                    .credential("username", clientId)
                    .credential("password", apiToken);
        }

        /**
         * Defines the filter for assets to include when crawling.
         *
         * @param workbooks the GUIDs of workbooks to include when crawling
         * @return the builder, set to include only those workbooks specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public SigmaCrawlerBuilder<C, B> include(List<String> workbooks) throws InvalidRequestException {
            Map<String, Map<String, String>> toInclude = buildFlatFilter(workbooks);
            try {
                return this.parameter("include-filter", Serde.allInclusiveMapper.writeValueAsString(toInclude));
            } catch (JsonProcessingException e) {
                throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
            }
        }

        /**
         * Defines the filter for assets to exclude when crawling.
         *
         * @param workbooks the GUIDs of workbooks to exclude when crawling
         * @return the builder, set to exclude only those workbooks specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public SigmaCrawlerBuilder<C, B> exclude(List<String> workbooks) throws InvalidRequestException {
            Map<String, Map<String, String>> toExclude = buildFlatFilter(workbooks);
            try {
                return this.parameter("exclude-filter", Serde.allInclusiveMapper.writeValueAsString(toExclude));
            } catch (JsonProcessingException e) {
                throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
            }
        }

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        protected SigmaCrawlerBuilder<C, B> metadata() {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this.prefix(PREFIX)
                    .name("@atlan/sigma")
                    .runName(PREFIX + "-" + epoch)
                    .label("orchestration.atlan.com/certified", "true")
                    .label("orchestration.atlan.com/source", "sigma")
                    .label("orchestration.atlan.com/sourceCategory", "bi")
                    .label("orchestration.atlan.com/type", "connector")
                    .label("orchestration.atlan.com/verified", "true")
                    .label("package.argoproj.io/installer", "argopm")
                    .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hsigma")
                    .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    .label("orchestration.atlan.com/default-sigma-" + epoch, "true")
                    .label("orchestration.atlan.com/atlan-ui", "true")
                    .annotation("orchestration.atlan.com/allowSchedule", "true")
                    .annotation("orchestration.atlan.com/categories", "sigma,crawler")
                    .annotation("orchestration.atlan.com/dependentPackage", "")
                    .annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/8731744918813")
                    .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    .annotation("orchestration.atlan.com/icon", "http://assets.atlan.com/assets/sigma.svg")
                    .annotation("orchestration.atlan.com/logo", "http://assets.atlan.com/assets/sigma.svg")
                    .annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/sigma")
                    .annotation("orchestration.atlan.com/name", "Sigma Assets")
                    .annotation("package.argoproj.io/author", "Atlan")
                    .annotation(
                            "package.argoproj.io/description",
                            "Package to crawl Sigma assets and publish to Atlan for discovery")
                    .annotation(
                            "package.argoproj.io/homepage", "https://github.com/atlanhq/marketplace-packages#readme")
                    .annotation("package.argoproj.io/keywords", "[\"sigma\",\"bi\",\"connector\",\"crawler\"]")
                    .annotation("package.argoproj.io/name", "@atlan/sigma")
                    .annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    .annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    .annotation("package.argoproj.io/support", "support@atlan.com")
                    .annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-sigma-" + epoch);
        }

        private Map<String, String> params() {
            return Map.ofEntries(
                    Map.entry("credential-guid", "{{credentialGuid}}"),
                    Map.entry("connection", connection.toJson(client)),
                    Map.entry("atlas-auth-type", "internal"),
                    Map.entry("publish-mode", "production"));
        }
    }
}
