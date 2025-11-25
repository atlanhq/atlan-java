/* SPDX-License-Identifier: Apache-2.0 */
plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:2.2.21")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:8.1.0")
    implementation("io.freefair.gradle:lombok-plugin:9.1.0")
    implementation("net.ltgt.errorprone:net.ltgt.errorprone.gradle.plugin:4.3.0")
    implementation("com.adarshr:gradle-test-logger-plugin:4.0.0")
    implementation("org.pkl-lang:org.pkl-lang.gradle.plugin:0.30.0")
    implementation("org.wiremock:wiremock-jetty12:3.13.2")
}
