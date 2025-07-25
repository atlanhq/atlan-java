/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.ApiConnectionException;
import com.atlan.exception.ApiException;
import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.TypeDefResponse;
import com.atlan.util.Stopwatch;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
     * Sends the given request to Atlan's API, and returns a buffered response.
     *
     * @param request the request
     * @return the response
     * @throws ApiConnectionException if an error occurs when sending or receiving
     */
    public abstract AtlanEventStreamResponse requestES(AtlanRequest request) throws AtlanException;

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
     * Sends the given request to Atlan's API, retrying the request in cases of intermittent problems.
     *
     * @param request the request
     * @return the response
     * @throws AtlanException If the request fails for any reason
     */
    public AtlanEventStreamResponse requestEventStream(AtlanRequest request) throws AtlanException {
        AtlanException requestException = null;
        AtlanEventStreamResponse response = null;
        try {
            response = requestES(request);
        } catch (ApiException e) {
            requestException = e;
        }
        if (requestException != null) {
            throw requestException;
        }
        return response;
    }

    /**
     * Builds the value of the {@code User-Agent} header.
     *
     * @param client through which to connect to Atlan
     * @return a string containing the value of the {@code User-Agent} header
     */
    protected static String buildUserAgentString(AtlanClient client) {
        return buildXAtlanClientUserAgentString(client);
    }

    /**
     * Builds the value of the {@code X-Atlan-Client-User-Agent} header.
     *
     * @param client through which to connect to Atlan
     * @return a string containing the value of the {@code X-Atlan-Client-User-Agent} header
     */
    protected static String buildXAtlanClientUserAgentString(AtlanClient client) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("atlan-java/%s", Atlan.VERSION));
        sb.append(String.format(
                " (%s; %s; rv:%s)",
                System.getProperty("os.name"), System.getProperty("os.arch"), System.getProperty("os.version")));
        sb.append(String.format(" %s/%s", System.getProperty("java.vendor"), System.getProperty("java.version")));
        sb.append(String.format(" %s/%s", System.getProperty("java.vm.vendor"), System.getProperty("java.vm.version")));
        if (client.getAppInfo() != null) {
            sb.append(formatAppInfo(client.getAppInfo()));
        }
        return sb.toString();
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
        // Continue (without retrying) in case of a successful response
        if (response != null && response.code() >= 200 && response.code() < 300) {
            return false;
        }

        // Continue retrying in case of connection errors (ignore max retries in these cases)
        if ((exception != null)
                && (exception.getCause() != null)
                && (exception.getCause() instanceof ConnectException
                        || exception.getCause() instanceof SocketTimeoutException)) {
            log.debug(" ... network issue, will retry.", exception);
            return true;
        }

        // Continue retrying in case of rate limiting (ignore max retries in this case)
        if (response != null && response.code() == 429) {
            Optional<String> retryAfter = response.headers.firstValue("Retry-After");
            if (retryAfter.isPresent()) {
                try {
                    String retryInSeconds = retryAfter.get();
                    long waitTime = Long.parseLong(retryInSeconds);
                    if (waitTime > 0) {
                        rateLimit(waitTime * 1000);
                    } else {
                        rateLimit(waitTime(numRetries).toMillis());
                    }
                } catch (NumberFormatException e) {
                    log.warn(" ... unable to parse retry-after header value: {}", retryAfter.get(), e);
                    rateLimit(waitTime(numRetries).toMillis());
                }
            } else {
                log.debug(
                        " ... rate limit had no Retry-After header in its response, so only exponentially backing-off retries");
                rateLimit(waitTime(numRetries).toMillis());
            }
            return true;
        }

        // Continue retrying in case of locking (ignore max retries in this case)
        if (response != null && response.code() == 423) {
            if (exception != null) {
                log.debug(" ... lock encountered, will retry with a long delay: {}", response.body(), exception);
            } else {
                log.debug(" ... lock encountered, will retry with a long delay: {}", response.body());
            }
            try {
                Thread.sleep(waitLongTime(numRetries).toMillis());
            } catch (InterruptedException e) {
                log.warn(" ... wait interrupted.", exception);
            }
            return true;
        }

        // Otherwise, do not retry if we are out of retries.
        if (numRetries >= request.options().getMaxNetworkRetries()) {
            if (exception != null) {
                log.error(
                        " ... beyond max retries ({}), failing! If this is unexpected, you can try increasing the maximum retries through Atlan.setMaxNetworkRetries()",
                        request.options().getMaxNetworkRetries(),
                        exception);
            } else {
                log.error(
                        " ... beyond max retries ({}), failing! If this is unexpected, you can try increasing the maximum retries through Atlan.setMaxNetworkRetries()",
                        request.options().getMaxNetworkRetries());
            }
            return false;
        }

        // There can be ephemeral network routing problems, which we should retry on
        // (but only up to the maximum retry limit, otherwise if they are not ephemeral
        // we will cause an infinite loop)
        if ((exception != null) && (exception.getCause() instanceof NoRouteToHostException)) {
            return true;
        }

        if (response != null) {
            if (response.code() == 302) {
                // Retry on a redirect (with back-off), rather than actually redirecting
                log.debug(" ... redirect received, will retry: {}", response.body());
            } else if (response.code() == 401) {
                // Retry authentication on an authentication failure (token could have expired)
                String userId = request.client().getUserId();
                if (userId != null) {
                    try {
                        log.info(" ... authentication failed, attempting to exchange new token for user: {}", userId);
                        AtlanClient client = request.client();
                        String token = client.impersonate.user(userId);
                        client.setApiToken(token);
                        request.rebuildHeaders();
                        TypeDefResponse td = client.typeDefs.list(List.of(AtlanTypeCategory.STRUCT));
                        int retryCount = 1;
                        try {
                            // Before retrying this particular request, first confirm the refreshed token is "active"
                            //  (by making and retrying a call that should retrieve details only when truly active)
                            while (retryCount < client.getMaxNetworkRetries()
                                    && (td == null
                                            || td.getStructDefs() == null
                                            || td.getStructDefs().isEmpty())) {
                                Thread.sleep(waitTime(retryCount).toMillis());
                                td = client.typeDefs.list(List.of(AtlanTypeCategory.STRUCT));
                                retryCount++;
                            }
                        } catch (InterruptedException e) {
                            log.warn(" ... retry loop interrupted.", exception);
                        }
                        return true;
                    } catch (AtlanException e) {
                        log.warn(" ... attempt to impersonate user {} failed, not retrying.", userId, exception);
                    }
                }
                // If there is no user to impersonate, no need to retry, just short-circuit to failure
                return false;
            } else if (response.code() == 403) {
                // Retry on permission failure (since these are granted asynchronously)
                if (exception != null) {
                    log.debug(" ... no permission for the operation (yet), will retry: {}", response.body(), exception);
                } else {
                    log.debug(" ... no permission for the operation (yet), will retry: {}", response.body());
                }
            } else if (response.code() >= 500) {
                // Retry on 500, 503, and other internal errors.
                if (exception != null) {
                    log.debug(" ... internal server error, will retry: {}", response.body(), exception);
                } else {
                    log.debug(" ... internal server error, will retry: {}", response.body());
                }
            }
            return (response.code() == 302 || response.code() == 403 || response.code() >= 500);
        }
        return false;
    }

    private void rateLimit(long waitTime) {
        GlobalRateLimiter limiter = GlobalRateLimiter.getInstance();
        limiter.setRateLimit(waitTime);
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
        return waitTime(attempt, 1);
    }

    /**
     * Add an extensive wait (with jitter), in particular for operations that rely on
     * centralized back-end locking (like type updates).
     *
     * @param attempt the retry attempt (count)
     * @return a duration giving the time to wait (sleep)
     */
    public static Duration waitLongTime(int attempt) {
        return waitTime(attempt, 12);
    }

    private static Duration waitTime(int attempt, int multiplier) {
        Duration delay = Duration.ofNanos((long) (minNetworkRetriesDelay.toNanos() * Math.pow(2, attempt - 1)));

        Duration max = maxNetworkRetriesDelay.multipliedBy(multiplier);

        // Do not allow the number to exceed MaxNetworkRetriesDelay
        if (delay.compareTo(max) > 0) {
            delay = max;
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
