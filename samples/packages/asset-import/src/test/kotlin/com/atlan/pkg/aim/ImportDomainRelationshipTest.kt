/* SPDX-License-Identifier: Apache-2.0
   Copyright 2025 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils

class ImportDomainRelationshipTest : PackageTest("idr") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val testFile = "input.csv"
    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    override fun setup() {
        TODO("Not yet implemented")
    }

    override fun teardown() {
        TODO("Not yet implemented")
    }
}
