// SPDX-License-Identifier: Apache-2.0
version = providers.gradleProperty("VERSION_NAME").get()
val jarName = "package-toolkit-test"

plugins {
    id("com.atlan.kotlin")
}

dependencies {
    implementation(project(":package-toolkit:runtime"))
}
