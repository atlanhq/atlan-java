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
public class TableauCrawler extends AbstractCrawler {

    public static final String PREFIX = AtlanPackageType.TABLEAU.getValue();

    /**
     * Create the base configuration for a new Tableau crawler. Sets all admins as connection admins.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @return the builder for the base configuration of a Tableau crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static TableauCrawlerBuilder<?, ?> creator(AtlanClient client, String connectionName) throws AtlanException {
        return creator(client, connectionName, List.of(client.getRoleCache().getIdForSid("$admin")), null, null);
    }

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
        return _internal()
                .setup(
                        PREFIX,
                        "@atlan/tableau",
                        client,
                        getConnection(
                                client,
                                connectionName,
                                AtlanConnectorType.TABLEAU,
                                adminRoles,
                                adminGroups,
                                adminUsers,
                                false,
                                false,
                                0L,
                                "https://img.icons8.com/color/480/000000/tableau-software.png"))
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
        public B direct(String hostname, String site, boolean sslEnabled) {
            localCreds
                    .name("default-tableau-" + epoch + "-0")
                    .host(hostname)
                    .port(443)
                    .extra("protocol", sslEnabled ? "https" : "http")
                    .extra("defaultSite", site)
                    .connectorConfigName("atlan-connectors-tableau");
            return this._parameter("extraction-method", "direct")._credential(localCreds);
        }

        /**
         * Set up the crawler to use basic authentication.
         *
         * @param username through which to access Tableau
         * @param password through which to access Tableau
         * @return the builder, set up to use basic authentication
         */
        public B basicAuth(String username, String password) {
            localCreds.authType("basic").username(username).password(password);
            return this._credential(localCreds);
        }

        /**
         * Set up the crawler to use PAT-based authentication.
         *
         * @param username through which to access Tableau
         * @param accessToken personal access token for the user, through which to access Tableau
         * @return the builder, set up to use PAT-based authentication
         */
        public B personalAccessToken(String username, String accessToken) {
            localCreds.authType("personal_access_token").username(username).password(accessToken);
            return this._credential(localCreds);
        }

        /**
         * Defines the filter for projects to include when crawling.
         *
         * @param projects the GUIDs of projects to include when crawling
         * @return the builder, set to include only those projects specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public B include(List<String> projects) throws InvalidRequestException {
            Map<String, Map<String, String>> toIncludeProjects = buildFlatFilter(projects);
            try {
                return this._parameter(
                        "include-filter", Serde.allInclusiveMapper.writeValueAsString(toIncludeProjects));
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
        public B exclude(List<String> projects) throws InvalidRequestException {
            Map<String, Map<String, String>> toExcludeProjects = buildFlatFilter(projects);
            try {
                return this._parameter(
                        "exclude-filter", Serde.allInclusiveMapper.writeValueAsString(toExcludeProjects));
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
        public B exclude(String regex) {
            return this._parameter("exclude-projects-regex", regex);
        }

        /**
         * Whether to crawl hidden datasource fields (true) or not.
         *
         * @param enabled if true, hidden datasource fields will be crawled otherwise they will not
         * @return the builder, set to include or exclude hidden datasource fields
         */
        public B crawlHiddenFields(boolean enabled) {
            return this._parameter("crawl-hidden-datasource-fields", "" + enabled);
        }

        /**
         * Whether to crawl unpublished worksheets and dashboards (true) or not.
         *
         * @param enabled if true, unpublished worksheets and dashboards will be crawled otherwise they will not
         * @return the builder, set to include or exclude unpublished worksheets and dashboards
         */
        public B crawlUnpublished(boolean enabled) {
            return this._parameter("crawl-unpublished-worksheets-dashboards", "" + enabled);
        }

        /**
         * Set an alternate host to use for the "View in Tableau" button for assets in the UI.
         *
         * @param hostname alternate hostname (and protocol) to use
         * @return the builder, set to use an alternate host for viewing assets in Tableau
         */
        public B alternateHost(String hostname) {
            return this._parameter("tableau-alternate-host", hostname);
        }

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        @Override
        protected B metadata() {
            return this._label("orchestration.atlan.com/certified", "true")
                    ._label("orchestration.atlan.com/source", "tableau")
                    ._label("orchestration.atlan.com/sourceCategory", "bi")
                    ._label("orchestration.atlan.com/type", "connector")
                    ._label("orchestration.atlan.com/verified", "true")
                    ._label("package.argoproj.io/installer", "argopm")
                    ._label("package.argoproj.io/name", "a-t-ratlans-l-a-s-htableau")
                    ._label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    ._label("orchestration.atlan.com/default-tableau-" + epoch, "true")
                    ._label("orchestration.atlan.com/atlan-ui", "true")
                    ._annotation("orchestration.atlan.com/allowSchedule", "true")
                    ._annotation("orchestration.atlan.com/categories", "tableau,crawler")
                    ._annotation("orchestration.atlan.com/dependentPackage", "")
                    ._annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6332449996689")
                    ._annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    ._annotation(
                            "orchestration.atlan.com/icon",
                            "https://img.icons8.com/color/480/000000/tableau-software.png")
                    ._annotation(
                            "orchestration.atlan.com/logo",
                            "https://img.icons8.com/color/480/000000/tableau-software.png")
                    ._annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/tableau")
                    ._annotation("orchestration.atlan.com/name", "Tableau Assets")
                    ._annotation("package.argoproj.io/author", "Atlan")
                    ._annotation(
                            "package.argoproj.io/description",
                            "Package to crawl Tableau assets and publish to Atlan for discovery")
                    ._annotation(
                            "package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/tableau")
                    ._annotation("package.argoproj.io/keywords", "[\"tableau\",\"bi\",\"connector\",\"crawler\"]")
                    ._annotation("package.argoproj.io/name", "@atlan/tableau")
                    ._annotation("package.argoproj.io/parent", ".")
                    ._annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    ._annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    ._annotation("package.argoproj.io/support", "support@atlan.com")
                    ._annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-tableau-" + epoch)
                    ._parameters(Map.ofEntries(
                            Map.entry("credential-guid", "{{credentialGuid}}"),
                            Map.entry("connection", connection.toJson(client)),
                            Map.entry("atlas-auth-type", "internal"),
                            Map.entry("publish-mode", "production")));
        }
    }
}
