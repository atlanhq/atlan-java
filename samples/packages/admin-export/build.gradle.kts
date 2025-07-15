// SPDX-License-Identifier: Apache-2.0
plugins {
    id("com.atlan.kotlin-custom-package")
}

configurations.all {
    resolutionStrategy {
        // Note: force a safe version of all of these libraries, even if transitive, to avoid potential CVEs
        force(
            libs.jetty.http,
            libs.jetty.server,
            libs.jetty.http2.common,
            libs.jetty.http2.hpack,
            libs.commons.lang,
            libs.nimbus,
        )
    }
}
