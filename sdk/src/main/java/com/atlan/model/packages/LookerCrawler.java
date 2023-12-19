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

    /** Connectivity to the Atlan tenant where the package will run. */
    AtlanClient client;

    /** Connection through which the package will manage its assets. */
    Connection connection;

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
        Connection connection = getConnection(
                client,
                connectionName,
                AtlanConnectorType.LOOKER,
                adminRoles,
                adminGroups,
                adminUsers,
                false,
                false,
                0L,
                "https://www.pngrepo.com/png/354012/512/looker-icon.png");
        return _internal()
                .client(client)
                .connection(connection)
                .metadata()
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
        public LookerCrawlerBuilder<C, B> direct(String hostname, String port, String clientId, String clientSecret) {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this.parameters(params())
                    .parameter("extraction-method", "direct")
                    .credential("name", "default-looker-" + epoch + "-0")
                    .credential("host", hostname)
                    .credential("port", port)
                    .credential("authType", "resource_owner")
                    .credential("username", clientId)
                    .credential("password", clientSecret)
                    .credential("connectorConfigName", "atlan-connectors-looker");
        }

        /**
         * Enable field-level lineage when crawling Looker.
         *
         * @param privateKey the SSH private key to use to connect to Git for field-level lineage
         * @param privateKeyPassphrase the passphrase for the SSH private key
         * @return the builder, set up to crawl field-level lineage for Looker
         */
        public LookerCrawlerBuilder<C, B> fieldLevelLineage(String privateKey, String privateKeyPassphrase) {
            return this.parameter("use-field-level-lineage", "true")
                    .credential("extra", Map.of("ssh_private_key", privateKey, "passphrase", privateKeyPassphrase));
        }

        /**
         * Defines the filter for folders to include when crawling.
         *
         * @param folders the numeric IDs of folders to include when crawling
         * @return the builder, set to include only those folders specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public LookerCrawlerBuilder<C, B> includeFolders(List<String> folders) throws InvalidRequestException {
            Map<String, Map<String, String>> toInclude = buildFlatFilter(folders);
            try {
                return this.parameter("include-folders", Serde.allInclusiveMapper.writeValueAsString(toInclude));
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
        public LookerCrawlerBuilder<C, B> excludeFolders(List<String> folders) throws InvalidRequestException {
            Map<String, Map<String, String>> toExclude = buildFlatFilter(folders);
            try {
                return this.parameter("exclude-folders", Serde.allInclusiveMapper.writeValueAsString(toExclude));
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
        public LookerCrawlerBuilder<C, B> includeProjects(List<String> projects) throws InvalidRequestException {
            Map<String, Map<String, String>> toInclude = buildFlatFilter(projects);
            try {
                return this.parameter("include-projects", Serde.allInclusiveMapper.writeValueAsString(toInclude));
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
        public LookerCrawlerBuilder<C, B> excludeProjects(List<String> projects) throws InvalidRequestException {
            Map<String, Map<String, String>> toExclude = buildFlatFilter(projects);
            try {
                return this.parameter("exclude-projects", Serde.allInclusiveMapper.writeValueAsString(toExclude));
            } catch (JsonProcessingException e) {
                throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
            }
        }

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        protected LookerCrawlerBuilder<C, B> metadata() {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this.prefix(PREFIX)
                    .name("@atlan/looker")
                    .runName(PREFIX + "-" + epoch)
                    .label("orchestration.atlan.com/certified", "true")
                    .label("orchestration.atlan.com/source", "looker")
                    .label("orchestration.atlan.com/sourceCategory", "bi")
                    .label("orchestration.atlan.com/type", "connector")
                    .label("orchestration.atlan.com/verified", "true")
                    .label("package.argoproj.io/installer", "argopm")
                    .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hlooker")
                    .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    .label("orchestration.atlan.com/default-looker-" + epoch, "true")
                    .label("orchestration.atlan.com/atlan-ui", "true")
                    .annotation("orchestration.atlan.com/allowSchedule", "true")
                    .annotation("orchestration.atlan.com/dependentPackage", "")
                    .annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6330214610193")
                    .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    .annotation(
                            "orchestration.atlan.com/icon", "https://www.pngrepo.com/png/354012/512/looker-icon.png")
                    .annotation("orchestration.atlan.com/logo", "https://looker.com/assets/img/images/logos/looker.svg")
                    .annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/looker")
                    .annotation("orchestration.atlan.com/name", "Looker Assets")
                    .annotation("orchestration.atlan.com/usecase", "crawling,auto-classifications")
                    .annotation("package.argoproj.io/author", "Atlan")
                    .annotation(
                            "package.argoproj.io/description",
                            "Package to crawl Looker assets and publish to Atlan for discovery")
                    .annotation("package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/looker")
                    .annotation(
                            "package.argoproj.io/keywords", "[\"looker\",\"bi\",\"connector\",\"crawler\",\"lookml\"]")
                    .annotation("package.argoproj.io/name", "@atlan/looker")
                    .annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    .annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    .annotation("package.argoproj.io/support", "support@atlan.com")
                    .annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-looker-" + epoch);
        }

        private Map<String, String> params() {
            return Map.ofEntries(
                    Map.entry("credential-guid", "{{credentialGuid}}"),
                    Map.entry("connection", connection.toJson(client)));
        }
    }
}
