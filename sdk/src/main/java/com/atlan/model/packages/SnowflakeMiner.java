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
        return _internal()
                .setup(PREFIX, "@atlan/snowflake-miner")
                ._parameter("connection-qualified-name", connectionQualifiedName);
    }

    public abstract static class SnowflakeMinerBuilder<C extends SnowflakeMiner, B extends SnowflakeMinerBuilder<C, B>>
            extends AbstractMinerBuilder<C, B> {

        /**
         * Set up the miner to extract directly from Snowflake, using the default database.
         *
         * @param startEpoch date and time from which to start mining, as an epoch
         * @return the builder, set up to extract directly from Snowflake using the default database
         */
        public B direct(long startEpoch) {
            return this._parameter("snowflake-database", "default")
                    ._parameter("extraction-method", "query_history")
                    ._parameter("miner-start-time-epoch", "" + startEpoch);
        }

        /**
         * Set up the miner to extract directly from Snowflake, using a cloned database.
         *
         * @param database name of the database to extract from
         * @param schema name of the schema to extract from
         * @param startEpoch date and time from which to start mining, as an epoch
         * @return the builder, set up to extract directly from Snowflake using a cloned database
         */
        public B direct(String database, String schema, long startEpoch) {
            return this._parameter("database-name", database)
                    ._parameter("schema-name", schema)
                    ._parameter("extraction-method", "query_history")
                    ._parameter("miner-start-time-epoch", "" + startEpoch);
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
        public B s3(
                String s3Bucket,
                String s3Prefix,
                String queryKey,
                String defaultDatabase,
                String defaultSchema,
                String sessionId) {
            return this._parameter("extraction-method", "s3")
                    ._parameter("extraction-s3-bucket", s3Bucket)
                    ._parameter("extraction-s3-prefix", s3Prefix)
                    ._parameter("sql-json-key", queryKey)
                    ._parameter("catalog-json-key", defaultDatabase)
                    ._parameter("schema-json-key", defaultSchema)
                    ._parameter("session-json-key", sessionId);
        }

        /**
         * Defines users who should be excluded when calculating usage metrics for assets (for example, system accounts).
         *
         * @param users list of users to exclude when calculating usage metrics
         * @return the builder, set to exclude the specified users from usage metrics
         * @throws InvalidRequestException in the unlikely event the provided list cannot be translated
         */
        public B excludeUsers(List<String> users) throws InvalidRequestException {
            try {
                return this._parameter(
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
        public B nativeLineage(boolean enabled) {
            return this._parameter("control-config-strategy", "custom")
                    ._parameter("native-lineage-active", "" + enabled);
        }

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        @Override
        protected B metadata() {
            return this._label("orchestration.atlan.com/certified", "true")
                    ._label("orchestration.atlan.com/source", "snowflake")
                    ._label("orchestration.atlan.com/sourceCategory", "warehouse")
                    ._label("orchestration.atlan.com/type", "miner")
                    ._label("orchestration.atlan.com/verified", "true")
                    ._label("package.argoproj.io/installer", "argopm")
                    ._label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hsnowflake-miner")
                    ._label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    // .label("orchestration.atlan.com/default-snowflake-" + epoch, "true")
                    ._label("orchestration.atlan.com/atlan-ui", "true")
                    ._annotation("orchestration.atlan.com/allowSchedule", "true")
                    ._annotation("orchestration.atlan.com/categories", "warehouse,miner")
                    ._annotation("orchestration.atlan.com/dependentPackage", "")
                    ._annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6482067592337")
                    ._annotation("orchestration.atlan.com/emoji", "\uD83D\uDE80")
                    ._annotation(
                            "orchestration.atlan.com/icon",
                            "https://docs.snowflake.com/en/_images/logo-snowflake-sans-text.png")
                    ._annotation(
                            "orchestration.atlan.com/logo",
                            "https://1amiydhcmj36tz3733v94f15-wpengine.netdna-ssl.com/wp-content/themes/snowflake/assets/img/logo-blue.svg")
                    ._annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/snowflake-miner")
                    ._annotation("orchestration.atlan.com/name", "Snowflake Miner")
                    ._annotation("package.argoproj.io/author", "Atlan")
                    ._annotation(
                            "package.argoproj.io/description",
                            "Package to mine query history data from Snowflake and store it for further processing. The data mined will be used for generating lineage and usage metrics.")
                    ._annotation(
                            "package.argoproj.io/homepage",
                            "https://packages.atlan.com/-/web/detail/@atlan/snowflake-miner")
                    ._annotation(
                            "package.argoproj.io/keywords", "[\"snowflake\",\"warehouse\",\"connector\",\"miner\"]")
                    ._annotation("package.argoproj.io/name", "@atlan/snowflake-miner")
                    ._annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    ._annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    ._annotation("package.argoproj.io/support", "support@atlan.com")
                    ._annotation("orchestration.atlan.com/atlanName", PREFIX + "-" + epoch)
                    ._parameters(Map.ofEntries(
                            Map.entry("control-config-strategy", "default"), Map.entry("single-session", "false")));
        }
    }
}
