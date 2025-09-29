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
        // Ensure this extension package is scanned for serde (either should work, you don't need both)
        systemProperty("asset.scan.external", "com.probable.guacamole.model.assets")
        environment("ASSET_SCAN_EXTERNAL", "com.probable.guacamole.model.assets")
    }
}
