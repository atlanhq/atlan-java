/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.core.AtlanObject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about a Google tag.
 */
@Getter
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class GoogleTag extends AtlanObject {
    /**
     * Quickly create a new Google tag.
     * @param key key of the tag
     * @param value value of the tag
     * @return a Google tag with the provided key and value
     */
    public static GoogleTag of(String key, String value) {
        return GoogleTag.builder().googleTagKey(key).googleTagValue(value).build();
    }

    /** Key of the tag. */
    String googleTagKey;

    /** Value of the tag. */
    String googleTagValue;
}
