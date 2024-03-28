plugins {
    id("com.atlan.kotlin-custom-package")
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
    getByName("genCustomPkg") {
        dependsOn(":package-toolkit:config:generateBuildInfo")
        dependsOn(":package-toolkit:config:processResources")
    }
    assemble {
        dependsOn(getByName("genCustomPkg"))
    }
}
