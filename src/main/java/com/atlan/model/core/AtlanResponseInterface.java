/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import com.atlan.net.AtlanResponse;

public interface AtlanResponseInterface {
    AtlanResponse getLastResponse();

    void setLastResponse(AtlanResponse response);
}
