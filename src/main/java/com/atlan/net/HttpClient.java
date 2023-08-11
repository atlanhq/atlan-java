/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.ApiConnectionException;
import com.atlan.exception.AtlanException;
import com.atlan.serde.Serde;
import com.atlan.util.Stopwatch;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;

/** Base abstract class for HTTP clients used to send requests to Atlan's API. */
@Slf4j
public abstract class HttpClient {
    /** Maximum sleep time between tries to send HTTP requests after network failure. */
    public static final Duration maxNetworkRetriesDelay = Duration.ofSeconds(5);

    /** Minimum sleep time between tries to send HTTP requests after network failure. */
    public static final Duration minNetworkRetriesDelay = Duration.ofMillis(500);

    /** A value indicating whether the client should sleep between automatic request retries. */
    boolean networkRetriesSleep = true;

    /** Initializes a new instance of the {@link HttpClient} class. */
    protected HttpClient() {}

    /**
     * Sends the given request to Atlan's API, buffering the response body into memory.
     *
     * @param request the request
     * @return the response
     * @throws AtlanException If the request fails for any reason
     */
    public abstract AtlanResponse request(AtlanRequest request) throws AtlanException;

    /**
     * Sends the given request to Atlan's API, streaming the response body.
     *
     * @param request the request
     * @return the response
     * @throws AtlanException If the request fails for any reason
     */
    public AtlanResponseStream requestStream(AtlanRequest request) throws AtlanException {
        throw new UnsupportedOperationException("requestStream is unimplemented for this HttpClient");
    }

    @FunctionalInterface
    private interface RequestSendFunction<R> {
        R apply(AtlanRequest request) throws AtlanException;
    }

    private <T extends AbstractAtlanResponse<?>> T sendWithTelemetry(AtlanRequest request, RequestSendFunction<T> send)
            throws AtlanException {
        if (!Atlan.enableTelemetry) {
            // If telemetry is disabled, just make the request and respond
            return send.apply(request);
        } else {
            // Otherwise, time the request / response and embed the metrics back into the response itself
            Stopwatch stopwatch = Stopwatch.startNew();
            T response = send.apply(request);
            stopwatch.stop();
            RequestMetrics.embed(response, stopwatch.getElapsed());
            return response;
        }
    }

    /**
     * Sends the given request to Atlan's API, handling telemetry if not disabled.
     *
     * @param request the request
     * @return the response
     * @throws AtlanException If the request fails for any reason
     */
    public AtlanResponse requestWithTelemetry(AtlanRequest request) throws AtlanException {
        return sendWithTelemetry(request, this::request);
    }

    /**
     * Sends the given request to Atlan's API, retrying if it encounters certain problems.
     *
     * @param request the request
     * @param send the function to use for sending the request (e.g. with or without telemetry)
     * @return the response
     * @param <T> the type of the response
     * @throws AtlanException if the request fails for any reason, even after retries
     */
    public <T extends AbstractAtlanResponse<?>> T sendWithRetries(AtlanRequest request, RequestSendFunction<T> send)
            throws AtlanException {
        AtlanException requestException = null;
        T response = null;
        int retry = 0;

        while (true) {
            requestException = null;

            try {
                response = send.apply(request);
            } catch (ApiConnectionException e) {
                requestException = e;
            }

            if (!this.shouldRetry(retry, requestException, request, response)) {
                break;
            }

            retry += 1;

            try {
                Thread.sleep(this.sleepTime(retry).toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (requestException != null) {
            throw requestException;
        }

        response.numRetries(retry);

        return response;
    }

    /**
     * Sends the given request to Atlan's API, retrying the request in cases of intermittent problems.
     *
     * @param request the request
     * @return the response
     * @throws AtlanException If the request fails for any reason
     */
    public AtlanResponse requestWithRetries(AtlanRequest request) throws AtlanException {
        return sendWithRetries(request, (r) -> this.requestWithTelemetry(r));
    }

    /**
     * Builds the value of the {@code User-Agent} header.
     *
     * @param client through which to connect to Atlan
     * @return a string containing the value of the {@code User-Agent} header
     */
    protected static String buildUserAgentString(AtlanClient client) {
        String userAgent = String.format("Atlan-JavaSDK/%s", Atlan.VERSION);

        if (client.getAppInfo() != null) {
            userAgent += " " + formatAppInfo(client.getAppInfo());
        }

        return userAgent;
    }

    /**
     * Builds the value of the {@code X-Atlan-Client-User-Agent} header.
     *
     * @param client through which to connect to Atlan
     * @return a string containing the value of the {@code X-Atlan-Client-User-Agent} header
     */
    protected static String buildXAtlanClientUserAgentString(AtlanClient client) {
        String[] propertyNames = {
            "os.name", "os.version", "os.arch", "java.version", "java.vendor", "java.vm.version", "java.vm.vendor"
        };

        Map<String, String> propertyMap = new HashMap<>();
        for (String propertyName : propertyNames) {
            propertyMap.put(propertyName, System.getProperty(propertyName));
        }
        propertyMap.put("bindings.version", Atlan.VERSION);
        propertyMap.put("lang", "Java");
        propertyMap.put("publisher", "Atlan");

        try {
            if (client.getAppInfo() != null) {
                propertyMap.put("application", Serde.allInclusiveMapper.writeValueAsString(client.getAppInfo()));
            }
            return Serde.allInclusiveMapper.writeValueAsString(propertyMap);
        } catch (IOException e) {
            throw new RuntimeException("Unable to build client user agent string.", e);
        }
    }

    private static String formatAppInfo(Map<String, String> info) {
        String str = info.get("name");

        if (info.get("version") != null) {
            str += String.format("/%s", info.get("version"));
        }

        if (info.get("url") != null) {
            str += String.format(" (%s)", info.get("url"));
        }

        return str;
    }

    private <T extends AbstractAtlanResponse<?>> boolean shouldRetry(
            int numRetries, AtlanException exception, AtlanRequest request, T response) {
        // Do not retry if we are out of retries.
        if (numRetries >= request.options().getMaxNetworkRetries()) {
            log.error(
                    " ... beyond max retries ({}), failing! If this is unexpected, you can try increasing the maximum retries through Atlan.setMaxNetworkRetries()",
                    request.options().getMaxNetworkRetries(),
                    exception);
            return false;
        }

        // Retry on connection error.
        if ((exception != null)
                && (exception.getCause() != null)
                && (exception.getCause() instanceof ConnectException
                        || exception.getCause() instanceof SocketTimeoutException)) {
            log.debug(" ... network issue, will retry.", exception);
            return true;
        }

        if (response != null) {
            if (response.code() == 403) {
                // Retry on permission failure (since these are granted asynchronously)
                log.debug(" ... no permission for the operation (yet), will retry: {}", response.body(), exception);
            } else if (response.code() >= 500) {
                // Retry on 500, 503, and other internal errors.
                log.debug(" ... internal server error, will retry: {}", response.body(), exception);
            }
            return (response.code() == 403 || response.code() >= 500);
        }
        return false;
    }

    private Duration sleepTime(int numRetries) {
        // We disable sleeping in some cases for tests.
        if (!this.networkRetriesSleep) {
            return Duration.ZERO;
        }
        return waitTime(numRetries);
    }

    /**
     * Calculate an exponential-backoff time to wait, with a jitter.
     *
     * @param attempt the retry attempt (count)
     * @return a duration giving the time to wait (sleep)
     */
    public static Duration waitTime(int attempt) {
        // Apply exponential backoff with MinNetworkRetriesDelay on the number of numRetries
        // so far as inputs.
        Duration delay = Duration.ofNanos((long) (minNetworkRetriesDelay.toNanos() * Math.pow(2, attempt - 1)));

        // Do not allow the number to exceed MaxNetworkRetriesDelay
        if (delay.compareTo(maxNetworkRetriesDelay) > 0) {
            delay = maxNetworkRetriesDelay;
        }

        // Apply some jitter by randomizing the value in the range of 75%-100%.
        double jitter = ThreadLocalRandom.current().nextDouble(0.75, 1.0);
        delay = Duration.ofNanos((long) (delay.toNanos() * jitter));

        // But never sleep less than the base sleep seconds.
        if (delay.compareTo(minNetworkRetriesDelay) < 0) {
            delay = minNetworkRetriesDelay;
        }

        return delay;
    }
}
