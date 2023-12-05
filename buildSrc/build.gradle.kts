/* SPDX-License-Identifier: Apache-2.0 */
plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.21")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.21.0")
    implementation( "io.freefair.gradle:lombok-plugin:8.4")
    implementation("net.ltgt.gradle:gradle-errorprone-plugin:3.1.0")
    implementation("com.adarshr:gradle-test-logger-plugin:4.0.0")
}
