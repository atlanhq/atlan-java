/* SPDX-License-Identifier: Apache-2.0 */
plugins {
    kotlin("jvm")
    id("org.pkl-lang")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":typedef-toolkit:model"))
}
