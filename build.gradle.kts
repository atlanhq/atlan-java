/* SPDX-License-Identifier: Apache-2.0 */
plugins {
    id("com.atlan.java")
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    id("org.owasp.dependencycheck") version "12.1.9"
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
        }
    }
}

dependencyCheck {
    nvd {
        apiKey = System.getenv("NVD_API_KEY") ?: ""
    }
    failBuildOnCVSS = 7.0F
}

val wireMockService = gradle.sharedServices.registerIfAbsent("wireMock", WireMockService::class) {
    parameters {
        wireMockResourcesDir.set(layout.buildDirectory.dir("wiremock").get())
        port.set(8765)
    }
}

val prepareWireMockResources = tasks.register("prepareWireMockResources") {
    group = "test setup"
    description = "Copies shared WireMock resources and allows module overrides"

    doLast {
        val wireMockDir = layout.buildDirectory.dir("wiremock").get().asFile
        wireMockDir.mkdirs()

        val mappingsDir = File(wireMockDir, "mappings")
        val filesDir = File(wireMockDir, "__files")
        mappingsDir.mkdirs()
        filesDir.mkdirs()

        // Copy shared WireMock files from mock module
        val mockModuleDir = project(":mocks").projectDir
        val sharedMappings = File(mockModuleDir, "src/main/resources/wiremock/mappings")
        val sharedFiles = File(mockModuleDir, "src/main/resources/wiremock/__files")

        if (sharedMappings.exists()) {
            sharedMappings.copyRecursively(mappingsDir, overwrite = true)
            println("Copied shared mappings from mock module")
        }

        if (sharedFiles.exists()) {
            sharedFiles.copyRecursively(filesDir, overwrite = true)
            println("Copied shared response files from mock module")
        }

        // Allow individual modules to override/extend
        allprojects.forEach { subproject ->
            if (subproject != rootProject && subproject.name != "mocks") {
                val moduleMappings = File(subproject.projectDir, "src/test/resources/wiremock/mappings")
                val moduleFiles = File(subproject.projectDir, "src/test/resources/wiremock/__files")

                if (moduleMappings.exists()) {
                    moduleMappings.copyRecursively(mappingsDir, overwrite = true)
                    println("Applied ${subproject.name} module mapping overrides")
                }

                if (moduleFiles.exists()) {
                    moduleFiles.copyRecursively(filesDir, overwrite = true)
                    println("Applied ${subproject.name} module file overrides")
                }
            }
        }

        println("WireMock resources prepared in ${wireMockDir.absolutePath}")
    }
}

val startWireMock = tasks.register("startWireMock") {
    group = "test setup"
    description = "Starts WireMock server for all tests"
    usesService(wireMockService)
    dependsOn(prepareWireMockResources)

    doLast {
        wireMockService.get().startServer()
    }
}

val stopWireMock = tasks.register("stopWireMock") {
    group = "test setup"
    description = "Stops WireMock server after all tests"
    usesService(wireMockService)

    doLast {
        wireMockService.get().stopServer()
    }
}

allprojects {
    tasks.withType<JavaCompile> {
        options.isFork = true
        options.forkOptions.jvmArgs = listOf(
            "-Xmx4g",
            "-Xms2g"
        )
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions.freeCompilerArgs.add("-Xannotation-default-target=param-property")
    }
    tasks.withType<Test> {
        dependsOn(startWireMock)
        finalizedBy(stopWireMock)
    }
    configurations.all {
        resolutionStrategy {
            // Note: force a safe version of all of these libraries, even if transitive, to avoid potential CVEs
            force(
                libs.parsson,
                libs.json.path,
                libs.json.smart,
                libs.guava,
                libs.commons.compress,
                libs.commons.io,
                libs.jetty.http,
                libs.jetty.server,
                libs.jetty.http2.common,
                libs.jetty.http2.hpack,
                libs.jetty.http2.server,
                libs.netty.common,
                libs.netty.codec.http2,
                libs.rhino,
                libs.commons.lang,
                libs.nimbus,
            )
        }
    }
}
