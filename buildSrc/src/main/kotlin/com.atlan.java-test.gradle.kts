/* SPDX-License-Identifier: Apache-2.0 */
import gradle.kotlin.dsl.accessors._ca5747d1b3cbd1b85a6091961ac9b314.testImplementation
import gradle.kotlin.dsl.accessors._ca5747d1b3cbd1b85a6091961ac9b314.testing
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.kotlin.dsl.*

plugins {
    id("jvm-test-suite")
    id("com.adarshr.test-logger")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("com.aventrix.jnanoid:jnanoid:2.0.0")
    testImplementation("org.testng:testng:7.8.0")
    testImplementation("ch.qos.logback:logback-classic:1.4.8")
    testImplementation("com.github.tomakehurst:wiremock:2.27.2")
}

testing {
    suites {
        getting(JvmTestSuite::class) {
            useTestNG()
            targets {
                all {
                    testTask.configure {
                        testLogging.showStandardStreams = true
                    }
                }
            }
            dependencies {
                implementation(project())
                implementation("org.testng:testng:7.8.0")
                implementation("ch.qos.logback:logback-classic:1.4.8")
                implementation("com.github.tomakehurst:wiremock:2.27.2")
            }
            sources {
                java {
                    setSrcDirs(listOf("src/test/java"))
                }
            }
        }
    }
}

/* TODO
tasks.jacocoTestReport {
    reports {
        xml.required.set(true) // coveralls plugin depends on xml format report
        html.required.set(true)
    }
}

coveralls {
    jacocoReportPath = "build/reports/jacoco/test/jacocoTestReport.xml"
}

jacoco {
    // test code instrumentation for Java 18
    toolVersion = "0.8.8"
}
*/
