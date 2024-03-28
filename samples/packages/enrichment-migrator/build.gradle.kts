/* SPDX-License-Identifier: Apache-2.0 */
val jarPath = "$rootDir/jars"

plugins {
    id("com.atlan.kotlin-custom-package")
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(project(":samples:packages:asset-export-basic"))
    implementation(project(":samples:packages:asset-import"))
}

pkl {
    evaluators {
        register("genCustomPkg") {
            sourceModules.add("src/main/resources/package.pkl")
            modulePath.from(file("$rootDir/package-toolkit/config/build/resources/main"))
            multipleFileOutputDir.set(layout.projectDirectory)
        }
    }
}

tasks {
    shadowJar {
        isZip64 = true
        destinationDirectory.set(file(jarPath))
        dependencies {
            include(project(":samples:packages:asset-export-basic"))
            include(project(":samples:packages:asset-import"))
        }
        mergeServiceFiles()
    }
    getByName("genCustomPkg") {
        dependsOn(":package-toolkit:config:generateBuildInfo")
        dependsOn(":package-toolkit:config:processResources")
    }
    assemble {
        dependsOn(getByName("genCustomPkg"))
    }
    jar {
        // Override the default jar task so we get the shadowed jar
        // as the only jar output
        actions = listOf()
        doLast { shadowJar }
    }
}
