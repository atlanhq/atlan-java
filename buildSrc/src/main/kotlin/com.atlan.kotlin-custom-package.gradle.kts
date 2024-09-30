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
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    runtimeOnly("org.apache.logging.log4j:log4j-core:2.24.1")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j2-impl:2.24.1")
    testImplementation(project(":package-toolkit:testing"))
    // In your own project, you would use this in place of the 1 dependency above:
    //testImplementation("com.atlan:package-toolkit-testing:+")
    testImplementation("org.jetbrains.kotlin:kotlin-test:2.0.20")
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
        onlyIf {
            project.hasProperty("packageTests")
        }
    }
    clean {
        delete(jarPath)
    }
    getByName("genCustomPkg") {
        dependsOn(":package-toolkit:config:generateBuildInfo")
        dependsOn(":package-toolkit:config:processResources")
        dependsOn("compileKotlin")
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
