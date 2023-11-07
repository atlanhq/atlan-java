/* SPDX-License-Identifier: Apache-2.0 */
plugins {
    id("com.atlan.java")
    id("maven-publish")
    id("signing")
    id("biz.aQute.bnd.builder") version "6.1.0"
}

dependencies {
    implementation(project(":sdk"))
}
