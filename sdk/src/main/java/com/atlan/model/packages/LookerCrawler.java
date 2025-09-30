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
public class LookerCrawler extends AbstractCrawler {

    public static final String PREFIX = AtlanPackageType.LOOKER.getValue();

    /**
     * Create the base configuration for a new Looker crawler. Sets all admins as connection admins.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @return the builder for the base configuration of a Looker crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static LookerCrawlerBuilder<?, ?> creator(AtlanClient client, String connectionName) throws AtlanException {
        return creator(client, connectionName, List.of(client.getRoleCache().getIdForSid("$admin")), null, null);
    }

    /**
     * Create the base configuration for a new Looker crawler.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @param adminRoles unique identifiers (GUIDs) of roles who will be connection admins on the connection
     * @param adminGroups internal names of groups who will be connection admins on the connection
     * @param adminUsers usernames of users who will be connection admins on the connection
     * @return the builder for the base configuration of a Looker crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static LookerCrawlerBuilder<?, ?> creator(
            AtlanClient client,
            String connectionName,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers)
            throws AtlanException {
        return _internal()
                .setup(
                        PREFIX,
                        "@atlan/looker",
                        client,
                        getConnection(
                                client,
                                connectionName,
                                AtlanConnectorType.LOOKER,
                                adminRoles,
                                adminGroups,
                                adminUsers,
                                false,
                                false,
                                0L,
                                "https://www.pngrepo.com/png/354012/512/looker-icon.png"))
                .includeFolders(null)
                .excludeFolders(null)
                .includeProjects(null)
                .excludeProjects(null);
    }

    public abstract static class LookerCrawlerBuilder<C extends LookerCrawler, B extends LookerCrawlerBuilder<C, B>>
            extends AbstractCrawlerBuilder<C, B> {

        /**
         * Set up the crawler to extract directly from Looker.
         *
         * @param hostname of Looker
         * @param port for Looker (either 443 or 19999)
         * @param clientId through which to access Looker
         * @param clientSecret through which to access Looker
         * @return the builder, set up to extract directly from Looker
         */
        public B direct(String hostname, int port, String clientId, String clientSecret) {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            localCreds
                    .name("default-looker-" + epoch + "-0")
                    .host(hostname)
                    .port(port)
                    .authType("resource_owner")
                    .username(clientId)
                    .password(clientSecret)
                    .connectorConfigName("atlan-connectors-looker");
            return this._parameter("extraction-method", "direct")._credential(localCreds);
        }

        /**
         * Enable field-level lineage when crawling Looker.
         *
         * @param privateKey the SSH private key to use to connect to Git for field-level lineage
         * @param privateKeyPassphrase the passphrase for the SSH private key
         * @return the builder, set up to crawl field-level lineage for Looker
         */
        public B fieldLevelLineage(String privateKey, String privateKeyPassphrase) {
            localCreds.extra("ssh_private_key", privateKey).extra("passphrase", privateKeyPassphrase);
            return this._parameter("use-field-level-lineage", "true")._credential(localCreds);
        }

        /**
         * Defines the filter for folders to include when crawling.
         *
         * @param folders the numeric IDs of folders to include when crawling
         * @return the builder, set to include only those folders specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public B includeFolders(List<String> folders) throws InvalidRequestException {
            Map<String, Map<String, String>> toInclude = buildFlatFilter(folders);
            try {
                return this._parameter("include-folders", Serde.allInclusiveMapper.writeValueAsString(toInclude));
            } catch (JsonProcessingException e) {
                throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
            }
        }

        /**
         * Defines the filter for folders to exclude when crawling.
         *
         * @param folders the numeric IDs of folders to exclude when crawling
         * @return the builder, set to exclude only those folders specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public B excludeFolders(List<String> folders) throws InvalidRequestException {
            Map<String, Map<String, String>> toExclude = buildFlatFilter(folders);
            try {
                return this._parameter("exclude-folders", Serde.allInclusiveMapper.writeValueAsString(toExclude));
            } catch (JsonProcessingException e) {
                throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
            }
        }

        /**
         * Defines the filter for projects to include when crawling.
         *
         * @param projects the names of projects to include when crawling
         * @return the builder, set to include only those projects specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public B includeProjects(List<String> projects) throws InvalidRequestException {
            Map<String, Map<String, String>> toInclude = buildFlatFilter(projects);
            try {
                return this._parameter("include-projects", Serde.allInclusiveMapper.writeValueAsString(toInclude));
            } catch (JsonProcessingException e) {
                throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
            }
        }

        /**
         * Defines the filter for projects to exclude when crawling.
         *
         * @param projects the names of projects to exclude when crawling
         * @return the builder, set to exclude only those projects specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public B excludeProjects(List<String> projects) throws InvalidRequestException {
            Map<String, Map<String, String>> toExclude = buildFlatFilter(projects);
            try {
                return this._parameter("exclude-projects", Serde.allInclusiveMapper.writeValueAsString(toExclude));
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
                    ._label("orchestration.atlan.com/source", "looker")
                    ._label("orchestration.atlan.com/sourceCategory", "bi")
                    ._label("orchestration.atlan.com/type", "connector")
                    ._label("orchestration.atlan.com/verified", "true")
                    ._label("package.argoproj.io/installer", "argopm")
                    ._label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hlooker")
                    ._label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    ._label("orchestration.atlan.com/default-looker-" + epoch, "true")
                    ._label("orchestration.atlan.com/atlan-ui", "true")
                    ._annotation("orchestration.atlan.com/allowSchedule", "true")
                    ._annotation("orchestration.atlan.com/dependentPackage", "")
                    ._annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6330214610193")
                    ._annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    ._annotation(
                            "orchestration.atlan.com/icon", "https://www.pngrepo.com/png/354012/512/looker-icon.png")
                    ._annotation(
                            "orchestration.atlan.com/logo", "https://looker.com/assets/img/images/logos/looker.svg")
                    ._annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/looker")
                    ._annotation("orchestration.atlan.com/name", "Looker Assets")
                    ._annotation("orchestration.atlan.com/usecase", "crawling,auto-classifications")
                    ._annotation("package.argoproj.io/author", "Atlan")
                    ._annotation(
                            "package.argoproj.io/description",
                            "Package to crawl Looker assets and publish to Atlan for discovery")
                    ._annotation(
                            "package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/looker")
                    ._annotation(
                            "package.argoproj.io/keywords", "[\"looker\",\"bi\",\"connector\",\"crawler\",\"lookml\"]")
                    ._annotation("package.argoproj.io/name", "@atlan/looker")
                    ._annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    ._annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    ._annotation("package.argoproj.io/support", "support@atlan.com")
                    ._annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-looker-" + epoch)
                    ._parameters(Map.ofEntries(
                            Map.entry("credential-guid", "{{credentialGuid}}"),
                            Map.entry("connection", connection.toJson(client))));
        }
    }
}
