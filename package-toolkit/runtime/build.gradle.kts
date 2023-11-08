// SPDX-License-Identifier: Apache-2.0
val jarPath = "$rootDir/jars"
val versionId: String = providers.gradleProperty("VERSION_NAME").get()
val jarFile = "package-toolkit-runtime-$versionId.jar"

plugins {
    id("com.atlan.kotlin")
    alias(libs.plugins.shadow)
}

tasks {
    shadowJar {
        isZip64 = true
        archiveFileName.set(jarFile)
        destinationDirectory.set(file(jarPath))
        dependencies {
            include(dependency("org.jetbrains.kotlin:.*:.*"))
            include(dependency("io.github.microutils:kotlin-logging-jvm:.*"))
            include(dependency("org.apache.logging.log4j:log4j-api:.*"))
            include(dependency("org.apache.logging.log4j:log4j-core:.*"))
            include(dependency("org.apache.logging.log4j:log4j-slf4j2-impl:.*"))
        }
        mergeServiceFiles()
    }

    jar {
        dependsOn(shadowJar)
        // Necessary to avoid log4j falling back to a non-performant way to walk the stack
        manifest {
            attributes(Pair("Multi-Release", "true"))
        }
    }
}
