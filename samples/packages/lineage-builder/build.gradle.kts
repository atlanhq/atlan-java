/* SPDX-License-Identifier: Apache-2.0 */
version = providers.gradleProperty("VERSION_NAME").get()
val jarPath = "$rootDir/jars"

plugins {
    id("com.atlan.kotlin-custom-package")
    alias(libs.plugins.shadow)
    `maven-publish`
    signing
}

dependencies {
    implementation(project(":samples:packages:asset-import"))
}

pkl {
    evaluators {
        register("genCustomPkg") {
            sourceModules.add("src/main/resources/package.pkl")
            modulePath.from(file("$rootDir/package-toolkit/config/build/resources/main"))
            multipleFileOutputDir.set(layout.projectDirectory)
        }
    }
}

tasks {
    shadowJar {
        isZip64 = true
        archiveClassifier.set("")
        destinationDirectory.set(file(jarPath))
        dependencies {
            include(project(":samples:packages:asset-import"))
        }
        mergeServiceFiles()
    }
    getByName("genCustomPkg") {
        dependsOn(":package-toolkit:config:generateBuildInfo")
        dependsOn(":package-toolkit:config:processResources")
    }
    assemble {
        dependsOn(getByName("genCustomPkg"))
    }
    jar {
        // Override the default jar task so we get the shadowed jar
        // as the only jar output
        actions = listOf()
        doLast { shadowJar }
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJavaPkgCfg") {
            groupId = providers.gradleProperty("GROUP").get()
            artifactId = providers.gradleProperty("LB_ARTIFACT_ID").get()
            version = providers.gradleProperty("VERSION_NAME").get()
            from(components["java"])
            pom {
                name.set(providers.gradleProperty("LB_ARTIFACT_ID").get())
                description.set(providers.gradleProperty("LB_DESCRIPTION").get())
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
    sign(publishing.publications["mavenJavaPkgCfg"])
}
