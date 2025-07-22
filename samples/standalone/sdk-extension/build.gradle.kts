// SPDX-License-Identifier: Apache-2.0
plugins {
    id("com.atlan.java")
}

dependencies {
    implementation(project(":sdk"))
    // In your own project, you would use this in place of the 1 dependency above:
    //implementation("com.atlan:atlan-java:+")
    runtimeOnly(libs.bundles.log4j)
    // You would not need the dependencies below in reality, they are to simulate a running tenant
    implementation(libs.bundles.java.test)
    testImplementation(project(":mocks"))
}

tasks {
    test {
        useTestNG {
            options {
                testLogging.showStandardStreams = true
            }
        }
    }
}
