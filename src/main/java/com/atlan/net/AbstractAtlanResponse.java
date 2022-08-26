/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.NonFinal;

/** Common interface representing an HTTP response from Atlan. */
@Accessors(fluent = true)
abstract class AbstractAtlanResponse<T> {
    /** The HTTP status code of the response. */
    int code;

    /** The HTTP headers of the response. */
    HttpHeaders headers;

    /** The body of the response. */
    T body;

    public final int code() {
        return this.code;
    }

    public final HttpHeaders headers() {
        return this.headers;
    }

    public final T body() {
        return this.body;
    }

    /** Number of times the request was retried. Used for internal tests only. */
    @NonFinal
    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    int numRetries;

    /**
     * Gets the date of the request, as returned by Atlan.
     *
     * @return the date of the request, as returned by Atlan
     */
    public Instant date() {
        Optional<String> dateStr = this.headers.firstValue("Date");
        if (!dateStr.isPresent()) {
            return null;
        }
        return ZonedDateTime.parse(dateStr.get(), DateTimeFormatter.RFC_1123_DATE_TIME)
                .toInstant();
    }

    /**
     * Gets the idempotency key of the request, as returned by Atlan.
     *
     * @return the idempotency key of the request, as returned by Atlan
     */
    public String idempotencyKey() {
        return this.headers.firstValue("Idempotency-Key").orElse(null);
    }

    /**
     * Gets the ID of the request, as returned by Atlan.
     *
     * @return the ID of the request, as returned by Atlan
     */
    public String requestId() {
        return this.headers.firstValue("Request-Id").orElse(null);
    }

    protected AbstractAtlanResponse(int code, HttpHeaders headers, T body) {
        requireNonNull(headers);
        requireNonNull(body);

        this.code = code;
        this.headers = headers;
        this.body = body;
    }
}
