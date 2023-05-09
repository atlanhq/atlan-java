/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.core.AtlanObject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about the value attached to a tag in a source system.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class SourceTagAttachmentValue extends AtlanObject {

    /**
     * Quickly create a new SourceTagAttachmentValue.
     * @param tagAttachmentKey Attachment key, for example "has_pii" or "type_pii".
     * @param tagAttachmentValue Attachment value, for example "true" or "email".
     * @return a SourceTagAttachmentValue with the provided information
     */
    public static SourceTagAttachmentValue of(String tagAttachmentKey, String tagAttachmentValue) {
        return SourceTagAttachmentValue.builder()
                .tagAttachmentKey(tagAttachmentKey)
                .tagAttachmentValue(tagAttachmentValue)
                .build();
    }

    /** Attachment key, for example "has_pii" or "type_pii". */
    String tagAttachmentKey;

    /** Attachment value, for example "true" or "email". */
    String tagAttachmentValue;
}
