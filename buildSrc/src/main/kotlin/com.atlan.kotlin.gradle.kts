/* SPDX-License-Identifier: Apache-2.0 */
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    id("com.diffplug.spotless")
    id("com.adarshr.test-logger")
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":sdk"))
    implementation(versionCatalogs.named("libs").findLibrary("kotlin-logging").orElseThrow(::AssertionError))
    runtimeOnly(versionCatalogs.named("libs").findBundle("log4j").orElseThrow(::AssertionError))
    testImplementation(versionCatalogs.named("libs").findLibrary("testng").orElseThrow(::AssertionError))
    testImplementation(versionCatalogs.named("libs").findLibrary("kotlin-test").orElseThrow(::AssertionError))
}

tasks {
    test {
        useTestNG {
            maxParallelForks = 4
            options {
                parallel = "classes"
                threadCount = 1
            }
        }
    }
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

spotless {
    kotlin {
        licenseHeaderFile("$rootDir/LICENSE_HEADER")
        ktlint()
    }
    kotlinGradle {
        ktlint()
    }
}
