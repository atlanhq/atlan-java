/* SPDX-License-Identifier: Apache-2.0 */
plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:2.1.10")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:7.0.2")
    implementation("io.freefair.gradle:lombok-plugin:8.12.2.1")
    implementation("net.ltgt.gradle:gradle-errorprone-plugin:4.0.1")
    implementation("com.adarshr:gradle-test-logger-plugin:4.0.0")
    implementation("org.pkl-lang:org.pkl-lang.gradle.plugin:0.28.0")
}
