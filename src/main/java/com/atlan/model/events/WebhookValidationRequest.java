/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.events;

import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Request that will be sent when a webhook is created or updated in Atlan, to validate
 * that the endpoint defined for the webhook is capable of receiving and responding to events.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class WebhookValidationRequest extends AtlanObject {
    private static final String EXPECTED_PAYLOAD = "Hello, humans of data! It worked. Excited to see what you build!";

    @JsonProperty("atlan-webhook")
    String atlanWebhook;
}
