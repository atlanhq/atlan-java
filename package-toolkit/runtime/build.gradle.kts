// SPDX-License-Identifier: Apache-2.0
version = providers.gradleProperty("VERSION_NAME").get()
val jarName = "package-toolkit-runtime"

plugins {
    id("com.atlan.kotlin")
    alias(libs.plugins.shadow)
    `maven-publish`
    signing
}

dependencies {
    api(libs.jackson.kotlin)
    api(libs.fastcsv)
    api(libs.bundles.poi)
    api(libs.awssdk.s3)
    implementation(libs.simple.java.mail)
    // You would not need the dependencies below in reality, they are to simulate a running tenant
    testImplementation(libs.bundles.java.test)
    testImplementation(project(":mocks"))
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
            include(dependency("org.jetbrains.kotlin:kotlin-reflect:.*"))
            // JSON
            include(dependency("com.fasterxml.jackson.module:jackson-module-kotlin:.*"))
            // CSV
            include(dependency("de.siegmar:fastcsv:.*"))
            // XLS(X)
            include(dependency("org.apache.poi:poi:.*"))
            include(dependency("commons-codec:commons-codec:.*"))
            include(dependency("org.apache.commons:commons-collections4:.*"))
            include(dependency("org.apache.commons:commons-math3:.*"))
            include(dependency("commons-io:commons-io:.*"))
            include(dependency("com.zaxxer:SparseBitSet:.*"))
            include(dependency("org.apache.poi:poi-ooxml:.*"))
            include(dependency("org.apache.poi:poi-ooxml-lite:.*"))
            include(dependency("org.apache.xmlbeans:xmlbeans:.*"))
            include(dependency("org.apache.commons:commons-compress:.*"))
            include(dependency("com.github.virtuald:curvesapi:.*"))
            // Email
            include(dependency("org.simplejavamail:simple-java-mail:.*"))
            include(dependency("org.simplejavamail:core-module:.*"))
            include(dependency("com.sun.mail:jakarta.mail:.*"))
            include(dependency("com.sun.activation:jakarta.activation:.*"))
            include(dependency("com.sanctionco.jmail:jmail:.*"))
            include(dependency("com.github.bbottema:jetbrains-runtime-annotations:.*"))
            include(dependency("com.pivovarit:throwing-function:.*"))
            // S3
            include(dependency("software.amazon.awssdk:.*:.*"))
            include(dependency("org.reactivestreams:reactive-streams:.*"))
            include(dependency("org.apache.httpcomponents:httpclient:.*"))
            include(dependency("org.apache.httpcomponents:httpcore:.*"))
            include(dependency("commons-logging:commons-logging:.*"))
            include(dependency("commons-codec:commons-codec:.*"))
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
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJavaPkgRun") {
            groupId = providers.gradleProperty("GROUP").get()
            artifactId = providers.gradleProperty("PKG_RUN_ARTIFACT_ID").get()
            version = providers.gradleProperty("VERSION_NAME").get()
            from(components["java"])
            pom {
                name.set(providers.gradleProperty("PKG_RUN_ARTIFACT_ID").get())
                description.set(providers.gradleProperty("PKG_RUN_DESCRIPTION").get())
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
