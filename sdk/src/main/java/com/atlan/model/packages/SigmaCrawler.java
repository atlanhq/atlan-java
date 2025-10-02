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
public class SigmaCrawler extends AbstractCrawler {

    public static final String PREFIX = AtlanPackageType.SIGMA.getValue();

    /**
     * Create the base configuration for a new Sigma crawler. Sets all admins as connection admins.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @return the builder for the base configuration of a Sigma crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static SigmaCrawlerBuilder<?, ?> creator(AtlanClient client, String connectionName) throws AtlanException {
        return creator(client, connectionName, List.of(client.getRoleCache().getIdForSid("$admin")), null, null);
    }

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
        return _internal()
                .setup(
                        PREFIX,
                        "@atlan/sigma",
                        client,
                        getConnection(
                                client,
                                connectionName,
                                AtlanConnectorType.SIGMA,
                                adminRoles,
                                adminGroups,
                                adminUsers,
                                false,
                                false,
                                0L,
                                "http://assets.atlan.com/assets/sigma.svg"))
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
        public B direct(String hostname) {
            localCreds
                    .name("default-sigma-" + epoch + "-0")
                    .host(hostname)
                    .port(443)
                    .connectorConfigName("atlan-connectors-sigma");
            return this._credential(localCreds);
        }

        /**
         * Set up the crawler to use API token-based authentication.
         *
         * @param clientId through which to access Sigma
         * @param apiToken through which to access Sigma
         * @return the builder, set up to use API token-based authentication
         */
        public B apiToken(String clientId, String apiToken) {
            localCreds.authType("api_token").username(clientId).password(apiToken);
            return this._credential(localCreds);
        }

        /**
         * Defines the filter for assets to include when crawling.
         *
         * @param workbooks the GUIDs of workbooks to include when crawling
         * @return the builder, set to include only those workbooks specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public B include(List<String> workbooks) throws InvalidRequestException {
            Map<String, Map<String, String>> toInclude = buildFlatFilter(workbooks);
            try {
                return this._parameter("include-filter", Serde.allInclusiveMapper.writeValueAsString(toInclude));
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
        public B exclude(List<String> workbooks) throws InvalidRequestException {
            Map<String, Map<String, String>> toExclude = buildFlatFilter(workbooks);
            try {
                return this._parameter("exclude-filter", Serde.allInclusiveMapper.writeValueAsString(toExclude));
            } catch (JsonProcessingException e) {
                throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
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
                    ._label("orchestration.atlan.com/source", "sigma")
                    ._label("orchestration.atlan.com/sourceCategory", "bi")
                    ._label("orchestration.atlan.com/type", "connector")
                    ._label("orchestration.atlan.com/verified", "true")
                    ._label("package.argoproj.io/installer", "argopm")
                    ._label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hsigma")
                    ._label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    ._label("orchestration.atlan.com/default-sigma-" + epoch, "true")
                    ._label("orchestration.atlan.com/atlan-ui", "true")
                    ._annotation("orchestration.atlan.com/allowSchedule", "true")
                    ._annotation("orchestration.atlan.com/categories", "sigma,crawler")
                    ._annotation("orchestration.atlan.com/dependentPackage", "")
                    ._annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/8731744918813")
                    ._annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    ._annotation("orchestration.atlan.com/icon", "http://assets.atlan.com/assets/sigma.svg")
                    ._annotation("orchestration.atlan.com/logo", "http://assets.atlan.com/assets/sigma.svg")
                    ._annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/sigma")
                    ._annotation("orchestration.atlan.com/name", "Sigma Assets")
                    ._annotation("package.argoproj.io/author", "Atlan")
                    ._annotation(
                            "package.argoproj.io/description",
                            "Package to crawl Sigma assets and publish to Atlan for discovery")
                    ._annotation(
                            "package.argoproj.io/homepage", "https://github.com/atlanhq/marketplace-packages#readme")
                    ._annotation("package.argoproj.io/keywords", "[\"sigma\",\"bi\",\"connector\",\"crawler\"]")
                    ._annotation("package.argoproj.io/name", "@atlan/sigma")
                    ._annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    ._annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    ._annotation("package.argoproj.io/support", "support@atlan.com")
                    ._annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-sigma-" + epoch)
                    ._parameters(Map.ofEntries(
                            Map.entry("credential-guid", "{{credentialGuid}}"),
                            Map.entry("connection", connection.toJson(client)),
                            Map.entry("atlas-auth-type", "internal"),
                            Map.entry("publish-mode", "production")));
        }
    }
}
