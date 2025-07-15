// SPDX-License-Identifier: Apache-2.0
val jarPath = "$rootDir/jars"

plugins {
    id("com.atlan.kotlin-custom-package")
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(project(":samples:packages:asset-import"))
}

tasks {
    shadowJar {
        isZip64 = true
        destinationDirectory.set(file(jarPath))
        dependencies {
            include(project(":samples:packages:asset-import"))
        }
        mergeServiceFiles()
        dependsOn(":package-toolkit:runtime:genPklConnectors")
    }
    jar {
        // Override the default jar task so we get the shadowed jar
        // as the only jar output
        actions = listOf()
        doLast { shadowJar }
    }
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
