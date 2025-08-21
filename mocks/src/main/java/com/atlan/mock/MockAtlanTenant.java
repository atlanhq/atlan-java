/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.mock;

import com.atlan.AtlanClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockAtlanTenant {

    public static AtlanClient client = null;

    public void createClient() {
        client = new AtlanClient("http://localhost:8765", "unused");
    }

    public void closeClient() {
        try {
            client.close();
        } catch (Exception e) {
            log.error("Unable to close client used for unit tests.", e);
        }
    }
}
