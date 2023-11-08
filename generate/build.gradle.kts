/* SPDX-License-Identifier: Apache-2.0 */
plugins {
    id("com.atlan.java")
}

dependencies {
    implementation(project(":sdk"))
    implementation("ch.qos.logback:logback-classic:1.4.8")
}
