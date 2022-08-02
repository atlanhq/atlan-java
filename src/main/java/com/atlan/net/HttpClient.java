/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.net;

import com.atlan.Atlan;
import com.atlan.exception.ApiConnectionException;
import com.atlan.exception.AtlanException;
import com.atlan.util.Stopwatch;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/** Base abstract class for HTTP clients used to send requests to Atlan's API. */
public abstract class HttpClient {
    /** Maximum sleep time between tries to send HTTP requests after network failure. */
    public static final Duration maxNetworkRetriesDelay = Duration.ofSeconds(5);

    /** Minimum sleep time between tries to send HTTP requests after network failure. */
    public static final Duration minNetworkRetriesDelay = Duration.ofMillis(500);

    private final RequestTelemetry requestTelemetry = new RequestTelemetry();

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
        Optional<String> telemetryHeaderValue = requestTelemetry.getHeaderValue(request.headers());
        if (telemetryHeaderValue.isPresent()) {
            request = request.withAdditionalHeader(RequestTelemetry.HEADER_NAME, telemetryHeaderValue.get());
        }

        Stopwatch stopwatch = Stopwatch.startNew();

        T response = send.apply(request);

        stopwatch.stop();

        requestTelemetry.maybeEnqueueMetrics(response, stopwatch.getElapsed());

        return response;
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
     * Sends the given request to Atlan's API, streaming the response, and handling telemetry if not
     * disabled.
     *
     * @param request the request
     * @return the response
     * @throws AtlanException If the request fails for any reason
     */
    public AtlanResponseStream requestStreamWithTelemetry(AtlanRequest request) throws AtlanException {
        return sendWithTelemetry(request, this::requestStream);
    }

    public <T extends AbstractAtlanResponse<?>> T sendWithRetries(AtlanRequest request, RequestSendFunction<T> send)
            throws AtlanException {
        ApiConnectionException requestException = null;
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
     * Sends the given request to Atlan's API, streaming the response, retrying the request in cases
     * of intermittent problems.
     *
     * @param request the request
     * @return the response
     * @throws AtlanException If the request fails for any reason
     */
    public AtlanResponseStream requestStreamWithRetries(AtlanRequest request) throws AtlanException {
        return sendWithRetries(request, (r) -> this.requestStreamWithTelemetry(r));
    }

    /**
     * Builds the value of the {@code User-Agent} header.
     *
     * @return a string containing the value of the {@code User-Agent} header
     */
    protected static String buildUserAgentString() {
        String userAgent = String.format("Atlan/v1 JavaBindings/%s", Atlan.VERSION);

        if (Atlan.getAppInfo() != null) {
            userAgent += " " + formatAppInfo(Atlan.getAppInfo());
        }

        return userAgent;
    }

    /**
     * Builds the value of the {@code X-Atlan-Client-User-Agent} header.
     *
     * @return a string containing the value of the {@code X-Atlan-Client-User-Agent} header
     */
    protected static String buildXAtlanClientUserAgentString() {
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
        if (Atlan.getAppInfo() != null) {
            propertyMap.put("application", ApiResource.GSON.toJson(Atlan.getAppInfo()));
        }

        return ApiResource.GSON.toJson(propertyMap);
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
            return false;
        }

        // Retry on connection error.
        if ((exception != null)
                && (exception.getCause() != null)
                && (exception.getCause() instanceof ConnectException
                        || exception.getCause() instanceof SocketTimeoutException)) {
            return true;
        }

        // The API may ask us not to retry (eg; if doing so would be a no-op)
        // or advise us to retry (eg; in cases of lock timeouts); we defer to that.
        if ((response != null) && (response.headers() != null)) {
            String value = response.headers().firstValue("Atlan-Should-Retry").orElse(null);

            if ("true".equals(value)) {
                return true;
            }

            if ("false".equals(value)) {
                return false;
            }
        }

        // Retry on conflict errors.
        if ((response != null) && (response.code() == 409)) {
            return true;
        }

        // Retry on 500, 503, and other internal errors.
        //
        // Note that we expect the Atlan-Should-Retry header to be false
        // in most cases when a 500 is returned, since our idempotency framework
        // would typically replay it anyway.
        if ((response != null) && (response.code() >= 500)) {
            return true;
        }

        return false;
    }

    private Duration sleepTime(int numRetries) {
        // We disable sleeping in some cases for tests.
        if (!this.networkRetriesSleep) {
            return Duration.ZERO;
        }

        // Apply exponential backoff with MinNetworkRetriesDelay on the number of numRetries
        // so far as inputs.
        Duration delay = Duration.ofNanos((long) (minNetworkRetriesDelay.toNanos() * Math.pow(2, numRetries - 1)));

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
