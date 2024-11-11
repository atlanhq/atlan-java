// SPDX-License-Identifier: Apache-2.0
plugins {
    id("com.atlan.kotlin-custom-typedef")
}

tasks {
    jar {
        dependsOn("genPklTypedefs")
    }
    assemble {
        dependsOn("genPklTypedefs")
    }
}

pkl {
    evaluators {
        register("genPklTypedefs") {
            sourceModules.add("src/main/resources/MultiDimensionalDataset.pkl")
            sourceModules.add("src/main/resources/DataModel.pkl")
            sourceModules.add("src/main/resources/Application.pkl")
            modulePath.from(file("../../typedef-toolkit/model/src/main/resources"))
            outputFormat.set("json")
            multipleFileOutputDir.set(layout.projectDirectory.dir("build"))
        }
    }
}

tasks {
    getByName("genPklTypedefsGatherImports") {
        dependsOn(":typedef-toolkit:model:genKotlin")
    }
}
