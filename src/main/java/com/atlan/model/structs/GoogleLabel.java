/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.core.AtlanObject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about a Google label.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GoogleLabel extends AtlanObject {

    /**
     * Quickly create a new GoogleLabel.
     * @param googleLabelKey Key of the Google label.
     * @param googleLabelValue Value for the Google label.
     * @return a GoogleLabel with the provided information
     */
    public static GoogleLabel of(String googleLabelKey, String googleLabelValue) {
        return GoogleLabel.builder()
                .googleLabelKey(googleLabelKey)
                .googleLabelValue(googleLabelValue)
                .build();
    }

    /** Key of the Google label. */
    String googleLabelKey;

    /** Value for the Google label. */
    String googleLabelValue;
}
