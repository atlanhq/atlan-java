/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
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
public class SnowflakeMiner extends AbstractMiner {

    public static final String PREFIX = AtlanPackageType.SNOWFLAKE_MINER.getValue();

    /**
     * Create the base configuration for a new Snowflake miner.
     *
     * @param connectionQualifiedName unique name of the Snowflake connection whose assets should be mined
     * @return the builder for the base configuration of a Snowflake miner
     */
    public static SnowflakeMinerBuilder<?, ?> creator(String connectionQualifiedName) {
        return _internal().metadata().parameter("connection-qualified-name", connectionQualifiedName);
    }

    public abstract static class SnowflakeMinerBuilder<C extends SnowflakeMiner, B extends SnowflakeMinerBuilder<C, B>>
            extends AbstractMinerBuilder<C, B> {

        /**
         * Set up the miner to extract directly from Snowflake, using the default database.
         *
         * @param startEpoch date and time from which to start mining, as an epoch
         * @return the builder, set up to extract directly from Snowflake using the default database
         */
        public SnowflakeMinerBuilder<C, B> direct(long startEpoch) {
            return this.parameters(params())
                    .parameter("snowflake-database", "default")
                    .parameter("extraction-method", "query_history")
                    .parameter("miner-start-time-epoch", "" + startEpoch);
        }

        /**
         * Set up the miner to extract directly from Snowflake, using a cloned database.
         *
         * @param database name of the database to extract from
         * @param schema name of the schema to extract from
         * @param startEpoch date and time from which to start mining, as an epoch
         * @return the builder, set up to extract directly from Snowflake using a cloned database
         */
        public SnowflakeMinerBuilder<C, B> direct(String database, String schema, long startEpoch) {
            return this.parameters(params())
                    .parameter("database-name", database)
                    .parameter("schema-name", schema)
                    .parameter("extraction-method", "query_history")
                    .parameter("miner-start-time-epoch", "" + startEpoch);
        }

        /**
         * Set up the miner to extract from S3 (using JSON line-separated files).
         *
         * @param s3Bucket S3 bucket where the JSON line-separated files are located
         * @param s3Prefix prefix within the S3 bucket in which the JSON line-separated files are located
         * @param queryKey JSON key containing the query definition
         * @param defaultDatabase JSON key containing the default database name to use if a query is not qualified with database name
         * @param defaultSchema JSON key containing the default schema name to use if a query is not qualified with schema name
         * @param sessionId JSON key containing the session ID of the SQL query
         * @return the builder, set up to extract from a set of JSON line-separated files in S3
         */
        public SnowflakeMinerBuilder<C, B> s3(
                String s3Bucket,
                String s3Prefix,
                String queryKey,
                String defaultDatabase,
                String defaultSchema,
                String sessionId) {
            return this.parameters(params())
                    .parameter("extraction-method", "s3")
                    .parameter("extraction-s3-bucket", s3Bucket)
                    .parameter("extraction-s3-prefix", s3Prefix)
                    .parameter("sql-json-key", queryKey)
                    .parameter("catalog-json-key", defaultDatabase)
                    .parameter("schema-json-key", defaultSchema)
                    .parameter("session-json-key", sessionId);
        }

        /**
         * Defines users who should be excluded when calculating usage metrics for assets (for example, system accounts).
         *
         * @param users list of users to exclude when calculating usage metrics
         * @return the builder, set to exclude the specified users from usage metrics
         * @throws InvalidRequestException in the unlikely event the provided list cannot be translated
         */
        public SnowflakeMinerBuilder<C, B> excludeUsers(List<String> users) throws InvalidRequestException {
            try {
                return this.parameters(params())
                        .parameter(
                                "popularity-exclude-user-config", Serde.allInclusiveMapper.writeValueAsString(users));
            } catch (JsonProcessingException e) {
                throw new InvalidRequestException(ErrorCode.UNABLE_TO_TRANSLATE_FILTERS, e);
            }
        }

        /**
         * Whether to enable native lineage from Snowflake, using Snowflake's ACCESS_HISTORY.OBJECTS_MODIFIED Column.
         * Note: this is only available only for Snowflake Enterprise customers.
         *
         * @param enabled if true, native lineage from Snowflake will be used for crawling
         * @return the builder, set to include / exclude native lineage from Snowflake
         */
        public SnowflakeMinerBuilder<C, B> nativeLineage(boolean enabled) {
            return this.parameters(params())
                    .parameter("control-config-strategy", "custom")
                    .parameter("native-lineage-active", "" + enabled);
        }

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        protected SnowflakeMinerBuilder<C, B> metadata() {
            String epoch = getEpoch();
            return this.prefix(PREFIX)
                    .name("@atlan/snowflake-miner")
                    .runName(PREFIX + "-" + epoch)
                    .label("orchestration.atlan.com/certified", "true")
                    .label("orchestration.atlan.com/source", "snowflake")
                    .label("orchestration.atlan.com/sourceCategory", "warehouse")
                    .label("orchestration.atlan.com/type", "miner")
                    .label("orchestration.atlan.com/verified", "true")
                    .label("package.argoproj.io/installer", "argopm")
                    .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hsnowflake-miner")
                    .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    // .label("orchestration.atlan.com/default-snowflake-" + epoch, "true")
                    .label("orchestration.atlan.com/atlan-ui", "true")
                    .annotation("orchestration.atlan.com/allowSchedule", "true")
                    .annotation("orchestration.atlan.com/categories", "warehouse,miner")
                    .annotation("orchestration.atlan.com/dependentPackage", "")
                    .annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6482067592337")
                    .annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    .annotation(
                            "orchestration.atlan.com/icon",
                            "https://docs.snowflake.com/en/_images/logo-snowflake-sans-text.png")
                    .annotation(
                            "orchestration.atlan.com/logo",
                            "https://1amiydhcmj36tz3733v94f15-wpengine.netdna-ssl.com/wp-content/themes/snowflake/assets/img/logo-blue.svg")
                    .annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/snowflake-miner")
                    .annotation("orchestration.atlan.com/name", "Snowflake Miner")
                    .annotation("package.argoproj.io/author", "Atlan")
                    .annotation(
                            "package.argoproj.io/description",
                            "Package to mine query history data from Snowflake and store it for further processing. The data mined will be used for generating lineage and usage metrics.")
                    .annotation(
                            "package.argoproj.io/homepage",
                            "https://packages.atlan.com/-/web/detail/@atlan/snowflake-miner")
                    .annotation("package.argoproj.io/keywords", "[\"snowflake\",\"warehouse\",\"connector\",\"miner\"]")
                    .annotation("package.argoproj.io/name", "@atlan/snowflake-miner")
                    .annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    .annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    .annotation("package.argoproj.io/support", "support@atlan.com")
                    .annotation("orchestration.atlan.com/atlanName", PREFIX + "-" + epoch);
        }

        private Map<String, String> params() {
            return Map.ofEntries(Map.entry("control-config-strategy", "default"), Map.entry("single-session", "false"));
        }
    }
}
