/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public final class FormEncoder {
    public static HttpContent createHttpContent(Map<String, Object> params) throws IOException {
        // If params is null, we create an empty HttpContent because we still want to send the
        // Content-Type header.
        if (params == null) {
            return HttpContent.buildFormURLEncodedContent(new ArrayList<>());
        }
        Collection<KeyValuePair<String, String>> kvp = new ArrayList<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            kvp.add(new KeyValuePair<>(entry.getKey(), entry.getValue().toString()));
        }
        return HttpContent.buildFormURLEncodedContent(kvp);
    }

    /**
     * Creates the HTTP query string for a collection of name/value tuples.
     *
     * @param nameValueCollection The collection of name/value tuples.
     * @return The query string.
     */
    public static String createQueryString(Collection<KeyValuePair<String, String>> nameValueCollection) {
        if (nameValueCollection == null) {
            return "";
        }

        String result = String.join(
                "&",
                nameValueCollection.stream()
                        .map(kvp -> String.format("%s=%s", urlEncode(kvp.getKey()), urlEncode(kvp.getValue())))
                        .collect(Collectors.toList()));
        return result;
    }

    /**
     * URL-encodes a string.
     *
     * @param value The string to URL-encode.
     * @return The URL-encoded string.
     */
    private static String urlEncode(String value) {
        if (value == null) {
            return null;
        }

        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            // This can literally never happen, and lets us avoid having to catch
            // UnsupportedEncodingException in callers.
            throw new AssertionError("UTF-8 is unknown");
        }
    }
}
