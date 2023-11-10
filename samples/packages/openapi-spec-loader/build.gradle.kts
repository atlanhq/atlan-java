val jarPath = "$rootDir/jars"
val jarFile = "openapi-spec-loader-$version.jar"

plugins {
    id("com.atlan.kotlin-custom-package")
    alias(libs.plugins.shadow)
}

dependencies {
    implementation("io.swagger.parser.v3:swagger-parser:2.1.16")
}

tasks {
    shadowJar {
        isZip64 = true
        archiveFileName.set(jarFile)
        destinationDirectory.set(file(jarPath))
        dependencies {
            include(dependency("io.swagger.parser.v3:swagger-parser:.*"))
            include(dependency("io.swagger.core.v3:swagger-models:.*"))
            include(dependency("io.swagger.core.v3:swagger-core:.*"))
            include(dependency("io.swagger.parser.v3:swagger-parser-core:.*"))
            include(dependency("io.swagger.parser.v3:swagger-parser-v3:.*"))
            include(dependency("io.swagger.core.v3:swagger-annotations:.*"))
            include(dependency("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:.*"))
            include(dependency("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:.*"))
            include(dependency("org.yaml:snakeyaml:.*"))
            include(dependency("org.apache.commons:commons-lang3:.*"))
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
