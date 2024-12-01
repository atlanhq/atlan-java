/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.events

import com.atlan.events.AtlanEventHandler
import com.atlan.exception.AtlanException
import com.atlan.model.events.AtlanEvent
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.numaproj.numaflow.mapper.Datum
import io.numaproj.numaflow.mapper.Mapper
import io.numaproj.numaflow.mapper.Message
import io.numaproj.numaflow.mapper.MessageList
import mu.KotlinLogging
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * Base class for event handlers.
 *
 * @param handler the handler that defines actual processing logic
 */
abstract class AbstractNumaflowHandler(private val handler: AtlanEventHandler) : Mapper() {
    private val logger = KotlinLogging.logger {}

    companion object {
        const val SUCCESS = "success"
        const val RETRY = "retry"
        const val DLQ = "dlq"
        const val MAX_RETRIES = 5
        const val RETRY_COUNT = "retryCount"
        val mapper = jacksonObjectMapper()
    }

    // Note: we don't set the client or workflow options here, those will be set through the
    // subclass, since they depend on a configuration being injected by the UI (workflow)
    // that should be there in tandem with every pipeline

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
    protected fun processEvent(
        event: AtlanEvent,
        keys: Array<String>,
        data: Datum,
    ): MessageList {
        try {
            if (!handler.validatePrerequisites(event, logger)) {
                return failed(keys, data)
            }
        } catch (e: AtlanException) {
            logger.error("Unable to validate prerequisites, failing.", e)
            return failed(keys, data)
        }
        return try {
            val current =
                handler.getCurrentState(
                    handler.client,
                    event.payload.asset,
                    logger,
                )
            val updated = handler.calculateChanges(current, logger)
            if (!updated.isEmpty()) {
                handler.saveChanges(handler.client, updated, logger)
                succeeded(keys, data)
            } else {
                drop()
            }
        } catch (e: AtlanException) {
            logger.error(
                "Unable to update Atlan asset: {}",
                event.payload.asset.qualifiedName,
                e,
            )
            failed(keys, data)
        }
    }

    /** {@inheritDoc}  */
    override fun processMessage(
        keys: Array<String>,
        data: Datum,
    ): MessageList {
        return try {
            processEvent(getAtlanEvent(data), keys, data)
        } catch (e: IOException) {
            logger.error("Unable to deserialize event: {}", String(data.value, StandardCharsets.UTF_8), e)
            failed(keys, data.value)
        }
    }

    /**
     * Translate the Numaflow message into an Atlan event object.
     *
     * @param data the Numaflow message
     * @return an Atlan event object representation of the message
     * @throws IOException if an Atlan event cannot be parsed from the message
     */
    @Throws(IOException::class)
    protected fun getAtlanEvent(data: Datum): AtlanEvent {
        return AtlanEventHandler.getAtlanEvent(handler.client, data.value)
    }

    /**
     * Route the message as failed.
     *
     * @param keys the Numaflow keys for the message
     * @param data the Numaflow message
     * @return a message list indicating the message failed to be processed
     */
    protected fun failed(
        keys: Array<String>,
        data: Datum,
    ): MessageList {
        return failed(keys, data.value)
    }

    /**
     * Route the message as failed.
     *
     * @param keys the Numaflow keys for the message
     * @param data the Numaflow message
     * @return a message list indicating the message failed to be processed
     */
    protected fun failed(
        keys: Array<String>,
        data: ByteArray,
    ): MessageList {
        val map = mapper.readValue<MutableMap<String, Any>>(data.decodeToString())
        if (!map.containsKey(RETRY_COUNT)) {
            map[RETRY_COUNT] = 0
        }
        map[RETRY_COUNT] = (map[RETRY_COUNT] as Int) + 1
        val tag =
            if (map[RETRY_COUNT] as Int > MAX_RETRIES) {
                logger.info { "Routing to: $DLQ (exceeded $MAX_RETRIES retries)" }
                DLQ
            } else {
                logger.info { "Routing to: $RETRY (retry #${map[RETRY_COUNT]})" }
                RETRY
            }
        return MessageList.newBuilder()
            .addMessage(Message(mapper.writeValueAsBytes(map), keys, arrayOf(tag)))
            .build()
    }

    /**
     * Route the message as succeeded.
     *
     * @param keys the Numaflow keys for the message
     * @param data the Numaflow message
     * @return a message list indicating the message was successfully processed
     */
    protected fun succeeded(
        keys: Array<String>,
        data: Datum,
    ): MessageList {
        return succeeded(keys, data.value)
    }

    /**
     * Route the message as succeeded.
     *
     * @param keys the Numaflow keys for the message
     * @param data the Numaflow message
     * @return a message list indicating the message was successfully processed
     */
    protected fun succeeded(
        keys: Array<String>,
        data: ByteArray,
    ): MessageList {
        logger.info { "Routing to: $SUCCESS" }
        return MessageList.newBuilder()
            .addMessage(Message(data, keys, arrayOf(SUCCESS)))
            .build()
    }

    /**
     * Route the message forward, as-is.
     *
     * @param data the Numaflow message
     * @return a message list indicating the message should be forwarded as-is
     */
    protected fun forward(data: Datum): MessageList {
        return forward(data.value)
    }

    /**
     * Route the message forward, as-is.
     *
     * @param data the Numaflow message
     * @return a message list indicating the message should be forwarded as-is
     */
    protected fun forward(data: ByteArray): MessageList {
        return MessageList.newBuilder().addMessage(Message(data)).build()
    }

    /**
     * Drop the message. Mostly this should be used when receiving an event that is
     * the result of this handler taking an action on a previous event.
     * (Without this, we could have an infinite loop of that action being applied
     * over and over again.)
     *
     * @return a message list indicating the message can be safely ignored
     */
    protected fun drop(): MessageList {
        return MessageList.newBuilder().addMessage(Message.toDrop()).build()
    }
}
