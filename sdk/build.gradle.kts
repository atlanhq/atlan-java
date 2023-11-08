/* SPDX-License-Identifier: Apache-2.0 */
val versionId = providers.gradleProperty("VERSION_NAME").get()

plugins {
    id("com.atlan.java")
    id("com.atlan.java-test")
    id("biz.aQute.bnd.builder") version "6.1.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.ajoberstar.git-publish") version "3.0.1"
    `maven-publish`
    signing
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
    archiveClassifier.set("jar-with-dependencies")
    configurations = listOf(project.configurations.runtimeClasspath.get())
}

tasks.javadoc {
    title = "Atlan Java SDK $versionId"
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

tasks.create<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    //classifier = "sources"
    from(tasks.delombok)
}

tasks.create<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    //classifier = "javadoc"
    from(tasks.javadoc.get().destinationDir)
}

publishing {
    publications {
        create<MavenPublication>("mavenJavaSdk") {
            groupId = providers.gradleProperty("GROUP").get()
            artifactId = providers.gradleProperty("POM_ARTIFACT_ID").get()
            version = providers.gradleProperty("VERSION_NAME").get()
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
            pom {
                name.set(providers.gradleProperty("POM_NAME").get())
                description.set(providers.gradleProperty("POM_DESCRIPTION").get())
                url.set(providers.gradleProperty("POM_URL").get())
                packaging = providers.gradleProperty("POM_PACKAGING").get()
                licenses {
                    license {
                        name.set(providers.gradleProperty("POM_LICENCE_NAME").get())
                        url.set(providers.gradleProperty("POM_LICENCE_URL").get())
                        distribution.set(providers.gradleProperty("POM_LICENCE_DIST").get())
                    }
                }
                developers {
                    developer {
                        id.set(providers.gradleProperty("POM_DEVELOPER_ID").get())
                        name.set(providers.gradleProperty("POM_DEVELOPER_NAME").get())
                        email.set(providers.gradleProperty("POM_DEVELOPER_EMAIL").get())
                    }
                }
                organization {
                    name.set(providers.gradleProperty("POM_DEVELOPER_NAME").get())
                    url.set(providers.gradleProperty("POM_ORGANIZATION_URL").get())
                }
                scm {
                    connection.set(providers.gradleProperty("POM_SCM_CONNECTION").get())
                    developerConnection.set(providers.gradleProperty("POM_SCM_DEV_CONNECTION").get())
                    url.set(providers.gradleProperty("POM_SCM_URL").get())
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["mavenJavaSdk"])
}
