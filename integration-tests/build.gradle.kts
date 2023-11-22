/* SPDX-License-Identifier: Apache-2.0 */
plugins {
    id("com.atlan.java")
    id("jvm-test-suite")
    id("com.adarshr.test-logger")
}

dependencies {
    implementation(project(":sdk"))
    testImplementation(libs.bundles.java.test)
    testImplementation(libs.bundles.log4j)
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
                    setSrcDirs(listOf("src/test/java"))
                }
            }
        }
    }
}

tasks {
    test {
        onlyIf {
            project.hasProperty("integrationTests")
        }
    }
}
