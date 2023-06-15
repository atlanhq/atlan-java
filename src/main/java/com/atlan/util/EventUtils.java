/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.util;

import com.atlan.model.events.AtlanEvent;
import com.atlan.model.events.WebhookValidationRequest;
import com.atlan.serde.Serde;
import java.io.IOException;

public class EventUtils {

    public static AtlanEvent getAtlanEvent(String payload) throws IOException {
        return Serde.mapper.readValue(payload, AtlanEvent.class);
    }

    public static AtlanEvent getAtlanEvent(byte[] payload) throws IOException {
        return Serde.mapper.readValue(payload, AtlanEvent.class);
    }

    public static WebhookValidationRequest getValidationRequest(String payload) throws IOException {
        return Serde.mapper.readValue(payload, WebhookValidationRequest.class);
    }

    public static boolean validateRequest() throws IOException {}
}
