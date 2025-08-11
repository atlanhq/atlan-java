/* SPDX-License-Identifier: Apache-2.0 */
plugins {
    id("com.atlan.java")
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    id("org.owasp.dependencycheck") version "12.1.3"
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
        }
    }
}

dependencyCheck {
    nvd {
        apiKey = System.getenv("NVD_API_KEY") ?: ""
    }
    failBuildOnCVSS = 7.0F
}

allprojects {
    tasks.withType<JavaCompile> {
        options.isFork = true
        options.forkOptions.jvmArgs = listOf(
            "-Xmx4g",
            "-Xms2g"
        )
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions.freeCompilerArgs.add("-Xannotation-default-target=param-property")
    }
    configurations.all {
        resolutionStrategy {
            // Note: force a safe version of all of these libraries, even if transitive, to avoid potential CVEs
            force(
                libs.parsson,
                libs.json.path,
                libs.json.smart,
                libs.guava,
                libs.commons.compress,
                libs.commons.io,
                libs.jetty.http,
                libs.jetty.server,
                libs.jetty.http2.common,
                libs.jetty.http2.hpack,
                libs.netty.common,
                libs.rhino,
                libs.commons.lang,
                libs.nimbus,
            )
        }
    }
}
