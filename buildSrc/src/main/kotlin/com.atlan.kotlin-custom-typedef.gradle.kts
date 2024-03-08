/* SPDX-License-Identifier: Apache-2.0 */
val jarPath = "$rootDir/jars"

plugins {
    kotlin("jvm")
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
    implementation(project(":typedef-toolkit:config"))
    // In your own project, you would use these in place of the 3 dependencies above:
    //implementation("com.atlan:atlan-java:+")
    //implementation("com.atlan:typedef-toolkit-config:+")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    runtimeOnly("org.apache.logging.log4j:log4j-core:2.23.0")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j2-impl:2.23.0")
    testImplementation(project(":package-toolkit:testing"))
    // In your own project, you would use this in place of the 1 dependency above:
    //testImplementation("com.atlan:package-toolkit-testing:+")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.22")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useTestNG()
            targets {
                all {
                    testTask.configure {
                        testLogging.showStandardStreams = true
                    }
                }
            }
            sources {
                java {
                    setSrcDirs(listOf("src/test/java", "src/test/kotlin"))
                }
            }
        }
    }
}

tasks {
    jar {
        destinationDirectory.set(file(jarPath))
    }
    test {
        useTestNG()
        onlyIf {
            project.hasProperty("typeTests")
        }
    }
    clean {
        delete(jarPath)
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
