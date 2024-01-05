/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.util.StreamUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for handling API interactions as streams.
 */
public class AtlanResponseStream extends AbstractAtlanResponse<InputStream> {
    /**
     * Initializes a new instance of the {@link AtlanResponseStream} class.
     *
     * @param code the HTTP status code of the response
     * @param headers the HTTP headers of the response
     * @param body streaming body response
     * @throws NullPointerException if {@code headers} or {@code body} is {@code null}
     */
    public AtlanResponseStream(int code, HttpHeaders headers, InputStream body) {
        super(code, headers, body);
    }

    /**
     * Buffers the entire response body into a string, constructing the appropriate AtlanResponse
     *
     * @return the AtlanResponse
     * @throws IOException on any errors processing the response
     */
    AtlanResponse unstream() throws IOException {
        final String bodyString = StreamUtils.readToEnd(this.body, ApiResource.CHARSET);
        this.body.close();
        return new AtlanResponse(this.code, this.headers, bodyString);
    }

    /**
     * Buffers each line of the response body into an event, constructing the appropriate AtlanEventStreamResponse
     *
     * @return the AtlanEventStreamResponse
     * @throws IOException on any errors processing the response
     */
    AtlanEventStreamResponse toEvents() throws IOException {
        String line;
        List<String> events = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.body, StandardCharsets.UTF_8))) {
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    events.add(line);
                }
            }
        }
        return new AtlanEventStreamResponse(this.code, this.headers, events);
    }
}
