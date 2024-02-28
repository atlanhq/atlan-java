/* SPDX-License-Identifier: Apache-2.0 */
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
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    runtimeOnly("org.apache.logging.log4j:log4j-core:2.23.0")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j2-impl:2.23.0")
    testImplementation("org.testng:testng:7.8.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.22")
}

tasks {
    test {
        useTestNG()
    }
}

kotlin {
    jvmToolchain(17)
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
