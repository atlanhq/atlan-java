// SPDX-License-Identifier: Apache-2.0
plugins {
    id("com.atlan.java")
    id("com.atlan.java-test")
}

dependencies {
    implementation(project(":sdk"))
    // In your own project, you would use this in place of the 1 dependency above:
    //implementation("com.atlan:atlan-java:+")
    runtimeOnly(libs.bundles.log4j)
    testImplementation(project(":mocks"))
}
