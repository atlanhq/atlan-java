// SPDX-License-Identifier: Apache-2.0
version = providers.gradleProperty("VERSION_NAME").get()
val jarName = "package-toolkit-runtime"

plugins {
    id("com.atlan.kotlin")
    id("org.pkl-lang")
    alias(libs.plugins.shadow)
    `maven-publish`
    signing
}

dependencies {
    constraints {
        api(libs.commons.compress) {
            because("version 1.25.0 pulled from poi-ooxml has CWE-835 (CVE-2024-25710) and CWE-770 (CVE-2024-26308)")
        }
        api(libs.parsson) {
            because("version 1.0.0 pulled from elasticsearch-java has CWE-20 (CVE-2023-4043)")
        }
    }
    api(libs.pkl.config)
    api(libs.jackson.kotlin)
    api(libs.fastcsv)
    api(libs.bundles.poi)
    api(libs.awssdk.s3)
    api(platform(libs.gcs.bom))
    api(libs.gcs)
    api(libs.azure.identity)
    api(libs.adls)
    implementation(libs.sqlite)
    implementation(libs.simple.java.mail)
    // You would not need the dependencies below in reality, they are to simulate a running tenant
    testImplementation(libs.bundles.java.test)
    testImplementation(project(":mocks"))
}

tasks {
    shadowJar {
        isZip64 = true
        archiveBaseName.set(jarName)
        archiveClassifier.set("jar-with-dependencies")
        dependencies {
            include(dependency("org.jetbrains.kotlin:.*:.*"))
            include(dependency("io.github.microutils:kotlin-logging-jvm:.*"))
            include(dependency("org.apache.logging.log4j:log4j-api:.*"))
            include(dependency("org.apache.logging.log4j:log4j-core:.*"))
            include(dependency("org.apache.logging.log4j:log4j-slf4j2-impl:.*"))
            include(dependency("org.jetbrains.kotlin:kotlin-reflect:.*"))
            // JSON
            include(dependency("com.fasterxml.jackson.module:jackson-module-kotlin:.*"))
            // CSV
            include(dependency("de.siegmar:fastcsv:.*"))
            // XLS(X)
            include(dependency("org.apache.poi:poi:.*"))
            include(dependency("commons-codec:commons-codec:.*"))
            include(dependency("org.apache.commons:commons-collections4:.*"))
            include(dependency("org.apache.commons:commons-math3:.*"))
            include(dependency("commons-io:commons-io:.*"))
            include(dependency("com.zaxxer:SparseBitSet:.*"))
            include(dependency("org.apache.poi:poi-ooxml:.*"))
            include(dependency("org.apache.poi:poi-ooxml-lite:.*"))
            include(dependency("org.apache.xmlbeans:xmlbeans:.*"))
            include(dependency("org.apache.commons:commons-compress:.*"))
            include(dependency("com.github.virtuald:curvesapi:.*"))
            // Email
            include(dependency("org.simplejavamail:simple-java-mail:.*"))
            include(dependency("org.simplejavamail:core-module:.*"))
            include(dependency("com.sun.mail:jakarta.mail:.*"))
            include(dependency("com.sun.activation:jakarta.activation:.*"))
            include(dependency("com.sanctionco.jmail:jmail:.*"))
            include(dependency("com.github.bbottema:jetbrains-runtime-annotations:.*"))
            include(dependency("com.pivovarit:throwing-function:.*"))
            include(dependency("jakarta.mail:jakarta.mail-api:.*"))
            include(dependency("jakarta.activation:jakarta.activation-api:.*"))
            include(dependency("org.eclipse.angus:angus-mail:.*"))
            include(dependency("org.eclipse.angus:angus-core:.*"))
            include(dependency("org.eclipse.angus:angus-activation:.*"))
            include(dependency("org.eclipse.angus:smtp:.*"))
            include(dependency("org.eclipse.angus:logging-mailhandler:.*"))
            // S3
            include(dependency("software.amazon.awssdk:.*:.*"))
            include(dependency("org.reactivestreams:reactive-streams:.*"))
            include(dependency("org.apache.httpcomponents:httpclient:.*"))
            include(dependency("org.apache.httpcomponents:httpcore:.*"))
            include(dependency("commons-logging:commons-logging:.*"))
            include(dependency("commons-codec:commons-codec:.*"))
            // GCS
            include(dependency("com.google.cloud:google-cloud-storage:.*"))
            include(dependency("com.google.guava:guava:.*"))
            include(dependency("com.google.guava:failureaccess:.*"))
            include(dependency("com.google.guava:listenablefuture:.*"))
            include(dependency("com.google.errorprone:error_prone_annotations:.*"))
            include(dependency("com.google.j2objc:j2objc-annotations:.*"))
            include(dependency("com.google.http-client:google-http-client:.*"))
            include(dependency("io.grpc:grpc-context:.*"))
            include(dependency("io.opencensus:opencensus-contrib-http-util:.*"))
            include(dependency("com.google.http-client:google-http-client-jackson2:.*"))
            include(dependency("com.google.http-client:google-http-client-gson:.*"))
            include(dependency("com.google.api-client:google-api-client:.*"))
            include(dependency("commons-codec:commons-codec:.*"))
            include(dependency("com.google.oauth-client:google-oauth-client:.*"))
            include(dependency("com.google.http-client:google-http-client-apache-v2:.*"))
            include(dependency("com.google.apis:google-api-services-storage:.*"))
            include(dependency("com.google.code.gson:gson:.*"))
            include(dependency("com.google.cloud:google-cloud-core:.*"))
            include(dependency("com.google.auto.value:auto-value-annotations:.*"))
            include(dependency("com.google.cloud:google-cloud-core-http:.*"))
            include(dependency("com.google.http-client:google-http-client-appengine:.*"))
            include(dependency("com.google.api:gax-httpjson:.*"))
            include(dependency("com.google.cloud:google-cloud-core-grpc:.*"))
            include(dependency("com.google.api:gax:.*"))
            include(dependency("com.google.api:gax-grpc:.*"))
            include(dependency("io.grpc:grpc-inprocess:.*"))
            include(dependency("io.grpc:grpc-alts:.*"))
            include(dependency("io.grpc:grpc-grpclb:.*"))
            include(dependency("org.conscrypt:conscrypt-openjdk-uber:.*"))
            include(dependency("io.grpc:grpc-auth:.*"))
            include(dependency("com.google.auth:google-auth-library-credentials:.*"))
            include(dependency("com.google.auth:google-auth-library-oauth2-http:.*"))
            include(dependency("com.google.api:api-common:.*"))
            include(dependency("javax.annotation:javax.annotation-api:.*"))
            include(dependency("io.opencensus:opencensus-api:.*"))
            include(dependency("com.google.api.grpc:proto-google-iam-v1:.*"))
            include(dependency("com.google.protobuf:protobuf-java:.*"))
            include(dependency("com.google.protobuf:protobuf-java-util:.*"))
            include(dependency("io.grpc:grpc-core:.*"))
            include(dependency("com.google.android:annotations:.*"))
            include(dependency("org.codehaus.mojo:animal-sniffer-annotations:.*"))
            include(dependency("io.perfmark:perfmark-api:.*"))
            include(dependency("io.grpc:grpc-protobuf:.*"))
            include(dependency("io.grpc:grpc-protobuf-lite:.*"))
            include(dependency("com.google.api.grpc:proto-google-common-protos:.*"))
            include(dependency("org.threeten:threetenbp:.*"))
            include(dependency("com.google.api.grpc:proto-google-cloud-storage-v2:.*"))
            include(dependency("com.google.api.grpc:grpc-google-cloud-storage-v2:.*"))
            include(dependency("com.google.api.grpc:gapic-google-cloud-storage-v2:.*"))
            include(dependency("com.google.code.findbugs:jsr305:.*"))
            include(dependency("io.grpc:grpc-api:.*"))
            include(dependency("io.grpc:grpc-netty-shaded:.*"))
            include(dependency("io.grpc:grpc-util:.*"))
            include(dependency("io.grpc:grpc-stub:.*"))
            include(dependency("io.grpc:grpc-googleapis:.*"))
            include(dependency("org.checkerframework:checker-qual:.*"))
            include(dependency("io.grpc:grpc-xds:.*"))
            include(dependency("io.opencensus:opencensus-proto:.*"))
            include(dependency("io.grpc:grpc-services:.*"))
            include(dependency("com.google.re2j:re2j:.*"))
            include(dependency("io.grpc:grpc-rls:.*"))
            // ADLS
            include(dependency("com.azure:azure-identity:.*"))
            include(dependency("com.microsoft.azure:msal4j:.*"))
            include(dependency("com.microsoft.azure:msal4j-persistence-extension:.*"))
            include(dependency("net.java.dev.jna:jna-platform:.*"))
            include(dependency("com.azure:azure-core-http-netty:.*"))
            include(dependency("com.azure:azure-core:.*"))
            include(dependency("com.azure:azure-json:.*"))
            include(dependency("com.azure:azure-storage-blob:.*"))
            include(dependency("com.azure:azure-storage-common:.*"))
            include(dependency("com.azure:azure-storage-file-datalake:.*"))
            include(dependency("com.azure:azure-storage-internal-avro:.*"))
            include(dependency("com.azure:azure-xml:.*"))
            include(dependency("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:.*"))
            include(dependency("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:.*"))
            include(dependency("com.fasterxml.woodstox:woodstox-core:.*"))
            include(dependency("io.netty:netty-buffer:.*"))
            include(dependency("io.netty:netty-codec-dns:.*"))
            include(dependency("io.netty:netty-codec-http2:.*"))
            include(dependency("io.netty:netty-codec-http:.*"))
            include(dependency("io.netty:netty-codec-socks:.*"))
            include(dependency("io.netty:netty-codec:.*"))
            include(dependency("io.netty:netty-common:.*"))
            include(dependency("io.netty:netty-handler-proxy:.*"))
            include(dependency("io.netty:netty-handler:.*"))
            include(dependency("io.netty:netty-resolver-dns-classes-macos:.*"))
            include(dependency("io.netty:netty-resolver-dns-native-macos:.*"))
            include(dependency("io.netty:netty-resolver-dns:.*"))
            include(dependency("io.netty:netty-resolver:.*"))
            include(dependency("io.netty:netty-tcnative-boringssl-static:.*"))
            include(dependency("io.netty:netty-tcnative-classes:.*"))
            include(dependency("io.netty:netty-transport-classes-epoll:.*"))
            include(dependency("io.netty:netty-transport-classes-kqueue:.*"))
            include(dependency("io.netty:netty-transport-native-epoll:.*"))
            include(dependency("io.netty:netty-transport-native-kqueue:.*"))
            include(dependency("io.netty:netty-transport-native-unix-common:.*"))
            include(dependency("io.netty:netty-transport:.*"))
            include(dependency("io.projectreactor.netty:reactor-netty-core:.*"))
            include(dependency("io.projectreactor.netty:reactor-netty-http:.*"))
            include(dependency("io.projectreactor:reactor-core:.*"))
            include(dependency("org.codehaus.woodstox:stax2-api:.*"))
            include(dependency("org.reactivestreams:reactive-streams:.*"))
        }
        mergeServiceFiles()
    }

    jar {
        archiveBaseName.set(jarName)
        dependsOn(shadowJar)
        // Necessary to avoid log4j falling back to a non-performant way to walk the stack
        manifest {
            attributes(Pair("Multi-Release", "true"))
        }
    }

    assemble {
        dependsOn("genPklConnectors")
    }
}

pkl {
    evaluators {
        register("genPklConnectors") {
            sourceModules.add("src/main/resources/csa-connectors-objectstore.pkl")
            modulePath.from(file("../config/src/main/resources"))
            outputFormat.set("yaml")
            multipleFileOutputDir.set(layout.projectDirectory.dir("build"))
        }
    }
}

tasks.getByName("genPklConnectors") {
    dependsOn(":package-toolkit:config:generateBuildInfo")
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJavaPkgRun") {
            groupId = providers.gradleProperty("GROUP").get()
            artifactId = providers.gradleProperty("PKG_RUN_ARTIFACT_ID").get()
            version = providers.gradleProperty("VERSION_NAME").get()
            from(components["java"])
            pom {
                name.set(providers.gradleProperty("PKG_RUN_ARTIFACT_ID").get())
                description.set(providers.gradleProperty("PKG_RUN_DESCRIPTION").get())
                url.set(providers.gradleProperty("POM_URL").get())
                packaging = providers.gradleProperty("POM_PACKAGING").get()
                licenses {
                    license {
                        name.set(providers.gradleProperty("POM_LICENCE_NAME").get())
                        url.set(providers.gradleProperty("POM_LICENCE_URL").get())
                        distribution.set(providers.gradleProperty("POM_LICENCE_DIST").get())
                    }
                }
                developers {
                    developer {
                        id.set(providers.gradleProperty("POM_DEVELOPER_ID").get())
                        name.set(providers.gradleProperty("POM_DEVELOPER_NAME").get())
                        email.set(providers.gradleProperty("POM_DEVELOPER_EMAIL").get())
                    }
                }
                organization {
                    name.set(providers.gradleProperty("POM_DEVELOPER_NAME").get())
                    url.set(providers.gradleProperty("POM_ORGANIZATION_URL").get())
                }
                scm {
                    connection.set(providers.gradleProperty("POM_SCM_CONNECTION").get())
                    developerConnection.set(providers.gradleProperty("POM_SCM_DEV_CONNECTION").get())
                    url.set(providers.gradleProperty("POM_SCM_URL").get())
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["mavenJavaPkgRun"])
}
