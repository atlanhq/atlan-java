/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.Atlan;
import com.atlan.exception.ApiConnectionException;
import com.atlan.exception.ErrorCode;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Cleanup;

/**
 * Class that actually handles the HTTP-level communications with Atlan.
 */
public class HttpURLConnectionClient extends HttpClient {
    /** Initializes a new instance of the {@link HttpURLConnectionClient}. */
    public HttpURLConnectionClient() {
        super();
    }

    /**
     * Sends the given request to Atlan's API.
     *
     * @param request the request
     * @return the response
     * @throws ApiConnectionException if an error occurs when sending or receiving
     */
    @Override
    public AtlanResponseStream requestStream(AtlanRequest request) throws ApiConnectionException {
        try {
            final HttpURLConnection conn = createAtlanConnection(request);

            // Calling `getResponseCode()` triggers the request.
            final int responseCode = conn.getResponseCode();

            final HttpHeaders headers = HttpHeaders.of(conn.getHeaderFields());

            final InputStream responseStream =
                    (responseCode >= 200 && responseCode < 300) ? conn.getInputStream() : conn.getErrorStream();

            return new AtlanResponseStream(responseCode, headers, responseStream);

        } catch (IOException e) {
            throw new ApiConnectionException(ErrorCode.CONNECTION_ERROR, e, Atlan.getBaseUrlSafe());
        }
    }

    /**
     * Sends the given request to Atlan's API, and returns a buffered response.
     *
     * @param request the request
     * @return the response
     * @throws ApiConnectionException if an error occurs when sending or receiving
     */
    @Override
    public AtlanResponse request(AtlanRequest request) throws ApiConnectionException {
        final AtlanResponseStream responseStream = requestStream(request);
        try {
            return responseStream.unstream();
        } catch (IOException e) {
            throw new ApiConnectionException(ErrorCode.CONNECTION_ERROR, e, Atlan.getBaseUrlSafe());
        }
    }

    /**
     * Returns the HTTP headers to use for the request.
     *
     * @param request the request being sent
     * @return the HTTP headers
     */
    static HttpHeaders getHeaders(AtlanRequest request) {
        Map<String, List<String>> userAgentHeadersMap = new HashMap<>();

        userAgentHeadersMap.put("User-Agent", List.of(buildUserAgentString()));
        userAgentHeadersMap.put("X-Atlan-Client-User-Agent", List.of(buildXAtlanClientUserAgentString()));
        userAgentHeadersMap.put("x-atlan-agent", List.of("sdk"));
        userAgentHeadersMap.put("x-atlan-agent-id", List.of("java"));

        return request.headers().withAdditionalHeaders(userAgentHeadersMap);
    }

    private static HttpURLConnection createAtlanConnection(AtlanRequest request)
            throws IOException, ApiConnectionException {
        HttpURLConnection conn = null;

        if (request.options().getConnectionProxy() != null) {
            conn = (HttpURLConnection)
                    request.url().openConnection(request.options().getConnectionProxy());
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return request.options().getProxyCredential();
                }
            });
        } else {
            conn = (HttpURLConnection) request.url().openConnection();
        }

        conn.setConnectTimeout(request.options().getConnectTimeout());
        conn.setReadTimeout(request.options().getReadTimeout());
        conn.setUseCaches(false);
        for (Map.Entry<String, List<String>> entry : getHeaders(request).map().entrySet()) {
            conn.setRequestProperty(entry.getKey(), String.join(",", entry.getValue()));
        }

        conn.setRequestMethod(request.method().name());

        if (request.content() != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", request.content().contentType());

            @Cleanup OutputStream output = conn.getOutputStream();
            output.write(request.content().byteArrayContent());
        }

        return conn;
    }
}
