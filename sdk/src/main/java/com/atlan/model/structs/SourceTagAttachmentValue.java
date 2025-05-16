/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about the value of a source tag's attachment to an asset.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class SourceTagAttachmentValue extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SourceTagAttachmentValue";

    /** Fixed typeName for SourceTagAttachmentValue. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Attachment key, for example: 'has_pii' or 'type_pii'. */
    String tagAttachmentKey;

    /** Attachment value, for example: 'true' or 'email'. */
    String tagAttachmentValue;

    /**
     * Quickly create a new SourceTagAttachmentValue.
     * @param tagAttachmentKey Attachment key, for example: 'has_pii' or 'type_pii'.
     * @param tagAttachmentValue Attachment value, for example: 'true' or 'email'.
     * @return a SourceTagAttachmentValue with the provided information
     */
    public static SourceTagAttachmentValue of(String tagAttachmentKey, String tagAttachmentValue) {
        return SourceTagAttachmentValue.builder()
                .tagAttachmentKey(tagAttachmentKey)
                .tagAttachmentValue(tagAttachmentValue)
                .build();
    }

    public abstract static class SourceTagAttachmentValueBuilder<
                    C extends SourceTagAttachmentValue, B extends SourceTagAttachmentValueBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
