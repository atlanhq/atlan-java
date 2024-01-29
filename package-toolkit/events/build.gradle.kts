// SPDX-License-Identifier: Apache-2.0
version = providers.gradleProperty("VERSION_NAME").get()
val jarName = "package-toolkit-events"

plugins {
    id("com.atlan.kotlin")
    alias(libs.plugins.shadow)
    `maven-publish`
    signing
}

dependencies {
    implementation(project(":package-toolkit:runtime"))
    api(libs.numaflow.java)
    api("io.netty:netty-transport-native-epoll:4.1.100.Final:linux-x86_64")
}

tasks {
    shadowJar {
        isZip64 = true
        archiveBaseName.set(jarName)
        archiveClassifier.set("jar-with-dependencies")
        dependencies {
            include(dependency("io.numaproj.numaflow:numaflow-java:.*"))
            include(dependency("io.netty:netty-transport-native-epoll:4.1.100.Final:linux-x86_64"))
        }
        mergeServiceFiles()
    }

    jar {
        archiveBaseName.set(jarName)
        dependsOn(shadowJar)
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJavaPkgEvents") {
            groupId = providers.gradleProperty("GROUP").get()
            artifactId = providers.gradleProperty("PKG_EVT_ARTIFACT_ID").get()
            version = providers.gradleProperty("VERSION_NAME").get()
            from(components["java"])
            pom {
                name.set(providers.gradleProperty("PKG_EVT_ARTIFACT_ID").get())
                description.set(providers.gradleProperty("PKG_EVT_DESCRIPTION").get())
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
    sign(publishing.publications["mavenJavaPkgEvents"])
}
