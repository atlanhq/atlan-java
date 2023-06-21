/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.events;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.atlan.Atlan;
import com.atlan.model.events.AtlanEvent;
import com.atlan.model.events.AwsEventWrapper;
import com.atlan.serde.Serde;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractLambdaHandler implements AtlanEventHandler, RequestStreamHandler {

    // Set up Atlan connectivity through environment variables
    static {
        Atlan.setBaseUrl(System.getenv("ATLAN_BASE_URL"));
        Atlan.setApiToken(System.getenv("ATLAN_API_KEY"));
    }

    private static final String SIGNING_SECRET = System.getenv("SIGNING_SECRET");

    /**
     * Implement the logic for how the Atlan event should be processed through overriding this method.
     *
     * @param event the event payload, from Atlan
     * @param context context in which the event was received by the AWS Lambda function
     * @throws IOException on any error during processing of the event
     */
    public abstract void processEvent(AtlanEvent event, Context context) throws IOException;

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        try (input;
                output) {
            // Parse the AWS payload... (for some reason readValue directly on InputStream just times out)
            String request = getRequestAsString(input);
            AwsEventWrapper wrapper = Serde.mapper.readValue(request, AwsEventWrapper.class);
            // Pull out the embedded Atlan request...
            String body = wrapper.getBody();
            if (AtlanEventHandler.isValidationRequest(body)) {
                // If this is a validation request, just let it succeed
                // (Note: this must be done BEFORE the signing secret is validated)
                log.info("Matches a validation request — doing nothing and succeeding.");
            } else {
                // Otherwise, finally:
                // 1. Validate the request by its signing key
                if (AtlanEventHandler.validSignature(SIGNING_SECRET, wrapper.getHeaders())) {
                    // pull out the embedded Atlan event and delegate it to be processed...
                    try {
                        processEvent(AtlanEventHandler.getAtlanEvent(body), context);
                    } catch (IOException e) {
                        log.error("Unable to process the event.", e);
                    }
                } else {
                    throw new IOException("Invalid signing secret received — will not process this request.");
                }
            }
        }
    }

    /**
     * Convert the input stream into a string.
     *
     * @param input the input stream containing the request payload
     * @return a string detailing the request payload
     * @throws IOException on any issues reading the input stream
     */
    private static String getRequestAsString(InputStream input) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining());
        }
    }
}
