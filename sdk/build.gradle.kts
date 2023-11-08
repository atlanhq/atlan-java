/* SPDX-License-Identifier: Apache-2.0 */
val versionId = providers.gradleProperty("VERSION_NAME").get()

plugins {
    id("com.atlan.java")
    id("com.atlan.java-test")
    //id("biz.aQute.bnd.builder") version "6.1.0"
    alias(libs.plugins.shadow)
    alias(libs.plugins.git.publish)
    `maven-publish`
    signing
}

dependencies {
    api(libs.jackson.databind)
    api(libs.slf4j)
    api(libs.elasticsearch.java)
    api(libs.freemarker)
    implementation(libs.classgraph)
    testImplementation(libs.bundles.java.test)
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
    archiveFileName.set("atlan-java-$versionId-${archiveClassifier.get()}.jar")
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
    from(tasks.delombok)
}

tasks.create<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc)
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
