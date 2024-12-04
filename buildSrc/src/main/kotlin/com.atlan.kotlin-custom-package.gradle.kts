/* SPDX-License-Identifier: Apache-2.0 */
val jarPath = "$rootDir/jars"

plugins {
    kotlin("jvm")
    id("org.pkl-lang")
    id("com.diffplug.spotless")
    id("jvm-test-suite")
    id("com.adarshr.test-logger")
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":sdk"))
    implementation(project(":package-toolkit:runtime"))
    implementation(project(":package-toolkit:config"))
    // In your own project, you would use these in place of the 3 dependencies above:
    //implementation("com.atlan:atlan-java:+")
    //implementation("com.atlan:package-toolkit-runtime:+")
    //implementation("com.atlan:package-toolkit-config:+")
    implementation(versionCatalogs.named("libs").findLibrary("kotlin-logging").orElseThrow(::AssertionError))
    runtimeOnly(versionCatalogs.named("libs").findBundle("log4j").orElseThrow(::AssertionError))
    testImplementation(project(":package-toolkit:testing"))
    // In your own project, you would use this in place of the 1 dependency above:
    //testImplementation("com.atlan:package-toolkit-testing:+")
    testImplementation(versionCatalogs.named("libs").findLibrary("kotlin-test").orElseThrow(::AssertionError))
}

pkl {
    evaluators {
        register("genCustomPkg") {
            sourceModules.add("src/main/resources/package.pkl")
            modulePath.from(file("$rootDir/package-toolkit/config/src/main/resources"))
            multipleFileOutputDir.set(layout.projectDirectory)
            externalProperties.put("VERSION_NAME", version.toString())
        }
    }
}

tasks {
    jar {
        destinationDirectory.set(file(jarPath))
    }
    test {
        useTestNG {
            maxParallelForks = 4
            options {
                parallel = "classes"
                threadCount = 1
                testLogging.showStandardStreams = true
            }
        }
        testLogging {
            events("failed")
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
        onlyIf {
            project.hasProperty("packageTests")
        }
    }
    clean {
        delete(jarPath)
    }
    getByName("genCustomPkg") {
        dependsOn(":package-toolkit:config:processResources")
        dependsOn("compileKotlin")
    }
    getByName("genCustomPkgGatherImports") {
        dependsOn(":package-toolkit:config:processResources")
    }
    assemble {
        dependsOn("genCustomPkg")
    }
    processResources {
        dependsOn("genCustomPkg")
    }
    processTestResources {
        dependsOn("genCustomPkg")
    }
}

configurations.all {
    resolutionStrategy {
        // Note: force a safe version of all of these libraries, even if transitive, to avoid potential CVEs
        force(
            versionCatalogs.named("libs").findLibrary("parsson").orElseThrow(::AssertionError),
            versionCatalogs.named("libs").findLibrary("json-path").orElseThrow(::AssertionError),
            versionCatalogs.named("libs").findLibrary("guava").orElseThrow(::AssertionError),
            versionCatalogs.named("libs").findLibrary("commons-compress").orElseThrow(::AssertionError),
            versionCatalogs.named("libs").findLibrary("commons-io").orElseThrow(::AssertionError),
        )
    }
}

kotlin {
    jvmToolchain(17)
}

spotless {
    kotlin {
        licenseHeaderFile("$rootDir/LICENSE_HEADER")
        ktlint()
    }
    kotlinGradle {
        ktlint()
    }
}
