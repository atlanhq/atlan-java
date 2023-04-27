/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.core.AtlanObject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about an AWS tag.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class AwsTag extends AtlanObject {

    /**
     * Quickly create a new AwsTag.
     * @param awsTagKey Key of the AWS tag.
     * @param awsTagValue Value for the AWS tag.
     * @return a AwsTag with the provided information
     */
    public static AwsTag of(String awsTagKey, String awsTagValue) {
        return AwsTag.builder().awsTagKey(awsTagKey).awsTagValue(awsTagValue).build();
    }

    /** Key of the AWS tag. */
    String awsTagKey;

    /** Value for the AWS tag. */
    String awsTagValue;
}
