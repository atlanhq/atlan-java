// SPDX-License-Identifier: Apache-2.0
plugins {
    id("com.atlan.java")
}

dependencies {
    implementation(project(":sdk"))
    implementation(libs.bundles.java.test)
    runtimeOnly(libs.bundles.log4j)
}
