/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about an AWS CloudWatch metric.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AwsCloudWatchMetric extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AwsCloudWatchMetric";

    /** Fixed typeName for AwsCloudWatchMetric. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Name of the AWS CloudWatch metric. */
    String awsCloudWatchMetricName;

    /** Scope of the AWS CloudWatch metric. */
    String awsCloudWatchMetricScope;

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

    public abstract static class AwsCloudWatchMetricBuilder<
                    C extends AwsCloudWatchMetric, B extends AwsCloudWatchMetricBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
