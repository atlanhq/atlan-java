// SPDX-License-Identifier: Apache-2.0
version = providers.gradleProperty("VERSION_NAME").get()
val jarName = "package-toolkit-serde"

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
        archiveBaseName.set(jarName)
        archiveClassifier.set("jar-with-dependencies")
        dependencies {
            include(dependency("org.jetbrains.kotlin:kotlin-reflect:.*"))
            include(dependency("com.fasterxml.jackson.module:jackson-module-kotlin:.*"))
        }
        mergeServiceFiles()
    }

    jar {
        archiveBaseName.set(jarName)
        dependsOn(shadowJar)
    }
}
