/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.Credential;
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
public class FivetranCrawler extends AbstractCrawler {

    public static final String PREFIX = AtlanPackageType.FIVETRAN.getValue();

    /** Connectivity to the Atlan tenant where the package will run. */
    AtlanClient client;

    /** Connection through which the package will manage its assets. */
    Connection connection;

    /** Credentials for this connection. */
    Credential.CredentialBuilder<?, ?> localCreds;

    /**
     * Create the base configuration for a new Fivetran crawler.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @param adminRoles unique identifiers (GUIDs) of roles who will be connection admins on the connection
     * @param adminGroups internal names of groups who will be connection admins on the connection
     * @param adminUsers usernames of users who will be connection admins on the connection
     * @return the builder for the base configuration of a Fivetran crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static FivetranCrawlerBuilder<?, ?> creator(
            AtlanClient client,
            String connectionName,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers)
            throws AtlanException {
        Connection connection = getConnection(
                client,
                connectionName,
                AtlanConnectorType.FIVETRAN,
                adminRoles,
                adminGroups,
                adminUsers,
                false,
                false,
                0L,
                "https://res.cloudinary.com/crunchbase-production/image/upload/c_lpad,f_auto,q_auto:eco,dpr_1/mmhosuxvz2msbiieekl3");
        return _internal().client(client).connection(connection).metadata().publishAnnouncements(false);
    }

    public abstract static class FivetranCrawlerBuilder<
                    C extends FivetranCrawler, B extends FivetranCrawlerBuilder<C, B>>
            extends AbstractCrawlerBuilder<C, B> {

        /**
         * Set up the crawler to use API token-based authentication.
         *
         * @param apiKey through which to access Fivetran APIs
         * @param apiSecret through which to access Fivetran APIs
         * @return the builder, set up to extract directly from Fivetran APIs using API token authentication
         */
        public FivetranCrawlerBuilder<C, B> apiToken(String apiKey, String apiSecret) {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            localCreds
                    .name("default-fivetran-" + epoch + "-0")
                    .host("https://api.fivetran.com")
                    .port(443)
                    .authType("api")
                    .username(apiKey)
                    .password(apiSecret)
                    .connectorConfigName("atlan-connectors-fivetran");
            return this.parameters(params()).credential(localCreds);
        }

        /**
         * Whether to publish process announcements (true) or not.
         *
         * @param enabled if true, will publish process announcements
         * @return the builder, set up to publish (or not) process announcements
         */
        public FivetranCrawlerBuilder<C, B> publishAnnouncements(boolean enabled) {
            return this.parameter("advanced-config-strategy", "custom")
                    .parameter("publish-announcements", "" + enabled);
        }

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        protected FivetranCrawlerBuilder<C, B> metadata() {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this.prefix(PREFIX)
                    .name("@atlan/fivetran")
                    .runName(PREFIX + "-" + epoch)
                    .label("orchestration.atlan.com/certified", "true")
                    .label("orchestration.atlan.com/source", "fivetran")
                    .label("orchestration.atlan.com/sourceCategory", "elt")
                    .label("orchestration.atlan.com/type", "connector")
                    .label("orchestration.atlan.com/verified", "true")
                    .label("package.argoproj.io/installer", "argopm")
                    .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hfivetran")
                    .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    .label("orchestration.atlan.com/default-fivetran-" + epoch, "true")
                    .label("orchestration.atlan.com/atlan-ui", "true")
                    .annotation("orchestration.atlan.com/allowSchedule", "true")
                    .annotation("orchestration.atlan.com/dependentPackage", "")
                    .annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/8427123935121")
                    .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    .annotation(
                            "orchestration.atlan.com/icon",
                            "https://res.cloudinary.com/crunchbase-production/image/upload/c_lpad,f_auto,q_auto:eco,dpr_1/mmhosuxvz2msbiieekl3")
                    .annotation(
                            "orchestration.atlan.com/logo",
                            "https://alternative.me/media/256/fivetran-icon-qfxkppdpdx2oh4r9-c.png")
                    .annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/fivetran")
                    .annotation("orchestration.atlan.com/name", "Fivetran Enrichment")
                    .annotation("orchestration.atlan.com/usecase", "crawling,enrichment")
                    .annotation("package.argoproj.io/author", "Atlan")
                    .annotation(
                            "package.argoproj.io/description",
                            "Enrich known assets associated with Fivetran Connectors with column-level lineage.  Requires access to Fivetran's Metadata API.")
                    .annotation(
                            "package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/fivetran")
                    .annotation("package.argoproj.io/keywords", "[\"connector\",\"elt\",\"fivetran\",\"lineage\"]")
                    .annotation("package.argoproj.io/name", "@atlan/fivetran")
                    .annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    .annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    .annotation("package.argoproj.io/support", "support@atlan.com")
                    .annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-fivetran-" + epoch);
        }

        private Map<String, String> params() {
            return Map.ofEntries(
                    Map.entry("credential-guid", "{{credentialGuid}}"),
                    Map.entry("connection", connection.toJson(client)));
        }
    }
}
