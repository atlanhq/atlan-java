/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag
import LakeFormationTagSyncCfg
import com.atlan.pkg.PackageTest
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class LakeTagSynchronizerTest : PackageTest() {

    @BeforeClass
    fun beforeClass() {
        setup(
            LakeFormationTagSyncCfg(
                "CLOUD",
                "s3",
                "stuff",
                null,
                null,
                null,
            ),
        )
        LakeTagSynchronizer.main(arrayOf(testDirectory))
        Thread.sleep(15000)
    }

    @Test
    fun stuffHappened() {
        println("done")
    }
}
