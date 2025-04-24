/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about a Google label.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class GoogleLabel extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "GoogleLabel";

    /** Fixed typeName for GoogleLabel. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Key of the Google label. */
    String googleLabelKey;

    /** Value for the Google label. */
    String googleLabelValue;

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
}
