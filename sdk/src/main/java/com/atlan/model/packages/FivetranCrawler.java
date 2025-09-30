/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
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

    /**
     * Create the base configuration for a new Fivetran crawler. Sets all admins as connection admins.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @return the builder for the base configuration of a Fivetran crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static FivetranCrawlerBuilder<?, ?> creator(AtlanClient client, String connectionName)
            throws AtlanException {
        return creator(client, connectionName, List.of(client.getRoleCache().getIdForSid("$admin")), null, null);
    }

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
        return _internal()
                .setup(
                        PREFIX,
                        "@atlan/fivetran",
                        client,
                        getConnection(
                                client,
                                connectionName,
                                AtlanConnectorType.FIVETRAN,
                                adminRoles,
                                adminGroups,
                                adminUsers,
                                false,
                                false,
                                0L,
                                "https://res.cloudinary.com/crunchbase-production/image/upload/c_lpad,f_auto,q_auto:eco,dpr_1/mmhosuxvz2msbiieekl3"))
                .publishAnnouncements(false);
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
        public B apiToken(String apiKey, String apiSecret) {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            localCreds
                    .name("default-fivetran-" + epoch + "-0")
                    .host("https://api.fivetran.com")
                    .port(443)
                    .authType("api")
                    .username(apiKey)
                    .password(apiSecret)
                    .connectorConfigName("atlan-connectors-fivetran");
            return this._credential(localCreds);
        }

        /**
         * Whether to publish process announcements (true) or not.
         *
         * @param enabled if true, will publish process announcements
         * @return the builder, set up to publish (or not) process announcements
         */
        public B publishAnnouncements(boolean enabled) {
            return this._parameter("advanced-config-strategy", "custom")
                    ._parameter("publish-announcements", "" + enabled);
        }

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        @Override
        protected B metadata() {
            return this._label("orchestration.atlan.com/certified", "true")
                    ._label("orchestration.atlan.com/source", "fivetran")
                    ._label("orchestration.atlan.com/sourceCategory", "elt")
                    ._label("orchestration.atlan.com/type", "connector")
                    ._label("orchestration.atlan.com/verified", "true")
                    ._label("package.argoproj.io/installer", "argopm")
                    ._label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hfivetran")
                    ._label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    ._label("orchestration.atlan.com/default-fivetran-" + epoch, "true")
                    ._label("orchestration.atlan.com/atlan-ui", "true")
                    ._annotation("orchestration.atlan.com/allowSchedule", "true")
                    ._annotation("orchestration.atlan.com/dependentPackage", "")
                    ._annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/8427123935121")
                    ._annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    ._annotation(
                            "orchestration.atlan.com/icon",
                            "https://res.cloudinary.com/crunchbase-production/image/upload/c_lpad,f_auto,q_auto:eco,dpr_1/mmhosuxvz2msbiieekl3")
                    ._annotation(
                            "orchestration.atlan.com/logo",
                            "https://alternative.me/media/256/fivetran-icon-qfxkppdpdx2oh4r9-c.png")
                    ._annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/fivetran")
                    ._annotation("orchestration.atlan.com/name", "Fivetran Enrichment")
                    ._annotation("orchestration.atlan.com/usecase", "crawling,enrichment")
                    ._annotation("package.argoproj.io/author", "Atlan")
                    ._annotation(
                            "package.argoproj.io/description",
                            "Enrich known assets associated with Fivetran Connectors with column-level lineage.  Requires access to Fivetran's Metadata API.")
                    ._annotation(
                            "package.argoproj.io/homepage", "https://packages.atlan.com/-/web/detail/@atlan/fivetran")
                    ._annotation("package.argoproj.io/keywords", "[\"connector\",\"elt\",\"fivetran\",\"lineage\"]")
                    ._annotation("package.argoproj.io/name", "@atlan/fivetran")
                    ._annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    ._annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    ._annotation("package.argoproj.io/support", "support@atlan.com")
                    ._annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-fivetran-" + epoch)
                    ._parameters(Map.ofEntries(
                            Map.entry("credential-guid", "{{credentialGuid}}"),
                            Map.entry("connection", connection.toJson(client))));
        }
    }
}
