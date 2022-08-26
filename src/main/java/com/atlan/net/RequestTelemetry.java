/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.net;

import com.atlan.Atlan;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/** Helper class used by {@link LiveAtlanResponseGetter} to manage request telemetry. */
@Slf4j
class RequestTelemetry {
    /** The name of the header used to send request telemetry in requests. */
    public static final String HEADER_NAME = "X-Atlan-Client-Telemetry";

    private static final int MAX_REQUEST_METRICS_QUEUE_SIZE = 100;

    private static final ObjectMapper mapper = new ObjectMapper();

    private static ConcurrentLinkedQueue<RequestMetrics> prevRequestMetrics = new ConcurrentLinkedQueue<>();

    /**
     * Returns an {@link Optional} containing the value of the {@code X-Atlan-Telemetry} header to add
     * to the request. If the header is already present in the request, or if there is available
     * metrics, or if telemetry is disabled, then the returned {@code Optional} is empty.
     *
     * @param headers the request headers
     */
    public Optional<String> getHeaderValue(HttpHeaders headers) {
        if (headers.firstValue(HEADER_NAME).isPresent()) {
            return Optional.empty();
        }

        RequestMetrics requestMetrics = prevRequestMetrics.poll();
        if (requestMetrics == null) {
            return Optional.empty();
        }

        if (!Atlan.enableTelemetry) {
            return Optional.empty();
        }

        ClientTelemetryPayload payload = new ClientTelemetryPayload(requestMetrics);
        try {
            return Optional.of(mapper.writeValueAsString(payload));
        } catch (JsonProcessingException e) {
            log.error("Unable to serialize request metrics details: {}", payload, e);
        }
        return Optional.empty();
    }

    /**
     * If telemetry is enabled and the queue is not full, then enqueue a new metrics item; otherwise,
     * do nothing.
     *
     * @param response the Atlan response
     * @param duration the request duration
     */
    public void maybeEnqueueMetrics(AbstractAtlanResponse<?> response, Duration duration) {
        if (!Atlan.enableTelemetry) {
            return;
        }

        if (response.requestId() == null) {
            return;
        }

        if (prevRequestMetrics.size() >= MAX_REQUEST_METRICS_QUEUE_SIZE) {
            return;
        }

        RequestMetrics metrics = new RequestMetrics(response.requestId(), duration.toMillis());
        prevRequestMetrics.add(metrics);
    }

    @Data
    private static class ClientTelemetryPayload {
        private final RequestMetrics lastRequestMetrics;
    }

    @Data
    private static class RequestMetrics {
        private final String requestId;
        private final long requestDurationMs;
    }
}
