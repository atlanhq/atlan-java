/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.events;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.events.AtlanEvent;
import io.numaproj.numaflow.function.handlers.MapHandler;
import io.numaproj.numaflow.function.interfaces.Datum;
import io.numaproj.numaflow.function.types.Message;
import io.numaproj.numaflow.function.types.MessageList;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for event handlers.
 */
@Getter
@Slf4j
public abstract class AbstractNumaflowHandler extends MapHandler {

    public static final String FAILURE = "failure";
    public static final String SUCCESS = "success";

    // Set up Atlan connectivity through environment variables
    static {
        Atlan.setBaseUrl(System.getenv("ATLAN_BASE_URL"));
        Atlan.setApiToken(System.getenv("ATLAN_API_KEY"));
    }

    private final AtlanEventHandler handler;

    public AbstractNumaflowHandler(AtlanEventHandler handler) {
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
     * @param keys the Numaflow keys for the message
     * @param data the Numanflow message itself
     * @return an array of messages that can be passed to further vertexes in the pipeline, often produced by one of the helper methods
     */
    public MessageList processEvent(AtlanEvent event, String[] keys, Datum data) {
        try {
            if (!handler.validatePrerequisites(event, log)) {
                return failed(keys, data);
            }
        } catch (AtlanException e) {
            log.error("Unable to validate prerequisites, failing.", e);
            return failed(keys, data);
        }
        try {
            Asset current = handler.getCurrentState(event.getPayload().getAsset(), log);
            Asset updated = handler.calculateChanges(current, log);
            if (handler.hasChanges(current, updated, log)) {
                handler.upsertChanges(updated, log);
                return succeeded(keys, data);
            } else {
                return drop();
            }
        } catch (AtlanException e) {
            log.error(
                    "Unable to update Atlan asset: {}",
                    event.getPayload().getAsset().getQualifiedName(),
                    e);
            return failed(keys, data);
        }
    }

    /** {@inheritDoc} */
    @Override
    public MessageList processMessage(String[] keys, Datum data) {
        try {
            return processEvent(getAtlanEvent(data), keys, data);
        } catch (IOException e) {
            log.error("Unable to deserialize event: {}", new String(data.getValue(), StandardCharsets.UTF_8), e);
            return failed(keys, data.getValue());
        }
    }

    /**
     * Translate the Numaflow message into an Atlan event object.
     *
     * @param data the Numaflow message
     * @return an Atlan event object representation of the message
     * @throws IOException if an Atlan event cannot be parsed from the message
     */
    protected static AtlanEvent getAtlanEvent(Datum data) throws IOException {
        return AtlanEventHandler.getAtlanEvent(data.getValue());
    }

    /**
     * Route the message as failed.
     *
     * @param keys the Numaflow keys for the message
     * @param data the Numaflow message
     * @return a message list indicating the message failed to be processed
     */
    protected static MessageList failed(String keys[], Datum data) {
        return failed(keys, data.getValue());
    }

    /**
     * Route the message as failed.
     *
     * @param keys the Numaflow keys for the message
     * @param data the Numaflow message
     * @return a message list indicating the message failed to be processed
     */
    protected static MessageList failed(String keys[], byte[] data) {
        log.info("Routing to: {}", FAILURE);
        return MessageList.newBuilder()
                .addMessage(new Message(data, keys, new String[] {FAILURE}))
                .build();
    }

    /**
     * Route the message as succeeded.
     *
     * @param keys the Numaflow keys for the message
     * @param data the Numaflow message
     * @return a message list indicating the message was successfully processed
     */
    protected static MessageList succeeded(String keys[], Datum data) {
        return succeeded(keys, data.getValue());
    }

    /**
     * Route the message as succeeded.
     *
     * @param keys the Numaflow keys for the message
     * @param data the Numaflow message
     * @return a message list indicating the message was successfully processed
     */
    protected static MessageList succeeded(String keys[], byte[] data) {
        log.info("Routing to: {}", SUCCESS);
        return MessageList.newBuilder()
                .addMessage(new Message(data, keys, new String[] {SUCCESS}))
                .build();
    }

    /**
     * Route the message forward, as-is.
     *
     * @param data the Numaflow message
     * @return a message list indicating the message should be forwarded as-is
     */
    protected static MessageList forward(Datum data) {
        return forward(data.getValue());
    }

    /**
     * Route the message forward, as-is.
     *
     * @param data the Numaflow message
     * @return a message list indicating the message should be forwarded as-is
     */
    protected static MessageList forward(byte[] data) {
        return MessageList.newBuilder().addMessage(new Message(data)).build();
    }

    /**
     * Drop the message. Mostly this should be used when receiving an event that is
     * the result of this handler taking an action on a previous event.
     * (Without this, we could have an infinite loop of that action being applied
     * over and over again.)
     *
     * @return a message list indicating the message can be safely ignored
     */
    protected static MessageList drop() {
        return MessageList.newBuilder().addMessage(Message.toDrop()).build();
    }
}
