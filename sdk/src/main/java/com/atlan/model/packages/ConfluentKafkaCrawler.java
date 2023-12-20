/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
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
public class ConfluentKafkaCrawler extends AbstractCrawler {

    public static final String PREFIX = AtlanPackageType.KAFKA_CONFLUENT_CLOUD.getValue();

    /** Connectivity to the Atlan tenant where the package will run. */
    AtlanClient client;

    /** Connection through which the package will manage its assets. */
    Connection connection;

    /** Credentials for this connection. */
    Credential.CredentialBuilder<?, ?> localCreds;

    /**
     * Create the base configuration for a new Confluent Cloud Kafka crawler.
     *
     * @param client connectivity to an Atlan tenant
     * @param connectionName name of the connection to create when running the crawler for the first time
     * @param adminRoles unique identifiers (GUIDs) of roles who will be connection admins on the connection
     * @param adminGroups internal names of groups who will be connection admins on the connection
     * @param adminUsers usernames of users who will be connection admins on the connection
     * @return the builder for the base configuration of a Confluent Cloud Kafka crawler
     * @throws AtlanException if there is not at least one connection admin specified, or any specified are invalid
     */
    public static ConfluentKafkaCrawlerBuilder<?, ?> creator(
            AtlanClient client,
            String connectionName,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers)
            throws AtlanException {
        Connection connection = getConnection(
                client,
                connectionName,
                AtlanConnectorType.CONFLUENT_KAFKA,
                adminRoles,
                adminGroups,
                adminUsers,
                false,
                false,
                0L,
                "https://cdn.confluent.io/wp-content/uploads/apache-kafka-icon-2021-e1638496305992.jpg");
        return _internal()
                .client(client)
                .connection(connection)
                .metadata()
                .include(null)
                .exclude(null)
                .skipInternal(true);
    }

    public abstract static class ConfluentKafkaCrawlerBuilder<
                    C extends ConfluentKafkaCrawler, B extends ConfluentKafkaCrawlerBuilder<C, B>>
            extends AbstractCrawlerBuilder<C, B> {

        /**
         * Set up the crawler to extract directly from Kafka.
         *
         * @param bootstrap hostname and port number (host.example.com:9092) for the Kafka bootstrap server
         * @param encrypted whether to use encrypted SSL connection (true), or plaintext (false)
         * @return the builder, set up to extract directly from Kafka
         */
        public ConfluentKafkaCrawlerBuilder<C, B> direct(String bootstrap, boolean encrypted) {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            localCreds
                    .name("default-confluent-kafka-" + epoch + "-0")
                    .host(bootstrap)
                    .port(9092)
                    .extra("security_protocol", encrypted ? "SASL_SSL" : "SASL_PLAINTEXT")
                    .connectorConfigName("atlan-connectors-kafka-confluent-cloud");
            return this.parameters(params())
                    .parameter("extraction-method", "direct")
                    .credential(localCreds);
        }

        /**
         * Set up the crawler to use API token-based authentication.
         *
         * @param apiKey through which to access Kafka
         * @param apiSecret through which to access Kafka
         * @return the builder, set up to use API token-based authentication
         */
        public ConfluentKafkaCrawlerBuilder<C, B> apiToken(String apiKey, String apiSecret) {
            localCreds.authType("basic").username(apiKey).password(apiSecret);
            return this.credential(localCreds);
        }

        /**
         * Defines the filter for topics to include when crawling.
         *
         * @param regex any topic names that match this regular expression will be included in crawling
         * @return the builder, set to include only those topics specified
         * @throws InvalidRequestException in the unlikely event the provided filter cannot be translated
         */
        public ConfluentKafkaCrawlerBuilder<C, B> include(String regex) throws InvalidRequestException {
            return this.parameter("include-filter", regex);
        }

        /**
         * Defines a regular expression to use for excluding topics when crawling.
         *
         * @param regex any topic names that match this regular expression will be excluded from crawling
         * @return the builder, set to exclude any topics that match the provided regular expression
         */
        public ConfluentKafkaCrawlerBuilder<C, B> exclude(String regex) {
            return this.parameter("exclude-filter", regex);
        }

        /**
         * Whether to skip internal topics when crawling (true) or include them.
         *
         * @param enabled if true, internal topics will be skipped when crawling
         * @return the builder, set to include or exclude internal topics
         */
        public ConfluentKafkaCrawlerBuilder<C, B> skipInternal(boolean enabled) {
            return this.parameter("skip-internal-topics", "" + enabled);
        }

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        protected ConfluentKafkaCrawlerBuilder<C, B> metadata() {
            String epoch = Connection.getEpochFromQualifiedName(connection.getQualifiedName());
            return this.prefix(PREFIX)
                    .name("@atlan/kafka-confluent-cloud")
                    .runName(PREFIX + "-" + epoch)
                    .label("orchestration.atlan.com/certified", "true")
                    .label("orchestration.atlan.com/source", "confluent-kafka")
                    .label("orchestration.atlan.com/sourceCategory", "eventbus")
                    .label("orchestration.atlan.com/type", "connector")
                    .label("orchestration.atlan.com/verified", "true")
                    .label("package.argoproj.io/installer", "argopm")
                    .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hkafka-confluent-cloud")
                    .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    .label("orchestration.atlan.com/default-confluent-kafka-" + epoch, "true")
                    .label("orchestration.atlan.com/atlan-ui", "true")
                    .annotation("orchestration.atlan.com/allowSchedule", "true")
                    .annotation("orchestration.atlan.com/dependentPackage", "")
                    .annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6778924963599")
                    .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    .annotation(
                            "orchestration.atlan.com/icon",
                            "https://cdn.confluent.io/wp-content/uploads/apache-kafka-icon-2021-e1638496305992.jpg")
                    .annotation(
                            "orchestration.atlan.com/logo",
                            "https://cdn.confluent.io/wp-content/uploads/apache-kafka-icon-2021-e1638496305992.jpg")
                    .annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/kafka-confluent-cloud")
                    .annotation("orchestration.atlan.com/name", "Confluent Kafka Assets")
                    .annotation("orchestration.atlan.com/usecase", "crawling,discovery")
                    .annotation("package.argoproj.io/author", "Atlan")
                    .annotation(
                            "package.argoproj.io/description",
                            "Package to crawl Confluent Kafka assets and publish to Atlan for discovery.")
                    .annotation(
                            "package.argoproj.io/homepage",
                            "https://packages.atlan.com/-/web/detail/@atlan/kafka-confluent-cloud")
                    .annotation(
                            "package.argoproj.io/keywords",
                            "[\"kafka-confluent-cloud\",\"confluent-kafka\",\"eventbus\",\"connector\",\"kafka\"]")
                    .annotation("package.argoproj.io/name", "@atlan/kafka-confluent-cloud")
                    .annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    .annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    .annotation("package.argoproj.io/support", "support@atlan.com")
                    .annotation("orchestration.atlan.com/atlanName", PREFIX + "-default-confluent-kafka-" + epoch);
        }

        private Map<String, String> params() {
            return Map.ofEntries(
                    Map.entry("credential-guid", "{{credentialGuid}}"),
                    Map.entry("connection", connection.toJson(client)),
                    Map.entry("publish-mode", "production"),
                    Map.entry("atlas-auth-type", "internal"));
        }
    }
}
