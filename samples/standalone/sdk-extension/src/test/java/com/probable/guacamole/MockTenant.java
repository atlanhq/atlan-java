/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.probable.guacamole;

import com.atlan.mock.MockAtlanTenant;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class MockTenant extends MockAtlanTenant {
    @BeforeSuite
    void start() throws InterruptedException {
        initializeClient();
    }

    @AfterSuite
    void stop() {
        closeClient();
    }
}
