/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

import java.util.List;

/**
 * A response from Atlan's API, with body represented as a list of Strings
 * (one element in the list per event emitted from the event stream).
 */
public class AtlanEventStreamResponse extends AbstractAtlanResponse<List<String>> {
    /**
     * Initializes a new instance of the {@link AtlanEventStreamResponse} class.
     *
     * @param code the HTTP status code of the response
     * @param headers the HTTP headers of the response
     * @param events the events emitted in the response
     * @throws NullPointerException if {@code headers} or {@code events} is {@code null}
     */
    public AtlanEventStreamResponse(int code, HttpHeaders headers, List<String> events) {
        super(code, headers, events);
    }
}
