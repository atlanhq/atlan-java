/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about a source tag's attributes.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("cast")
public class SourceTagAttribute extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SourceTagAttribute";

    /** Fixed typeName for SourceTagAttribute. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Attribute key, for example: 'allowedValues' or 'enabled'. */
    String tagAttributeKey;

    /** Attribute value, for example: '["Private", "PII"]' for allowedValues key or 'true' for enabled key. */
    String tagAttributeValue;

    /** Properties of the attribute. */
    @Builder.Default
    Map<String, String> tagAttributeProperties = null;

    /**
     * Quickly create a new SourceTagAttribute.
     * @param tagAttributeKey Attribute key, for example: 'allowedValues' or 'enabled'.
     * @param tagAttributeValue Attribute value, for example: '["Private", "PII"]' for allowedValues key or 'true' for enabled key.
     * @param tagAttributeProperties Properties of the attribute.
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
}
