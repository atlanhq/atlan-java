// SPDX-License-Identifier: Apache-2.0
val jarPath = "$rootDir/jars"
val versionId: String = providers.gradleProperty("VERSION_NAME").get()
val jarFile = "package-toolkit-serde-$version.jar"

plugins {
    id("com.atlan.kotlin")
    alias(libs.plugins.shadow)
}

dependencies {
    api(libs.jackson.kotlin)
}

tasks {
    shadowJar {
        isZip64 = true
        archiveFileName.set(jarFile)
        destinationDirectory.set(file(jarPath))
        dependencies {
            include(dependency("org.jetbrains.kotlin:kotlin-reflect:.*"))
            include(dependency("com.fasterxml.jackson.module:jackson-module-kotlin:.*"))
        }
        mergeServiceFiles()
    }

    jar {
        dependsOn(shadowJar)
    }
}
