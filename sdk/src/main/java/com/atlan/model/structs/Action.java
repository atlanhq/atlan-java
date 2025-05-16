/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Action for the task
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class Action extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Action";

    /** Fixed typeName for Action. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** url to call to take action */
    String taskActionFulfillmentUrl;

    /** method to call to take action */
    String taskActionFulfillmentMethod;

    /** payload to send to the fulfillment endpoint */
    String taskActionFulfillmentPayload;

    /** Display text for the UI component */
    String taskActionDisplayText;

    /**
     * Quickly create a new Action.
     * @param taskActionFulfillmentUrl url to call to take action
     * @param taskActionFulfillmentMethod method to call to take action
     * @param taskActionFulfillmentPayload payload to send to the fulfillment endpoint
     * @param taskActionDisplayText Display text for the UI component
     * @return a Action with the provided information
     */
    public static Action of(
            String taskActionFulfillmentUrl,
            String taskActionFulfillmentMethod,
            String taskActionFulfillmentPayload,
            String taskActionDisplayText) {
        return Action.builder()
                .taskActionFulfillmentUrl(taskActionFulfillmentUrl)
                .taskActionFulfillmentMethod(taskActionFulfillmentMethod)
                .taskActionFulfillmentPayload(taskActionFulfillmentPayload)
                .taskActionDisplayText(taskActionDisplayText)
                .build();
    }

    public abstract static class ActionBuilder<C extends Action, B extends ActionBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
