/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.util;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import static java.util.Objects.requireNonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import lombok.Cleanup;

/**
 * Utilities for working with streams of data.
 */
public final class StreamUtils {
    private static final int DEFAULT_BUF_SIZE = 8192;
    private static final int MAX_SIZE = 1024 * 1024 * 1024; // 1GB

    /**
     * Reads the provided stream until the end and returns a string encoded with the provided charset.
     *
     * @param stream the stream to read
     * @param charset the charset to use
     * @return a string with the contents of the input stream
     * @throws NullPointerException if {@code stream} or {@code charset} is {@code null}
     * @throws IOException if an I/O error occurs
     */
    public static String readToEnd(InputStream stream, Charset charset) throws IOException {
        requireNonNull(stream);
        requireNonNull(charset);

        int anticipatedSize = stream.available();
        validateSize(anticipatedSize);

        @Cleanup
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(Math.max(anticipatedSize, DEFAULT_BUF_SIZE));
        final byte[] buffer = new byte[DEFAULT_BUF_SIZE];

        int bytesRead;
        int totalBytes = 0;
        while ((bytesRead = stream.read(buffer)) != -1) {
            totalBytes += bytesRead;
            validateSize(totalBytes);
            baos.write(buffer, 0, bytesRead);
        }
        return baos.toString(charset);
    }

    private static void validateSize(int size) throws IOException {
        if (size > MAX_SIZE) {
            throw new IOException("Response exceeds " + MAX_SIZE + " bytes -- too large to be processed.");
        }
    }
}
