// SPDX-License-Identifier: Apache-2.0
plugins {
    id("com.atlan.kotlin-custom-typedef")
}

tasks {
    assemble {
        dependsOn("genPklTypedefs")
    }
}

pkl {
    evaluators {
        register("genPklTypedefs") {
            sourceModules.add("src/main/resources/MultiDimensionalDataset.pkl")
            modulePath.from(file("../../typedef-toolkit/model/src/main/resources"))
            outputFormat.set("json")
            multipleFileOutputDir.set(layout.projectDirectory.dir("build"))
        }
    }
}
