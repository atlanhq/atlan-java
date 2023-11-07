/* SPDX-License-Identifier: Apache-2.0 */
buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

repositories {
    mavenCentral()
}

plugins {
    id("java-library")
    id("jvm-test-suite")
    id("signing")
    id("jacoco")
    id("io.freefair.lombok") version "6.3.0"
    id("com.diffplug.spotless") version "6.4.0"
    id("com.github.kt3k.coveralls") version "2.12.0"
    id("biz.aQute.bnd.builder") version "6.1.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.adarshr.test-logger") version "3.2.0"
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(project(":sdk"))
    implementation("com.aventrix.jnanoid:jnanoid:2.0.0")
    implementation("org.testng:testng:7.8.0")
    implementation("ch.qos.logback:logback-classic:1.4.8")
    implementation("com.github.tomakehurst:wiremock:2.27.2")
}

lombok {
    version.set("1.18.24")
}

tasks.delombok {
    // empty format option, otherwise the default is to use pretty formatting which overrides
    // options Lombok config options and does not add generated annotations.
    format = emptyMap()
}

// TODO: apply(from = "../deploy.gradle")

testing {
    suites {
        val liveTest by registering(JvmTestSuite::class) {
            useTestNG()
            targets {
                all {
                    testTask.configure {
                        filter {
                            excludeTestsMatching("com.atlan.model.assets.*")
                            excludeTestsMatching("com.atlan.loop.*")
                        }
                        testLogging.showStandardStreams = true
                    }
                }
            }
            /*dependencies {
                implementation(project())
                implementation("com.aventrix.jnanoid:jnanoid:2.0.0")
                implementation("org.testng:testng:7.8.0")
                implementation("ch.qos.logback:logback-classic:1.4.8")
            }*/
            sources {
                java {
                    setSrcDirs(listOf("src/main/java"))
                }
            }
        }
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true) // coveralls plugin depends on xml format report
        html.required.set(true)
    }
}

coveralls {
    jacocoReportPath = "build/reports/jacoco/test/jacocoTestReport.xml"
}

/*
// TODO: JavaDocs publishing
gitPublish {
    repoUri.set("https://github.com/atlanhq/atlan-java.git")
    branch.set("gh-pages")
    sign.set(false) // disable commit signing

    contents {
        from(jdk.javadoc.internal.tool.resources.javadoc()) {
            into(".")
        }
    }
}
 */

jacoco {
    // test code instrumentation for Java 18
    toolVersion = "0.8.8"
}
