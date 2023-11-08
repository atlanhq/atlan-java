/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.net.AtlanResponse;

public interface AtlanResponseInterface {
    AtlanResponse getLastResponse();

    void setLastResponse(AtlanResponse response);
}
