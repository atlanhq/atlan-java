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
