/* SPDX-License-Identifier: Apache-2.0 */
plugins {
    id("jvm-test-suite")
    id("com.adarshr.test-logger")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useTestNG()
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
