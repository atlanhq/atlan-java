/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanResponseInterface;
import java.io.InputStream;

/**
 * Interface through which API interaction wrapping is handled.
 */
public interface AtlanResponseGetter {
    <T extends AtlanResponseInterface> T request(
            ApiResource.RequestMethod method, String url, String body, Class<T> clazz, RequestOptions options)
            throws AtlanException;

    <T extends AtlanResponseInterface> T request(
            ApiResource.RequestMethod method,
            String url,
            InputStream upload,
            String filename,
            Class<T> clazz,
            RequestOptions options)
            throws AtlanException;
}
