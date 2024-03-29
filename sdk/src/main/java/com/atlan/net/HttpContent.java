/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import static java.util.Objects.requireNonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.UUID;
import lombok.Value;
import lombok.experimental.Accessors;

/**
 * Represents the content of an HTTP request, i.e. the request's body. This class also holds the
 * value of the {@code Content-Type} header, which can depend on the body in some cases (e.g. for
 * multipart requests).
 */
@Value
@Accessors(fluent = true)
public class HttpContent {
    /** The request's content, as a byte array. */
    byte[] byteArrayContent;

    /** The value of the {@code Content-Type} header. */
    String contentType;

    private HttpContent(byte[] byteArrayContent, String contentType) {
        this.byteArrayContent = byteArrayContent;
        this.contentType = contentType;
    }

    /**
     * Builds a new HttpContent for the provided string using {@code application/json} MIME type.
     *
     * @param body the JSON payload
     * @return the encoded HttpContent instance
     */
    public static HttpContent buildJSONEncodedContent(String body) {
        requireNonNull(body);
        return new HttpContent(
                body.getBytes(ApiResource.CHARSET), String.format("application/json;charset=%s", ApiResource.CHARSET));
    }

    /**
     * Builds a new HttpContent for name/value tuples encoded using {@code
     * application/x-www-form-urlencoded} MIME type.
     *
     * @param nameValueCollection the collection of name/value tuples to encode
     * @return the encoded HttpContent instance
     * @throws IllegalArgumentException if nameValueCollection is null
     */
    public static HttpContent buildFormURLEncodedContent(Collection<KeyValuePair<String, String>> nameValueCollection)
            throws IOException {
        requireNonNull(nameValueCollection);

        return new HttpContent(
                FormEncoder.createQueryString(nameValueCollection).getBytes(ApiResource.CHARSET),
                String.format("application/x-www-form-urlencoded;charset=%s", ApiResource.CHARSET));
    }

    /** The request's content, as a string. */
    public String stringContent() {
        return new String(this.byteArrayContent, ApiResource.CHARSET);
    }

    /**
     * Builds a new HttpContent for name/value tuples encoded using {@code multipart/form-data} MIME
     * type.
     *
     * @param nameValueCollection the collection of name/value tuples to encode
     * @param filename name of the file read via an InputStream (for non-InputStream args can be null)
     * @return the encoded HttpContent instance
     * @throws IllegalArgumentException if nameValueCollection is null
     */
    public static HttpContent buildMultipartFormDataContent(
            Collection<KeyValuePair<String, Object>> nameValueCollection, String filename) throws IOException {
        String boundary = UUID.randomUUID().toString();
        return buildMultipartFormDataContent(nameValueCollection, boundary, filename);
    }

    /**
     * Builds a new HttpContent for name/value tuples encoded using {@code multipart/form-data} MIME
     * type.
     *
     * @param nameValueCollection the collection of name/value tuples to encode
     * @param boundary the boundary
     * @param filename name of the file read via an InputStream (for non-InputStream args can be null)
     * @return the encoded HttpContent instance
     * @throws IllegalArgumentException if nameValueCollection is null
     */
    public static HttpContent buildMultipartFormDataContent(
            Collection<KeyValuePair<String, Object>> nameValueCollection, String boundary, String filename)
            throws IOException {
        requireNonNull(nameValueCollection);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MultipartProcessor multipartProcessor = null;
        try {
            multipartProcessor = new MultipartProcessor(baos, boundary, ApiResource.CHARSET);

            for (KeyValuePair<String, Object> entry : nameValueCollection) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value instanceof File) {
                    File file = (File) value;
                    multipartProcessor.addFileField(key, file.getName(), new FileInputStream(file));
                } else if (value instanceof InputStream) {
                    multipartProcessor.addFileField(key, filename, (InputStream) value);
                } else {
                    multipartProcessor.addFormField(key, (String) value);
                }
            }
        } finally {
            if (multipartProcessor != null) {
                multipartProcessor.finish();
            }
        }

        return new HttpContent(baos.toByteArray(), String.format("multipart/form-data; boundary=%s", boundary));
    }
}
