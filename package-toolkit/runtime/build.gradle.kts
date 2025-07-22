// SPDX-License-Identifier: Apache-2.0
import com.github.jengelman.gradle.plugins.shadow.ShadowStats
import com.github.jengelman.gradle.plugins.shadow.transformers.CacheableTransformer
import com.github.jengelman.gradle.plugins.shadow.transformers.DontIncludeResourceTransformer
import com.github.jengelman.gradle.plugins.shadow.transformers.TransformerContext
import com.github.jengelman.gradle.plugins.shadow.transformers.TransformerContext.Companion.getEntryTimestamp
import org.apache.commons.io.output.CloseShieldOutputStream
import org.apache.logging.log4j.core.config.plugins.processor.PluginCache
import org.apache.logging.log4j.core.config.plugins.processor.PluginProcessor
import org.apache.tools.zip.ZipEntry
import org.apache.tools.zip.ZipOutputStream
import java.net.URL
import java.util.Collections
import java.util.Enumeration

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
    api(libs.pkl.config)
    api(libs.jackson.kotlin)
    api(libs.fastcsv)
    api(libs.bundles.poi)
    api(libs.awssdk.s3)
    api(libs.awssdk.sts)
    api(platform(libs.gcs.bom))
    api(libs.gcs)
    api(libs.azure.identity)
    api(libs.adls)
    implementation(libs.sqlite)
    implementation(libs.simple.java.mail)
    implementation(libs.log4j.core) // This gives us the OOTB-log4j appenders (that we MUST have for pattern-handling)
    implementation(platform(libs.otel.bom))
    implementation(platform(libs.otel.instrumentation.bom))
    implementation(libs.bundles.otel)
    implementation(libs.slf4j)
    // You would not need the dependencies below in reality, they are to simulate a running tenant
    testImplementation(libs.bundles.java.test)
    testImplementation(project(":mocks"))
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

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    shadowJar {
        dependsOn("genPklConnectors")
        isZip64 = true
        archiveBaseName.set(jarName)
        archiveClassifier.set("jar-with-dependencies")
        dependencies {
            include(dependency("org.jetbrains.kotlin:.*:.*"))
            include(dependency("io.github.microutils:kotlin-logging-jvm:.*"))
            include(dependency("org.slf4j:slf4j-api:.*"))
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
            include(dependency("com.nimbusds:oauth2-oidc-sdk:.*"))
            include(dependency("com.nimbusds:content-type:.*"))
            include(dependency("net.minidev:json-smart:.*"))
            include(dependency("net.minidev:accessors-smart:.*"))
            include(dependency("org.ow2.asm:asm:.*"))
            include(dependency("com.nimbusds:lang-tag:.*"))
            include(dependency("com.nimbusds:nimbus-jose-jwt:.*"))
            include(dependency("com.github.stephenc.jcip:jcip-annotations:.*"))
            include(dependency("com.microsoft.azure:msal4j:.*"))
            include(dependency("com.microsoft.azure:msal4j-persistence-extension:.*"))
            include(dependency("net.java.dev.jna:jna:.*"))
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
            // SQLite
            include(dependency("org.xerial:sqlite-jdbc:.*"))
            // OpenTelemetry
            include(dependency("io.opentelemetry.instrumentation:opentelemetry-log4j-appender-2.17:.*"))
            include(dependency("io.opentelemetry.instrumentation:opentelemetry-log4j-context-data-2.17-autoconfigure:.*"))
            include(dependency("io.opentelemetry.instrumentation:opentelemetry-instrumentation-api:.*"))
            include(dependency("io.opentelemetry.instrumentation:opentelemetry-instrumentation-api-incubator:.*"))
            include(dependency("io.opentelemetry:opentelemetry-api:.*"))
            include(dependency("io.opentelemetry:opentelemetry-api-incubator:.*"))
            include(dependency("io.opentelemetry.semconv:opentelemetry-semconv:.*"))
            include(dependency("io.opentelemetry:opentelemetry-sdk:.*"))
            include(dependency("io.opentelemetry:opentelemetry-sdk-common:.*"))
            include(dependency("io.opentelemetry:opentelemetry-sdk-trace:.*"))
            include(dependency("io.opentelemetry:opentelemetry-sdk-metrics:.*"))
            include(dependency("io.opentelemetry:opentelemetry-sdk-logs:.*"))
            include(dependency("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure:.*"))
            include(dependency("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure-spi:.*"))
            include(dependency("io.opentelemetry:opentelemetry-exporter-otlp:.*"))
            include(dependency("io.opentelemetry:opentelemetry-exporter-otlp-common:.*"))
            include(dependency("io.opentelemetry:opentelemetry-exporter-common:.*"))
            include(dependency("io.opentelemetry:opentelemetry-exporter-sender-okhttp:.*"))
            include(dependency("io.opentelemetry:opentelemetry-context:.*"))
            include(dependency("com.squareup.okhttp3:okhttp:.*"))
            include(dependency("com.squareup.okhttp3:okhttp-jvm:.*"))
            include(dependency("com.squareup.okio:okio:.*"))
            include(dependency("com.squareup.okio:okio-jvm:.*"))
        }
        mergeServiceFiles()
        transform(Log4j2PluginsCustomTransformer())
        transform(DontIncludeResourceTransformer::class.java) {
            resource = "LICENSE"
        }
    }

    jar {
        archiveBaseName.set(jarName)
        dependsOn(
            shadowJar,
            "genPklConnectors",
        )
        // Necessary to avoid log4j falling back to a non-performant way to walk the stack
        manifest {
            attributes(Pair("Multi-Release", "true"))
        }
    }

    assemble {
        dependsOn("genPklConnectors")
    }

    getByName("sourcesJar") {
        dependsOn("genPklConnectors")
    }
    getByName("javadocJar") {
        dependsOn("genPklConnectors")
    }
    getByName("test") {
        dependsOn("genPklConnectors")
    }
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

/**
 * Modified from the original, to simplify (and as the original was not working)
 *
 * Modified from [org.apache.logging.log4j.maven.plugins.shade.transformer.Log4j2PluginCacheFileTransformer.java](https://github.com/apache/logging-log4j-transform/blob/main/log4j-transform-maven-shade-plugin-extensions/src/main/java/org/apache/logging/log4j/maven/plugins/shade/transformer/Log4j2PluginCacheFileTransformer.java).
 *
 * @author Christopher Grote
 * @author Paul Nelson Baker
 * @author John Engelman
 */
@CacheableTransformer
open class Log4j2PluginsCustomTransformer : com.github.jengelman.gradle.plugins.shadow.transformers.Transformer {
    private val temporaryFiles = mutableListOf<File>()
    private var stats: ShadowStats? = null

    override fun canTransformResource(element: FileTreeElement): Boolean = PluginProcessor.PLUGIN_CACHE_FILE == element.name

    override fun transform(context: TransformerContext) {
        val temporaryFile = File.createTempFile("Log4j2Plugins", ".dat")
        temporaryFile.deleteOnExit()
        temporaryFiles.add(temporaryFile)
        val fos = temporaryFile.outputStream()
        context.inputStream.use {
            it.copyTo(fos)
        }
        if (stats == null) {
            stats = context.stats
        }
    }

    override fun hasTransformedResource(): Boolean = temporaryFiles.isNotEmpty()

    override fun modifyOutputStream(
        os: ZipOutputStream,
        preserveFileTimestamps: Boolean,
    ) {
        val pluginCache = PluginCache()
        pluginCache.loadCacheFiles(urlEnumeration)
        val entry = ZipEntry(PluginProcessor.PLUGIN_CACHE_FILE)
        entry.time = getEntryTimestamp(preserveFileTimestamps, entry.time)
        os.putNextEntry(entry)
        pluginCache.writeCache(CloseShieldOutputStream.wrap(os))
        temporaryFiles.clear()
    }

    private val urlEnumeration: Enumeration<URL>
        get() {
            val urls = temporaryFiles.map { it.toURI().toURL() }
            return Collections.enumeration(urls)
        }
}
