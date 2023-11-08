/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.model.enums.AtlanPackageType;
import com.atlan.model.workflow.*;
import java.util.Collections;
import java.util.List;

public class SnowflakeMiner extends AbstractMiner {

    public static final String PREFIX = AtlanPackageType.SNOWFLAKE_MINER.getValue();

    /**
     * Builds the minimal object necessary to create a new miner for Snowflake,
     * using source extraction, the default database and default configuration
     * (mining the previous 2 weeks of query history).
     *
     * @param connectionName name of the connection to mine
     * @return the minimal workflow necessary to mine Snowflake
     */
    public static Workflow defaultSource(String connectionName) {
        return defaultSource(connectionName, getStartEpoch(14));
    }

    /**
     * Builds the minimal object necessary to create a new miner for Snowflake.
     *
     * @param connectionName name of the connection to create
     * @param startEpoch timestamp for the earliest query history to mine, in seconds
     * @return the minimal workflow necessary to mine Snowflake
     */
    public static Workflow defaultSource(String connectionName, long startEpoch) {

        String epoch = getEpoch();

        WorkflowParameters.WorkflowParametersBuilder<?, ?> argsBuilder = WorkflowParameters.builder()
                .parameter(NameValuePair.of("connection-qualified-name", connectionName))
                .parameter(NameValuePair.of("extraction-method", "query_history"))
                .parameter(NameValuePair.of("miner-start-time-epoch", "" + startEpoch))
                .parameter(NameValuePair.of("snowflake-database", "default"))
                .parameter(NameValuePair.of("database-name", "SNOWFLAKE"))
                .parameter(NameValuePair.of("schema-name", "ACCOUNT_USAGE"))
                .parameter(NameValuePair.of("sql-json-key", "QUERY_TEXT"))
                .parameter(NameValuePair.of("catalog-json-key", "DATABASE_NAME"))
                .parameter(NameValuePair.of("schema-json-key", "SCHEMA_NAME"))
                .parameter(NameValuePair.of("session-json-key", "SESSION_ID"))
                .parameter(NameValuePair.of("single-session", "false"))
                .parameter(NameValuePair.of("control-config-strategy", "default"));

        String atlanName = PREFIX + "-" + epoch;
        return Workflow.builder()
                .metadata(WorkflowMetadata.builder()
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
                                "orchestration.atlan.com/docsUrl",
                                "https://ask.atlan.com/hc/en-us/articles/6482067592337")
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
                        .annotation(
                                "package.argoproj.io/keywords", "[\"snowflake\",\"warehouse\",\"connector\",\"miner\"]")
                        .annotation("package.argoproj.io/name", "@atlan/snowflake-miner")
                        .annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                        .annotation(
                                "package.argoproj.io/repository",
                                "git+https://github.com/atlanhq/marketplace-packages.git")
                        .annotation("package.argoproj.io/support", "support@atlan.com")
                        .annotation("orchestration.atlan.com/atlanName", atlanName)
                        .name(atlanName)
                        .namespace("default")
                        .build())
                .spec(WorkflowSpec.builder()
                        .templates(List.of(WorkflowTemplate.builder()
                                .name("main")
                                .dag(WorkflowDAG.builder()
                                        .task(WorkflowTask.builder()
                                                .name("run")
                                                .arguments(argsBuilder.build())
                                                .templateRef(WorkflowTemplateRef.builder()
                                                        .name(PREFIX)
                                                        .template("main")
                                                        .clusterScope(true)
                                                        .build())
                                                .build())
                                        .build())
                                .build()))
                        .entrypoint("main")
                        .workflowMetadata(WorkflowMetadata.builder()
                                .annotation("package.argoproj.io/name", "@atlan/snowflake-miner")
                                .build())
                        .build())
                .payload(Collections.emptyList())
                .build();
    }
}
