/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.net;

import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.AtlanResponseInterface;
import com.google.gson.JsonObject;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ApiResource extends AtlanObject implements AtlanResponseInterface {
    public static final Charset CHARSET = StandardCharsets.UTF_8;

    private static AtlanResponseGetter atlanResponseGetter = new LiveAtlanResponseGetter();

    public static void setAtlanResponseGetter(AtlanResponseGetter arg) {
        ApiResource.atlanResponseGetter = arg;
    }

    private transient AtlanResponse lastResponse;

    private transient JsonObject rawJsonObject;

    @Override
    public AtlanResponse getLastResponse() {
        return lastResponse;
    }

    @Override
    public void setLastResponse(AtlanResponse response) {
        this.lastResponse = response;
    }

    /**
     * Returns the raw JsonObject exposed by the Gson library. This can be used to access properties
     * that are not directly exposed by Atlan's Java library.
     *
     * <p>Note: You should always prefer using the standard property accessors whenever possible.
     * Because this method exposes Gson's underlying API, it is not considered fully stable. Atlan's
     * Java library might move off Gson in the future and this method would be removed or change
     * significantly.
     *
     * @return The raw JsonObject.
     */
    public JsonObject getRawJsonObject() {
        // Lazily initialize this the first time the getter is called.
        if ((this.rawJsonObject == null) && (this.getLastResponse() != null)) {
            this.rawJsonObject =
                    ApiResource.GSON.fromJson(this.getLastResponse().body(), JsonObject.class);
        }

        return this.rawJsonObject;
    }

    public enum RequestMethod {
        GET,
        POST,
        PUT,
        DELETE
    }

    /** URL-encodes a string. */
    public static String urlEncode(String str) {
        // Preserve original behavior that passing null for an object id will lead
        // to us actually making a request to /v1/foo/null
        if (str == null) {
            return null;
        }

        // Don't use strict form encoding by changing the square bracket control
        // characters back to their literals. This is fine by the server, and
        // makes these parameter strings easier to read.
        return URLEncoder.encode(str, CHARSET).replaceAll("%5B", "[").replaceAll("%5D", "]");
    }

    /** URL-encode a string ID in url path formatting. */
    public static String urlEncodeId(String id) throws InvalidRequestException {
        if (id == null) {
            throw new InvalidRequestException(
                    "Invalid null ID found for url path formatting. This can be because your string ID "
                            + "argument to the API method is null, or the ID field in your Atlan object "
                            + "instance is null. Please contact support@atlan.com on the latter case. ",
                    null,
                    null,
                    null,
                    0,
                    null);
        }

        return urlEncode(id);
    }

    public static <T extends ApiResource> T request(
            ApiResource.RequestMethod method, String url, AtlanObject payload, Class<T> clazz, RequestOptions options)
            throws AtlanException {
        checkNullTypedParams(url, payload);
        return request(method, url, payload.toJson(), clazz, options);
    }

    public static <T extends ApiResource> T request(
            ApiResource.RequestMethod method, String url, String body, Class<T> clazz, RequestOptions options)
            throws AtlanException {
        log.debug("({}) {} with: {}", method, url, body);
        T response = ApiResource.atlanResponseGetter.request(method, url, body, clazz, options);
        if (log.isDebugEnabled()) {
            log.debug(" ... response: {}", response.getRawJsonObject());
        }
        return response;
    }

    /**
     * Invalidate null typed parameters.
     *
     * @param url request url associated with the given parameters.
     * @param params typed parameters to check for null value.
     */
    public static void checkNullTypedParams(String url, AtlanObject params) {
        if (params == null) {
            throw new IllegalArgumentException(String.format(
                    "Found null params for %s. "
                            + "Please pass empty params using param builder via `builder().build()` instead.",
                    url));
        }
    }
}
