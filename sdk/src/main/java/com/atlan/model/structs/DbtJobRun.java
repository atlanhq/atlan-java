/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about a dbt job run.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DbtJobRun extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtJobRun";

    /** Fixed typeName for DbtJobRun. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Unique Id of the DBT job */
    String dbtJobId;

    /** Name of the DBT job instance */
    String dbtJobName;

    /** Unique environment id for the DBT job */
    String dbtEnvironmentId;

    /** Environment name where the DBT job runs */
    String dbtEnvironmentName;

    /** Unique id for the DBT job run */
    String dbtJobRunId;

    /** Dbt job instance run completion timestamp */
    Long dbtJobRunCompletedAt;

    /** Status of the dbt job run */
    String dbtJobRunStatus;

    /** Status of the dbt test executed by current job run */
    String dbtTestRunStatus;

    /** Status of the dbt model executed by current job run */
    String dbtModelRunStatus;

    /** compiled sql executed by a dbt model/test triggered by this dbt job run instance */
    String dbtCompiledSQL;

    /** compiled code executed by a dbt model/test triggered by this dbt job run instance */
    String dbtCompiledCode;

    /**
     * Quickly create a new DbtJobRun.
     * @param dbtJobId Unique Id of the DBT job
     * @param dbtJobName Name of the DBT job instance
     * @param dbtEnvironmentId Unique environment id for the DBT job
     * @param dbtEnvironmentName Environment name where the DBT job runs
     * @param dbtJobRunId Unique id for the DBT job run
     * @param dbtJobRunCompletedAt Dbt job instance run completion timestamp
     * @param dbtJobRunStatus Status of the dbt job run
     * @param dbtTestRunStatus Status of the dbt test executed by current job run
     * @param dbtModelRunStatus Status of the dbt model executed by current job run
     * @param dbtCompiledSQL compiled sql executed by a dbt model/test triggered by this dbt job run instance
     * @param dbtCompiledCode compiled code executed by a dbt model/test triggered by this dbt job run instance
     * @return a DbtJobRun with the provided information
     */
    public static DbtJobRun of(
            String dbtJobId,
            String dbtJobName,
            String dbtEnvironmentId,
            String dbtEnvironmentName,
            String dbtJobRunId,
            Long dbtJobRunCompletedAt,
            String dbtJobRunStatus,
            String dbtTestRunStatus,
            String dbtModelRunStatus,
            String dbtCompiledSQL,
            String dbtCompiledCode) {
        return DbtJobRun.builder()
                .dbtJobId(dbtJobId)
                .dbtJobName(dbtJobName)
                .dbtEnvironmentId(dbtEnvironmentId)
                .dbtEnvironmentName(dbtEnvironmentName)
                .dbtJobRunId(dbtJobRunId)
                .dbtJobRunCompletedAt(dbtJobRunCompletedAt)
                .dbtJobRunStatus(dbtJobRunStatus)
                .dbtTestRunStatus(dbtTestRunStatus)
                .dbtModelRunStatus(dbtModelRunStatus)
                .dbtCompiledSQL(dbtCompiledSQL)
                .dbtCompiledCode(dbtCompiledCode)
                .build();
    }
}
