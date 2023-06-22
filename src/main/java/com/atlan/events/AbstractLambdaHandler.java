/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.events;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.events.AtlanEvent;
import com.atlan.model.events.AwsEventWrapper;
import com.atlan.serde.Serde;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractLambdaHandler implements RequestStreamHandler {

    // Set up Atlan connectivity through environment variables
    static {
        Atlan.setBaseUrl(System.getenv("ATLAN_BASE_URL"));
        Atlan.setApiToken(System.getenv("ATLAN_API_KEY"));
    }

    private static final String SIGNING_SECRET = System.getenv("SIGNING_SECRET");

    private final AtlanEventHandler handler;

    public AbstractLambdaHandler(AtlanEventHandler handler) {
        this.handler = handler;
    }

    /**
     * Handle the Atlan event using the standard 5-step flow:
     * 1. Validate prerequisites.
     * 2. Retrieve current state of the asset.
     * 3. Apply any changes (in-memory).
     * 4. Determine whether any changes actually would be applied (idempotency).
     * 5. Apply changes back to Atlan (only if (4) shows there are changes to apply).
     *
     * @param event the event payload, from Atlan
     * @param context context in which the event was received by the AWS Lambda function
     * @throws IOException on any error during processing of the event
     */
    public void processEvent(AtlanEvent event, Context context) throws IOException {
        boolean proceed;
        try {
            proceed = handler.validatePrerequisites(event, log);
        } catch (AtlanException e) {
            throw new IOException("Unable to validate prerequisites, failing.", e);
        }
        if (proceed) {
            try {
                Asset current = handler.getCurrentState(event.getPayload().getAsset(), log);
                Collection<Asset> updated = handler.calculateChanges(current, log);
                if (!updated.isEmpty()) {
                    handler.upsertChanges(updated, log);
                }
            } catch (AtlanException e) {
                throw new IOException(
                        "Unable to update Atlan asset: "
                                + event.getPayload().getAsset().getQualifiedName(),
                        e);
            }
        } else {
            throw new IOException("Prerequisites failed, will not proceed with processing the event.");
        }
    }

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
