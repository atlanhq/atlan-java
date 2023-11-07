/* SPDX-License-Identifier: Apache-2.0
buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath("io.codearte.gradle.nexus:gradle-nexus-staging-plugin:0.30.0")
    }
}

repositories {
    mavenCentral()
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

plugins {
    id("java-library")
    id("jvm-test-suite")
    id("io.freefair.lombok") version "6.3.0"
    id("biz.aQute.bnd.builder") version "6.1.0"
}

apply(from = "deploy.gradle")
*/
