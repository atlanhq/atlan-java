/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.mock;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class MockTenant extends MockAtlanTenant {
    @BeforeSuite
    void start() {
        startServer();
    }

    @AfterSuite
    void stop() {
        stopServer();
    }
}
