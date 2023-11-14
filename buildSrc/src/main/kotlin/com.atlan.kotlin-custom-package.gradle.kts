/* SPDX-License-Identifier: Apache-2.0 */
val jarPath = "$rootDir/jars"

plugins {
    kotlin("jvm")
    id("com.diffplug.spotless")
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":sdk"))
    implementation(project(":package-toolkit:runtime"))
    implementation(project(":package-toolkit:config"))
    // In your own project, you would use these in place of the 3 dependencies above:
    //implementation("com.atlan:atlan-java:+")
    //implementation("com.atlan:package-toolkit-runtime:+")
    //implementation("com.atlan:package-toolkit-config:+")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    runtimeOnly("org.apache.logging.log4j:log4j-core:2.20.0")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.10")
}

tasks.create<JavaExec>("generatePackageConfig") {
    dependsOn(tasks.build)
    mainClass.set("PackageConfig")
    classpath = sourceSets.main.get().runtimeClasspath
    workingDir = rootDir
}

tasks {
    jar {
        destinationDirectory.set(file(jarPath))
    }
    test {
        useTestNG()
    }
    clean {
        delete(jarPath)
    }
}

kotlin {
    jvmToolchain(17)
}

spotless {
    kotlin {
        licenseHeaderFile("$rootDir/LICENSE_HEADER")
        ktlint()
    }
    kotlinGradle {
        ktlint()
    }
}
