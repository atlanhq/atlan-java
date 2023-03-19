/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.core.AtlanObject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about a Google label.
 */
@Getter
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class GoogleLabel extends AtlanObject {
    /**
     * Quickly create a new Google label.
     * @param key key of the label
     * @param value value of the label
     * @return a Google label with the provided key and value
     */
    public static GoogleLabel of(String key, String value) {
        return GoogleLabel.builder().googleLabelKey(key).googleLabelValue(value).build();
    }

    /** Key of the label. */
    String googleLabelKey;

    /** Value of the label. */
    String googleLabelValue;
}
