/* SPDX-License-Identifier: Apache-2.0 */
plugins {
    id("com.atlan.java")
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    id("org.owasp.dependencycheck") version "12.1.0"
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}

dependencyCheck {
    nvd {
        apiKey = System.getenv("NVD_API_KEY") ?: ""
    }
    failBuildOnCVSS = 7.0F
}

configurations.all {
    resolutionStrategy {
        // Note: force a safe version of all of these libraries, even if transitive, to avoid potential CVEs
        force(
            libs.parsson,
            libs.json.path,
            libs.guava,
            libs.commons.compress,
            libs.commons.io,
            libs.jetty.http,
            libs.netty.common,
            libs.otel.sdk,
            libs.otel.exporter,
        )
    }
}
