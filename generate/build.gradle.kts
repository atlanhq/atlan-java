/* SPDX-License-Identifier: Apache-2.0 */
buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

repositories {
    mavenCentral()
}

plugins {
    id("java-library")
    id("jvm-test-suite")
    id("maven-publish")
    id("signing")
    id("io.freefair.lombok") version "6.3.0"
    id("com.diffplug.spotless") version "6.4.0"
    id("biz.aQute.bnd.builder") version "6.1.0"
}

dependencies {
    implementation(project(":sdk"))
}
