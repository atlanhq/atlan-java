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
    implementation("com.diffplug.spotless:spotless-plugin-gradle:7.2.1")
    implementation("io.freefair.gradle:lombok-plugin:9.0.0")
    implementation("net.ltgt.errorprone:net.ltgt.errorprone.gradle.plugin:4.2.0")
    implementation("com.adarshr:gradle-test-logger-plugin:4.0.0")
    implementation("org.pkl-lang:org.pkl-lang.gradle.plugin:0.29.1")
    implementation("org.wiremock:wiremock-jetty12:3.13.1")
}
