// SPDX-License-Identifier: Apache-2.0
version = providers.gradleProperty("VERSION_NAME").get()
val jarName = "package-toolkit-testing"

plugins {
    id("com.atlan.kotlin")
    alias(libs.plugins.shadow)
    `maven-publish`
    signing
}

dependencies {
    implementation(project(":package-toolkit:runtime"))
    api(libs.system.stubs)
    api(libs.bundles.java.test)
}

tasks {
    shadowJar {
        isZip64 = true
        archiveBaseName.set(jarName)
        archiveClassifier.set("jar-with-dependencies")
        dependencies {
            include(dependency("org.jetbrains.kotlin:.*:.*"))
            include(dependency("io.github.microutils:kotlin-logging-jvm:.*"))
            include(dependency("org.apache.logging.log4j:log4j-api:.*"))
            include(dependency("org.apache.logging.log4j:log4j-core:.*"))
            include(dependency("org.apache.logging.log4j:log4j-slf4j2-impl:.*"))
            include(dependency("com.aventrix.jnanoid:jnanoid:.*"))
            include(dependency("org.testng:testng:.*"))
            include(dependency("com.github.tomakehurst:wiremock:.*"))
            include(dependency("uk.org.webcompere:system-stubs-testng:.*"))
        }
        mergeServiceFiles()
    }

    jar {
        archiveBaseName.set(jarName)
        dependsOn(shadowJar)
        // Necessary to avoid log4j falling back to a non-performant way to walk the stack
        manifest {
            attributes(Pair("Multi-Release", "true"))
        }
    }

    shadowJar {
        dependsOn(":package-toolkit:runtime:genPklConnectors")
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJavaPkgRun") {
            groupId = providers.gradleProperty("GROUP").get()
            artifactId = providers.gradleProperty("PKG_TST_ARTIFACT_ID").get()
            version = providers.gradleProperty("VERSION_NAME").get()
            from(components["java"])
            pom {
                name.set(providers.gradleProperty("PKG_TST_ARTIFACT_ID").get())
                description.set(providers.gradleProperty("PKG_TST_DESCRIPTION").get())
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
    sign(publishing.publications["mavenJavaPkgRun"])
}

configurations.all {
    resolutionStrategy {
        // Note: force a safe version of all of these libraries, even if transitive, to avoid potential CVEs
        force(
            libs.jetty,
            libs.jetty.http,
            libs.jetty.server,
            libs.jetty.http2.common,
            libs.jetty.http2.hpack,
        )
    }
}
