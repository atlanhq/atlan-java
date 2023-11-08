/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */

/** A response from Atlan's API, with body represented as a String. */
public class AtlanResponse extends AbstractAtlanResponse<String> {
    /**
     * Initializes a new instance of the {@link AtlanResponse} class.
     *
     * @param code the HTTP status code of the response
     * @param headers the HTTP headers of the response
     * @param body the body of the response
     * @throws NullPointerException if {@code headers} or {@code body} is {@code null}
     */
    public AtlanResponse(int code, HttpHeaders headers, String body) {
        super(code, headers, body);
    }
}
