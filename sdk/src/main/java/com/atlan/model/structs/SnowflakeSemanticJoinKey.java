/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * One column pair in a Snowflake semantic view join, from the foreign-key column to the column it references.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class SnowflakeSemanticJoinKey extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SnowflakeSemanticJoinKey";

    /** Fixed typeName for SnowflakeSemanticJoinKey. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Column on the logical table holding the foreign key. */
    String snowflakeSemanticJoinKeySource;

    /** Column on the referenced logical table. */
    String snowflakeSemanticJoinKeyTarget;

    /**
     * Quickly create a new SnowflakeSemanticJoinKey.
     * @param snowflakeSemanticJoinKeySource Column on the logical table holding the foreign key.
     * @param snowflakeSemanticJoinKeyTarget Column on the referenced logical table.
     * @return a SnowflakeSemanticJoinKey with the provided information
     */
    public static SnowflakeSemanticJoinKey of(
            String snowflakeSemanticJoinKeySource, String snowflakeSemanticJoinKeyTarget) {
        return SnowflakeSemanticJoinKey.builder()
                .snowflakeSemanticJoinKeySource(snowflakeSemanticJoinKeySource)
                .snowflakeSemanticJoinKeyTarget(snowflakeSemanticJoinKeyTarget)
                .build();
    }

    public abstract static class SnowflakeSemanticJoinKeyBuilder<
                    C extends SnowflakeSemanticJoinKey, B extends SnowflakeSemanticJoinKeyBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
