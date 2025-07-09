// SPDX-License-Identifier: Apache-2.0
plugins {
    id("com.atlan.java")
}

dependencies {
    implementation(project(":sdk"))
    implementation(libs.bundles.java.test)
    runtimeOnly(libs.bundles.log4j)
}

configurations.all {
    resolutionStrategy {
        // Note: force a safe version of all of these libraries, even if transitive, to avoid potential CVEs
        force(
            libs.jetty,
            libs.jetty.http,
            libs.jetty.server,
            libs.jetty.http2.common,
            libs.jetty.http2.hpack,
        )
    }
}
