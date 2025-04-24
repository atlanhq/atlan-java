/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about an AWS tag.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AwsTag extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AwsTag";

    /** Fixed typeName for AwsTag. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Key of the AWS tag. */
    String awsTagKey;

    /** Value for the AWS tag. */
    String awsTagValue;

    /**
     * Quickly create a new AwsTag.
     * @param awsTagKey Key of the AWS tag.
     * @param awsTagValue Value for the AWS tag.
     * @return a AwsTag with the provided information
     */
    public static AwsTag of(String awsTagKey, String awsTagValue) {
        return AwsTag.builder().awsTagKey(awsTagKey).awsTagValue(awsTagValue).build();
    }
}
