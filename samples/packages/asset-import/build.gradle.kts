val jarPath = "$rootDir/jars"
val jarFile = "migration-assistant-import-$version.jar"

plugins {
    id("com.atlan.kotlin-custom-package")
    alias(libs.plugins.shadow)
}

dependencies {
    implementation("de.siegmar:fastcsv:2.2.2")
}

tasks {
    shadowJar {
        isZip64 = true
        archiveFileName.set(jarFile)
        destinationDirectory.set(file(jarPath))
        dependencies {
            include(dependency("de.siegmar:fastcsv:.*"))
        }
        mergeServiceFiles()
    }

    jar {
        // Override the default jar task so we get the shadowed jar
        // as the only jar output
        actions = listOf()
        doLast { shadowJar }
    }
}
