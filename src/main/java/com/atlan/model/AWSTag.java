/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model;

import com.atlan.model.core.AtlanObject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about an AWS tag.
 */
@Data
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AWSTag extends AtlanObject {
    /**
     * Quickly create a new AWS tag.
     * @param key key of the tag
     * @param value value of the tag
     * @return an AWS tag with the provided key and value
     */
    public static AWSTag of(String key, String value) {
        return AWSTag.builder().awsTagKey(key).awsTagValue(value).build();
    }

    /** Key of the tag. */
    String awsTagKey;

    /** Value of the tag. */
    String awsTagValue;
}
