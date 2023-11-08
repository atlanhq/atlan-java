/* SPDX-License-Identifier: Apache-2.0 */
plugins {
    id("com.atlan.java")
    id("com.atlan.java-test")
}

dependencies {
    implementation(project(":sdk"))
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

/*testing {
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
                    setSrcDirs(listOf("src/test/java"))
                }
            }
        }
    }
}*/
