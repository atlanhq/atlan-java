/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about an attribute attached to a tag in a source system.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class SourceTagAttribute extends AtlanStruct {

    public static final String TYPE_NAME = "SourceTagAttribute";

    /** Fixed typeName for SourceTagAttribute. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /**
     * Quickly create a new SourceTagAttribute.
     * @param tagAttributeKey Attribute key, for example "allowedValues" or "enabled".
     * @param tagAttributeValue Attribute value, for example ["Private", "PII"] for allowedValues, or "true" for enabled.
     * @param tagAttributeProperties Properties associated with the attribute.
     * @return a SourceTagAttribute with the provided information
     */
    public static SourceTagAttribute of(
            String tagAttributeKey, String tagAttributeValue, Map<String, String> tagAttributeProperties) {
        return SourceTagAttribute.builder()
                .tagAttributeKey(tagAttributeKey)
                .tagAttributeValue(tagAttributeValue)
                .tagAttributeProperties(tagAttributeProperties)
                .build();
    }

    /** Attribute key, for example "allowedValues" or "enabled". */
    String tagAttributeKey;

    /** Attribute value, for example ["Private", "PII"] for allowedValues, or "true" for enabled. */
    String tagAttributeValue;

    /** Properties associated with the attribute. */
    Map<String, String> tagAttributeProperties;
}
