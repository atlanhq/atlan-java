/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * A verified query defined on a Snowflake semantic view: a question paired with the SQL that answers it.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class SnowflakeSemanticVerifiedQuery extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SnowflakeSemanticVerifiedQuery";

    /** Fixed typeName for SnowflakeSemanticVerifiedQuery. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Name of the verified query in the semantic view. */
    String snowflakeSemanticVerifiedQueryName;

    /** Natural-language question the query answers. */
    String snowflakeSemanticVerifiedQueryQuestion;

    /** SQL that answers the question. */
    String snowflakeSemanticVerifiedQuerySql;

    /** Whether Cortex Analyst offers this question to new users as a suggestion (true) or not (false). */
    Boolean snowflakeSemanticVerifiedQueryUseAsOnboardingQuestion;

    /**
     * Quickly create a new SnowflakeSemanticVerifiedQuery.
     * @param snowflakeSemanticVerifiedQueryName Name of the verified query in the semantic view.
     * @param snowflakeSemanticVerifiedQueryQuestion Natural-language question the query answers.
     * @param snowflakeSemanticVerifiedQuerySql SQL that answers the question.
     * @param snowflakeSemanticVerifiedQueryUseAsOnboardingQuestion Whether Cortex Analyst offers this question to new users as a suggestion (true) or not (false).
     * @return a SnowflakeSemanticVerifiedQuery with the provided information
     */
    public static SnowflakeSemanticVerifiedQuery of(
            String snowflakeSemanticVerifiedQueryName,
            String snowflakeSemanticVerifiedQueryQuestion,
            String snowflakeSemanticVerifiedQuerySql,
            Boolean snowflakeSemanticVerifiedQueryUseAsOnboardingQuestion) {
        return SnowflakeSemanticVerifiedQuery.builder()
                .snowflakeSemanticVerifiedQueryName(snowflakeSemanticVerifiedQueryName)
                .snowflakeSemanticVerifiedQueryQuestion(snowflakeSemanticVerifiedQueryQuestion)
                .snowflakeSemanticVerifiedQuerySql(snowflakeSemanticVerifiedQuerySql)
                .snowflakeSemanticVerifiedQueryUseAsOnboardingQuestion(
                        snowflakeSemanticVerifiedQueryUseAsOnboardingQuestion)
                .build();
    }

    public abstract static class SnowflakeSemanticVerifiedQueryBuilder<
                    C extends SnowflakeSemanticVerifiedQuery, B extends SnowflakeSemanticVerifiedQueryBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
