/* SPDX-License-Identifier: Apache-2.0 */
plugins {
    id("com.atlan.java")
}

dependencies {
    implementation(project(":sdk"))
    implementation(libs.bundles.log4j)
}

tasks.register<JavaExec>("genModel") {
    group = "application"
    description = "Regenerate the model for assets"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.atlan.generators.ModelGeneratorV2")
    workingDir = file("../")
}
