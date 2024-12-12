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

tasks {
    test {
        filter {
            isFailOnNoMatchingTests = false
        }
        useTestNG {
            maxParallelForks = 4
            options {
                parallel = "classes"
                threadCount = 1
                testLogging.showStandardStreams = true
            }
        }
        onlyIf {
            project.hasProperty("integrationTests")
        }
    }
}
