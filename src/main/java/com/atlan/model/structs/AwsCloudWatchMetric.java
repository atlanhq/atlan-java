/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about an AWS CloudWatch metric.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class AwsCloudWatchMetric extends AtlanStruct {

    public static final String TYPE_NAME = "AwsCloudWatchMetric";

    /** Fixed typeName for AwsCloudWatchMetric. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /**
     * Quickly create a new AwsCloudWatchMetric.
     * @param awsCloudWatchMetricName Name of the AWS CloudWatch metric.
     * @param awsCloudWatchMetricScope Scope of the AWS CloudWatch metric.
     * @return a AwsCloudWatchMetric with the provided information
     */
    public static AwsCloudWatchMetric of(String awsCloudWatchMetricName, String awsCloudWatchMetricScope) {
        return AwsCloudWatchMetric.builder()
                .awsCloudWatchMetricName(awsCloudWatchMetricName)
                .awsCloudWatchMetricScope(awsCloudWatchMetricScope)
                .build();
    }

    /** Name of the AWS CloudWatch metric. */
    String awsCloudWatchMetricName;

    /** Scope of the AWS CloudWatch metric. */
    String awsCloudWatchMetricScope;
}
