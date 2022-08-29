/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanResponseInterface;

public interface AtlanResponseGetter {
    <T extends AtlanResponseInterface> T request(
            ApiResource.RequestMethod method, String url, String body, Class<T> clazz, RequestOptions options)
            throws AtlanException;
}
