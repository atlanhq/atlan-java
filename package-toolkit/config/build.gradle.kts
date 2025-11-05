// SPDX-License-Identifier: Apache-2.0
version = providers.gradleProperty("VERSION_NAME").get()
val jarName = "package-toolkit-config"

plugins {
    id("com.atlan.kotlin")
    id("org.pkl-lang")
    alias(libs.plugins.shadow)
    `maven-publish`
    signing
}

dependencies {
    api(libs.pkl.config)
    api(libs.pkl.codegen)
    implementation(libs.jackson.kotlin)
    implementation(libs.jackson.yaml)
    implementation(project(":package-toolkit:runtime"))
    testImplementation(libs.pkl.config.java)
}

pkl {
    kotlinCodeGenerators {
        register("genKotlin") {
            indent.set("    ")
            outputDir.set(layout.projectDirectory.dir("src/main"))
            sourceModules.add(file("src/main/resources/Connectors.pkl"))
            sourceModules.add(file("src/main/resources/Credential.pkl"))
            sourceModules.add(file("src/main/resources/Config.pkl"))
            sourceModules.add(file("src/main/resources/Renderers.pkl"))
        }
    }
    project {
        packagers {
            register("makePklPackages") {
                projectDirectories.from(file("src/main/resources/"))
                environmentVariables.put("VERSION_NAME", version.toString())
            }
        }
    }
}

tasks {
    shadowJar {
        isZip64 = true
        archiveBaseName.set(jarName)
        archiveClassifier.set("jar-with-dependencies")
        dependencies {
            include(dependency("org.pkl-lang:pkl-config-kotlin:.*"))
            include(dependency("org.pkl-lang:pkl-config-java-all:.*"))
            include(dependency("com.fasterxml.jackson.module:jackson-module-kotlin:.*"))
            include(dependency("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:.*"))
            include(dependency("org.jetbrains.kotlin:kotlin-reflect:.*"))
        }
        mergeServiceFiles()
        dependsOn(
            ":package-toolkit:runtime:genPklConnectors",
        )
    }
    jar {
        archiveBaseName.set(jarName)
        dependsOn(shadowJar)
    }
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        dependsOn(
            ":package-toolkit:runtime:genPklConnectors",
        )
    }
    assemble {
        dependsOn("makePklPackages")
    }
    getByName("genKotlin") {
        dependsOn(":package-toolkit:runtime:genPklConnectorsGatherImports")
    }
    getByName("makePklPackages") {
        dependsOn("processResources")
    }
}

tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJavaPkgCfg") {
            groupId = providers.gradleProperty("GROUP").get()
            artifactId = providers.gradleProperty("PKG_CFG_ARTIFACT_ID").get()
            version = providers.gradleProperty("VERSION_NAME").get()
            from(components["java"])
            pom {
                name.set(providers.gradleProperty("PKG_CFG_ARTIFACT_ID").get())
                description.set(providers.gradleProperty("PKG_CFG_DESCRIPTION").get())
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

spotless {
    // For now disable the check on generated code, as it will not be properly formatted
    kotlin {
        targetExclude(
            "src/main/kotlin/com/atlan/pkg/Config.kt",
            "src/main/kotlin/com/atlan/pkg/Connectors.kt",
            "src/main/kotlin/com/atlan/pkg/Credential.kt",
            "src/main/kotlin/com/atlan/pkg/Renderers.kt",
        )
    }
}
