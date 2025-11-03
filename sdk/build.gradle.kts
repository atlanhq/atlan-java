/* SPDX-License-Identifier: Apache-2.0 */
val versionId: String = providers.gradleProperty("VERSION_NAME").get()
val jarName = "atlan-java"
version = versionId

plugins {
    id("com.atlan.java")
    id("com.adarshr.test-logger")
    alias(libs.plugins.shadow)
    alias(libs.plugins.git.publish)
    `maven-publish`
    signing
}

dependencies {
    api(platform(libs.otel.bom))
    api(libs.jackson.databind)
    api(libs.jackson.yaml)
    api(libs.slf4j)
    api(libs.elasticsearch.java)
    api(libs.freemarker)
    api(libs.openlineage)
    implementation(libs.classgraph)
    implementation(libs.rocksdb)
    // implementation(variantOf(libs.rocksdb) { classifier("linux64") })
    // implementation(variantOf(libs.rocksdb) { classifier("linux32") })
    // implementation(variantOf(libs.rocksdb) { classifier("linux64-musl") })
    // implementation(variantOf(libs.rocksdb) { classifier("linux32-musl") })
    // implementation(variantOf(libs.rocksdb) { classifier("osx") })
    // implementation(variantOf(libs.rocksdb) { classifier("win64") })
    testImplementation(libs.bundles.java.test)
    testImplementation(libs.bundles.log4j)
    testImplementation(project(":mocks"))
}

tasks {
    test {
        useTestNG {
            options {
                testLogging.showStandardStreams = true
            }
        }
    }
    jar {
        manifest {
            attributes(
                "Implementation-Title" to providers.gradleProperty("SDK_ARTIFACT_ID").get(),
                "Implementation-Version" to versionId,
                "Implementation-Vendor" to providers.gradleProperty("VENDOR_NAME").get(),
                "Bundle-SymbolicName" to providers.gradleProperty("SDK_ARTIFACT_ID").get(),
                "Export-Package" to "${providers.gradleProperty("GROUP").get()}.*"
            )
            archiveVersion.set(versionId)
        }
        archiveBaseName.set(jarName)
    }
    shadowJar {
        archiveBaseName.set(jarName)
        archiveClassifier.set("jar-with-dependencies")
        configurations = listOf(project.configurations.runtimeClasspath.get())
    }
    javadoc {
        title = "Atlan Java SDK $versionId"
    }
    spotlessJava {
        dependsOn("generateJava")
    }
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

tasks.register<Copy>("generateJava") {
    val templateContext = mapOf("version" to version)
    inputs.properties(templateContext) // for gradle up-to-date check
    from("src/template/java")
    into("${layout.buildDirectory.get()}/generated/java")
    expand(templateContext)
    finalizedBy("generateEffectiveLombokConfig")
}

tasks.compileJava {
    sourceSets["main"].java.srcDir("${layout.buildDirectory.get()}/generated/java")
    dependsOn(tasks.getByName("generateJava"))
}

tasks.register<Zip>("buildZip") {
    into("java/lib") {
        from(tasks.shadowJar)
    }
}

tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(tasks.delombok)
}

tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc)
}

testlogger {
    showFullStackTraces = true
}

publishing {
    publications {
        create<MavenPublication>("mavenJavaSdk") {
            groupId = providers.gradleProperty("GROUP").get()
            artifactId = providers.gradleProperty("SDK_ARTIFACT_ID").get()
            version = versionId
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
            pom {
                name.set(providers.gradleProperty("SDK_ARTIFACT_ID").get())
                description.set(providers.gradleProperty("SDK_DESCRIPTION").get())
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
