/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import java.util.List;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Error handling strategy within an Atlan application.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AtlanAppErrorHandling extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AtlanAppErrorHandling";

    /** Fixed typeName for AtlanAppErrorHandling. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Initial interval for the error handling strategy. */
    String atlanAppErrorHandlingInitialInterval;

    /** Backoff coefficient for the error handling strategy. */
    Double atlanAppErrorHandlingBackoffCoefficient;

    /** Maximum interval for the error handling strategy. */
    String atlanAppErrorHandlingMaximumInterval;

    /** Maximum attempts for the error handling strategy. */
    Integer atlanAppErrorHandlingMaximumAttempts;

    /** Non-retryable error types for the error handling strategy. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<String> atlanAppErrorHandlingNonRetryableErrorTypes;

    /**
     * Quickly create a new AtlanAppErrorHandling.
     * @param atlanAppErrorHandlingInitialInterval Initial interval for the error handling strategy.
     * @param atlanAppErrorHandlingBackoffCoefficient Backoff coefficient for the error handling strategy.
     * @param atlanAppErrorHandlingMaximumInterval Maximum interval for the error handling strategy.
     * @param atlanAppErrorHandlingMaximumAttempts Maximum attempts for the error handling strategy.
     * @param atlanAppErrorHandlingNonRetryableErrorTypes Non-retryable error types for the error handling strategy.
     * @return a AtlanAppErrorHandling with the provided information
     */
    public static AtlanAppErrorHandling of(
            String atlanAppErrorHandlingInitialInterval,
            Double atlanAppErrorHandlingBackoffCoefficient,
            String atlanAppErrorHandlingMaximumInterval,
            Integer atlanAppErrorHandlingMaximumAttempts,
            List<String> atlanAppErrorHandlingNonRetryableErrorTypes) {
        return AtlanAppErrorHandling.builder()
                .atlanAppErrorHandlingInitialInterval(atlanAppErrorHandlingInitialInterval)
                .atlanAppErrorHandlingBackoffCoefficient(atlanAppErrorHandlingBackoffCoefficient)
                .atlanAppErrorHandlingMaximumInterval(atlanAppErrorHandlingMaximumInterval)
                .atlanAppErrorHandlingMaximumAttempts(atlanAppErrorHandlingMaximumAttempts)
                .atlanAppErrorHandlingNonRetryableErrorTypes(atlanAppErrorHandlingNonRetryableErrorTypes)
                .build();
    }

    public abstract static class AtlanAppErrorHandlingBuilder<
                    C extends AtlanAppErrorHandling, B extends AtlanAppErrorHandlingBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
