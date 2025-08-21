/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.mock

import org.testng.annotations.AfterSuite
import org.testng.annotations.BeforeSuite

class MockTenant : MockAtlanTenant() {
    @BeforeSuite
    fun start() {
        createClient()
    }

    @AfterSuite
    fun stop() {
        closeClient()
    }
}
