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
public class TableauCrawler extends AbstractCrawler {

    public static final String PREFIX = AtlanPackageType.TABLEAU.getValue();

    /** Connectivity to the Atlan tenant where the package will run. */
    AtlanClient client;

    /** Connection through which the package will manage its assets. */
    Connection connection;

    /**
     * Create the base configuration for a new Tableau crawler.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @param adminRoles unique identifiers (GUIDs) of roles who will be connection admins on the connection
     * @param adminGroups internal names of groups who will be connection admins on the connection
     * @param adminUsers usernames of users who will be connection admins on the connection
     * @return the builder for the base configuration of a Tableau crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static TableauCrawlerBuilder<?, ?> creator(
            AtlanClient client,
            String connectionName,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers)
            throws AtlanException {
        Connection connection = getConnection(
                client,
                connectionName,
                AtlanConnectorType.TABLEAU,
                adminRoles,
                adminGroups,
                adminUsers,
                false,
                false,
                0L,
                "https://img.icons8.com/color/480/000000/tableau-software.png");
        return _internal()
                .client(client)
                .connection(connection)
                .metadata()
                .include(null)
                .exclude((List<String>) null)
                .crawlHiddenFields(true)
                .crawlUnpublished(true);
    }

    public abstract static class TableauCrawlerBuilder<C extends TableauCrawler, B extends TableauCrawlerBuilder<C, B>>
            extends AbstractCrawlerBuilder<C, B> {

        /**
         * Set up the crawler to extract directly from Tableau.
         *
         * @param hostname of Tableau
         * @param site in Tableau from which to extract
         * @param sslEnabled if true, use SSL for the connection, otherwise do not use SSL
         * @return the builder, set up to extract directly from Tableau
         */
        public TableauCrawlerBuilder<C, B> direct(String hostname, String site, boolean sslEnabled) {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this.parameters(params())
                    .parameter("extraction-method", "direct")
                    .credential("name", "default-tableau-" + epoch + "-0")
                    .credential("host", hostname)
                    .credential("port", 443)
                    .credential("extra", Map.of("protocol", sslEnabled ? "https" : "http", "defaultSite", site))
                    .credential("connectorConfigName", "atlan-connectors-tableau");
        }

        /**
         * Set up the crawler to use basic authentication.
         *
         * @param username through which to access Tableau
         * @param password through which to access Tableau
         * @return the builder, set up to use basic authentication
         */
        public TableauCrawlerBuilder<C, B> basicAuth(String username, String password) {
            return this.credential("authType", "basic")
                    .credential("username", username)
                    .credential("password", password);
        }

        /**
         * Set up the crawler to use PAT-based authentication.
         *
         * @param username through which to access Tableau
         * @param accessToken personal access token for the user, through which to access Tableau
         * @return the builder, set up to use PAT-based authentication
         */
        public TableauCrawlerBuilder<C, B> personalAccessToken(String username, String accessToken) {
            return this.credential("authType", "personal_access_token")
                    .credential("username", username)
                    .credential("password", accessToken);
        }

        /**
         * Defines the filter for projects to include when crawling.
         *
         * @param projects the GUIDs of projects to include when crawling
         * @return the builder, set to include only those projects specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public TableauCrawlerBuilder<C, B> include(List<String> projects) throws InvalidRequestException {
            Map<String, Map<String, String>> toIncludeProjects = buildFlatFilter(projects);
            try {
                return this.parameter("include-filter", Serde.allInclusiveMapper.writeValueAsString(toIncludeProjects));
            } catch (JsonProcessingException e) {
                throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
            }
        }

        /**
         * Defines the filter for projects to exclude when crawling.
         *
         * @param projects the GUIDs of projects to exclude when crawling
         * @return the builder, set to exclude only those projects specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public TableauCrawlerBuilder<C, B> exclude(List<String> projects) throws InvalidRequestException {
            Map<String, Map<String, String>> toExcludeProjects = buildFlatFilter(projects);
            try {
                return this.parameter("exclude-filter", Serde.allInclusiveMapper.writeValueAsString(toExcludeProjects));
            } catch (JsonProcessingException e) {
                throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
            }
        }

        /**
         * Defines a regular expression to use for excluding projects when crawling.
         *
         * @param regex any project names that match this regular expression will be excluded from crawling
         * @return the builder, set to exclude any assets that match the provided regular expression
         */
        public TableauCrawlerBuilder<C, B> exclude(String regex) {
            return this.parameter("exclude-projects-regex", regex);
        }

        /**
         * Whether to crawl hidden datasource fields (true) or not.
         *
         * @param enabled if true, hidden datasource fields will be crawled otherwise they will not
         * @return the builder, set to include or exclude hidden datasource fields
         */
        public TableauCrawlerBuilder<C, B> crawlHiddenFields(boolean enabled) {
            return this.parameter("crawl-hidden-datasource-fields", "" + enabled);
        }

        /**
         * Whether to crawl unpublished worksheets and dashboards (true) or not.
         *
         * @param enabled if true, unpublished worksheets and dashboards will be crawled otherwise they will not
         * @return the builder, set to include or exclude unpublished worksheets and dashboards
         */
        public TableauCrawlerBuilder<C, B> crawlUnpublished(boolean enabled) {
            return this.parameter("crawl-unpublished-worksheets-dashboards", "" + enabled);
        }

        /**
         * Set an alternate host to use for the "View in Tableau" button for assets in the UI.
         *
         * @param hostname alternate hostname (and protocol) to use
         * @return the builder, set to use an alternate host for viewing assets in Tableau
         */
        public TableauCrawlerBuilder<C, B> alternateHost(String hostname) {
            return this.parameter("tableau-alternate-host", hostname);
        }

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        protected TableauCrawlerBuilder<C, B> metadata() {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this.prefix(PREFIX)
                    .name("@atlan/tableau")
                    .runName(PREFIX + "-" + epoch)
                    .label("orchestration.atlan.com/certified", "true")
                    .label("orchestration.atlan.com/source", "tableau")
                    .label("orchestration.atlan.com/sourceCategory", "bi")
                    .label("orchestration.atlan.com/type", "connector")
                    .label("orchestration.atlan.com/verified", "true")
                    .label("package.argoproj.io/installer", "argopm")
                    .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-htableau")
                    .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    .label("orchestration.atlan.com/default-tableau-" + epoch, "true")
                    .label("orchestration.atlan.com/atlan-ui", "true")
                    .annotation("orchestration.atlan.com/allowSchedule", "true")
                    .annotation("orchestration.atlan.com/categories", "tableau,crawler")
                    .annotation("orchestration.atlan.com/dependentPackage", "")
                    .annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6332449996689")
                    .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    .annotation(
                            "orchestration.atlan.com/icon",
                            "https://img.icons8.com/color/480/000000/tableau-software.png")
                    .annotation(
                            "orchestration.atlan.com/logo",
                            "https://img.icons8.com/color/480/000000/tableau-software.png")
                    .annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/tableau")
                    .annotation("orchestration.atlan.com/name", "Tableau Assets")
                    .annotation("package.argoproj.io/author", "Atlan")
                    .annotation(
                            "package.argoproj.io/description",
                            "Package to crawl Tableau assets and publish to Atlan for discovery")
                    .annotation(
                            "package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/tableau")
                    .annotation("package.argoproj.io/keywords", "[\"tableau\",\"bi\",\"connector\",\"crawler\"]")
                    .annotation("package.argoproj.io/name", "@atlan/tableau")
                    .annotation("package.argoproj.io/parent", ".")
                    .annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    .annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    .annotation("package.argoproj.io/support", "support@atlan.com")
                    .annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-tableau-" + epoch);
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
