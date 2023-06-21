/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.events;

import com.atlan.Atlan;
import com.atlan.model.events.AtlanEvent;
import io.numaproj.numaflow.function.handlers.MapHandler;
import io.numaproj.numaflow.function.interfaces.Datum;
import io.numaproj.numaflow.function.types.Message;
import io.numaproj.numaflow.function.types.MessageList;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for event handlers.
 */
@Slf4j
public abstract class AbstractNumaflowHandler extends MapHandler implements AtlanEventHandler {

    public static final String FAILURE = "failure";
    public static final String SUCCESS = "success";

    // Set up Atlan connectivity through environment variables
    static {
        Atlan.setBaseUrl(System.getenv("ATLAN_BASE_URL"));
        Atlan.setApiToken(System.getenv("ATLAN_API_KEY"));
    }

    /**
     * Implement the logic for how the Atlan event should be processed through overriding this method.
     *
     * @param event the event payload, from Atlan
     * @param keys unique key of the event, for use in returned message list
     * @return an array of messages that can be passed to further vertexes in the pipeline, often produced by one of the helper methods
     * @see #succeeded(String[], String)
     * @see #failed(String[], String)
     * @see #forward(String)
     * @see #drop()
     */
    public abstract MessageList processEvent(AtlanEvent event, String[] keys);

    /** {@inheritDoc} */
    @Override
    public MessageList processMessage(String[] keys, Datum data) {
        try {
            return processEvent(getAtlanEvent(data), keys);
        } catch (IOException e) {
            log.error("Unable to deserialize event: {}", new String(data.getValue(), StandardCharsets.UTF_8), e);
            return failed(keys, new String(data.getValue()));
        }
    }

    /**
     * Translate the Numaflow message into an Atlan event object.
     *
     * @param data the Numaflow message
     * @return an Atlan event object representation of the message
     * @throws IOException if an Atlan event cannot be parsed from the message
     */
    static AtlanEvent getAtlanEvent(Datum data) throws IOException {
        return AtlanEventHandler.getAtlanEvent(data.getValue());
    }

    /**
     * Route the message as failed.
     *
     * @param keys the Numaflow keys for the message
     * @param data the Numaflow message
     * @return a message list indicating the message failed to be processed
     */
    static MessageList failed(String keys[], String data) {
        log.info("Routing to: {}", FAILURE);
        return MessageList.newBuilder()
                .addMessage(new Message(data.getBytes(StandardCharsets.UTF_8), keys, new String[] {FAILURE}))
                .build();
    }

    /**
     * Route the message as succeeded.
     *
     * @param keys the Numaflow keys for the message
     * @param data the Numaflow message
     * @return a message list indicating the message was successfully processed
     */
    static MessageList succeeded(String keys[], String data) {
        log.info("Routing to: {}", SUCCESS);
        return MessageList.newBuilder()
                .addMessage(new Message(data.getBytes(StandardCharsets.UTF_8), keys, new String[] {SUCCESS}))
                .build();
    }

    /**
     * Route the message forward, as-is.
     *
     * @param data the Numaflow message
     * @return a message list indicating the message should be forwarded as-is
     */
    static MessageList forward(String data) {
        return MessageList.newBuilder()
                .addMessage(new Message(data.getBytes(StandardCharsets.UTF_8)))
                .build();
    }

    /**
     * Drop the message. Mostly this should be used when receiving an event that is
     * the result of this handler taking an action on a previous event.
     * (Without this, we could have an infinite loop of that action being applied
     * over and over again.)
     *
     * @return a message list indicating the message can be safely ignored
     */
    static MessageList drop() {
        return MessageList.newBuilder().addMessage(Message.toDrop()).build();
    }
}
