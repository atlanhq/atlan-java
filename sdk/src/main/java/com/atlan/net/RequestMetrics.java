/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

import java.time.Duration;
import java.util.Optional;
import lombok.Getter;

/**
 * Class to capture response time metrics at the lowest level of granularity we can from the client-side.
 */
@Getter
public class RequestMetrics {

    /** Name of the header used to track milliseconds spent server-side in Atlan. */
    public static final String UPSTREAM_HEADER = "X-Kong-Upstream-Latency";

    /** Name of the header used to track milliseconds spent in Atlan's API proxy. */
    public static final String PROXY_HEADER = "X-Kong-Proxy-Latency";

    private final Long requestDurationUpstreamMs;
    private final Long requestDurationProxyMs;
    private final long requestDurationTotalMs;

    /**
     * Create new set of metrics.
     *
     * @param upstream time spent server-side in Atlan, in milliseconds
     * @param proxy time spent in Atlan's API proxy, in milliseconds
     * @param elapsed total elapsed time for the SDK's request / response, in milliseconds
     */
    private RequestMetrics(Long upstream, Long proxy, long elapsed) {
        this.requestDurationUpstreamMs = upstream;
        this.requestDurationProxyMs = proxy;
        this.requestDurationTotalMs = elapsed;
    }

    /**
     * Embed a new set of metrics into the provided response.
     *
     * @param response into which to embed the metrics
     * @param elapsed elapsed time of the response that was measured by the SDK
     */
    public static void embed(AbstractAtlanResponse<?> response, Duration elapsed) {
        Optional<String> upstream = response.headers().firstValue(UPSTREAM_HEADER);
        Optional<String> proxy = response.headers().firstValue(PROXY_HEADER);
        RequestMetrics metrics = new RequestMetrics(
                upstream.isEmpty() ? null : Long.valueOf(upstream.get()),
                proxy.isEmpty() ? null : Long.valueOf(proxy.get()),
                elapsed.toMillis());
        response.metrics(metrics);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (requestDurationUpstreamMs != null) {
            sb.append(requestDurationUpstreamMs).append("/");
        }
        if (requestDurationProxyMs != null) {
            sb.append(requestDurationProxyMs).append("/");
        }
        sb.append(requestDurationTotalMs).append("ms");
        return sb.toString();
    }
}
