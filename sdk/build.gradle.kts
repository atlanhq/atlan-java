/* SPDX-License-Identifier: Apache-2.0 */
val versionId = providers.gradleProperty("VERSION_NAME").get()

plugins {
    id("com.atlan.java")
    id("com.atlan.java-test")
    id("maven-publish")
    id("signing")
    id("biz.aQute.bnd.builder") version "6.1.0"
    id("org.ajoberstar.git-publish") version "3.0.1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    api("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    api("org.slf4j:slf4j-api:2.0.7")
    api("co.elastic.clients:elasticsearch-java:8.8.2")
    api("org.freemarker:freemarker:2.3.32")
    api("org.apache.commons:commons-csv:1.10.0")
    implementation("io.github.classgraph:classgraph:4.8.160")
}

tasks.jar {
    manifest {
        attributes(
            "Implementation-Title" to providers.gradleProperty("POM_NAME").get(),
            "Implementation-Version" to versionId,
            "Implementation-Vendor" to providers.gradleProperty("VENDOR_NAME").get(),
            "Bundle-SymbolicName" to providers.gradleProperty("POM_ARTIFACT_ID").get(),
            "Export-Package" to "${providers.gradleProperty("GROUP").get()}.*")
        archiveVersion.set(versionId)
    }
    archiveFileName.set("atlan-java-$versionId.jar")
}

tasks.shadowJar {
    archiveFileName.set("atlan-java-$versionId-jar-with-dependencies.jar")
    configurations = listOf(project.configurations.runtimeClasspath.get())
}

gitPublish {
    repoUri.set("https://github.com/atlanhq/atlan-java.git")
    branch.set("gh-pages")
    sign.set(false) // disable commit signing

    contents {
        from(tasks.javadoc) {
            into(".")
        }
    }
}

tasks.create<Zip>("buildZip") {
    into("java/lib") {
        from(tasks.shadowJar)
    }
}
