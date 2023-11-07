/* SPDX-License-Identifier: Apache-2.0 */
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

plugins {
    id("java-library")
    id("jvm-test-suite")
    id("maven-publish")
    id("signing")
    id("io.freefair.lombok") version "6.3.0"
    id("biz.aQute.bnd.builder") version "6.1.0"
    id("org.ajoberstar.git-publish") version "3.0.1"
}

apply(from = "deploy.gradle")
