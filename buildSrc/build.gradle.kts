/* SPDX-License-Identifier: Apache-2.0 */
plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.22")
    // Note: we must use a back-version of spotless here otherwise we get spurious errors about
    // max line length being exceeded (see: https://github.com/diffplug/spotless/issues/1913
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.21.0")
    implementation("io.freefair.gradle:lombok-plugin:8.6")
    implementation("net.ltgt.gradle:gradle-errorprone-plugin:3.1.0")
    implementation("com.adarshr:gradle-test-logger-plugin:4.0.0")
    implementation("org.pkl-lang:org.pkl-lang.gradle.plugin:0.25.2")
}
